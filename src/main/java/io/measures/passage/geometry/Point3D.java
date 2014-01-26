package io.measures.passage.geometry;

/**
 * Point3D
 * @author Dietrich Featherston
 */
public class Point3D implements Projectable3D {

    private final float x, y, z;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public float z() {
        return z;
    }
}
