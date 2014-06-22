package com.d2fn.passage.geometry;

import java.util.List;

import static com.d2fn.passage.Sketch.dist;

/**
 * Circle
 * @author Dietrich Featherston
 */
public class Circle {

    private final Projectable2D center;
    private final float radius;

    public Circle(Projectable2D center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Projectable2D getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public Circle shrink() {
        return new Circle(center, radius-0.5f);
    }

    public boolean insideAny(List<Circle> env) {
        for(Circle c : env) {
            if(dist(c.center, center) < c.radius) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesWith(List<Circle> env) {
        for(Circle c : env) {
            if(dist(c.center, center) < radius + c.radius) {
                return true;
            }
        }
        return false;
    }
}
