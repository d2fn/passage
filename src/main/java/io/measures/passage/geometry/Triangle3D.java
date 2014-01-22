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
}
