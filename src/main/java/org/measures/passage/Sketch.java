package org.measures.passage;

import processing.core.PApplet;

import org.measures.passage.geometry.SphericalPoint;

/**
 * Sketch
 * @author Dietrich Featherston
 */
public class Sketch extends PApplet {

    @Override
    public final void draw() {
        beforeDraw();
        super.draw();
        sketch();
        afterDraw();
    }

    protected void beforeDraw() {}
    protected void sketch() {}
    protected void afterDraw() {}

    void setup720p(String renderer) {
        size(1280, 720, renderer);
    }

    void setup1440p(String renderer) {
        size(2560, 1440, renderer);
    }

    void vertex(SphericalPoint p) {
        vertex(p.x(), p.y(), p.z());
    }

    void line(SphericalPoint a, SphericalPoint b) {
        line(a.x(), b.y(), b.z(), b.x(), b.y(), b.z());
    }

    void triangles(SphericalPoint a, SphericalPoint b, SphericalPoint c, SphericalPoint d) {
        beginShape(TRIANGLES);
        vertex(a);
        vertex(b);
        vertex(c);
        vertex(d);
        vertex(c);
        vertex(b);
        endShape();
    }
}
