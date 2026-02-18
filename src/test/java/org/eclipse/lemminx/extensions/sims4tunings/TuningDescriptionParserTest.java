package org.eclipse.lemminx.extensions.sims4tunings;

import org.junit.Test;

public class TuningDescriptionParserTest {
    @Test
    public void test_parse() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();

        TuningDescriptionRegistry registry = new TuningDescriptionRegistry();

        parsedTuningDescriptions.forEach(registry::addTuningDescription);
        var r = registry.getClassElementByPath("statistics-statistic_conditions.HiddenOrShownCondition.Timing");
        r = registry.getClassElementByPath("ui-ui_dialog.UiDialogResponse.UiDialogUiRequest");
        r = registry.getClassElementByPath("ui_dialog_notification.UiDialogNotification.UiDialogNotificationLevel");
        r = registry.getClassElementByPath("ui_dialog_notification.UiDialogNotification.UiDialogNotificationAutoDeleteReason");
        System.out.println("hello");
    }
}