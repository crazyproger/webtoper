package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutor;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author crazyproger
 */
public class PropertiesToNlsSubstitutor extends LanguageSubstitutor {
    @Nullable
    @Override
    public Language getLanguage(@NotNull VirtualFile file, @NotNull Project project) {
        GlobalSearchScope nlsScope = NlsUtils.getNlsScope(project);
        if (nlsScope != null && nlsScope.contains(file)) {
            return NlsLanguage.INSTANCE;
        }
        return null;
    }
}
