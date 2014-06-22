package com.d2fn.passage.function;

import com.d2fn.passage.geometry.Point2D;
import com.d2fn.passage.geometry.Projectable2D;

/**
 * Function2D
 * @author Dietrich Featherston
 */
public class Function2D {

    private final LFunction fun;

    public Function2D(LFunction fun) {
        this.fun = fun;
    }

    public Projectable2D call(float x, float theta) {
        return new Point2D(x, fun.call(theta));
    }
}
