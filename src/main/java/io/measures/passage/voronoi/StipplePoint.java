package io.measures.passage.voronoi;

import io.measures.passage.geometry.Point2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * StipplePoint
 * @author Dietrich Featherston
 */
public class StipplePoint {

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
}
