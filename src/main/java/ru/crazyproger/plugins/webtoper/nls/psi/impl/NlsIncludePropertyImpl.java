package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.parser.NlsElementTypes;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludeProperty;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsIncludePropertyStub;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsName;

/**
 * @author crazyproger
 */
public class NlsIncludePropertyImpl extends NlsStubElementImpl<NlsIncludePropertyStub> implements NlsIncludeProperty {

    public NlsIncludePropertyImpl(final NlsIncludePropertyStub stub) {
        super(stub, NlsElementTypes.INCLUDE_PROPERTY);
    }

    public NlsIncludePropertyImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public NlsName[] getIncludedNames() {
        return NlsName.EMPTY_ARRAY; // todo
    }

    @Override
    public NlsFileImpl getNlsFile() {
        return (NlsFileImpl) super.getContainingFile();
    }
}
