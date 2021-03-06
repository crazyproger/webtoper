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

import com.google.common.collect.Collections2;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;

import static ru.crazyproger.plugins.webtoper.nls.codeinsight.NlsCompletionContributor.NlsFile2LookupElementFunction;

public class XmlTagNlsReference extends AbstractNlsReference<XmlTag> {
    public XmlTagNlsReference(XmlTag element, @Nullable PsiElement resolveTo) {
        super(element, resolveTo);
        mySoft = true;
    }

    @Override
    protected String getElementText() {
        return getElement().getValue().getText();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        final Project project = getElement().getProject();
        Collection<NlsFileImpl> filtered = NlsCompletionContributor.getNlsFiles(project);
        Collection<LookupElement> result = Collections2.transform(filtered, new NlsFile2LookupElementFunction());
        return result.toArray(new Object[result.size()]);
    }
}
