//package top.popko.agentdemo.handler.hookpoint.models.policy;
///*    */
///*    */ import io.dongtai.iast.core.handler.hookpoint.models.taint.range.TaintCommand;
///*    */ import java.util.Set;
///*    */
///*    */ public class PropagatorNode
///*    */   extends TaintFlowNode
///*    */ {
///*    */   private Set<TaintPosition> sources;
///*    */   private TaintCommand command;
///*    */   private String[] commandArguments;
///*    */   private String[] tags;
///*    */   private String[] untags;
///*    */
///*    */   public PropagatorNode(Set<TaintPosition> sources, Set<TaintPosition> targets, TaintCommand command, String[] commandArguments, MethodMatcher methodMatcher) {
///* 16 */     super(targets, methodMatcher);
///* 17 */     this.sources = sources;
///* 18 */     this.command = command;
///* 19 */     this.commandArguments = commandArguments;
///*    */   }
///*    */
///*    */   public PolicyNodeType getType() {
///* 23 */     return PolicyNodeType.PROPAGATOR;
///*    */   }
///*    */
///*    */   public Set<TaintPosition> getSources() {
///* 27 */     return this.sources;
///*    */   }
///*    */
///*    */   public void setSources(Set<TaintPosition> sources) {
///* 31 */     this.sources = sources;
///*    */   }
///*    */
///*    */   public TaintCommand getCommand() {
///* 35 */     return this.command;
///*    */   }
///*    */
///*    */   public void setCommand(TaintCommand command) {
///* 39 */     this.command = command;
///*    */   }
///*    */
///*    */   public String[] getCommandArguments() {
///* 43 */     return this.commandArguments;
///*    */   }
///*    */
///*    */   public void setCommandArguments(String[] commandArguments) {
///* 47 */     this.commandArguments = commandArguments;
///*    */   }
///*    */
///*    */   public String[] getTags() {
///* 51 */     return this.tags;
///*    */   }
///*    */
///*    */   public boolean hasTags() {
///* 55 */     return (this.tags != null && this.tags.length > 0);
///*    */   }
///*    */
///*    */   public void setTags(String[] tags) {
///* 59 */     this.tags = tags;
///*    */   }
///*    */
///*    */   public String[] getUntags() {
///* 63 */     return this.untags;
///*    */   }
///*    */
///*    */   public void setUntags(String[] untags) {
///* 67 */     this.untags = untags;
///*    */   }
///*    */ }
//
//
///* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\PropagatorNode.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.3
// */