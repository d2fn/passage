package com.d2fn.passage.control.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.d2fn.passage.control.metadata.ControlMetadata;
import com.d2fn.passage.control.metadata.ControlPort;

import java.util.List;
import java.util.Map;

/**
 * AtomicControlState
 * @author Dietrich Featherston
 */
public class AtomicControlState implements ControlState {

    private final ControlMetadata md;
    private Map<ControlPort, ControlPortState> state;

    public AtomicControlState(ControlMetadata md) {
        this.md = md;
        this.state = Maps.newConcurrentMap();

        // fill initial state
        for(ControlPort port : this.md.getPorts()) {
            ControlPortState portState = port.instantiate();
            state.put(port, portState);
        }
    }


    @Override
    public Iterable<ControlPortState> ports() {
        List<ControlPort> ports = md.getPorts();
        List<ControlPortState> out = Lists.newArrayListWithCapacity(ports.size());
        for(ControlPort port : ports) {
            out.add(state.get(port));
        }
        return out;
    }

    @Override
    public ControlPortState getPort(ControlPort port) {
        return state.get(port);
    }

    @Override
    public <T> T getValue(ControlPort<T> port) {
        return (T)state.get(port).getValue();
    }

    @Override
    public <T> void setValue(ControlPort<T> port, T value) {
        state.get(port).setValue(value);
    }

    @Override
    public boolean booleanValue(ControlPort port) {
        return state.get(port).booleanValue();
    }

    @Override
    public int intValue(ControlPort port) {
        return state.get(port).intValue();
    }

    @Override
    public float floatValue(ControlPort port) {
        return state.get(port).floatValue();
    }
}
