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
                if (setIfSourcesInTaintPool(event, propagatorNode)) {//PropagatorImpl需要判断sources是否在污点池中
                    TaintPosition tp;
                    Iterator var5 = propagatorNode.getTargets().iterator();//targets遍历
                    while (var5.hasNext()) {
                        tp = (TaintPosition) var5.next();
                        setTargetValue(event, tp);//setTargetValue->addTaintHash添加hash入池
                    }
//                boolean valid = setTarget(propagatorNode, event);
//                boolean valid = setTargetValue(event, tp);
//                if (valid) {
//                    if (!TaintPosition.hasObject(sources) && !TaintPosition.hasObject(propagatorNode.getTargets())) {
//                        event.setObjectValue(false);
//                    }

//        if (!event.getSourceHashes().equals(event.getTargetHashes()) || sources.size() != 1 || targets.size() != 1 || !TaintPosition.hasObject(sources) || !TaintPosition.hasObject(targets) && !TaintPosition.hasReturn(targets)) {
                    event.setCallStacks(StackUtils.createCallStack(4));
                    event.setTaintPositions(propagatorNode.getSources(), propagatorNode.getTargets());
                    addTrackEvent(event);//addTrackEvent->TRACK_MAP.addTrackMethod保存事件到map
//        }
//                }
                }
            }
        }
    }

    private void addPropagator(PropagatorNode propagatorNode, MethodEvent event, AtomicInteger invokeIdSequencer) {

    }


//    private boolean setTarget(PropagatorNode propagatorNode, MethodEvent event) {
//        Set<TaintPosition> targets = propagatorNode.getTargets();
//        if (targets != null && !targets.isEmpty()) {
//            boolean hasTaint = false;
//            Iterator var4 = targets.iterator();
//
//            while (var4.hasNext()) {
//                TaintPosition position = (TaintPosition) var4.next();
//                boolean retHasTaint;
//                if (position.isObject()) {
//                    retHasTaint = false;
//                    if (TaintPoolUtils.isNotEmpty(event.objectInstance) && TaintPoolUtils.isAllowTaintType(event.objectInstance)) {
//                        EngineManager.TAINT_HASH_CODES.addObject(event.objectInstance, event);
//                        retHasTaint = true;
//                        hasTaint = true;
//                    }
//
//                    event.setObjectValue(retHasTaint);
//                } else if (position.isReturn()) {
//                    retHasTaint = false;
//                    if (TaintPoolUtils.isNotEmpty(event.returnInstance) && TaintPoolUtils.isAllowTaintType(event.returnInstance)) {
//                        EngineManager.TAINT_HASH_CODES.addObject(event.returnInstance, event);
//                        retHasTaint = true;
//                        hasTaint = true;
//                    }
//
//                    event.setReturnValue(retHasTaint);
//                } else if (position.isParameter()) {
//                    int parameterIndex = position.getParameterIndex();
//                    if (parameterIndex < event.parameterInstances.length) {
//                        Object parameter = event.parameterInstances[parameterIndex];
//                        if (TaintPoolUtils.isNotEmpty(parameter) && TaintPoolUtils.isAllowTaintType(parameter)) {
//                            EngineManager.TAINT_HASH_CODES.addObject(parameter, event);
//                            event.addParameterValue(parameterIndex, parameter, true);
//                            hasTaint = true;
//                        }
//                    }
//                }
//            }
//
//            if (hasTaint) {
//                trackTaintRange(propagatorNode, event);
//            }
//
//            return hasTaint;
//        } else {
//            return false;
//        }
//    }

