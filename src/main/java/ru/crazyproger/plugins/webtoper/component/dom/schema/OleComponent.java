package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author crazyproger
 */
public interface OleComponent extends DomElement {
    GenericAttributeValue<Boolean> getEnabled();
}
