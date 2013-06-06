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
import com.intellij.facet.FacetType;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class WebtoperFacetType extends FacetType<WebtoperFacet, WebtoperFacetConfiguration> {
    public WebtoperFacetType() {
        super(WebtoperFacet.ID, "webtoper", "Webtoper", WebFacet.ID);
    }

    @Override
    public WebtoperFacetConfiguration createDefaultConfiguration() {
        return new WebtoperFacetConfiguration();
    }

    @Override
    public WebtoperFacet createFacet(@NotNull Module module, String name, @NotNull WebtoperFacetConfiguration configuration, @Nullable Facet underlyingFacet) {
        // DO NOT COMMIT MODULE-ROOT MODELS HERE!
        // modules are not initialized yet, so some data may be lost
        return new WebtoperFacet(module, name, configuration, underlyingFacet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return moduleType instanceof JavaModuleType;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.W16;
    }

    @Override
    public boolean isOnlyOneFacetAllowed() {
        return false;
    }
}
