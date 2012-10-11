package ru.crazyproger.plugins.webtoper.config.ui;

import ru.crazyproger.plugins.webtoper.config.ProjectConfig;

import javax.swing.*;

/**
 * User: crazyproger<br/>
 * Date: 10/11/12
 */
public class ProjectSettingsPanel {
    private JTextField tfFolders;
    private JPanel rootPanel;

    public void setData(ProjectConfig data) {
        tfFolders.setText(data.getFoldersAsString());
    }

    public void getData(ProjectConfig data) {
        data.setFoldersAsString(tfFolders.getText());
    }

    public boolean isModified(ProjectConfig data) {
        if (tfFolders.getText() != null ? !tfFolders.getText().equals(data.getFoldersAsString()) : data.getFoldersAsString() != null)
            return true;
        return false;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
