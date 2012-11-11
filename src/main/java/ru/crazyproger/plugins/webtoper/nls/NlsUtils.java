package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
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
}
