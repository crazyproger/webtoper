package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;

/**
 * @author crazyproger
 */
public class WholeElementTextRefProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        Project project = element.getContainingFile().getProject();
        String text = StringUtils.trim(element.getText());

        GlobalSearchScope nlsScope = NlsUtils.getNlsScope(project);
        if (nlsScope == null) {
            return PsiReference.EMPTY_ARRAY;
        }
        Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
        for (VirtualFile virtualFile : files) {
            PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
            if (file != null) {
                String fullName = NlsUtils.getNlsName(virtualFile, project);
                if (StringUtils.equals(text, fullName)) {
                    return new PsiReference[]{new NlsReference(element, (NlsFileImpl) file)};
                }
            }
        }
        return PsiReference.EMPTY_ARRAY;
    }
}
