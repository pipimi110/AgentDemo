package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ public abstract class TaintFlowNode extends PolicyNode {
/*    */   protected Set<TaintPosition> targets;
            protected Set<TaintPosition> sources;
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
/*    */   public void setTargets(Set<TaintPosition> targets) {
/* 18 */     this.targets = targets;
/*    */   }
/*    */   public Set<TaintPosition> getSources() {
/* 19 */     return this.sources;
/*    */   }
/*    */
/*    */   public void setSources(Set<TaintPosition> sources) {
/* 23 */     this.sources = sources;
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\TaintFlowNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */