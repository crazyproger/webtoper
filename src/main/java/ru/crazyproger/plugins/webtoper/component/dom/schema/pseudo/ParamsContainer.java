package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.util.xml.SubTagList;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Params;

/**
 * @author crazyproger
 */
public interface ParamsContainer {
    @SubTagList
    Params getParams();
}
