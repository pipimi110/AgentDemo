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
//    private final Set<String> BLACKS;
//    private final String[] START_WITH_BLACKS;
//    private final String[] END_WITH_BLACKS;
//    private final Set<String> BLACKS_SET;
//    private final String[] START_ARRAY;
//    private final String[] END_ARRAY;
    private final String[] DISABLE_EXT;
//    private final AbstractMatcher INTERNAL_CLASS = new InternalClass();
//    private final AbstractMatcher FRAMEWORK_CLASS = new FrameworkClass();
//    private final AbstractMatcher SERVER_CLASS = new ServerClass();
    private Instrumentation inst;
//    private final Set<String> BLACK_URL;
//    public final Set<String> FALLBACK_URL = new HashSet();

    public static ConfigMatcher getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ConfigMatcher();
        }

        return INSTANCE;
    }

    public ConfigMatcher() {
        PropertyUtils cfg = PropertyUtils.getInstance();
//        String blackListFuncFile = cfg.getBlackFunctionFilePath();
//        String blackList = cfg.getBlackClassFilePath();
//        String blackUrl = cfg.getBlackUrl();
//        Set<String>[] items = ConfigUtils.loadConfigFromFile(blackListFuncFile);
//        this.BLACKS = items[0];
//        this.END_WITH_BLACKS = (String[])items[2].toArray(new String[0]);
//        this.START_WITH_BLACKS = (String[])items[1].toArray(new String[0]);
//        this.BLACK_URL = ConfigUtils.loadConfigFromFileByLine(blackUrl);
//        items = ConfigUtils.loadConfigFromFile(blackList);
//        this.START_ARRAY = (String[])items[1].toArray(new String[0]);
//        this.END_ARRAY = (String[])items[2].toArray(new String[0]);
//        this.BLACKS_SET = items[0];
//        this.DISABLE_EXT = ConfigUtils.loadExtConfigFromFile(disableExtList);
//        String disableExtList = cfg.getBlackExtFilePath();
        this.DISABLE_EXT = ".js,.css,.htm,.html,.jpg,.png,.gif,.woff,.woff2,.ico,.maps,.xml,.map".split(",");
    }

    public boolean disableExtension(String uri) {
        return uri != null && !uri.isEmpty() ? StringUtils.endsWithAny(uri, this.DISABLE_EXT) : false;
    }

//    public boolean getBlackUrl(Map<String, Object> var1) {
//        // $FF: Couldn't be decompiled
//    }

//    private boolean inHookBlacklist(String className) {
//        return this.BLACKS_SET.contains(className) || StringUtils.startsWithAny(className, this.START_ARRAY) || StringUtils.endsWithAny(className, this.END_ARRAY);
//    }
//
//    public PropagatorType blackFunc(String signature) {
//        return !this.BLACKS.contains(signature) && !StringUtils.startsWithAny(signature, this.START_WITH_BLACKS) && !StringUtils.endsWithAny(signature, this.END_WITH_BLACKS) ? ConfigMatcher.PropagatorType.NONE : ConfigMatcher.PropagatorType.BLACK;
//    }

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
//        if (className.startsWith("[")) {
//            DongTaiLog.trace("ignore transform {}. Reason: class is a Array Type", new Object[]{className});
//            return false;
//        } else if (className.contains("/$Proxy")) {
//            DongTaiLog.trace("ignore transform {}. Reason: classname is a aop class by Proxy", new Object[]{className});
//            return false;
//        } else if (!className.startsWith("com/secnium/iast/") && !className.startsWith("java/lang/iast/") && !className.startsWith("cn/huoxian/iast/") && !className.startsWith("io/dongtai/") && !className.startsWith("oshi/") && !className.startsWith("io/dongtai/iast/thirdparty/com/sun/jna/")) {
//            if (this.inHookBlacklist(className)) {
//                DongTaiLog.trace("ignore transform {}. Reason: classname is startswith com/secnium/iast/", new Object[]{className});
//                return false;
//            } else if (className.contains("CGLIB$$")) {
//                DongTaiLog.trace("ignore transform {}. Reason: classname is a aop class by CGLIB", new Object[]{className});
//                return false;
//            } else if (className.contains("$$Lambda$")) {
//                DongTaiLog.trace("ignore transform {}. Reason: classname is a aop class by Lambda", new Object[]{className});
//                return false;
//            } else if (className.contains("_$$_jvst")) {
//                DongTaiLog.trace("ignore transform {}. Reason: classname is a aop class", new Object[]{className});
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            DongTaiLog.trace("ignore transform {}. Reason: class is in blacklist", new Object[]{className});
//            return false;
//        }
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
