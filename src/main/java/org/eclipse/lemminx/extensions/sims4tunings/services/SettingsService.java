package org.eclipse.lemminx.extensions.sims4tunings.services;

// TODO: make vscode extension, that registers the settings in vscode

import org.eclipse.lemminx.extensions.sims4tunings.ISettingsObserver;
import org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel.RootSettings;
import org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel.Sims4TuningSettings;
import org.eclipse.lemminx.utils.JSONUtility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class is used to store the settings of the extension.
 */
public class SettingsService {
    private static final Logger LOGGER = Logger.getLogger(SettingsService.class.getName());

    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final Path DEFAULT_TDESC_PATH = Paths.get(PROJECT_DIRECTORY + "/tdesc");

    private Sims4TuningSettings settings;
    private final ArrayList<ISettingsObserver> observers = new ArrayList<>();

    private static SettingsService instance;

    private SettingsService() { }

    /**
     * Get the singleton instance of the service.
     * @return the instance of the SettingsService.
     */
    public static SettingsService getSingletonInstance() {
        if (instance == null) {
            synchronized (SettingsService.class) {
                instance = new SettingsService();
            }
        }
        return instance;
    }

    /**
     * Updates the settings of the extension from the given XML settings object.
     * Also notifies all the observers.
     * @param xmlSettings the XML settings object.
     */
    public void updateSettings(Object xmlSettings) {
        LOGGER.info("updating root settings");
        RootSettings rootSettings = JSONUtility.toModel(xmlSettings, RootSettings.class);
        if (rootSettings != null) {
            LOGGER.info("updating sims4tuning settings");
            settings = JSONUtility.toModel(rootSettings.getSims4tunings(), Sims4TuningSettings.class);
        }
        observers.forEach(ISettingsObserver::onSettingsUpdate);
    }

    /**
     * Returns the path to the tuning description directory.
     * @return the path to the tuning description directory.
     */
    public Path getTdescPath() {
        LOGGER.info("retrieving tdesc path");
        if (settings != null && settings.getTdescPath() != null) {
            return Paths.get(settings.getTdescPath());
        } else {
            LOGGER.info("no tdesc path found, using default");
            return DEFAULT_TDESC_PATH;
        }
    }

    /**
     * Registers an observer for updates of settings.
     * @param settingsObserver the observer to add.
     */
    public void registerObserver(ISettingsObserver settingsObserver) {
        observers.add(settingsObserver);
    }

    /**
     * Unregisters a settings observer.
     * @param settingsObserver the observer to remove.
     */
    public void unregisterObserver(ISettingsObserver settingsObserver) {
        observers.remove(settingsObserver);
    }
}
