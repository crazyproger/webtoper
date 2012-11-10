package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author crazyproger
 */
public class PropertiesToNlsSubstitutor extends LanguageSubstitutor {
    @Nullable
    @Override
    public Language getLanguage(@NotNull VirtualFile file, @NotNull Project project) {
        return NlsLanguage.INSTANCE;
    }
}
