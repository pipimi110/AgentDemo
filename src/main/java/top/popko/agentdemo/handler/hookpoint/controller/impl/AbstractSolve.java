package top.popko.agentdemo.handler.hookpoint.controller.impl;

import top.popko.agentdemo.EngineManager;
import top.popko.agentdemo.handler.hookpoint.SpyDispatcherImpl;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.SourceNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.TaintFlowNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.TaintPosition;
import top.popko.agentdemo.util.TaintPoolUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractSolve {
    public void addTrackEvent(MethodEvent event) {
        int invokeId = SpyDispatcherImpl.INVOKE_ID_SEQUENCER.getAndIncrement();//线程安全
        event.setInvokeId(invokeId);
        EngineManager.TRACK_MAP.addTrackMethod(invokeId, event);
    }
    abstract boolean isInTaintPool(Object obj, MethodEvent event);

//    public static void setSourceValue(MethodEvent event, TaintPosition tp) {
//        if (tp.isObject()) {
////                        event.setObjectValue(event.returnInstance, true);
//            event.setObjectValue(true);
//        } else if (tp.isParameter() && event.parameterInstances.length > tp.getParameterIndex()) {
//            event.addParameterValue(tp.getParameterIndex(), event.parameterInstances[tp.getParameterIndex()], true);
//        }
//    }
    protected boolean setSourceValue(MethodEvent event, TaintPosition tp) {
        boolean hasTaint = false;
        if (tp.isObject() && isInTaintPool(event.objectInstance, event)) {
                hasTaint = true;
                event.setObjectValue(true);
//                this.addSourceInstance(sourceInstances, event.objectInstance);
        } else if (tp.isParameter() && event.parameterInstances.length > tp.getParameterIndex()) {
            Object parameter = event.parameterInstances[tp.getParameterIndex()];
            if (isInTaintPool(parameter, event)) {
//                this.addSourceInstance(sourceInstances, parameter);
                hasTaint = true;
                event.addParameterValue(tp.getParameterIndex(), parameter, true);
            }
        }
        return hasTaint;
    }
    public void setTargetValue(MethodEvent event, TaintPosition tp) {
        if (tp.isObject()) {
            event.setObjectValue(true);
            addTaintHash(event, event.objectInstance,0);
        } else if (tp.isParameter() && event.parameterInstances.length > tp.getParameterIndex()) {
            addTaintHash(event, event.parameterInstances[tp.getParameterIndex()],0);
            event.addParameterValue(tp.getParameterIndex(), event.parameterInstances[tp.getParameterIndex()], true);
        } else if (tp.isReturn()) {
            addTaintHash(event, event.returnInstance,0);
            event.setReturnValue(true);
        }
    }

//    protected void addTaintHash(MethodEvent event, TaintFlowNode taintFlowNode, Object obj, int depth) {
    protected void addTaintHash(MethodEvent event, Object obj, int depth) {
        //addTaintHash() 把target中的污点hash加入污点池
        if (depth < 10 && TaintPoolUtils.isNotEmpty(obj) && TaintPoolUtils.isAllowTaintType(obj)) {//限制对象嵌套10层
            int hash = System.identityHashCode(obj);
            if (!EngineManager.TAINT_HASH_CODES.contains(hash)) {
                Class<?> cls = obj.getClass();//return hash加入taintHashCodes,类型为map/array/list时递归加入taintHashCodes
//                if (cls.isArray() && !cls.getComponentType().isPrimitive()) {
//                    trackArray(event, sourceNode, obj, depth);
//                } else if (obj instanceof Iterator) {
//                    trackIterator(event, sourceNode, (Iterator)obj, depth);
//                } else if (obj instanceof Map) {
//                    trackMap(event, sourceNode, (Map)obj, depth);
//                } else if (obj instanceof Map.Entry) {
//                    trackMapEntry(event, sourceNode, (Map.Entry)obj, depth);
//                } else if (obj instanceof Collection) {
//                    if (obj instanceof List) {
//                        trackList(event, sourceNode, (List)obj, depth);
//                    } else {
//                        trackIterator(event, sourceNode, ((Collection)obj).iterator(), depth);
//                    }
//                } else if ("java.util.Optional".equals(obj.getClass().getName())) {
//                    trackOptional(event, sourceNode, obj, depth);
//                } else {
//                    int len = TaintRangesBuilder.getLength(obj);
//                    if (len == 0) {
//                        return;
//                    }

//                    TaintRanges tr = new TaintRanges(new TaintRange[]{new TaintRange(0, len)});
//                    if (sourceNode.hasTags()) {
//                        String[] tags = sourceNode.getTags();
//                        String[] var9 = tags;
//                        int var10 = tags.length;
//
//                        for(int var11 = 0; var11 < var10; ++var11) {
//                            String tag = var9[var11];
//                            tr.add(new TaintRange(tag, 0, len));
//                        }
//                    }

//                    event.targetRanges.add(new MethodEvent.MethodEventTargetRange(hash, tr));
                EngineManager.TAINT_HASH_CODES.add(hash);
                event.addTargetHash(hash);
//                    EngineManager.TAINT_RANGES_POOL.add(hash, tr);
//                }

            }
        }
    }
    protected boolean setIfSourcesInTaintPool(MethodEvent event, TaintFlowNode taintFlowNode) {
//        Iterator var3 = SOURCE_CHECKERS.iterator();
//
//        while(var3.hasNext()) {
//            SinkSourceChecker chk = (SinkSourceChecker)var3.next();
//            if (chk.match(event, sinkNode)) {
//                return chk.checkSource(event, sinkNode);
//            }
//        }

//        List<Object> sourceInstances = new ArrayList();
        boolean hasTaint = false;
        Set<TaintPosition> sources = taintFlowNode.getSources();
        Iterator var7 = sources.iterator();

        while (var7.hasNext()) {
            TaintPosition tp = (TaintPosition) var7.next();
             if(setSourceValue(event, tp)){//规则中source是多个,target只能一个
                 hasTaint = true;//只要source有一个是污点即可
             }
        }

//        if (VulnType.REFLECTED_XSS.equals(sinkNode.getVulType()) && !sourceInstances.isEmpty()) {
//            boolean tagsHit = false;
//            Iterator var17 = sourceInstances.iterator();
//
//            while(var17.hasNext()) {
//                Object sourceInstance = var17.next();
//                int hash = System.identityHashCode(sourceInstance);
//                TaintRanges tr = EngineManager.TAINT_RANGES_POOL.get(hash);
//                if (tr != null && !tr.isEmpty()) {
//                    TaintTag[] required = new TaintTag[]{TaintTag.UNTRUSTED, TaintTag.CROSS_SITE};
//                    TaintTag[] disallowed = new TaintTag[]{TaintTag.XSS_ENCODED, TaintTag.URL_ENCODED, TaintTag.HTML_ENCODED, TaintTag.BASE64_ENCODED};
//                    if (tr.hasRequiredTaintTags(required) && !tr.hasDisallowedTaintTags(disallowed)) {
//                        tagsHit = true;
//                    }
//                }
//            }
//
//            if (!tagsHit) {
//                return false;
//            }
//        }

//        if (hasTaint) {
//            event.setObjectValue(event.objectInstance, objHasTaint);
//        }
        return hasTaint;
    }
}
