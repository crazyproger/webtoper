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

package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.google.common.base.Joiner;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.StringUtils;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

/**
 * @author crazyproger
 */
public class TextElementNlsReference extends AbstractNlsReference<PsiElement> {

    public TextElementNlsReference(PsiElement element, PsiElement resolveTo) {
        super(element, resolveTo);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        final String oldText = myElement.getText();
        final String[] chunks = NlsUtils.nlsNameToPathChunks(oldText);
        String newFileName = StringUtils.substringBeforeLast(newElementName, ".");
        assert chunks != null;
        chunks[chunks.length - 1] = newFileName;
        final String newNlsName = Joiner.on(".").join(chunks);
        return getManipulator().handleContentChange(myElement, getRangeInElement(), newNlsName);
    }

}
