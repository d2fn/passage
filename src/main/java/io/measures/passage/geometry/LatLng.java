package io.measures.passage.geometry;

import static io.measures.passage.Sketch.*;

/**
 * LatLng
 * @author Dietrich Featherston
 */
public class LatLng implements Projectable3D {

    private final float r;
    private final float lat;
    private final float lng;

    private final SphericalPoint p;

    public LatLng(float r, float lat, float lng) {
        this.r = r;
        this.lat = lat;
        this.lng = lng;

        float phi = radians(lng);
        float theta = map(radians(lat), -PI/2, PI/2, 0, PI);
        this.p = new SphericalPoint(r, phi, theta);
    }

    public float r() {
        return r;
    }

    public float lat() {
        return lat;
    }

    public float lng() {
        return lng;
    }

    @Override
    public float x() {
        return p.x();
    }

    @Override
    public float y() {
        return p.y();
    }

    @Override
    public float z() {
        return p.z();
    }
}
