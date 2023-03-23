package top.popko.agentdemo.handler.hookpoint.models.policy;
/*    */ 
/*    */ public enum PolicyNodeType {
/*  4 */   SOURCE(2, "source"),
/*  5 */   PROPAGATOR(1, "propagator"),
/*  6 */   FILTER(3, "filter"),
/*  7 */   SINK(4, "sink");
/*    */   
/*    */   private final int type;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   PolicyNodeType(int type, String name) {
/* 14 */     this.name = name;
/* 15 */     this.type = type;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 19 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public static PolicyNodeType get(Integer type) {
/* 27 */     if (type == null) {
/* 28 */       return null;
/*    */     }
/* 30 */     PolicyNodeType[] values = values();
/* 31 */     for (PolicyNodeType policyNodeType : values) {
/* 32 */       if (policyNodeType.type == type.intValue()) {
/* 33 */         return policyNodeType;
/*    */       }
/*    */     } 
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\PolicyNodeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */