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
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

import java.util.Collection;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * @author crazyproger
 */
public class NlsIncludesCompletionContributor extends CompletionContributor {
    public NlsIncludesCompletionContributor() {
        extend(CompletionType.BASIC, psiElement(NlsNameImpl.class), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                Project project = parameters.getPosition().getContainingFile().getProject();
                NlsFileImpl currentFile = (NlsFileImpl) parameters.getOriginalFile();
                GlobalSearchScope nlsScope = NlsUtils.getNlsScope(project);
                if (nlsScope == null) {
                    return;
                }
                Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
                Collection<PsiFile> includedFiles = currentFile.getIncludedFiles();

                for (VirtualFile virtualFile : files) {
                    PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                    if (file != null && !file.equals(currentFile) && !includedFiles.contains(file)) {
                        String fullName = NlsUtils.getNlsName(virtualFile, project);
                        LookupElementBuilder element = LookupElementBuilder.create(file, fullName).withIcon(file.getIcon(0));
                        result.addElement(element);
                    }
                }
            }
        });
    }
}
