package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.*;
import org.eclipse.lemminx.services.extensions.completion.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.completion.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.completion.ICompletionResponse;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TuningDescriptionCompletionProvider implements ICompletionParticipant {
    // completion provider for tuning files based on the tuning description
    private static final Logger LOGGER = Logger.getLogger(TuningDescriptionCompletionProvider.class.getName());

    @Override
    public void onTagOpen(ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {
        if (iCompletionRequest.getParentElement() != null) {
            LOGGER.info("Tag open completion requested for element with parent " + iCompletionRequest.getParentElement().getNodeName());
            Optional<ITuningDescriptionElement> parentDescription = TuningValidator.getDescriptionOfNode(iCompletionRequest.getXMLDocument(), iCompletionRequest.getParentElement());
            if (parentDescription.isEmpty()) {
                return;
            }

            List<CompletionItem> completionItems = getCompletionItemsForChildren(iCompletionRequest, parentDescription.get());
            LOGGER.info("Received " + completionItems.size() + " completion items");
            for (CompletionItem completionItem : completionItems) {
                iCompletionResponse.addCompletionItem(completionItem);
            }
        }
    }

    private List<CompletionItem> getCompletionItemsForChildren(ICompletionRequest request, ITuningDescriptionElement parentDescription) {
        LOGGER.info("Request for completion items for parent element of type " + parentDescription.getClass().getSimpleName());
        // get completion items from parent description
        List<ITuningDescriptionElement> childrenDescriptions = TuningValidator.getChildrenOfTuningDescriptionElement(parentDescription);
        List<CompletionItem> completionItems = new ArrayList<>();

        // tuples only suggest the next element in the tuple
        // TODO: children with default values are optional and should be skippable -> suggest children up to next chld without a default
        if (parentDescription instanceof TunableTuple tunableTuple) {
            childrenDescriptions = tunableTuple.getTunableElements();

            Optional<Integer> indexOfNode = TuningValidator.getIndexOfElementInList(request.getNode());
            Optional<CompletionItem> item = buildCompletionItemForElement(request, childrenDescriptions.get(indexOfNode.orElseThrow()));
            item.ifPresent(completionItems::add);
        } else {
            // all other container elements suggest their children
            for (ITuningDescriptionElement childDescription : childrenDescriptions) {
                if (childDescription instanceof TdescFragTag tdescFragTag) {
                    childDescription = TuningValidator.getTdescFragTagContent(tdescFragTag);
                }

                Optional<CompletionItem> item = buildCompletionItemForElement(request, childDescription);
                item.ifPresent(completionItems::add);
            }
        }

        return completionItems;
    }

    // TODO: move buildCompletionItemForElement into separate method

    private Optional<CompletionItem> buildCompletionItemForElement(ICompletionRequest request, ITuningDescriptionElement element) {
        LOGGER.info("Building completion item for element of type " + element.getClass().getSimpleName());
        // only tunable elements may appear in tuning files
        if (!(element instanceof ITunable)) {
            return Optional.empty();
        }


        Optional<String> name = TuningValidator.getTuningDescriptionElementName(element);
        Optional<String> description = TuningValidator.getTuningDescriptionElementDescription(element);
        Optional<String> display = TuningValidator.getTuningDescriptionElementDisplay(element);
        Optional<String> className = TuningValidator.getTuningDescriptionElementClassName(element);
        Optional<String> typeName = TuningValidator.getTuningDescriptionElementTypeName(element);

        String label;
        if (name.isPresent()) {
            label = name.get();
        } else if (display.isPresent()) {
            label = display.get();
        } else {
            label = className.orElseThrow();
        }


        CompletionItem item = new CompletionItem();



        String newText = "<" + ((ITunable) element).getTunableTag();

        if (name.isPresent()) {
            newText += " n=\"" + name.get() + "\"";
        }
        if (element instanceof TunableVariant) {
            newText += " t=\"$1\"";
        }
        newText += ">$0</" + ((ITunable) element).getTunableTag() + ">";

        item.setLabel(label);
        item.setDocumentation(Either.forLeft(description.orElse("")));
        TextEdit textEdit = new TextEdit();
        textEdit.setNewText(newText);
        textEdit.setRange(request.getReplaceRange());
        item.setTextEdit(Either.forLeft(textEdit));
        item.setFilterText(textEdit.getNewText());
        item.setSortText(item.getLabel());

        // TODO: build textedit text from element. e.g. add <E...> and content tag for TunableEnums
        // TODO: Compute Display text, e.g. use "Display" if available, otherwise try name or even Tag type

        return Optional.of(item);
    }

    @Override
    public void onXMLContent(ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onAttributeName(boolean b, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onAttributeValue(String s, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onDTDSystemId(String s, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }
}
