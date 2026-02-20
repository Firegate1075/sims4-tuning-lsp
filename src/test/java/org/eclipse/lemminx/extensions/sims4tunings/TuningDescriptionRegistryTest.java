package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.InstanceElement;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.TdescFrag;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.TunableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

class TuningDescriptionRegistryTest {
    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = TuningDescriptionRegistry.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void addTuningDescription() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = TuningDescriptionRegistry.getInstance();

        // add all the tuning descriptions
        parsedTuningDescriptions.forEach(registry::addTuningDescription);
    }

    @Test
    void getInstanceElementByClassName() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = TuningDescriptionRegistry.getInstance();

        // add all the tuning descriptions
        parsedTuningDescriptions.forEach(registry::addTuningDescription);

        // get one example
        Optional<InstanceElement> buff_instance = registry.getInstanceElementByClassName("Buff");
        assert(buff_instance.isPresent());
        assert(buff_instance.get().getClassName().equals("Buff"));
    }

    @Test
    void getClassElementByPath() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = TuningDescriptionRegistry.getInstance();

        parsedTuningDescriptions.forEach(registry::addTuningDescription);
        var r = registry.getClassElementByPath("statistics-statistic_conditions.HiddenOrShownCondition.Timing");
        assert(r.isPresent());
        assert(r.get().getName().equals("Timing"));
        r = registry.getClassElementByPath("ui-ui_dialog.UiDialogResponse.UiDialogUiRequest");
        assert(r.isPresent());
        assert(r.get().getName().equals("UiDialogUiRequest"));
        r = registry.getClassElementByPath("ui-ui_dialog_notification.UiDialogNotification.UiDialogNotificationLevel");
        assert(r.isPresent());
        assert(r.get().getName().equals("UiDialogNotificationLevel"));
        r = registry.getClassElementByPath("ui-ui_dialog_notification.UiDialogNotification.UiDialogNotificationAutoDeleteReason");
        assert(r.isPresent());
        assert(r.get().getName().equals("UiDialogNotificationAutoDeleteReason"));
    }

    @Test
    void getTdescFragByClassName() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = TuningDescriptionRegistry.getInstance();

        // add all the tuning descriptions
        parsedTuningDescriptions.forEach(registry::addTuningDescription);

        // get one example
        Optional<TdescFrag> tunableTestSet = registry.getTdescFragByClassName("TunableTestSet");
        assert(tunableTestSet.isPresent());
        assert(((TunableList) tunableTestSet.get().getTunableElements().getFirst()).getClassName().equals("TunableTestSet"));
    }
}