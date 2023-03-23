package top.popko.agentdemo.handler.hookpoint.models;

import top.popko.agentdemo.handler.hookpoint.models.policy.TaintPosition;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  int[] sourcePositions/targetPositions :base/result表示为-1/-2 参数0-n)
 */
public class MethodEvent {
//    private static final int MAX_VALUE_LENGTH = 1024;
    private int invokeId;
    public boolean source;
    private Set<TaintPosition> sourcePositions;
//    public int[] sourcePositions;
    private Set<TaintPosition> targetPositions;
//    public int[] targetPositions;
    public final String originClassName;
    private final String matchedClassName;
    public final String methodName;
    public final  String signature;
    public Object objectInstance;
    public String objectValue;
    public Object[] parameterInstances;
    public List<Parameter> parameterValues = new ArrayList();
    public Object returnInstance;
    public String returnValue;
    public final Set<Integer> sourceHashes = new HashSet();
    public final Set<Integer> targetHashes = new HashSet();
//    public List<MethodEventTargetRange> targetRanges = new ArrayList();
//    public List<MethodEventSourceType> sourceTypes;
    private StackTraceElement
        callStack;

    public static class Parameter {
        private final String index;
        private final String value;

        public Parameter(String index, String value) {
            this.index = index;
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        //        public JSONObject toJson() {
//            JSONObject json = new JSONObject();
//            json.put("index", this.index);
//            json.put("value", this.value);
//            return json;
//        }
    }

    public MethodEvent(String originClassName, String matchedClassName, String methodName, String signature, Object objectInstance, Object[] parameterInstances, Object returnInstance) {
        //初始化//规则
        this.matchedClassName = matchedClassName;
        this.originClassName = originClassName;
        this.methodName = methodName;
        this.signature = signature;
        this.objectInstance = objectInstance;
        this.parameterInstances = parameterInstances;
        this.returnInstance = returnInstance;
        this.source = false;
    }
    public int getInvokeId() {
        return this.invokeId;
    }

    public void setInvokeId(int invokeId) {
        this.invokeId = invokeId;
    }

    public boolean isSource() {
        return source;
    }

    public void setObjectInstance(Object objectInstance) {
        this.objectInstance = objectInstance;
    }

    public void setParameterInstances(Object[] parameterInstances) {
        this.parameterInstances = parameterInstances;
    }

    public void setReturnInstance(Object returnInstance) {
        this.returnInstance = returnInstance;
    }

//    public void setObjectValue(Object obj, boolean hasTaint) {
    public void setObjectValue(boolean hasTaint) {
        Object obj = this.objectInstance;
        if (obj != null) {
            this.objectValue = this.formatValue(obj, hasTaint);
        }
    }

    public void addParameterValue(int index, Object param, boolean hasTaint) {
        if (param != null) {
            String indexString = "P" + String.valueOf(index + 1);
            Parameter parameter = new Parameter(indexString, this.formatValue(param, hasTaint));
            this.parameterValues.add(parameter);
        }
    }
    public void setParameterValues(boolean hasTaint) {
        int index = 0;
        Object[] params = this.parameterInstances;
        if (params != null) {
            for(Object param:params){
                String indexString = "P" + String.valueOf(index + 1);
                Parameter parameter = new Parameter(indexString, this.formatValue(param, hasTaint));
                this.parameterValues.add(parameter);
                index++;
            }
        }
    }

//    public void setReturnValue(Object ret, boolean hasTaint) {
    public void setReturnValue(boolean hasTaint) {
        Object ret = this.returnInstance;
        if (ret != null) {
            this.returnValue = this.formatValue(ret, hasTaint);
        }
    }

    private String formatValue(Object val, boolean hasTaint) {
        String str = this.obj2String(val);
//        return "[" + StringUtils.normalize(str, 1024) + "]" + (hasTaint ? "*" : "") + str.length();
        return str;
    }


    public String obj2String(Object value) {
        StringBuilder sb = new StringBuilder();
        if (null == value) {
            return "";
        } else {
            try {
                if (value.getClass().isArray() && !value.getClass().getComponentType().isPrimitive()) {
                    Object[] taints = (Object[])((Object[])value);
                    Object[] var4 = taints;
                    int var5 = taints.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        Object taint = var4[var6];
                        if (taint != null) {
                            if (taint.getClass().isArray() && !taint.getClass().getComponentType().isPrimitive()) {
                                Object[] subTaints = (Object[])((Object[])taint);
                                Object[] var9 = subTaints;
                                int var10 = subTaints.length;

                                for(int var11 = 0; var11 < var10; ++var11) {
                                    Object subTaint = var9[var11];
                                    sb.append(subTaint.toString()).append(" ");
                                }
                            } else {
                                sb.append(taint.toString()).append(" ");
                            }
                        }
                    }
                } else if (value instanceof StringWriter) {
                    sb.append(((StringWriter)value).getBuffer().toString());
                } else {
                    sb.append(value.toString());
                }
            } catch (Exception var13) {
                sb.append(value.getClass().getName()).append("@").append(Integer.toHexString(System.identityHashCode(value)));
            }

            return sb.toString();
        }
    }

    public void setTaintPositions(Set<TaintPosition> sourcePositions, Set<TaintPosition> targetPositions) {
        this.sourcePositions = sourcePositions;
        this.targetPositions = targetPositions;
    }
}
