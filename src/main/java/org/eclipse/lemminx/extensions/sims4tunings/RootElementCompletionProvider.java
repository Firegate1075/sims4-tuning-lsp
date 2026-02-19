package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.InstanceElement;
import org.eclipse.lemminx.services.extensions.completion.CompletionParticipantAdapter;
import org.eclipse.lemminx.services.extensions.completion.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.completion.ICompletionResponse;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

import java.util.List;
import java.util.logging.Logger;

public class RootElementCompletionProvider extends CompletionParticipantAdapter {

    private final static Logger LOGGER = Logger.getLogger(RootElementCompletionProvider.class.getName());

    private final TuningDescriptionRegistry registry;

    public RootElementCompletionProvider(TuningDescriptionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onTagOpen(ICompletionRequest completionRequest, ICompletionResponse completionResponse, CancelChecker cancelChecker) throws Exception {
        if (completionRequest.getParentElement() == null) {
            // request for root element
            var completionItem = getInstanceCompletionItem(completionRequest);
            cancelChecker.checkCanceled();
            completionResponse.addCompletionItem(completionItem);
        }
    }


    public void onXMLContent(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        //var completionItem = new CompletionItem();
        //completionItem.setLabel("Furz");
        //response.addCompletionItem(completionItem);
    }

    public void onAttributeName(boolean generateValue, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        LOGGER.info("Attribute completion requested for item of type" + request.getCurrentTag());
        var completionItem = new CompletionItem();
        completionItem.setLabel("mfg");
        cancelChecker.checkCanceled();
        response.addCompletionItem(completionItem);
    }

    public void onAttributeValue(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        if (request.getCurrentTag().equals("I") && request.getCurrentAttributeName().equals("c")) {
            LOGGER.info("Class name attribute completion requested");
            List<String> classNames = registry.getClassNamesOfInstanceElementEntries();
            for (String className : classNames) {
                InstanceElement element = registry.getInstanceElementByClassName(className).orElseThrow();

                // build completion item
                CompletionItem completionItem = new CompletionItem(className);
                String documentationString = element.getDescription().orElse("");
                completionItem.setDocumentation(Either.forRight(new MarkupContent("plaintext", documentationString)));
                completionItem.setInsertTextFormat(InsertTextFormat.PlainText);
                TextEdit edit = new TextEdit();
                edit.setNewText(className);
                edit.setRange(request.getReplaceRange());
                completionItem.setTextEdit(Either.forLeft(edit));
                completionItem.setKind(CompletionItemKind.Value);
                completionItem.setFilterText(className);
                completionItem.setSortText(className);
                completionItem.setDetail(element.getDescription().orElse(""));

                // add text edits for "m" and "i" attribute completion
                TextEdit moduleTextEdit = new TextEdit();
                DOMNode node = request.getNode();
                DOMAttr moduleAttribute = node.getAttributeNode("m");
                Range moduleValueRange = XMLPositionUtility.selectAttributeValue(moduleAttribute, true);
                moduleTextEdit.setNewText(element.getModuleName());
                moduleTextEdit.setRange(moduleValueRange);

                TextEdit instanceTextEdit = new TextEdit();
                DOMAttr instanceAttribute = node.getAttributeNode("i");
                Range instanceValueRange = XMLPositionUtility.selectAttributeValue(instanceAttribute, true);
                instanceTextEdit.setNewText(element.getInstanceType());
                instanceTextEdit.setRange(instanceValueRange);

                completionItem.setAdditionalTextEdits(List.of(moduleTextEdit, instanceTextEdit));


                // add completion item
                cancelChecker.checkCanceled();
                response.addCompletionItem(completionItem);
            }
        } else {
            var completionItem = new CompletionItem();
            completionItem.setLabel("furzler");
            cancelChecker.checkCanceled();
            response.addCompletionItem(completionItem);
        }
    }

    private static CompletionItem getInstanceCompletionItem(ICompletionRequest request) {
        CompletionItem item = new CompletionItem("Instance");
        item.setDocumentation(Either.forRight(new MarkupContent("plaintext", "Instance tunable")));
        item.setInsertTextFormat(InsertTextFormat.Snippet);
        TextEdit edit = new TextEdit();
        edit.setNewText("<I c=\"$1\" i=\"\" m=\"\" n=\"$2\" s=\"\">$0</I>");
        edit.setRange(request.getReplaceRange());
        item.setTextEdit(Either.forLeft(edit));
        item.setKind(CompletionItemKind.Field);
        item.setFilterText(edit.getNewText());
        item.setSortText("Instance");
        return item;
    }

    private static CompletionItem toTag(String name, MarkupContent description, ICompletionRequest request) {
        CompletionItem res = new CompletionItem(name);
        res.setDocumentation(Either.forRight(description));
        res.setInsertTextFormat(InsertTextFormat.Snippet);
        TextEdit edit = new TextEdit();
        edit.setNewText('<' + name + "$1>$0</" + name + '>');
        edit.setRange(request.getReplaceRange());
        res.setTextEdit(Either.forLeft(edit));
        res.setKind(CompletionItemKind.Field);
        res.setFilterText(edit.getNewText());
        res.setSortText(name);
        return res;
    }
}
