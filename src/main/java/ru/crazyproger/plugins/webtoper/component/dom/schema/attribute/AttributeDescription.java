package ru.crazyproger.plugins.webtoper.component.dom.schema.attribute;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.SubTag;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.CustomChildrenContainer;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.InvocationDescriptionContainer;

public interface AttributeDescription extends Attribute, CustomChildrenContainer, InvocationDescriptionContainer {

    @com.intellij.util.xml.Attribute
    GenericAttributeValue<String> getType();

    @com.intellij.util.xml.Attribute("repeatingonly")
    GenericAttributeValue<Boolean> isRepeatingOnly();

    @com.intellij.util.xml.Attribute("singleonly")
    GenericAttributeValue<Boolean> isSingleOnly();

    @SubTag("valuehandler")
    GenericDomValue<PsiClass> getValueHandler();

    @SubTag("valueformatter")
    GenericDomValue<PsiClass> getValueFormatter();

    @SubTag("tagclass")
    GenericDomValue<PsiClass> getTagClass();

    @SubTag("labeltagclass")
    GenericDomValue<PsiClass> getLabelClass();

    @SubTag("valuetagclass")
    GenericDomValue<PsiClass> getValueTagClass();

    @SubTag("editcomponent")
    GenericDomValue<String> getEditComponentName();

}
