package com.d2fn.passage.dendrite;

import com.d2fn.passage.Integrator;
import com.d2fn.passage.Sketch;
import com.d2fn.passage.geometry.Point3D;
import com.d2fn.passage.geometry.Projectable3D;
import com.d2fn.passage.geometry.SphericalPoint;

public class Cell3D {

    private Integrator x;
    private Integrator y;
    private Integrator z;
    private final float r;

    private Cell3D nearestNeighbor;
    private float connectionStrength = 1;

    public Cell3D(Projectable3D p, float r) {
        x = new Integrator(p.x());
        y = new Integrator(p.y());
        z = new Integrator(p.z());
        this.r = r;
    }

    private void moveTo(Projectable3D p) {
        this.x.target(p.x());
        this.y.target(p.y());
        this.z.target(p.z());
    }

    public void placeNear(Cell3D c) {
        float dx = this.point().x() - c.targetPoint().x();
        float dy = this.point().y() - c.targetPoint().y();
        float dz = this.point().z() - c.targetPoint().z();
        float t = (float)Math.atan2(dy, dx);
        float p = (float)Math.atan2(dz, dx);
        Projectable3D pf = c.targetPoint().add(new SphericalPoint(this.r()+c.r(), p, t));
        this.moveTo(pf);
        nearestNeighbor = c;
        nearestNeighbor.boostConnectionStrength();
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

    public Projectable3D point() {
        return new Point3D(x.get(), y.get(), z.get());
    }

    public Projectable3D targetPoint() {
        return new Point3D(x.target(), y.target(), z.target());
    }

    public float r() {
        return r;
    }

    public Cell3D getNearestNeighbor() {
        return nearestNeighbor;
    }

    /**
     * @param c - the referenced cell
     * @return the distance of the edges of the two cells once they arrive
     *         at their final locations
     */
    public float targetEdgeDistance(Cell3D c) {
        return Sketch.dist(c.targetPoint(), this.targetPoint()) - c.r() - this.r();
    }

    public void update() {
        x.update();
        y.update();
        z.update();
    }
}
