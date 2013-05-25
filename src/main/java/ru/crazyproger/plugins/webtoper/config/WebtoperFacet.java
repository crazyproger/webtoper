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

import com.intellij.facet.Facet;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class WebtoperFacet extends Facet<WebtoperFacetConfiguration> {

    public static final FacetTypeId<WebtoperFacet> ID = new FacetTypeId<WebtoperFacet>("webtoper");
    public static final String NLS_ROOT_NAME = "strings";

    public WebtoperFacet(@NotNull Module module, @NotNull String name, @NotNull WebtoperFacetConfiguration configuration, Facet underlyingFacet) {
        super(getFacetType(), module, name, configuration, underlyingFacet);
        configuration.setFacet(this);
    }

    public static WebtoperFacetType getFacetType() {
        return (WebtoperFacetType) FacetTypeRegistry.getInstance().findFacetType(ID);
    }


    /**
     * If facet is not valid - it should not be considered as Webtop layer.
     *
     * @return valid configuration or not
     */
    public boolean isValid() {
        VirtualFile facetRoot = getConfiguration().getFacetRoot();
        return facetRoot != null;
    }

    public VirtualFile getNlsRoot() {
        if (!isValid()) {
            throw new IllegalStateException("Facet is invalid:" + this);
        }
        VirtualFile facetRoot = getConfiguration().getFacetRoot();
        // NPE checked in upper isValid() call
        //noinspection ConstantConditions
        return facetRoot.findFileByRelativePath(NLS_ROOT_NAME);
    }
}
