package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.SubTag;

public interface ModalPopupDescription extends DomElement {

    @SubTag("windowsize")
    GenericDomValue<String> getWindowSize();

    @SubTag("refreshparentwindow")
    GenericDomValue<String> getRefreshParentWindow();
}
