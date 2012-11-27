package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.xml.XmlTokenType;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.XmlPatterns.xmlTag;

/**
 * @author crazyproger
 */
public class XmlNlsReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        PsiElementPattern.Capture<PsiElement> xmlPattern = psiElement(XmlTokenType.XML_DATA_CHARACTERS).withSuperParent(2, xmlTag().withName(NlsXmlCompletionContributor.TAG_NAME));
        registrar.registerReferenceProvider(xmlPattern, new WholeElementTextRefProvider());
    }
}
