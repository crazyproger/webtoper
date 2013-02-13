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

package ru.crazyproger.plugins.webtoper.config;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class WebtoperIcons {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, WebtoperIcons.class);
    }

    public static final Icon WEBTOPER_16 = load("/icons/wletter16.png"); // 48x48
    public static final Icon WEBTOPER = load("/icons/wletter48.png"); // 48x48
}
