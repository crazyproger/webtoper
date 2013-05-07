package ru.crazyproger.plugins.webtoper.component.dom.schema.attribute;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;

public interface Attribute extends DomElement {

    @com.intellij.util.xml.Attribute
    @NameValue
    GenericAttributeValue<String> getName();
}
