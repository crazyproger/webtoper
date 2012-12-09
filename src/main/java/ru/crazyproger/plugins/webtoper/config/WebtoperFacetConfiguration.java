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

/**
 * @author crazyproger
 */
public class WebtoperFacetConfiguration implements FacetConfiguration {
    public static final String WEBTOP_ROOT_LAYER = "Webtop";
    public static final String PARENT_LAYER_ATTRIBUTE = "parentLayer";
    public static final String NLS_ROOT_ATTRIBUTE = "nlsRoot";
    private WebtoperFacet facet;
    private VirtualFile nlsRoot;
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

    @java.lang.Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[]{new WebtoperFacetEditorTab(editorContext, this)};
    }

    @java.lang.Override
    public void readExternal(Element element) throws InvalidDataException {
        parentLayer = element.getAttributeValue(PARENT_LAYER_ATTRIBUTE);
        String path = element.getAttributeValue(NLS_ROOT_ATTRIBUTE);
        if (path != null) {
            nlsRoot = LocalFileSystem.getInstance().findFileByPath(path);
        } else {
            nlsRoot = null;
        }
    }

    @java.lang.Override
    public void writeExternal(Element element) throws WriteExternalException {
        element.setAttribute(PARENT_LAYER_ATTRIBUTE, parentLayer);
        String path = nlsRoot == null ? null : toRelativePath(nlsRoot.getPath());
        element.setAttribute(NLS_ROOT_ATTRIBUTE, path);

    }

    public WebtoperFacet getFacet() {
        return facet;
    }

    public void setFacet(WebtoperFacet facet) {
        this.facet = facet;
    }

    public VirtualFile getNlsRoot() {
        return nlsRoot;
    }

    public void setNlsRoot(VirtualFile nlsRoot) {
        this.nlsRoot = nlsRoot;
    }

    public String getParentLayer() {
        return parentLayer;
    }

    public void setParentLayer(String parentLayer) {
        this.parentLayer = parentLayer;
    }

    @Nullable
    private String toRelativePath(String absPath) {
        absPath = FileUtil.toSystemIndependentName(absPath);
        PathMacroManager macroManager = PathMacroManager.getInstance(getFacet().getModule());
        return macroManager.collapsePath(absPath);
    }
}
