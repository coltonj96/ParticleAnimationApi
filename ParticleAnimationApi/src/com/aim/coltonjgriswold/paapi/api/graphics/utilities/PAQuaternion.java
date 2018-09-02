package com.aim.coltonjgriswold.paapi.api.graphics.utilities;

import java.util.Map;

import org.bukkit.util.Vector;

public class PAQuaternion implements Cloneable {

    private double a, b, c, d;

    /**
     * Create a blank PAQuaternion
     */
    public PAQuaternion() {
	a = 0.0;
	b = 0.0;
	c = 0.0;
	d = 1.0;
    }

    /**
     * Create a new PAQuaternion from another
     * 
     * @param other
     *            The other PAQuternion
     */
    public PAQuaternion(PAQuaternion other) {
	a = other.a;
	b = other.b;
	c = other.c;
	d = other.d;
    }

    /**
     * Create a PAQuaternion
     * 
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public PAQuaternion(double x, double y, double z, double w) {
	a = x;
	b = y;
	c = z;
	d = w;
	normalize();
    }

    /**
     * Contructor for deserializing
     * 
     * @param object
     *            The PAQuaternion to deserialize
     */
    public PAQuaternion(Map<String, Object> object) {
	a = (double) object.get("x");
	b = (double) object.get("y");
	c = (double) object.get("z");
	d = (double) object.get("w");
    }

    /**
     * Get the X value of this PAQuaternion
     * 
     * @return double
     */
    public double getX() {
	return a;
    }

    /**
     * Get the Y value of this PAQuaternion
     * 
     * @return double
     */
    public double getY() {
	return b;
    }

    /**
     * Get the Z value of this PAQuaternion
     * 
     * @return double
     */
    public double getZ() {
	return c;
    }

    /**
     * Get the W value of this PAQuaternion
     * 
     * @return double
     */
    public double getW() {
	return d;
    }

    /**
     * Sets this PAQuaternion to have values (0,0,0,1)
     */
    public void setIdentity() {
	a = 0.0;
	b = 0.0;
	c = 0.0;
	d = 1.0;
    }

    /**
     * Sets this PAQuaternion to another PAQuaternion
     * 
     * @param other
     *            The other PAQuaternion
     */
    public void setTo(PAQuaternion other) {
	a = other.a;
	b = other.b;
	c = other.c;
	d = other.d;
    }

    /**
     * Gets the dot product of this PAQUaternion and another
     * 
     * @param other
     *            The other PAQuaternion
     * @return double
     */
    public double dot(PAQuaternion other) {
	return a * other.a + b * other.b + c * other.c + d * other.d;
    }

    /**
     * Applies this PAQuaternion to a Vector
     * 
     * @param vector
     *            The Vector to use
     */
    public void apply(Vector vector) {
	double x = vector.getX();
	double y = vector.getY();
	double z = vector.getZ();
	vector.setX(x + 2.0 * (x * (-b * b - c * c) + y * (a * b - c * d) + z * (a * c + b * d)));
	vector.setY(y + 2.0 * (x * (a * b + c * d) + y * (-a * a - c * c) + z * (b * c - a * d)));
	vector.setZ(z + 2.0 * (x * (a * c - b * d) + y * (b * c + a * d) + z * (-a * a - b * b)));
    }

    /**
     * Get the Right Vector of this PAQuaternion
     * 
     * @return Vector
     */
    public Vector rightVector() {
	return new Vector(1.0 + 2.0 * (-b * b - c * c), 2.0 * (a * b + c * d), 2.0 * (a * c - b * d));
    }

    /**
     * Get the Up Vector of this PAQuaternion
     * 
     * @return Vector
     */
    public Vector upVector() {
	return new Vector(2.0 * (a * b - c * d), 1.0 + 2.0 * (-a * a - c * c), 2.0 * (b * c + a * d));
    }

    /**
     * Get the Forward Vector of this PAQuaternion
     * 
     * @return Vector
     */
    public Vector forwardVector() {
	return new Vector(2.0 * (a * c + b * d), 2.0 * (b * c - a * d), 1.0 + 2.0 * (-a * a - b * b));
    }

