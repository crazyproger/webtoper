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

package ru.crazyproger.plugins.webtoper.component.dom;

import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Config;

/**
 * @author crazyproger
 */
public class EmptyInspection extends BasicDomElementsInspection<Config> {
    public EmptyInspection() {
        super(Config.class);
    }

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return WebtoperBundle.message("webtoper.inspections.group.name");
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return WebtoperBundle.message("emptyInspection.inspection.displayName");
    }

    @NotNull
    @Override
    public String getShortName() {
        return "EmptyInspection";
    }
}
