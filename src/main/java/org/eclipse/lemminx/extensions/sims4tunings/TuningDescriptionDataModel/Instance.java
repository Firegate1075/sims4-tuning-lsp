package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Instance {
    // mandatory attributes

    @JacksonXmlProperty(isAttribute = true, localName = "instance_type")
    private String instanceType;

    @JacksonXmlProperty(isAttribute = true, localName = "class")
    private String className;

    @JacksonXmlProperty(isAttribute = true, localName = "module")
    private String moduleName;

    // optional attributes

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "instance_subclasses_only")
    private Boolean instanceSubclassesOnly;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String parents;


    // sub elements

    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<ITuningDescriptionElement> tunableElements = new ArrayList<>();


    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public Optional<Boolean> getInstanceSubclassesOnly() {
        return Optional.ofNullable(instanceSubclassesOnly);
    }

    public void setInstanceSubclassesOnly(@Nullable Boolean instanceSubclassesOnly) {
        this.instanceSubclassesOnly = instanceSubclassesOnly;
    }

    public Optional<String> getParents() {
        return Optional.ofNullable(parents);
    }

    public void setParents(@Nullable String parents) {
        this.parents = parents;
    }

    public List<ITuningDescriptionElement> getTunableElements() {
        return tunableElements;
    }
}
