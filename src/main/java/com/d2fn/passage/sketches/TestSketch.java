package com.d2fn.passage.sketches;

import io.measures.passage.Sketch;
import processing.core.PImage;

/**
 * TestSketch
 * @author Dietrich Featherston
 */
public class TestSketch extends Sketch {

    PImage img;

    @Override
    public void setup() {
        initSize(P2D);
        colorMode(RGB, 255);
        img = loadImage("raster.png");
        img.resize(img.width/3, img.height/3);
        printenv();
    }

    @Override
    public void renderFrame() {
        background(0);
        pushMatrix();
        translate(width/2-img.width/2, height/2-img.height/2);
        image(img, 0, 0);
        popMatrix();
        stroke(240, 100, 100);
        line(0, 0, width, height);
        colorMode(HSB, 360);
        fill(frameCount%360, 360, 360);
        ellipse(mouseX, mouseY, 10, 20);
    }
}
