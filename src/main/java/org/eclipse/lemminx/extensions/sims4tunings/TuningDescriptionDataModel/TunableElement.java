package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public abstract class TunableElement {
    @JacksonXmlProperty(isAttribute = true)
    String name;
    @JacksonXmlProperty(isAttribute = true, localName = "class")
    String className;
    @JacksonXmlProperty(isAttribute = true)
    String display;
    @JacksonXmlProperty(isAttribute = true)
    String description;
    @JacksonXmlProperty(isAttribute = true, localName = "Deprecated")
    boolean deprecated;
    @JacksonXmlProperty(isAttribute = true)
    Integer filter;
    @JacksonXmlProperty(isAttribute = true)
    String group;
}
