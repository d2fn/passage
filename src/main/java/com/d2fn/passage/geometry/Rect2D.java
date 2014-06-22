package com.d2fn.passage.geometry;

/**
 * Rect2D
 * @author Dietrich Featherston
 */
public class Rect2D {
    private final Projectable2D upperLeft;
    private final float width;
    private final float height;

    public Rect2D(float x, float y, float width, float height) {
        this.upperLeft = new Point2D(x, y);
        this.width = width;
        this.height = height;
    }

    public Rect2D(Projectable2D upperLeft, float width, float height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }

    public Projectable2D getUpperLeft() {
        return upperLeft;
    }

    public Projectable2D getLowerRight() {
        return upperLeft.add(new Point2D(width, height));
    }

    public float x() { return upperLeft.x(); }
    public float y() { return upperLeft.y(); }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean contains(Projectable2D p) {
        return getUpperLeft().x() <= p.x() && getUpperLeft().y() <= p.y() && getLowerRight().x() > p.x() && getLowerRight().y() > p.y();
    }
}
