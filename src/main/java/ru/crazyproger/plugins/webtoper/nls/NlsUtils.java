package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.config.ProjectConfig;

/**
 * @author crazyproger
 */
public class NlsUtils {
    @Nullable
    public static GlobalSearchScope getNlsScope(@NotNull Project project) {
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        GlobalSearchScope scope = null;
        for (VirtualFile folder : config.getNlsRoots()) {
            GlobalSearchScope folderScope = GlobalSearchScopes.directoryScope(project, folder, true);
            if (scope == null) {
                scope = folderScope;
            } else {
                scope = scope.union(folderScope);
            }
        }
        return scope;
    }

    public static String getNlsName(@NotNull VirtualFile file, @NotNull Project project) {
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        for (VirtualFile folder : config.getNlsRoots()) {
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
}
