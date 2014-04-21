package io.measures.passage.packing;

import io.measures.passage.geometry.Circle;

import java.util.List;

/**
 * CirclePacking
 * @author Dietrich Featherston
 */
public interface CirclePacking {
    public List<Circle> get();
    public void step();
}
