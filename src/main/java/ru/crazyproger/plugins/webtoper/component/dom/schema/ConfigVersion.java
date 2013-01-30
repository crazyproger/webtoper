package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.NamedEnum;

/**
 * @author crazyproger
 */
public enum ConfigVersion implements NamedEnum {
    VERSION_1_0("1.0");

    private ConfigVersion(String value) {
        this.value = value;
    }

    private String value;

    @Override
    public String getValue() {
        return value;
    }
}
