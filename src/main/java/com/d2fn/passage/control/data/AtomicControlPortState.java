package com.d2fn.passage.control.data;

import com.d2fn.passage.control.metadata.ControlPort;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicControlPortState
 * @author Dietrich Featherston
 */
public abstract class AtomicControlPortState<T> implements ControlPortState<T> {

    protected final ControlPort port;
    protected final AtomicReference<T> ref;

    public AtomicControlPortState(ControlPort port, T initialValue) {
        this.port = port;
        this.ref = new AtomicReference<>(initialValue);
    }

    @Override
    public ControlPort<T> getPort() {
        return port;
    }

    @Override
    public T getValue() {
        return ref.get();
    }

    @Override
    public void setValue(T newVal) {
        ref.set(newVal);
    }
}
