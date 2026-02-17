package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.annotation.Nullable;
import java.util.Optional;

public class TunableList implements ITunableElement{
    // mandatory attributes

    @JacksonXmlProperty(isAttribute = true, localName = "class")
    private String className;

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

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "Deprecated")
    private Boolean deprecated;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String group;

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "unique_entries")
    private Boolean uniqueEntries;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String max;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String min;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Optional<Boolean> isDeprecated() {
        return Optional.ofNullable(deprecated);
    }

    public void setDeprecated(@Nullable Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Optional<String> getGroup() {
        return Optional.ofNullable(group);
    }

    public void setGroup(@Nullable String group) {
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

    public Optional<Boolean> getUniqueEntries() {
        return Optional.ofNullable(uniqueEntries);
    }

    public void setUniqueEntries(@Nullable Boolean uniqueEntries) {
        this.uniqueEntries = uniqueEntries;
    }

    public Optional<String> getMax() {
        return Optional.ofNullable(max);
    }

    public void setMax(@Nullable String max) {
        this.max = max;
    }

    public Optional<String> getMin() {
        return Optional.ofNullable(min);
    }

    public void setMin(@Nullable String min) {
        this.min = min;
    }
}
