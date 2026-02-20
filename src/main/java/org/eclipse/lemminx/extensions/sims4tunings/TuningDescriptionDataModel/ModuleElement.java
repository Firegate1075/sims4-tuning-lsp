package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Module")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleElement implements ITuningDescriptionElement, IHasName, ITunable {
    @XmlAttribute
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public List<ITuningDescriptionElement> getTunableElements() {
        return tunableElements;
    }

    @Override
    public String getTunableTag() {
        return "M";
    }
}
