/*
 * Copyright 2013 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
