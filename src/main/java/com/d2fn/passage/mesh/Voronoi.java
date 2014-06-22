package com.d2fn.passage.mesh;

import com.d2fn.passage.geometry.Point2D;

import java.util.List;

/**
 * Voronoi
 * @author Dietrich Featherston
 */
public class Voronoi {

    private final megamu.mesh.Voronoi v;

    public Voronoi(List<Point2D> points) {
        float[][] _p = new float[points.size()][2];
        for(int i = 0; i < points.size(); i++) {
            _p[i][0] = points.get(i).x();
            _p[i][1] = points.get(i).y();
        }
        v = new megamu.mesh.Voronoi(_p);
    }
}
