package com.d2fn.passage.control.data;

import com.d2fn.passage.control.metadata.ControlPort;

/**
 * ControlState
 * The state of a controllable object a la ControlMetadata
 * @author Dietrich Featherston
 */
public interface ControlState {

    public Iterable<ControlPortState> ports();
    public ControlPortState getPort(ControlPort port);

    // base getter/setter
    public <T> T getValue(ControlPort<T> port);
    public <T> void setValue(ControlPort<T> port, T value);

    // convenience for coercing values into known types
    public boolean booleanValue(ControlPort port);
    public int intValue(ControlPort port);
    public float floatValue(ControlPort port);

}
