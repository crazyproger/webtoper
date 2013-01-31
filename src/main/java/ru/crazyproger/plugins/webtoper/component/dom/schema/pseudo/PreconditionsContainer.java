package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Preconditions;

/**
 * @author crazyproger
 */
public interface PreconditionsContainer {
    @SubTag
    Preconditions getPreconditions();
}
