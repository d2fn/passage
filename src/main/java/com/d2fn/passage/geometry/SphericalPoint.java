package com.d2fn.passage.geometry;

import com.google.common.base.Objects;

import static com.d2fn.passage.Sketch.*;

public class SphericalPoint implements Projectable3D {

    private float r, phi, theta;

    public SphericalPoint(float r, float phi, float theta) {
        this.r = r;
        this.phi = phi;
        this.theta = theta;
    }

    public float r() {
        return r;
    }

    public float phi() {
        return phi;
    }

    public float theta() {
        return theta;
    }

    // todo - make latlng its own Projectable3D impl
    public SphericalPoint latlng(float r, float lat, float lng) {
        this.r = r;
        this.phi = radians(lng);
        this.theta = map(radians(lat), -PI/2, PI/2, 0, PI);
        return this;
    }

    public SphericalPoint extend(float dr) {
        return new SphericalPoint(r + dr, phi, theta);
    }

    public float x() { return r * sin(theta) * cos(phi); }
    public float y() { return r * sin(theta) * sin(phi); }
    public float z() { return r * cos(theta); }

    private Point3D point3d() {
        return new Point3D(x(), y(), z());
    }

    @Override
    public Projectable3D add(Projectable3D p) {
        return new Point3D(x() + p.x(), y() + p.y(), z() + p.z());
    }

    @Override
    public Projectable3D sub(Projectable3D p) {
        return point3d().sub(p);
    }

    @Override
    public Projectable3D rotateX(float theta) {
        return point3d().rotateX(theta);
    }

    @Override
    public Projectable3D rotateY(float theta) {
        return point3d().rotateZ(theta);
    }

    @Override
    public Projectable3D rotateZ(float theta) {
        return point3d().rotateZ(theta);
    }

    @Override
    public Projectable3D rotate(float xradians, float yradians, float zradians) {
        return rotateX(xradians).rotateX(yradians).rotateZ(zradians);
    }

    @Override
    public Projectable3D rotate(Projectable3D angleVec) {
        return rotate(angleVec.x(), angleVec.y(), angleVec.z());
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

    public Projectable3D scale(float amt) {
        return new SphericalPoint(r * amt, phi, theta);
    }

    public static SphericalPoint fromXYZ(float x, float y, float z) {
        float r = dist(0, 0, 0, x, y, z);
        float phi = atan(y/x);
        if(phi > TWO_PI) {
            phi = phi % TWO_PI;
        }
        while(phi < 0) {
            phi = TWO_PI + phi;
        }
        float theta = acos(z/r);
        return new SphericalPoint(r, phi, theta);
    }

    public String toString() {
        return toStringXY();
    }

    public String toStringXY() {
        return Objects.toStringHelper(getClass()).add("x", x()).add("y", y()).add("z", z()).toString();
    }

    public String toStringR() {
        return Objects.toStringHelper(getClass()).add("r", r).add("phi", phi).add("theta", theta).toString();
    }

    private static final SphericalPoint origin = new SphericalPoint(0, 0, 0);
}

