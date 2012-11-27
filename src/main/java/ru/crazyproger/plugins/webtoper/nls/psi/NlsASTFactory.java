package ru.crazyproger.plugins.webtoper.nls.psi;

import com.intellij.lang.properties.psi.impl.PropertiesASTFactory;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsTokenTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

/**
 * @author crazyproger
 */
public class NlsASTFactory extends PropertiesASTFactory {

    @Nullable
    @Override
    public LeafElement createLeaf(IElementType type, CharSequence text) {
        if (type == NlsTokenTypes.NLS_NAME) {
            return new NlsNameImpl(type, text);
        }
        return super.createLeaf(type, text);
    }
}
