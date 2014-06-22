package com.d2fn.passage.mesh;

import com.google.common.collect.Lists;
import com.d2fn.passage.geometry.Line2D;
import com.d2fn.passage.geometry.Point2D;
import com.d2fn.passage.geometry.Projectable2D;
import com.d2fn.passage.geometry.Triangle2D;
import quickhull3d.Point3d;

import java.util.List;

import static com.d2fn.passage.Sketch.*;

/**
 * Delaunay
 * @author Dietrich Featherston
 */
public class Delaunay {

    private final List<Line2D> edges;
    private final int[][] links;
    private float maxLength;
    private final List<Triangle2D> faces;

    public Delaunay(List<? extends Projectable2D> points) {
        float[][] _p = new float[points.size()][2];
        for(int i = 0; i < points.size(); i++) {
            _p[i][0] = points.get(i).x();
            _p[i][1] = points.get(i).y();
        }
        megamu.mesh.Delaunay d = new megamu.mesh.Delaunay(_p);
        edges = Lists.newArrayListWithCapacity(d.edgeCount());
        for(float[] edge : d.getEdges()) {
            Line2D line = new Line2D(edge[0], edge[1], edge[2], edge[3]);
            edges.add(line);
            maxLength = max(maxLength, line.getLength());
        }
        links = d.getLinks();

        Point3d[] vertices = d.getHullVertices();
        faces = Lists.newArrayList();
        for(int[] face : d.getFaces()) {
            if(face[0] < vertices.length && face[1] < vertices.length && face[2] < vertices.length) {
                Projectable2D a = point2D(vertices[face[0]]);
                Projectable2D b = point2D(vertices[face[1]]);
                Projectable2D c = point2D(vertices[face[2]]);
                faces.add(new Triangle2D(a, b, c));
            }
        }
    }

    private static Projectable2D point2D(Point3d p) {
        return new Point2D((float)p.x, (float)p.y);
    }

    public List<Line2D> getEdges() {
        return edges;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public int[][] getLinks() {
        return links;
    }

    public List<Triangle2D> getFaces() {
        return faces;
    }

    public float getMaxLength() {
        return maxLength;
    }
}
