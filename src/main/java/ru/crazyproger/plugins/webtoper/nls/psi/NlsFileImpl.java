package ru.crazyproger.plugins.webtoper.nls.psi;

import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author crazyproger
 */
public class NlsFileImpl extends PropertiesFileImpl {

    public NlsFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public String toString() {
        return "Nls file: " + getName();
    }

    public Collection<NlsNameImpl> getIncludes() {
        return PsiTreeUtil.findChildrenOfType(this, NlsNameImpl.class);
    }

    public Collection<PsiFile> getIncludedFiles() {
        Collection<NlsNameImpl> includes = getIncludes();
        if (includes == null || includes.isEmpty()) {
            return Collections.emptyList();
        }
        List<PsiFile> included = new ArrayList<PsiFile>(includes.size());
        for (NlsNameImpl include : includes) {
            PsiReference reference = include.getReference();
            if (reference == null) {
                continue;
            }
            PsiElement resolved = reference.resolve();
            if (resolved != null) {
                included.add((PsiFile) resolved);
            }
        }
        return included;
    }
}
