package ru.crazyproger.plugins.webtoper.component;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.PrimaryElement;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author crazyproger
 */
public class ClassLineMarkerProvider implements LineMarkerProvider {
    public static final Icon COMPONENT_LINE_MARK = IconLoader.getIcon("/icons/puzzle_green_16.png");

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
        for (PsiElement element : elements) {
            if (element instanceof PsiClass) {
                collectClassLineMarkers((PsiClass) element, result);
            }
        }
    }

    private void collectClassLineMarkers(PsiClass element, Collection<LineMarkerInfo> result) {
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(element.getProject()).getFileIndex();
        PsiFile containingFile = element.getContainingFile();
        if (containingFile == null) return;
        VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile == null) return;
        Module module = fileIndex.getModuleForFile(virtualFile);
        if (module == null) return;
        Collection<PsiElement> elements = getNavigatablePsiElements(element, module);
        if (elements.isEmpty()) return;
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(COMPONENT_LINE_MARK);
        builder.setTargets(elements);
        PsiIdentifier identifier = PsiTreeUtil.getChildOfType(element, PsiIdentifier.class);
        if (identifier == null) return;
        result.add(builder.createLineMarkerInfo(identifier));
    }

    private Collection<PsiElement> getNavigatablePsiElements(PsiClass element, Module module) {
        Collection<PsiElement> elements = new ArrayList<PsiElement>();
        PsiReference[] references = ReferencesSearch.search(element, Utils.getWebRootsScope(module), false).toArray(new PsiReference[0]);
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
}
