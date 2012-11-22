package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsName;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsNameStub;

/**
 * @author crazyproger
 */
public class NlsNameStubImpl extends StubBase<NlsName> implements NlsNameStub {
    public NlsNameStubImpl(StubElement parent) {
        super(parent, NlsElementTypes.NLS_NAME);
    }
}
