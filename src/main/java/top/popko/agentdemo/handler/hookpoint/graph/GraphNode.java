package top.popko.agentdemo.handler.hookpoint.graph;

import org.json.JSONArray;
import org.json.JSONObject;
import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.policy.TaintPosition;
import top.popko.agentdemo.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GraphNode {
    private final boolean isSource;
    private final int invokeId;
    private List<String> sourcePositions = new ArrayList();
    private List<String> targetPositions = new ArrayList();
    private final String callerClass;
    private final String callerMethod;
    private final int callerLineNumber;
    private final String matchedClassName;
    private final String originClassName;
    private final String methodName;
    private final String signature;
    private final String objectValue;
    private final List<MethodEvent.Parameter> parameterValues;
    private final String returnValue;
    private final Set<Integer> sourceHash;
    private final Set<Integer> targetHash;
//    private List<MethodEvent.MethodEventTargetRange> targetRanges = new ArrayList();
    public List<MethodEvent.MethodEventSourceType> sourceTypes;

    public GraphNode(MethodEvent event) {
        this.isSource = event.isSource();
        this.invokeId = event.getInvokeId();
        Iterator var2;
        TaintPosition tgt;
        if (event.getSourcePositions() != null && event.getSourcePositions().size() > 0) {
            var2 = event.getSourcePositions().iterator();

            while(var2.hasNext()) {
                tgt = (TaintPosition)var2.next();
                this.sourcePositions.add(tgt.toString());
            }
        }

        if (event.getTargetPositions() != null && event.getTargetPositions().size() > 0) {
            var2 = event.getTargetPositions().iterator();

            while(var2.hasNext()) {
                tgt = (TaintPosition)var2.next();
                this.targetPositions.add(tgt.toString());
            }
        }

        this.matchedClassName = event.getMatchedClassName();
        this.originClassName = event.getOriginClassName();
        this.methodName = event.getMethodName();
        this.signature = event.getSignature();
        this.objectValue = event.objectValue;
        this.parameterValues = event.parameterValues;
        this.returnValue = event.returnValue;
        this.callerClass = event.getCallerClass();
        this.callerMethod = event.getCallerMethod();
        this.callerLineNumber = event.getCallerLine();
        this.sourceHash = event.getSourceHashes();
        this.targetHash = event.getTargetHashes();
//        this.targetRanges = event.targetRanges;
        this.sourceTypes = event.sourceTypes;
    }

    public JSONObject toJson() {
        JSONObject value = new JSONObject();
        JSONArray parameterArray = new JSONArray();
        JSONArray sourceHashArray = new JSONArray();
        JSONArray targetHashArray = new JSONArray();
        JSONObject taintPosition = new JSONObject();
        value.put("invokeId", this.invokeId);
        value.put("source", this.isSource);
        value.put("originClassName", this.originClassName);
        value.put("className", this.matchedClassName);
        value.put("methodName", this.methodName);
        value.put("signature", this.signature);
        value.put("callerClass", this.callerClass);
        value.put("callerMethod", this.callerMethod);
        value.put("callerLineNumber", this.callerLineNumber);
        value.put("sourceHash", sourceHashArray);
        value.put("targetHash", targetHashArray);
        value.put("taintPosition", taintPosition);
        if (this.sourcePositions.size() > 0) {
            taintPosition.put("source", this.sourcePositions);
        }

        if (this.targetPositions.size() > 0) {
            taintPosition.put("target", this.targetPositions);
        }

        if (!StringUtils.isEmpty(this.objectValue)) {
            value.put("objValue", this.objectValue);
        } else {
            value.put("objValue", "");
        }

        Iterator var6;
        if (this.parameterValues != null && this.parameterValues.size() > 0) {
            var6 = this.parameterValues.iterator();

            while(var6.hasNext()) {
                MethodEvent.Parameter parameter = (MethodEvent.Parameter)var6.next();
                parameterArray.put(parameter.toJson());
            }

            value.put("parameterValues", parameterArray);
        }

        if (!StringUtils.isEmpty(this.returnValue)) {
            value.put("retValue", this.returnValue);
        }

        var6 = this.sourceHash.iterator();

        Integer hash;
        while(var6.hasNext()) {
            hash = (Integer)var6.next();
            sourceHashArray.put(hash);
        }

        var6 = this.targetHash.iterator();

        while(var6.hasNext()) {
            hash = (Integer)var6.next();
            targetHashArray.put(hash);
        }

        JSONArray st;
        Iterator var11;
//        if (this.targetRanges.size() > 0) {
//            st = new JSONArray();
//            value.put("targetRange", st);
//            var11 = this.targetRanges.iterator();
//
//            while(var11.hasNext()) {
//                MethodEvent.MethodEventTargetRange range = (MethodEvent.MethodEventTargetRange)var11.next();
//                st.put(range.toJson());
//            }
//        }

        if (this.sourceTypes != null && this.sourceTypes.size() > 0) {
            st = new JSONArray();
            value.put("sourceType", st);
            var11 = this.sourceTypes.iterator();

            while(var11.hasNext()) {
                MethodEvent.MethodEventSourceType s = (MethodEvent.MethodEventSourceType)var11.next();
                st.put(s.toJson());
            }
        }

        return value;
    }
}
