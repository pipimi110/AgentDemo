package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SinkNode extends TaintFlowNode {
/*    */   private String vulType;
/*    */   private String[] stackDenyList;
/*    */   
/*    */   public SinkNode(Set<TaintPosition> sources, MethodMatcher methodMatcher) {
/* 11 */     super(null, methodMatcher);
/* 12 */     this.sources = sources;
/*    */   }
/*    */   
/*    */   public PolicyNodeType getType() {
/* 16 */     return PolicyNodeType.SINK;
/*    */   }
/*    */   
/*    */   public String getVulType() {
/* 28 */     return this.vulType;
/*    */   }
/*    */   
/*    */   public void setVulType(String vulType) {
/* 32 */     this.vulType = vulType;
/*    */   }
/*    */   
/*    */   public boolean hasDenyStack(StackTraceElement[] stackTraceElements) {
/* 36 */     if (this.stackDenyList == null || this.stackDenyList.length == 0) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     int stackSize = stackTraceElements.length;
/* 41 */     boolean multi = (this.stackDenyList.length > 1);
/* 42 */     String[] cachedStack = null;
/* 43 */     if (multi) {
/* 44 */       cachedStack = new String[stackSize];
/*    */     }
/* 46 */     for (int i = 0; i < this.stackDenyList.length; i++) {
/* 47 */       for (int j = 0; j < stackSize; j++) {
/*    */         String stack;
/* 49 */         if (multi && i == 0) {
/* 50 */           stack = stackTraceElements[j].toString();
/* 51 */           cachedStack[j] = stack;
/* 52 */         } else if (multi) {
/* 53 */           stack = cachedStack[j];
/*    */         } else {
/* 55 */           stack = stackTraceElements[j].toString();
/*    */         } 
/* 57 */         if (stack.contains(this.stackDenyList[i])) {
/* 58 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public void setStackDenyList(String[] stackDenyList) {
/* 67 */     this.stackDenyList = stackDenyList;
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\SinkNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */