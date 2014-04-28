package io.measures.passage.packing;

import com.google.common.collect.Lists;
import io.measures.passage.Sketch;
import io.measures.passage.geometry.ImagePackedCircle;
import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.PolarPoint;
import io.measures.passage.geometry.Projectable2D;
import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

import static io.measures.passage.Sketch.*;

public class TangentialImageCirclePacking {

    private final Sketch s;
    private final PImage img;
    private final List<ImagePackedCircle> circles;
    private int index = 0;
    private float maxR, dr, minR, errorThreshold;
    private Color c0;

    private ImageCircleIndex imageIndex;
    private Future indexLoader;

    public TangentialImageCirclePacking(Sketch s, PImage img, String imgName, float maxR, float dr, float minR, int errorThreshold, Color c0) {
        this.s = s;
        this.img = img;
        this.maxR = maxR;
        this.dr = dr;
        this.minR = minR;
        this.c0 = c0;
        this.errorThreshold = errorThreshold;
        circles = Lists.newArrayList();
        circles.add(new ImagePackedCircle(img.width/2, img.height/2, minR, c0));
        imageIndex = new ImageCircleIndex();
        indexLoader = imageIndex.load(s, imgName, img, minR, maxR, errorThreshold);
        index = 0;
    }

    public List<ImagePackedCircle> get() {
        return circles;
    }

    public void step() {
        Random rand = new Random(System.currentTimeMillis());
        ImagePackedCircle parent = circles.get(index);
        for(float r = maxR; r >= minR; r -= dr) {
            float t0 = rand.nextFloat()*TWO_PI;
            float tf = t0 + TWO_PI;
            float dt = atan(dr/(r+parent.getTargetRadius()));
            for(float t = t0; t < tf; t += dt) {
                float r0 = parent.getRadius();
                Projectable2D p0 = parent.getCenter().add(new PolarPoint(r0, t));
                Projectable2D pf = parent.getTargetCenter().add(new PolarPoint(parent.getTargetRadius() + r, t));
                if(imageIndex.check(pf.x(), pf.y(), r, errorThreshold)) {
                    ImagePackedCircle c = new ImagePackedCircle(p0.x(), p0.y(), 0, c0);
                    c.tweenTo(pf);
                    c.targetRadius(r);
                    if(img.contains(pf)) {
                        if(!c.collidesWith(circles)) {
                            c.targetColor(new Color(getAverageColor(c)));
                            circles.add(c);
                        }
                    }
                }
            }
        }
        if(index < circles.size()-1) {
            index++;
        }
    }

    public void step(int n) {
        for(int i = 0; i < n; i++) {
            step();
        }
    }

    public void update() {
        for(ImagePackedCircle c : circles) {
            c.update();
        }
    }


    public int getAverageColor(ImagePackedCircle c) {
        float r = 0, g = 0, b = 0;
        int n = 0;
        // compute average brightness
        for(int x = round(c.getTargetCenter().x()-c.getTargetRadius()); x < round(c.getTargetCenter().x()+c.getTargetRadius()); x++) {
            for(int y = round(c.getTargetCenter().y()-c.getTargetRadius()); y < round(c.getTargetCenter().y()+c.getTargetRadius()); y++) {
                if(x >= 0 && x < img.width && y >= 0 && y < img.height) {
                    if(dist(new Point2D(x, y), c.getTargetCenter()) <= c.getTargetRadius()) {
                        int clr = img.get(x, y);
                        r += s.red(clr);
                        g += s.green(clr);
                        b += s.blue(clr);
                        n++;
                    }
                }
            }
        }
        if(n > 0) {
            r /= n;
            g /= n;
            b /= n;
            return s.color(r, g, b);
        }
        return s.color(0);
    }

    public int getCandidateCount(int x, int y) {
        return imageIndex.getCount(x, y, round(errorThreshold));
    }

    public boolean isDone() {
        return indexLoader.isDone();
    }
}

