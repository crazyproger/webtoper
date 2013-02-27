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

import com.google.common.collect.Sets;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Set;

public class WholeElementTextRefProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        Project project = element.getContainingFile().getProject();
        VirtualFile[] nlsRoots = NlsUtils.getAllNlsRoots(project);
        if (ArrayUtils.isEmpty(nlsRoots)) return PsiReference.EMPTY_ARRAY;

        String text = StringUtils.trim(element.getText());

        VirtualFile virtualFile = element.getContainingFile().getVirtualFile();
        if (virtualFile == null) return PsiReference.EMPTY_ARRAY;

        Module module = ProjectFileIndex.SERVICE.getInstance(project).getModuleForFile(virtualFile);

        Set<NlsFileImpl> nlsFiles = NlsUtils.getNlsFiles(text, module);

        Set<PsiReference> references = Sets.newHashSet();
        for (NlsFileImpl nlsFile : nlsFiles) {
            references.add(new TextElementNlsReference(element, nlsFile));
        }
        if (references.isEmpty()) {
            return new PsiReference[]{new TextElementNlsReference(element, null)};
//            return PsiReference.EMPTY_ARRAY;
        }
        return references.toArray(new PsiReference[references.size()]);
    }
}
