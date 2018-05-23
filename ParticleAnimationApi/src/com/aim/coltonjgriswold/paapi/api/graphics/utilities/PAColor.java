package com.aim.coltonjgriswold.paapi.api.graphics.utilities;


import java.util.Random;

public final class PAColor {

    private byte red, green, blue;
    
    /**
     * Represents a 24-bit RGB color
     * 
     * @param red Must be an integer ranging from 0-255
     * @param green Must be an integer ranging from 0-255
     * @param blue Must be an integer ranging from 0-255
     */
    public PAColor(int red, int green, int blue) {
	this.red = (byte) red;
	this.green = (byte) green;
	this.blue = (byte) blue;
    }
    
    /**
     * Gets red amount
     * 
     * @return Red amount
     */
    public int getRed() {
	return 255 & red;
    }
    
    /**
     * Gets green amount
     * 
     * @return Green amount
     */
    public int getGreen() {
	return 255 & green;
    }
    
    /**
     * Gets blue amount
     * 
     * @return Blue amount
     */
    public int getBlue() {
	return 255 & blue;
    }
    
    /**
     * Creates a new ParticleColor from three values
     * 
     * @param r Red amount
     * @param g Green amount
     * @param b Blue amount
     * @return A new ParticalColor
     */
    public static PAColor fromRGB(int r, int g, int b) {
	return new PAColor(r, g, b);
    }
    
    /**
     * Creates a new ParticleColor from a hex code as an integer IE '0xff0000' would be red
     * 
     * @param rgb A hexadeciml literal
     * @return A new ParticalColor
     */
    public static PAColor fromRGB(int rgb) {
	if ((rgb >> 24) != 0) {
	    return null;
	}
	return fromRGB(rgb >> 16 & 255, rgb >> 8 & 255, rgb >> 0 & 255);
    }
    
    /**
     * Get a random particle color
     * 
     * @return Randomly generated PaarticleColor
     */
    public static PAColor randomColor() {
	Random r = new Random();
	return fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }
}
