package ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * @author crazyproger
 */
public interface ClassAttributeContainer {
    @Required
    @Attribute("class")
    GenericAttributeValue<PsiClass> getClassValue();
}
