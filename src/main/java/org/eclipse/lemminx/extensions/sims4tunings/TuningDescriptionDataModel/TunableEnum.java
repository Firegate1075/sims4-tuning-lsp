package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import javax.annotation.Nullable;
import java.util.Optional;

@XmlRootElement(name = "TunableEnum")
@XmlAccessorType(XmlAccessType.FIELD)
public class TunableEnum implements ITuningDescriptionElement, IHasClass, IHasOptionalName, IHasOptionalDisplay, IHasOptionalDescription, ITunable, IHasType {
    // mandatory attributes

    @XmlAttribute
    private String type;

    @XmlAttribute(name = "class")
    private String className;

    @XmlAttribute(name = "default")
    private String defaultValue;

    @XmlAttribute(name = "static_entries")
    private String staticEntries;

    // optional attributes

    @Nullable
    @XmlAttribute
    private String name;

    @Nullable
    @XmlAttribute
    private String display;

    @Nullable
    @XmlAttribute
    private String description;

    @Nullable
    @XmlAttribute(name = "Deprecated")
    private Boolean deprecated;

    @Nullable
    @XmlAttribute
    private String group;

    @Nullable
    @XmlAttribute(name = "pack_safe")
    private Boolean packSafe;

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

    public String getStaticEntries() {
        return staticEntries;
    }

    public void setStaticEntries(String staticEntries) {
        this.staticEntries = staticEntries;
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

    public Optional<Boolean> getPackSafe() {
        return Optional.ofNullable(packSafe);
    }

    public void setPackSafe(@Nullable Boolean packSafe) {
        this.packSafe = packSafe;
    }

    public String getTunableTag() {
        return "E";
    }
}
