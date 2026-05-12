package org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel;

import org.eclipse.lemminx.dom.DOMNode;

public interface INode {
    INodeWithChildren parent();
    DOMNode domNode();
}
