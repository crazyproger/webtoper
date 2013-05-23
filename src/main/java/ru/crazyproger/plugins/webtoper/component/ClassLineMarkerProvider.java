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
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.PrimaryElement;
import ru.crazyproger.plugins.webtoper.config.Icons;

import javax.swing.Icon;
import java.util.Collection;
import java.util.List;

import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;

public class ClassLineMarkerProvider extends AbstractXmlReferencedLineMarkerProvider<PsiElement> implements LineMarkerProvider {

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
        Collection<PsiElement> elements = getNavigablePsiElements(element, module);
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

    @Override
    protected PsiElement getReferenceElement(PsiReference reference, DomManager manager) {
        PsiElement referenceElement = reference.getElement();
        DomElement domElement = getDomElement(referenceElement, manager);
        if (domElement == null) return null;
        PrimaryElement primary = domElement.getParentOfType(PrimaryElement.class, false);
        if (primary == null) return null;
        return referenceElement;
    }

    protected DomElement getDomElement(PsiElement element, DomManager domManager) {
        XmlAttribute parentAttribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
        if (parentAttribute != null) return domManager.getDomElement(parentAttribute);
        XmlTag parentTag = PsiTreeUtil.getParentOfType(element, XmlTag.class);
        if (parentTag != null) return domManager.getDomElement(parentTag);
        return null;
    }
}
