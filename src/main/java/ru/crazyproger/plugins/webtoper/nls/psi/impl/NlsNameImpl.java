package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsName;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsNameStub;

/**
 * @author crazyproger
 */
public class NlsNameImpl extends NlsStubElementImpl<NlsNameStub> implements NlsName {
    public NlsNameImpl(@NotNull NlsNameStub stub) {
        super(stub, NlsElementTypes.NLS_NAME);
    }

    public NlsNameImpl(@NotNull ASTNode node) {
        super(node);
    }
}
