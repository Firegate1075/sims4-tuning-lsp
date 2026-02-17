package org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel;


import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({
        ClassElement.class,
        DeletedElement.class,
        EnumItem.class,
        InstanceElement.class,
        ModuleElement.class,
        TdescFrag.class,
        TdescFragTag.class,
        Tunable.class,
        TunableEnum.class,
        TunableList.class,
        TunableTuple.class,
        TunableVariant.class,
        TuningRoot.class
})
public interface ITuningDescriptionElement {

}
