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

import ru.crazyproger.plugins.webtoper.BeforeAfterTreeTestCase;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;


public class RenameTest extends BeforeAfterTreeTestCase {
    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/" + NlsTestCase.NLS_FOLDER;
    }

    public void testRenameFromXml() throws Exception {
        doTest("renamed.properties");
    }

    private void doTest(String newName) throws Exception {
        myFixture.configureFromExistingVirtualFile(myFixture.findFileInTempDir(WebtoperFacet.CONFIG_ROOT_NAME + "/nlsXml.xml"));
        myFixture.renameElementAtCaret(newName);
        checkAfter();
    }
}
