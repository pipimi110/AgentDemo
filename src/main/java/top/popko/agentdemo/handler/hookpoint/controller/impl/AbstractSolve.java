package top.popko.agentdemo.handler.hookpoint.controller.impl;

import top.popko.agentdemo.handler.hookpoint.SpyDispatcherImpl;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.track.TaintTrack;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractSolve {
    public static void addTrackEvent(MethodEvent event) {
        int invokeId = SpyDispatcherImpl.INVOKE_ID_SEQUENCER.getAndIncrement();//线程安全
        event.setInvokeId(invokeId);
        TaintTrack.TRACK_MAP.addTrackMethod(invokeId, event);
    }
}
