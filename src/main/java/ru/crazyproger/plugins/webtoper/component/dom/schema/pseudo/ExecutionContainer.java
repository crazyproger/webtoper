package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Execution;

/**
 * @author crazyproger
 */
public interface ExecutionContainer {
    @SubTag
    Execution getExecution();
}
