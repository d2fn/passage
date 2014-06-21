package io.measures.passage.geometry;

import static io.measures.passage.Sketch.*;

/**
 * Line2D
 * @author Dietrich Featherston
 */
public class Line2D {
    private final Projectable2D a;
    private final Projectable2D b;

    public Line2D(float x1, float y1, float x2, float y2) {
        this(new Point2D(x1, y1), new Point2D(x2, y2));
    }

    public Line2D(Projectable2D a, Projectable2D b) {
        this.a = a;
        this.b = b;
    }

    public Projectable2D a() {
        return a;
    }

    public Projectable2D b() {
        return b;
    }

    public float getLength() {
        return dist(a, b);
    }

    public Projectable2D mid() {
        return a.mid(b);
    }

    public Line2D scale(float amt) {
        return new Line2D(a.scale(amt), b.scale(amt));
    }
}
