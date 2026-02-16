package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.services.extensions.completion.CompletionParticipantAdapter;
import org.eclipse.lemminx.services.extensions.completion.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.completion.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.completion.ICompletionResponse;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public class TuningCompletionParticipant extends CompletionParticipantAdapter {
    @Override
    public void onTagOpen(ICompletionRequest completionRequest, ICompletionResponse completionResponse, CancelChecker cancelChecker) throws Exception {
        var completionItem = toTag("Furz", new MarkupContent("plaintext", "OK GARMIN FURZ SPEICHERN!!!!"), completionRequest);
        cancelChecker.checkCanceled();
        completionResponse.addCompletionItem(completionItem);
    }
    public void onXMLContent(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        var completionItem = new CompletionItem();
        completionItem.setLabel("Furz");
        response.addCompletionItem(completionItem);
    }

    public void onAttributeName(boolean generateValue, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        var completionItem = new CompletionItem();
        completionItem.setLabel("mfg");
        response.addCompletionItem(completionItem);
    }

    public void onAttributeValue(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        var completionItem = new CompletionItem();
        completionItem.setLabel("furzler");
        response.addCompletionItem(completionItem);
    }

    private static CompletionItem toTag(String name, MarkupContent description, ICompletionRequest request) {
        CompletionItem res = new CompletionItem(name);
        res.setDocumentation(Either.forRight(description));
        res.setInsertTextFormat(InsertTextFormat.Snippet);
        TextEdit edit = new TextEdit();
        edit.setNewText('<' + name + ">$0</" + name + '>');
        edit.setRange(request.getReplaceRange());
        res.setTextEdit(Either.forLeft(edit));
        res.setKind(CompletionItemKind.Field);
        res.setFilterText(edit.getNewText());
        res.setSortText(name);
        return res;
    }
}
