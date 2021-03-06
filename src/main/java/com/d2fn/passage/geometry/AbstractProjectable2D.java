package com.d2fn.passage.geometry;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

/**
 * AbstractProjectable2D
 * @author Dietrich Featherston
 */
public abstract class AbstractProjectable2D implements Projectable2D {

    @Override
    public Projectable2D add(Projectable2D b) {
        return new Point2D(this.x() + b.x(), this.y() + b.y());
    }

    @Override
    public Projectable2D sub(Projectable2D b) {
        return new Point2D(this.x() - b.x(), this.y() - b.y());
    }

    @Override
    public Projectable2D mid(Projectable2D b) {
        float x = (this.x() + b.x())/2f;
        float y = (this.y() + b.y())/2f;
        return new Point2D(x, y);
    }

    @Override
    public Projectable2D scale(float amt) {
        return new Point2D(amt * x(), amt * y());
    }

    @Override
    public Projectable2D rotate(float theta) {
        final float co = cos(theta);
        final float si = sin(theta);
        final float x = x();
        final float y = y();
        return
            new Point2D(
                co*x - si*y,
                si*x + co*y
            );
    }


    @Override
    public boolean within(Rect2D bounds) {
        return this.x() >= bounds.getUpperLeft().x() && this.x() < bounds.getLowerRight().x() &&
               this.y() >= bounds.getUpperLeft().y() && this.y() < bounds.getLowerRight().y();
    }
}
