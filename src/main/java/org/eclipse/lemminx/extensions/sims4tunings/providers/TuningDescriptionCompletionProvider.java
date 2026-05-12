package org.eclipse.lemminx.extensions.sims4tunings.providers;

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.ContainerNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.INode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.INodeWithChildren;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.INode;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TuningDescriptionCompletionProvider implements ICompletionParticipant {
    // completion provider for tuning files based on the tuning description
    private static final Logger LOGGER = Logger.getLogger(TuningDescriptionCompletionProvider.class.getName());

    @Override
    public void onTagOpen(ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {
        // check for root
        if (iCompletionRequest.getParentElement() != null && !iCompletionRequest.getNode().equals(iCompletionRequest.getXMLDocument().getDocumentElement())) {
            LOGGER.info("Tag open completion requested for element with parent " + iCompletionRequest.getParentElement().getNodeName());

            // parse the DOM tree
            Optional<ContainerNode> root = TuningTreeParser.parseTree(iCompletionRequest.getXMLDocument());
            if (root.isEmpty()) {
                return;
            }

            INode parentNode = TuningTreeParser.getNodeFromDOM(root.get(), iCompletionRequest.getParentElement()).get();
            if (parentNode instanceof ContainerNode parent) {
                List<CompletionItem> completionItems = getCompletionItemsForChildren(parent, iCompletionRequest);
                LOGGER.info("Received " + completionItems.size() + " completion items");
                for (CompletionItem completionItem : completionItems) {
                    iCompletionResponse.addCompletionItem(completionItem);
                }
            }
        }
    }

    private List<CompletionItem> getCompletionItemsForChildren(ContainerNode parentNode, ICompletionRequest request) {
        ITuningDescriptionElement parentDescription = parentNode.tuningDescription();
        LOGGER.info("Request for completion items for parent element of type " + parentDescription.getClass().getSimpleName());
        List<ITuningDescriptionElement> childrenDescriptions = TuningUtils.getChildrenOfTuningDescriptionElement(parentDescription);
        List<CompletionItem> completionItems = new ArrayList<>();

        if (parentDescription instanceof TunableTuple || parentDescription instanceof ClassElement || parentDescription instanceof ModuleElement || parentDescription instanceof InstanceElement) {
            for (ITuningDescriptionElement childDescription : childrenDescriptions) {
                // resolve tdesc frag tags
                if (childDescription instanceof TdescFragTag tdescFragTag) {
                    childDescription = TuningUtils.getTdescFragTagContent(tdescFragTag);
                }


                // check whether the candidate is already present in the tunable tuple
                boolean alreadyPresent = false;
                for (INode siblingNode : parentNode.children()) {
                    if (siblingNode instanceof ContainerNode siblingContainer) {
                        if (siblingContainer.tuningDescription().equals(childDescription)) {
                            alreadyPresent = true;
                            break;
                        }
                    }
                }

                // skip elements that are already present
                if (alreadyPresent) {
                    continue;
                }

                Optional<CompletionItem> item = buildCompletionItemForElement(request, childDescription);
                item.ifPresent(completionItems::add);
            }
        } else if (parentDescription instanceof TunableVariant tunableVariant) {
            // if the t="" attribute is present and correct, we need to find the corresponding child and suggest it
            // otherwise suggest all variants

            String typeAttribute = parentNode.domNode().getAttribute("t");

            if (typeAttribute != null) {
                // try to find the corresponding child
                Optional<ITuningDescriptionElement> child = childrenDescriptions.stream()
                        .filter(childDescription -> {
                            Optional<String> childDescriptionName = TuningUtils.getTuningDescriptionElementName(childDescription);
                            return childDescriptionName.map(name -> name.equals(typeAttribute)).orElse(false);
                        })
                        .findAny();

                if (child.isPresent()) {
                    Optional<CompletionItem> item = buildCompletionItemForElement(request, child.get());
                    item.ifPresent(completionItems::add);
                }
            } else {
                // suggest all variants
                for (ITuningDescriptionElement childDescription : childrenDescriptions) {
                    if (childDescription instanceof TdescFragTag tdescFragTag) {
                        childDescription = TuningUtils.getTdescFragTagContent(tdescFragTag);
                    }

                    Optional<CompletionItem> item = buildCompletionItemForElement(request, childDescription);
                    item.ifPresent(completionItems::add);
                }
            }
        } else if (parentDescription instanceof TunableList tunableList) {
            // lists suggest all their children (which should only be one)
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

        //List<CompletionItem> completionItems = getCompletionItemsForChildren(iCompletionRequest, elementDescription.get());
        //for (CompletionItem completionItem : completionItems) {
        //    iCompletionResponse.addCompletionItem(completionItem);
        //}
        // TODO
        // for TunableVariant types -> also autocomplete the corresponding child element
    }

    @Override
    public void onDTDSystemId(String s, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }
}
