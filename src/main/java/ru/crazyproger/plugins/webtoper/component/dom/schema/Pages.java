package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.CustomChildrenContainer;

/**
 * @author crazyproger
 */
public interface Pages extends CustomChildrenContainer, DomElement {
    @SubTag
    GenericDomValue<String> getStart();
}