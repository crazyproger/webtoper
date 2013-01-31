package ru.crazyproger.plugins.webtoper.component.dom.schema;

import com.intellij.util.xml.NamedEnum;

/**
 * @author crazyproger
 */
public enum NavigatonType implements NamedEnum {
    JUMP("jump"),
    RETURN_JUMP("returnjump"),
    NESTED("nested");

    private final String value;

    private NavigatonType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
