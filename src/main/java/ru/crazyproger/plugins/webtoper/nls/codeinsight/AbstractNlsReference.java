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

package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.google.common.base.Joiner;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.LocalQuickFixProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.WebtoperUtil;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

public abstract class AbstractNlsReference<T extends PsiElement> extends PsiReferenceBase<T> implements LocalQuickFixProvider {

    private static final Logger LOG = Logger.getInstance("#" + AbstractNlsReference.class.getName());

    @Nullable
    protected final PsiElement resolveTo;

    public AbstractNlsReference(T element, @Nullable PsiElement resolveTo) {
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
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {

        PsiFile file = element.getContainingFile();
        if (!(file instanceof NlsFileImpl)) throw new IllegalArgumentException("Can't bind to element " + element);
        String name = ((NlsFileImpl) file).getNlsName();
        if (name == null) throw new IllegalArgumentException("Can't bind to element " + element);
        return getManipulator().handleContentChange(myElement, getRangeInElement(), name);
    }

    protected ElementManipulator<T> getManipulator() {
        ElementManipulator<T> manipulator = ElementManipulators.getManipulator(myElement);
        if (manipulator == null) {
            LOG.error("Cannot find manipulator for " + myElement + " in " + this + " class " + getClass());
        }
        return manipulator;
    }

    protected abstract String getElementText();

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        final String oldText = getElementText();
        final String[] chunks = NlsUtils.nlsNameToPathChunks(oldText);
        String newFileName = StringUtils.substringBeforeLast(newElementName, ".");
        chunks[chunks.length - 1] = newFileName;
        final String newNlsName = Joiner.on(".").join(chunks);
        return getManipulator().handleContentChange(getElement(), getRangeInElement(), newNlsName);
    }

    @Nullable
    @Override
    public LocalQuickFix[] getQuickFixes() {
        WebtoperFacet facet = WebtoperUtil.findFacetForElement(getElement());
        if (facet == null || !facet.isValid()) return LocalQuickFix.EMPTY_ARRAY;

        CreateNlsQuickFix createNlsQuickFix = new CreateNlsQuickFix(getElementText(), facet);
        return new LocalQuickFix[]{createNlsQuickFix};
    }
}
