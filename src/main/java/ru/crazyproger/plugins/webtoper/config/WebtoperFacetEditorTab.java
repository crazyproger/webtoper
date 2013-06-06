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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.intellij.facet.pointers.FacetPointer;
import com.intellij.facet.pointers.FacetPointersManager;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ui.configuration.FacetsProvider;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.MutableCollectionComboBoxModel;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;
import ru.crazyproger.plugins.webtoper.WebtoperUtil;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebtoperFacetEditorTab extends FacetEditorTab {
    private final WebtoperFacetConfiguration configuration;
    private final FacetEditorContext context;
    private JPanel rootPanel;
    private JComboBox cbParentLayer;
    private TextFieldWithBrowseButton pFacetRoot;
    private JLabel lParentLayer;
    private JLabel lFacetRoot;

    public WebtoperFacetEditorTab(FacetEditorContext editorContext, WebtoperFacetConfiguration configuration) {
        this.configuration = configuration;
        this.context = editorContext;

        lParentLayer.setLabelFor(cbParentLayer);
        lFacetRoot.setLabelFor(pFacetRoot);
        pFacetRoot.getButton().addActionListener(new MyFolderFieldListener(pFacetRoot, configuration.getFacetRoot(), false, null));
    }

    @Nls
    @Override
    public String getDisplayName() {
        return WebtoperBundle.message("facet.settings.displayName");
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return rootPanel;
    }

    private void updateComboModel(MyFacetComboListModel selectedItem) {

        final FacetPointersManager pointersManager = FacetPointersManager.getInstance(context.getProject());

        Collection<WebtoperFacet> facets = getFacets();
        Collection<MyFacetComboListModel> items = Collections2.transform(facets, new Function<WebtoperFacet, MyFacetComboListModel>() {
            @Override
            public MyFacetComboListModel apply(WebtoperFacet webtoperFacet) {
                return new MyFacetComboListModel(pointersManager.create(webtoperFacet), context);
            }
        });

        ArrayList<MyFacetComboListModel> listModels = new ArrayList<MyFacetComboListModel>(items);
        // remove item for current facet - facet can't have itself as a parent
        listModels.remove(new MyFacetComboListModel(pointersManager.create(configuration.getFacet()), context));
        MyFacetComboListModel emptyElement = new MyFacetComboListModel(null, context);
        listModels.add(0, emptyElement);
        MyFacetComboListModel selected = emptyElement;
        if (listModels.contains(selectedItem)) {
            selected = selectedItem;
        }
        MutableCollectionComboBoxModel comboBoxModel = new MutableCollectionComboBoxModel(listModels, selected);
        cbParentLayer.setModel(comboBoxModel);
    }

    private Collection<WebtoperFacet> getFacets() {
        List<WebtoperFacet> result = new ArrayList<WebtoperFacet>();
        Module[] modules = context.getModulesProvider().getModules();
        FacetsProvider facetsProvider = context.getFacetsProvider();
        for (Module module : modules) {
            result.addAll(facetsProvider.getFacetsByType(module, WebtoperFacet.ID));
        }
        return Collections2.filter(result, new WebtoperUtil.ValidFacetPredicate());
    }

    @Override
    public void onTabEntering() {
        super.onTabEntering();
        updateComboModel((MyFacetComboListModel) cbParentLayer.getSelectedItem());
    }

    @Override
    public boolean isModified() {
        MyFacetComboListModel selectedItem = (MyFacetComboListModel) cbParentLayer.getSelectedItem();
        if (((selectedItem == null || selectedItem.pointer == null) && configuration.getParentFacetPointer() != null) ||
                !(selectedItem == null || selectedItem.pointer == null || selectedItem.pointer.equals(configuration.getParentFacetPointer()))) {
            return true;
        }
        VirtualFile facetRoot = configuration.getFacetRoot();
        String rootText = pFacetRoot.getText();
        if ((facetRoot == null && !StringUtil.isEmpty(rootText)) || (facetRoot != null && !facetRoot.getPath().equals(rootText))) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (!isModified()) return;
        String facetRootText = pFacetRoot.getText();
        List<VirtualFile> toReparse = new ArrayList<VirtualFile>(2);
        VirtualFile oldRoot = configuration.getFacetRoot();
        if (oldRoot != null) {
            toReparse.add(0, oldRoot);
            configuration.setFacetRoot(null);
        }
        if (StringUtils.isNotBlank(facetRootText)) {
            VirtualFile facetRoot = LocalFileSystem.getInstance().findFileByIoFile(new File(facetRootText));
            if (facetRoot != null) {
                configuration.setFacetRoot(facetRoot);
                toReparse.add(facetRoot);
            }
        }

        WebtoperUtil.reparseFilesInRoots(context.getProject(), toReparse, PropertiesFileType.DEFAULT_EXTENSION);
        Object selectedItem = cbParentLayer.getSelectedItem();
        if (selectedItem != null) {
            MyFacetComboListModel selected = (MyFacetComboListModel) selectedItem;
            configuration.setParentFacetPointer(selected.pointer);
        }
    }

    @Override
    public void reset() {
        MyFacetComboListModel selectedItem = new MyFacetComboListModel(configuration.getParentFacetPointer(), context);
        updateComboModel(selectedItem);
        VirtualFile facetRoot = configuration.getFacetRoot();
        if (facetRoot != null) {
            pFacetRoot.setText(FileUtil.toSystemDependentName(facetRoot.getPath()));
        } else {
            pFacetRoot.setText("");
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

    private class MyFacetComboListModel {
        private FacetPointer<WebtoperFacet> pointer;
        private FacetEditorContext context;

        private MyFacetComboListModel(FacetPointer<WebtoperFacet> pointer, FacetEditorContext context) {
            this.pointer = pointer;
            this.context = context;
        }

        @Override
        public String toString() {
            if (pointer != null) {
                return pointer.getFacetName(context.getModulesProvider(), context.getFacetsProvider());
            }
            return "";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyFacetComboListModel that = (MyFacetComboListModel) o;

            if (pointer != null) {
                if (!pointer.equals(that.pointer)) {
                    return that.pointer != null && pointer.getId().equals(that.pointer.getId());
                }
            } else {
                if (that.pointer != null) return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return pointer != null ? pointer.hashCode() : 0;
        }
    }
}
