package io.measures.passage.geometry;

/**
 * Triangle3D
 * @author Dietrich Featherston
 */
public class Triangle3D {
    private final Projectable3D a, b, c;

    public Triangle3D(Projectable3D a, Projectable3D b, Projectable3D c) {
        this.a = a;
        this.b = b;
        this.c = c;
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

    public Point3D center() {
        float x = (a.x() + b.x() + c.x())/3;
        float y = (a.y() + b.y() + c.y())/3;
        float z = (a.z() + b.z() + c.z())/3;
        return new Point3D(x, y, z);
    }
}
