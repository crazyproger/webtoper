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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
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
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

import java.util.Collection;
import java.util.Collections;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * @author crazyproger
 */
public class NlsCompletionContributor extends CompletionContributor {
    public NlsCompletionContributor() {
        extend(CompletionType.BASIC, psiElement(NlsNameImpl.class), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                Project project = parameters.getPosition().getContainingFile().getProject();
                Collection<NlsFileImpl> files = getNlsFiles(project);
                if (files.isEmpty()) return;
                final NlsFileImpl currentFile = (NlsFileImpl) parameters.getOriginalFile();
                final Collection<NlsFileImpl> includedFiles = currentFile.getIncludedFiles();
                Collection<NlsFileImpl> filtered = Collections2.filter(files, new Predicate<NlsFileImpl>() {
                    @Override
                    public boolean apply(@Nullable NlsFileImpl nlsFile) {
                        assert nlsFile != null;
                        return !nlsFile.equals(currentFile) && !includedFiles.contains(nlsFile);
                    }
                });

                Collection<LookupElement> lookupElements = Collections2.transform(filtered, new NlsFile2LookupElementFunction(project));

                result.addAllElements(lookupElements);
            }
        });
    }

    @NotNull
    public static Collection<NlsFileImpl> getNlsFiles(Project project) {
        Collection<VirtualFile> files = getNlsVirtualFiles(project);
        if (files.isEmpty()) return Collections.emptyList();
        final PsiManager psiManager = PsiManager.getInstance(project);
        Collection<NlsFileImpl> nlsFiles = Collections2.transform(files, new Function<VirtualFile, NlsFileImpl>() {
            @Override
            public NlsFileImpl apply(@Nullable VirtualFile virtualFile) {
                if (virtualFile != null) {
                    return (NlsFileImpl) psiManager.findFile(virtualFile);
                }
                return null;
            }
        });
        return Collections2.filter(nlsFiles, Predicates.notNull());
    }

    @NotNull
    private static Collection<VirtualFile> getNlsVirtualFiles(Project project) {
        GlobalSearchScope nlsScope = NlsUtils.getNlsScope(project);
        if (nlsScope == null) {
            return Collections.emptyList();
        }
        return FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
    }

    public static class NlsFile2LookupElementFunction implements Function<NlsFileImpl, LookupElement> {
        private final Project project;

        public NlsFile2LookupElementFunction(Project project) {
            this.project = project;
        }

        @Override
        public LookupElement apply(@Nullable NlsFileImpl nlsFile) {
            assert nlsFile != null;
            VirtualFile virtualFile = nlsFile.getVirtualFile();
            assert virtualFile != null;
            String fullName = NlsUtils.getNlsName(virtualFile, project);   // todo getName must be from NlsNameImpl
            assert fullName != null;
            return LookupElementBuilder.create(nlsFile, fullName).withIcon(nlsFile.getIcon(0));
        }
    }
}
