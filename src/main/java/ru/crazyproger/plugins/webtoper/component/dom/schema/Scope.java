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

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import ru.crazyproger.plugins.webtoper.component.dom.schema.primary.*;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.ScopeDefiner;

import java.util.List;

/**
 * @author crazyproger
 */
public interface Scope extends ScopeDefiner, DomElement {

    @SubTagList("action")
    List<Action> getActions();

    @SubTagList("component")
    List<Component> getComponents();

    @SubTagList("docbaseobjectconfig")
    List<DocbaseObjectConfig> getDocbaseObjectConfigs();

    @SubTagList("application")
    List<Application> getApplications();

    @SubTagList("attributelist")
    List<AttributeList> getAttributeLists();


}
