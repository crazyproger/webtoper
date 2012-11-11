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

import java.io.File;
import java.util.List;

/**
 * @author crazyproger
 */
public class NlsUtils {
    @Nullable
    public static GlobalSearchScope getNlsScope(@NotNull Project project) {
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        List<String> folders = config.getFolders();
        GlobalSearchScope scope = null;
        for (String folder : folders) {
            VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(folder), true);
            if (virtualFile != null) {
                GlobalSearchScope folderScope = GlobalSearchScopes.directoryScope(project, virtualFile, true);
                if (scope == null) {
                    scope = folderScope;
                } else {
                    scope = scope.union(folderScope);
                }
            }
        }
        return scope;
    }

    public static String getNlsName(@NotNull VirtualFile file, @NotNull Project project) {
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        List<String> folders = config.getFolders();
        String result = null;
        for (String folder : folders) {
            VirtualFile nlsFolder = VfsUtil.findFileByIoFile(new File(folder), true);
            if (nlsFolder != null) {
                VirtualFile ancestor = VfsUtil.getCommonAncestor(nlsFolder, file);
                if (ancestor != null) {
                    String relativePath = FileUtil.getRelativePath(nlsFolder.getPath(), file.getPath(), File.separatorChar);
                    assert relativePath != null : "relative path must be";
                    String dottedPath = relativePath.replaceAll("/", ".");
                    return StringUtil.trimEnd(dottedPath, PropertiesFileType.DOT_DEFAULT_EXTENSION);
                }
            }
        }
        return result;
    }
}
