/*
 * Copyright 2012 Vladimir Rudev
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

package ru.crazyproger.plugins.webtoper;

import com.intellij.openapi.application.PathManager;

import java.io.File;

public class WebtoperTestHelper {

    public static final String TEST_DATA_PATH = "/src/test/testData";

    public static String getTestDataPath() {
        String projectRoot = getRootPath();
        return projectRoot + TEST_DATA_PATH;
    }

    public static String getRootPath() {
        return new File(PathManager.getResourceRoot(WebtoperTestHelper.class, "/ru/crazyproger/plugins/webtoper/WebtoperTestHelper.class")).getParentFile().getParent();
    }
}
