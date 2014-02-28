package io.measures.passage.geometry;

/**
 * Projectable3D
 * represents a thing that can be projected to a single x, y, z point
 * @author Dietrich Featherston
 */
public interface Projectable3D extends Projectable2D {
    public float z();
}
