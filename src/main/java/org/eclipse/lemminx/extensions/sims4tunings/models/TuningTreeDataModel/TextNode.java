package org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel;

import org.eclipse.lemminx.dom.DOMNode;

public class TextNode implements INode {
    private final String text;
    private final DOMNode domNode;
    private final INodeWithChildren parent;

    public TextNode(String text, DOMNode domNode, INodeWithChildren parent) {
        this.domNode = domNode;
        this.parent = parent;
        this.text = text;
    }

    public String text() {
        return text;
    }

    public DOMNode domNode() {
        return domNode;
    }

    public INodeWithChildren parent() {
        return parent;
    }
}
