package com.d2fn.passage.control.metadata;

import com.d2fn.passage.control.data.ControlPortState;

/**
 * ControlPort
 * Metadata for a single configurable property
 * @author Dietrich Featherston
 */
public interface ControlPort<T> {
    public String getName();
    public String getLabel();
    public T getInitialValue();
    public ControlPortState<T> instantiate();
}
