package com.d2fn.passage.control.metadata;

import com.d2fn.passage.control.data.BooleanControlPortState;
import com.d2fn.passage.control.data.ControlPortState;

/**
 * BooleanControlPort
 * @author Dietrich Featherston
 */
public class BooleanControlPort extends AbstractControlPort<Boolean> {
    public BooleanControlPort(String name, Boolean initialValue) {
        super(name, initialValue);
    }

    @Override
    public ControlPortState<Boolean> instantiate() {
        return new BooleanControlPortState(this, getInitialValue());
    }
}
