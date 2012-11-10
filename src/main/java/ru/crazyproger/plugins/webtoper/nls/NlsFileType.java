package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.icons.AllIcons;
import com.intellij.lang.properties.PropertiesFileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * todo remove this class?
 *
 * @author crazyproger
 */
public class NlsFileType extends LanguageFileType {
    public static final LanguageFileType INSTANCE = new NlsFileType();

    private NlsFileType() {
        super(NlsLanguage.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "Nls properties";
    }

    @NotNull
    public String getDescription() {
        return NlsBundle.message("nls.files.file.type.description");
    }

    @NotNull
    public String getDefaultExtension() {
        return PropertiesFileType.DEFAULT_EXTENSION;
    }

    public Icon getIcon() {
        return AllIcons.FileTypes.Properties;
    }

    public String getCharset(@NotNull VirtualFile file, final byte[] content) {
        return PropertiesFileType.INSTANCE.getCharset(file, content);
    }
}
