package top.popko.agentdemo.handler.hookpoint.controller.impl;


import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.SinkNode;
import top.popko.agentdemo.handler.hookpoint.models.policy.TaintPosition;
import top.popko.agentdemo.handler.hookpoint.models.track.TaintTrack;
import top.popko.agentdemo.util.TaintPoolUtils;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class SinkImpl extends AbstractSolve{
    public SinkImpl() {
    }

    public static void solveSink(MethodEvent event, SinkNode sinkNode) {
        System.out.println("[+]solveSink");
        if (null != event) {
            String vulType = sinkNode.getVulType();
//            if (VulnType.CRYPTO_WEAK_RANDOMNESS.equals(vulType)) {
//                (new CryptoWeakRandomnessVulScan()).scan(event, sinkNode);
//            } else if (VulnType.CRYPTO_BAD_MAC.equals(vulType)) {
//                (new CryptoBadMacVulScan()).scan(event, sinkNode);
//            } else if (VulnType.CRYPTO_BAC_CIPHERS.equals(vulType)) {
//                (new CryptoBacCiphersVulScan()).scan(event, sinkNode);
//            } else if (VulnType.COOKIE_FLAGS_MISSING.equals(vulType)) {
//                (new CookieFlagsMissingVulScan()).scan(event, sinkNode);
//            } else if (!EngineManager.TAINT_HASH_CODES.isEmpty()) {

//            if (!EngineManager.TAINT_HASH_CODES.isEmpty()) {
//                (new DynamicPropagatorScanner()).scan(event, sinkNode);
//            }
                if (sinkSourceHitTaintPool(event, sinkNode)) {//sink需要sink.source在hash池里
//                    StackTraceElement[] stackTraceElements = StackUtils.createCallStack(5);
//                    if (sinkNode.hasDenyStack(stackTraceElements)) {
//                        return;
//                    }

//                    event.setCallStacks(stackTraceElements);
                    event.setTaintPositions(sinkNode.getSources(), null);
                    addTrackEvent(event);
                }
        }
    }

    private static boolean sinkSourceHitTaintPool(MethodEvent event, SinkNode sinkNode) {
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
        Iterator var7 = sinkNode.getSources().iterator();

        while(var7.hasNext()) {
            TaintPosition position = (TaintPosition)var7.next();
            if (position.isObject()) {
                //比source多一个TaintPoolUtils.poolContains检查hash是否存在
                if (TaintPoolUtils.isNotEmpty(event.objectInstance) && TaintPoolUtils.isAllowTaintType(event.objectInstance) && TaintPoolUtils.poolContains(event.objectInstance, event)) {
                    hasTaint = true;
                    event.setObjectValue(true);
//                    this.addSourceInstance(sourceInstances, event.objectInstance);
                }
            } else if (position.isParameter()) {
                int parameterIndex = position.getParameterIndex();
                if (parameterIndex < event.parameterInstances.length) {
//                    boolean paramHasTaint = false;
                    Object parameter = event.parameterInstances[parameterIndex];
                    if (TaintPoolUtils.isNotEmpty(parameter) && TaintPoolUtils.isAllowTaintType(parameter) && TaintPoolUtils.poolContains(parameter, event)) {
//                        paramHasTaint = true;
                        hasTaint = true;
//                        this.addSourceInstance(sourceInstances, parameter);
                        event.addParameterValue(parameterIndex, parameter, true);
                    }

//                    event.addParameterValue(parameterIndex, parameter, paramHasTaint);
                }
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
