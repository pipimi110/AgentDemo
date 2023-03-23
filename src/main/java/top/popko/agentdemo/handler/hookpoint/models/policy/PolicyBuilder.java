package top.popko.agentdemo.handler.hookpoint.models.policy;
/*     */ 

/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
import top.popko.agentdemo.util.StringUtils;
///*     */ import io.dongtai.log.DongTaiLog;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class PolicyBuilder
/*     */ {
/*     */   private static final String KEY_DATA = "data";
/*     */   private static final String KEY_TYPE = "type";
/*     */   private static final String KEY_SOURCE = "source";
/*     */   private static final String KEY_TARGET = "target";
/*     */   
/*     */   public static JSONArray fetchFromServer() throws Exception {
///*     */     try {
///*  27 */       StringBuilder resp = HttpClientUtils.sendGet("/api/v1/profilesv2", null);
///*  28 */       JSONObject respObj = new JSONObject(resp.toString());
///*  29 */       return respObj.getJSONArray("data");
///*  30 */     } catch (JSONException e) {
///*  31 */       throw new PolicyException("policy config from server is invalid", e);
///*     */     }
/*     */   throw new Exception("test not allow fetchFromServer()");}
/*     */   private static final String KEY_SIGNATURE = "signature"; private static final String KEY_INHERIT = "inherit"; private static final String KEY_VUL_TYPE = "vul_type"; private static final String KEY_COMMAND = "command";
/*     */   public static JSONArray fetchFromFile(String path) throws PolicyException {
/*     */     try {
/*  37 */       File file = new File(path);
/*  38 */       String content = FileUtils.readFileToString(file);
/*  39 */       JSONObject respObj = new JSONObject(content);
/*  40 */       return respObj.getJSONArray("data");
/*  41 */     } catch (IOException e) {
/*  42 */       throw new PolicyException(String.format("read policy config file %s failed", new Object[] { path }), e);
/*  43 */     } catch (JSONException e) {
/*  44 */       throw new PolicyException(String.format("policy config file %s is invalid", new Object[] { path }), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Policy build(JSONArray policyConfig) throws PolicyException {
/*  49 */     if (policyConfig == null || policyConfig.length() == 0) {
/*  50 */       throw new PolicyException("policy config can not be empty");
/*     */     }
/*  52 */     int policyLen = policyConfig.length();
/*  53 */     Policy policy = new Policy();
/*  54 */     for (int i = 0; i < policyLen; i++) {
/*  55 */       JSONObject node = policyConfig.getJSONObject(i);
/*  56 */       if (node == null || node.length() == 0) {
/*  57 */         throw new PolicyException("policy node can not be empty");
/*     */       }
/*     */       
/*     */       try {
/*  61 */         PolicyNodeType nodeType = parseNodeType(node);
/*  62 */         buildSource(policy, nodeType, node);
///*  63 */         buildPropagator(policy, nodeType, node);
/*  64 */         buildSink(policy, nodeType, node);
/*  65 */       } catch (PolicyException e) {
///*  66 */         DongTaiLog.error(e.getMessage());
/*     */       } 
/*     */     } 
/*  69 */     return policy;
/*     */   }
/*     */   
/*     */   public static void buildSource(Policy policy, PolicyNodeType type, JSONObject node) throws PolicyException {
/*  73 */     if (!PolicyNodeType.SOURCE.equals(type)) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     Set<TaintPosition> sources = parseSource(node, type);
/*  78 */     Set<TaintPosition> targets = parseTarget(node, type);
/*  79 */     MethodMatcher methodMatcher = buildMethodMatcher(node);
/*  80 */     SourceNode sourceNode = new SourceNode(sources, targets, methodMatcher);
/*  81 */     setInheritable(node, sourceNode);
/*  82 */     List<String[]> tags = parseTags(node, sourceNode);
/*  83 */     sourceNode.setTags(tags.get(0));
/*  84 */     policy.addSource(sourceNode);
/*     */   }
/*     */   
///*     */   public static void buildPropagator(Policy policy, PolicyNodeType type, JSONObject node) throws PolicyException {
///*  88 */     if (!PolicyNodeType.PROPAGATOR.equals(type)) {
///*     */       return;
///*     */     }
///*     */
///*  92 */     Set<TaintPosition> sources = parseSource(node, type);
///*  93 */     Set<TaintPosition> targets = parseTarget(node, type);
///*  94 */     MethodMatcher methodMatcher = buildMethodMatcher(node);
///*     */
///*  96 */     PropagatorNode propagatorNode = new PropagatorNode(sources, targets, null, new String[0], methodMatcher);
///*  97 */     setInheritable(node, propagatorNode);
///*  98 */     List<String[]> tags = parseTags(node, propagatorNode);
///*  99 */     propagatorNode.setTags(tags.get(0));
///* 100 */     propagatorNode.setUntags(tags.get(1));
///* 101 */     policy.addPropagator(propagatorNode);
///*     */   }
/*     */   public static void buildSink(Policy policy, PolicyNodeType type, JSONObject node) throws PolicyException {
/*     */     SinkNode sinkNode;
/* 105 */     if (!PolicyNodeType.SINK.equals(type)) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     MethodMatcher methodMatcher = buildMethodMatcher(node);
/* 110 */     String vulType = parseVulType(node);
/*     */     
///* 112 */     if (VulnType.CRYPTO_WEAK_RANDOMNESS.equals(vulType)) {
///* 113 */       sinkNode = new SinkNode(new HashSet(), methodMatcher);
///*     */     } else {
/* 115 */       sinkNode = new SinkNode(parseSource(node, type), methodMatcher);
///*     */     }
/* 117 */     setInheritable(node, sinkNode);
/* 118 */     sinkNode.setVulType(vulType);
/* 119 */     sinkNode.setStackDenyList(parseStackDenyList(sinkNode));
/* 120 */     policy.addSink(sinkNode);
/*     */   }
/*     */   
/*     */   private static PolicyNodeType parseNodeType(JSONObject node) throws PolicyException {
/*     */     try {
/* 125 */       int type = node.getInt("type");
/* 126 */       PolicyNodeType nodeType = PolicyNodeType.get(Integer.valueOf(type));
/* 127 */       if (nodeType == null) {
/* 128 */         throw new PolicyException("policy node type is invalid: " + node.toString());
/*     */       }
/* 130 */       return nodeType;
/* 131 */     } catch (JSONException e) {
/* 132 */       throw new PolicyException("policy node type is invalid: " + node.toString(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Set<TaintPosition> parseSource(JSONObject node, PolicyNodeType type) throws PolicyException {
/*     */     try {
/* 138 */       return TaintPosition.parse(node.getString("source"));
/* 139 */     } catch (JSONException e) {
/* 140 */       if (!PolicyNodeType.SOURCE.equals(type) && !PolicyNodeType.FILTER.equals(type)) {
/* 141 */         throw new PolicyException("policy node source is invalid: " + node.toString(), e);
/*     */       }
/* 143 */     } catch (TaintPositionException e) {
/* 144 */       if (!PolicyNodeType.SOURCE.equals(type) && !PolicyNodeType.FILTER.equals(type)) {
/* 145 */         throw new PolicyException("policy node source is invalid: " + node.toString(), e);
/*     */       }
/*     */     } 
/* 148 */     return new HashSet<>();
/*     */   }
/*     */   
/*     */   private static Set<TaintPosition> parseTarget(JSONObject node, PolicyNodeType type) throws PolicyException {
/*     */     try {
/* 153 */       return TaintPosition.parse(node.getString("target"));
/* 154 */     } catch (JSONException e) {
/* 155 */       if (!PolicyNodeType.FILTER.equals(type)) {
/* 156 */         throw new PolicyException("policy node target is invalid: " + node.toString(), e);
/*     */       }
/* 158 */     } catch (TaintPositionException e) {
/* 159 */       if (!PolicyNodeType.FILTER.equals(type)) {
/* 160 */         throw new PolicyException("policy node target is invalid: " + node.toString(), e);
/*     */       }
/*     */     } 
/* 163 */     return new HashSet<>();
/*     */   }
/*     */   
/*     */   private static void setInheritable(JSONObject node, PolicyNode policyNode) throws PolicyException {
/*     */     try {
/* 168 */       Inheritable inheritable = Inheritable.parse(node.getString("inherit"));
/* 169 */       policyNode.setInheritable(inheritable);
/* 170 */     } catch (JSONException e) {
/* 171 */       throw new PolicyException("policy node inheritable value is invalid: " + node.toString(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String parseVulType(JSONObject node) throws PolicyException {
/*     */     try {
/* 177 */       String vulType = node.getString("vul_type");
/* 178 */       if (vulType == null || vulType.isEmpty()) {
/* 179 */         throw new PolicyException("policy sink node vul type is invalid: " + node.toString());
/*     */       }
/* 181 */       return vulType;
/* 182 */     } catch (JSONException e) {
/* 183 */       throw new PolicyException("policy sink node vul type is invalid: " + node.toString(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static MethodMatcher buildMethodMatcher(JSONObject node) throws PolicyException {
/*     */     try {
/* 189 */       String sign = node.getString("signature");
/* 190 */       if (StringUtils.isEmpty(sign)) {
/* 191 */         throw new PolicyException("policy node signature is invalid: " + node.toString());
/*     */       }
/* 193 */       Signature signature = Signature.parse(sign);
/*     */ 
/*     */       
/* 196 */       return new SignatureMethodMatcher(signature);
/* 197 */     } catch (Exception e) {
/* 198 */       throw new PolicyException("policy node signature is invalid: " + node.toString(), e);
/* 199 */     }


/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] parseStackDenyList(SinkNode node) {
/* 209 */     if (!(node.getMethodMatcher() instanceof SignatureMethodMatcher)) {
/* 210 */       return new String[0];
/*     */     }
/*     */     
/* 213 */     String signature = ((SignatureMethodMatcher)node.getMethodMatcher()).getSignature().toString();
/* 214 */     if ("java.lang.Class.forName(java.lang.String)".equals(signature))
/* 215 */       return new String[] { "java.net.URL.getURLStreamHandler" }; 
/* 216 */     if ("java.lang.Class.forName(java.lang.String,boolean,java.lang.ClassLoader)".equals(signature)) {
/* 217 */       return new String[] { "org.jruby.javasupport.JavaSupport.loadJavaClass" };
/*     */     }
/*     */     
/* 220 */     return new String[0];
/*     */   }
/*     */   
/*     */   private static List<String[]> parseTags(JSONObject node, PolicyNode policyNode) {
/* 224 */     List<String[]> empty = (List)Arrays.asList(new String[][] { {}, {} });
/* 225 */     if (!(policyNode.getMethodMatcher() instanceof SignatureMethodMatcher)) {
/* 226 */       return empty;
/*     */     }
/* 228 */     String signature = ((SignatureMethodMatcher)policyNode.getMethodMatcher()).getSignature().toString();
/*     */ 
/*     */     
/* 231 */     List<String[]> taintTags = PolicyTag.TAGS.get(signature);
/* 232 */     if (taintTags == null || taintTags.size() != 2) {
/* 233 */       return empty;
/*     */     }
/*     */     
/* 236 */     return taintTags;
/*     */   }
/*     */ }


/* Location:              D:\javaWebTool\iast\dongtai\dongtai-agent-docker\bin\dongtai-core.jar!\io\dongtai\iast\core\handler\hookpoint\models\policy\PolicyBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */