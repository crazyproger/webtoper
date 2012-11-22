package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsName;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsNameStub;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameStubImpl;

import java.io.IOException;

/**
 * @author crazyproger
 */
public class NlsNameElementType extends NlsStubElementType<NlsNameStub, NlsName> {
    public NlsNameElementType() {
        super("NLS_NAME");
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
    public NlsNameStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new NlsNameStubImpl(parentStub);
    }
}
