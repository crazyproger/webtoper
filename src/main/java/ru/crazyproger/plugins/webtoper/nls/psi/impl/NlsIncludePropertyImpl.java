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

package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludeProperty;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludePropertyStub;

/**
 * @author crazyproger
 */
public class NlsIncludePropertyImpl extends NlsStubElementImpl<NlsIncludePropertyStub> implements NlsIncludeProperty {

    public NlsIncludePropertyImpl(final NlsIncludePropertyStub stub) {
        super(stub, NlsElementTypes.INCLUDE_PROPERTY);
    }

    public NlsIncludePropertyImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public NlsFileImpl getNlsFile() {
        return (NlsFileImpl) super.getContainingFile();
    }
}
