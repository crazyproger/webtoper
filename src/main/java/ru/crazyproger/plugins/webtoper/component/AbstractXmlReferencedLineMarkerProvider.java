package ru.crazyproger.plugins.webtoper.component;

import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.presentation.java.SymbolPresentationUtil;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.NullableFunction;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.WebtoperUtil;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.DomIconable;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.IdentifiedById;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.Collection;

import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;

public abstract class AbstractXmlReferencedLineMarkerProvider<T> {

    protected Collection<T> getNavigablePsiElements(PsiElement element, Module module) {
        Collection<T> elements = new ArrayList<T>();
        PsiReference[] references = ReferencesSearch.search(element, WebtoperUtil.getXmlConfigsScope(module), false).toArray(new PsiReference[0]);
        DomManager domManager = DomManager.getDomManager(element.getProject());
        for (PsiReference reference : references) {
            T referenceElement = getReferenceElement(reference, domManager);
            if (referenceElement != null) {
                elements.add(referenceElement);
            }
        }
        return elements;
    }

    protected abstract T getReferenceElement(PsiReference reference, DomManager manager);

    protected static class XmlFileByElementNamer implements NullableFunction<PsiElement, String> {
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

    protected static class MyPsiElementCellRenderer extends PsiElementListCellRenderer<PsiElement> {

        private DomManager domManager;

        protected MyPsiElementCellRenderer(DomManager domManager) {
            this.domManager = domManager;
        }

        @Override
        protected Icon getIcon(PsiElement element) {
            XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag.class, false);
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
            XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag.class, false);
            DomElement domElement = domManager.getDomElement(tag);
            if (domElement != null) {
                IdentifiedById byId = domElement.getParentOfType(IdentifiedById.class, false);
                if (byId != null && byId.isValid() && byId.getId().getValue() != null) {
                    return message("lineMarker.byId.element.text", byId.getId().getValue());
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
