package ru.crazyproger.plugins.webtoper;

import com.intellij.openapi.application.PathManager;

import java.io.File;

public class WebtoperTestHelper {

    public static final String TEST_DATA_PATH = "/src/test/testData";

    public static String getTestDataPath() {
        String projectRoot = getRootPath();
        return projectRoot + TEST_DATA_PATH;
    }

    public static String getRootPath() {
        return new File(PathManager.getResourceRoot(WebtoperTestHelper.class, "/ru/crazyproger/plugins/webtoper/WebtoperTestHelper.class")).getParentFile().getParent();
    }
}
