package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class TuningDescriptionRegistry {
    private static final Logger LOGGER = Logger.getLogger(TuningDescriptionRegistry.class.getName());

    // map that stores a tuning description element (i.e. ClassElement, InstanceElement or TdescFrag) for a given className string
    private final Map<String, ITuningDescriptionElement> tuningDescriptionElementMap = HashMap.newHashMap(1764);

    public void addTuningDescriptionElement(ITuningDescriptionElement element) {
        String className = "";

        if (element instanceof InstanceElement instanceElement) {
            className = instanceElement.getClassName();
        } else if (element instanceof ClassElement classElement) {
            className = classElement.getName();
        } else if (element instanceof TdescFrag tdescFrag) {
            ITuningDescriptionElement child = tdescFrag.getTunableElements().getFirst();
            switch (child) {
                case TunableVariant tunableVariant -> className = tunableVariant.getClassName();
                case TunableList tunableList -> className = tunableList.getClassName();
                case TunableTuple tunableTuple -> className = tunableTuple.getClassName();
                default ->
                        LOGGER.warning("Unexpected child element of type" + child.getClass() + " for TdescFrag " + tdescFrag);
            }
        }

        if (!className.isEmpty()) {
            tuningDescriptionElementMap.put(className, element);
        }
    }

    public Optional<ITuningDescriptionElement> getByClassName(String className) {
        return Optional.ofNullable(tuningDescriptionElementMap.get(className));
    }
}
