package org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel;

import java.util.List;

public interface INodeWithChildren extends INode {
    List<INode> children();
}
