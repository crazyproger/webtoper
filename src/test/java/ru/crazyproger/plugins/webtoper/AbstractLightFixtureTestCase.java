package ru.crazyproger.plugins.webtoper;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;

public abstract class AbstractLightFixtureTestCase extends LightCodeInsightFixtureTestCase {
    public static final String TEST_DATA_PATH = "/src/test/testData";
    protected String testName;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testName = getTestName(true);
    }

    public static String getRootPath() {
        return new File("").getAbsolutePath();
    }

    protected String getTestDataPath() {
        String projectRoot = getRootPath();
        return projectRoot + TEST_DATA_PATH;
    }
}
