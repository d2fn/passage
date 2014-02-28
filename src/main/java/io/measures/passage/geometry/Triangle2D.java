package io.measures.passage.geometry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Triangle;

import static io.measures.passage.Sketch.*;

/**
 * Triangle2D
 * @author Dietrich Featherston
 */
public class Triangle2D {

    protected final Projectable2D a, b, c;

    public Triangle2D(Projectable2D a, Projectable2D b, Projectable2D c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Projectable2D a() {
        return a;
    }

    public Projectable2D b() {
        return b;
    }

    public Projectable2D c() {
        return c;
    }

    public Point2D centroid() {
        float x = (a.x() + b.x() + c.x())/3;
        float y = (a.y() + b.y() + c.y())/3;
        return new Point2D(x, y);
    }

    public Projectable2D incenter() {
        Triangle t = jtsTri();
        Coordinate c = t.inCentre();
        return new Point2D((float)c.x, (float)c.y);
    }

    public float inradius() {
        Triangle t = jtsTri();
        return 2 * (float)t.area() / perimeter();
    }

    private Triangle jtsTri() {
        return new Triangle(
                new Coordinate((double)a.x(), (double)a.y()),
                new Coordinate((double)b.x(), (double)b.y()),
                new Coordinate((double)c.x(), (double)c.y()));
    }

    public float perimeter() {
        return dist(a, b) + dist(b, c) + dist(c, a);
    }
}
