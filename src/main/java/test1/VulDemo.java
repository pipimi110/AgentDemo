package test1;

import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.Param.Source;
import top.popko.agentdemo.handler.hookpoint.models.Param.Propator;
import top.popko.agentdemo.util.ASMUtilsForJavaH;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class VulDemo {
    HashMap eventMap = new HashMap();
    HashMap firstEdges = new HashMap();
    HashMap otherEdges = new HashMap();
    List<Object> sinkObjList = new ArrayList<>();

    static int invokeId = 0;
    public void soutplus(String arg){
        System.out.println("[]plus: ".concat(arg));
    }
    public String func1(String arg){
        System.out.println("[]func1");
        return "[]func1: ".concat(arg);
    }


    public static void main(String[] args) {
        new VulDemo().soutplus("qwe");
//        VulDemo vulDemo = new VulDemo();
//        //测试demo无法获取return
//        //
//        Source source = vulDemo.source(new Source());
//        Propator propagator = vulDemo.propagate(source);
//        vulDemo.sink(propagator);
//        vulDemo.genChain();
//        vulDemo.showChain();
    }

    public Source source(Source arg) {
        setMethodEvent(new int[]{0}, new int[]{-2}, this, new Object[]{arg}, arg,Source.class);
        return arg;
    }

    private Propator propagate(Source arg) {
//        String ret = "[]propagated ".concat(arg);
        Propator propator = new Propator();
        setMethodEvent(new int[]{0}, new int[]{-2}, this, new Object[]{arg}, propator,Source.class);
        return propator;
    }

    //    private void sink(String arg, String ccc) {
    private void sink(Propator arg) {
        setMethodEvent(new int[]{0}, null, this, new Object[]{arg}, null,Propator.class);
        System.out.println(arg);
//        System.out.println(ccc.concat(this.cmd));
    }

    private void setMethodEvent(int[] sourcePositions, int[] targetPositions, Object objectInstance, Object[] parameterInstances, Object returnInstance,Class argClazz) {
        StackTraceElement currentStack = Thread.currentThread().getStackTrace()[2];
//        stackTraceElement[]
        String classname = currentStack.getClassName();
        String methodname = currentStack.getMethodName();
        try {
            Class clazz = Class.forName(classname, false, this.getClass().getClassLoader());
            Method method = clazz.getDeclaredMethod(methodname, argClazz);//todo: 多参数适配
            String signature = ASMUtilsForJavaH.getDesc(method);//desc
            MethodEvent methodEvent = new MethodEvent(sourcePositions, targetPositions, classname, methodname, signature);
            if (methodname.equals("source")) {
                methodEvent.source = true;
            }
            methodEvent.setObjectInstance(objectInstance);
            methodEvent.setParameterInstances(parameterInstances);
            methodEvent.setReturnInstance(returnInstance);
            methodEvent.setAllValue(true);//不知道作用
            eventMap.put(invokeId, methodEvent);
            invokeId++;
            System.out.println("setMethodEvent over");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void genChain() {
        Iterator it2 = this.eventMap.keySet().iterator();
        while (it2.hasNext()) {
            int key = (int) it2.next();
            System.out.println("[key = " + key + "] value = " + this.eventMap.get(key));
            MethodEvent methodEvent = (MethodEvent) this.eventMap.get(key);
            if (methodEvent.targetPositions == null) {
                for (int sp : methodEvent.sourcePositions) {
                    sinkObjList.add(p2obj(sp, methodEvent));
                }
            } else {
                if (methodEvent.isSource()) {
                    for (int sp : methodEvent.sourcePositions) {
                        for (int tp : methodEvent.targetPositions) {
//                        System.out.println(p2obj(sp,methodEvent)+"-->"+p2obj(tp,methodEvent));
                            firstEdges.put(p2obj(sp, methodEvent), p2obj(tp, methodEvent));
                        }
                    }
                } else {
                    for (int sp : methodEvent.sourcePositions) {
                        for (int tp : methodEvent.targetPositions) {
//                        System.out.println(p2obj(sp,methodEvent)+"-->"+p2obj(tp,methodEvent));
                            otherEdges.put(p2obj(sp, methodEvent), p2obj(tp, methodEvent));
                        }
                    }
                }
            }
        }
    }

    public void showChain() {
        Iterator firstEdgesIterator = this.firstEdges.keySet().iterator();
        while (firstEdgesIterator.hasNext()) {
            Object sourceObj = firstEdgesIterator.next();
            Object targetObj = this.firstEdges.get(sourceObj);
            recuNextEdge(targetObj);
        }
        System.out.println("1");
    }

    public void recuNextEdge(Object currentObj) {
        boolean flag = false;
        for (Object sink : sinkObjList) {
            if (sink instanceof MethodEvent.Parameter) {
                if (((MethodEvent.Parameter) sink).getValue() == currentObj) {
                    System.out.println("-->" + currentObj);
                    flag = true;
                    break;
                }
            } else {
                if (sink == currentObj) {
                    System.out.println("-->" + currentObj);
                    flag = true;
                    break;
                }
            }
        }
        if(!flag){
//        if (sinkObjList.contains(currentObj)) {//可能有漏报误报
//        } else {
            Object nextTargetObj = otherEdges.get(currentObj);
            if (nextTargetObj != null) {
                recuNextEdge(nextTargetObj);
            }
        }
    }

    public Object p2obj(int p, MethodEvent methodEvent) {
        if (p == -1) {
            return methodEvent.objectValue;
        } else if (p == -2) {
            return methodEvent.returnInstance;
        } else {
            return methodEvent.parameterValues.get(p);
        }
    }
}
