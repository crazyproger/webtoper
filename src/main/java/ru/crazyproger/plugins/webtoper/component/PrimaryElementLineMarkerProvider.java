package ru.crazyproger.plugins.webtoper.component;

import com.google.common.collect.Iterables;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.WebtoperUtil;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.PrimaryElement;
import ru.crazyproger.plugins.webtoper.config.Icons;

import javax.swing.Icon;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;

public class PrimaryElementLineMarkerProvider extends AbstractXmlReferencedLineMarkerProvider<GenericAttributeValue> implements LineMarkerProvider {
    public static final String EXTENDS_ATTRIBUTE_NAME = "extends";
    public static final String MODIFIES_ATTRIBUTE_NAME = "modifies";

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
        if (elements.isEmpty()) return;
        PsiElement first = Iterables.getFirst(elements, null);
        if (!isInConfigScope(first)) return;

        for (PsiElement element : elements) {
            if (element instanceof XmlTag) {
                DomElement domElement = DomManager.getDomManager(element.getProject()).getDomElement((XmlTag) element);
                if (domElement instanceof PrimaryElement) {
                    PrimaryElement primaryElement = (PrimaryElement) domElement;
                    collectOverriddenModified(element, primaryElement, result);
                }
            }
        }
    }

    private boolean isInConfigScope(PsiElement first) {
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(first.getProject()).getFileIndex();
        PsiFile containingFile = first.getContainingFile();
        if (containingFile == null) return false;
        VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile == null) return false;
        GlobalSearchScope scope = WebtoperUtil.getWebRootsScope(fileIndex.getModuleForFile(virtualFile));
        return scope.contains(virtualFile);
    }

    private void collectOverriddenModified(PsiElement psiElement, PrimaryElement domElement, Collection<LineMarkerInfo> result) {
        if (!domElement.isValid()) return;
        Module module = domElement.getModule();
        Collection<GenericAttributeValue> elements = getNavigablePsiElements(psiElement, module);
        if (elements.isEmpty()) return;

        // separate modifies and extends
        Set<PsiElement> extendsList = new HashSet<PsiElement>();
        Set<PsiElement> modifiesList = new HashSet<PsiElement>();
        for (GenericAttributeValue attribute : elements) {
            String name = attribute.getXmlElementName();

            XmlElement xmlElement = attribute.getParent().getXmlElement();
            if (xmlElement != null) {
                if (EXTENDS_ATTRIBUTE_NAME.equals(name)) {
                    extendsList.add(xmlElement);
                } else if (MODIFIES_ATTRIBUTE_NAME.equals(name)) {
                    modifiesList.add(xmlElement);
                }
            }
        }

        DomManager domManager = DomManager.getDomManager(psiElement.getProject());

        if (!extendsList.isEmpty()) {
            NavigationGutterIconBuilder<PsiElement> extendsBuilder = createBuilder(Icons.EXTENDED16,
                    message("primaryElement.extended.lineMarker.popupTitle"),
                    message("primaryElement.extended.lineMarker.tooltip.title"),
                    domManager,
                    extendsList);
            result.add(extendsBuilder.createLineMarkerInfo(psiElement));
        }

        if (!modifiesList.isEmpty()) {
            NavigationGutterIconBuilder<PsiElement> modifiesBuilder = createBuilder(Icons.MODIFIED16,
                    message("primaryElement.modified.lineMarker.popupTitle"),
                    message("primaryElement.modified.lineMarker.tooltip.title"),
                    domManager,
                    modifiesList);
            result.add(modifiesBuilder.createLineMarkerInfo(psiElement));
        }

    }

    @Override
    protected GenericAttributeValue getReferenceElement(PsiReference reference, DomManager manager) {
        PsiElement referenceElement = reference.getElement();
        XmlAttribute parentAttribute = PsiTreeUtil.getParentOfType(referenceElement, XmlAttribute.class);
        if (parentAttribute == null) return null;
        GenericAttributeValue domElement = manager.getDomElement(parentAttribute);
        if (domElement == null) return null;
        DomElement parent = domElement.getParent();
        if (parent instanceof PrimaryElement) {
            return domElement;
        }
        return null;
    }

    private NavigationGutterIconBuilder<PsiElement> createBuilder(Icon icon, String popupTitle, String tooltipTitle, DomManager domManager, Collection<PsiElement> targets) {
        MyPsiElementCellRenderer renderer = new MyPsiElementCellRenderer(domManager);
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(icon);
        builder.setTargets(targets);
        builder.setPopupTitle(popupTitle);
        builder.setTooltipTitle(tooltipTitle);
        builder.setNamer(new XmlFileByElementNamer());
        builder.setCellRenderer(renderer);
        return builder;
    }
}
