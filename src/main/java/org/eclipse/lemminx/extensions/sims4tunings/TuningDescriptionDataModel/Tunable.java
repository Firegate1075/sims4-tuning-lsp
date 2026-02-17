package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.annotation.Nullable;
import java.util.Optional;

public class Tunable implements ITunableElement{
    // mandatory attributes

    @JacksonXmlProperty(isAttribute = true, localName = "class")
    String className;

    // optional attributes

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String display;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "default")
    private String defaultValue;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "needs_tuning")
    private Boolean needsTuning;

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "Deprecated")
    private Boolean deprecated;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String group;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String min;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String max;

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "allow_none")
    private Boolean allowNone;

    @Nullable
    @JacksonXmlProperty(isAttribute = true, localName = "pack_safe")
    private Boolean packSafe;

    @Nullable
    @JacksonXmlProperty(isAttribute = true)
    private String restrict;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public void setDefaultValue(@Nullable String defaultValue) {
        this.defaultValue = defaultValue;
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

    public Optional<Boolean> getNeedsTuning() {
        return Optional.ofNullable(needsTuning);
    }

    public void setNeedsTuning(@Nullable Boolean needsTuning) {
        this.needsTuning = needsTuning;
    }

    public Optional<String> getMin() {
        return Optional.ofNullable(min);
    }

    public void setMin(@Nullable String min) {
        this.min = min;
    }

    public Optional<String> getMax() {
        return Optional.ofNullable(max);
    }

    public void setMax(@Nullable String max) {
        this.max = max;
    }

    public Optional<Boolean> getAllowNone() {
        return Optional.ofNullable(allowNone);
    }

    public void setAllowNone(@Nullable Boolean allowNone) {
        this.allowNone = allowNone;
    }

    public Optional<Boolean> getPackSafe() {
        return Optional.ofNullable(packSafe);
    }

    public void setPackSafe(@Nullable Boolean packSafe) {
        this.packSafe = packSafe;
    }

    public Optional<String> getRestrict() {
        return Optional.ofNullable(restrict);
    }

    public void setRestrict(@Nullable String restrict) {
        this.restrict = restrict;
    }

}
