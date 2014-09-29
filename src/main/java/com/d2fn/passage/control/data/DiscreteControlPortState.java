package com.d2fn.passage.control.data;

import com.d2fn.passage.control.metadata.ControlPort;

/**
 * DiscreteControlPortState
 * @author Dietrich Featherston
 */
public class DiscreteControlPortState extends AtomicControlPortState<Integer> {

    public DiscreteControlPortState(ControlPort port, Integer initialValue) {
        super(port, initialValue);
    }

    @Override
    public boolean booleanValue() {
        return getValue() != null;
    }

    @Override
    public int intValue() {
        return getValue();
    }

    @Override
    public float floatValue() {
        return getValue();
    }
}
