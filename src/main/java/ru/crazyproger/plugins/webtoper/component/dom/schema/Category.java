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

package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.attribute.AttributeNamesEnumeration;
import ru.crazyproger.plugins.webtoper.component.dom.schema.attribute.AttributeNamesWithSeparatorsEnumeration;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.IdentifiedById;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.NlsIdContainer;

public interface Category extends DomElement, IdentifiedById {
    @Attribute
    @NameValue
    @Required
    GenericAttributeValue<String> getId();

    @SubTag("name")
    NlsIdContainer getName();

    @SubTag("attributes")
    AttributeNamesWithSeparatorsEnumeration getAttributes();

    @SubTag("moreattributes")
    AttributeNamesEnumeration getMoreAttributes();
}
