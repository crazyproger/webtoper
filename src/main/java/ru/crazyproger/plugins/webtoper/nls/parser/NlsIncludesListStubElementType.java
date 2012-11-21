package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.stubs.*;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesList;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesListStub;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludesListImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludesListStubImpl;

import java.io.IOException;

/**
 * @author crazyproger
 */
public class NlsIncludesListStubElementType extends IStubElementType<NlsIncludesListStub, NlsIncludesList> {
    public NlsIncludesListStubElementType() {
        super("NLS_INCLUDES_LIST", NlsLanguage.INSTANCE);
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
    public String getExternalId() {
        return "nls.includeslist";
    }

    @Override
    public NlsIncludesListStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new NlsIncludesListStubImpl(parentStub);
    }

    @Override
    public void serialize(NlsIncludesListStub stub, StubOutputStream dataStream) throws IOException {
    }

    @Override
    public void indexStub(NlsIncludesListStub stub, IndexSink sink) {
    }
}
