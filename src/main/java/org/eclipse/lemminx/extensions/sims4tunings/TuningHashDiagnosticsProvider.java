package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import java.util.List;
import java.util.logging.Logger;

public class TuningHashDiagnosticsProvider implements IDiagnosticsParticipant {
    private static final Logger LOGGER = Logger.getLogger(TuningHashDiagnosticsProvider.class.getName());

    @Override
    public void doDiagnostics(DOMDocument domDocument, List<Diagnostic> list, XMLValidationSettings xmlValidationSettings, CancelChecker cancelChecker) {
        DOMNode rootNode = domDocument.getDocumentElement();
        if (rootNode == null || !rootNode.hasAttribute("s")) {
            return;
        }

        String oldHash = rootNode.getAttribute("s");

        // compute hash
        String nameAttributeString = rootNode.getAttribute("n");
        String newHash = Long.toUnsignedString(Hashing.fnv1Hash(nameAttributeString, true));
        if (!oldHash.equals(newHash)) {
            LOGGER.info("Incorrect hash of name attribute. Expected: " + newHash + ", found: " + oldHash + ". Reporting Error.");
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.setSeverity(DiagnosticSeverity.Error);
            diagnostic.setMessage("Incorrect hash of name attribute.");
            Range errorRange = XMLPositionUtility.selectAttributeValue(rootNode.getAttributeNode("s"), true);
            if (errorRange != null) {
                diagnostic.setRange(XMLPositionUtility.selectAttributeValue(rootNode.getAttributeNode("s"), true));
            }
            diagnostic.setCode("sims4tunings.incorrectHash");

            cancelChecker.checkCanceled();
            list.add(diagnostic);
        }
    }
}
