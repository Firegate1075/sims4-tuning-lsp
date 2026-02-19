package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import javax.annotation.Nullable;
import java.util.Optional;

@XmlRootElement(name = "TdescFragTag")
@XmlAccessorType(XmlAccessType.FIELD)
public class TdescFragTag implements ITuningDescriptionElement, IHasClass, IHasDescription, IHasOptionalName{
    // mandatory attributes

    @XmlAttribute(name = "class")
    private String className;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private String group;

    // optional attributes

    @Nullable
    @XmlAttribute
    private String name;

    @Nullable
    @XmlAttribute
    private String display;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


}
