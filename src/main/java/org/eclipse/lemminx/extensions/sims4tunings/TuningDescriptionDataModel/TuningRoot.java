package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class TuningRoot {

    // sub elements

    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<ITuningDescriptionElement> tunableElements = new ArrayList<>();

    public List<ITuningDescriptionElement> getTunableElements() {
        return tunableElements;
    }
}
