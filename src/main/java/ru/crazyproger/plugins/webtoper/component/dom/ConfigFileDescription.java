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
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.WebtoperUtil;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Config;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;

public class ConfigFileDescription extends DomFileDescription<Config> {
    private static final String ROOT_TAG_NAME = "config";

    public ConfigFileDescription() {
        super(Config.class, ROOT_TAG_NAME);
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) return false;

        WebtoperFacet facet = WebtoperUtil.findFacetForVirtualFile(virtualFile, file.getProject());
        if (facet == null || !facet.isValid()) return false;

        VirtualFile configRoot = facet.getConfigRoot();
        if (configRoot == null) return false;
        VfsUtil.isAncestor(configRoot, virtualFile, false);
        XmlTag rootTag = file.getRootTag();
        return rootTag != null && ROOT_TAG_NAME.equals(rootTag.getName());
    }
}
