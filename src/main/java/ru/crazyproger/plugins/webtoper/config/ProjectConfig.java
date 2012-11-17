/*
 * Copyright 2012 Vladimir Rudev
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

import com.google.common.base.Joiner;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: crazyproger
 */
@State(
        name = ProjectConfig.ROOT_ENTRY,
        storages = {@Storage(id = "default", file = "$PROJECT_FILE$"), @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/other.xml",
                scheme = StorageScheme.DIRECTORY_BASED)})
public class ProjectConfig
        implements PersistentStateComponent<Element> {

    public static final String ROOT_ENTRY = "WebtoperProjectConfiguration";
    private final Logger log = Logger.getInstance(this.getClass().getCanonicalName());
    private final Project project;
    private final PathMacroManager macroManager;
    private List<String> folders = new ArrayList<String>();
    private VirtualFile[] nlsRoots = null;

    public ProjectConfig(Project project) {
        this.project = project;
        macroManager = PathMacroManager.getInstance(this.project);
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
        updateNlsRoots();
    }

    @Override
    public Element getState() {
        Element configurationsElement = new Element(ROOT_ENTRY);
        for (String entry : folders) {
            Element element = new Element("folder");
            element.setAttribute("folder", macroManager.collapsePath(entry));
            configurationsElement.addContent(element);
        }
        return configurationsElement;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadState(Element state) {
        if (state.getChildren().isEmpty()) {
            log.debug("no config element");
            return;
        }
        List<String> folders = new ArrayList<String>();
        List<Element> foldersChildren = state.getChildren("folder");
        for (Element entry : foldersChildren) {
            String folder = macroManager.expandPath(entry.getAttributeValue("folder"));
            folders.add(folder);
        }
        this.folders = folders;
        updateNlsRoots();
    }

    public String getFoldersAsString() {
        Joiner joiner = Joiner.on("; ").skipNulls();
        return joiner.join(folders);
    }

    public void setFoldersAsString(final String foldersAsString) {
        String[] split = foldersAsString.split(";");
        folders.clear();
        Collections.addAll(folders, split);
        updateNlsRoots();
    }

    public VirtualFile[] getNlsRoots() {
        if (nlsRoots == null) {
            updateNlsRoots();
        }
        return nlsRoots;
    }

    public void setNlsRoots(VirtualFile[] nlsRoots) {
        this.nlsRoots = nlsRoots;
    }

    private void updateNlsRoots() {
        List<VirtualFile> list = new ArrayList<VirtualFile>();
        for (String folder : folders) {
            VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(folder), true);
            if (virtualFile != null) {
                list.add(virtualFile);
            }
        }
        nlsRoots = list.toArray(new VirtualFile[list.size()]);
    }
}
