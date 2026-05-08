package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.InstanceElement;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.TdescFrag;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.TunableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

class TuningDescriptionRegistryTest {
    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final Path DEFAULT_TDESC_PATH = Paths.get(PROJECT_DIRECTORY + "/tdesc");

    @Test
    void addTuningDescription() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML(DEFAULT_TDESC_PATH);

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = new TuningDescriptionRegistry();

        // add all the tuning descriptions
        parsedTuningDescriptions.forEach(registry::addTuningDescription);
    }

    @Test
    void getInstanceElementByClassName() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML(DEFAULT_TDESC_PATH);

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = new TuningDescriptionRegistry();

        // add all the tuning descriptions
        parsedTuningDescriptions.forEach(registry::addTuningDescription);

        // get one example
        Optional<InstanceElement> buff_instance = registry.getInstanceElementByClassName("Buff");
        assert(buff_instance.isPresent());
        assert(buff_instance.get().getClassName().equals("Buff"));
    }

    @Test
    void getClassElementByPath() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML(DEFAULT_TDESC_PATH);

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = new TuningDescriptionRegistry();

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
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML(DEFAULT_TDESC_PATH);

        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = new TuningDescriptionRegistry();

        // add all the tuning descriptions
        parsedTuningDescriptions.forEach(registry::addTuningDescription);

        // get one example
        Optional<TdescFrag> tunableTestSet = registry.getTdescFragByClassName("TunableTestSet");
        assert(tunableTestSet.isPresent());
        assert(((TunableList) tunableTestSet.get().getTunableElements().getFirst()).getClassName().equals("TunableTestSet"));
    }
}