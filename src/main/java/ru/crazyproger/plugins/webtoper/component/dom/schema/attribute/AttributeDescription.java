/*
 * Copyright 2013 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.crazyproger.plugins.webtoper.component.dom.schema.attribute;

import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.CustomChildrenContainer;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.NlsIdContainer;

/**
 * Description such as:
 * <code>
 * <attribute name="show_topic" type="show_topic" category="dm_info" display_after="subject">
 * <label_text>
 * <nlsid>MSG_SHOW_TOPIC</nlsid>
 * </label_text>
 * </attribute>
 * </code>
 */
public interface AttributeDescription extends Attribute, CustomChildrenContainer {
    @com.intellij.util.xml.Attribute
    GenericAttributeValue<String> getType();

    @com.intellij.util.xml.Attribute
    GenericAttributeValue<String> getCategory();

    @com.intellij.util.xml.Attribute
    GenericAttributeValue<String> getDisplayAfter();

    @SubTag("label_text")
    NlsIdContainer getLabelText();
}
