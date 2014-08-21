package com.d2fn.passage.geometry;

/**
 * Points
 * @author Dietrich Featherston
 */
public class Points {

    public static Projectable3D xyz(float x, float y, float z) {
        return new Point3D(x, y, z);
    }

    public static Projectable2D xy(float x, float y) {
        return new Point2D(x, y);
    }
}
