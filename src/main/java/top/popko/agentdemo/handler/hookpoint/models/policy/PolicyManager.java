package top.popko.agentdemo.handler.hookpoint.models.policy;


import org.json.JSONArray;
import top.popko.agentdemo.util.StringUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PolicyManager {
    private static PolicyManager instance;
    private Policy policy;
    private static final Set<String> HOOK_CLASS_NAMES = new HashSet<>(Arrays.asList(new String[]{" javax.servlet.Filter"
            .substring(1), " javax.servlet.FilterChain"
            .substring(1), " javax.servlet.http.HttpServlet"
            .substring(1), " jakarta.servlet.http.HttpServlet"
            .substring(1), " javax.faces.webapp.FacesServlet"
            .substring(1), " javax.servlet.jsp.JspPage"
            .substring(1), " org.apache.jasper.runtime.HttpJspBase"
            .substring(1), " org.springframework.web.servlet.FrameworkServlet"
            .substring(1), " javax.servlet.http.Cookie"
            .substring(1), " org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter$ServletHandlerMethodInvoker"
            .substring(1)}));

    private static final Set<String> HOOK_CLASS_SUFFIX_NAMES = new HashSet<>(Collections.singletonList(".dubbo.monitor.support.MonitorFilter"));

    public static PolicyManager getInstance() {
        if (instance == null) {
            instance = new PolicyManager();
        }
        return instance;
    }


    public Policy getPolicy() {
        return this.policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public void loadPolicy(String configPath) {
        try {
            JSONArray policyConfig;
            if (StringUtils.isEmpty(configPath)) {
                policyConfig = PolicyBuilder.fetchFromServer();
            } else {
                policyConfig = PolicyBuilder.fetchFromFile(configPath + "policy.json");
            }
            this.policy = PolicyBuilder.build(policyConfig);
        } catch (Throwable e) {
        }
    }

    public static boolean isHookClass(String className) {
        return (HOOK_CLASS_NAMES.contains(className) || hookBySuffix(className));
    }

    private static boolean hookBySuffix(String classname) {
        for (String suffix : HOOK_CLASS_SUFFIX_NAMES) {
            if (classname.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
