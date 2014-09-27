package com.d2fn.passage.control.metadata;

/**
 * AbstractControlPort
 * @author Dietrich Featherston
 */
public abstract class AbstractControlPort<T> implements ControlPort<T> {

    private final String name;
    private final T initialValue;

    public AbstractControlPort(String name, T initialValue) {
        this.name = name;
        this.initialValue = initialValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        // todo - allow label to be set independently
        return name;
    }

    @Override
    public T getInitialValue() {
        return initialValue;
    }
}
