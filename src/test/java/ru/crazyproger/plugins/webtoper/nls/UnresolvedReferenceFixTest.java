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

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.BeforeAfterTreeTestCase;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;
import ru.crazyproger.plugins.webtoper.component.dom.EmptyInspection;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.nls.codeinsight.NlsFileInspection;

import java.io.IOException;

/**
 * @see EmptyInspection
 * @see NlsFileInspection
 * @see ru.crazyproger.plugins.webtoper.nls.codeinsight.AbstractNlsReference#getQuickFixes()
 */
public class UnresolvedReferenceFixTest extends BeforeAfterTreeTestCase {

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/" + NlsTestCase.NLS_FOLDER + "/" + "unresolvedReference";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //noinspection unchecked
        myFixture.enableInspections(NlsFileInspection.class, EmptyInspection.class);
    }

    public void testSimplest() throws Exception {
        doNlsFileTest("created");
    }

    public void testWithFolders() throws Exception {
        doNlsFileTest("newFolder.second.created");
    }

    public void testFromXml() throws Exception {
        doTest("newFolder.second.created", "conf.xml");
    }

    private void doNlsFileTest(String nlsName) throws IOException {
        doTest(nlsName, WebtoperFacet.NLS_ROOT_NAME + "/" + "nls.properties");
    }

    private void doTest(String nlsName, String filePath) throws IOException {
        doTestWithFix(WebtoperBundle.message("create.nls.file.quickfix.text", nlsName), filePath);
    }

    private void doTestWithFix(@NotNull String message, @NotNull String filePath) throws IOException {
        final IntentionAction action = doTestHighlightingAndGetQuickfix(message, filePath);
        assertNotNull(action);
        assertTrue(action.isAvailable(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile()));

        new WriteCommandAction(myFixture.getProject(), "") {
            @Override
            protected void run(Result result) throws Throwable {
                action.invoke(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile());
            }
        }.execute();
        checkAfter();
    }

    @Nullable
    private IntentionAction doTestHighlightingAndGetQuickfix(@NotNull String message,
                                                             @NotNull String filePath) throws IOException {
        doTestHighlighting(filePath);

        IntentionAction action = null;

        for (IntentionAction a : myFixture.getAvailableIntentions()) {
            if (message.equals(a.getText())) {
                action = a;
            }
        }
        return action;
    }

    private void doTestHighlighting(@NotNull String filePath)
            throws IOException {

        final VirtualFile file = myFixture.findFileInTempDir(filePath);
        myFixture.configureFromExistingVirtualFile(file);
        myFixture.doHighlighting();
        myFixture.checkHighlighting();
    }
}
