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
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.properties.IProperty;
import com.intellij.lang.properties.psi.Property;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.refactoring.psi.SearchUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Sets.newHashSet;
import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;
import static ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl.Property2PsiElementFunction;
import static ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl.PropertyKeyEqualsPredicate;

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
                collectOverriddenLineMarkers((Property) element, result);
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
            Collection<IProperty> sameKey = findOverriddenProperties(key, parent, Sets.<PsiFile>newHashSet(currentFile));
            parentProperties.addAll(sameKey);
        }
        if (parentProperties.isEmpty()) return;

        MarkerInfo info = new MarkerInfo(element,
                parentProperties,
                result,
                AllIcons.General.OverridingMethod,
                "nls.lineMarker.overrides.popupTitle",
                "nls.lineMarker.overrides.tooltip.multiple",
                "nls.lineMarker.overrides.tooltip.oneBundle");
        fillLineMarkers(info);
    }

    @NotNull
    private Collection<IProperty> findOverriddenProperties(@NotNull final String key, @NotNull NlsFileImpl rootFile, @NotNull Collection<PsiFile> excludes) {
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
        Set<PsiFile> newExcludes = newHashSet(excludes);
        newExcludes.add(rootFile);

        for (NlsFileImpl nlsFile : includedFiles) {
            result.addAll(findOverriddenProperties(key, nlsFile, newExcludes));
        }
        return result;
    }

    private void collectOverriddenLineMarkers(Property element, Collection<LineMarkerInfo> result) {
        final String key = element.getKey();
        if (key == null) return;

        NlsFileImpl currentFile = (NlsFileImpl) element.getContainingFile();
        Collection<NlsFileImpl> referencing = getReferencingFiles(currentFile);
        Set<IProperty> properties = newHashSet();
        for (NlsFileImpl child : referencing) {
            Collection<IProperty> overridingProperties = findOverridingProperties(key, child, Sets.<PsiFile>newHashSet(currentFile));
            properties.addAll(overridingProperties);
        }

        if (properties.isEmpty()) return;

        MarkerInfo info = new MarkerInfo(element,
                properties,
                result,
                AllIcons.General.OverridenMethod,
                "nls.lineMarker.overridden.popupTitle",
                "nls.lineMarker.overridden.tooltip.multiple",
                "nls.lineMarker.overridden.tooltip.oneBundle");
        fillLineMarkers(info);
    }

    private Collection<NlsFileImpl> getReferencingFiles(NlsFileImpl currentFile) {
        Iterable<PsiReference> references = SearchUtils.findAllReferences(currentFile);

        Collection<NlsFileImpl> directChilds = transform(Lists.<PsiReference>newArrayList(references), new Reference2ContainedFileFunction());

        return filter(directChilds, Predicates.notNull());
    }

    private Collection<IProperty> findOverridingProperties(String key, NlsFileImpl currentFile, Set<PsiFile> excludes) {
        if (excludes.contains(currentFile)) {
            return Collections.emptyList();
        }
        List<IProperty> properties = currentFile.getProperties();
        List<IProperty> result = new LinkedList<IProperty>();
        Collection<IProperty> withSameKey = filter(properties, new PropertyKeyEqualsPredicate(key));
        result.addAll(withSameKey);

        excludes.add(currentFile);
        Collection<NlsFileImpl> children = getReferencingFiles(currentFile);
        for (NlsFileImpl child : children) {
            result.addAll(findOverridingProperties(key, child, excludes));
        }
        return result;
    }

    private void fillLineMarkers(MarkerInfo info) {
        Collection<PsiElement> targets = transform(info.getNavigationTargets(), new Property2PsiElementFunction());
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(info.getIcon());
        targets = filter(targets, Predicates.notNull());
        builder.setTargets(targets);
        String tooltipText;
        if (targets.size() > 1) {
            tooltipText = message(info.getMultiBundleKey());
        } else {
            PsiFile psiFile = targets.iterator().next().getContainingFile();
            assert psiFile instanceof NlsFileImpl;
            tooltipText = message(info.getOneBundleKey(), ((NlsFileImpl) psiFile).getNlsName());
        }
        builder.setTooltipText(tooltipText);
        builder.setPopupTitle(message(info.getToolTipKey()));
        info.getResult().add(builder.createLineMarkerInfo(info.getTarget()));
    }

    /**
     * parameter object
     */
    private static class MarkerInfo {
        private final Property target;
        private final Collection<IProperty> navigationTargets;
        private final Collection<LineMarkerInfo> result;
        private final Icon icon;
        private final String toolTipKey;
        private final String multiBundleKey;
        private final String oneBundleKey;

        private MarkerInfo(Property target, Collection<IProperty> navigationTargets, Collection<LineMarkerInfo> result, Icon icon, String toolTipKey, String multiBundleKey, String oneBundleKey) {
            this.target = target;
            this.navigationTargets = navigationTargets;
            this.result = result;
            this.icon = icon;
            this.toolTipKey = toolTipKey;
            this.multiBundleKey = multiBundleKey;
            this.oneBundleKey = oneBundleKey;
        }

        public Property getTarget() {
            return target;
        }

        public Collection<IProperty> getNavigationTargets() {
            return navigationTargets;
        }

        public Collection<LineMarkerInfo> getResult() {
            return result;
        }

        public Icon getIcon() {
            return icon;
        }

        public String getToolTipKey() {
            return toolTipKey;
        }

        public String getMultiBundleKey() {
            return multiBundleKey;
        }

        public String getOneBundleKey() {
            return oneBundleKey;
        }
    }

    public static class Reference2ContainedFileFunction implements Function<PsiReference, NlsFileImpl> {
        @Override
        public NlsFileImpl apply(@Nullable PsiReference psiReference) {
            if (psiReference != null) {
                PsiElement element = psiReference.getElement();
                if (element != null && element.isValid()) {
                    PsiFile psiFile = element.getContainingFile();
                    if (psiFile instanceof NlsFileImpl) {
                        return (NlsFileImpl) psiFile;
                    }
                }
            }
            return null;
        }
    }
}
