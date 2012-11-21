package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

/**
 * @author crazyproger
 */
public class NlsLexer extends FlexAdapter {
    public NlsLexer() {
        super(new _NlsLexer((Reader) null));
    }
}
