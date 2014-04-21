package io.measures.passage.geometry;

/**
 * Triangle3D
 * @author Dietrich Featherston
 */
public class Triangle3D {

    private final Projectable3D a, b, c, centroid;

    public Triangle3D(Projectable3D a, Projectable3D b, Projectable3D c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.centroid = new Point3D(
                (a.x() + b.x() + c.x())/3f,
                (a.y() + b.y() + c.y())/3f,
                (a.z() + b.z() + c.z())/3f);
    }

    public Projectable3D a() {
        return a;
    }

    public Projectable3D b() {
        return b;
    }

    public Projectable3D c() {
        return c;
    }

    public Projectable3D centroid() {
        return centroid;
    }
}
