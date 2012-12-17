/*
 * Copyright 2012 Vladimir Rudev
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

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludeProperty;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludePropertyStub;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludePropertyImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludePropertyStubImpl;

import java.io.IOException;

/**
 * @author crazyproger
 */
public class NlsIncludePropertyStubElementType extends NlsStubElementType<NlsIncludePropertyStub, NlsIncludeProperty> {
    public NlsIncludePropertyStubElementType() {
        super("NLS_INCLUDE_PROPERTY");
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
}
