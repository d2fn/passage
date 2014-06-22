package com.d2fn.passage.geo;

import com.d2fn.passage.geometry.Projectable2D;

/**
 * GeoProjection
 * @author Dietrich Featherston
 */
public interface GeoProjection {
    public Projectable2D screen(float lat, float lng);
    public GeoProjection grow(float xamt, float yamt);
}
