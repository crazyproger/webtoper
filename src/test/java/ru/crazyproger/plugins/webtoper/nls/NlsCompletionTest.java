package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import ru.crazyproger.plugins.webtoper.WebtoperTestHelper;
import ru.crazyproger.plugins.webtoper.config.ProjectConfig;

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
        Project project = myFixture.getProject();
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        config.setNlsRoots(ModuleRootManager.getInstance(myModule).getContentRoots());
        testName = getTestName(true);
    }
}