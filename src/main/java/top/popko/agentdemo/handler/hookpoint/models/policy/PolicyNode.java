package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */ 
/*    */ public abstract class PolicyNode
/*    */ {
/*    */   private String hashString;
/*    */   private Inheritable inheritable;
/*    */   protected MethodMatcher methodMatcher;
/*    */   
/*    */   public PolicyNode(MethodMatcher methodMatcher) {
/* 10 */     this.methodMatcher = methodMatcher;
/*    */   }
/*    */   
/*    */   public abstract PolicyNodeType getType();
/*    */   
/*    */   public Inheritable getInheritable() {
/* 16 */     return this.inheritable;
/*    */   }
/*    */   
/*    */   public void setInheritable(Inheritable inheritable) {
/* 20 */     this.inheritable = inheritable;
/*    */   }
/*    */   
/*    */   public MethodMatcher getMethodMatcher() {
/* 24 */     return this.methodMatcher;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 29 */     if (this.hashString == null) {
/* 30 */       this.hashString = getType().getName() + "/" + this.methodMatcher.toString();
/*    */     }
/* 32 */     return this.hashString;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 37 */     if (obj == null) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     if (this == obj) {
/* 42 */       return true;
/*    */     }
/*    */     
/* 45 */     if (getClass() == obj.getClass()) {
/* 46 */       return toString().equals(obj.toString());
/*    */     }
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     return toString().hashCode();
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\PolicyNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */