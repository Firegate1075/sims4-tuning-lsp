package org.eclipse.lemminx.extensions.sims4tunings.providers;

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.ContainerNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.INode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.INodeWithChildren;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.Node;
import org.eclipse.lemminx.extensions.sims4tunings.util.TuningTreeParser;
import org.eclipse.lemminx.extensions.sims4tunings.util.TuningUtils;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.*;
import org.eclipse.lemminx.services.extensions.completion.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.completion.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.completion.ICompletionResponse;
import org.eclipse.lsp4j.CompletionItem;
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

            // parse the DOM tree
            Optional<ContainerNode> root = TuningTreeParser.parseTree(iCompletionRequest.getXMLDocument());
            if (root.isEmpty()) {
                return;
            }
            Optional<INode> parentNode = TuningTreeParser.getNodeFromDOM(root.get(), iCompletionRequest.getParentElement());
            if (parentNode instanceof)


            Optional<ITuningDescriptionElement> parentDescription = TuningUtils.getDescriptionOfNode(iCompletionRequest.getXMLDocument(), iCompletionRequest.getParentElement());
            if (parentDescription.isEmpty()) {
                return;
            }

            List<CompletionItem> completionItems = getCompletionItemsForChildren(iCompletionRequest, parentDescription.get());
            LOGGER.info("Received " + completionItems.size() + " completion items");
            for (CompletionItem completionItem : completionItems) {
                iCompletionResponse.addCompletionItem(completionItem);
            }
        }

        if (iCompletionRequest.getParentElement() != null) {
            LOGGER.info("Tag open completion requested for element with parent " + iCompletionRequest.getParentElement().getNodeName());
            Optional<ITuningDescriptionElement> parentDescription = TuningUtils.getDescriptionOfNode(iCompletionRequest.getXMLDocument(), iCompletionRequest.getParentElement());
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

    private List<CompletionItem> getCompletionItemsForChildren(ContainerNode parentNode) {

    }

    private List<CompletionItem> getCompletionItemsForChildren(ICompletionRequest request, ITuningDescriptionElement parentDescription) {
        LOGGER.info("Request for completion items for parent element of type " + parentDescription.getClass().getSimpleName());
        // get completion items from parent description
        List<ITuningDescriptionElement> childrenDescriptions = TuningUtils.getChildrenOfTuningDescriptionElement(parentDescription);
        List<CompletionItem> completionItems = new ArrayList<>();

        // we need to first find the description for the latest node in the XML document
        // then, we can suggest all the descriptions until the next child without a default
        if (parentDescription instanceof TunableTuple tunableTuple) {

            // we first need to find the corresponding description for the last node
            int indexOfNode = TuningUtils.getIndexOfElementInList(request.getNode()).orElseThrow();

            Optional<ITuningDescriptionElement> predecessorDescription = Optional.empty();
            if (indexOfNode > 0) {
                // we have a predecessor within the container element
                int predecessorIndex = indexOfNode - 1;
                DOMNode predecessor = request.getNode().getParentNode().getChild(predecessorIndex);
                predecessorDescription = TuningUtils.getDescriptionOfNode(request.getXMLDocument(), predecessor);
            }

            // now we suggest all descriptions after the predecessor and up to the next one without a default
            for (ITuningDescriptionElement childDescription : tunableTuple.getTunableElements()) {
                if (childDescription instanceof TdescFragTag tdescFragTag) {
                    childDescription = TuningUtils.getTdescFragTagContent(tdescFragTag);
                }

                // if there is a predecessor, skip until we find it
                if (predecessorDescription.isPresent() && childDescription.equals(predecessorDescription.get())) {
                    continue;
                }

                // add the elements
                Optional<CompletionItem> item = buildCompletionItemForElement(request, childDescription);
                item.ifPresent(completionItems::add);


                // we stop if the description is not optional
                if (!TuningUtils.isElementOptional(childDescription)) {
                    break;
                }
            }
        } else {
            // all other container elements suggest all their children
            for (ITuningDescriptionElement childDescription : childrenDescriptions) {
                if (childDescription instanceof TdescFragTag tdescFragTag) {
                    childDescription = TuningUtils.getTdescFragTagContent(tdescFragTag);
                }

                Optional<CompletionItem> item = buildCompletionItemForElement(request, childDescription);
                item.ifPresent(completionItems::add);
            }
        }

        return completionItems;
    }

    private Optional<CompletionItem> buildCompletionItemForElement(ICompletionRequest request, ITuningDescriptionElement element) {
        LOGGER.info("Building completion item for element of type " + element.getClass().getSimpleName());
        // only tunable elements may appear in tuning files
        if (!(element instanceof ITunable)) {
            return Optional.empty();
        }


        Optional<String> name = TuningUtils.getTuningDescriptionElementName(element);
        Optional<String> description = TuningUtils.getTuningDescriptionElementDescription(element);
        Optional<String> display = TuningUtils.getTuningDescriptionElementDisplay(element);
        Optional<String> className = TuningUtils.getTuningDescriptionElementClassName(element);
        Optional<String> typeName = TuningUtils.getTuningDescriptionElementTypeName(element);

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

        return Optional.of(item);
    }

    @Override
    public void onXMLContent(ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {
        LOGGER.info("XML content completion requested. request: parentElement=" + iCompletionRequest.getParentElement());
        // TODO
    }

    @Override
    public void onAttributeName(boolean b, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onAttributeValue(String s, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {
        LOGGER.info("Attribute value completion requested for attribute " + iCompletionRequest.getCurrentAttributeName() + " of element " + iCompletionRequest.getCurrentTag());
        Optional<ContainerNode> root = TuningTreeParser.parseTree(iCompletionRequest.getXMLDocument());
        Optional<INode> node = TuningTreeParser.getNodeFromDOM(root.get(), iCompletionRequest.getNode()); // TODO: remove .get()

        Optional<ITuningDescriptionElement> elementDescription = TuningUtils.getDescriptionOfNode(iCompletionRequest.getXMLDocument(), iCompletionRequest.getNode());
        LOGGER.info("Element description: " + elementDescription);
        if (elementDescription.isEmpty()) {
            return;
        }

        List<CompletionItem> completionItems = getCompletionItemsForChildren(iCompletionRequest, elementDescription.get());
        for (CompletionItem completionItem : completionItems) {
            iCompletionResponse.addCompletionItem(completionItem);
        }
        // TODO
        // for TunableVariant types -> also autocomplete the corresponding child element
    }

    @Override
    public void onDTDSystemId(String s, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }
}
