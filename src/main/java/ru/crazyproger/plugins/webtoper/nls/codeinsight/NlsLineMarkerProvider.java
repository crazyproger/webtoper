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
import com.google.common.collect.Sets;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.Property;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Collections2.filter;
import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;

/**
 * @author crazyproger
 */
public class NlsLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
        for (PsiElement element : elements) {
            if (element instanceof Property) {
                collectOverridingLineMarkers((Property) element, result);
            }
        }
    }

    private void collectOverridingLineMarkers(final Property element, Collection<LineMarkerInfo> result) {
        final String key = element.getKey();
        if (key == null) return;

        NlsFileImpl currentFile = (NlsFileImpl) element.getContainingFile();
        Collection<NlsFileImpl> includedFiles = currentFile.getIncludedFiles();
        if (includedFiles.isEmpty()) return;

        Set<IProperty> parentProperties = new HashSet<IProperty>();
        for (NlsFileImpl parent : includedFiles) {
            Collection<IProperty> sameKey = getPropertiesWithKey(key, parent, Sets.<PsiFile>newHashSet(currentFile));
            parentProperties.addAll(sameKey);
        }
        if (parentProperties.isEmpty()) return;

        Collection<PsiElement> targets = Collections2.transform(parentProperties, new Property2PsiElementFunction());
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(AllIcons.General.OverridingMethod);
        targets = filter(targets, Predicates.notNull());
        builder.setTargets(targets);
        String tooltipText;
        if (targets.size() > 1) {
            tooltipText = message("nls.lineMarker.overrides.tooltip.multiple");
        } else {
            PsiFile psiFile = targets.iterator().next().getContainingFile();
            assert psiFile instanceof NlsFileImpl;
            tooltipText = message("nls.lineMarker.overrides.tooltip.oneBundle", ((NlsFileImpl) psiFile).getNlsName());
        }
        builder.setTooltipText(tooltipText);
        builder.setPopupTitle(message("nls.lineMarker.overrides.popupTitle"));
        result.add(builder.createLineMarkerInfo(element));
    }

    @NotNull
    private Collection<IProperty> getPropertiesWithKey(@NotNull final String key, @NotNull NlsFileImpl rootFile, @NotNull Collection<PsiFile> excludes) {
        if (excludes.contains(rootFile)) {
            return Collections.emptyList();
        }
        List<IProperty> fileProperties = rootFile.getProperties();
        Collection<IProperty> withSameKey = filter(fileProperties, new PropertyKeyEqualsPredicate(key));
        if (!withSameKey.isEmpty()) {
            return withSameKey;
        }

        Collection<NlsFileImpl> includedFiles = rootFile.getIncludedFiles();
        if (includedFiles.isEmpty()) return Collections.emptyList();

        Collection<IProperty> result = new LinkedList<IProperty>();
        Set<PsiFile> newExcludes = Sets.newHashSet(excludes);
        newExcludes.add(rootFile);

        for (NlsFileImpl nlsFile : includedFiles) {
            result.addAll(getPropertiesWithKey(key, nlsFile, newExcludes));
        }
        return result;
    }

    private static class PropertyKeyEqualsPredicate implements Predicate<IProperty> {
        private final String key;

        public PropertyKeyEqualsPredicate(String key) {
            this.key = key;
        }

        @Override
        public boolean apply(@Nullable IProperty iProperty) {
            return iProperty != null && key.equals(iProperty.getKey());
        }
    }

    private static class Property2PsiElementFunction implements Function<IProperty, PsiElement> {
        @Override
        public PsiElement apply(@Nullable IProperty iProperty) {
            if (iProperty != null) {
                return iProperty.getPsiElement();
            }
            return null;
        }
    }
}
