package io.measures.passage.geometry;

import static io.measures.passage.Sketch.*;

public class SphericalPoint implements Projectable3D {

    private float r, phi, theta;

    public SphericalPoint(float r, float phi, float theta) {
        this.r = r;
        this.phi = phi;
        this.theta = theta;
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
}

