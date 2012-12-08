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

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

/**
 * @author crazyproger
 */
public class NlsUnresolvedNameAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof NlsNameImpl)) return;
        NlsNameImpl name = (NlsNameImpl) element;
        PsiReference reference = name.getReference();
        if (reference == null || reference.resolve() == null) {
            Annotation annotation = holder.createErrorAnnotation(element, WebtoperBundle.message("nls.name.unresolved.error.message", name.getText()));
            annotation.setHighlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL);
            // todo 'create nls' quickfix must be placed here
        }
    }
}
