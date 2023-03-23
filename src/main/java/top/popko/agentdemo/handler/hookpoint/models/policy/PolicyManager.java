package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */

/*    */ import org.json.JSONArray;
import top.popko.agentdemo.util.StringUtils;
///*    */ import io.dongtai.log.DongTaiLog;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */
/*    */ public class PolicyManager
/*    */ {
/*    */   private static PolicyManager instance;
/*    */   private Policy policy;
/* 14 */   private static final Set<String> HOOK_CLASS_NAMES = new HashSet<>(Arrays.asList(new String[] { " javax.servlet.Filter"
/* 15 */           .substring(1), " javax.servlet.FilterChain"
/* 16 */           .substring(1), " javax.servlet.http.HttpServlet"
/* 17 */           .substring(1), " jakarta.servlet.http.HttpServlet"
/* 18 */           .substring(1), " javax.faces.webapp.FacesServlet"
/* 19 */           .substring(1), " javax.servlet.jsp.JspPage"
/* 20 */           .substring(1), " org.apache.jasper.runtime.HttpJspBase"
/* 21 */           .substring(1), " org.springframework.web.servlet.FrameworkServlet"
/* 22 */           .substring(1), " javax.servlet.http.Cookie"
/* 23 */           .substring(1), " org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter$ServletHandlerMethodInvoker"
/* 24 */           .substring(1) }));
/*    */
/* 26 */   private static final Set<String> HOOK_CLASS_SUFFIX_NAMES = new HashSet<>(Collections.singletonList(".dubbo.monitor.support.MonitorFilter"));

    public static PolicyManager getInstance(){
        if(instance == null){
            instance = new PolicyManager();
        }
        return instance;
    }

    /*    */
/*    */
/*    */
/*    */   public Policy getPolicy() {
/* 31 */     return this.policy;
/*    */   }
/*    */
/*    */   public void setPolicy(Policy policy) {
/* 35 */     this.policy = policy;
/*    */   }
/*    */
/*    */   public void loadPolicy(String policyPath) {
/*    */     try {
/*    */       JSONArray policyConfig;
/* 41 */       if (StringUtils.isEmpty(policyPath)) {
/* 42 */         policyConfig = PolicyBuilder.fetchFromServer();
/*    */       } else {
/* 44 */         policyConfig = PolicyBuilder.fetchFromFile(policyPath);
/*    */       }
/* 46 */       this.policy = PolicyBuilder.build(policyConfig);
/* 47 */     } catch (Throwable e) {
///* 48 */       DongTaiLog.error("load policy failed", e);
/*    */     }
/*    */   }
/*    */
/*    */   public static boolean isHookClass(String className) {
/* 53 */     return (HOOK_CLASS_NAMES.contains(className) || hookBySuffix(className));
/*    */   }
/*    */
/*    */   private static boolean hookBySuffix(String classname) {
/* 57 */     for (String suffix : HOOK_CLASS_SUFFIX_NAMES) {
/* 58 */       if (classname.endsWith(suffix)) {
/* 59 */         return true;
/*    */       }
/*    */     }
/* 62 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\PolicyManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */