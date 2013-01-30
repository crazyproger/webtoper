package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.google.common.base.Joiner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

/**
 * @author crazyproger
 */
public class XmlTagNlsReference extends AbstractNlsReference<XmlTag> {
    public XmlTagNlsReference(XmlTag element, @Nullable PsiElement resolveTo) {
        super(element, resolveTo);
        mySoft = true;
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        final String oldText = myElement.getValue().getText();
        final String[] chunks = NlsUtils.nlsNameToPathChunks(oldText);
        String newFileName = StringUtils.substringBeforeLast(newElementName, ".");
        assert chunks != null;
        chunks[chunks.length - 1] = newFileName;
        final String newNlsName = Joiner.on(".").join(chunks);
        return getManipulator().handleContentChange(myElement, getRangeInElement(), newNlsName);
    }
}
