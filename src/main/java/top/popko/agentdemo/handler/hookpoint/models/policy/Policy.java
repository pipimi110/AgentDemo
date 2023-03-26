package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */ import java.util.*;
/*    */

/*    */
/*    */ public class Policy {
/*  6 */   private final List<SourceNode> sources = new ArrayList<>();
/*  7 */   private final List<PropagatorNode> propagators = new ArrayList<>();
/*  8 */   private final List<SinkNode> sinks = new ArrayList<>();
/*  9 */   private final Map<String, PolicyNode> policyNodesMap = new HashMap<>();
/* 10 */   private final Set<String> classHooks = new HashSet<>();
/* 11 */   private final Set<String> ancestorClassHooks = new HashSet<>();
/*    */   
/*    */   public List<SourceNode> getSources() {
/* 14 */     return this.sources;
/*    */   }
/*    */   
/*    */   public void addSource(SourceNode source) {
/* 18 */     this.sources.add(source);
/* 19 */     addPolicyNode(source);
/*    */   }
/*    */   
/*    */   public List<PropagatorNode> getPropagators() {
/* 23 */     return this.propagators;
/*    */   }
/*    */
/*    */   public void addPropagator(PropagatorNode propagator) {
/* 27 */     this.propagators.add(propagator);
/* 28 */     addPolicyNode(propagator);
/*    */   }
/*    */   
/*    */   public List<SinkNode> getSinks() {
/* 32 */     return this.sinks;
/*    */   }
/*    */   
/*    */   public void addSink(SinkNode sink) {
/* 36 */     this.sinks.add(sink);
/* 37 */     addPolicyNode(sink);
/*    */   }
/*    */   
/*    */   public PolicyNode getPolicyNode(String policyKey) {
/* 41 */     return this.policyNodesMap.get(policyKey);
/*    */   }
/*    */   
/*    */   public Map<String, PolicyNode> getPolicyNodesMap() {
/* 45 */     return this.policyNodesMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addPolicyNode(PolicyNode node) {
/* 50 */     if (node.getMethodMatcher() instanceof SignatureMethodMatcher) {
/* 51 */       SignatureMethodMatcher methodMatcher = (SignatureMethodMatcher)node.getMethodMatcher();
/* 52 */       this.policyNodesMap.put(node.toString(), node);
/* 53 */       addHooks(methodMatcher.getSignature().getClassName(), node.getInheritable());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addHooks(String className, Inheritable inheritable) {
/* 58 */     if (Inheritable.ALL.equals(inheritable) || Inheritable.SELF.equals(inheritable)) {
/* 59 */       this.classHooks.add(className);
/*    */     }
/* 61 */     if (Inheritable.ALL.equals(inheritable) || Inheritable.SUBCLASS.equals(inheritable)) {
/* 62 */       this.ancestorClassHooks.add(className);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getMatchedClass(String className, Set<String> ancestors) {
/* 67 */     if (this.classHooks.contains(className)) {
/* 68 */       return className;
/*    */     }
///* 70 */     for (String ancestor : ancestors) {
///* 71 */       if (this.ancestorClassHooks.contains(ancestor)) {
///* 72 */         return ancestor;
///*    */       }
///*    */     }// todo: 匹配父类继承的方法
/* 75 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isMatchClass(String className) {
/* 79 */     return (this.classHooks.contains(className) || this.ancestorClassHooks.contains(className));
/*    */   }
/*    */   
/*    */   public Set<String> getClassHooks() {
/* 83 */     return this.classHooks;
/*    */   }
/*    */   
/*    */   public Set<String> getAncestorClassHooks() {
/* 87 */     return this.ancestorClassHooks;
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\Policy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */