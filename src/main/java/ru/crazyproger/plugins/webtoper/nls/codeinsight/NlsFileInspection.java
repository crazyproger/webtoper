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

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

public class NlsFileInspection extends LocalInspectionTool {

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof NlsNameImpl) {
                    NlsNameImpl name = (NlsNameImpl) element;
                    PsiReference reference = name.getReference();
                    assert reference != null;
                    if (reference.resolve() == null) {
                        String errorMessage = WebtoperBundle.message("nls.name.unresolved.error.message", element.getText());
                        holder.registerProblem(reference, errorMessage, ProblemHighlightType.LIKE_UNKNOWN_SYMBOL);
                    }
                }
            }
        };
    }

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return WebtoperBundle.message("webtoper.inspections.group.name");
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return WebtoperBundle.message("nlsFiles.inspection.displayName");
    }

    @NotNull
    @Override
    public String getShortName() {
        return "NlsInspection";
    }
}
