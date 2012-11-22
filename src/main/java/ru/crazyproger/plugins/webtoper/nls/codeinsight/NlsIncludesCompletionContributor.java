package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsTokenTypes;

import java.util.Collection;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * @author crazyproger
 */
public class NlsIncludesCompletionContributor extends CompletionContributor {
    public NlsIncludesCompletionContributor() {
        extend(CompletionType.BASIC, psiElement(NlsTokenTypes.NLS_NAME), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                Project project = parameters.getPosition().getContainingFile().getProject();
                PsiFile originalFile = parameters.getOriginalFile();
                GlobalSearchScope nlsScope = NlsUtils.getNlsScope(project);
                if (nlsScope == null) {
                    return;
                }
                // todo check indexes - maybe searching by language index is better or faster(may need create own index)
                Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
                for (VirtualFile virtualFile : files) {
                    PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                    // todo add filter of already added Nls'es
                    if (file != null && !file.equals(originalFile)) {
                        String fullName = NlsUtils.getNlsName(virtualFile, project);
                        LookupElementBuilder element = LookupElementBuilder.create(file, fullName).withIcon(file.getIcon(0));
                        result.addElement(element);
                    }
                }
            }
        });
    }
}
