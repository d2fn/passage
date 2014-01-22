package com.d2fn.passage.sketches;

import org.measures.passage.Sketch;

/**
 * TestSketch
 * @author Dietrich Featherston
 */
public class TestSketch extends Sketch {

    @Override
    public void setup() {
        initSize(P2D);
        printenv();
    }

    @Override
    public void renderFrame() {
        background(0);
        colorMode(RGB, 255);
        stroke(100, 100, 100);
        line(0, 0, width, height);
        colorMode(HSB, 360);
        fill(frameCount%360, 360, 360);
        ellipse(mouseX, mouseY, 10, 20);
    }
}
