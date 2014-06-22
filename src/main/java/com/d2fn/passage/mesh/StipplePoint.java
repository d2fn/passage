package com.d2fn.passage.mesh;

import com.d2fn.passage.geometry.Point2D;
import com.d2fn.passage.geometry.Projectable2D;
import com.d2fn.passage.geometry.Rect2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * StipplePoint
 * @author Dietrich Featherston
 */
public class StipplePoint implements Projectable2D {

    private final Point2D p;
    private final float density;

    public StipplePoint(Point2D p, float density) {
        this.p = p;
        this.density = density;
    }

    public Point2D getPoint() {
        return p;
    }

    public float getDensity() {
        return density;
    }

    public void writeTo(DataOutputStream dos) throws IOException {
        dos.writeFloat(p.x());
        dos.writeFloat(p.y());
        dos.writeFloat(density);
    }

    public static StipplePoint readFrom(DataInputStream dis) throws IOException {
        float x = dis.readFloat();
        float y = dis.readFloat();
        float density = dis.readFloat();
        return new StipplePoint(new Point2D(x, y), density);
    }

    @Override
    public float x() {
        return p.x();
    }

    @Override
    public float y() {
        return p.y();
    }

    @Override
    public Projectable2D add(Projectable2D b) {
        return p.add(b);
    }

    @Override
    public Projectable2D sub(Projectable2D b) {
        return p.sub(b);
    }

    @Override
    public Projectable2D mid(Projectable2D b) {
        return p.mid(b);
    }

    @Override
    public Projectable2D scale(float amt) {
        return p.scale(amt);
    }

    @Override
    public boolean within(Rect2D bounds) {
        return p.within(bounds);
    }
}
