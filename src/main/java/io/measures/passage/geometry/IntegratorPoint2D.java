package io.measures.passage.geometry;

import io.measures.passage.Integrator;

/**
 * IntegratorPoint2D
 * @author Dietrich Featherston
 */
public class IntegratorPoint2D extends AbstractProjectable2D {

    private final Integrator x;
    private final Integrator y;

    public IntegratorPoint2D(float x, float y) {
        this.x = new Integrator(x);
        this.y = new Integrator(y);
    }

    public void update() {
        x.update();
        y.update();
    }

    @Override
    public float x() {
        return x.get();
    }

    @Override
    public float y() {
        return y.get();
    }

    public Projectable2D target() {
        return new Point2D(x.target(), y.target());
    }

    public void tweenTo(Projectable2D p) {
        this.x.target(p.x());
        this.y.target(p.y());
    }
}
