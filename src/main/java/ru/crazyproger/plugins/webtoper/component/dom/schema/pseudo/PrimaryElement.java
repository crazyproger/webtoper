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

package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.util.xml.*;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Filter;
import ru.crazyproger.plugins.webtoper.component.dom.schema.converter.ExtendsModifiesConverter;
import ru.crazyproger.plugins.webtoper.component.dom.schema.converter.NlsDomConverter;

import java.util.List;

/**
 * @author crazyproger
 */
public interface PrimaryElement extends CustomChildrenContainer, DomElement {

    @Attribute("extends")
    @Convert(ExtendsModifiesConverter.class)
    GenericAttributeValue<PrimaryElement> getExtendsValue();

    @Attribute("modifies")
    @Convert(ExtendsModifiesConverter.class)
    GenericAttributeValue<PrimaryElement> getModifiesValue();

    @Attribute("version")
    GenericAttributeValue<String> getVersion();

    @SubTag("nlsbundle")
    @Convert(NlsDomConverter.class)
    GenericDomValue<PropertiesFile> getNlsBundle();

    @SubTagList("filter")
    List<Filter> getFilters();

    // todo modify(after, before), insert, remove
}
