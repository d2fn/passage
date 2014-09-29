package com.d2fn.passage.control.data;

import com.d2fn.passage.Integrator;
import com.d2fn.passage.control.metadata.ContinuousControlPort;
import com.d2fn.passage.control.metadata.ControlPort;

/**
 * ContinuousControlPortState
 * @author Dietrich Featherston
 */
public class ContinuousControlPortState implements ControlPortState<Float> {

    private final ContinuousControlPort port;
    private final Integrator value;

    public ContinuousControlPortState(ContinuousControlPort port, Float initialValue) {
        this.port = port;
        this.value = new Integrator(initialValue);
    }

    public float lo() {
        return ((ContinuousControlPort)getPort()).lo();
    }

    public float hi() {
        return ((ContinuousControlPort)getPort()).hi();
    }

    @Override
    public ControlPort<Float> getPort() {
        return port;
    }

    @Override
    public Float getValue() {
        return value.update().get();
    }

    @Override
    public void setValue(Float newVal) {
        value.target(newVal);
    }

    @Override
    public boolean booleanValue() {
        return intValue() > 0;
    }

    @Override
    public int intValue() {
        return Math.round(floatValue());
    }

    @Override
    public float floatValue() {
        return getValue();
    }
}
