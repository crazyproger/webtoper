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

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesList;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesListStub;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludesListImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludesListStubImpl;

import java.io.IOException;

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
