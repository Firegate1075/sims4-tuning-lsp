package org.eclipse.lemminx.extensions.sims4tunings.util;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.ITuningDescriptionElement;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.TdescFragTag;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.*;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TuningTreeParser {

    public static Optional<ContainerNode> parseTree(DOMDocument domDocument) {
        // handle root element
        DOMNode domRoot = domDocument.getDocumentElement();
        Optional<? extends ITuningDescriptionElement> rootDescription = switch (domRoot.getNodeName()) {
            case "I" -> TuningDescriptionService.getSingletonInstance().getInstanceElementByClassName(domRoot.getAttribute("c"));
            case "M" -> TuningDescriptionService.getSingletonInstance().getModuleElementByName(domRoot.getAttribute("n"));
            default -> Optional.empty();
        };

        if (rootDescription.isEmpty()) {
            return Optional.empty();
        }

        ArrayList<INode> children = new ArrayList<>();
        ContainerNode rootNode = new ContainerNode(rootDescription.get(), domRoot, null, children);
        addChildNodes(rootNode);

        return Optional.of(rootNode);
    }

    /**
     * Takes a parent node and adds all child nodes to the parent node
     * @param parentNode the parent node
     */
    private static void addChildNodes(INodeWithChildren parentNode) {
        List<DOMNode> domChildren = parentNode.domNode().getChildren();

        for (DOMNode domChild : domChildren) {
            if (domChild.isElement()) {
                // if parent node has tuning description
                if (parentNode instanceof ContainerNode parentContainerNode) {
                    Optional<ITuningDescriptionElement> tuningDescription = getDescriptionOfNode(parentContainerNode.tuningDescription(), domChild);
                    INodeWithChildren childNode;

                    if (tuningDescription.isEmpty()) {
                        childNode = new InvalidNode(domChild, parentNode, new ArrayList<>());
                    } else {
                        // found matching tuning description
                        childNode = new ContainerNode(tuningDescription.get(), domChild, parentNode, new ArrayList<>());
                    }

                    parentNode.children().add(childNode);
                    addChildNodes(childNode);
                } else if (parentNode instanceof InvalidNode parentInvalidNode){
                    InvalidNode childNode = new InvalidNode(domChild, parentNode, new ArrayList<>());
                    parentNode.children().add(childNode);
                    addChildNodes(childNode);
                }
            } else if (domChild.isText()) {
                TextNode childNode = new TextNode(domChild.getTextContent(), domChild, parentNode);
                parentNode.children().add(childNode);
            } else if (domChild.isComment()) {
                CommentNode childNode = new CommentNode(domChild.getTextContent(), domChild, parentNode);
                parentNode.children().add(childNode);
            }
        }
    }

    /**
     * Get the description of a DOM element node with the given parent tuning description element.
     * @param parentDescription tuning description element of the parent node
     * @param node the DOM element node
     * @return the optional description of the node
     */
    private static Optional<ITuningDescriptionElement> getDescriptionOfNode(ITuningDescriptionElement parentDescription, DOMNode node) {
        for (ITuningDescriptionElement tuningDescription : TuningUtils.getChildrenOfTuningDescriptionElement(parentDescription)) {
            // resolve tdesc frag tags to content
            if (tuningDescription instanceof TdescFragTag tdescFragTag) {
                tuningDescription = TuningUtils.getTdescFragTagContent(tdescFragTag);
            }

            if (TuningUtils.isTunableNodeMatchingDescription(node, tuningDescription)) {
                return Optional.of(tuningDescription);
            }
        }

        return Optional.empty();
    }

    public static Optional<INode> getNodeFromDOM(ContainerNode root, DOMNode targetDomNode) {
        Deque<DOMNode> domNodeSequence = new ArrayDeque<>();
        Deque<INode> nodeSequence = new ArrayDeque<>();

        // build sequence of dom nodes from root to target node
        DOMNode currentNode = targetDomNode;
        while (currentNode != root.domNode()) {
            domNodeSequence.addFirst(currentNode);
            currentNode = currentNode.getParentNode();
        }

        nodeSequence.addFirst(root);

        for (DOMNode domNode : domNodeSequence) {
            INode lastNode = nodeSequence.peekLast();

            // check if the last found node has children
            if (lastNode instanceof INodeWithChildren nodeWithChildren) {
                for (INode child : nodeWithChildren.children()) {
                    if (child.domNode().equals(domNode)) {
                        nodeSequence.addLast(child);
                    }
                }
            }
        }

        INode lastNode = nodeSequence.peekLast();

        if (lastNode == null) {
            return Optional.empty();
        }

        if (lastNode.domNode().equals(targetDomNode)) {
            return Optional.of(lastNode);
        }

        return  Optional.empty();
    }
}
