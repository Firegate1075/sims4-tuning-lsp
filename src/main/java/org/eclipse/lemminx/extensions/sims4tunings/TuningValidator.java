package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.*;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class TuningValidator {
    public static Optional<ITuningDescriptionElement> getDescriptionOfNode(DOMDocument document, DOMNode node) {
        // TODO: does not check order of tunable tuples!

        if (node.getNodeType() != DOMNode.ELEMENT_NODE) {
            return Optional.empty();
        }

        //DOMNode currentNode = document.getDocumentElement();
        DOMNode currentNode = node;

        Deque<DOMNode> nodeSequence = new ArrayDeque<>();
        Deque<ITuningDescriptionElement> tuningDescriptionSequence = new ArrayDeque<>();
        TuningDescriptionRegistry registry = TuningDescriptionRegistry.getInstance();

        // while we have not found our node
        //while (!currentNode.isEqualNode(node)) {
        //    nodeSequence.addFirst(currentNode);
        //    currentNode = currentNode.getParentNode();
        //}
        while (currentNode != document.getDocumentElement()) {
            nodeSequence.addFirst(currentNode);
            currentNode = currentNode.getParentNode();
        }

        // handle root separately
        DOMNode rootNode = document.getDocumentElement();
        Optional<? extends ITuningDescriptionElement> rootDescription = switch (rootNode.getNodeName()) {
            case "I" -> registry.getInstanceElementByClassName(rootNode.getAttribute("c"));
            case "M" -> registry.getModuleElementByName(rootNode.getAttribute("n"));
            default -> Optional.empty();
        };

        if (rootDescription.isEmpty()) {
            return Optional.empty();
        }

        tuningDescriptionSequence.add(rootDescription.get());

        for (DOMNode nodeInSequence : nodeSequence) {
            Optional<ITuningDescriptionElement> foundMatchingDescription = getChildrenOfTuningDescriptionElement(tuningDescriptionSequence.peekLast()).stream()
                    .filter(child -> isTunableNodeMatchingDescription(nodeInSequence, child))
                    .findFirst();
            if (foundMatchingDescription.isPresent()) {
                tuningDescriptionSequence.addLast(foundMatchingDescription.get());

                // if it was a TdescFrag, also add the content element
                if (foundMatchingDescription.get() instanceof TdescFragTag tdescFragTag) {
                    String className = tdescFragTag.getClassName();
                    TdescFrag tdescFrag = TuningDescriptionRegistry.getInstance().getTdescFragByClassName(className).orElseThrow();
                    ITuningDescriptionElement tdescFragContent = getTdescFragContent(tdescFrag);
                    setTdescFragContentName(tdescFragContent, tdescFragTag.getName().orElse(null));
                    tuningDescriptionSequence.addLast(tdescFragContent);
                }
            } else {
                return Optional.empty();
            }
        }

        ITuningDescriptionElement candidate = tuningDescriptionSequence.removeLast();
        if (isTunableNodeMatchingDescription(node, candidate)) {
            // we have a final match
            return Optional.of(candidate);
        }

        return Optional.empty();
    }

    private static boolean isTunableNodeMatchingDescription(DOMNode node, ITuningDescriptionElement description) {
        if (node.getNodeType() != DOMNode.ELEMENT_NODE) {
            return false;
        }

        // check for TdescFragTag
        if (description instanceof TdescFragTag tdescFragTag) {
            String className = tdescFragTag.getClassName();
            Optional<TdescFrag> tdescFrag = TuningDescriptionRegistry.getInstance().getTdescFragByClassName(className);
            if (tdescFrag.isPresent()) {
                // match against the entry of the corresponding TdescFrag content
                ITuningDescriptionElement tdescFragContent = getTdescFragContent(tdescFrag.get());
                setTdescFragContentName(tdescFragContent, tdescFragTag.getName().orElse(null));
                return isTunableNodeMatchingDescription(node, tdescFragContent);
            }
        }

        boolean isCorrectType = switch (node.getNodeName()) {
            case "C" -> description instanceof ClassElement;
            case "T" -> description instanceof Tunable || description instanceof EnumItem;
            case "L" -> description instanceof TunableList;
            case "V" -> description instanceof TunableVariant;
            case "U" -> description instanceof TunableTuple;
            case "E" -> description instanceof TunableEnum;
            case "I" -> description instanceof InstanceElement;
            case "M" -> description instanceof ModuleElement;
            // ignore other types
            default -> false;
        };

        // disregard type check for <Deleted> elements
        if (description instanceof DeletedElement) {
            isCorrectType = true;
        }

        boolean hasCorrectName = false;

        // name is not required if the element is the only one in a TunableList, TunableTuple, etc.
        Optional<String> descriptionName = getTuningDescriptionElementName(description);
        if (descriptionName.isEmpty()) {
            hasCorrectName = true;
        } else {
            hasCorrectName = node.getAttribute("n").equals(descriptionName.get());

        }

        boolean variantTypeValid = true;

        if (description instanceof TunableVariant) {
            // check if one of the children has a name attribute that matches the variant type
            variantTypeValid = getChildrenOfTuningDescriptionElement(description).stream().anyMatch(
                    child -> getTuningDescriptionElementName(child).isPresent() && getTuningDescriptionElementName(child).get().equals(node.getAttribute("t")));
        } else if (node.getParentNode().getNodeName().equals("V")) {
            //check that the name actually matches the parent-variant's type
            variantTypeValid = node.getAttribute("n").equals(node.getParentNode().getAttribute("t"));
        }

        return isCorrectType && hasCorrectName && variantTypeValid;
    }

    private static ITuningDescriptionElement getTdescFragContent(TdescFrag tdescFrag) {
        return tdescFrag.getTunableElements().getFirst();
    }
    private static void setTdescFragContentName(ITuningDescriptionElement tdescFragContent, String name) {
        switch (tdescFragContent) {
            case TunableVariant tunableVariant -> tunableVariant.setName(name);
            case TunableList tunableList -> tunableList.setName(name);
            case TunableTuple tunableTuple -> tunableTuple.setName(name);
            default -> {
            }
        }
    }

    private static Optional<String> getTuningDescriptionElementName(ITuningDescriptionElement description) {
        return switch (description) {
            case Tunable tunable -> tunable.getName();
            case TunableList tunableList -> tunableList.getName();
            case TunableVariant tunableVariant -> tunableVariant.getName();
            case TunableTuple tunableTuple -> tunableTuple.getName();
            case TunableEnum tunableEnum -> tunableEnum.getName();
            case EnumItem enumItem -> Optional.of(enumItem.getName());
            default -> Optional.empty();
        };
    }

    private static List<ITuningDescriptionElement> getChildrenOfTuningDescriptionElement(ITuningDescriptionElement parent) {
        return switch (parent) {
            case InstanceElement instanceElement -> instanceElement.getTunableElements();
            case TunableList tunableList -> tunableList.getTunableElements();
            case TunableVariant tunableVariant -> tunableVariant.getTunableElements();
            case TunableTuple tunableTuple -> tunableTuple.getTunableElements();
            case ClassElement classElement -> classElement.getTunableElements();
            case ModuleElement moduleElement -> moduleElement.getTunableElements();
            case TdescFrag tdescFrag -> tdescFrag.getTunableElements();
            default -> List.of();
        };
    }
}
