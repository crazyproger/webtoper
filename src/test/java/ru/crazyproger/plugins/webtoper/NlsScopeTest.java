package ru.crazyproger.plugins.webtoper;

import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.TestSourceBasedTestCase;
import ru.crazyproger.plugins.webtoper.config.ProjectConfig;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

import java.util.Collection;

/**
 * Simple test that check configuration of NLS folders and {@link NlsUtils#getNlsScope(com.intellij.openapi.project.Project)} method.
 * <p/>
 * Test needs to be run with -Didea.home.path=[path_to_module_dir]
 *
 * @author crazyproger
 */
public class NlsScopeTest extends TestSourceBasedTestCase {
    //    @Override
    protected String getBasePath() {
        return "/src/test/";
    }

    @Override
    protected String getTestPath() {
        return getBasePath();
    }

    protected String getTestDataPath() {
        return PathManager.getHomePath();
    }

    //
    public void testResources() throws Throwable {
        ProjectConfig config = ServiceManager.getService(getProject(), ProjectConfig.class);
        String moduleRootPath = ModuleRootManager.getInstance(getModule()).getContentRoots()[0].getPath();
        config.setFoldersAsString(moduleRootPath + "/layer1;" +
                moduleRootPath + "/layer2;" +
                moduleRootPath + "/layer3");

        GlobalSearchScope nlsScope = NlsUtils.getNlsScope(getProject());
        assertNotNull(nlsScope);
        Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
        assertNotNull(files);
        assertEquals(3, files.size());
    }
}
