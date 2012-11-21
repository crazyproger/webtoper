package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.tree.IElementType;

/**
 * @author crazyproger
 */
public interface NlsTokenTypes {
    IElementType INCLUDE_KEYWORD = new NlsElementType("INCLUDE_KEYWORD");
    IElementType NLS_NAME = new NlsElementType("NLS_NAME");
    IElementType NLS_SEPARATOR = new NlsElementType("NLS_SEPARATOR");
}
