package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Instance {
    @JacksonXmlProperty(isAttribute = true, localName = "instance_type")
    String instanceType;
    @JacksonXmlProperty(isAttribute = true, localName = "class")
    String className;
    @JacksonXmlProperty(isAttribute = true, localName = "module")
    String moduleName;
    @JacksonXmlProperty(isAttribute = true)
    String description;
    @JacksonXmlProperty(isAttribute = true)
    String parents;

    @JacksonXmlElementWrapper(useWrapping = false)
    List<ITuningDescriptionElement> tunableElements;
}
