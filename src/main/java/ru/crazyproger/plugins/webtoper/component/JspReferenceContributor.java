/*
 * Copyright 2013 Vladimir Rudev
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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.XmlTagPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.jsp.JspFile;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.Utils;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.XmlPatterns.xmlTag;

/**
 * @author crazyproger
 */
public class JspReferenceContributor extends PsiReferenceContributor {

    public static final XmlTagPattern.Capture COMPONENT_CAPTURE = xmlTag().withName("component").withParent(
            xmlTag().withName("scope"));
    public static final PsiElementPattern.Capture<PsiElement> JSP_CAPTURE = psiElement(XmlTokenType.XML_DATA_CHARACTERS)
            .inside(COMPONENT_CAPTURE);

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(JSP_CAPTURE, new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                final String text = StringUtils.trimToEmpty(element.getText());
                if (!text.endsWith(".jsp")) return PsiReference.EMPTY_ARRAY;

                final Project project = element.getProject();
                final VirtualFile virtualFile = Utils.findFileInArtifact(text, project);
                if (virtualFile == null) return PsiReference.EMPTY_ARRAY;

                final PsiFile file;
                file = PsiManager.getInstance(project).findFile(virtualFile);
                if (!(file instanceof JspFile)) return PsiReference.EMPTY_ARRAY;

                return new PsiReference[]{new PsiReferenceBase.Immediate<PsiElement>(element, file)};
            }
        });
    }
}
