package ru.crazyproger.plugins.webtoper.nls.parser;

import com.intellij.lang.PsiParser;
import com.intellij.lang.properties.parsing.PropertiesParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsLanguage;
import ru.crazyproger.plugins.webtoper.nls.psi.NlsFileImpl;

/**
 * @author crazyproger
 */
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
}
