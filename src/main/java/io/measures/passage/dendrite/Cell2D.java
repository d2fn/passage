package io.measures.passage.dendrite;

import io.measures.passage.Integrator;
import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.PolarPoint;
import io.measures.passage.geometry.Projectable2D;

import java.util.List;

import static io.measures.passage.Sketch.dist;
import static io.measures.passage.Sketch.PI;
import static io.measures.passage.Sketch.TWO_PI;
import static io.measures.passage.Sketch.round;

public class Cell2D {

    private Integrator x;
    private Integrator y;
    private float r, curvature;

    private Cell2D nearestNeighbor;
    private float connectionStrength = 1;

    public Cell2D(Projectable2D p, float r, float curvature) {
        x = new Integrator(p.x());
        y = new Integrator(p.y());
        this.r = r;
        this.curvature = curvature;
    }

    private void moveTo(Projectable2D p) {
        this.x.set(p.x());
        this.y.set(p.y());
    }

    private void target(Projectable2D p) {
        this.x.target(p.x());
        this.y.target(p.y());
    }

    public void placeNear(Cell2D c) {
        placeNear(c, null);
    }

    public void placeNear(Cell2D c, List<Cell2D> env) {
        // compute the angle a line makes between the two points
        float dx = this.point().x() - c.targetPoint().x();
        float dy = this.point().y() - c.targetPoint().y();
        float t = (float)Math.atan2(dy, dx);
        // add arbitrary curvature if necessary
        t += curvature;
        // move the cell in a straight line towards the target so that the edges just touch
        Projectable2D pf = c.targetPoint().add(new PolarPoint(this.r()+c.r(), t));
        float dt = PI/256f;
        int iterations = round(TWO_PI/dt);
        boolean add = false;
        if(env != null && !env.isEmpty()) {
            int i = 1;
            while(i < iterations) {
                boolean snag = false;
                for(Cell2D test : env) {
                    if(test != c && test.collidesAtTarget(c.r(), pf.x(), pf.y())) {
                        t += dt;
                        pf = c.targetPoint().add(new PolarPoint(this.r()+c.r(), t));
                        snag = true;
                        break;
                    }
                }
                // if we make it through a whole check without a snag then add
                if(!snag) {
                    add = true;
                    i = iterations;
                }
                i++;
            }
        }
        else {
            add = true;
        }

        if(add) {
            this.moveTo(c.targetPoint());
            this.target(pf);
            nearestNeighbor = c;
            nearestNeighbor.boostConnectionStrength();
        }
        else {
            System.out.println("skipping point");
        }
    }

    private void boostConnectionStrength() {
        connectionStrength *= 1.01f;
        if(nearestNeighbor != null) {
            nearestNeighbor.boostConnectionStrength();
        }
    }

    public float getConnectionStrength() {
        return connectionStrength;
    }

    public Projectable2D point() {
        return new Point2D(x.get(), y.get());
    }

    public Projectable2D targetPoint() {
        return new Point2D(x.target(), y.target());
    }

    public boolean collidesAtTarget(float r, float x, float y) {
        return dist(this.x.target(), this.y.target(), x, y) < (this.r() + r);
    }

    public float r() {
        return r;
    }

    public Cell2D getNearestNeighbor() {
        return nearestNeighbor;
    }

    /**
     * @param c - the referenced cell
     * @return the distance of the edges of the two cells once they arrive
     *         at their final locations
     */
    public float targetEdgeDistance(Cell2D c) {
        return dist(c.targetPoint(), this.targetPoint()) - c.r() - this.r();
    }

    public void update() {
        x.update();
        y.update();
    }
}