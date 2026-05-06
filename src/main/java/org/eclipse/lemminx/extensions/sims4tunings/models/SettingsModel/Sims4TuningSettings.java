package org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel;

/**
 * Model for xml.sims4tunings settings.
 */
public class Sims4TuningSettings {
    // path to the tuning description file directory
    private String tdescPath;

    /**
     * Retuns the path to the tuning description directory.
     * @return the path that contains the tuning description files.
     */
    public String getTdescPath() {
        return tdescPath;
    }

    /**
     * Sets the path to the tuning description directory.
     * @param tdescPath the path that contains the tuning description files.
     */
    public void setTdescPath(String tdescPath) {
        this.tdescPath = tdescPath;
    }
}
