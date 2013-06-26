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

package ru.crazyproger.plugins.webtoper.component;

import com.intellij.psi.PsiElement;
import com.intellij.psi.search.searches.ReferencesSearch;
import ru.crazyproger.plugins.webtoper.WebtoperLightFixtureTestCase;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;


public class FindComponentUsagesTest extends WebtoperLightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/component";
    }

    public void testFindComponentUsagesFullQName() throws Exception {
        myFixture.configureByFile(WebtoperFacet.CONFIG_ROOT_NAME + "/" + getTestName(false) + ".xml");
        PsiElement elementAtCaret = myFixture.getElementAtCaret();
        assertEquals(2, ReferencesSearch.search(elementAtCaret).findAll().size());
    }
}
