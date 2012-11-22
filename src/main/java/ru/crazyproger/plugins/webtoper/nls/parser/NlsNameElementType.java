package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.stubs.*;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsName;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsNameStub;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameStubImpl;

import java.io.IOException;

/**
 * @author crazyproger
 */
public class NlsNameElementType extends IStubElementType<NlsNameStub, NlsName> {
    public NlsNameElementType() {
        super("NLS_NAME", NlsLanguage.INSTANCE);
    }

    @Override
    public NlsName createPsi(@NotNull NlsNameStub stub) {
        return new NlsNameImpl(stub);
    }

    @Override
    public NlsNameStub createStub(@NotNull NlsName psi, StubElement parentStub) {
        return new NlsNameStubImpl(parentStub);
    }

    @Override
    public String getExternalId() {
        return "nls.name";
    }

    @Override
    public NlsNameStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new NlsNameStubImpl(parentStub);
    }

    @Override
    public void serialize(NlsNameStub stub, StubOutputStream dataStream) throws IOException {
    }

    @Override
    public void indexStub(NlsNameStub stub, IndexSink sink) {
    }
}
