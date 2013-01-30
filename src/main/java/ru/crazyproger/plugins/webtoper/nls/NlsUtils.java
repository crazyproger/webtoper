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

package ru.crazyproger.plugins.webtoper.nls;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacetConfiguration;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.*;

/**
 * @author crazyproger
 */
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

    @Nullable
    public static String getNlsName(@NotNull VirtualFile file, @NotNull Project project) {
        for (VirtualFile folder : getAllNlsRoots(project)) {
            if (folder != null) {
                if (VfsUtil.isAncestor(folder, file, true)) {
                    String relativePath = FileUtil.getRelativePath(folder.getPath(), file.getPath(), '/');
                    assert relativePath != null : "relative path must be";
                    String dottedPath = relativePath.replaceAll("/", ".");
                    return StringUtil.trimEnd(dottedPath, PropertiesFileType.DOT_DEFAULT_EXTENSION);
                }
            }
        }
        return null;
    }

    @Nullable
    public static String[] nlsNameToPathChunks(@NotNull String nlsName) {
        if (StringUtil.isEmpty(nlsName)) {
            return null;
        }
        String[] chunks = nlsName.split("\\.");
        chunks[chunks.length - 1] += PropertiesFileType.DOT_DEFAULT_EXTENSION;
        return chunks;
    }

    @NotNull
    public static List<VirtualFile> getAllNlsRoots(Project project) {
        List<WebtoperFacet> facets = Utils.getWebtoperFacets(project);
        List<VirtualFile> nlsRoots = new ArrayList<VirtualFile>(facets.size());
        for (WebtoperFacet facet : facets) {
            WebtoperFacetConfiguration configuration = facet.getConfiguration();
            if (!configuration.getNlsRoots().isEmpty()) {
                Collection<VirtualFile> filtered = Collections2.filter(configuration.getNlsRoots(), Predicates.notNull());
                nlsRoots.addAll(filtered);
            }
        }
        return nlsRoots;
    }

    /**
     * todo here should be module instead of project, for using layout inheritance
     */
    @NotNull
    public static Set<NlsFileImpl> getNlsFiles(String nlsName, Project project) {
        List<VirtualFile> nlsRoots = getAllNlsRoots(project);
        String[] pathChunks = nlsNameToPathChunks(nlsName);
        if (pathChunks == null) return Collections.emptySet();

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
