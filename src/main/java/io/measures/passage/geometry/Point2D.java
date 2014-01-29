package io.measures.passage.geometry;

/**
 * Point2D
 * @author Dietrich Featherston
 */
public class Point2D implements Projectable2D {

    private final float x, y;

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x() { return x; }
    public float y() { return y; }

    public float[] toArray() {
        return new float[] { x(), y() };
    }
}
