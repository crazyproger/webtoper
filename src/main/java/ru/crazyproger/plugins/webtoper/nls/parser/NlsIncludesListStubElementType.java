package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesList;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesListStub;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludesListImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludesListStubImpl;

import java.io.IOException;

/**
 * @author crazyproger
 */
public class NlsIncludesListStubElementType extends NlsStubElementType<NlsIncludesListStub, NlsIncludesList> {
    public NlsIncludesListStubElementType() {
        super("NLS_INCLUDES_LIST");
    }

    @Override
    public NlsIncludesList createPsi(@NotNull NlsIncludesListStub stub) {
        return new NlsIncludesListImpl(stub);
    }

    @Override
    public NlsIncludesListStub createStub(@NotNull NlsIncludesList psi, StubElement parentStub) {
        return new NlsIncludesListStubImpl(parentStub);
    }

    @Override
    public NlsIncludesListStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new NlsIncludesListStubImpl(parentStub);
    }

}
