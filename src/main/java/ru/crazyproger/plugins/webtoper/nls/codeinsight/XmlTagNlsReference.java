package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.google.common.collect.Collections2;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;

import static ru.crazyproger.plugins.webtoper.nls.codeinsight.NlsCompletionContributor.NlsFile2LookupElementFunction;

/**
 * @author crazyproger
 */
public class XmlTagNlsReference extends AbstractNlsReference<XmlTag> {
    public XmlTagNlsReference(XmlTag element, @Nullable PsiElement resolveTo) {
        super(element, resolveTo);
        mySoft = true;
    }

    @Override
    protected String getElementText() {
        return getElement().getValue().getText();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        final Project project = getElement().getProject();
        Collection<NlsFileImpl> filtered = NlsCompletionContributor.getNlsFiles(project);
        Collection<LookupElement> result = Collections2.transform(filtered, new NlsFile2LookupElementFunction(project));
        return result.toArray(new Object[result.size()]);
    }
}
