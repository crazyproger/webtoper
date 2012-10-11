package ru.crazyproger.plugins.webtoper.config;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.config.ui.ProjectSettingsPanel;

import javax.swing.*;

/**
 * User: crazyproger<br/>
 */
public class ProjectConfigConfigurable
        implements SearchableConfigurable {

    private final Project project;
    private final ProjectConfig configuration;
    private ProjectSettingsPanel settingsPanel;


    public ProjectConfigConfigurable(final Project project) {
        this.project = project;
        this.configuration = ServiceManager.getService(this.project, ProjectConfig.class);
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
        if (settingsPanel == null) {
            settingsPanel = new ProjectSettingsPanel();
        }
        return settingsPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return settingsPanel.isModified(configuration);
    }

    @Override
    public void apply() throws ConfigurationException {
        settingsPanel.getData(configuration);
    }

    @Override
    public void reset() {
        settingsPanel.setData(configuration);
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }
}