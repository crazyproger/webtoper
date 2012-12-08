package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

/**
 * @author crazyproger
 */
public class NlsNameManipulator extends AbstractElementManipulator<NlsNameImpl> {
    @Override
    public NlsNameImpl handleContentChange(NlsNameImpl element, TextRange range, String newContent)
            throws IncorrectOperationException {
        final String oldText = element.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());
        return (NlsNameImpl) element.replaceWithText(newText);
    }
}
