package com.d2fn.passage;

import com.d2fn.passage.geometry.Projectable3D;
import com.d2fn.passage.geometry.Triangle3D;

import java.awt.*;
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

    public static float log10(float n) {
        return (log(n) / log(10));
    }

    public static float sunAngle(long utc) {
        long hour = utc / 3600;
        long hourOfDay = hour % 24;
        return -hourOfDay * TWO_PI/24 - PI;
    }

    /**
     * @param rgb int
     * @return the gamma/brightness on the range [0, 1]
     */
    public static float gamma(int rgb) {
        float[] cacheHsbValue = new float[3];
        Color.RGBtoHSB((rgb >> 16) & 0xff, (rgb >> 8) & 0xff,
                rgb & 0xff, cacheHsbValue);
        return cacheHsbValue[2];
    }

    private Functions() {}
}
