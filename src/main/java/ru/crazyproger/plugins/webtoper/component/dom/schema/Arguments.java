package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author crazyproger
 */
public interface Arguments extends DomElement {
    @SubTagList("argument")
    List<Argument> getArguments();
}
