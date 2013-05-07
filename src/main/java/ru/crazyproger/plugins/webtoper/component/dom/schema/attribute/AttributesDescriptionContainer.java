package ru.crazyproger.plugins.webtoper.component.dom.schema.attribute;

import com.intellij.util.xml.SubTagList;

import java.util.List;

public interface AttributesDescriptionContainer {

    @SubTagList("attribute")
    List<AttributeDescription> getAttributesDescriptions();
}
