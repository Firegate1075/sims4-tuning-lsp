package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import jakarta.xml.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "TunableTuple")
@XmlAccessorType(XmlAccessType.FIELD)
public class TunableTuple implements ITuningDescriptionElement {
    // mandatory attributes

    @XmlAttribute(name = "class")
    private String className;

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

    public List<ITuningDescriptionElement> getTunableElements() {
        return tunableElements;
    }

}
