package ru.crazyproger.plugins.webtoper.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * User: crazyproger<br/>
 */
public class ProjectConfigConfigurable
        implements SearchableConfigurable {

    private final Project project;

    public ProjectConfigConfigurable(final Project project) {
        this.project = project;
    }

    @NotNull
    @Override
    public String getId() {
        return "Webtoper.ProjectConfig";
    }

    @Override
    public Runnable enableSearch(final String option) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Webtoper";
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
    }
}