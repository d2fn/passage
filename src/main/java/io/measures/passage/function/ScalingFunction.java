package io.measures.passage.function;

/**
 * ScalingFunction
 * @author Dietrich Featherston
 */
public class ScalingFunction implements LFunction {

    private final float scale;
    private final LFunction inner;

    public ScalingFunction(float scale, LFunction inner) {
        this.scale = scale;
        this.inner = inner;
    }

    @Override
    public float call(float theta) {
        return scale * inner.call(theta);
    }
}
