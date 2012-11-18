package ru.crazyproger.plugins.webtoper.nls;

import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.psi.FileViewProvider;

/**
 * @author crazyproger
 */
public class NlsFileImpl extends PropertiesFileImpl {

    public NlsFileImpl(FileViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    public String toString() {
        return "Nls file: " + getName();
    }
}
