package com.d2fn.passage.function;

/**
 * LFunction
 * @author Dietrich Featherston
 */
public interface LFunction {
    /**
     * @param theta - angle in radians
     * @return a value making the function continuous for all values of theta [0, Inf]
     */
    public float call(float theta);
}
