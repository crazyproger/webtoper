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

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.javaee.web.facet.WebFacetType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;

import java.io.File;

public abstract class WebtoperLightFixtureTestCase extends LightCodeInsightFixtureTestCase {

    public static final String TEST_DATA_PATH = "/src/test/testData";

    protected VirtualFile moduleRoot;
    protected String testName;

    protected String getTestDataPath() {
        String projectRoot = getRootPath();
        return projectRoot + TEST_DATA_PATH;
    }

    public static String getRootPath() {
        return new File("").getAbsolutePath();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Module module = myFixture.getModule();
        FacetManager facetManager = FacetManager.getInstance(module);
        WebFacet container = facetManager.createFacet(WebFacetType.getInstance(), "Web", null);
        moduleRoot = ModuleRootManager.getInstance(module).getContentRoots()[0];
        container.addWebRoot(moduleRoot, "/");
        WebtoperFacet facet = facetManager.createFacet(WebtoperFacet.getFacetType(), "Webtoper", container);
        facet.getConfiguration().setNlsRoot(moduleRoot);
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

    @Override
    protected void tearDown() throws Exception {
        Module module = myFixture.getModule();
        FacetManager facetManager = FacetManager.getInstance(module);
        final ModifiableFacetModel facetModel = facetManager.createModifiableModel();
        Facet[] allFacets = facetModel.getAllFacets();
        for (Facet facet : allFacets) {
            facetModel.removeFacet(facet);
        }
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                facetModel.commit();
            }
        });
        super.tearDown();
    }
}
