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
import com.intellij.facet.pointers.FacetPointer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import org.jetbrains.annotations.NotNull;

public class WebtoperFacet extends Facet<WebtoperFacetConfiguration> {

    public static final FacetTypeId<WebtoperFacet> ID = new FacetTypeId<WebtoperFacet>("webtoper");
    public static final String NLS_ROOT_NAME = "strings";
    public static final String CONFIG_ROOT_NAME = "config";

    public WebtoperFacet(@NotNull Module module, @NotNull String name, @NotNull WebtoperFacetConfiguration configuration, Facet underlyingFacet) {
        super(getFacetType(), module, name, configuration, underlyingFacet);
        configuration.setFacet(this);
    }

    public static WebtoperFacetType getFacetType() {
        return (WebtoperFacetType) FacetTypeRegistry.getInstance().findFacetType(ID);
    }

    public WebtoperFacet getParentFacet() {
        FacetPointer<WebtoperFacet> pointer = getConfiguration().getParentFacetPointer();
        return pointer == null ? null : pointer.getFacet();
    }

    /**
     * If facet is not valid - it should not be considered as Webtop layer.
     *
     * @return valid configuration or not
     */
    public boolean isValid() {
        VirtualFile facetRoot = getConfiguration().getFacetRoot();
        return facetRoot != null && facetRoot.exists();
    }

    public VirtualFile getNlsRoot() {
        checkValid();
        return getChildVirtualFile(NLS_ROOT_NAME);
    }

    public VirtualFile getConfigRoot() {
        checkValid();
        return getChildVirtualFile(CONFIG_ROOT_NAME);
    }

    private VirtualFile getChildVirtualFile(String relPath) {
        VirtualFile facetRoot = getConfiguration().getFacetRoot();
        // NPE checked in upper checkValid() call
        //noinspection ConstantConditions
        return facetRoot.findFileByRelativePath(relPath);
    }

    /**
     * @return scope with all NLS roots that can be accessed from this layer
     */
    public GlobalSearchScope getNlsScope() {
        checkValid();
        GlobalSearchScope scope = GlobalSearchScopes.directoryScope(getModule().getProject(), getNlsRoot(), true);
        WebtoperFacet parentFacet = getParentFacet();
        if (parentFacet != null && parentFacet.isValid()) {
            return scope.union(parentFacet.getNlsScope());
        }
        return scope;
    }

    /**
     * @return scope with all config roots that can be accessed from this layer
     */
    public GlobalSearchScope getConfigScope() {
        checkValid();
        GlobalSearchScope scope = GlobalSearchScopes.directoryScope(getModule().getProject(), getConfigRoot(), true);
        WebtoperFacet parentFacet = getParentFacet();
        if (parentFacet != null && parentFacet.isValid()) {
            return scope.union(parentFacet.getConfigScope());
        }
        return scope;
    }

    private void checkValid() {
        if (!isValid()) {
            throw new IllegalStateException("Facet is invalid:" + this);
        }
    }
}
