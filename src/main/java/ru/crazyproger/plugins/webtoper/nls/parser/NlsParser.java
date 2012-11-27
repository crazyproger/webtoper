package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.properties.parsing.Parsing;
import com.intellij.lang.properties.parsing.PropertiesElementTypes;
import com.intellij.lang.properties.parsing.PropertiesTokenTypes;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;

/**
 * @author crazyproger
 */
public class NlsParser implements PsiParser {
    private static final Logger LOG = Logger.getInstance("#" + NlsParser.class.getName());

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
        LOG.assertTrue(builder.getTokenType() == NlsTokenTypes.INCLUDE_KEYWORD);
        final PsiBuilder.Marker includePropertyMarker = builder.mark();
        builder.advanceLexer();
        if (builder.getTokenType() != PropertiesTokenTypes.KEY_VALUE_SEPARATOR) {
            error(builder, "nls.separator.expected.parsing.error.message");
        } else {
            builder.advanceLexer();
            final PsiBuilder.Marker includesListMarker = builder.mark();
            if (builder.getTokenType() == NlsTokenTypes.NLS_NAME) {
                parseNlsName(builder);
                while (builder.getTokenType() == NlsTokenTypes.NLS_SEPARATOR) {
                    builder.advanceLexer();
                    if (builder.getTokenType() == NlsTokenTypes.NLS_NAME) {
                        parseNlsName(builder);
                    } else {
                        error(builder, "nls.name.expected.parsing.error.message");
                    }
                }
            } else {
                error(builder, "nls.name.expected.parsing.error.message");
            }
            includesListMarker.done(NlsElementTypes.INCLUDES_LIST);
        }
        includePropertyMarker.done(NlsElementTypes.INCLUDE_PROPERTY);
    }

    private void error(PsiBuilder builder, String errorKey) {
        builder.advanceLexer();
        builder.error(WebtoperBundle.message(errorKey));
    }

    private void parseNlsName(PsiBuilder builder) {
        builder.advanceLexer();
    }
}
