package org.eclipse.lemminx.extensions.sims4tunings.services;

import org.eclipse.lemminx.extensions.sims4tunings.util.TuningDescriptionParser;
import org.eclipse.lemminx.extensions.sims4tunings.repository.TuningDescriptionRegistry;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.*;

import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TuningDescriptionService implements ISettingsObserver {
    private static final Logger LOGGER = Logger.getLogger(TuningDescriptionService.class.getName());

    private TuningDescriptionRegistry tdesc_registry;
    private boolean isInitialized = false;
    private Path tdescPath;

    private final ArrayList<ITuningDescriptionObserver> observers =  new ArrayList<>();

    private static TuningDescriptionService instance;

    private TuningDescriptionService(TuningDescriptionRegistry tdesc_registry) {
        this.tdesc_registry = tdesc_registry;
        SettingsService.getSingletonInstance().registerObserver(this);
    }

    public static TuningDescriptionService createSingletonInstance(TuningDescriptionRegistry tdesc_registry) throws IllegalStateException {
        if (instance == null) {
            synchronized (TuningDescriptionService.class) {
                instance = new TuningDescriptionService(tdesc_registry);
                return instance;
            }
        } else {
            throw new IllegalStateException("TuningDescriptionService is already initialized.");
        }
    }

    /**
     * Get the singleton instance of the service.
     * @return the instance of the TuningDescriptionService.
     */
    public static TuningDescriptionService getSingletonInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("TuningDescriptionService is not initialized.");
        }
        return instance;
    }



    private void readTuningDescriptions() {
        try {
            List<TuningRoot> tuningRoots = TuningDescriptionParser.parseTuningDescriptionXML(SettingsService.getSingletonInstance().getTdescPath());

            // if successful, update the registry
            tdesc_registry.clear();
            tuningRoots.forEach(tdesc_registry::addTuningDescription);

            isInitialized = true;
            observers.forEach(ITuningDescriptionObserver::onTuningDescriptionInitialized);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error reading Tuning Descriptions from the specified path: " + SettingsService.getSingletonInstance().getTdescPath(), e);
        }
    }

    @Override
    public void onSettingsUpdate() {
        Path path = SettingsService.getSingletonInstance().getTdescPath();
        if (!path.equals(this.tdescPath)) {
            // update the path
            this.tdescPath = path;
            readTuningDescriptions();
        }
    }

    /**
     * Registers an observer for the updates of the tuning description registry.
     * @param tuningDescriptionObserver the observer to add.
     */
    public void registerObserver(ITuningDescriptionObserver tuningDescriptionObserver) {
        observers.add(tuningDescriptionObserver);
    }

    /**
     * Unregisters a tuning description registry observer.
     * @param tuningDescriptionObserver the observer to remove.
     */
    public void unregisterObserver(ITuningDescriptionObserver tuningDescriptionObserver) {
        observers.remove(tuningDescriptionObserver);
    }

    /**
     * Returns whether the tuning descriptions have been parsed.
     * @return true if the tuning descriptions have been parsed.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Retrieves the tuning description of an instance element by the class name string.
     * @param className the "class" attribute of the instance element
     * @return optional of the tuning description
     */
    public Optional<InstanceElement> getInstanceElementByClassName(String className) {
        return tdesc_registry.getInstanceElementByClassName(className);
    }

    /**
     * Retrieves the tuning description of an instance element by its path.
     * The  path is a string [moduleName].[className]..., where moduleName is the name of the module with dots replaced by dashes
     * @param path the "class" attribute of the instance element
     * @return optional of the tuning description
     */
    public Optional<ClassElement> getClassElementByPath(String path) {
        return tdesc_registry.getClassElementByPath(path);
    }

    /**
     * Retrieves the tuning description of a TdescFrag element by the class name string.
     * @param className the "class" attribute of the TdescFrag element
     * @return optional of the tuning description
     */
    public Optional<TdescFrag> getTdescFragByClassName(String className) {
        return tdesc_registry.getTdescFragByClassName(className);
    }

    /**
     * Retrieves the tuning description of a Module element by the name string.
     * @param moduleName the "name" attribute of the TdescFrag element
     * @return optional of the tuning description
     */
    public Optional<ModuleElement> getModuleElementByName(String moduleName) {
        return tdesc_registry.getModuleElementByName(moduleName);
    }

    /**
     * Gets a list of all class names of the instance element descriptions.
     * @return a list of names.
     */
    public List<String> getClassNamesOfInstanceElementEntries() {
        return tdesc_registry.getClassNamesOfInstanceElementEntries();
    }
}
