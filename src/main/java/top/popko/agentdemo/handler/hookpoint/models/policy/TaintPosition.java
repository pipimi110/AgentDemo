package top.popko.agentdemo.handler.hookpoint.models.policy;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ public class TaintPosition
/*     */ {
/*     */   public static final String OBJECT = "O";
/*     */   public static final String RETURN = "R";
/*     */   public static final String PARAM_PREFIX = "P";
/*     */   public static final String OR = "\\|";
/*  13 */   public static final TaintPosition POS_OBJECT = new TaintPosition("O");
/*  14 */   public static final TaintPosition POS_RETURN = new TaintPosition("R");
/*     */   
/*     */   public static final String ERR_POSITION_EMPTY = "taint position can not empty";
/*     */   
/*     */   public static final String ERR_POSITION_INVALID = "taint position invalid";
/*     */   
/*     */   public static final String ERR_POSITION_PARAMETER_INDEX_INVALID = "taint position parameter index invalid";
/*     */   
/*     */   private final String value;
/*     */   
/*     */   private final int parameterIndex;
/*     */   
/*     */   public TaintPosition(String value) {
/*  27 */     if (value == null) {
/*  28 */       throw new IllegalArgumentException("taint position can not empty");
/*     */     }
/*  30 */     value = value.trim();
/*  31 */     if (value.isEmpty()) {
/*  32 */       throw new IllegalArgumentException("taint position can not empty");
/*     */     }
/*     */     
/*  35 */     int index = -1;
/*  36 */     boolean isParameter = value.startsWith("P");
/*  37 */     if (isParameter) {
/*  38 */       String idx = value.substring(1).trim();
/*     */       try {
/*  40 */         index = Integer.parseInt(idx) - 1;
/*  41 */         if (index < 0) {
/*  42 */           throw new NumberFormatException("position index can not be negative: " + String.valueOf(index));
/*     */         }
/*  44 */       } catch (NumberFormatException e) {
/*  45 */         throw new IllegalArgumentException("taint position parameter index invalid: " + value + ", " + e.getMessage());
/*     */       } 
/*  47 */       this.value = "P" + idx;
/*     */     } else {
/*  49 */       if (!"O".equals(value) && !"R".equals(value)) {
/*  50 */         throw new IllegalArgumentException("taint position invalid: " + value);
/*     */       }
/*  52 */       this.value = value;
/*     */     } 
/*     */     
/*  55 */     this.parameterIndex = index;
/*     */   }
/*     */   
/*     */   public boolean isObject() {
/*  59 */     return equals(POS_OBJECT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReturn() {
/*  64 */     return equals(POS_RETURN);
/*     */   }
/*     */   
/*     */   public boolean isParameter() {
/*  68 */     return (this.parameterIndex >= 0);
/*     */   }
/*     */   
/*     */   public int getParameterIndex() {
/*  72 */     return this.parameterIndex;
/*     */   }
/*     */   
/*     */   public static Set<TaintPosition> parse(String position) throws TaintPositionException {
/*  76 */     if (position == null || position.isEmpty()) {
/*  77 */       throw new TaintPositionException("taint position can not be empty");
/*     */     }
/*  79 */     return parse(position.split("\\|"));
/*     */   }
/*     */   
/*     */   private static Set<TaintPosition> parse(String[] positions) throws TaintPositionException {
/*  83 */     if (positions == null || positions.length == 0) {
/*  84 */       throw new TaintPositionException("taint positions can not be empty");
/*     */     }
/*  86 */     Set<TaintPosition> tps = new HashSet<>();
/*  87 */     for (String position : positions) {
/*  88 */       position = position.trim();
/*  89 */       if (position.startsWith("P")) {
/*  90 */         Set<TaintPosition> paramPos = parseParameter(position.substring(1));
/*  91 */         if (!paramPos.isEmpty()) {
/*  92 */           tps.addAll(paramPos);
/*     */         }
/*     */       }
/*  95 */       else if (!position.isEmpty()) {
/*     */ 
/*     */         
/*  98 */         tps.add(parseOne(position));
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return tps;
/*     */   }
/*     */   
/*     */   private static Set<TaintPosition> parseParameter(String indiesStr) throws TaintPositionException {
/* 106 */     String[] indies = indiesStr.split(",");
/* 107 */     Set<TaintPosition> tps = new HashSet<>();
/* 108 */     for (String index : indies) {
/* 109 */       tps.add(parseOne("P" + index));
/*     */     }
/* 111 */     return tps;
/*     */   }
/*     */   
/*     */   private static TaintPosition parseOne(String position) throws TaintPositionException {
/*     */     try {
/* 116 */       return new TaintPosition(position);
/* 117 */     } catch (IllegalArgumentException e) {
/* 118 */       throw new TaintPositionException(e.getMessage(), e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean hasObject(Set<TaintPosition> positions) {
/* 123 */     if (positions == null) {
/* 124 */       return false;
/*     */     }
/* 126 */     return positions.contains(POS_OBJECT);
/*     */   }
/*     */   
/*     */   public static boolean hasReturn(Set<TaintPosition> positions) {
/* 130 */     if (positions == null) {
/* 131 */       return false;
/*     */     }
/* 133 */     return positions.contains(POS_RETURN);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasParameter(Set<TaintPosition> positions) {
/* 138 */     if (positions == null) {
/* 139 */       return false;
/*     */     }
/* 141 */     for (TaintPosition position : positions) {
/* 142 */       if (position.isParameter()) {
/* 143 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 147 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean hasParameterIndex(Set<TaintPosition> positions, int index) {
/* 151 */     if (positions == null || index < 0) {
/* 152 */       return false;
/*     */     }
/* 154 */     for (TaintPosition position : positions) {
/* 155 */       if (position.getParameterIndex() == index) {
/* 156 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 170 */     if (obj == null) {
/* 171 */       return false;
/*     */     }
/*     */     
/* 174 */     if (!(obj instanceof TaintPosition)) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     TaintPosition position = (TaintPosition)obj;
/* 179 */     return this.value.equals(position.value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 185 */     return this.value.hashCode();
/*     */   }
/*     */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\TaintPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */