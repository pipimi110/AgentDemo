package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ public abstract class TaintFlowNode extends PolicyNode {
/*    */   protected Set<TaintPosition> targets;
/*    */   
/*    */   public TaintFlowNode(Set<TaintPosition> targets, MethodMatcher methodMatcher) {
/*  9 */     super(methodMatcher);
/* 10 */     this.targets = targets;
/*    */   }
/*    */   
/*    */   public Set<TaintPosition> getTargets() {
/* 14 */     return this.targets;
/*    */   }
/*    */   
/*    */   public void setTargets(Set<TaintPosition> sources) {
/* 18 */     this.targets = sources;
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\TaintFlowNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */