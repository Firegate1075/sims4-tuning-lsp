package org.eclipse.lemminx.extensions.sims4tunings;

import org.junit.Test;

public class TuningDescriptionParserTest {
    @Test
    public void test_parse() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();

        TuningDescriptionRegistry registry = new TuningDescriptionRegistry();

        parsedTuningDescriptions.forEach(registry::addTuningDescription);
        System.out.println("hello");
    }
}