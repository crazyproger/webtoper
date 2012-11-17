package ru.crazyproger.plugins.webtoper;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import ru.crazyproger.plugins.webtoper.config.ProjectConfig;

/**
 * @author crazyproger
 */
public class NlsCompletionTest extends LightCodeInsightFixtureTestCase {

    protected String getTestDataPath() {
        return WebtoperTestHelper.getTestDataPath() + "/completion";
    }

    public void testNlsXml() throws Throwable {
        final String testName = getTestName(true);
        myFixture.configureByFiles(testName + ".xml", testName + "/Document.properties", testName + "/Document2.properties");
        myFixture.testCompletionVariants(testName + ".xml", testName + ".Document", testName + ".Document2");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Project project = myFixture.getProject();
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        config.setNlsRoots(ModuleRootManager.getInstance(myModule).getContentRoots());
    }
}