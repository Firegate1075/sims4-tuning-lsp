package org.eclipse.lemminx.extensions.sims4tunings;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;

public class TuningDescriptionParserTest {
    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = TuningDescriptionRegistry.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void test_parse() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();

        assert(parsedTuningDescriptions.size() == 1764);
    }
}