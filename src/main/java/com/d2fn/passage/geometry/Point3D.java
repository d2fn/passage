package com.d2fn.passage.geometry;

import static com.d2fn.passage.Sketch.cos;
import static com.d2fn.passage.Sketch.sin;

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

    public Projectable3D sub(Projectable3D b) {
        return new Point3D(x() - b.x(), y() - b.y(), z() - b.z());
    }

    public Projectable2D sub(Projectable2D b) {
        return new Point2D(x() - b.x(), y() - b.y());
    }

    public Projectable2D mid(Projectable2D b) {
        return new Point2D((x() + b.x())/2, (y() + b.y())/2);
    }

    public Projectable3D scale(float amt) {
        return new Point3D(x * amt, y * amt, z*amt);
    }

    public Projectable3D rotateX(float theta) {
        final float co = cos(theta);
        final float si = sin(theta);
        return
                new Point3D(
                        x,
                        si*z + co*y,
                        co*z - si*y
                );
    }

    public Projectable3D rotateY(float theta) {
        final float co = cos(theta);
        final float si = sin(theta);
        return
                new Point3D(
                        co*x - si*z,
                        y,
                        si*x + co*z
                );
    }

    public Projectable3D rotateZ(float theta) {
        final float co = cos(theta);
        final float si = sin(theta);
        return
            new Point3D(
                    co*x - si*y,
                    si*x + co*y,
                    z
            );
    }

    @Override
    public Projectable3D rotate(float xradians, float yradians, float zradians) {
        return rotateX(xradians).rotateX(yradians).rotateZ(zradians);
    }

    @Override
    public Projectable3D rotate(Projectable3D angleVec) {
        return rotate(angleVec.x(), angleVec.y(), angleVec.z());
    }

    public static final Point3D origin = new Point3D(0, 0, 0);
}
