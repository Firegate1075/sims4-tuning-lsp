package org.eclipse.lemminx.extensions.sims4tunings.services;

// TODO: make vscode extension, that registers the settings in vscode

/**
 * This class is used to store the settings of the extension.
 */
public class SettingsService {

    private String tuningDescriptionPath;

    /**
     * Retuns the path to the tuning description directory.
     * @return the path that contains the tuning description files.
     */
    public String getTuningDescriptionPath() {
        return tuningDescriptionPath;
    }

    /**
     * Sets the path to the tuning description directory.
     * @param tuningDescriptionPath the path that contains the tuning description files.
     */
    public void setTuningDescriptionPath(String tuningDescriptionPath) {
        this.tuningDescriptionPath = tuningDescriptionPath;
    }
}
