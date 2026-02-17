package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class ModuleElement {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // sub elements

    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<ITuningDescriptionElement> tunableElements = new ArrayList<>();

    public List<ITuningDescriptionElement> getTunableElements() {
        return tunableElements;
    }
}
