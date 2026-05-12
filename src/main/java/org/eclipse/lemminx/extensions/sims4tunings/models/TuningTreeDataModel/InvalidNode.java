package org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel;

import org.eclipse.lemminx.dom.DOMNode;

import java.util.List;

public class InvalidNode implements INodeWithChildren {
    private final List<INode> children;
    private final DOMNode domNode;
    private final INodeWithChildren parent;


    public InvalidNode(DOMNode domNode, INodeWithChildren parent, List<INode> children) {
        this.domNode = domNode;
        this.parent = parent;
        this.children = children;
    }

    public List<INode> children() {
        return children;
    }

    public DOMNode domNode() {
        return domNode;
    }

    public INodeWithChildren parent() {
        return parent;
    }
}
