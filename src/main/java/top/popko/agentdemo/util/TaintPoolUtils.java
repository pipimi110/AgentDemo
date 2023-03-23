package top.popko.agentdemo.util;

import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;
import top.popko.agentdemo.handler.hookpoint.models.track.TaintTrack;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaintPoolUtils {
    public TaintPoolUtils() {
    }

    public static boolean isJdkType(Object obj) {
        return obj instanceof String || obj instanceof Map || obj instanceof List;
    }

    public static boolean poolContains(Object obj, MethodEvent event) {
        if (obj == null) {
            return false;
        } else {
            boolean isContains = contains(obj);
            if (isContains) {
//                event.addSourceHash(System.identityHashCode(obj));
                return true;
            } else {
                int var5;
                int var6;
                if (obj instanceof String[]) {
                    String[] stringArray = (String[])((String[])obj);
                    String[] var4 = stringArray;
                    var5 = stringArray.length;

                    for(var6 = 0; var6 < var5; ++var6) {
                        String stringItem = var4[var6];
                        if (poolContains(stringItem, event)) {
                            return true;
                        }
                    }
                } else if (obj instanceof Object[]) {
                    Object[] objArray = (Object[])((Object[])obj);
                    Object[] var9 = objArray;
                    var5 = objArray.length;

                    for(var6 = 0; var6 < var5; ++var6) {
                        Object objItem = var9[var6];
                        if (poolContains(objItem, event)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }
    }

    private static boolean contains(Object obj) {
        return TaintTrack.TAINT_HASH_CODES.contains(System.identityHashCode(obj));
    }

    public static boolean isNotEmpty(Object obj) {
        try {
            if (obj == null) {
                return false;
            } else if (HashCode.calc(obj) == 0) {
                return false;
            } else if (obj instanceof Map) {
                Map<?, ?> taintValue = (Map)obj;
                return !taintValue.isEmpty();
            } else if (obj instanceof List) {
                List<?> taintValue = (List)obj;
                return !taintValue.isEmpty();
            } else if (obj instanceof Set) {
                Set<?> taintValue = (Set)obj;
                return !taintValue.isEmpty();
            } else if (obj instanceof String) {
                String taintValue = (String)obj;
                return !taintValue.isEmpty();
            } else {
                return true;
            }
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isAllowTaintType(Class<?> objType) {
        return objType != Boolean.class && objType != Boolean[].class && objType != Short.class && objType != Short[].class && objType != Integer.class && objType != Integer[].class && objType != Long.class && objType != Long[].class && objType != Double.class && objType != Double[].class && objType != Float.class && objType != Float[].class && objType != BigDecimal.class && objType != BigDecimal[].class && objType != Boolean.TYPE && objType != boolean[].class && objType != Short.TYPE && objType != short[].class && objType != Integer.TYPE && objType != int[].class && objType != Long.TYPE && objType != long[].class && objType != Double.TYPE && objType != double[].class && objType != float[].class && objType != Float.TYPE;
    }

    public static boolean isAllowTaintType(Object obj) {
        return obj == null ? false : isAllowTaintType(obj.getClass());
    }

    public static boolean isAllowTaintGetterMethod(Method method) {
        String methodName = method.getName();
        return methodName.startsWith("get") && !methodName.equals("getClass") && !methodName.equals("getParserForType") && !methodName.equals("getDefaultInstance") && !methodName.equals("getDefaultInstanceForType") && !methodName.equals("getDescriptor") && !methodName.equals("getDescriptorForType") && !methodName.equals("getAllFields") && !methodName.equals("getInitializationErrorString") && !methodName.equals("getUnknownFields") && !methodName.equals("getDetailOrBuilderList") && !methodName.equals("getAllFieldsMutable") && !methodName.equals("getAllFieldsRaw") && !methodName.equals("getOneofFieldDescriptor") && !methodName.equals("getField") && !methodName.equals("getFieldRaw") && !methodName.equals("getRepeatedFieldCount") && !methodName.equals("getRepeatedField") && !methodName.equals("getSerializedSize") && !methodName.equals("getMethodOrDie") && !methodName.endsWith("Bytes") && method.getParameterCount() == 0 ? isAllowTaintType(method.getReturnType()) : false;
    }
}
