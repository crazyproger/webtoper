package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * @author crazyproger
 */
public interface Argument extends DomElement {
    @Required
    GenericAttributeValue<String> getName(); // todo should be unique with siblings

    @Required
    @Attribute("value")
    GenericAttributeValue<String> getArgumentValue();
}
