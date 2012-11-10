package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author crazyproger
 */
public class NlsCompletionContributor extends CompletionContributor {
    public NlsCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(XmlTokenType.XML_DATA_CHARACTERS), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                String test = "test";
                test = "b";
            }
        });
    }
}
