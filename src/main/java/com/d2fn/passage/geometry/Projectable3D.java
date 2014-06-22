package com.d2fn.passage.geometry;

/**
 * Projectable3D
 * represents a thing that can be projected to a single x, y, z point
 * @author Dietrich Featherston
 */
public interface Projectable3D {
    public float x();
    public float y();
    public float z();
    public Projectable3D add(Projectable3D p);
}
