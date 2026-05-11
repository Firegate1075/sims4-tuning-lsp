package org.eclipse.lemminx.extensions.sims4tunings.models.TuningTreeDataModel;

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.ITuningDescriptionElement;

import java.util.List;



public record Node (ITuningDescriptionElement tuningDescription, DOMNode domNode, Node parent, List<Node> children) {

}
