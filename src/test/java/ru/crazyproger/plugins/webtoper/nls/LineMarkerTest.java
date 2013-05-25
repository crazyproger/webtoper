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

import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.TestDataFile;
import org.jetbrains.annotations.NonNls;
import ru.crazyproger.plugins.webtoper.LineMarkerTestCase;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;

import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;

/**
 * @see ru.crazyproger.plugins.webtoper.nls.codeinsight.NlsLineMarkerProvider
 */
public class LineMarkerTest extends LineMarkerTestCase {

    public static final String EXT = PropertiesFileType.DOT_DEFAULT_EXTENSION;

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/nls/linemarker";
    }

    // tests for 'overriding'

    public void testSimpleChild() throws Exception {
        configureByFiles(getTestName(true) + EXT, "rootPack/root" + EXT);
        doTest(message("nls.lineMarker.overrides.tooltip.oneBundle", "rootPack.root"));
    }

    public void testSecondLevel() throws Exception {
        configureByFiles(getTestName(true) + EXT, "rootPack/root" + EXT, "simpleChild" + EXT);
        String simpleChild = message("nls.lineMarker.overrides.tooltip.oneBundle", "simpleChild");
        doTest(simpleChild,
                message("nls.lineMarker.overrides.tooltip.oneBundle", "rootPack.root"),
                simpleChild);
    }

    public void testRecursiveInclude() throws Exception {
        configureByFiles(getTestName(true) + EXT, "firstRecursive" + EXT);
        doTest(message("nls.lineMarker.overrides.tooltip.oneBundle", "firstRecursive"),
                message("nls.lineMarker.overridden.tooltip.oneBundle", "firstRecursive"));
    }

    public void testMultipleInclude() throws Exception {
        configureByFiles(getTestName(true) + EXT, "rootPack/root" + EXT, "anotherRoot" + EXT);
        doTest(message("nls.lineMarker.overrides.tooltip.oneBundle", "rootPack.root"),
                message("nls.lineMarker.overrides.tooltip.multiple"));
    }

    // tests for 'overridden'

    public void testRoot() throws Exception {
        configureByFiles("rootPack/" + getTestName(true) + EXT, "simpleChild" + EXT, "secondLevel" + EXT);
        doTest(message("nls.lineMarker.overridden.tooltip.multiple"),
                message("nls.lineMarker.overridden.tooltip.oneBundle", "secondLevel"));
    }

    private PsiFile[] configureByFiles(@TestDataFile @NonNls String... filePaths) {
        for (int i = 0; i < filePaths.length; i++) {
            String filePath = filePaths[i];
            filePaths[i] = WebtoperFacet.NLS_ROOT_NAME + "/" + filePath;
        }
        return myFixture.configureByFiles(filePaths);
    }
}
