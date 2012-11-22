package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesList;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludesListStub;

/**
 * @author crazyproger
 */
public class NlsIncludesListImpl extends NlsStubElementImpl<NlsIncludesListStub> implements NlsIncludesList {
    public NlsIncludesListImpl(final NlsIncludesListStub stub) {
        super(stub, NlsElementTypes.INCLUDES_LIST);
    }

    public NlsIncludesListImpl(@NotNull ASTNode node) {
        super(node);
    }
}
