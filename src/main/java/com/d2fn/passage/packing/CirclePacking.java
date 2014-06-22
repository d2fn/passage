package com.d2fn.passage.packing;

import com.d2fn.passage.geometry.Circle;

import java.util.List;

/**
 * CirclePacking
 * @author Dietrich Featherston
 */
public interface CirclePacking {
    public List<Circle> get();
    public void step();
}
