package com.aim.coltonjgriswold.paapi.api.graphics.geometry.mesh;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.util.Vector;

public class PAFace {

    private PAHalfEdge a;
    private Set<PANode> b;
    private Set<PAHalfEdge> c;

    /**
     * Creates a face with three or more different nodes
     * 
     * @param node0
     * @param node1
     * @param node2
     * @param nodeN
     */
    public PAFace(PANode node0, PANode node1, PANode node2, PANode... nodeN) {
	PAHalfEdge e0 = new PAHalfEdge(node0, this);
	PAHalfEdge e1 = new PAHalfEdge(node1, this);
	PAHalfEdge e2 = new PAHalfEdge(node2, this);
	e2.setPrev(e1);
	e1.setPrev(e0);
	b = new HashSet<PANode>();
	b.add(node0);
	b.add(node1);
	b.add(node2);
	c = new HashSet<PAHalfEdge>();
	c.add(e0);
	c.add(e1);
	c.add(e2);
	if (nodeN.length != 0) {
	    PAHalfEdge[] edges = new PAHalfEdge[nodeN.length];
	    PAHalfEdge prev = e2;
	    for (int i = 0; i < nodeN.length; i++) {
		PAHalfEdge edge = new PAHalfEdge(nodeN[i], this);
		edge.setPrev(prev);
		edges[i] = edge;
		prev = edge;
		b.add(nodeN[i]);
		c.add(edge);
	    }
	    e0.setPrev(prev);
	} else {
	    e0.setPrev(e2);
	}
	a = e0;
    }

    /**
     * Gets the starting PAHalfEdge of this face
     * 
     * @return PAHalfEdge
     */
    public PAHalfEdge getStartEdge() {
	return a;
    }

    /**
     * Gets all the nodes of this face
     * 
     * @return Set<PANode>
     */
    public Set<PANode> getNodes() {
	return b;
    }

    /**
     * Gets all the halfedges of thhis face
     * 
     * @return Set<PAHalfEdge>
     */
    public Set<PAHalfEdge> getHalfEdges() {
	return c;
    }

    /**
     * Gets the center of this face
     * 
     * @return Vector
     */
    public Vector getCenter() {
	Vector v = new Vector();
	for (PANode node : b) {
	    v.add(node.getOffset());
	}
	double n = b.size();
	return v.divide(new Vector(n, n, n));
    }

    /**
     * Gets the normal of this face
     * 
     * @return Vector
     */
    public Vector getNormal() {
	Vector v = new Vector();
	PAHalfEdge edge = a;
	for (int n = 0; n < c.size(); n++) {
	    v.add(edge.head().getOffset().crossProduct(edge.prev().head().getOffset()));
	    edge = edge.prev();
	}
	return v;
    }
}
