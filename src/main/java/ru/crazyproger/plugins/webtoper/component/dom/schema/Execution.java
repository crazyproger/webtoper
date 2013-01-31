package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.ArgumentsContainer;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.ClassAttributeContainer;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.CustomChildrenContainer;

/**
 * @author crazyproger
 *         todo if arguments provided, then container sibling must be(component optional)
 *         (p221)
 */
public interface Execution extends ArgumentsContainer, ClassAttributeContainer, CustomChildrenContainer, DomElement {

    GenericDomValue<Permit> getPermit();

    @SubTag("olecomponent")
    OleComponent getOleComponent();

    @SubTag("navigation")
    GenericDomValue<NavigatonType> getNavigationType();

    GenericDomValue<String> getComponent(); // todo reference to component

    GenericDomValue<String> getContainer(); // todo reference to container(component)
}
