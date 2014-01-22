package org.measures.passage;

import joons.JoonsRenderer;
import org.measures.passage.geometry.SphericalPoint;
import org.measures.passage.shapes.Rock;
import processing.event.KeyEvent;

/**
 * JoonsSketch
 * @author Dietrich Featherston
 */
public class JoonsSketch extends Sketch {

    protected JoonsRenderer jr;
    protected boolean triggerRender = false;
    protected boolean waitingForRender = false;

    @Override
    protected void beforeFrame() {
        super.beforeFrame();
    }

    @Override
    protected void afterFrame() {
        super.afterFrame();
        if(triggerRender && !jr.isRendering()) {
            triggerRender = false;
            waitingForRender = true;
            jr.render();
        }
        else if(jr.isRendered() && waitingForRender) {
            waitingForRender = false;
            snapshot();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);
        if (key == 'r' || key == 'R') {
            triggerRender = true;
        }
    }

    public void panelXY(float w, float h) {
        beginShape(QUADS);
        vertex(-w/2, -h/2, 0);
        vertex(-w/2,  h/2, 0);
        vertex( w/2,  h/2, 0);
        vertex( w/2, -h/2, 0);
        endShape();
    }

    public void rock(Rock r, int hi, int lo) {
        float dphi = PI/r.getDetail();
        float dtheta = 2*PI/r.getDetail();
        for(float phi = 0; phi <= TWO_PI; phi += dphi) {
            for(float theta = 0; theta <= PI; theta += dtheta) {
                SphericalPoint p1 = new SphericalPoint(r.getR(), phi, theta);
                SphericalPoint p2 = new SphericalPoint(r.getR(), phi+dphi, theta);
                SphericalPoint p3 = new SphericalPoint(r.getR(), phi, theta+dtheta);
                SphericalPoint p4 = new SphericalPoint(r.getR(), phi+dphi, theta+dtheta);
                float noise1 = noiseZ(r.getNoiseScale(), p1.x(), p1.y(), p1.z());
                float noise2 = noiseZ(r.getNoiseScale(), p2.x(), p2.y(), p2.z());
                float noise3 = noiseZ(r.getNoiseScale(), p3.x(), p3.y(), p3.z());
                float noise4 = noiseZ(r.getNoiseScale(), p4.x(), p4.y(), p4.z());
                float h1 = map(noise1, 0, 1, -r.getRDevation()/2, r.getRDevation()/2);
                float h2 = map(noise2, 0, 1, -r.getRDevation()/2, r.getRDevation()/2);
                float h3 = map(noise3, 0, 1, -r.getRDevation()/2, r.getRDevation()/2);
                float h4 = map(noise4, 0, 1, -r.getRDevation()/2, r.getRDevation()/2);
                p1 = p1.extend(h1);
                p2 = p2.extend(h2);
                p3 = p3.extend(h3);
                p4 = p4.extend(h4);
                int c = lerpColor(lo, hi, noise1);
                jr.fill("diffuse", red(c), green(c), blue(c));
                stroke(c);
                fill(c);
                triangles(p1, p2, p3, p4);
            }
        }
    }
}
