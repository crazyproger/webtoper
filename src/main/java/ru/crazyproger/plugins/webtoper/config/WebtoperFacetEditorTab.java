/*
 * Copyright 2013 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.crazyproger.plugins.webtoper.config;

import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

/**
 * @author crazyproger
 */
public class WebtoperFacetEditorTab extends FacetEditorTab {
    private final WebtoperFacetConfiguration configuration;
    private final FacetEditorContext context;
    private JPanel rootPanel;
    private JComboBox cbParentLayer;
    private TextFieldWithBrowseButton pNlsRoot;
    private JLabel lParentLayer;
    private JLabel lNlsRoot;

    public WebtoperFacetEditorTab(FacetEditorContext editorContext, WebtoperFacetConfiguration configuration) {
        this.configuration = configuration;
        this.context = editorContext;

        lParentLayer.setLabelFor(cbParentLayer);
        lNlsRoot.setLabelFor(pNlsRoot);
        pNlsRoot.getButton().addActionListener(new MyFolderFieldListener(pNlsRoot, configuration.getNlsRoot(), false, null));
        cbParentLayer.addItem(WebtoperFacetConfiguration.WEBTOP_ROOT_LAYER);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Webtoper layer settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        Object selectedItem = cbParentLayer.getSelectedItem();
        if ((selectedItem == null && configuration.getParentLayer() != null) || !(selectedItem == null || selectedItem.equals(configuration.getParentLayer()))) {
            return true;
        }
        VirtualFile nlsRoot = configuration.getNlsRoot();
        String rootText = pNlsRoot.getText();
        if ((nlsRoot == null && !StringUtil.isEmpty(rootText)) || (nlsRoot != null && checkRelativePath(nlsRoot.getPath(), rootText))) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (!isModified()) return;
        VirtualFile nlsRoot = LocalFileSystem.getInstance().findFileByIoFile(new File(pNlsRoot.getText()));
        VirtualFile oldRoot = configuration.getNlsRoot();
        configuration.setNlsRoot(nlsRoot);
        configuration.setParentLayer((String) cbParentLayer.getSelectedItem());
        if (nlsRoot != null) {
            if (oldRoot != null) {
                Utils.reparseFilesInRoots(context.getProject(), Arrays.asList(oldRoot, nlsRoot), PropertiesFileType.DEFAULT_EXTENSION);
            } else {
                Utils.reparseFilesInRoot(context.getProject(), nlsRoot, PropertiesFileType.DEFAULT_EXTENSION);
            }
        }
    }

    @Override
    public void reset() {
        String parentLayer = configuration.getParentLayer();
        if (parentLayer == null) {
            cbParentLayer.setSelectedIndex(-1);
        } else {
            cbParentLayer.setSelectedItem(parentLayer);
        }
        VirtualFile nlsRoot = configuration.getNlsRoot();
        if (nlsRoot != null) {
            pNlsRoot.setText(FileUtil.toSystemDependentName(nlsRoot.getPath()));
        } else {
            pNlsRoot.setText("");
        }
    }

    @Override
    public void disposeUIResources() {
    }

    private VirtualFile[] chooserDirsUnderModule(@Nullable VirtualFile initialFile,
                                                 final boolean chooseFile,
                                                 boolean chooseMultiple,
                                                 @Nullable final Condition<VirtualFile> filter) {
        if (initialFile == null) {
            initialFile = context.getModule().getModuleFile();
        }
        if (initialFile == null) {
            String p = WebtoperFacetConfiguration.getModuleDirPath(context.getModule());
            if (p != null) {
                initialFile = LocalFileSystem.getInstance().findFileByPath(p);
            }
        }
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(chooseFile, !chooseFile, false, false, false, chooseMultiple) {
            @Override
            public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
                if (!super.isFileVisible(file, showHiddenFiles)) {
                    return false;
                }

                if (!file.isDirectory() && !chooseFile) {
                    return false;
                }

                return filter == null || filter.value(file);
            }
        };
        return FileChooser.chooseFiles(descriptor, rootPanel, context.getProject(), initialFile);
    }

    private boolean checkRelativePath(String relativePathFromConfig, String absPathFromTextField) {
        String pathFromConfig = relativePathFromConfig;
        if (pathFromConfig != null && pathFromConfig.length() > 0) {
            pathFromConfig = toAbsolutePath(pathFromConfig);
        }
        String pathFromTextField = absPathFromTextField.trim();
        return !FileUtil.pathsEqual(pathFromConfig, pathFromTextField);
    }

    @Nullable
    private String toAbsolutePath(String genRelativePath) {
        if (genRelativePath == null) {
            return null;
        }
        if (genRelativePath.length() == 0) {
            return "";
        }
        String moduleDirPath = WebtoperFacetConfiguration.getModuleDirPath(context.getModule());
        if (moduleDirPath == null) return null;
        final String path = PathUtil.getCanonicalPath(new File(moduleDirPath, genRelativePath).getPath());
        return path != null ? PathUtil.getLocalPath(path) : null;
    }

    private class MyFolderFieldListener implements ActionListener {
        private final TextFieldWithBrowseButton myTextField;
        private final VirtualFile myDefaultDir;
        private final boolean myChooseFile;
        private final Condition<VirtualFile> myFilter;

        public MyFolderFieldListener(TextFieldWithBrowseButton textField,
                                     VirtualFile defaultDir,
                                     boolean chooseFile,
                                     @Nullable Condition<VirtualFile> filter) {
            myTextField = textField;
            myDefaultDir = defaultDir;
            myChooseFile = chooseFile;
            myFilter = filter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            VirtualFile initialFile = null;
            String path = myTextField.getText().trim();
            if (path.length() == 0) {
                VirtualFile dir = myDefaultDir;
                path = dir != null ? dir.getPath() : null;
            }
            if (path != null) {
                initialFile = LocalFileSystem.getInstance().findFileByPath(path);
            }
            VirtualFile[] files = chooserDirsUnderModule(initialFile, myChooseFile, false, myFilter);
            if (files.length > 0) {
                assert files.length == 1;
                myTextField.setText(FileUtil.toSystemDependentName(files[0].getPath()));
            }
        }

    }
}
