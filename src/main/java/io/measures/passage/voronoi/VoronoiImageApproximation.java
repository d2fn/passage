package io.measures.passage.voronoi;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.measures.passage.Sketch;
import io.measures.passage.geometry.Model2D;
import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.Point3D;
import io.measures.passage.geometry.Triangle2D;
import io.measures.passage.geometry.Triangle3D;
import megamu.mesh.MPolygon;
import megamu.mesh.Voronoi;
import processing.core.PImage;
import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;

import static processing.core.PApplet.*;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * VoronoiImageApproximation
 * @author Dietrich Featherston
 */
public class VoronoiImageApproximation implements Model2D {

    private final Sketch s;
    private final PImage img;
    private final LinkedList<Point2D> available;
    private final LinkedList<Point2D> voronoiPoints;
    private final Set<String> used;
    private final DecimalFormat df = new DecimalFormat("0.##");

    private MPolygon[] regions;

    private final float maxHeight;

    public VoronoiImageApproximation(Sketch s, PImage img, int brightnessThreshold, int weighting, float maxHeight) {
        this.s = s;
        this.img = img;
        this.maxHeight = maxHeight;
        // pick appropriate pixels where a voronoi point could be added
        // giving weight to brighter pixels
        voronoiPoints = Lists.newLinkedList();
        used = Sets.newHashSet();
        available = Lists.newLinkedList();
        for(int i = 0; i < img.width; i++) {
            for(int j = 0; j < img.height; j++) {
                float b = s.brightness(img.get(i, j));
                if(b > brightnessThreshold) {
                    int times = round(map(b, 0, 255, 0, weighting));
                    Point2D p = new Point2D(i, j);
                    for(int k = 0; k < times; k++) {
                        available.add(p);
                    }
                }
            }
        }

        // shuffle available points
        Collections.shuffle(available);
    }

    /**
     * @param n - number of steps to evolve
     */
    public void evolve(int n) {
        for(int i = 0; i < n; i++) {
            voronoiPoints.add(pop());
        }
        update();
    }

    private Point2D pop() {
        Point2D p;
        String key;
        do {
            p = available.poll();
            key = df.format(p.x()) + ":" + df.format(p.y());
        } while(used.contains(key));
        used.add(key);
        return p;
    }

    private void update() {
        if(voronoiPoints.isEmpty()) return;
        float[][] fpoints = new float[voronoiPoints.size()][2];
        for(int i = 0; i < voronoiPoints.size(); i++) {
            fpoints[i] = voronoiPoints.get(i).toArray();
        }
        print("building voronoi... ");
        Voronoi v = new Voronoi(fpoints);
        regions = v.getRegions();
        println("done");
    }

    private float z(float x, float y) {
        return (s.brightness(img.get(round(x), round(y)))/255f) * maxHeight;
    }

    @Override
    public Iterable<Triangle2D> getTriangles() {
        List<Triangle2D> out = Lists.newArrayList();
        for(MPolygon p : regions) {
            float[][] coords = p.getCoords();
            if(coords.length == 3) {
                out.add(
                        new Triangle2D(
                            new Point2D(coords[0][0], coords[0][1]),
                            new Point2D(coords[1][0], coords[1][1]),
                            new Point2D(coords[2][0], coords[2][1])
                        ));
            }
            else {
                QuickHull3D qh = new QuickHull3D(doubles(coords));
                qh.triangulate();
                Point3d[] vertices = qh.getVertices();
                int[][] faces = qh.getFaces(QuickHull3D.POINT_RELATIVE + QuickHull3D.CLOCKWISE);
                for(int i = 0; i < faces.length; i++) {
                    Point3d a = vertices[faces[i][0]];
                    Point3d b = vertices[faces[i][1]];
                    Point3d c = vertices[faces[i][2]];
                    out.add(
                            new Triangle2D(
                                new Point2D((float)a.x, (float)a.y),
                                new Point2D((float)b.x, (float)b.y),
                                new Point2D((float)c.x, (float)c.y)
                            ));
                }
            }
        }
        return out;
    }

    double[] doubles(float[][] f) {
        double[] d = new double[f.length*3];
        for(int i = 0; i < f.length; i++) {
            d[3*i  ] = f[i][0];
            d[3*i+1] = f[i][1];
            d[3*i+2] = z(f[i][0], f[i][1]);
        }
        return d;
    }

    public int getWidth() {
        return img.width;
    }

    public int getHeight() {
        return img.height;
    }
}
