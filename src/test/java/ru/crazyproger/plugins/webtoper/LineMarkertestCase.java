package ru.crazyproger.plugins.webtoper;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.GutterIconRenderer;

import javax.swing.Icon;
import java.util.List;

public abstract class LineMarkertestCase extends WebtoperLightFixtureTestCase {

    protected void doTest(TextIconTuple... texts) {
        List<LineMarkerInfo> infos = doHighlighting();
        assertEquals(texts.length, infos.size());
        for (int i = 0; i < infos.size(); i++) {
            LineMarkerInfo markerInfo = infos.get(i);
            checkMarker(texts[i].tooltip, texts[i].icon, markerInfo);
        }
    }

    protected void doTest(String... strings) {
        List<LineMarkerInfo> infos = doHighlighting();
        assertEquals(strings.length, infos.size());
        for (int i = 0; i < infos.size(); i++) {
            LineMarkerInfo markerInfo = infos.get(i);
            checkMarker(strings[i], null, markerInfo);
        }
    }

    private List<LineMarkerInfo> doHighlighting() {
        myFixture.doHighlighting();
        Document document = myFixture.getEditor().getDocument();
        List<LineMarkerInfo> infoList = DaemonCodeAnalyzerImpl.getLineMarkers(document, myFixture.getProject());
        assertNotNull(infoList);
        return infoList;
    }

    private void checkMarker(String tooltip, Icon expectedIcon, LineMarkerInfo markerInfo) {
        assertNotNull(markerInfo);
        String markerTooltip = markerInfo.getLineMarkerTooltip();
        assertEquals(tooltip, markerTooltip);
        if (expectedIcon != null) {
            GutterIconRenderer renderer = markerInfo.createGutterRenderer();
            assertNotNull(renderer);
            assertEquals(expectedIcon, renderer.getIcon());
        }
    }

    protected class TextIconTuple {
        protected String tooltip;
        protected Icon icon;

        public TextIconTuple(String tooltip, Icon icon) {
            this.tooltip = tooltip;
            this.icon = icon;
        }
    }
}
