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

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import java.util.List;

import static ru.crazyproger.plugins.webtoper.WebtoperBundle.message;

/**
 * @author crazyproger
 */
public class LineMarkerTest extends NlsTestCase {

    public static final String EXT = PropertiesFileType.DOT_DEFAULT_EXTENSION;

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/linemarker";
    }

    // tests for 'overriding'

    public void testSimpleChild() throws Exception {
        myFixture.configureByFiles(getTestName(true) + EXT, "rootPack/root" + EXT);
        doTest(1, message("nls.lineMarker.overrides.tooltip.oneBundle", "rootPack.root"));
    }

    public void testSecondLevel() throws Exception {
        myFixture.configureByFiles(getTestName(true) + EXT, "rootPack/root" + EXT, "simpleChild" + EXT);
        doTest(3, message("nls.lineMarker.overrides.tooltip.oneBundle", "simpleChild"));
    }

    public void testRecursiveInclude() throws Exception {
        myFixture.configureByFiles(getTestName(true) + EXT, "firstRecursive" + EXT);
        doTest(1, message("nls.lineMarker.overrides.tooltip.oneBundle", "firstRecursive"));
    }

    public void testMultipleInclude() throws Exception {
        myFixture.configureByFiles(getTestName(true) + EXT, "rootPack/root" + EXT, "anotherRoot" + EXT);
        doTest(2, message("nls.lineMarker.overrides.tooltip.multiple"));
    }

    // tests for 'overridden'

    public void testRoot() throws Exception {
        myFixture.configureByFile("rootPack/" + getTestName(true) + EXT);
//        doTest(2, prefix);
    }

    private void doTest(int count, String text) {
        final Editor editor = myFixture.getEditor();
        final Project project = myFixture.getProject();

        myFixture.doHighlighting();

        final List<LineMarkerInfo> infoList = DaemonCodeAnalyzerImpl.getLineMarkers(editor.getDocument(), project);
        assertNotNull(infoList);
        assertEquals(count, infoList.size());
        for (LineMarkerInfo markerInfo : infoList) {
            assertNotNull(markerInfo);
            String tooltip = markerInfo.getLineMarkerTooltip();
            assertEquals(tooltip, text);
        }
    }
}
