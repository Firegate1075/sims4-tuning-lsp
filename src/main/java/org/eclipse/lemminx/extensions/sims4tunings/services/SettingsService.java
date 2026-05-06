package org.eclipse.lemminx.extensions.sims4tunings.services;

// TODO: make vscode extension, that registers the settings in vscode

import org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel.RootSettings;
import org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel.Sims4TuningSettings;
import org.eclipse.lemminx.utils.JSONUtility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * This class is used to store the settings of the extension.
 */
public class SettingsService {
    private static final Logger LOGGER = Logger.getLogger(SettingsService.class.getName());

    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    Path DEFAULT_TDESC_PATH = Paths.get(PROJECT_DIRECTORY + "/tdesc");

    private Sims4TuningSettings settings;

    /**
     * Updates the settings of the extension from the given XML settings object.
     * @param xmlSettings the XML settings object.
     */
    public void updateSettings(Object xmlSettings) {
        RootSettings rootSettings = JSONUtility.toModel(xmlSettings, RootSettings.class);
        if (rootSettings != null) {
            settings = JSONUtility.toModel(rootSettings.getSims4tunings(), Sims4TuningSettings.class);
        }
    }

    /**
     * Returns the path to the tuning description directory.
     * @return the path to the tuning description directory.
     */
    public Path getTdescPath() {
        if (settings != null && settings.getTdescPath() != null) {
            return Paths.get(settings.getTdescPath());
        } else {
            return DEFAULT_TDESC_PATH;
        }
    }
}
