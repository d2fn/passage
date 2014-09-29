package com.d2fn.passage.control.data;

import com.d2fn.passage.control.metadata.ControlPort;

/**
 * BooleanControlPortState
 * @author Dietrich Featherston
 */
public class BooleanControlPortState extends AtomicControlPortState<Boolean> {

    public BooleanControlPortState(ControlPort port, Boolean initialValue) {
        super(port, initialValue);
    }

    @Override
    public boolean booleanValue() {
        return getValue();
    }

    @Override
    public int intValue() {
        boolean b = booleanValue();
        return b ? 1 : 0;
    }

    @Override
    public float floatValue() {
        return intValue();
    }
}
