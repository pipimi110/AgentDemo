package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SourceNode extends TaintFlowNode {
/*    */   private Set<TaintPosition> sources;
/*    */   private String[] tags;
/*    */   
/*    */   public SourceNode(Set<TaintPosition> sources, Set<TaintPosition> targets, MethodMatcher methodMatcher) {
/* 10 */     super(targets, methodMatcher);
/* 11 */     this.sources = sources;
/*    */   }
/*    */   
/*    */   public PolicyNodeType getType() {
/* 15 */     return PolicyNodeType.SOURCE;
/*    */   }
/*    */   
/*    */   public Set<TaintPosition> getSources() {
/* 19 */     return this.sources;
/*    */   }
/*    */   
/*    */   public void setSources(Set<TaintPosition> sources) {
/* 23 */     this.sources = sources;
/*    */   }
/*    */   
/*    */   public String[] getTags() {
/* 27 */     return this.tags;
/*    */   }
/*    */   
/*    */   public boolean hasTags() {
/* 31 */     return (this.tags != null && this.tags.length > 0);
/*    */   }
/*    */   
/*    */   public void setTags(String[] tags) {
/* 35 */     this.tags = tags;
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\SourceNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */