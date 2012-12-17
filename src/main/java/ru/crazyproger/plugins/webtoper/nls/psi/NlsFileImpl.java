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

package ru.crazyproger.plugins.webtoper.nls.psi;

import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author crazyproger
 */
public class NlsFileImpl extends PropertiesFileImpl {

    public NlsFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public String toString() {
        return "Nls file: " + getName();
    }

    public Collection<NlsNameImpl> getIncludes() {
        return PsiTreeUtil.findChildrenOfType(this, NlsNameImpl.class);
    }

    public Collection<PsiFile> getIncludedFiles() {
        Collection<NlsNameImpl> includes = getIncludes();
        if (includes == null || includes.isEmpty()) {
            return Collections.emptyList();
        }
        List<PsiFile> included = new ArrayList<PsiFile>(includes.size());
        for (NlsNameImpl include : includes) {
            PsiReference reference = include.getReference();
            if (reference == null) {
                continue;
            }
            PsiElement resolved = reference.resolve();
            if (resolved != null) {
                included.add((PsiFile) resolved);
            }
        }
        return included;
    }
}
