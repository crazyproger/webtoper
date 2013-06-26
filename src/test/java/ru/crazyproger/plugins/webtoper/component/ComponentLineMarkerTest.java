package ru.crazyproger.plugins.webtoper.component;

import ru.crazyproger.plugins.webtoper.LineMarkerTestCase;
import ru.crazyproger.plugins.webtoper.config.Icons;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;

/**
 * @see ClassLineMarkerProvider
 * @see PrimaryElementLineMarkerProvider
 */
public class ComponentLineMarkerTest extends LineMarkerTestCase {

    // this test was created after ClassLineMarkerProvider was implemented :)
    public static final String CLASS_TOOLTIP = "<html><body>Used in:<br>&nbsp;&nbsp;&nbsp;&nbsp;ClassLineMarker.xml<br></body></html>";

    public static final String PRIMARY_ELEMENT_EXTENDED_TOOLTIP = "<html><body>Extended in:<br>&nbsp;&nbsp;&nbsp;&nbsp;PrimaryElementLineMarkerExtends.xml<br></body></html>";
    public static final String PRIMARY_ELEMENT_MODIFIED_TOOLTIP = "<html><body>Modified in:<br>&nbsp;&nbsp;&nbsp;&nbsp;PrimaryElementLineMarkerModifies.xml<br></body></html>";

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/component";
    }

    public void testClassLineMarker() throws Exception {
        myFixture.configureByFiles(getTestName(false) + ".java", WebtoperFacet.CONFIG_ROOT_NAME + "/" + getTestName(false) + ".xml");
        doTest(new TextIconTuple(CLASS_TOOLTIP, Icons.C16), new TextIconTuple(CLASS_TOOLTIP, Icons.W16));
    }

    public void testPrimaryElementLineMarkerExtends() throws Exception {
        myFixture.configureByFiles(WebtoperFacet.CONFIG_ROOT_NAME + "/PrimaryElementLineMarkerParent.xml", WebtoperFacet.CONFIG_ROOT_NAME + "/" + getTestName(false) + ".xml");
        doTest(new TextIconTuple(PRIMARY_ELEMENT_EXTENDED_TOOLTIP, Icons.EXTENDED16));
    }

    public void testPrimaryElementLineMarkerModifies() throws Exception {
        myFixture.configureByFiles(WebtoperFacet.CONFIG_ROOT_NAME + "/PrimaryElementLineMarkerParent.xml", WebtoperFacet.CONFIG_ROOT_NAME + "/" + getTestName(false) + ".xml");
        doTest(new TextIconTuple(PRIMARY_ELEMENT_MODIFIED_TOOLTIP, Icons.MODIFIED16));
    }

    public void testPrimaryElementLineMarkerBoth() throws Exception {
        myFixture.configureByFiles(WebtoperFacet.CONFIG_ROOT_NAME + "/PrimaryElementLineMarkerParent.xml",
                WebtoperFacet.CONFIG_ROOT_NAME + "/PrimaryElementLineMarkerExtends.xml",
                WebtoperFacet.CONFIG_ROOT_NAME + "/PrimaryElementLineMarkerModifies.xml");
        doTest(
                new TextIconTuple(PRIMARY_ELEMENT_EXTENDED_TOOLTIP, Icons.EXTENDED16),
                new TextIconTuple(PRIMARY_ELEMENT_MODIFIED_TOOLTIP, Icons.MODIFIED16)
        );
    }
}
