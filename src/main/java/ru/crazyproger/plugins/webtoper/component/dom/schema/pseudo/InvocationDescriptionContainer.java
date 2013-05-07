package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.InvocationDescription;

public interface InvocationDescriptionContainer {

    @SubTag("invocation")
    InvocationDescription getInvocationDescription();
}
