package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import jakarta.xml.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "Instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class InstanceElement implements ITuningDescriptionElement, IHasClass{
    // mandatory attributes

    @XmlAttribute(name = "instance_type")
    private String instanceType;

    @XmlAttribute(name = "class")
    private String className;

    @XmlAttribute(name = "module")
    private String moduleName;

    // optional attributes

    @Nullable
    @XmlAttribute
    private String description;

    @Nullable
    @XmlAttribute(name = "instance_subclasses_only")
    private Boolean instanceSubclassesOnly;

    @Nullable
    @XmlAttribute
    private String parents;


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
