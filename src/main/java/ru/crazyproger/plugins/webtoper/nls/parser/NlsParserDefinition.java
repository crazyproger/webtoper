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

package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lang.properties.parsing.PropertiesParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludePropertyImpl;
import ru.crazyproger.plugins.webtoper.nls.psi.impl.NlsIncludesListImpl;

public class NlsParserDefinition extends PropertiesParserDefinition {
    public static final IStubFileElementType NLS_FILE_NODE_TYPE = new IStubFileElementType("NLS.file", NlsLanguage.INSTANCE);

    @Override
    public IFileElementType getFileNodeType() {
        return NLS_FILE_NODE_TYPE;
    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new NlsLexer();
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new NlsParser();
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new NlsFileImpl(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        final IElementType type = node.getElementType();
        if (type == NlsElementTypes.INCLUDES_LIST) {
            return new NlsIncludesListImpl(node);
        } else if (type == NlsElementTypes.INCLUDE_PROPERTY) {
            return new NlsIncludePropertyImpl(node);
        }
        return super.createElement(node);
    }
}
