package ru.crazyproger.plugins.webtoper.nls.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;

/**
 * @author crazyproger
 */
public class NlsStubElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> {
    public NlsStubElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NlsStubElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public Language getLanguage() {
        return NlsLanguage.INSTANCE;
    }
}
