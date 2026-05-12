package org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel;

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.ITuningDescriptionElement;

public class CommentNode implements INode {
    private final String comment;
    private final DOMNode domNode;
    private final INodeWithChildren parent;

    public CommentNode(String comment, DOMNode domNode, INodeWithChildren parent) {
        this.domNode = domNode;
        this.parent = parent;
        this.comment = comment;
    }

    public String comment() {
        return comment;
    }

    public DOMNode domNode() {
        return domNode;
    }

    public INodeWithChildren parent() {
        return parent;
    }
}
