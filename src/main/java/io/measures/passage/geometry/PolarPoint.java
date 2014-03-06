package io.measures.passage.geometry;

import static io.measures.passage.Sketch.*;

/**
 * PolarPoint
 * @author Dietrich Featherston
 */
public class PolarPoint implements Projectable2D {

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

    @Override
    public Projectable2D add(Projectable2D b) {
        return new Point2D(x() + b.x(), y() + b.y());
    }

    @Override
    public Projectable2D sub(Projectable2D b) {
        return new Point2D(x() - b.x(), y() - b.y());
    }

    @Override
    public Point2D mid(Projectable2D b) {
        return new Point2D((x() + b.x())/2f, (x() + b.y())/2f);
    }

    @Override
    public Projectable2D scale(float amt) {
        return new PolarPoint(r*amt, theta);
    }
}
