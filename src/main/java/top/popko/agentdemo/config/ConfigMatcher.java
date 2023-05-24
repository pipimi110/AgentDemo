package top.popko.agentdemo.config;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import top.popko.agentdemo.util.ConfigUtils;
import top.popko.agentdemo.util.PropertyUtils;
import top.popko.agentdemo.util.StringUtils;


public class ConfigMatcher {
    private static ConfigMatcher INSTANCE;
    private final String[] DISABLE_EXT;
    private Instrumentation inst;

    public static ConfigMatcher getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ConfigMatcher();
        }

        return INSTANCE;
    }

    public ConfigMatcher() {
        PropertyUtils cfg = PropertyUtils.getInstance();
        this.DISABLE_EXT = ".js,.css,.htm,.html,.jpg,.png,.gif,.woff,.woff2,.ico,.maps,.xml,.map".split(",");
    }

    public boolean disableExtension(String uri) {
        return uri != null && !uri.isEmpty() ? StringUtils.endsWithAny(uri, this.DISABLE_EXT) : false;
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

    public static enum PropagatorType {
        BLACK,
        SOURCE,
        SINK,
        NONE;

        private PropagatorType() {
        }
    }
}
