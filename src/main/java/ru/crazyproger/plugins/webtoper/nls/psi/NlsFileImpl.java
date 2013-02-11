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

package ru.crazyproger.plugins.webtoper.nls.psi;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @NotNull
    public Collection<NlsFileImpl> getIncludedFiles() {
        Collection<NlsNameImpl> includes = getIncludes();
        if (includes == null || includes.isEmpty()) {
            return Collections.emptyList();
        }
        List<NlsFileImpl> included = new ArrayList<NlsFileImpl>(includes.size());
        for (NlsNameImpl include : includes) {
            PsiReference reference = include.getReference();
            if (reference == null) {
                continue;
            }
            PsiElement resolved = reference.resolve();
            if (resolved != null) {
                included.add((NlsFileImpl) resolved);
            }
        }
        return included;
    }

    public Collection<IProperty> getAllProperties() {
        return getAllPropertiesRecursive(Sets.<NlsFileImpl>newHashSet(this));
    }

    public Collection<IProperty> getAllPropertiesRecursive(@NotNull Set<NlsFileImpl> processedFiles) {
        Collection<NlsFileImpl> includedFiles = getIncludedFiles();
        Map<String, IProperty> keyToProperty = Maps.newHashMap();
        for (NlsFileImpl nlsFile : includedFiles) {
            if (processedFiles.contains(nlsFile)) {
                continue;
            }
            processedFiles.add(nlsFile);
            Collection<IProperty> fileProperties = nlsFile.getAllPropertiesRecursive(processedFiles);
            addPropertiesToMap(fileProperties, keyToProperty);
        }
        addPropertiesToMap(getProperties(), keyToProperty);
        return keyToProperty.values();
    }

    private void addPropertiesToMap(Collection<IProperty> fileProperties, Map<String, IProperty> properties) {
        for (IProperty fileProperty : fileProperties) {
            String key = fileProperty.getKey();
            if (key != null) {
                properties.put(key, fileProperty);
            }
        }
    }
}
