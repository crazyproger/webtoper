package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author crazyproger
 */
public class NlsCompletionContributor extends CompletionContributor {
    public static final String TAG_NAME = "nlsbundle";

    public NlsCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(XmlTokenType.XML_DATA_CHARACTERS), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                Project project = parameters.getPosition().getContainingFile().getProject();
                XmlTag xmlTag = PsiTreeUtil.getParentOfType(parameters.getPosition(), XmlTag.class);
                if (xmlTag == null || !TAG_NAME.equals(xmlTag.getName())) return;

                // todo check indexes - maybe searching by language index is better or faster(may need create own index)
                Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, NlsUtils.getNlsScope(project));
                for (VirtualFile virtualFile : files) {
                    PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                    if (file != null) {
                        String fullName = NlsUtils.getNlsName(virtualFile, project);
                        LookupElementBuilder element = LookupElementBuilder.create(file, fullName).withIcon(file.getIcon(0));
                        result.addElement(element);
                    }
                }
            }
        });
    }
}
