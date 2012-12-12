package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author crazyproger
 */
public class WholeElementTextRefProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        Project project = element.getContainingFile().getProject();
        List<VirtualFile> nlsRoots = NlsUtils.getAllNlsRoots(project);
        if (nlsRoots.isEmpty()) return PsiReference.EMPTY_ARRAY;

        String text = StringUtils.trim(element.getText());

        String[] pathChunks = NlsUtils.nlsNameToPathChunks(text);
        if (pathChunks == null) return PsiReference.EMPTY_ARRAY;
        Set<PsiReference> references = new HashSet<PsiReference>();
        for (VirtualFile nlsRoot : nlsRoots) {
            VirtualFile relativeFile = VfsUtil.findRelativeFile(nlsRoot, pathChunks);
            if (relativeFile != null && !relativeFile.isDirectory()) {
                PsiFile file = PsiManager.getInstance(project).findFile(relativeFile);
                references.add(new NlsReference(element, (NlsFileImpl) file));
            }
        }
        if (references.isEmpty()) {
            return PsiReference.EMPTY_ARRAY;
        }
        return references.toArray(new PsiReference[references.size()]);
    }
}
