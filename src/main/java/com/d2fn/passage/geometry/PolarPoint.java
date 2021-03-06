package com.d2fn.passage.geometry;

import static com.d2fn.passage.Sketch.*;

/**
 * PolarPoint
 * @author Dietrich Featherston
 */
public class PolarPoint extends AbstractProjectable2D {

    private final float r, theta;

    public PolarPoint(float r, float theta) {
        this.r = r;
        this.theta = theta;
    }

    @Override
    public float x() {
        return r * cos(theta);
    }

    @Override
    public float y() {
        return r * sin(theta);
    }

    private static final PolarPoint origin = new PolarPoint(0, 0);
}
