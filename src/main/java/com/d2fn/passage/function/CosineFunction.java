package com.d2fn.passage.function;

/**
 * CosineFunction
 * @author Dietrich Featherston
 */
public class CosineFunction implements LFunction {

    private final LFunction amplitude;
    private final LFunction lambda;
    private final LFunction phase;
    private final LFunction offset;

    public CosineFunction(LFunction amplitude, LFunction lambda, LFunction phase, LFunction offset) {
        this.amplitude = amplitude;
        this.lambda = lambda;
        this.phase = phase;
        this.offset = offset;
    }

    @Override
    public float call(float theta) {
        return amplitude.call(theta) * (float)Math.sin(phase.call(theta)+ lambda.call(theta)*theta) + offset.call(theta);
    }
}
