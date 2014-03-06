package io.measures.passage.geometry;

import com.google.common.collect.Lists;

import java.util.List;

import static io.measures.passage.Sketch.*;

/**
 * Tendril
 * @author Dietrich Featherston
 */
public class Tendril implements Model3D {

    private final float r;
    private final float h;
    private final float verticalDetail;
    private final float radialDetail;
    private final float curve;

    public Tendril(float r, float h, float verticalDetail, float radialDetail, float curve) {
        this.r = r;
        this.h = h;
        this.verticalDetail = verticalDetail;
        this.radialDetail = radialDetail;
        this.curve = curve;
    }


    @Override
    public Iterable<Triangle3D> getTriangles() {
        List<Triangle3D> out = Lists.newArrayList();
        float steps = h*verticalDetail;
        float inc = h/steps;
        float tinc = TWO_PI/radialDetail;
        for(int i = 1; i < steps; i++) {
            float z = i * inc;
            for(float t = 0; t < TWO_PI; t += tinc) {
                float r1 = map(i-1, 0, steps, r, 0);
                float r2 = map(  i, 0, steps, r, 0);
                Point3D a = new Point3D(r1*cos(t), r1*sin(t), z);
                Point3D b = new Point3D(r2*cos(t), r2*sin(t), z+inc);
                Point3D c = new Point3D(r2*cos(t+tinc), r2*sin(t+tinc), z+inc);
                Point3D d = new Point3D(r1*cos(t+tinc), r1*sin(t+tinc), z);
                Triangle3D t1 = new Triangle3D(a, b, c);
                Triangle3D t2 = new Triangle3D(c, a, d);
                out.add(t1);
                out.add(t2);
            }

        }
        return out;
    }

    public int size() {
        return round(h * verticalDetail * radialDetail);
    }
}
