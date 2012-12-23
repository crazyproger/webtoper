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

package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesList;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesListStub;

/**
 * @author crazyproger
 */
public class NlsIncludesListImpl extends NlsStubElementImpl<NlsIncludesListStub> implements NlsIncludesList {
    public NlsIncludesListImpl(final NlsIncludesListStub stub) {
        super(stub, NlsElementTypes.INCLUDES_LIST);
    }

    public NlsIncludesListImpl(@NotNull ASTNode node) {
        super(node);
    }
}
