package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.XMLAssert;
import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel.RootSettings;
import org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel.Sims4TuningSettings;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;
import org.eclipse.lemminx.services.XMLLanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class TuningDescriptionCompletionProviderTest {
    private final static String TEST_DOCUMENT = """
            <?xml version="1.0" encoding="utf-8"?>
            <!-- S4TK Group: 00000000 -->
            <I c="LootActions" i="action" m="interactions.utils.loot" n="Firegate1075:LootActions" s="16087474564407868122">
                <L n="loot_actions">
                    <E>
                        content
                    </E>
                    <|
                    <V t="">
                    </V>
                </L>
            </I>
            """;

    @Test
    void getCompletionItemsForChildren() throws BadLocationException {

        XMLAssert.testCompletionFor(TEST_DOCUMENT, XMLAssert.c("1", XMLAssert.te(2,3,2,0,"3"),"2"));
    }
}