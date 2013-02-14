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

package ru.crazyproger.plugins.webtoper.component.dom.schema.converter;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.codeinsight.NlsCompletionContributor;
import ru.crazyproger.plugins.webtoper.nls.codeinsight.XmlTagNlsReference;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;
import java.util.Set;

public class NlsDomConverter extends ResolvingConverter<NlsFileImpl> implements CustomReferenceConverter<NlsFileImpl> {
    @Nullable
    @Override
    public NlsFileImpl fromString(@Nullable @NonNls String s, ConvertContext context) {
        Set<NlsFileImpl> files = NlsUtils.getNlsFiles(s, context.getModule());
        return files.isEmpty() ? null : files.iterator().next();
    }

    @Nullable
    @Override
    public String toString(@Nullable NlsFileImpl propertiesFile, ConvertContext context) {
        return propertiesFile != null ? propertiesFile.getNlsName() : null;
    }

    @Nullable
    @Override
    public String getErrorMessage(@Nullable String s, ConvertContext context) {
        return super.getErrorMessage(s, context);
    }

    @NotNull
    @Override
    public Collection<? extends NlsFileImpl> getVariants(ConvertContext context) {
        final Project project = context.getProject();
        return NlsCompletionContributor.getNlsFiles(project);
    }

    @NotNull
    @Override
    public PsiReference[] createReferences(GenericDomValue<NlsFileImpl> value, PsiElement element, ConvertContext context) {
        return new PsiReference[]{new XmlTagNlsReference((XmlTag) element, value.getValue())};
    }
}
