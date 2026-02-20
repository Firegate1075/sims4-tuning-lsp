package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import jakarta.xml.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "TunableVariant")
@XmlAccessorType(XmlAccessType.FIELD)
public class TunableVariant implements ITuningDescriptionElement, IHasClass, IHasOptionalName, IHasOptionalDisplay, IHasOptionalDescription, ITunable {
    // mandatory attributes

    @XmlAttribute
    private String type;

    @XmlAttribute(name = "class")
    private String className;

    @XmlAttribute(name = "default")
    private String defaultValue;

    @XmlAttribute(name = "Deprecated")
    private boolean deprecated;

    @XmlAttribute
    private String group;

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

    // sub elements

    @XmlElements({
            @XmlElement(name = "Class", type = ClassElement.class),
            @XmlElement(name = "Deleted", type = DeletedElement.class),
            @XmlElement(name = "EnumItem", type = EnumItem.class),
            @XmlElement(name = "Instance", type = InstanceElement.class),
            @XmlElement(name = "Module", type = ModuleElement.class),
            @XmlElement(name = "TdescFrag", type = TdescFrag.class),
            @XmlElement(name = "TdescFragTag", type = TdescFragTag.class),
            @XmlElement(name = "Tunable", type = Tunable.class),
            @XmlElement(name = "TunableEnum", type = TunableEnum.class),
            @XmlElement(name = "TunableList", type = TunableList.class),
            @XmlElement(name = "TunableTuple", type = TunableTuple.class),
            @XmlElement(name = "TunableVariant", type = TunableVariant.class)
    })
    private final List<ITuningDescriptionElement> tunableElements = new ArrayList<>();


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

    public List<ITuningDescriptionElement> getTunableElements() {
        return tunableElements;
    }

    public String getTunableTag() {
        return "V";
    }
}
