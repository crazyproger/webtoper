package ru.crazyproger.plugins.webtoper.nls.psi;

import com.intellij.psi.StubBasedPsiElement;

/**
 * @author crazyproger
 */
public interface NlsIncludeProperty extends StubBasedPsiElement<NlsIncludePropertyStub> {

    NlsName[] getIncludedNames();

    NlsFileImpl getNlsFile();
}
