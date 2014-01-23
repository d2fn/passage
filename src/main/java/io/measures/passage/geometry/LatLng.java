package io.measures.passage.geometry;

import static io.measures.passage.Sketch.*;

/**
 * LatLng
 * @author Dietrich Featherston
 */
public class LatLng {

    private final float lat;
    private final float lng;

    public LatLng(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public float lat() {
        return lat;
    }

    public float lng() {
        return lng;
    }

    public SphericalPoint toSpherical(float r) {
        float phi = radians(lng);
        float theta = map(radians(lat), -PI/2, PI/2, 0, PI);
        return new SphericalPoint(r, phi, theta);
    }
}
