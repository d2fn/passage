package io.measures.passage.geo;

import io.measures.passage.geometry.Projectable2D;

/**
 * GeoProjection
 * @author Dietrich Featherston
 */
public interface GeoProjection {
    public Projectable2D screen(float lat, float lng);
    public GeoProjection grow(float xamt, float yamt);
}