    /**
     * Divides this PAQuaternion by another
     * 
     * @param other
     *            The other PAQuaternion
     * @return PAQuaternion
     */
    public PAQuaternion divide(PAQuaternion other) {
	double x = d * -other.a + a * other.d + b * -other.c - c * -other.b;
	double y = d * -other.b + b * other.d + c * -other.a - a * -other.c;
	double z = d * -other.c + c * other.d + a * -other.b - b * -other.a;
	double w = d * other.d - a * -other.a - b * -other.b - c * -other.c;
	a = x;
	b = y;
	c = z;
	d = w;
	normalize();
	return this;
    }

    /**
     * Multilies this PAQuaternion by another
     * 
     * @param other
     *            The other PAQuaternion
     * @return PAQuaternion
     */
    public PAQuaternion multiply(PAQuaternion other) {
	double x = d * other.a + a * other.d + b * other.c - c * other.b;
	double y = d * other.b + b * other.d + c * other.a - a * other.c;
	double z = d * other.c + c * other.d + a * other.b - b * other.a;
	double w = d * other.d - a * other.a - b * other.b - c * other.c;
	a = x;
	b = y;
	c = z;
	d = w;
	normalize();
	return this;
    }

    /**
     * Rotates this PAQuaternion
     * 
     * @param axis
     *            The axis to rotate on
     * @param degrees
     *            The amount to rotate
     */
    public final void rotateAxis(Vector axis, double degrees) {
	rotateAxis(axis.getX(), axis.getY(), axis.getZ(), degrees);
    }

    /**
     * Rotates this PAQuaternion
     * 
     * @param axisX
     *            The X axis to rotate on
     * @param axisY
     *            The Y axis to rotate on
     * @param axisZ
     *            The Z axis to rotate on
     * @param degrees
     *            The amount to rotate
     */
    public final void rotateAxis(double axisX, double axisY, double axisZ, double degrees) {
	multiply(PAQuaternion.fromAxisAngles(axisX, axisY, axisZ, degrees));
    }

    /**
     * Rotate this PAQuaternion around the X axis
     * 
     * @param degrees
     *            The amount to rotate
     */
    public void rotateX(double degrees) {
	if (degrees != 0.0) {
	    double r = 0.5 * Math.toRadians(degrees % 360);
	    rotX(Math.cos(r), Math.sin(r));
	}
    }

    private void rotX(double ry, double rz) {
	double x = a * ry + d * rz;
	double y = b * ry + c * rz;
	double z = c * ry - b * rz;
	double w = d * ry - a * rz;
	a = x;
	b = y;
	c = z;
	d = w;
	normalize();
    }

    /**
     * Rotate this PAQuaternion around the Y axis
     * 
     * @param degrees
     *            The amount to rotate
     */
    public final void rotateY(double degrees) {
	if (degrees != 0.0) {
	    double r = 0.5 * Math.toRadians(degrees % 360);
	    rotY(Math.cos(r), Math.sin(r));
	}
    }

    private final void rotY(double rx, double rz) {
	double x = a * rx - c * rz;
	double y = b * rx + d * rz;
	double z = c * rx + a * rz;
	double w = d * rx - b * rz;
	a = x;
	b = y;
	c = z;
	d = w;
	normalize();
    }

    /**
     * Rotate this PAQuaternion around the Z axis
     * 
     * @param degrees
     *            The amount to rotate
     */
    public final void rotateZ(double degrees) {
	if (degrees != 0.0) {
	    double r = 0.5 * Math.toRadians(degrees % 360);
	    rotZ(Math.cos(r), Math.sin(r));
	}
    }

    private final void rotZ(double rx, double ry) {
	double x = a * rx + b * ry;
	double y = b * rx - a * ry;
	double z = c * rx + d * ry;
	double w = d * rx - c * ry;
	a = x;
	b = y;
	c = z;
	d = w;
	normalize();
    }

    /**
     * Inverts this PAQuaternion.
     */
    public void invert() {
	a = -a;
	b = -b;
	c = -c;
    }

