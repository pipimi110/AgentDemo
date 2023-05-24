package top.popko.agentdemo.handler.hookpoint.controller.impl;

import top.popko.agentdemo.EngineManager;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.PropagatorNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.TaintFlowNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.TaintPosition;
import top.popko.agentdemo.util.StackUtils;
import top.popko.agentdemo.util.TaintPoolUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PropagatorImpl extends AbstractSolve {
    private final Set<String> SKIP_SCOPE_METHODS = new HashSet(Arrays.asList("java.net.URI.<init>(java.lang.String)", "java.net.URI.<init>(java.lang.String,java.lang.String,java.lang.String)", "java.net.URI.<init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)", "java.net.URI.<init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String)", "java.net.URI.<init>(java.lang.String,java.lang.String,java.lang.String,int,java.lang.String,java.lang.String,java.lang.String)", "java.net.URL.<init>(java.lang.String)", "java.net.URL.<init>(java.net.URL,java.lang.String)", "java.net.URL.<init>(java.net.URL,java.lang.String,java.net.URLStreamHandler)", "java.net.URL.<init>(java.lang.String,java.lang.String,java.lang.String)", "java.net.URL.<init>(java.lang.String,java.lang.String,int,java.lang.String)", "java.net.URL.<init>(java.lang.String,java.lang.String,int,java.lang.String,java.net.URLStreamHandler)"));

    public PropagatorImpl() {
    }

    public void solvePropagator(MethodEvent event, PropagatorNode propagatorNode, AtomicInteger invokeIdSequencer) {
        System.out.println("[+]solvePropagator");
        if (!EngineManager.TAINT_HASH_CODES.isEmpty()) {
            Set<TaintPosition> sources = propagatorNode.getSources();
            Set<TaintPosition> targets = propagatorNode.getTargets();
            if (!sources.isEmpty() && !targets.isEmpty()) {
                if (setIfSourcesInTaintPool(event, propagatorNode)) {
                    TaintPosition tp;//需要判断污点输入sources是否在污点池中
                    Iterator var5 = propagatorNode.getTargets().iterator();
                    while (var5.hasNext()) {//污点输出targets遍历
                        tp = (TaintPosition) var5.next();
                        setTargetValue(event, tp);//->addTaintHash添加hash入池
                    }

                    event.setCallStacks(StackUtils.createCallStack(4));
                    event.setTaintPositions(propagatorNode.getSources(), propagatorNode.getTargets());
                    addTrackEvent(event);//->TRACK_MAP.addTrackMethod保存事件到map
                }
            }
        }
    }

    private void addPropagator(PropagatorNode propagatorNode, MethodEvent event, AtomicInteger invokeIdSequencer) {

    }

    public boolean isInTaintPool(Object obj, MethodEvent event) {
        //比SourceImpl多一个TaintPoolUtils.poolContains检查hash是否存在
        if (TaintPoolUtils.isNotEmpty(obj) && TaintPoolUtils.isAllowTaintType(obj) && TaintPoolUtils.poolContains(obj, event)) {
            return true;
        }
        return false;
    }
}
