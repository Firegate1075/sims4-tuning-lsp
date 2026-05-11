package org.eclipse.lemminx.extensions.sims4tunings.util;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.ITuningDescriptionElement;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.TdescFragTag;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.CommentElement;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.NoValidTuningDescription;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.Node;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel.TextElement;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;

import java.util.*;

public class TuningTreeParser {

    public static Optional<Node> parseTree(DOMDocument domDocument) {
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

        ArrayList<Node> children = new ArrayList<>();
        Node rootNode = new Node(rootDescription.get(), domRoot, null, children);
        addChildNodes(rootNode);

        return Optional.of(rootNode);
    }

    /**
     * Takes a parent node and adds all child nodes to the parent node
     * @param parentNode the parent node
     */
    private static void addChildNodes(Node parentNode) {
        List<DOMNode> domChildren = parentNode.domNode().getChildren();

        for (DOMNode domChild : domChildren) {
            // find tuning description
            ITuningDescriptionElement tuningDescription = getDescriptionOfNode(parentNode.tuningDescription(), domChild).orElse(new NoValidTuningDescription());

            Node childNode = new Node(tuningDescription, domChild, parentNode, new ArrayList<>());
            parentNode.children().add(childNode);
            addChildNodes(childNode);
        }
    }

    private static Optional<ITuningDescriptionElement> getDescriptionOfNode(ITuningDescriptionElement parentDescription, DOMNode node) {
        if (node.isElement()) {
            // node is an element (tag)
            for (ITuningDescriptionElement tuningDescription : TuningUtils.getChildrenOfTuningDescriptionElement(parentDescription)) {
                // resolve tdesc frag tags to content
                if (tuningDescription instanceof TdescFragTag tdescFragTag) {
                    tuningDescription = TuningUtils.getTdescFragTagContent(tdescFragTag);
                }

                if (TuningUtils.isTunableNodeMatchingDescription(node, tuningDescription)) {
                    return Optional.of(tuningDescription);
                }
            }
        } else if (node.isText()) {
            // node is a text node (terminal node)
            return Optional.of(new TextElement());
        } else if (node.isComment()) {
            // node is a comment node
            return Optional.of(new CommentElement());
        }

        return Optional.empty();
    }

    public static Optional<Node> getNodeFromDOM(Node root, DOMNode targetDomNode) {
        Deque<DOMNode> domNodeSequence = new ArrayDeque<>();
        Deque<Node> nodeSequence = new ArrayDeque<>();

        // build sequence of dom nodes from root to target node
        DOMNode currentNode = targetDomNode;
        while (currentNode != root.domNode()) {
            domNodeSequence.addFirst(currentNode);
            currentNode = currentNode.getParentNode();
        }

        nodeSequence.addFirst(root);

        for (DOMNode domNode : domNodeSequence) {
            assert nodeSequence.peekLast() != null;
            for (Node child : nodeSequence.peekLast().children()) {
                if (child.domNode().equals(domNode)) {
                    nodeSequence.addLast(child);
                }
            }
        }

        assert nodeSequence.peekLast() != null;
        if (nodeSequence.peekLast().domNode().equals(targetDomNode)) {
            return Optional.of(nodeSequence.peekLast());
        }

        return  Optional.empty();
    }
}
