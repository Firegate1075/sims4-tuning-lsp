package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.*;

import java.util.*;
import java.util.stream.Stream;

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
                    ITuningDescriptionElement tdescFragContent = getTdescFragTagContent(tdescFragTag);
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

    public static boolean isTunableNodeMatchingDescription(DOMNode node, ITuningDescriptionElement description) {
        if (node.getNodeType() != DOMNode.ELEMENT_NODE) {
            return false;
        }

        // check for TdescFragTag
        if (description instanceof TdescFragTag tdescFragTag) {
            return isTunableNodeMatchingDescription(node, getTdescFragTagContent(tdescFragTag));
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

    public static ITuningDescriptionElement getTdescFragTagContent(TdescFragTag tdescFragTag) {
        String className = tdescFragTag.getClassName();
        TdescFrag tdescFrag = TuningDescriptionRegistry.getInstance().getTdescFragByClassName(className).orElseThrow();
        ITuningDescriptionElement tdescFragContent = tdescFrag.getTunableElements().getFirst();;

        // set tdescFragContent's name attribute to that of the tag
        String name = tdescFragTag.getName().orElse(null);
        switch (tdescFragContent) {
            case TunableVariant tunableVariant -> tunableVariant.setName(name);
            case TunableList tunableList -> tunableList.setName(name);
            case TunableTuple tunableTuple -> tunableTuple.setName(name);
            default -> {
            }
        }

        // set the description of the tdescFrag content
        Optional<String> contentDescription = switch (tdescFragContent) {
            case TunableVariant tunableVariant -> tunableVariant.getDescription();
            case TunableList tunableList -> tunableList.getDescription();
            case TunableTuple tunableTuple -> tunableTuple.getDescription();
            default -> Optional.empty();
        };
        String tdescFragTagDescription = tdescFragTag.getDescription();
        String description = contentDescription.orElse("") + ": " + tdescFragTagDescription;

        switch (tdescFragContent) {
            case TunableVariant tunableVariant -> tunableVariant.setDescription(description);
            case TunableList tunableList -> tunableList.setDescription(description);
            case TunableTuple tunableTuple -> tunableTuple.setDescription(description);
            default -> {
            }
        }

        // set the display value of the tdescFrag content to the tag's
        String display = tdescFragTag.getDisplay().orElse(null);
        switch (tdescFragContent) {
            case TunableVariant tunableVariant -> tunableVariant.setDisplay(display);
            case TunableList tunableList -> tunableList.setDisplay(display);
            case TunableTuple tunableTuple -> tunableTuple.setDisplay(display);
            default -> {
            }
        }
        return tdescFragContent;
    }

    public static Optional<String> getTuningDescriptionElementName(ITuningDescriptionElement description) {
        if (description instanceof IHasName iHasName) {
            return Optional.of(iHasName.getName());
        } else if (description instanceof IHasOptionalName iHasOptionalName) {
            return iHasOptionalName.getName();
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getTuningDescriptionElementDisplay(ITuningDescriptionElement description) {
        if (description instanceof IHasOptionalDisplay iHasOptionalDisplay) {
            return iHasOptionalDisplay.getDisplay();
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getTuningDescriptionElementDescription(ITuningDescriptionElement description) {
        if (description instanceof IHasDescription iHasDescription) {
            return Optional.of(iHasDescription.getDescription());
        } else if (description instanceof IHasOptionalDescription iHasOptionalDescription) {
            return iHasOptionalDescription.getDescription();
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getTuningDescriptionElementClassName(ITuningDescriptionElement description) {
        if (description instanceof IHasClass iHasClass) {
            return Optional.of(iHasClass.getClassName());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getTuningDescriptionElementTypeName(ITuningDescriptionElement description) {
        if (description instanceof IHasType iHasType) {
            return Optional.of(iHasType.getType());
        } else if (description instanceof IHasOptionalType iHasOptionalType) {
            return iHasOptionalType.getType();
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Integer> getIndexOfElementInList(DOMNode node) {
        if (node.getNodeType() != DOMNode.ELEMENT_NODE) {
            return Optional.empty();
        }

        DOMNode parent = node.getParentNode();
        if (parent == null) {
            return Optional.empty();
        }

        int index = 0;

        for (DOMNode child : parent.getChildren()) {
            if (child.equals(node)) {
                break;
            }
            index++;
        }
        return Optional.of(index);
    }

    public static List<ITuningDescriptionElement> getChildrenOfTuningDescriptionElement(ITuningDescriptionElement parent) {
        List<ITuningDescriptionElement> children = new ArrayList<>();
        if (parent instanceof IHasChildren parentWithChildren) {
            children = parentWithChildren.getTunableElements();
        }

        // resolve "parent" attribute for inherited elements
        if (parent instanceof InstanceElement instanceElement) {
            if (instanceElement.getParents().isPresent()) {
                String parentClassName = instanceElement.getParents().get().split(",")[0];
                Optional<InstanceElement> parentInstance = TuningDescriptionRegistry.getInstance().getInstanceElementByClassName(parentClassName);

                return Stream.concat(children.stream(), getChildrenOfTuningDescriptionElement(parentInstance.orElseThrow()).stream()).toList();
            }
        }

        return children;
    }

    public static boolean isElementOptional(ITuningDescriptionElement descriptionElement) {
        // resolve TdescFragTags
        if (descriptionElement instanceof TdescFragTag) {
            return isElementOptional(getTdescFragTagContent((TdescFragTag) descriptionElement));
        }

        switch (descriptionElement) {
            case Tunable tunable -> {
                // check if the tunable has allow_none="True"
                Optional<Boolean> allowNone = tunable.getAllowNone();
                if (allowNone.isPresent() && allowNone.get()) {
                    return true;
                }
                // otherwise: allow empty if it has a default value
                return tunable.getDefaultValue().isPresent();
            }
            case TunableList tunableList -> {
                return true;
            }
            case TunableTuple tunableTuple -> {
                // optional if all children are optional
                for (ITuningDescriptionElement child : tunableTuple.getTunableElements()) {
                    if (!isElementOptional(child)) {
                        return false;
                    }
                }
            }
            case TunableVariant tunableVariant -> {
                // optional if the default variant is optional

                // find the default variant's description
                String defaultVariant = tunableVariant.getDefaultValue();
                for (ITuningDescriptionElement child : tunableVariant.getTunableElements()) {
                    if (getTuningDescriptionElementName(child).isPresent() && getTuningDescriptionElementName(child).get().equals(defaultVariant)) {
                        return isElementOptional(child);
                    }
                }
                // TODO: this is an error, maybe log or throw?
                return false;
            }
            case TunableEnum tunableEnum -> {
                // always optional, because they always have a default value
                return true;
            }
            default -> {}
        }

        // assume all other elements are optional
        return true;
    }
}
