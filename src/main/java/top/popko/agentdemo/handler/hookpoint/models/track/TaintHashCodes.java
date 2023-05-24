package top.popko.agentdemo.handler.hookpoint.models.track;

import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;

import java.util.HashSet;
import java.util.Map;

public class TaintHashCodes extends ThreadLocal<HashSet<Integer>> {
    public TaintHashCodes() {
    }

    protected HashSet<Integer> initialValue() {
        return null;
    }

    public boolean isEmpty() {
        return this.get() == null || ((HashSet) this.get()).isEmpty();
    }

    public boolean contains(Integer hashCode) {
        return this.get() == null ? false : ((HashSet) this.get()).contains(hashCode);
    }

    public void add(Integer hashCode) {
        if (this.get() != null) {
            ((HashSet) this.get()).add(hashCode);
        }
    }

    public void addObject(Object obj, MethodEvent event) {
//        if (TaintPoolUtils.isNotEmpty(obj) && TaintPoolUtils.isAllowTaintType(obj)) {
        try {
            int subHashCode;
            int var6;
            int var7;
            if (obj instanceof String[]) {
                String[] tempObjs = (String[]) obj;
                for (var7 = 0; var7 < tempObjs.length; ++var7) {
                    String tempObj = tempObjs[var7];
                    subHashCode = System.identityHashCode(tempObj);
                    this.add(subHashCode);
                    event.addTargetHash(subHashCode);
                }
            } else if (obj instanceof Map) {
                int hashCode = System.identityHashCode(obj);
                this.add(hashCode);
                    event.addTargetHash(hashCode);
            } else if (obj.getClass().isArray() && !obj.getClass().getComponentType().isPrimitive()) {
                Object[] tempObjs = (Object[]) ((Object[]) obj);
                if (tempObjs.length != 0) {
                    for (var7 = 0; var7 < tempObjs.length; ++var7) {
                        Object tempObj = tempObjs[var7];
                        this.addObject(tempObj, event);
                    }
                }
            } else {
                subHashCode = System.identityHashCode(obj);
                this.add(subHashCode);
                    event.addTargetHash(subHashCode);
            }
        } catch (Exception var9) {
        }

    }
}
