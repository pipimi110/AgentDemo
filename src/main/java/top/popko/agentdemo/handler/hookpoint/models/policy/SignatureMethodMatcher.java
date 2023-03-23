package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */
/*    */ import top.popko.agentdemo.enhance.MethodContext;
import java.util.Arrays;
/*    */ 
/*    */ public class SignatureMethodMatcher
/*    */   implements MethodMatcher {
/*    */   private final Signature signature;
/*    */   
/*    */   public SignatureMethodMatcher(Signature signature) {
/* 11 */     this.signature = signature;
/*    */   }
/*    */   
/*    */   public boolean match(MethodContext method) {
/* 15 */     if (!this.signature.getClassName().equals(method.getMatchedClassName())) {
/* 16 */       return false;
/*    */     }
/* 18 */     if (!this.signature.getMethodName().equals(method.getMethodName())) {
/* 19 */       return false;
/*    */     }
/*    */     
/* 22 */     return Arrays.equals((Object[])this.signature.getParameters(), (Object[])method.getParameters());
/*    */   }
/*    */   
/*    */   public Signature getSignature() {
/* 26 */     return this.signature;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return getClass().getName() + "/" + this.signature.toString();
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\SignatureMethodMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */