package com.d2fn.passage.geometry;

import com.d2fn.passage.Integrator;

import java.awt.Color;
import java.util.List;

import static com.d2fn.passage.Sketch.*;

/**
 * PackedCircle
 * @author Dietrich Featherston
 */
public class PackedCircle {

    private final IntegratorPoint2D center;
    private final Integrator radius;
    private Integrator r,g,b;

    public PackedCircle(float x, float y, float radius, Color c) {
        this.center = new IntegratorPoint2D(x,y);
        this.radius = new Integrator(radius);
        this.r = new Integrator(c.getRed());
        this.g = new Integrator(c.getGreen());
        this.b = new Integrator(c.getBlue());
    }

    public float getRadius() {
        return radius.get();
    }

    public Projectable2D getCenter() {
        return center;
    }

    public Projectable2D getTargetCenter() {
        return center.target();
    }

    public float getTargetRadius() {
        return radius.target();
    }

    public void targetRadius(float radius) {
        this.radius.target(radius);
    }

    public void tweenTo(Projectable2D p) {
        this.center.tweenTo(p);
    }

    public void targetColor(Color c) {
        r.target(c.getRed());
        g.target(c.getGreen());
        b.target(c.getBlue());
    }

    public Color getColor() {
        return new Color(round(r.get()), round(g.get()), round(b.get()));
    }

    public void update() {
        center.update();
        radius.update();
        r.update();
        g.update();
        b.update();
    }

    public boolean collidesWith(List<PackedCircle> env) {
        for(PackedCircle c : env) {
            if(dist(c.center.target(), center.target()) < (radius.target() + c.radius.target())) {
                return true;
            }
        }
        return false;
    }
}
