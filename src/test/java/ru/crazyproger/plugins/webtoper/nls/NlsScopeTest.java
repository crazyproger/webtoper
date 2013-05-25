/*
 * Copyright 2013 Vladimir Rudev
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

package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.facet.FacetManager;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.javaee.web.facet.WebFacetType;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.TestSourceBasedTestCase;
import org.jetbrains.annotations.NonNls;
import ru.crazyproger.plugins.webtoper.WebtoperLightFixtureTestCase;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

import java.util.Collection;

/**
 * Simple test that check configuration of NLS folders and {@link NlsUtils#getNlsScope(com.intellij.openapi.project.Project)} method.
 * <p/>
 */
public class NlsScopeTest extends TestSourceBasedTestCase {

    @Override
    protected String getTestPath() {
        return WebtoperLightFixtureTestCase.TEST_DATA_PATH + "/" + NlsTestCase.NLS_FOLDER;
    }

    protected String getTestDataPath() {
        return WebtoperLightFixtureTestCase.getRootPath();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String moduleRootPath = ModuleRootManager.getInstance(getModule()).getContentRoots()[0].getPath();
        @NonNls FacetManager facetManager = FacetManager.getInstance(myModule);
        WebFacet container = facetManager.createFacet(WebFacetType.getInstance(), "Web", null);
        final ModifiableFacetModel facetModel = facetManager.createModifiableModel();
        findAndAdd(moduleRootPath + "/layer1", facetModel, facetManager, container);
        findAndAdd(moduleRootPath + "/layer2", facetModel, facetManager, container);
        findAndAdd(moduleRootPath + "/layer3", facetModel, facetManager, container);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                facetModel.commit();
            }
        });
    }

    private void findAndAdd(@NonNls String filePath, ModifiableFacetModel facetModel, FacetManager facetManager, WebFacet container) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(filePath);
        if (file == null) {
            return;
        }
        WebtoperFacet facet = facetManager.createFacet(WebtoperFacet.getFacetType(), "Webtoper", container);
        facet.getConfiguration().setFacetRoot(file);
        facetModel.addFacet(facet);
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
        PsiFile psiFile = getPsiManager().findFile(file);
        assertTrue(psiFile instanceof NlsFileImpl);
        String fullName = ((NlsFileImpl) psiFile).getNlsName();
        assertEquals("ru.crazyproger.l1.document.Document", fullName);
    }

}
