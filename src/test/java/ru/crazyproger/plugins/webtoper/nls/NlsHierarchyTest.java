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
public class NlsHierarchyTest extends LightCodeInsightFixtureTestCase {

    protected String getTestDataPath() {
        return WebtoperTestHelper.getTestDataPath() + "/hierarchy";
    }

    public void testOneParent() throws Throwable {
        String testName = getTestName(true);
        myFixture.configureByFiles(testName + "/child.properties", testName+"/parent.properties");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Project project = myFixture.getProject();
        ProjectConfig config = ServiceManager.getService(project, ProjectConfig.class);
        config.setNlsRoots(ModuleRootManager.getInstance(myModule).getContentRoots());
    }
}
