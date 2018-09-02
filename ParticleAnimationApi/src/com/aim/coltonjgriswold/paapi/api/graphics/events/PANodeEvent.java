package com.aim.coltonjgriswold.paapi.api.graphics.events;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.mesh.PANode;

public class PANodeEvent extends PAEvent {

    private PANode a;

    /**
     * An event relating to a node
     * 
     * @param node
     *            The node
     */
    public PANodeEvent(PANode node) {
	a = node;
    }

    /**
     * Gets the node
     * 
     * @return PANode
     */
    public PANode getNode() {
	if (!cancelled)
	    return a;
	return null;
    }
}
