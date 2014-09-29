package com.d2fn.passage.control.data;

import com.d2fn.passage.control.metadata.ControlPort;

/**
 * ControlPortState
 * @author Dietrich Featherston
 */
public interface ControlPortState<T> {

    // base methods
    public ControlPort<T> getPort();
    public T getValue();
    public void setValue(T newVal);

    public boolean booleanValue();
    public int intValue();
    public float floatValue();
}
