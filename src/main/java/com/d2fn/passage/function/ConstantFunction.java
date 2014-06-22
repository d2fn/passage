package com.d2fn.passage.function;

/**
 * ConstantFunction
 * @author Dietrich Featherston
 */
public class ConstantFunction implements LFunction {

    private final float c;

    public ConstantFunction(float c) {
        this.c = c;
    }

    @Override
    public float call(float theta) {
        return c;
    }
}
