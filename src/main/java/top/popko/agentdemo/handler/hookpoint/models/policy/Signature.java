package top.popko.agentdemo.handler.hookpoint.models.policy;
/*     */ 
/*     */ public class Signature
/*     */ {
/*     */   public static final String ERR_SIGNATURE_EMPTY = "signature can not be empty";
/*     */   public static final String ERR_SIGNATURE_INVALID = "signature is invalid";
/*     */   private String signature;
/*     */   private String className;
/*     */   private String methodName;
/*     */   private String[] parameters;
/*     */   
/*     */   public Signature(String className, String methodName, String[] parameters) {
/*  13 */     this.className = className;
/*  14 */     this.methodName = methodName;
/*  15 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  19 */     return this.className;
/*     */   }
/*     */   
/*     */   public void setClassName(String className) {
/*  23 */     this.className = className;
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/*  27 */     return this.methodName;
/*     */   }
/*     */   
/*     */   public void setMethodName(String methodName) {
/*  31 */     this.methodName = methodName;
/*     */   }
/*     */   
/*     */   public String[] getParameters() {
/*  35 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setParameters(String[] parameters) {
/*  39 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public static Signature parse(String sign) {
/*  43 */     if (sign == null) {
/*  44 */       throw new IllegalArgumentException("signature can not be empty");
/*     */     }
/*  46 */     sign = sign.replaceAll(" ", "");
/*  47 */     if (sign.isEmpty()) {
/*  48 */       throw new IllegalArgumentException("signature can not be empty");
/*     */     }
/*  50 */     int parametersStartIndex = sign.indexOf("(");
/*  51 */     int parametersEndIndex = sign.indexOf(")");
/*     */     
/*  53 */     if (parametersStartIndex <= 2 || parametersEndIndex <= 3 || parametersStartIndex > parametersEndIndex || parametersEndIndex != sign
/*     */       
/*  55 */       .length() - 1) {
/*  56 */       throw new IllegalArgumentException("signature is invalid: " + sign);
/*     */     }
/*     */     
/*  59 */     String classAndMethod = sign.substring(0, parametersStartIndex);
/*  60 */     int methodStartIndex = classAndMethod.lastIndexOf(".");
/*  61 */     if (methodStartIndex <= 0) {
/*  62 */       throw new IllegalArgumentException("signature is invalid: " + sign);
/*     */     }
/*     */     
/*  65 */     String className = classAndMethod.substring(0, methodStartIndex);
/*  66 */     String methodName = classAndMethod.substring(methodStartIndex + 1, parametersStartIndex);
/*  67 */     String parametersStr = sign.substring(parametersStartIndex + 1, parametersEndIndex).trim();
/*  68 */     if (!parametersStr.isEmpty() && (parametersStr.contains("(") || parametersStr.contains(")"))) {
/*  69 */       throw new IllegalArgumentException("signature is invalid: " + sign);
/*     */     }
/*     */     
/*  72 */     String[] parameters = new String[0];
/*  73 */     if (!parametersStr.isEmpty()) {
/*  74 */       parameters = parametersStr.split(",");
/*     */     }
/*  76 */     return new Signature(className, methodName, parameters);
/*     */   }
/*     */   
/*     */   public static String normalizeSignature(String className, String methodName, String[] parameters) {
/*  80 */     StringBuilder sb = new StringBuilder(64);
/*  81 */     sb.append(className);
/*  82 */     sb.append('.');
/*  83 */     sb.append(methodName);
/*  84 */     sb.append('(');
/*  85 */     if (parameters != null && parameters.length != 0) {
/*  86 */       int i = 0;
/*  87 */       for (String parameter : parameters) {
/*  88 */         if (i > 0) {
/*  89 */           sb.append(",");
/*     */         }
/*  91 */         sb.append(parameter);
/*  92 */         i++;
/*     */       } 
/*     */     } 
/*  95 */     sb.append(')');
/*  96 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void updateSignature() {
/* 100 */     this.signature = normalizeSignature(this.className, this.methodName, this.parameters);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     if (this.signature == null) {
/* 106 */       updateSignature();
/*     */     }
/* 108 */     return this.signature;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 113 */     if (obj == null) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     if (!(obj instanceof Signature)) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     return toString().equals(obj.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 126 */     return toString().hashCode();
/*     */   }
/*     */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\Signature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */