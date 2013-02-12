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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            // Obtaining all properties except properties in current file,
            // else if include cycle occurs all properties in this file will be marked as.
            // Due to getAllProperties particularity(it returns set(!) of properties,
            // aggregator file properties overrides included properties) whe can't simply get all properties(PsiElements) from all files
            // and remove those who from current file
            Collection<IProperty> properties = parent.getAllPropertiesRecursive(Sets.newHashSet(currentFile));
            Collection<IProperty> withSameKey = Collections2.filter(properties, new PropertyKeyEqualsPredicate(key));
            parentProperties.addAll(withSameKey);
        }
        if (parentProperties.isEmpty()) return;

        Collection<PsiElement> targets = Collections2.transform(parentProperties, new Function<IProperty, PsiElement>() {
            @Override
            public PsiElement apply(@Nullable IProperty iProperty) {
                if (iProperty != null) {
                    return iProperty.getPsiElement();
                }
                return null;
            }
        });
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(AllIcons.General.OverridingMethod);
        targets = Collections2.filter(targets, Predicates.notNull());
        builder.setTargets(targets);
        String tooltipText;
        if (targets.size() > 1) {
            tooltipText = message("nls.lineMarker.overrides.tooltip.multiple");
        } else {
            tooltipText = message("nls.lineMarker.overrides.tooltip.oneBundle", targets.iterator().next().getContainingFile().getName());
        }
        builder.setTooltipText(tooltipText);
        builder.setPopupTitle(message("nls.lineMarker.overrides.popupTitle"));
        result.add(builder.createLineMarkerInfo(element));
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
}
