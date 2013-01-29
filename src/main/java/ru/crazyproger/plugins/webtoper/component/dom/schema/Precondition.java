package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.*;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.ClassAttributeContainer;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.CustomChildrenContainer;

/**
 * @author crazyproger
 */
public interface Precondition extends CustomChildrenContainer, ClassAttributeContainer, DomElement {
    @SubTag("role")
    String getRoles();

    @SubTag("argument")
    String getArguments(); // todo link to parent primary-element params(p220)

    @SubTag
    GenericDomValue<Boolean> isReversePrecondition(); // todo should be with 'component' sibling

    @SubTag
    GenericDomValue<String> getComponent();

    @Attribute("onexecutiononly")
    GenericAttributeValue<Boolean> isOnExecutionOnly();
}
