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
import com.intellij.facet.pointers.FacetPointer;
import com.intellij.facet.pointers.FacetPointersManager;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class WebtoperFacetConfiguration implements FacetConfiguration, PersistentStateComponent<WebtoperFacetConfiguration.State> {

    private WebtoperFacet facet;

    private VirtualFile facetRoot;
    private FacetPointer<WebtoperFacet> parentFacetPointer;

    private State myState = new State();

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

    public WebtoperFacet getFacet() {
        return facet;
    }

    public void setFacet(WebtoperFacet facet) {
        this.facet = facet;
    }

    @Nullable
    public VirtualFile getFacetRoot() {
        if (facetRoot == null) {
            Module module = getFacet().getModule();
            if (myState.root != null) {
                PathMacroManager macroManager = PathMacroManager.getInstance(module);
                facetRoot = VirtualFileManager.getInstance().findFileByUrl(macroManager.expandPath(myState.root));
            }
        }
        return facetRoot;
    }

    public void setFacetRoot(VirtualFile facetRoot) {
        this.facetRoot = facetRoot;
        myState.root = null;
        if (facetRoot != null) {
            PathMacroManager macroManager = PathMacroManager.getInstance(getFacet().getModule());
            myState.root = macroManager.collapsePath(facetRoot.getUrl());
        }
    }

    public FacetPointer<WebtoperFacet> getParentFacetPointer() {
        if (parentFacetPointer == null) {
            if (myState.parentFacetId != null) {
                parentFacetPointer = FacetPointersManager.getInstance(getFacet().getModule().getProject()).create(myState.parentFacetId);
            }
        }
        return parentFacetPointer;
    }

    public void setParentFacetPointer(FacetPointer<WebtoperFacet> parentFacetPointer) {
        this.parentFacetPointer = parentFacetPointer;
        myState.parentFacetId = null;
        if (parentFacetPointer != null) {
            WebtoperFacet parentFacet = parentFacetPointer.getFacet();
            if (parentFacet != null) {
                myState.parentFacetId = FacetPointersManager.constructId(parentFacet);
            }
        }
    }

    @Nullable
    @Override
    public State getState() {
        // should be updated here because of updating in setParentFacetPointer can cause creation of invalid id
        // (when module or facet renamed)
        if (parentFacetPointer != null) {
            WebtoperFacet parentFacet = parentFacetPointer.getFacet();
            if (parentFacet != null) {
                myState.parentFacetId = FacetPointersManager.constructId(parentFacet);
            }
        }
        return new State(myState);
    }

    @Override
    public void loadState(State state) {
        myState = state;
        facetRoot = null;
        parentFacetPointer = null;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
    }

    public static class State {
        public State() {
        }

        public State(State state) {
            this.root = state.root;
            this.parentFacetId = state.parentFacetId;
        }

        public String root;
        public String parentFacetId;
    }
}
