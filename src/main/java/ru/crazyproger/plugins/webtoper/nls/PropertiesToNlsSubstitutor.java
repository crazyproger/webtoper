package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.lang.Language;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutor;
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
public class PropertiesToNlsSubstitutor extends LanguageSubstitutor {
    @Nullable
    @Override
    public Language getLanguage(@NotNull VirtualFile file, @NotNull Project project) {
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        List<String> folders = config.getFolders();
        for (String folder : folders) {
            VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(folder), true);
            if (virtualFile != null) {
                GlobalSearchScope scope = GlobalSearchScopes.directoryScope(project, virtualFile, true);
                if (scope.contains(file))
                    return NlsLanguage.INSTANCE;
            }
        }
        return null;
    }
}
