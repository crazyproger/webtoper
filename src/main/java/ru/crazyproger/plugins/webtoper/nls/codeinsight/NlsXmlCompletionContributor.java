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

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

import java.util.Collection;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * @author crazyproger
 */
public class NlsXmlCompletionContributor extends CompletionContributor {
    public static final String TAG_NAME = "nlsbundle";

    public NlsXmlCompletionContributor() {
        extend(CompletionType.BASIC, psiElement(XmlTokenType.XML_DATA_CHARACTERS), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                Project project = parameters.getPosition().getContainingFile().getProject();
                XmlTag xmlTag = PsiTreeUtil.getParentOfType(parameters.getPosition(), XmlTag.class);
                // todo convert this check into pattern
                if (xmlTag == null || !TAG_NAME.equals(xmlTag.getName())) return;

                GlobalSearchScope nlsScope = NlsUtils.getNlsScope(project);
                if (nlsScope == null) {
                    return;
                }
                Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
                for (VirtualFile virtualFile : files) {
                    PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                    if (file != null) {
                        String fullName = NlsUtils.getNlsName(virtualFile, project);
                        LookupElementBuilder element = LookupElementBuilder.create(file, fullName).withIcon(file.getIcon(0));
                        result.addElement(element);
                    }
                }
            }
        });
    }
}
