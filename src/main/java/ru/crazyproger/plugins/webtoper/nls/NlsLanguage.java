package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.lang.Language;
import com.intellij.lang.properties.PropertiesHighlighter;
import com.intellij.lang.properties.PropertiesLanguage;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author crazyproger
 */
public class NlsLanguage extends Language {
    public static final NlsLanguage INSTANCE = new NlsLanguage();

    public NlsLanguage() {
        super(PropertiesLanguage.INSTANCE, "Nls", "text/properties");
        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
            @NotNull
            protected SyntaxHighlighter createHighlighter() {
                return new PropertiesHighlighter();
            }
        });
    }
}