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

/**
 * @author crazyproger
 */
public class NlsCompletionTest extends NlsTestCase {

    protected String getTestDataPath() {
        return super.getTestDataPath() + "/completion";
    }

    public void testNlsXml() throws Throwable {
        final String testName = getTestName(true);
        myFixture.configureByFiles(testName + ".xml", testName + "/Document.properties", testName + "/Document2.properties");
        myFixture.testCompletionVariants(testName + ".xml", testName + ".Document", testName + ".Document2");
    }

    /**
     * Check that already included files did not appear in completion
     */
    public void testProperty() throws Throwable {
        myFixture.configureByFiles(testName + "/Case.properties", testName + "/Included.properties", testName + "/Variant.properties");
        myFixture.testCompletionVariants(testName + "/Case.properties", testName + ".Variant");
    }
}