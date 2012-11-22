package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.stubs.*;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludeProperty;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludePropertyStub;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludePropertyImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludePropertyStubImpl;

import java.io.IOException;

/**
 * @author crazyproger
 */
public class NlsIncludePropertyStubElementType extends IStubElementType<NlsIncludePropertyStub, NlsIncludeProperty> {
    public NlsIncludePropertyStubElementType() {
        super("NLS_INCLUDE_PROPERTY", NlsLanguage.INSTANCE);
    }

    @Override
    public String getExternalId() {
        return "nls.includeproperty";
    }

    @Override
    public NlsIncludeProperty createPsi(@NotNull NlsIncludePropertyStub stub) {
        return new NlsIncludePropertyImpl(stub);
    }

    @Override
    public NlsIncludePropertyStub createStub(@NotNull NlsIncludeProperty psi, StubElement parentStub) {
        return new NlsIncludePropertyStubImpl(parentStub);
    }

    @Override
    public NlsIncludePropertyStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new NlsIncludePropertyStubImpl(parentStub);
    }

    @Override
    public void serialize(NlsIncludePropertyStub stub, StubOutputStream dataStream) throws IOException {
    }

    @Override
    public void indexStub(NlsIncludePropertyStub stub, IndexSink sink) {
    }
}
