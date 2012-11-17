package ru.crazyproger.plugins.webtoper;

import com.intellij.lang.properties.PropertiesFileType;
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
 *
 * @author crazyproger
 */
public class NlsScopeTest extends TestSourceBasedTestCase {

    @Override
    protected String getTestPath() {
        return WebtoperTestHelper.TEST_DATA_PATH;
    }

    protected String getTestDataPath() {
        return WebtoperTestHelper.getRootPath();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ProjectConfig config = ServiceManager.getService(getProject(), ProjectConfig.class);
        String moduleRootPath = ModuleRootManager.getInstance(getModule()).getContentRoots()[0].getPath();
        config.setFoldersAsString(moduleRootPath + "/layer1;" +
                moduleRootPath + "/layer2;" +
                moduleRootPath + "/layer3");
    }

    public void testNlsFilesScope() throws Throwable {
        GlobalSearchScope nlsScope = NlsUtils.getNlsScope(getProject());
        assertNotNull(nlsScope);
        Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
        assertNotNull(files);
        assertEquals(3, files.size());
    }

    public void testNlsFullName() throws Throwable {
        GlobalSearchScope nlsScope = NlsUtils.getNlsScope(getProject());
        Collection<VirtualFile> files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, nlsScope);
        VirtualFile file = files.iterator().next();
        String fullName = NlsUtils.getNlsName(file, getProject());
        assertEquals("ru.crazyproger.l1.document.Document", fullName);
    }

}
