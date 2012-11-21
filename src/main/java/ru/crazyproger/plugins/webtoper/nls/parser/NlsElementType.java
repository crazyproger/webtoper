package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;

/**
 * @author crazyproger
 */
public class NlsElementType extends IElementType {
    public NlsElementType(@NonNls String debugName) {
        super(debugName, NlsLanguage.INSTANCE);
    }

    @SuppressWarnings({"HardCodedStringLiteral"})
    public String toString() {
        return "Nls:" + super.toString();
    }
}
