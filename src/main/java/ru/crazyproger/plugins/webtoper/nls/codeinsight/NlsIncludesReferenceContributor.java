package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * @author crazyproger
 */
public class NlsIncludesReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        PsiElementPattern.Capture<PsiElement> nlsIncludesPattern = psiElement(PsiElement.class);
        registrar.registerReferenceProvider(nlsIncludesPattern, new WholeElementTextRefProvider());
    }
}
