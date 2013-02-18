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

package ru.crazyproger.plugins.webtoper.component;

import com.google.common.collect.Iterables;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.presentation.java.SymbolPresentationUtil;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.NullableFunction;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.DomIconable;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.IdentifiedById;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.PrimaryElement;
import ru.crazyproger.plugins.webtoper.config.Icons;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;

public class ClassLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
        for (PsiElement element : elements) {
            if (element instanceof PsiIdentifier
                    && element.getParent() instanceof PsiClass
                    && element.getContainingFile() instanceof PsiJavaFile) {
                collectClassLineMarkers((PsiClass) element.getParent(), result);
            }
        }
    }

    private void collectClassLineMarkers(PsiClass element, Collection<LineMarkerInfo> result) {
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(element.getProject()).getFileIndex();
        PsiFile containingFile = element.getContainingFile();
        PsiIdentifier identifier = PsiTreeUtil.getChildOfType(element, PsiIdentifier.class);
        if (identifier == null) return;
        if (containingFile == null) return;
        VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile == null) return;
        Module module = fileIndex.getModuleForFile(virtualFile);
        if (module == null) return;
        Collection<PsiElement> elements = getNavigatablePsiElements(element, module);
        if (elements.isEmpty()) return;
        Icon icon;
        MyPsiElementCellRenderer renderer = new MyPsiElementCellRenderer(DomManager.getDomManager(element.getProject()));
        if (elements.size() > 1) {
            icon = Icons.W16;
        } else {
            icon = renderer.getIcon(Iterables.getFirst(elements, null));
        }
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(icon);
        builder.setTargets(elements);
        builder.setPopupTitle(message("class.lineMarker.popupTitle"));
        builder.setTooltipTitle(message("class.lineMarker.tooltip.title"));
        builder.setNamer(new XmlFileByElementNamer());
        builder.setCellRenderer(renderer);
        result.add(builder.createLineMarkerInfo(identifier));
    }

    private Collection<PsiElement> getNavigatablePsiElements(PsiClass element, Module module) {
        Collection<PsiElement> elements = new ArrayList<PsiElement>();
        PsiReference[] references = ReferencesSearch.search(element, Utils.getXmlConfigsScope(module), false).toArray(new PsiReference[0]);
        DomManager domManager = DomManager.getDomManager(element.getProject());
        for (PsiReference reference : references) {
            PsiElement referenceElement = reference.getElement();
            DomElement domElement = getDomElement(referenceElement, domManager);
            if (domElement == null) continue;
            PrimaryElement primary = domElement.getParentOfType(PrimaryElement.class, false);
            if (primary == null) continue;
            elements.add(referenceElement);
        }
        return elements;
    }

    private DomElement getDomElement(PsiElement element, DomManager domManager) {
        XmlAttribute parentAttribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
        if (parentAttribute != null) return domManager.getDomElement(parentAttribute);
        XmlTag parentTag = PsiTreeUtil.getParentOfType(element, XmlTag.class);
        if (parentTag != null) return domManager.getDomElement(parentTag);
        return null;
    }

    private static class XmlFileByElementNamer implements NullableFunction<PsiElement, String> {
        @Nullable
        @Override
        public String fun(PsiElement param) {
            PsiFile containingFile = param.getContainingFile();
            if (containingFile != null) {
                return containingFile.getName();
            }
            return null;
        }
    }

    private static class MyPsiElementCellRenderer extends PsiElementListCellRenderer<PsiElement> {

        private DomManager domManager;

        private MyPsiElementCellRenderer(DomManager domManager) {
            this.domManager = domManager;
        }

        @Override
        protected Icon getIcon(PsiElement element) {
            XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag.class);
            DomElement domElement = domManager.getDomElement(tag);
            if (domElement != null) {
                DomIconable parent = domElement.getParentOfType(DomIconable.class, false);
                if (parent != null) {
                    return parent.getIcon(getIconFlags());
                }
            }
            return super.getIcon(tag);
        }

        @Override
        public String getElementText(PsiElement element) {
            XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag.class);
            DomElement domElement = domManager.getDomElement(tag);
            if (domElement != null) {
                IdentifiedById byId = domElement.getParentOfType(IdentifiedById.class, false);
                if (byId != null && byId.isValid() && byId.getId().getValue() != null) {
                    return message("class.lineMarker.byId.element.text", byId.getId().getValue());
                }
            }
            return SymbolPresentationUtil.getSymbolPresentableText(element);
        }

        public String getContainerText(PsiElement element, final String name) {
            return SymbolPresentationUtil.getSymbolContainerText(element);
        }

        @Override
        protected int getIconFlags() {
            return Iconable.ICON_FLAG_VISIBILITY;
        }
    }
}
