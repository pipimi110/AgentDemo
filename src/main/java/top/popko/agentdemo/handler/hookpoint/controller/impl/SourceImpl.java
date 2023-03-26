package top.popko.agentdemo.handler.hookpoint.controller.impl;

import top.popko.agentdemo.EngineManager;
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
import top.popko.agentdemo.EngineManager;
import top.popko.agentdemo.util.StackUtils;
import top.popko.agentdemo.util.TaintPoolUtils;

public class SourceImpl extends AbstractSolve {
    private final ArrayList<String> WHITE_ATTRIBUTES = new ArrayList();
    private final String METHOD_OF_GETATTRIBUTE = "getAttribute";
    private final String VALUES_ENUMERATOR = " org.apache.tomcat.util.http.ValuesEnumerator".substring(1);
    private final String SPRING_OBJECT = " org.springframework.".substring(1);

    public SourceImpl() {
    }


    public void solveSource(MethodEvent event, SourceNode sourceNode) {
        System.out.println("[+]solveSource");
//        if (TaintPoolUtils.isNotEmpty(event.returnInstance) && TaintPoolUtils.isAllowTaintType(event.returnInstance) && allowCall(event)) {
        if (TaintPoolUtils.isNotEmpty(event.returnInstance) && TaintPoolUtils.isAllowTaintType(event.returnInstance)) {
//            int length = TaintRangesBuilder.getLength(event.returnInstance);//return不为空/空的字符串/空的map
//            if (length == 0) {
            if (event.returnInstance != null) {
                Iterator var5;
                setIfSourcesInTaintPool(event, sourceNode);//sourceImpl不用判断sources是否在污点池中
//                var5 = sourceNode.getSources().iterator();//sources遍历
                TaintPosition tp;
//                while (var5.hasNext()) {
//                    tp = (TaintPosition) var5.next();
//                    setSourceValue(event, tp);
//                }

                var5 = sourceNode.getTargets().iterator();//targets遍历
                while (var5.hasNext()) {
                    tp = (TaintPosition) var5.next();
                    setTargetValue(event, tp);
                }

//                if (!TaintPosition.hasObject(sourceNode.getSources()) && !TaintPosition.hasObject(sourceNode.getTargets())) {
//                    event.setObjectValue(event.objectInstance, false);//不是污点的base为什么要设置???
//                }
                event.source = true;//下面只添加一层
                event.setCallStacks(StackUtils.createCallStack(4));//collectMethod->solveSource->createCallStack->getStackTrace
                event.setTaintPositions(sourceNode.getSources(), sourceNode.getTargets());
                addTrackEvent(event);
            }
        }
    }

    public boolean isInTaintPool(Object obj, MethodEvent event) {
        return true;
    }

//    private boolean scanTarget(MethodEvent event, SourceNode sourceNode) {
//
//        if () {//只判断不为null
//            return false;
//        } else {
//            addTaintHash(event, sourceNode, event.returnInstance, 0);
////            handlerCustomModel(event, sourceNode);
//            return true;
//        }
//    }


//    private void trackArray(MethodEvent event, SourceNode sourceNode, Object arr, int depth) {
//        int length = Array.getLength(arr);
//
//        for(int i = 0; i < length; ++i) {
//            addTaintHash(event, sourceNode, Array.get(arr, i), depth);
//        }
//
//    }
//
//    private void trackIterator(MethodEvent event, SourceNode sourceNode, Iterator<?> it, int depth) {
//        while(it.hasNext()) {
//            addTaintHash(event, sourceNode, it.next(), depth + 1);
//        }
//
//    }
//
//    private void trackMap(MethodEvent event, SourceNode sourceNode, Map<?, ?> map, int depth) {
//        Iterator var4 = map.keySet().iterator();
//
//        while(var4.hasNext()) {
//            Object key = var4.next();
//            addTaintHash(event, sourceNode, key, depth);
//            addTaintHash(event, sourceNode, map.get(key), depth);
//        }
//
//    }
//
//    private void trackMapEntry(MethodEvent event, SourceNode sourceNode, Map.Entry<?, ?> entry, int depth) {
//        addTaintHash(event, sourceNode, entry.getKey(), depth + 1);
//        addTaintHash(event, sourceNode, entry.getValue(), depth + 1);
//    }
//
//    private void trackList(MethodEvent event, SourceNode sourceNode, List<?> list, int depth) {
//        Iterator var4 = list.iterator();
//
//        while(var4.hasNext()) {
//            Object obj = var4.next();
//            addTaintHash(event, sourceNode, obj, depth);
//        }
//
//    }
//
//    private void trackOptional(MethodEvent event, SourceNode sourceNode, Object obj, int depth) {
//        try {
//            Object v = ((Optional)obj).orElse((Object)null);
//            addTaintHash(event, sourceNode, v, depth);
//        } catch (Throwable var5) {
//            DongTaiLog.warn("track optional object failed: " + var5.getMessage());
//        }
//
//    }
//
//    public void handlerCustomModel(MethodEvent event, SourceNode sourceNode) {
//        if (!event.getMethodName().equals("getSession")) {
//            Set<Object> modelValues = parseCustomModel(event.returnInstance);
//            Iterator var3 = modelValues.iterator();
//
//            while(var3.hasNext()) {
//                Object modelValue = var3.next();
//                addTaintHash(event, sourceNode, modelValue, 0);
//            }
//        }
//
//    }
//
//    public Set<Object> parseCustomModel(Object model) {
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
//    private boolean allowCall(MethodEvent event) {
//        boolean allowed = true;
//        return "getAttribute".equals(event.getMethodName()) ? allowAttribute((String)event.parameterInstances[0]) : allowed;
//    }
//
//    private boolean allowAttribute(String attribute) {
//        return WHITE_ATTRIBUTES.contains(attribute);
//    }
//
//    {
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.HandlerMapping.bestMatchingPattern".substring(1));
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping".substring(1));
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.HandlerMapping.uriTemplateVariables".substring(1));
//        WHITE_ATTRIBUTES.add(" org.springframework.web.servlet.View.pathVariables".substring(1));
//    }
}
