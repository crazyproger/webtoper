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

package ru.crazyproger.plugins.webtoper.component.dom;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Config;

public class ConfigFileDescription extends DomFileDescription<Config> {
    private static final String ROOT_TAG_NAME = "config";

    public ConfigFileDescription() {
        super(Config.class, ROOT_TAG_NAME);
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        Module forPsiElement = ModuleUtilCore.findModuleForPsiElement(file);
        if (forPsiElement == null) {
            return false;
        }
        GlobalSearchScope scope = Utils.getWebRootsScope(forPsiElement);
        boolean isInWeb = scope.contains(file.getVirtualFile());
        XmlTag rootTag = file.getRootTag();
        return isInWeb && rootTag != null && ROOT_TAG_NAME.equals(rootTag.getName());
    }
}
