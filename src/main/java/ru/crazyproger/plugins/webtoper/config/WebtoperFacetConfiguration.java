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

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebtoperFacetConfiguration implements FacetConfiguration {
    public static final String WEBTOP_ROOT_LAYER = "Webtop";
    public static final String PARENT_LAYER_ATTRIBUTE = "parentLayer";
    public static final String NLS_ROOT_TAG = "nlsRoot";
    public static final String NLS_ROOT_FOLDER_ATTRIBUTE = "folder";
    private final List<VirtualFile> nlsRoots = new ArrayList<VirtualFile>();
    private WebtoperFacet facet;
    private String parentLayer;

    @Nullable
    public static String getModuleDirPath(Module module) {
        String moduleFilePath = module.getModuleFilePath();
        String moduleDirPath = new File(moduleFilePath).getParent();
        if (moduleDirPath != null) {
            moduleDirPath = FileUtil.toSystemIndependentName(moduleDirPath);
        }
        return moduleDirPath;
    }

    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[]{new WebtoperFacetEditorTab(editorContext, this)};
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(Element element) throws InvalidDataException {
        parentLayer = element.getAttributeValue(PARENT_LAYER_ATTRIBUTE);
        nlsRoots.clear();
        List<Element> children = element.getChildren(NLS_ROOT_TAG);
        for (Element child : children) {
            String path = child.getAttributeValue(NLS_ROOT_FOLDER_ATTRIBUTE);
            if (path != null) {
                nlsRoots.add(LocalFileSystem.getInstance().findFileByPath(path));
            }
        }
    }

    @java.lang.Override
    public void writeExternal(Element element) throws WriteExternalException {
        if (parentLayer != null) {
            element.setAttribute(PARENT_LAYER_ATTRIBUTE, parentLayer);
        }
        PathMacroManager macroManager = PathMacroManager.getInstance(getFacet().getModule());
        for (VirtualFile nlsRoot : nlsRoots) {
            Element folder = new Element(NLS_ROOT_TAG);
            folder.setAttribute(NLS_ROOT_FOLDER_ATTRIBUTE, nlsRoot.getPath());
            element.addContent(folder);
        }
        macroManager.collapsePaths(element);
    }

    public WebtoperFacet getFacet() {
        return facet;
    }

    public void setFacet(WebtoperFacet facet) {
        this.facet = facet;
    }

    public VirtualFile getNlsRoot() {
        return nlsRoots.isEmpty() ? null : nlsRoots.get(0);
    }

    public void setNlsRoot(VirtualFile nlsRoot) {
        nlsRoots.clear();
        nlsRoots.add(nlsRoot);
    }

    public void setNlsRoots(List<VirtualFile> nlsRoots) {
        this.nlsRoots.clear();
        this.nlsRoots.addAll(nlsRoots);
    }

    public List<VirtualFile> getNlsRoots() {
        return nlsRoots;
    }

    public String getParentLayer() {
        return parentLayer;
    }

    public void setParentLayer(String parentLayer) {
        this.parentLayer = parentLayer;
    }
}
