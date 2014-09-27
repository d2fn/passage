package com.d2fn.passage.control.metadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.d2fn.passage.control.Seq;
import com.d2fn.passage.control.data.AtomicControlState;
import com.d2fn.passage.control.data.ControlState;

import java.util.List;
import java.util.Set;

/**
 * ControlMetadata
 * Encapsulates metadata for the configurable, externally controllable state for a Sketch
 * @author Dietrich Featherston
 */
public class ControlMetadata {

    private final List<ControlPort> ports;

    private ControlMetadata(List<ControlPort> ports) {
        this.ports = ImmutableList.copyOf(ports);
    }

    public List<ControlPort> getPorts() {
        return ports;
    }

    public ControlState instantiate() {
        return new AtomicControlState(this);
    }

    public static IncBuilder begin() {
        return new IncBuilder();
    }

    public static BooleanControlPortBuilder booleanPort(String name) {
        return new BooleanControlPortBuilder(name);
    }

    public static DiscreteControlPortBuilder discretePort(String name) {
        return new DiscreteControlPortBuilder(name);
    }

    public static ContinuousControlPortBuilder continuousPort(String name) {
        return new ContinuousControlPortBuilder(name);
    }

    public static class IncBuilder {

        private final Set<String> portNames = Sets.newHashSet();
        private final List<ControlPort> ports = Lists.newArrayList();

        private IncBuilder() {}

        public IncBuilder add(ControlPort ... ports) {
            for(ControlPort p : ports) {
                add(p);
            }
            return this;
        }

        public IncBuilder add(ControlPort port) {
            if(portNames.contains(port.getName())) {
                throw new IllegalArgumentException(String.format("port name '%s' already taken", port.getName()));
            }
            portNames.add(port.getName());
            ports.add(port);
            return this;
        }

        public ControlMetadata end() {
            return new ControlMetadata(ports);
        }
    }

    public static abstract class ControlPortBuilder<T> {

        protected final String name;
        protected T initialValue;

        protected ControlPortBuilder(String name, T defaultValue) {
            this.name = name;
            this.initialValue = defaultValue;
        }

        public ControlPortBuilder<T> initialValue(T t) {
            this.initialValue = t;
            return this;
        }

        public abstract ControlPort<T> build();
    }

    public static class BooleanControlPortBuilder extends ControlPortBuilder<Boolean> {

        public BooleanControlPortBuilder(String name) {
            super(name, false);
        }

        @Override
        public BooleanControlPort build() {
            return new BooleanControlPort(name, initialValue);
        }
    }

    public static class DiscreteControlPortBuilder extends ControlPortBuilder<Integer> {

        private int lo, hi;
        private int inc = 1;

        public DiscreteControlPortBuilder(String name) {
            super(name, 0);
        }

        public DiscreteControlPortBuilder range(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
            return this;
        }

        public DiscreteControlPortBuilder inc(int inc) {
            this.inc = inc;
            return this;
        }

        @Override
        public DiscreteControlPort build() {
            Seq seq = new Seq(lo, hi, inc);
            if(!seq.contains(initialValue)) {
                throw new IllegalArgumentException(initialValue + " is out of range for sequence: ");
            }
            return new DiscreteControlPort(name, initialValue, seq);
        }
    }

    public static class ContinuousControlPortBuilder extends ControlPortBuilder<Float> {

        private float lo = 0, hi = 1;

        protected ContinuousControlPortBuilder(String name) {
            super(name, 0f);
        }

        public ContinuousControlPortBuilder range(float lo, float hi) {
            this.lo = lo;
            this.hi = hi;
            return this;
        }

        @Override
        public ContinuousControlPort build() {
            return new ContinuousControlPort(name, initialValue, lo, hi);
        }
    }
}
