package com.d2fn.passage.control;

import com.d2fn.passage.Sketch;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.d2fn.passage.control.data.ControlState;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import java.awt.Frame;
import java.util.Map;

/**
 * ControllerFrame
 * @author Dietrich Featherston
 */
public class ControllerFrame extends Sketch {

    private final Sketch parent;
    private final ControlState state;

    private ControlP5 cp5;
    private Layout layout;
    private Button playBtn;

    public ControllerFrame(Sketch parent, ControlState state) {
        this.parent = parent;
        this.state = state;
        layout = new LinearLayout();
    }

    public void setup() {
        size(1000, 400);
        frameRate(25);
        ControlP5 cp5 = new ControlP5(this);
        layout = new LinearLayout();
        playBtn =
            cp5.addButton("Play")
                .plugTo(this, "controlEvent")
                .setOff();
        layout.place(playBtn);
        // make components for ControlState
        cp5.printControllerMap();
    }

    public void renderFrame() {
        background(0);
    }

    public void toggleRunning(float v) {
        if(playBtn.isOn()) {
            playBtn.setCaptionLabel("Pause");
        }
        else {
            playBtn.setCaptionLabel("Play");
        }
    }

    public void controlEvent(ControlEvent event) {
        System.out.println("controlEvent -> " + event);
    }

    public static ControllerFrame show(Sketch parent, ControlState state) {
        String name = parent.getClass().getSimpleName();
        Frame f = new Frame(name);
        ControllerFrame p = new ControllerFrame(parent, state);
        f.add(p);
        p.init();
        f.setTitle(name);
        f.setSize(1000, 400);
        f.setLocation(20, 20);
        f.setResizable(true);
        f.setVisible(true);
        return p;
    }

    public void connectMidiController() {
        MidiDevice.Info[] all = MidiSystem.getMidiDeviceInfo();
        for(MidiDevice.Info info : all) {
            try(MidiDevice device = MidiSystem.getMidiDevice(info)) {
                device.open();
                if(device.getMaxTransmitters() != 0 && info.getVendor().toLowerCase().startsWith("korg")) {
                    System.out.println("[midi] opening device " + deviceInfo(info));
                    Transmitter t = device.getTransmitter();
                    t.setReceiver(new MReceiver());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            catch(MidiUnavailableException e) {
                System.err.println("midi device unavailable");
                e.printStackTrace(System.err);
            }
        }
    }

    private static String deviceInfo(MidiDevice.Info info) {
        Map<String, String> m = Maps.newHashMap();
        m.put("name", info.getName());
        m.put("version", info.getVersion());
        m.put("vendor", info.getVendor());
        m.put("description", info.getDescription());
        return "\n\t" + Joiner.on(", \n\t").withKeyValueSeparator(" = ").join(m);
    }

    public class MReceiver implements Receiver {
        public void send(MidiMessage msg, long timeStamp) {
            System.out.println("midi received");
        }
        public void close() {}
    }
}
