package com.d2fn.passage.control;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.d2fn.passage.control.data.ContinuousControlPortState;
import com.d2fn.passage.control.data.ControlPortState;
import com.d2fn.passage.control.data.ControlState;
import com.d2fn.passage.control.metadata.ControlPort;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Choreographer
 * @author Dietrich Featherston
 */
public class Choreographer {

    private final ControlState sketchState;
    private final float readerHz;
    private final Map<ControlPort, PortChoreographer> portChoreographers;
    private final List<PortChoreographer> pclist;

    private final ScheduledThreadPoolExecutor scheduler;
    private ScheduledFuture futureTasks;

    public Choreographer(ControlState sketchState, float readerHz) {
        this.sketchState = sketchState;
        this.readerHz = readerHz;
        this.pclist = Lists.newArrayList();
        this.portChoreographers = Maps.newConcurrentMap();
        for(ControlPortState portState : sketchState.ports()) {
            if(portState instanceof ContinuousControlPortState) {
                PortChoreographer pc = new PortChoreographer((ContinuousControlPortState)portState, 1f /*Hz limit*/);
                portChoreographers.put(portState.getPort(), pc);
                pclist.add(pc);
            }
        }
        this.scheduler = new ScheduledThreadPoolExecutor(1);
    }

    public List<PortChoreographer> getPortChoreographers() {
        return ImmutableList.copyOf(pclist);
    }

    public void start() {
        long intervalMillis = 5;
        futureTasks = scheduler.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        long ts = System.currentTimeMillis();
                        // todo - use relative time that stops when the scheduled task is cancelled
                        for(PortChoreographer pc : portChoreographers.values()) {
                            pc.sendState(ts);
                        }
                    }
                },
                0L, intervalMillis, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        futureTasks.cancel(false);
        futureTasks = null;
    }
}
