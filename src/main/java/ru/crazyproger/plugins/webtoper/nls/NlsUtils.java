package ru.crazyproger.plugins.webtoper.nls;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.intellij.facet.FacetManager;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacetConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module[] modules = moduleManager.getModules();
        List<VirtualFile> nlsRoots = new ArrayList<VirtualFile>(modules.length);
        for (Module module : modules) {
            FacetManager facetManager = FacetManager.getInstance(module);
            Collection<WebtoperFacet> facets = facetManager.getFacetsByType(WebtoperFacet.ID);
            for (WebtoperFacet facet : facets) {
                WebtoperFacetConfiguration configuration = facet.getConfiguration();
                if (!configuration.getNlsRoots().isEmpty()) {
                    Collection<VirtualFile> filtered = Collections2.filter(configuration.getNlsRoots(), Predicates.notNull());
                    nlsRoots.addAll(filtered);
                }
            }
        }
        return nlsRoots;
    }
}
