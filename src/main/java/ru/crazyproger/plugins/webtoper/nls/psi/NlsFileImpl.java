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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsNameImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Collection<IProperty> getAllPropertiesRecursive(@NotNull Set<NlsFileImpl> processedFiles) { // todo must be cached
        Collection<NlsFileImpl> includedFiles = getIncludedFiles();
        Map<String, IProperty> keyToProperty = Maps.newHashMap();
        for (NlsFileImpl nlsFile : includedFiles) {  // order matters!
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

    @Nullable
    public String getNlsName() { // todo must be cached
        for (VirtualFile folder : NlsUtils.getAllNlsRoots(getProject())) {
            if (folder != null) {
                VirtualFile file = getVirtualFile();
                assert file != null;
                if (VfsUtil.isAncestor(folder, file, true)) {
                    String relativePath = FileUtil.getRelativePath(folder.getPath(), file.getPath(), '/');
                    assert relativePath != null : "relative path must be";
                    String dottedPath = relativePath.replaceAll("/", ".");
                    return StringUtil.trimEnd(dottedPath, PropertiesFileType.DOT_DEFAULT_EXTENSION);
                }
            }
        }
        return null;
    }

    private void addPropertiesToMap(Collection<IProperty> fileProperties, Map<String, IProperty> properties) {
        for (IProperty fileProperty : fileProperties) {
            String key = fileProperty.getKey();
            if (key != null) {
                properties.put(key, fileProperty);
            }
        }
    }

    public static class PropertyKeyEqualsPredicate implements Predicate<IProperty> {
        private final String key;

        public PropertyKeyEqualsPredicate(String key) {
            this.key = key;
        }

        @Override
        public boolean apply(@Nullable IProperty iProperty) {
            return iProperty != null && key.equals(iProperty.getKey());
        }
    }

    public static class Property2PsiElementFunction implements Function<IProperty, PsiElement> {
        @Override
        public PsiElement apply(@Nullable IProperty iProperty) {
            if (iProperty != null) {
                return iProperty.getPsiElement();
            }
            return null;
        }
    }

}
