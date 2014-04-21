package io.measures.passage.packing;

import com.google.common.collect.Lists;
import io.measures.passage.Sketch;
import io.measures.passage.geometry.Circle;
import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.PolarPoint;
import io.measures.passage.geometry.Projectable2D;
import io.measures.passage.geometry.Rect2D;
import processing.core.PImage;

import java.util.List;

import static io.measures.passage.Sketch.*;

public class TangentialImageCirclePacking implements CirclePacking {

    private final Sketch s;
    private final PImage img;
    private final List<Circle> circles;
    private int index = 0;
    private float r0, dr, minR, dt, errorThreshold;

    public TangentialImageCirclePacking(Sketch s, PImage img, float r0, float dr, float minR, float dt, float errorThreshold) {
        this.s = s;
        this.img = img;
        this.r0 = r0;
        this.dr = dr;
        this.minR = minR;
        this.dt = dt;
        this.errorThreshold = errorThreshold;
        circles = Lists.newArrayList();
        circles.add(new Circle(new Point2D(0, 0), r0));
        index = 0;
    }

    @Override
    public List<Circle> get() {
        return circles;
    }

    @Override
    public void step() {
//        for(float r = 0.5f*r0*(1+sin(s.frameCount * TWO_PI/1000)) + minR; r >= minR; r -= dr) {
        for(float r = 0.5f*r0*(1+sin(dist(new Point2D(0, 0), circles.get(index).getCenter()) * TWO_PI/200)) + minR; r >= minR; r -= dr) {
            for(float t = 0; t < TWO_PI; t += dt) {
                Circle c = newCandidate(circles.get(index), r, t);
                if(inbounds(c.getCenter()) && !c.collidesWith(circles)) {
                    float e = brightnessError(c);
                    if(e < errorThreshold) {
                        circles.add(c);
                    }
                }
            }
        }
        index++;
    }

    private boolean inbounds(Projectable2D p) {
        return p.within(new Rect2D(0, 0, img.width, img.height));
    }

    private Circle newCandidate(Circle parent, float r, float t) {
        return new Circle(parent.getCenter().add(new PolarPoint(parent.getRadius()+r, t)), r);
    }

    private float brightnessError(Circle c) {
        float b = 0f;
        int n = 0;
        // compute average brightness
        for(int x = round(c.getCenter().x()-c.getRadius()); x < round(c.getCenter().x()+c.getRadius()); x++) {
            for(int y = round(c.getCenter().y()-c.getRadius()); y < round(c.getCenter().y()+c.getRadius()); y++) {
                if(x >= 0 && x < img.width && y >= 0 && y < img.height) {
                    if(dist(new Point2D(x, y), c.getCenter()) <= c.getRadius()) {
                        int clr = img.get(x, y);
                        b += s.brightness(clr);
                        n++;
                    }
                }
            }
        }
        b /= n;
        // compute error
        float error = 0f;
        for(int x = round(c.getCenter().x()-c.getRadius()); x < round(c.getCenter().x()+c.getRadius()); x++) {
            for(int y = round(c.getCenter().y()-c.getRadius()); y < round(c.getCenter().y()+c.getRadius()); y++) {
                if(x >= 0 && x < img.width && y >= 0 && y < img.height) {
                    if(dist(new Point2D(x, y), c.getCenter()) <= c.getRadius()) {
                        error += sqrt(pow((s.brightness(img.get(x, y)) - b), 2));
                    }
                }
            }
        }
        return error;
    }

    public int getAverageColor(Circle c) {
        float r = 0, g = 0, b = 0;
        int n = 0;
        // compute average brightness
        for(int x = round(c.getCenter().x()-c.getRadius()); x < round(c.getCenter().x()+c.getRadius()); x++) {
            for(int y = round(c.getCenter().y()-c.getRadius()); y < round(c.getCenter().y()+c.getRadius()); y++) {
                if(x >= 0 && x < img.width && y >= 0 && y < img.height) {
                    if(dist(new Point2D(x, y), c.getCenter()) <= c.getRadius()) {
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
}
