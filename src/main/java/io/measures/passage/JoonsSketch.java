package io.measures.passage;

import joons.JoonsRenderer;
import io.measures.passage.geometry.SphericalPoint;
import io.measures.passage.shapes.Rock;
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
    public void setup() {
        initSize(P3D);
        jr = new JoonsRenderer(this);
        jr.setSampler("bucket");
        jr.setSizeMultiplier(1);
        jr.setAA(0, 2, 4);
        noiseDetail(3, 0.5f);
        randomSeed(0);
        noiseSeed(1);
        frameRate(1);
    }

    @Override
    public void beforeFrame() {
        super.beforeFrame();
    }

    @Override
    public void afterFrame() {
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

    /**
     * default lights
     */
    @Override
    public void lights() {
        jr.fill("light", 30, 30, 30, 64);
        pushMatrix();
        translate(100, -50, 300);
        sphere(80);
        popMatrix();
    }

    /**
     * default camera
     */
    @Override
    public void camera() {
        //Camera Setting.
        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = 70;
        float centerX = 0;
        float centerY = -1;
        float centerZ = 0;

        float upX = 0;
        float upY = 0;
        float upZ = -1;
        float fov = PI/3;
        float zNear = 1;
        float zFar = 10000;

        camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        perspective(fov, getAspectRatio(), zNear, zFar);
    }

    public void panelXY(float w, float h) {
        beginShape(QUADS);
        vertex(-w/2, -h/2, 0);
        vertex(-w/2,  h/2, 0);
        vertex( w/2,  h/2, 0);
        vertex( w/2, -h/2, 0);
        endShape();
    }

    public void panelYZ(float w, float h) {
        beginShape(QUADS);
        vertex(0, -w/2, -h/2);
        vertex(0, -w/2,  h/2);
        vertex(0,  w/2,  h/2);
        vertex(0,  w/2, -h/2);
        endShape();
    }
}
