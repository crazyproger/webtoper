package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;

public interface InvocationDescription extends DomElement {

    @SubTag("modalpopup")
    ModalPopupDescription getModalPopupDescription();
}
