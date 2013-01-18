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

package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.facet.FacetManager;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.javaee.web.facet.WebFacetType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import ru.crazyproger.plugins.webtoper.WebtoperTestHelper;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;

/**
 * @author crazyproger
 */
public class NlsCompletionTest extends LightCodeInsightFixtureTestCase {

    private String testName;

    protected String getTestDataPath() {
        return WebtoperTestHelper.getTestDataPath() + "/nls" + "/completion";
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Module module = myFixture.getModule();
        FacetManager facetManager = FacetManager.getInstance(module);
        WebFacet container = facetManager.createFacet(WebFacetType.getInstance(), "Web", null);
        WebtoperFacet facet = facetManager.createFacet(WebtoperFacet.getFacetType(), "Webtoper", container);
        facet.getConfiguration().setNlsRoot(ModuleRootManager.getInstance(module).getContentRoots()[0]);
        final ModifiableFacetModel facetModel = facetManager.createModifiableModel();
        facetModel.addFacet(facet);
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                facetModel.commit();
            }
        });
        testName = getTestName(true);
    }
}