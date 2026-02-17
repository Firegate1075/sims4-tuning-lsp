package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.annotation.Nullable;
import java.util.Optional;

public class TunableVariant implements ITunableElement {
    // mandatory attributes

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true, localName = "class")
    private String className;

    @JacksonXmlProperty(isAttribute = true, localName = "default")
    private String defaultValue;

    @JacksonXmlProperty(isAttribute = true, localName = "Deprecated")
    private boolean deprecated;

    @JacksonXmlProperty(isAttribute = true)
    private String group;

    // optional attributes

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String display;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public Optional<String> getDisplay() {
        return Optional.ofNullable(display);
    }

    public void setDisplay(@Nullable String display) {
        this.display = display;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
