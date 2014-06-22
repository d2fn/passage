package com.d2fn.passage.geometry;

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

    @Override
    public Projectable3D add(Projectable3D p) {
        return new Point3D(x() + p.x(), y() + p.y(), z() + p.z());
    }

    public Projectable2D add(Projectable2D b) {
        return new Point2D(x() + b.x(), y() + b.y());
    }

    public Projectable2D sub(Projectable2D b) {
        return new Point2D(x() - b.x(), y() - b.y());
    }

    public Projectable2D mid(Projectable2D b) {
        return new Point2D((x() + b.x())/2, (y() + b.y())/2);
    }

    public Projectable2D scale(float amt) {
        return new Point2D(x * amt, y * amt);
    }
}
