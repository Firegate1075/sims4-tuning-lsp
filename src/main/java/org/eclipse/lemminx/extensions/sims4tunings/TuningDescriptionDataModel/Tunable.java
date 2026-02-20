package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import javax.annotation.Nullable;
import java.util.Optional;

@XmlRootElement(name = "Tunable")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tunable implements ITuningDescriptionElement, IHasClass, IHasOptionalName, IHasOptionalDescription, IHasOptionalDisplay, ITunable, IHasOptionalType {
    // mandatory attributes

    @XmlAttribute(name = "class")
    String className;

    // optional attributes

    @Nullable
    @XmlAttribute
    private String name;

    @Nullable
    @XmlAttribute
    private String display;

    @Nullable
    @XmlAttribute
    private String type;

    @Nullable
    @XmlAttribute(name = "default")
    private String defaultValue;

    @Nullable
    @XmlAttribute
    private String description;

    @Nullable
    @XmlAttribute(name = "needs_tuning")
    private Boolean needsTuning;

    @Nullable
    @XmlAttribute(name = "Deprecated")
    private Boolean deprecated;

    @Nullable
    @XmlAttribute
    private String group;

    @Nullable
    @XmlAttribute
    private String min;

    @Nullable
    @XmlAttribute
    private String max;

    @Nullable
    @XmlAttribute(name = "allow_none")
    private Boolean allowNone;

    @Nullable
    @XmlAttribute(name = "pack_safe")
    private Boolean packSafe;

    @Nullable
    @XmlAttribute
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

    @Override
    public String getTunableTag() {
        return "T";
    }
}
