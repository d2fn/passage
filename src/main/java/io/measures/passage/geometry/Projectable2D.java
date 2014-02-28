package io.measures.passage.geometry;

/**
 * Projectable2D
 * @author Dietrich Featherston
 */
public interface Projectable2D {
    public float x();
    public float y();
    public Projectable2D add(Projectable2D b);
    public Projectable2D sub(Projectable2D b);
    public Projectable2D mid(Projectable2D b);
    public Projectable2D scale(float amt);
}
