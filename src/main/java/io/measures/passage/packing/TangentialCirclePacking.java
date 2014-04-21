package io.measures.passage.packing;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.measures.passage.geometry.PackedCircle;
import io.measures.passage.geometry.PolarPoint;
import io.measures.passage.geometry.Projectable2D;
import io.measures.passage.geometry.Rect2D;

import java.awt.*;
import java.util.List;

import static io.measures.passage.Sketch.*;

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
        PackedCircle parent = circles.get(index);
        for(float r = rFunction.apply(parent.getCenter()); r > minR; r -= dr) {
            float dt = atan(dr/(r+parent.getTargetRadius()));
            for(float t = 0; t < TWO_PI; t += dt) {
                float r0 = parent.getTargetRadius();
                Projectable2D p0 = parent.getTargetCenter().add(new PolarPoint(r0, t));
                Projectable2D pf = p0.add(new PolarPoint(r, t));
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
        update();
    }
}
