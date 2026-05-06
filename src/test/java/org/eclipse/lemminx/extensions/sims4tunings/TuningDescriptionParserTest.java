package org.eclipse.lemminx.extensions.sims4tunings;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TuningDescriptionParserTest {
    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final Path DEFAULT_TDESC_PATH = Paths.get(PROJECT_DIRECTORY + "/tdesc");

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = TuningDescriptionRegistry.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void test_parse() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML(DEFAULT_TDESC_PATH);

        assert(parsedTuningDescriptions.size() == 1764);
    }
}