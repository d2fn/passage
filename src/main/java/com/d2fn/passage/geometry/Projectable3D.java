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
    public Projectable3D sub(Projectable3D p);
    public Projectable3D scale(float amt);
    public Projectable3D rotateX(float theta);
    public Projectable3D rotateY(float theta);
    public Projectable3D rotateZ(float theta);
    public Projectable3D rotate(float xradians, float yradians, float zradians);
    public Projectable3D rotate(Projectable3D angleVec);
}
