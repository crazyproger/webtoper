package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.lang.properties.parsing.PropertiesElementTypes;
import com.intellij.psi.stubs.IStubElementType;

/**
 * @author crazyproger
 */
public interface NlsElementTypes extends PropertiesElementTypes {
    IStubElementType INCLUDES_LIST = new NlsIncludesListStubElementType();
    IStubElementType INCLUDE_PROPERTY = new NlsIncludePropertyStubElementType();
    IStubElementType NLS_NAME = new NlsNameElementType();
}
