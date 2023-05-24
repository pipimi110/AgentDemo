package top.popko.agentdemo.handler.hookpoint.controller.impl;


import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.SinkNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.TaintPosition;
import top.popko.agentdemo.util.StackUtils;
import top.popko.agentdemo.util.TaintPoolUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SinkImpl extends AbstractSolve {
    public SinkImpl() {
    }

    public  void solveSink(MethodEvent event, SinkNode sinkNode) {
        System.out.println("[+]solveSink");
        if (null != event) {
            String vulType = sinkNode.getVulType();
            if (setIfSourcesInTaintPool(event, sinkNode)) {//sink需要sink.source在hash池里
                event.setCallStacks(StackUtils.createCallStack(4));//collectMethod->solveSink->createCallStack->getStackTrace
                event.setTaintPositions(sinkNode.getSources(), null);
                addTrackEvent(event);
            }
        }
    }


    public boolean isInTaintPool(Object obj, MethodEvent event) {
        //比SourceImpl多一个TaintPoolUtils.poolContains检查hash是否存在
        if(TaintPoolUtils.isNotEmpty(obj) && TaintPoolUtils.isAllowTaintType(obj) && TaintPoolUtils.poolContains(obj, event)){
            return true;
        }
        return false;
    }



}
