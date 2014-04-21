package io.measures.passage.voronoi;

import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.Projectable2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * StippleLine
 * @author Dietrich Featherston
 */
public class StippleLine {
    private final Point2D a;
    private final Point2D b;
    private final float density;

    public StippleLine(Point2D a, Point2D b, float density) {
        this.a = a;
        this.b = b;
        this.density = density;
    }

    public Point2D getA() {
        return a;
    }

    public Point2D getB() {
        return b;
    }

    public float getDensity() {
        return density;
    }

    public Projectable2D getMidpoint() {
        return a.mid(b);
    }

    public void writeTo(DataOutputStream dos) throws IOException {
        dos.writeFloat(a.x());
        dos.writeFloat(a.y());
        dos.writeFloat(b.x());
        dos.writeFloat(b.y());
        dos.writeFloat(density);
    }

    public static StippleLine readFrom(DataInputStream dis) throws IOException {
        Point2D a = new Point2D(dis.readFloat(), dis.readFloat());
        Point2D b = new Point2D(dis.readFloat(), dis.readFloat());
        return new StippleLine(a, b, dis.readFloat());
    }
}
