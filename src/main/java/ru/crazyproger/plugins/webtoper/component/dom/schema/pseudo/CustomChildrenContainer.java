package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.util.xml.CustomChildren;
import com.intellij.util.xml.DomElement;

import java.util.List;

/**
 * @author crazyproger
 */
public interface CustomChildrenContainer {
    @CustomChildren
    List<DomElement> getCustomChildren();
}
