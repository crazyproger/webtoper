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

package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

public class NlsNameManipulator extends AbstractElementManipulator<NlsNameImpl> {
    @Override
    public NlsNameImpl handleContentChange(NlsNameImpl element, TextRange range, String newContent)
            throws IncorrectOperationException {
        final String oldText = element.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());
        return (NlsNameImpl) element.replaceWithText(newText);
    }
}
