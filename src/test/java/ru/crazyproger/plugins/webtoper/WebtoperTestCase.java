package ru.crazyproger.plugins.webtoper;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.IdeaTestCase;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import org.junit.After;
import org.junit.Before;

import java.lang.reflect.Method;

public abstract class WebtoperTestCase<FixtureType extends IdeaProjectTestFixture> extends UsefulTestCase {

    protected FixtureType fixture;
    protected Method currentRunningTestMethod;

    protected WebtoperTestCase() {
        IdeaTestCase.initPlatformPrefix();
    }

    @Before
    public void before(Method method) throws Throwable {
        this.currentRunningTestMethod = method;

        TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder =
                getProjectBuilder();

//        final JavaModuleFixtureBuilder moduleFixtureBuilder = projectBuilder.addModule(JavaModuleFixtureBuilder.class);
//        moduleFixtureBuilder.addSourceContentRoot(myFixture.getTempDirPath());
//        customizeProject(projectBuilder);

        this.fixture = createTestFixture(projectBuilder.getFixture());
        this.fixture.setUp();
    }

    protected TestFixtureBuilder<IdeaProjectTestFixture> getProjectBuilder() {

        IdeaTestFixtureFactory testFixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();

        return testFixtureFactory.createLightFixtureBuilder(new DefaultLightProjectDescriptor());
    }

    @After
    public void after(Method method) throws Exception {
        this.fixture.tearDown();
        this.currentRunningTestMethod = null;
    }

    protected String getTestRootPath() {
        return "resources";
    }

    protected abstract String getRelativeDataPath();

    protected abstract FixtureType createTestFixture(IdeaProjectTestFixture projectTestFixture) throws Exception;

    protected Module getModule() {
        return fixture != null ? fixture.getModule() : null;
    }

    protected Project getProject() {
        return fixture != null ? fixture.getProject() : null;
    }
}
