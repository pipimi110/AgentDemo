package top.popko.agentdemo.config;

import java.lang.instrument.Instrumentation;

public class ConfigMatcher {
    private static ConfigMatcher INSTANCE;
    private Instrumentation inst;

    public static ConfigMatcher getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ConfigMatcher();
        }

        return INSTANCE;
    }

    public void setInst(Instrumentation inst) {
        this.inst = inst;
    }

    public boolean canHook(Class<?> clazz) {
        if (!this.inst.isModifiableClass(clazz)) {
            return false;
        } else if (clazz.isInterface()) {
            return false;
        } else {
            String className = clazz.getName().replace('.', '/');
            return this.canHook(className);
        }
    }

    public boolean canHook(String className) {
        if (className.startsWith("top/popko/agentdemo") || className.startsWith("[") || className.contains("/$Proxy") || className.contains("CGLIB$$") || className.contains("$$Lambda$") || className.contains("_$$_jvst")) {
            return false;
        } else {
            return true;
        }

    }
}
