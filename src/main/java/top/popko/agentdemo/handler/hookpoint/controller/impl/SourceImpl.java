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
        if (TaintPoolUtils.isNotEmpty(event.returnInstance) && TaintPoolUtils.isAllowTaintType(event.returnInstance)) {
//            int length = TaintRangesBuilder.getLength(event.returnInstance);//return不为空/空的字符串/空的map
            if (event.returnInstance != null) {
                Iterator var5;
                setIfSourcesInTaintPool(event, sourceNode);//sourceImpl不用判断sources是否在污点池中
//                var5 = sourceNode.getSources().iterator();//sources遍历
                TaintPosition tp;
                var5 = sourceNode.getTargets().iterator();//targets遍历
                while (var5.hasNext()) {
                    tp = (TaintPosition) var5.next();
                    setTargetValue(event, tp);
                }

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

}
