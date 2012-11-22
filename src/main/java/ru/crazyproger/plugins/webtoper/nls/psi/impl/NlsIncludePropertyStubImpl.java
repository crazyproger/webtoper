package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludeProperty;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludePropertyStub;

/**
 * @author crazyproger
 */
public class NlsIncludePropertyStubImpl extends StubBase<NlsIncludeProperty> implements NlsIncludePropertyStub {
    public NlsIncludePropertyStubImpl(StubElement parent) {
        super(parent, NlsElementTypes.INCLUDE_PROPERTY);
    }
}
