package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesList;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesListStub;

/**
 * @author crazyproger
 */
public class NlsIncludesListStubImpl extends StubBase<NlsIncludesList> implements NlsIncludesListStub {
    public NlsIncludesListStubImpl(StubElement parent) {
        super(parent, NlsElementTypes.INCLUDES_LIST);
    }
}
