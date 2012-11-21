package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.properties.parsing.Parsing;
import com.intellij.lang.properties.parsing.PropertiesElementTypes;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author crazyproger
 */
public class NlsParser implements PsiParser {
    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        final PsiBuilder.Marker rootMarker = builder.mark();
        final PsiBuilder.Marker propertiesList = builder.mark();
        while (!builder.eof()) {
            if (builder.getTokenType() == NlsTokenTypes.INCLUDE_KEYWORD) {
                parseInclude(builder);
            } else {
                Parsing.parseProperty(builder);
            }
        }
        propertiesList.done(PropertiesElementTypes.PROPERTIES_LIST);
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    private void parseInclude(PsiBuilder builder) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
