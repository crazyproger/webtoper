package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Arguments;

/**
 * @author crazyproger
 */
public interface ArgumentsContainer {
    @SubTag
    Arguments getArguments();
}
