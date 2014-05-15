package io.measures.passage.mesh;

import io.measures.passage.geometry.Line2D;
import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.Projectable2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * StippleLine
 * @author Dietrich Featherston
 */
public class StippleLine extends Line2D {
    private final float density;

    public StippleLine(Point2D a, Point2D b, float density) {
        super(a.x(), a.y(), b.x(), b.y());
        this.density = density;
    }

    public float getDensity() {
        return density;
    }

    public Projectable2D getMidpoint() {
        return a().mid(b());
    }

    public void writeTo(DataOutputStream dos) throws IOException {
        dos.writeFloat(a().x());
        dos.writeFloat(a().y());
        dos.writeFloat(b().x());
        dos.writeFloat(b().y());
        dos.writeFloat(density);
    }

    public static StippleLine readFrom(DataInputStream dis) throws IOException {
        Point2D a = new Point2D(dis.readFloat(), dis.readFloat());
        Point2D b = new Point2D(dis.readFloat(), dis.readFloat());
        return new StippleLine(a, b, dis.readFloat());
    }
}
