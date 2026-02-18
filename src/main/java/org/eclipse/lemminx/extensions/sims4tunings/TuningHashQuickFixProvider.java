package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.codeaction.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.codeaction.ICodeActionRequest;
import org.eclipse.lemminx.services.extensions.commands.IXMLCommandService;
import org.eclipse.lemminx.utils.TextEditUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.logging.Logger;

public class TuningHashQuickFixProvider implements ICodeActionParticipant {
    private static final Logger LOGGER = Logger.getLogger(TuningHashQuickFixProvider.class.getName());

    @Override
    public void doCodeAction(ICodeActionRequest request, List<CodeAction> codeActions, CancelChecker cancelChecker) throws CancellationException {
        if (request.getDiagnostic().getCode().getLeft().equals("sims4tunings.incorrectHash")) {
            getRecomputeHashAction(request.getDocument()).ifPresent(codeActions::add);
        }
    }

    private static Optional<CodeAction> getRecomputeHashAction(DOMDocument document) {
        DOMNode rootNode = document.getDocumentElement();
        if (rootNode == null || !rootNode.hasAttribute("s")) {
            return Optional.empty();
        }

        String nameAttributeString = rootNode.getAttribute("n");
        Range range = XMLPositionUtility.selectAttributeValue(rootNode.getAttributeNode("s"), true);

        if (range == null) {
            LOGGER.warning("Could not find attribute range for node " + rootNode.getNodeName());
            return Optional.empty();
        }

        TextEdit edit = new TextEdit();
        edit.setNewText(Long.toUnsignedString(Hashing.fnv1Hash(nameAttributeString, true)));
        edit.setRange(range);

        TextDocumentEdit documentEdit = TextEditUtils.creatTextDocumentEdit(document, List.of(edit));
        WorkspaceEdit workspaceEdit = TextEditUtils.createWorkspaceEdit(List.of(Either.forLeft(documentEdit)));

        CodeAction action = new CodeAction();
        action.setTitle("Recompute hash");
        action.setEdit(workspaceEdit);
        action.setKind(CodeActionKind.QuickFix);
        return Optional.of(action);
    }
}
