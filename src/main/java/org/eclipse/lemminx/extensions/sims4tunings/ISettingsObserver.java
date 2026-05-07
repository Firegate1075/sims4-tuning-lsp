package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel.Sims4TuningSettings;

public interface ISettingsObserver {
    void onSettingsUpdate(Sims4TuningSettings sims4TuningSettings);
}
