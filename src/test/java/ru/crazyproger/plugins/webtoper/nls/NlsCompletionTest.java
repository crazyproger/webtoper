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
        return WebtoperTestHelper.getTestDataPath() + "/completion";
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