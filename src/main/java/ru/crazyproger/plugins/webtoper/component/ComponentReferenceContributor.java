/*
 * Copyright 2012 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.crazyproger.plugins.webtoper.component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.XmlAttributeValuePattern;
import com.intellij.patterns.XmlPatterns;
import com.intellij.patterns.XmlTagPattern;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.patterns.XmlPatterns.xmlTag;

/**
 * @author crazyproger
 *         todo refactoring
 */
public class ComponentReferenceContributor extends PsiReferenceContributor {
    public static final Pattern EXTENDS_PATTERN = Pattern.compile("\"\\s*(\\w+)\\s*:\\s*/?(.+)\\s*\"");
    public static final XmlTagPattern.Capture COMPONENT_CAPTURE = xmlTag().withName("component").withParent(
            xmlTag().withName("scope"));

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        XmlAttributeValuePattern extendsPattern = XmlPatterns.xmlAttributeValue().withLocalName("extends")
                .withSuperParent(2, COMPONENT_CAPTURE);
        XmlAttributeValuePattern modifiesPattern = XmlPatterns.xmlAttributeValue().withLocalName("modifies")
                .withSuperParent(2, COMPONENT_CAPTURE);
        ComponentReferenceProvider provider = new ComponentReferenceProvider();
        registrar.registerReferenceProvider(extendsPattern, provider);
        registrar.registerReferenceProvider(modifiesPattern, provider);
    }

    private List<XmlTag> findComponentTags(String componentId, Collection<XmlTag> tags) {
        List<XmlTag> componentTags = new ArrayList<XmlTag>();
        for (XmlTag tag : tags) {
            if ("component".equals(tag.getName())) {
                final String id = tag.getAttributeValue("id", null);
                if (componentId.equals(id)) {
                    componentTags.add(tag);
                }
            }
        }
        return componentTags;
    }

    private class ComponentReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull final PsiElement element, @NotNull ProcessingContext context) {
            String text = element.getText();
            Matcher matcher = EXTENDS_PATTERN.matcher(text);
            if (!matcher.matches()) return PsiReference.EMPTY_ARRAY;

            final Project project = element.getProject();
            String configPath = matcher.group(2);
            VirtualFile config = Utils.findFileInArtifact(configPath, project);
            if (config == null) return PsiReference.EMPTY_ARRAY;

            final PsiFile file;
            file = PsiManager.getInstance(project).findFile(config);
            if (!(file instanceof XmlFile)) return PsiReference.EMPTY_ARRAY;

            String componentId = matcher.group(1);
            final Collection<XmlTag> tags = PsiTreeUtil.findChildrenOfType(file, XmlTag.class);
            List<XmlTag> componentTags = findComponentTags(componentId, tags);
            if (componentTags.isEmpty()) return PsiReference.EMPTY_ARRAY;

            Collection<PsiReference> references = Collections2.transform(componentTags, new Function<XmlTag, PsiReference>() {
                @Override
                public PsiReference apply(@Nullable XmlTag xmlTag) {
                    return new PsiReferenceBase.Immediate<PsiElement>(element, xmlTag);
                }
            });
            return references.toArray(new PsiReference[references.size()]);
        }
    }
}
