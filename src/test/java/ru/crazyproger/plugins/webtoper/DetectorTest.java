package ru.crazyproger.plugins.webtoper;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.framework.detection.DetectedFrameworkDescription;
import com.intellij.framework.detection.impl.FacetBasedDetectedFrameworkDescription;
import com.intellij.framework.detection.impl.FrameworkDetectionContextImpl;
import com.intellij.framework.detection.impl.FrameworkDetectionProcessor;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.javaee.web.facet.WebFacetType;
import com.intellij.mock.MockProgressIndicator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.TestSourceBasedTestCase;
import org.jetbrains.annotations.NonNls;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacetConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class DetectorTest extends TestSourceBasedTestCase {

    private String testRoot;
    private WebFacet webContainer;

    @Override
    protected String getTestPath() {
        return WebtoperLightFixtureTestCase.TEST_DATA_PATH + "/detector";
    }

    protected String getTestDataPath() {
        return WebtoperLightFixtureTestCase.getRootPath();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testRoot = ModuleRootManager.getInstance(getModule()).getContentRoots()[0].getPath();
        @NonNls FacetManager facetManager = FacetManager.getInstance(myModule);
        webContainer = facetManager.createFacet(WebFacetType.getInstance(), "Web", null);
        final ModifiableFacetModel facetModel = facetManager.createModifiableModel();
        facetModel.addFacet(webContainer);
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                facetModel.commit();
            }
        });

    }

    @Override
    protected void tearDown() throws Exception {
        @NonNls FacetManager facetManager = FacetManager.getInstance(myModule);
        final ModifiableFacetModel modifiableModel = facetManager.createModifiableModel();
        Facet[] allFacets = modifiableModel.getAllFacets();
        for (Facet facet : allFacets) {
            modifiableModel.removeFacet(facet);
        }
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                modifiableModel.commit();
            }
        });

        super.tearDown();
    }

    public void testSingle() throws Exception {
        List<Pair<String, String>> expected = new ArrayList<Pair<String, String>>();

        expected.add(new Pair<String, String>(testRoot, getTestName(true)));
        doTest(expected);
    }

    public void testWithExistent() throws Exception {
        @NonNls FacetManager facetManager = FacetManager.getInstance(myModule);
        final ModifiableFacetModel facetModel = facetManager.createModifiableModel();
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(testRoot + "/existent");
        WebtoperFacet facet = facetManager.createFacet(WebtoperFacet.getFacetType(), "Webtoper", webContainer);
        facet.getConfiguration().setFacetRoot(file);
        facetModel.addFacet(facet);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                facetModel.commit();
            }
        });

        List<Pair<String, String>> expected = new ArrayList<Pair<String, String>>();
        expected.add(new Pair<String, String>(testRoot + "/new", "new"));
        doTest(expected);
    }

    private void doTest(List<Pair<String, String>> expected) {
        FrameworkDetectionProcessor processor = new FrameworkDetectionProcessor(new MockProgressIndicator(), new FrameworkDetectionContextImpl(getProject()));
        List<? extends DetectedFrameworkDescription> detectedDescriptions = processor.processRoots(Arrays.asList(new File(testRoot)));
        assertEquals(expected.size(), detectedDescriptions.size());
        for (DetectedFrameworkDescription description : detectedDescriptions) {
            WebtoperFacetConfiguration configuration = ((FacetBasedDetectedFrameworkDescription<WebtoperFacet, WebtoperFacetConfiguration>) description).getConfiguration();
            VirtualFile facetRoot = configuration.getFacetRoot();
            assertNotNull(facetRoot);
            Pair<String, String> detected = new Pair<String, String>(facetRoot.getPath(),
                    configuration.getSuggestedName());
            assertTrue("Unexpected: " + detected, expected.contains(detected));
            expected.remove(detected);
        }
        assertTrue("Not found: " + expected, expected.isEmpty());
    }
}