//    private TaintRanges getTaintRanges(Object obj) {
//        int hash = System.identityHashCode(obj);
//        TaintRanges tr = EngineManager.TAINT_RANGES_POOL.get(hash);
//        if (tr == null) {
//            tr = new TaintRanges();
//        } else {
//            tr = tr.clone();
//        }
//
//        return tr;
//    }
//
//    private void trackTaintRange(PropagatorNode propagatorNode, MethodEvent event) {
//        TaintCommandRunner r = TaintCommandRunner.getCommandRunner(event.signature);
//        TaintRanges oldTaintRanges = new TaintRanges();
//        TaintRanges srcTaintRanges = new TaintRanges();
//        Object src = null;
//        Set<TaintPosition> sourceLocs = propagatorNode.getSources();
//        if (sourceLocs.size() == 1 && TaintPosition.hasObject(sourceLocs)) {
//            src = event.objectInstance;
//            srcTaintRanges = getTaintRanges(src);
//        } else {
//            Iterator var7;
//            TaintPosition sourceLoc;
//            int parameterIndex;
//            if (sourceLocs.size() == 2 && TaintPosition.hasObject(sourceLocs) && TaintPosition.hasParameter(sourceLocs)) {
//                oldTaintRanges = getTaintRanges(event.objectInstance);
//                var7 = sourceLocs.iterator();
//
//                while (var7.hasNext()) {
//                    sourceLoc = (TaintPosition) var7.next();
//                    if (sourceLoc.isParameter()) {
//                        parameterIndex = sourceLoc.getParameterIndex();
//                        if (event.parameterInstances.length > parameterIndex) {
//                            src = event.parameterInstances[parameterIndex];
//                            srcTaintRanges = getTaintRanges(src);
//                        }
//                        break;
//                    }
//                }
//            } else if (sourceLocs.size() == 1 && TaintPosition.hasParameter(sourceLocs)) {
//                var7 = sourceLocs.iterator();
//
//                while (var7.hasNext()) {
//                    sourceLoc = (TaintPosition) var7.next();
//                    parameterIndex = sourceLoc.getParameterIndex();
//                    if (event.parameterInstances.length > parameterIndex) {
//                        src = event.parameterInstances[parameterIndex];
//                        srcTaintRanges = getTaintRanges(src);
//                    }
//                }
//            }
//        }
//
//        int tgtHash = 0;
//        Object tgt = null;
//        Set<TaintPosition> targetLocs = propagatorNode.getTargets();
//        if (targetLocs.size() <= 1) {
//            if (TaintPosition.hasObject(targetLocs)) {
//                tgt = event.objectInstance;
//                tgtHash = System.identityHashCode(tgt);
//                oldTaintRanges = getTaintRanges(tgt);
//            } else if (TaintPosition.hasReturn(targetLocs)) {
//                tgt = event.returnInstance;
//                tgtHash = System.identityHashCode(tgt);
//            } else {
//                if (!TaintPosition.hasParameter(targetLocs)) {
//                    return;
//                }
//
//                Iterator var10 = targetLocs.iterator();
//
//                while (var10.hasNext()) {
//                    TaintPosition targetLoc = (TaintPosition) var10.next();
//                    int parameterIndex = targetLoc.getParameterIndex();
//                    if (event.parameterInstances.length > parameterIndex) {
//                        tgt = event.parameterInstances[parameterIndex];
//                        tgtHash = System.identityHashCode(tgt);
//                        oldTaintRanges = getTaintRanges(tgt);
//                    }
//                }
//            }
//
//            if (TaintPoolUtils.isNotEmpty(tgt) && TaintPoolUtils.isAllowTaintType(tgt) && tgtHash != 0) {
//                TaintRanges tr;
//                if (r != null && src != null) {
//                    tr = r.run(propagatorNode, src, tgt, event.parameterInstances, oldTaintRanges, srcTaintRanges);
//                } else {
//                    int len = TaintRangesBuilder.getLength(tgt);
//                    tr = new TaintRanges(new TaintRange[]{new TaintRange(0, len)});
//                    if (propagatorNode.hasTags()) {
//                        String[] tags = propagatorNode.getTags();
//                        String[] var13 = tags;
//                        int var14 = tags.length;
//
//                        for (int var15 = 0; var15 < var14; ++var15) {
//                            String tag = var13[var15];
//                            tr.add(new TaintRange(tag, 0, len));
//                        }
//                    }
//
//                    tr.addAll(srcTaintRanges.explode(len));
//                    tr.addAll(oldTaintRanges);
//                    tr.merge();
//                    tr.untag(propagatorNode.getUntags());
//                }
//
//                event.targetRanges.add(new MethodEvent.MethodEventTargetRange(tgtHash, tr));
//                EngineManager.TAINT_RANGES_POOL.add(tgtHash, tr);
//            }
//        }
//    }

    public boolean isSkipScope(String signature) {
        return SKIP_SCOPE_METHODS.contains(signature);
    }

    public boolean isInTaintPool(Object obj, MethodEvent event) {
        //比SourceImpl多一个TaintPoolUtils.poolContains检查hash是否存在
        if (TaintPoolUtils.isNotEmpty(obj) && TaintPoolUtils.isAllowTaintType(obj) && TaintPoolUtils.poolContains(obj, event)) {
            return true;
        }
        return false;
    }
}
