package io.measures.passage.geometry;

import com.google.common.base.Objects;

/**
 * Point2D
 * @author Dietrich Featherston
 */
public class Point2D extends AbstractProjectable2D {

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

    @Override
    public String toString() {
        return Objects.toStringHelper(Point2D.class).add("x", x).add("y", y).toString();
    }

    private static final Point2D ORIGIN = new Point2D(0, 0);
}
