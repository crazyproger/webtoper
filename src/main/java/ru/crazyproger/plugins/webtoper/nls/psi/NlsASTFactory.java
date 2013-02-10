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

package ru.crazyproger.plugins.webtoper.nls.psi;

import com.intellij.lang.properties.psi.impl.PropertiesASTFactory;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsTokenTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

/**
 * @author crazyproger
 */
public class NlsASTFactory extends PropertiesASTFactory {

    @Nullable
    @Override
    public LeafElement createLeaf(IElementType type, CharSequence text) {
        if (type == NlsTokenTypes.NLS_NAME) {
            return new NlsNameImpl(type, text);
        }
        return super.createLeaf(type, text);
    }
}
