package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DOMParser;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.ITuningDescriptionElement;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.Tunable;
import org.eclipse.lemminx.services.XMLLanguageService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TuningValidatorTest {

    private static final String TEST_DOCUMENT = """
<?xml version="1.0" encoding="utf-8"?>
<!-- S4TK Group: 0000003F -->
<I c="LootActions" i="action" m="interactions.utils.loot" n="Firegate1075:loot_add" s="7649138417670323179">
  <L n="loot_actions">
    <V t="buff_removal">
        <U n="buff_removal">
            <L n="buffs_to_remove">
                <T>8990942777016644408<!--Firegate1075:buff_saved--></T>
            </L>
        </U>
    </V>
    <V t="trait_add">
      <U n="trait_add">
        <T n="trait">1<!--trait--></T>
      </U>
    </V>
  </L>
  <T n="run_test_first">True</T>
  <L n="tests">
    <L>
      <V t="buff">
        <U n="buff">
          <V n="whitelist" t="enabled">
            <L n="enabled">
              <T>8990942777016644408<!--Firegate1075:buff_saved--></T>
            </L>
          </V>
        </U>
      </V>
    </L>
  </L>
</I>""";

    @Test
    void getDescriptionOfNode() {
        var parsedTuningDescriptions = TuningDescriptionParser.parseTuningDescriptionXML();
        assert(parsedTuningDescriptions.size() == 1764);
        TuningDescriptionRegistry registry = TuningDescriptionRegistry.getInstance();
        // add all the tuning descriptions
        parsedTuningDescriptions.forEach(registry::addTuningDescription);

        XMLLanguageService xmlLanguageService = new XMLLanguageService();
        TextDocument document = new TextDocument(TEST_DOCUMENT, "test://test/test.xml");
        DOMDocument domDocument = DOMParser.getInstance().parse(document, xmlLanguageService.getResolverExtensionManager());
        Optional<ITuningDescriptionElement> description = TuningValidator.getDescriptionOfNode(domDocument, domDocument.getDocumentElement());
        assertTrue(description.isPresent());

        DOMNode tunableNode = domDocument.getDocumentElement().getChild(2).getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild();
        description = TuningValidator.getDescriptionOfNode(domDocument, tunableNode);
        assertTrue(description.isPresent());
        assert(((Tunable) description.get()).getType().get().equals("buff"));
    }
}