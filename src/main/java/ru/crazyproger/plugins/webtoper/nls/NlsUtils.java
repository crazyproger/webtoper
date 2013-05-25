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

package ru.crazyproger.plugins.webtoper.nls;

import com.google.common.collect.Sets;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class NlsUtils {
    @Nullable
    public static GlobalSearchScope getNlsScope(@NotNull Project project) {
        GlobalSearchScope scope = null;
        for (VirtualFile folder : getAllNlsRoots(project)) {
            GlobalSearchScope folderScope = GlobalSearchScopes.directoryScope(project, folder, true);
            if (scope == null) {
                scope = folderScope;
            } else {
                scope = scope.union(folderScope);
            }
        }
        return scope;
    }

    @NotNull
    public static String[] nlsNameToPathChunks(@NotNull String nlsName) {
        String[] chunks = nlsName.trim().split("\\.");
        chunks[chunks.length - 1] += PropertiesFileType.DOT_DEFAULT_EXTENSION;
        return chunks;
    }

    @NotNull
    public static String nlsNameToPath(@NotNull String name, @NotNull String separator) {
        return name.replaceAll("\\.", separator) + PropertiesFileType.DOT_DEFAULT_EXTENSION;
    }

    @NotNull
    public static VirtualFile[] getAllNlsRoots(Project project) {
        List<WebtoperFacet> facets = Utils.getWebtoperFacets(project);
        return getNlsRoots(facets);
    }

    @NotNull
    private static VirtualFile[] getNlsRoots(Collection<WebtoperFacet> facets) {
        List<VirtualFile> nlsRoots = new ArrayList<VirtualFile>(facets.size());
        for (WebtoperFacet facet : facets) {
            if (!facet.isValid()) continue;
            VirtualFile nlsRoot = facet.getNlsRoot();
            // actually, by specification layer always contain nls root folder,
            // but what if programmer not yet create this folder
            if (nlsRoot != null && nlsRoot.isValid()) {
                nlsRoots.add(nlsRoot);
            }
        }
        return nlsRoots.toArray(new VirtualFile[nlsRoots.size()]);
    }

    @NotNull
    public static VirtualFile[] getNlsRoots(Module module) {
        Collection<WebtoperFacet> facets = Utils.getWebtoperFacets(module);
        return getNlsRoots(facets);
    }

    @NotNull
    public static Set<NlsFileImpl> getNlsFiles(String nlsName, Module module) {
        Project project = module.getProject();
        VirtualFile[] nlsRoots = getAllNlsRoots(project);
        String[] pathChunks = nlsNameToPathChunks(nlsName);
        if (ArrayUtils.isEmpty(pathChunks)) return Collections.emptySet();

        Set<NlsFileImpl> nlsFiles = Sets.newHashSet();
        for (VirtualFile nlsRoot : nlsRoots) {
            VirtualFile relativeFile = VfsUtil.findRelativeFile(nlsRoot, pathChunks);
            if (relativeFile != null && !relativeFile.isDirectory()) {
                PsiFile file = PsiManager.getInstance(project).findFile(relativeFile);
                nlsFiles.add((NlsFileImpl) file);
            }
        }
        return nlsFiles;
    }
}
