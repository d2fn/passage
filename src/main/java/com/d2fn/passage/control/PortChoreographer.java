package com.d2fn.passage.control;

import com.d2fn.passage.Sketch;
import com.d2fn.passage.control.data.ContinuousControlPortState;
import com.d2fn.passage.control.data.ControlPortState;
import com.d2fn.passage.control.data.ControlState;
import com.d2fn.passage.control.metadata.ContinuousControlPort;
import com.d2fn.passage.control.metadata.ControlMetadata;

import static com.d2fn.passage.Sketch.*;

/**
 * PortChoreographer
 * @author Dietrich Feahterston
 */
public class PortChoreographer {

    private final ContinuousControlPortState targetPort;

    private final ContinuousControlPort gainPort;
    private final ContinuousControlPort frequencyPort;
    private final ContinuousControlPort datumPort;

    private final ControlPortState<Float> gain;
    private final ControlPortState<Float> frequency;
    private final ControlPortState<Float> datum;

    private final ControlMetadata md;
    private final ControlState state;

    private GeneratorFunction f;

    public PortChoreographer(ContinuousControlPortState targetPort, float hz) {
        this.targetPort = targetPort;

        gainPort =
            ControlMetadata.continuousPort("gain")
                .range(0, 1)
                .build();

        frequencyPort =
            ControlMetadata.continuousPort("frequency")
                .range(0, hz)
                .build();

        datumPort =
            ControlMetadata.continuousPort("datum")
                .range(targetPort.lo(), targetPort.hi())
                .build();

        md = ControlMetadata
            .begin()
                .add(gainPort)
                .add(frequencyPort)
                .add(datumPort)
            .end();

        state = md.instantiate();

        gain = state.getPort(gainPort);
        frequency = state.getPort(frequencyPort);
        datum = state.getPort(datumPort);

        f = new Oscillator();
    }

    public String getTargetPortName() {
        return targetPort.getPort().getName();
    }

    public ControlMetadata getMetadata() {
        return md;
    }

    public void setGain(float normalizedValue) {
        gain.setValue(normalizedValue);
    }

    public void setFrequency(float normalizedValue) {
        frequency.setValue(normalizedValue * frequencyPort.hi());
    }

    public void setDatum(float normalizedValue) {
        datum.setValue(datumPort.lo() + (datumPort.hi()-datumPort.lo())*normalizedValue);
    }

    public void sendState(long timestampMillis) {

        ContinuousControlPort port = (ContinuousControlPort)targetPort.getPort();

        float gain = this.gain.floatValue();
        float frequency = this.frequency.floatValue();
        float datum = this.datum.floatValue();

        float value = f.call(timestampMillis, gain, frequency, port.lo(), datum, port.hi());
        targetPort.setValue(value);
    }

    public void cycleMode() {
        f = f.next();
    }

    private static float epsilon = 0.0001f;

    private static interface GeneratorFunction {
        public float call(long timestampMillis, float gain, float frequency, float lo, float datum, float hi);
        public GeneratorFunction next();
    }

    private static class Oscillator implements GeneratorFunction {

        // last timestamp seen
        private long lastTimestampMillis = Long.MIN_VALUE;
        // last theta
        private float lastP = 0;

        public Oscillator() {}

        public Oscillator(long startTimestampMillis, float startP) {
            this.lastTimestampMillis = startTimestampMillis;
            this.lastP = startP;
        }

        @Override
        public float call(long timestampMillis, float gain, float frequency, float lo, float datum, float hi) {

            long deltaT = lastTimestampMillis == Long.MIN_VALUE ? 0 : timestampMillis - lastTimestampMillis;
            long waveLengthMillis = Math.round(1000f/frequency);
            float p = (float)deltaT / (float)waveLengthMillis + lastP;
            float theta = 2 * (float)Math.PI * p;
            float amplitude = hi - datum;
            if(p >= 1) p -= 1;

            lastTimestampMillis = timestampMillis;
            lastP = p;

            if(gain < epsilon || frequency < epsilon) {
                return datum;
            }

            return gain * amplitude * (float)Math.sin(theta) + datum;
        }

        @Override
        public GeneratorFunction next() {
            return new Sawtooth(lastTimestampMillis, lastP);
        }
    }

    private static class Sawtooth implements GeneratorFunction {

        // last timestamp seen
        private long lastTimestampMillis = Long.MIN_VALUE;
        // last theta
        private float lastP = 0;

        public Sawtooth() {}

        public Sawtooth(long startTimestampMillis, float startP) {
            this.lastTimestampMillis = startTimestampMillis;
            this.lastP = startP;
        }

        @Override
        public float call(long timestampMillis, float gain, float frequency, float lo, float datum, float hi) {

            long deltaT = lastTimestampMillis == Long.MIN_VALUE ? 0 : timestampMillis - lastTimestampMillis;
            long periodMillis = Math.round(1000f/frequency);
            float p = (float)deltaT / (float)periodMillis + lastP;
            if(p >= 1) p -= 1;

            lastTimestampMillis = timestampMillis;
            lastP = p;

            if(gain < epsilon || frequency < epsilon) {
                return datum;
            }

            float range = gain * (hi - lo);
            float min = datum - range/2f;

            return min + p*range;
        }

        @Override
        public GeneratorFunction next() {
            return new Perlin();
        }
    }

    private static class Perlin implements GeneratorFunction {

        Sketch s = new Sketch();

        public Perlin() {
            s.randomSeed(0);
            s.noiseSeed(0);
        }

        @Override
        public float call(long timestampMillis, float gain, float frequency, float lo, float datum, float hi) {
            // inscribe a circle in perlin space
            long periodMillis = Math.round(1000f/frequency);
            long millisIntoCycle = timestampMillis % periodMillis;
            float theta = map(millisIntoCycle, 0, periodMillis, 0, TWO_PI);
            float r = 1;
            float x = r * cos(theta);
            float y = r * sin(theta);
            float v = s.noise(x, y);
            float range = gain * (hi - lo);
            float from = datum - range/2f;
            float to = datum + range/2f;
            return map(v, 0, 1, from, to);
        }

        @Override
        public GeneratorFunction next() {
            return new Oscillator();
        }
    }
}


