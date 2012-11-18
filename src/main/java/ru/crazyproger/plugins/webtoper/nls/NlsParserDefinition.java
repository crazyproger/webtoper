package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lang.properties.parsing.PropertiesParserDefinition;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author crazyproger
 */
public class NlsParserDefinition extends PropertiesParserDefinition {
    public static final IStubFileElementType NLS_FILE_NODE_TYPE = new IStubFileElementType("NLS.file", NlsLanguage.INSTANCE);

    @Override
    public IFileElementType getFileNodeType() {
        return NLS_FILE_NODE_TYPE;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new NlsFileImpl(viewProvider);
    }
}
