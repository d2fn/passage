package com.d2fn.passage.color;

/**
 * Spectrum
 * @author Dietrich Featherston
 */
public interface Spectrum {
    /**
     * @param value - a value normalized between 0 and 1
     * @return the color mapped to by this value
     */
    public int get(float value);
}
