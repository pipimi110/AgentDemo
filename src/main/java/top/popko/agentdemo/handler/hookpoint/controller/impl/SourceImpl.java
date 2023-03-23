package top.popko.agentdemo.handler.hookpoint.controller.impl;

import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.SourceNode;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import top.popko.agentdemo.handler.hookpoint.models.policy.TaintPosition;
import top.popko.agentdemo.handler.hookpoint.models.track.TaintTrack;
import top.popko.agentdemo.util.TaintPoolUtils;

public class SourceImpl extends AbstractSolve{
    private static final ArrayList<String> WHITE_ATTRIBUTES = new ArrayList();
    private static final String METHOD_OF_GETATTRIBUTE = "getAttribute";
    private static final String VALUES_ENUMERATOR = " org.apache.tomcat.util.http.ValuesEnumerator".substring(1);
    private static final String SPRING_OBJECT = " org.springframework.".substring(1);

    public SourceImpl() {
    }

    public static void solveSource(MethodEvent event, SourceNode sourceNode) {
        System.out.println("[+]solveSource");
//        if (TaintPoolUtils.isNotEmpty(event.returnInstance) && TaintPoolUtils.isAllowTaintType(event.returnInstance) && allowCall(event)) {
        if (TaintPoolUtils.isNotEmpty(event.returnInstance) && TaintPoolUtils.isAllowTaintType(event.returnInstance)) {
            event.source = true;
//            event.setCallStacks(StackUtils.createCallStack(4));
            boolean valid = trackTarget(event, sourceNode);//trackTarget()->trackObject() 把
            if (valid) {
                Iterator var5;
//                var5 = sourceNode.getSources().iterator();//sources遍历
////
                TaintPosition tgt;
//                while(var5.hasNext()) {
//                    tgt = (TaintPosition)var5.next();
//                    if (tgt.isObject()) {
////                        event.setObjectValue(event.returnInstance, true);
//                        event.setObjectValue(true);
//                    } else if (tgt.isParameter() && event.parameterInstances.length > tgt.getParameterIndex()) {
//                        event.addParameterValue(tgt.getParameterIndex(), event.parameterInstances[tgt.getParameterIndex()], true);
//                    }
//                }
//
                var5 = sourceNode.getTargets().iterator();//targets遍历

                while(var5.hasNext()) {
                    tgt = (TaintPosition)var5.next();
                    if (tgt.isObject()) {
                        event.setObjectValue(true);
                    } else if (tgt.isParameter()) {
                        if (event.parameterInstances.length > tgt.getParameterIndex()) {
                            event.addParameterValue(tgt.getParameterIndex(), event.parameterInstances[tgt.getParameterIndex()], true);
                        }
                    } else if (tgt.isReturn()) {//规则全是return//只会进入这里
                        event.setReturnValue(true);
                    }
                }

//                if (!TaintPosition.hasObject(sourceNode.getSources()) && !TaintPosition.hasObject(sourceNode.getTargets())) {
//                    event.setObjectValue(event.objectInstance, false);//不是污点的base为什么要设置???
//                }

                event.setTaintPositions(sourceNode.getSources(), sourceNode.getTargets());
                addTrackEvent(event);
            }
        }
    }

    private static boolean trackTarget(MethodEvent event, SourceNode sourceNode) {
//        int length = TaintRangesBuilder.getLength(event.returnInstance);//return不为空/空的字符串/空的map
//        if (length == 0) {
        if (event.returnInstance == null) {//只判断不为null
            return false;
        } else {
            trackObject(event, sourceNode, event.returnInstance, 0);//return hash加入taintHashCodes,类型为map/array/list时递归加入taintHashCodes
//            handlerCustomModel(event, sourceNode);
            return true;
        }
    }

