package ru.crazyproger.plugins.webtoper.component;

import ru.crazyproger.plugins.webtoper.LineMarkerTestCase;
import ru.crazyproger.plugins.webtoper.config.Icons;

/**
 * @see ClassLineMarkerProvider
 */
public class ClassLineMarkerTest extends LineMarkerTestCase {

    // this test was created after ClassLineMarkerProvider was implemented :)
    public static final String TOOLTIP = "<html><body>Used in:<br>&nbsp;&nbsp;&nbsp;&nbsp;ClassLineMarker.xml<br></body></html>";

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/component";
    }

    public void testClassLineMarker() throws Exception {
        myFixture.configureByFiles(getTestName(false) + ".java", getTestName(false) + ".xml");
        doTest(new TextIconTuple(TOOLTIP, Icons.C16), new TextIconTuple(TOOLTIP, Icons.W16));
    }
}
