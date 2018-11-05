package com.aim.coltonjgriswold.paapi.api.graphics.geometry.mesh;

public class PAHalfEdge {

    private PANode a;
    private PANode b;
    private PAHalfEdge c;
    private PAHalfEdge d;
    private PAHalfEdge e;
    private PAFace f;

    /**
     * Creates a new PAHalfEdge from a PANode an an associated PAFace
     * 
     * @param node
     * @param face
     */
    public PAHalfEdge(PANode node, PAFace face) {
	a = node;
	b = null;
	c = null;
	d = null;
	e = null;
	f = face;
    }

    /**
     * Gets the PANode linked to the start of this PAHalfEdge
     * 
     * @return PANode
     */
    public PANode head() {
	return a;
    }

    /**
     * Gets the PANode linked to the end of this PAHalfEdge
     * 
     * @return PANode
     */
    public PANode tail() {
	return b;
    }

    /**
     * Gets the next PAHalfEdge if it exists
     * 
     * @return PAHalfEdge
     */
    public PAHalfEdge next() {
	return c;
    }

    /**
     * Gets the previous PAHalfEdge if it exists
     * 
     * @return PAHalfEdge
     */
    public PAHalfEdge prev() {
	return d;
    }

    /**
     * Gets the opposite PAHalfEdge if it exists
     * 
     * @return PAHalfEdge
     */
    public PAHalfEdge getOpposite() {
	return e;
    }

    /**
     * Gets the face this PAHalfEdge is associated with
     * 
     * @return PAFace
     */
    public PAFace getFace() {
	return f;
    }

    /**
     * Sets the next PAHalfEdge
     * 
     * @param next
     *            The next PAHalfEdge
     * @return boolean
     */
    public boolean setNext(PAHalfEdge next) {
	if (!equals(next)) {
	    c = next;
	    next.b = a;
	    if (next.d == null || !next.d.equals(this))
		next.setPrev(this);
	    return true;
	}
	return false;
    }

    /**
     * Sets the previous PAHalfEdge
     * 
     * @param prev
     *            The previous PAHalfEdge
     * @return boolean
     */
    public boolean setPrev(PAHalfEdge prev) {
	if (!equals(prev)) {
	    d = prev;
	    b = prev.a;
	    if (prev.c == null || !prev.c.equals(this))
		prev.setNext(this);
	    return true;
	}
	return false;
    }

    /**
     * Sets the opposite PAHalfEdge
     * 
     * @param other
     *            The opposite PAHalfEdge
     * @return boolean
     */
    public boolean setOpposite(PAHalfEdge other) {
	if (!equals(other)) {
	    e = other;
	    if (other.e == null || !other.e.equals(this))
		other.setOpposite(this);
	    return true;
	}
	return false;
    }
}
