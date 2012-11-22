package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubOutputStream;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;

import java.io.IOException;

/**
 * @author vrudev
 */
public abstract class NlsStubElementType<StubT extends StubElement, PsiT extends PsiElement> extends IStubElementType<StubT, PsiT> {
    public NlsStubElementType(String debugName) {
        super(debugName, NlsLanguage.INSTANCE);
    }

    @Override
    public String getExternalId() {
        return "nls." + getClass().getSimpleName();
    }

    @Override
    public void serialize(StubT stub, StubOutputStream dataStream) throws IOException {
    }

    @Override
    public void indexStub(StubT stub, IndexSink sink) {
    }
}
