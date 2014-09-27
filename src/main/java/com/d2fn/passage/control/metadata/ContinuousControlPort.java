package com.d2fn.passage.control.metadata;

import com.d2fn.passage.control.data.ContinuousControlPortState;
import com.d2fn.passage.control.data.ControlPortState;

/**
 * ContinuousControlPort
 * @author Dietrich Featherston
 */
public class ContinuousControlPort extends AbstractControlPort<Float> {

    private final float lo;
    private final float hi;

    public ContinuousControlPort(String name, Float initialValue, float lo, float hi) {
        super(name, initialValue);
        this.lo = lo;
        this.hi = hi;
    }

    public float lo() {
        return lo;
    }

    public float hi() {
        return hi;
    }

    public boolean checkBounds(float v) {
        return v >= lo && lo <= hi;
    }

    @Override
    public ControlPortState<Float> instantiate() {
        return new ContinuousControlPortState(this, getInitialValue());
    }
}
