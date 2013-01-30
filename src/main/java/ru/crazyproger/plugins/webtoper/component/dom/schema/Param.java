package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author crazyproger
 */
public interface Param extends DomElement {
    GenericAttributeValue<String> getName(); // todo should be unique with siblings

    GenericAttributeValue<Boolean> getRequired();
}
