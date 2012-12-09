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
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author crazyproger
 */
public class WebtoperFacetConfiguration implements FacetConfiguration {
    private WebtoperFacet facet;

    @java.lang.Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[]{new FacetEditorTab() {
            @Nls
            @java.lang.Override
            public String getDisplayName() {
                return "Webtoper facet";
            }

            @Nullable
            @java.lang.Override
            public JComponent createComponent() {
                return new JLabel("Webtoper facet config");
            }

            @java.lang.Override
            public boolean isModified() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @java.lang.Override
            public void reset() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @java.lang.Override
            public void disposeUIResources() {

            }
        }};
    }

    @java.lang.Override
    public void readExternal(Element element) throws InvalidDataException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @java.lang.Override
    public void writeExternal(Element element) throws WriteExternalException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFacet(WebtoperFacet facet) {
        this.facet = facet;
    }

    public WebtoperFacet getFacet() {
        return facet;
    }
}
