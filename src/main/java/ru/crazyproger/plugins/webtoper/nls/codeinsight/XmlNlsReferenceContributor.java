package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlText;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

import java.util.Collection;

/**
 * @author crazyproger
 */
public class XmlNlsReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlTag().withName(NlsCompletionContributor.TAG_NAME), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                Project project = element.getContainingFile().getProject();
                XmlText childOfType = PsiTreeUtil.getChildOfType(element, XmlText.class);
                if (childOfType == null) {
                    return PsiReference.EMPTY_ARRAY;
                }
                String text = StringUtils.trim(childOfType.getText());

                Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, NlsUtils.getNlsScope(project));
                for (VirtualFile virtualFile : files) {
                    PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                    if (file != null) {
                        String fullName = NlsUtils.getNlsName(virtualFile, project);
                        if (StringUtils.equals(text, fullName)) {
                            return new PsiReference[]{new PsiReferenceBase.Immediate<PsiElement>(element, false, file)};
                        }
                    }
                }
                return PsiReference.EMPTY_ARRAY;
            }
        });
    }
}