    private void normalize() {
	double l = a * a + b * b + c * c + d * d;
	double f = 0;
	if (Math.abs(1.0 - l) < 2.107342e-08) {
	    f = (2.0 / (1.0 + l));
	} else {
	    f = 1.0 / Math.sqrt(l);
	}
	a *= f;
	b *= f;
	c *= f;
	d *= f;
    }

    @Override
    public PAQuaternion clone() {
	return new PAQuaternion(this);
    }

    @Override
    public boolean equals(Object o) {
	if (o == this) {
	    return true;
	} else if (o instanceof PAQuaternion) {
	    PAQuaternion q = (PAQuaternion) o;
	    return q.a == a && q.b == b && q.c == c && q.d == d;
	} else {
	    return false;
	}
    }

    @Override
    public String toString() {
	return "{" + a + ", " + b + ", " + c + ", " + d + "}";
    }

    public static PAQuaternion fromToRotation(Vector from, Vector to) {
	double dot = from.dot(to);
	PAQuaternion q = new PAQuaternion();
	q.a = from.getY() * to.getZ() - to.getY() * from.getZ();
	q.b = from.getZ() * to.getX() - to.getZ() * from.getX();
	q.c = from.getX() * to.getY() - to.getX() * from.getY();
	q.d = dot;
	q.d += Math.sqrt(q.a * q.a + q.b * q.b + q.c * q.c + q.d * q.d);
	q.normalize();

	if (Double.isNaN(q.d)) {
	    if (dot > 0.0) {
		q.setIdentity();
	    } else {
		double l = from.getZ() * from.getZ() + from.getY() * from.getY();
		double n = 0;
		if (Math.abs(1.0 - l) < 2.107342e-08) {
		    n = (2.0 / (1.0 + l));
		} else {
		    n = 1.0 / Math.sqrt(l);
		}
		if (Double.isInfinite(n)) {
		    l = from.getX() * from.getX() + from.getZ() * from.getZ();
		    if (Math.abs(1.0 - l) < 2.107342e-08) {
			n = (2.0 / (1.0 + l));
		    } else {
			n = 1.0 / Math.sqrt(l);
		    }
		    q.a = n * from.getZ();
		    q.b = 0.0;
		    q.c = n * -from.getX();
		    q.d = 0.0;
		} else {
		    q.a = 0.0;
		    q.b = n * -from.getZ();
		    q.c = n * from.getY();
		    q.d = 0.0;
		}
	    }
	}
	return q;
    }

    /**
     * Creates a PAQuaternion from an axis an rotation
     * 
     * @param axis
     * @param degrees
     * @return PAQuaternion
     */
    public static PAQuaternion fromAxisAngles(Vector axis, double degrees) {
	return fromAxisAngles(axis.getX(), axis.getY(), axis.getZ(), degrees);
    }

    /**
     * Creates a PAQuaternion from an axis an rotation
     * 
     * @param axisX
     * @param axisY
     * @param axisZ
     * @param degrees
     * @return PAQuaternion
     */
    public static PAQuaternion fromAxisAngles(double axisX, double axisY, double axisZ, double degrees) {
	double r = 0.5 * Math.toRadians(degrees % 360);
	double f = Math.sin(r);
	return new PAQuaternion(f * axisX, f * axisY, f * axisZ, Math.cos(r));
    }

    private static PAQuaternion lerp(PAQuaternion from, PAQuaternion to, double amountFrom, double amountTo) {
	return new PAQuaternion(amountFrom * from.a + amountTo * to.a, amountFrom * from.b + amountTo * to.b, amountFrom * from.c + amountTo * to.c, amountFrom * from.d + amountTo * to.d);
    }

    /**
     * Lerp between two PAQuaternion
     * 
     * @param from
     * @param to
     * @param amount
     *            A value between 0.0 and 1.0
     * @return PAQuaternion
     */
    public static PAQuaternion lerp(PAQuaternion from, PAQuaternion to, double amount) {
	return lerp(from, to, 1.0 - amount, amount);
    }
}