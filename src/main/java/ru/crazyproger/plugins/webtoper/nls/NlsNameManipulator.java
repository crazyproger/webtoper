package ru.crazyproger.plugins.webtoper.nls;

import com.google.common.base.Joiner;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.StringUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

/**
 * @author crazyproger
 */
public class NlsNameManipulator extends AbstractElementManipulator<NlsNameImpl> {
    @Override
    public NlsNameImpl handleContentChange(NlsNameImpl element, TextRange range, String newContent)
            throws IncorrectOperationException {
        final String oldText = element.getText();
        final String[] chunks = NlsUtils.nlsNameToPathChunks(oldText);
        String newFileName = StringUtils.substringBeforeLast(newContent, ".");
        assert chunks != null;
        chunks[chunks.length - 1] = newFileName;
        final String newNlsName = Joiner.on(".").join(chunks);
        String newText = oldText.substring(0, range.getStartOffset()) + newNlsName + oldText.substring(range.getEndOffset());
        return (NlsNameImpl) element.replaceWithText(newText);
    }
}
