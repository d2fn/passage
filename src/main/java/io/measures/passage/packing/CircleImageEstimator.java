package io.measures.passage.packing;

import com.google.common.collect.Lists;
import io.measures.passage.Sketch;
import io.measures.passage.geometry.Circle;
import io.measures.passage.geometry.Point2D;
import processing.core.PImage;

import java.util.List;

import static io.measures.passage.Sketch.*;

/**
 * CircleImageEstimator
 * @author Dietrich Featherston
 */
public class CircleImageEstimator {

    private final Sketch s;
    private final PImage img;
    private float maxRadius;
    private float curRadius;

    private final List<CircleImageInfo> candidates = Lists.newArrayList();
    private final List<CircleImageInfo> circleInfoList = Lists.newArrayList();
    private final List<Circle> circles = Lists.newArrayList();

    public CircleImageEstimator(Sketch s, PImage img, float maxRadius) {
        this.s = s;
        this.img = img;
        this.maxRadius = maxRadius;
        this.curRadius = maxRadius;
    }

    public List<CircleImageInfo> get() {
        return circleInfoList;
    }

    public List<CircleImageInfo> candidates() {
        return candidates;
    }

    public void step() {
        print("computing step " + circleInfoList.size() + " ... ");
        candidates.clear();
        CircleImageInfo best = null;
        while(candidates.size() < 10) {
            Circle c = new Circle(new Point2D(s.random(img.width), s.random(img.height)), curRadius);
            if(!c.insideAny(circles)) {
                while(c.collidesWith(circles)) {
                    c = c.shrink();
                }
                if(c.getRadius() > 0) {
                    // now that we have a non-overlapping circle, see how good of a fit it is
                    CircleImageInfo info = new CircleImageInfo(c);
                    candidates.add(info);
                }
            }
        }
        for(CircleImageInfo info : candidates) {
            if(best == null || info.betterFitThan(best)) {
                best = info;
            }
        }
        if(best != null) {
            circleInfoList.add(best);
            circles.add(best.getCircle());
        }
//        curRadius -= 0.5f;
        println("done");
    }

    public class CircleImageInfo {

        private final Circle c;
        private float r=0, g=0, b=0;
        private float lum = 0;
        private int n = 0;
        private float dist = 0;

        public CircleImageInfo(Circle c) {
            this.c = c;
            // compute average brightness
            for(int x = round(c.getCenter().x()-c.getRadius()); x < round(c.getCenter().x()+c.getRadius()); x++) {
                for(int y = round(c.getCenter().y()-c.getRadius()); y < round(c.getCenter().y()+c.getRadius()); y++) {
                    if(x >= 0 && x < img.width && y >= 0 && y < img.height) {
                        if(dist(new Point2D(x, y), c.getCenter()) <= c.getRadius()) {
                            int clr = img.get(x, y);
                            r += s.red(clr);
                            g += s.green(clr);
                            b += s.blue(clr);
                            lum += s.brightness(clr);
                            n++;
                        }
                    }
                }
            }
            if(n > 0) {
                r /= n;
                g /= n;
                b /= n;
                lum /= n;
                // sum the differences of pixel colors to average
                for(int x = round(c.getCenter().x()-c.getRadius()); x < round(c.getCenter().x()+c.getRadius()); x++) {
                    for(int y = round(c.getCenter().y()-c.getRadius()); y < round(c.getCenter().y()+c.getRadius()); y++) {
                        if(x >= 0 && x < img.width && y >= 0 && y < img.height) {
                            if(dist(new Point2D(x, y), c.getCenter()) <= c.getRadius()) {
                                dist += sqrt(pow((s.brightness(img.get(x, y)) - lum), 2));
                            }
                        }
                    }
                }
            }
        }

        public Circle getCircle() {
            return c;
        }

        public int getColor() {
            return s.color(r, g, b);
        }

        public float getDistance() {
            return dist;
        }

        public float getMeanDistance() {
            return dist/n;
        }

        public boolean isValid() {
            return n > 0;
        }

        public boolean betterFitThan(CircleImageInfo c) {
            return isValid() && getMeanDistance() < c.getMeanDistance();
        }
    }
}
