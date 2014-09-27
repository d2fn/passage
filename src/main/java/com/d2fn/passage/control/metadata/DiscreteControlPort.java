package com.d2fn.passage.control.metadata;

import com.d2fn.passage.control.Seq;
import com.d2fn.passage.control.data.ControlPortState;
import com.d2fn.passage.control.data.DiscreteControlPortState;

import java.util.List;

/**
 * DiscreteControlPort
 * @author Dierich Featherston
 */
public class DiscreteControlPort extends AbstractControlPort<Integer> {

    private final Seq seq;

    protected DiscreteControlPort(String name, int initialValue, Seq seq) {
        super(name, initialValue);
        this.seq = seq;
    }

    public List<Integer> values() {
        return seq.values();
    }

    @Override
    public ControlPortState<Integer> instantiate() {
        return new DiscreteControlPortState(this, getInitialValue());
    }
}
