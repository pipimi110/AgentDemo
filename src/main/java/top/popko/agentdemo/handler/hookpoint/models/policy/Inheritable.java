package top.popko.agentdemo.handler.hookpoint.models.policy;
import top.popko.agentdemo.util.StringUtils;
/*    */
/*    */ 
/*    */ public enum Inheritable {
/*  6 */   ALL("all"),
/*  7 */   SUBCLASS("true"),
/*  8 */   SELF("false");
/*    */   
/*    */   private final String value;
/*    */ 
/*    */   
/*    */   Inheritable(String value) {
/* 14 */     this.value = value;
/*    */   }
/*    */   
/*    */   public static Inheritable parse(String str) throws PolicyException {
/* 18 */     if (!StringUtils.isEmpty(str)) {
/* 19 */       Inheritable[] values = values();
/* 20 */       for (Inheritable inheritable : values) {
/* 21 */         if (inheritable.value.equalsIgnoreCase(str)) {
/* 22 */           return inheritable;
/*    */         }
/*    */       } 
/*    */     } 
/* 26 */     throw new PolicyException("policy node inheritable value is invalid: " + str);
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\Inheritable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */