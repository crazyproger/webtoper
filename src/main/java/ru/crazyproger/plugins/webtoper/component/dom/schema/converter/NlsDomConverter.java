package ru.crazyproger.plugins.webtoper.component.dom.schema.converter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.codeinsight.NlsCompletionContributor;
import ru.crazyproger.plugins.webtoper.nls.codeinsight.XmlTagNlsReference;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;
import java.util.Set;

/**
 * @author crazyproger
 */
public class NlsDomConverter extends ResolvingConverter<NlsFileImpl> implements CustomReferenceConverter<NlsFileImpl> {
    @Nullable
    @Override
    public NlsFileImpl fromString(@Nullable @NonNls String s, ConvertContext context) {
        Set<NlsFileImpl> files = NlsUtils.getNlsFiles(s, context.getProject());
        return files.isEmpty() ? null : files.iterator().next();  // todo we must show all variants(PsiPolyVarRef)
    }

    @Nullable
    @Override
    public String toString(@Nullable NlsFileImpl propertiesFile, ConvertContext context) {
        assert propertiesFile != null;
        PsiFile psiFile = propertiesFile.getContainingFile();
        if (NlsLanguage.INSTANCE.equals(psiFile.getLanguage())) {
            VirtualFile virtualFile = psiFile.getVirtualFile();
            assert virtualFile != null;
            return NlsUtils.getNlsName(virtualFile, context.getProject());
        }
        return null;
    }

    @Nullable
    @Override
    public String getErrorMessage(@Nullable String s, ConvertContext context) {
        return super.getErrorMessage(s, context);
    }

    @NotNull
    @Override
    public Collection<? extends NlsFileImpl> getVariants(ConvertContext context) {
        final Project project = context.getProject();
        return NlsCompletionContributor.getNlsFiles(project);
    }

    @NotNull
    @Override
    public PsiReference[] createReferences(GenericDomValue<NlsFileImpl> value, PsiElement element, ConvertContext context) {
        if (value.getValue() != null) {
            return new PsiReference[]{new XmlTagNlsReference((XmlTag) element, value.getValue())};
        }
        return new PsiReference[]{new XmlTagNlsReference((XmlTag) element, null)};
    }
}
