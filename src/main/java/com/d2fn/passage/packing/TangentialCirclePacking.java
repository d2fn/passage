package com.d2fn.passage.packing;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.d2fn.passage.geometry.PackedCircle;
import com.d2fn.passage.geometry.PolarPoint;
import com.d2fn.passage.geometry.Projectable2D;
import com.d2fn.passage.geometry.Rect2D;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static com.d2fn.passage.Sketch.*;

public class TangentialCirclePacking {

    private final List<PackedCircle> circles;
    private int index = 0;
    private Rect2D viewport;
    private Function<Projectable2D, Float> rFunction;
    private float dr, minR;
    private Color c0, cf;

    public TangentialCirclePacking(Rect2D viewport, Projectable2D seed, Function<Projectable2D, Float> rFunction, float dr, float minR, Color c0, Color cf) {
        this.rFunction = rFunction;
        this.dr = dr;
        this.minR = minR;
        this.viewport = viewport;
        this.c0 = c0;
        this.cf = cf;
        circles = Lists.newArrayList();
        PackedCircle c = new PackedCircle(seed.x(), seed.y(), rFunction.apply(seed), c0);
        c.targetColor(cf);
        circles.add(c);
        index = 0;
    }

    public List<PackedCircle> get() {
        return circles;
    }

    public void update() {
        for(PackedCircle c : circles) {
            c.update();
        }
    }

    public void step() {
        Random rand = new Random(System.currentTimeMillis());
        PackedCircle parent = circles.get(index);
        for(float r = rFunction.apply(parent.getCenter()); r >= minR; r -= dr) {
            float t0 = rand.nextFloat()*TWO_PI;
            float tf = t0 + TWO_PI;
            float dt = atan(dr/(r+parent.getTargetRadius()));
            for(float t = t0; t < tf; t += dt) {
                float r0 = parent.getRadius();
                Projectable2D p0 = parent.getCenter().add(new PolarPoint(r0, t));
                Projectable2D pf = parent.getTargetCenter().add(new PolarPoint(parent.getTargetRadius() + r, t));
                PackedCircle c = new PackedCircle(p0.x(), p0.y(), 0, c0);
                c.tweenTo(pf);
                c.targetColor(cf);
                c.targetRadius(r);
                if(viewport.contains(pf)) {
                    if(!c.collidesWith(circles)) {
                        circles.add(c);
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
}
