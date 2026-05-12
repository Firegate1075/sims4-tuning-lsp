package org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel;

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.ITuningDescriptionElement;

import java.util.List;


public class ContainerNode implements INodeWithChildren {
    private final ITuningDescriptionElement tuningDescription;
    private final List<INode> children;
    private final DOMNode domNode;
    private final INodeWithChildren parent;

    public ContainerNode(ITuningDescriptionElement tuningDescription, DOMNode domNode, INodeWithChildren parent, List<INode> children) {
        this.domNode = domNode;
        this.parent = parent;
        this.tuningDescription = tuningDescription;
        this.children = children;
    }

    public ITuningDescriptionElement tuningDescription() {
        return tuningDescription;
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
