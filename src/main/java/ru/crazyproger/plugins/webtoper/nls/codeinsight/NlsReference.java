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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

/**
 * @author vrudev
 */
public class NlsReference extends PsiReferenceBase<PsiElement> {

    private static final Logger LOG = Logger.getInstance("#" + NlsReference.class.getName());

    private final PsiElement resolveTo;

    public NlsReference(PsiElement element, NlsFileImpl resolveTo) {
        super(element, ElementManipulators.getValueTextRange(element));
        this.resolveTo = resolveTo;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return resolveTo;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return EMPTY_ARRAY;
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

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {

        PsiFile file = element.getContainingFile();
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) throw new IllegalArgumentException("Can't bind to element " + element);
        String name = NlsUtils.getNlsName(virtualFile, element.getProject());
        if (name == null) throw new IllegalArgumentException("Can't bind to element " + element);
        return getManipulator().handleContentChange(myElement, getRangeInElement(), name);
    }

    protected ElementManipulator<PsiElement> getManipulator() {
        ElementManipulator<PsiElement> manipulator = ElementManipulators.getManipulator(myElement);
        if (manipulator == null) {
            LOG.error("Cannot find manipulator for " + myElement + " in " + this + " class " + getClass());
        }
        return manipulator;
    }
}
