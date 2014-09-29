package com.d2fn.passage.control;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import java.util.List;
import java.util.Map;

/**
 * MidiControl
 * @author Dietrich Featherston
 */
public class MidiControl {

    private final MidiDevice device;
    private final Choreographer c;
    private final Map<Integer, ControlGroup> idTable;

    public MidiControl(MidiDevice device, List<ControlGroup> controlGroups, Choreographer c) {

        this.device = device;
        this.c = c;

        try {
            device.open();
            Transmitter t = device.getTransmitter();
            t.setReceiver(new ValueReceiver());
        } catch (MidiUnavailableException e) {
            throw new RuntimeException("cannot open transmitter on MIDI device", e);
        }

        idTable = Maps.newConcurrentMap();
        for(ControlGroup cg : controlGroups) {
            route(cg);
        }

        for(int i = 0; i < c.getPortChoreographers().size() && i < controlGroups.size(); i++) {
            PortChoreographer pc = c.getPortChoreographers().get(i);
            ControlGroup cg = controlGroups.get(i);
            cg.attach(pc);
        }
    }

    private void route(ControlGroup cg) {
        for(int id : cg.ids()) {
            idTable.put(id, cg);
        }
    }

    public static Builder begin(String deviceName) {
        return new Builder(deviceName);
    }

    private void controlValueChanged(int controlId, float v) {
        if(idTable.containsKey(controlId)) {
            ControlGroup cg = idTable.get(controlId);
            cg.controlValueChanged(controlId, v);
        }
    }

    private static class ControlGroup {
        private final int knobId;
        private final int sliderId;
        private final int sId;
        private final int mId;
        private final int rId;

        private PortChoreographer pc;

        private ControlGroup(int knobId, int sliderId, int sId, int mId, int rId) {
            this.knobId = knobId;
            this.sliderId = sliderId;
            this.sId = sId;
            this.mId = mId;
            this.rId = rId;
        }

        private void attach(PortChoreographer pc) {
            this.pc = pc;
            System.out.println("slider " + sliderId + " -> " + pc.getTargetPortName());
        }

        private void controlValueChanged(int controlId, float v) {
            if(controlId == knobId) {
                pc.setGain(v);
                pc.setFrequency(1-v);
            }
            else if(controlId == sliderId) {
                pc.setDatum(v);
            }
            else if(controlId == mId && v < 1) {
                pc.cycleMode();
            }
        }

        private final int[] ids() {
            return new int[] { knobId, sliderId, sId, mId, rId };
        }
    }

    private class ValueReceiver implements Receiver {

        @Override
        public void send(MidiMessage m, long timeStamp) {
            if(m instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage)m;
                int controlId = sm.getData1();
                int ival = sm.getData2();
//                System.out.println("controlId -> " + controlId + " ival -> " + ival + " channel -> " + sm.getChannel() + " command -> " + sm.getCommand());
                float fval = (float)ival/127f;
//                if(controlId == 7) {
//                    controlValueChanged(sm.getChannel(), fval);
//                }
//                else {
                    controlValueChanged(controlId, fval);
//                }
            }
        }

        @Override
        public void close() {}
    }

    public static class Builder {

        private final String deviceName;
        private final List<ControlGroup> controlGroupList;

        private Choreographer c;

        public Builder(String deviceName) {
            this.deviceName = deviceName;
            this.controlGroupList = Lists.newArrayList();
        }

        public Builder attach(Choreographer c) {
            this.c = c;
            return this;
        }

        public MidiControl end() {

            controlGroupList.addAll(Lists.newArrayList(
                new ControlGroup(16, 0, 32, 48, 64),
                new ControlGroup(17, 1, 33, 49, 65),
                new ControlGroup(18, 2, 34, 50, 66),
                new ControlGroup(19, 3, 35, 51, 67),
                new ControlGroup(20, 4, 36, 52, 68),
                new ControlGroup(21, 5, 37, 53, 69),
                new ControlGroup(22, 6, 38, 54, 70),
                new ControlGroup(23, 7, 39, 55, 71)
            ));

            MidiDevice.Info info = getMidiDeviceInfo(deviceName);
            try {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                return new MidiControl(device, controlGroupList, c);
            } catch (MidiUnavailableException e) {
                throw new RuntimeException("Missing MIDI input device: " + deviceName, e);
            }
        }

        private MidiDevice.Info getMidiDeviceInfo(String deviceName) {
            MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
            for (MidiDevice.Info dinfo : info) {
                if (dinfo.getName().equals(deviceName)) {
                    try {
                        MidiDevice device = MidiSystem.getMidiDevice(dinfo);
                        if(device.getMaxTransmitters() != 0) {
                            return dinfo;
                        }
                    }
                    catch (MidiUnavailableException e) {
                        throw new RuntimeException("MIDI device unavailable: " + deviceName, e);
                    }
                }
            }
            return null;
        }
    }
}
