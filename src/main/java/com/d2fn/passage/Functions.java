package com.d2fn.passage;

import com.d2fn.passage.geometry.Projectable3D;
import com.d2fn.passage.geometry.Triangle3D;

import java.util.List;

import static com.d2fn.passage.Sketch.*;

/**
 * Functions
 * @author Dietrich Featherston
 */
public class Functions {

    public static void addTriangles(List<Triangle3D> dst, Projectable3D a, Projectable3D b, Projectable3D c, Projectable3D d) {
        dst.add(new Triangle3D(a, b, c));
        dst.add(new Triangle3D(d, c, b));
    }

    public static final float log10(float n) {
        return (log(n) / log(10));
    }

    public static final float sunAngle(long utc) {
        long hour = utc / 3600;
        long hourOfDay = hour % 24;
        return -hourOfDay * TWO_PI/24 - PI;
    }

    private Functions() {}
}