    private static void trackObject(MethodEvent event, SourceNode sourceNode, Object obj, int depth) {
        if (depth < 10 && TaintPoolUtils.isNotEmpty(obj) && TaintPoolUtils.isAllowTaintType(obj)) {//限制对象嵌套10层
            int hash = System.identityHashCode(obj);
            if (!TaintTrack.TAINT_HASH_CODES.contains(hash)) {
                Class<?> cls = obj.getClass();
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
                    TaintTrack.TAINT_HASH_CODES.add(hash);
//                    event.addTargetHash(hash);
//                    EngineManager.TAINT_RANGES_POOL.add(hash, tr);
//                }

            }
        }
    }

//    private static void trackArray(MethodEvent event, SourceNode sourceNode, Object arr, int depth) {
//        int length = Array.getLength(arr);
//
//        for(int i = 0; i < length; ++i) {
//            trackObject(event, sourceNode, Array.get(arr, i), depth);
//        }
//
//    }
//
//    private static void trackIterator(MethodEvent event, SourceNode sourceNode, Iterator<?> it, int depth) {
//        while(it.hasNext()) {
//            trackObject(event, sourceNode, it.next(), depth + 1);
//        }
//
//    }
//
//    private static void trackMap(MethodEvent event, SourceNode sourceNode, Map<?, ?> map, int depth) {
//        Iterator var4 = map.keySet().iterator();
//
//        while(var4.hasNext()) {
//            Object key = var4.next();
//            trackObject(event, sourceNode, key, depth);
//            trackObject(event, sourceNode, map.get(key), depth);
//        }
//
//    }
//
//    private static void trackMapEntry(MethodEvent event, SourceNode sourceNode, Map.Entry<?, ?> entry, int depth) {
//        trackObject(event, sourceNode, entry.getKey(), depth + 1);
//        trackObject(event, sourceNode, entry.getValue(), depth + 1);
//    }
//
//    private static void trackList(MethodEvent event, SourceNode sourceNode, List<?> list, int depth) {
//        Iterator var4 = list.iterator();
//
//        while(var4.hasNext()) {
//            Object obj = var4.next();
//            trackObject(event, sourceNode, obj, depth);
//        }
//
//    }
//
//    private static void trackOptional(MethodEvent event, SourceNode sourceNode, Object obj, int depth) {
//        try {
//            Object v = ((Optional)obj).orElse((Object)null);
//            trackObject(event, sourceNode, v, depth);
//        } catch (Throwable var5) {
//            DongTaiLog.warn("track optional object failed: " + var5.getMessage());
//        }
//
//    }
//
//    public static void handlerCustomModel(MethodEvent event, SourceNode sourceNode) {
//        if (!event.getMethodName().equals("getSession")) {
//            Set<Object> modelValues = parseCustomModel(event.returnInstance);
//            Iterator var3 = modelValues.iterator();
//
//            while(var3.hasNext()) {
//                Object modelValue = var3.next();
//                trackObject(event, sourceNode, modelValue, 0);
//            }
//        }
//
//    }
//
//    public static Set<Object> parseCustomModel(Object model) {
//        try {
//            Set<Object> modelValues = new HashSet();
//            if (!TaintPoolUtils.isNotEmpty(model)) {
//                return modelValues;
//            } else {
//                Class<?> sourceClass = model.getClass();
//                if (sourceClass.getClassLoader() == null) {
//                    return modelValues;
//                } else {
//                    String className = sourceClass.getName();
//                    if (!className.startsWith("cn.huoxian.iast.api.") && !className.startsWith("io.dongtai.api.") && !className.startsWith(" org.apache.tomcat".substring(1)) && !className.startsWith(" org.apache.catalina".substring(1)) && !className.startsWith(" org.apache.shiro.web.servlet".substring(1)) && !VALUES_ENUMERATOR.equals(className) && !className.startsWith(SPRING_OBJECT) && !className.contains("RequestWrapper") && !className.contains("ResponseWrapper")) {
//                        Method[] methods = sourceClass.getMethods();
//                        Object itemValue = null;
//                        Method[] var6 = methods;
//                        int var7 = methods.length;
//
//                        for(int var8 = 0; var8 < var7; ++var8) {
//                            Method method = var6[var8];
//                            if (TaintPoolUtils.isAllowTaintGetterMethod(method)) {
//                                try {
//                                    method.setAccessible(true);
//                                    itemValue = method.invoke(model);
//                                    if (TaintPoolUtils.isNotEmpty(itemValue)) {
//                                        modelValues.add(itemValue);
//                                        if (itemValue instanceof List) {
//                                            List<?> itemValueList = (List)itemValue;
//                                            Iterator var11 = itemValueList.iterator();
//
//                                            while(var11.hasNext()) {
//                                                Object listValue = var11.next();
//                                                modelValues.addAll(parseCustomModel(listValue));
//                                            }
//                                        }
//                                    }
//                                } catch (Throwable var13) {
//                                    DongTaiLog.error("parse source custom model getter" + className + "." + method.getName() + " failed", var13);
//                                }
//                            }
//                        }
//
//                        return modelValues;
//                    } else {
//                        return modelValues;
//                    }
//                }
//            }
//        } catch (Throwable var14) {
//            return new HashSet();
//        }
//    }
//
//    private static boolean allowCall(MethodEvent event) {
//        boolean allowed = true;
//        return "getAttribute".equals(event.getMethodName()) ? allowAttribute((String)event.parameterInstances[0]) : allowed;
//    }
//
//    private static boolean allowAttribute(String attribute) {
//        return WHITE_ATTRIBUTES.contains(attribute);
//    }
//
//    static {
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.HandlerMapping.bestMatchingPattern".substring(1));
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping".substring(1));
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.HandlerMapping.uriTemplateVariables".substring(1));
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.View.pathVariables".substring(1));
//    }
}
