package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import java.util.List;

public interface IHasChildren {
    List<ITuningDescriptionElement> getTunableElements();
}
