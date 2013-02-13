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

package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.openapi.project.Project;


public class NlsHierarchyTest extends NlsTestCase {

    protected String getTestDataPath() {
        return super.getTestDataPath() + "/hierarchy";
    }

    public void testOneParent() throws Throwable {
        String testName = getTestName(true);
        myFixture.configureByFiles(testName + "/child.properties", testName + "/parent.properties");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Project project = myFixture.getProject();
//        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
//        config.setNlsRoots(ModuleRootManager.getInstance(myModule).getContentRoots());
    }
}
