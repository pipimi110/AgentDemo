package top.popko.agentdemo.handler.hookpoint.models.policy;


import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import top.popko.agentdemo.util.HttpClientUtils;
import top.popko.agentdemo.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolicyBuilder {

    public static JSONArray fetchFromServer() throws Exception {
        try {
            StringBuilder resp = HttpClientUtils.sendGet(System.getProperty("serverhost") + "/iast/show_hook_rules");
            JSONObject respObj = new JSONObject(resp.toString());
            return respObj.getJSONArray("data");
        } catch (JSONException e) {
            throw new PolicyException("policy config from server is invalid", e);
        }
//  throw new Exception("test not allow fetchFromServer()");
    }

    public static JSONArray fetchFromFile(String path) throws PolicyException {
        try {
            File file = new File(path);
            String content = FileUtils.readFileToString(file);
            JSONObject respObj = new JSONObject(content);
            return respObj.getJSONArray("data");
        } catch (IOException e) {
            throw new PolicyException(String.format("read policy config file %s failed", new Object[]{path}), e);
        } catch (JSONException e) {
            throw new PolicyException(String.format("policy config file %s is invalid", new Object[]{path}), e);
        }
    }

    public static Policy build(JSONArray policyConfig) throws PolicyException {
        if (policyConfig == null || policyConfig.length() == 0) {
            throw new PolicyException("policy config can not be empty");
        }
        int policyLen = policyConfig.length();
        Policy policy = new Policy();
        for (int i = 0; i < policyLen; i++) {
            JSONObject node = policyConfig.getJSONObject(i);
            if (node == null || node.length() == 0) {
                throw new PolicyException("policy node can not be empty");
            }

            try {
                PolicyNodeType nodeType = parseNodeType(node);
                buildSource(policy, nodeType, node);
                buildPropagator(policy, nodeType, node);
                buildSink(policy, nodeType, node);
            } catch (PolicyException e) {
            }
        }
        return policy;
    }

    public static void buildSource(Policy policy, PolicyNodeType type, JSONObject node) throws PolicyException {
        if (!PolicyNodeType.SOURCE.equals(type)) {
            return;
        }

        Set<TaintPosition> sources = parseSource(node, type);
        Set<TaintPosition> targets = parseTarget(node, type);
        MethodMatcher methodMatcher = buildMethodMatcher(node);
        SourceNode sourceNode = new SourceNode(sources, targets, methodMatcher);
        setInheritable(node, sourceNode);
        List<String[]> tags = parseTags(node, sourceNode);
        sourceNode.setTags(tags.get(0));
        policy.addSource(sourceNode);
    }

    public static void buildPropagator(Policy policy, PolicyNodeType type, JSONObject node) throws PolicyException {
        if (!PolicyNodeType.PROPAGATOR.equals(type)) {
            return;
        }

        Set<TaintPosition> sources = parseSource(node, type);
        Set<TaintPosition> targets = parseTarget(node, type);
        MethodMatcher methodMatcher = buildMethodMatcher(node);

//     PropagatorNode propagatorNode = new PropagatorNode(sources, targets, null, new String[0], methodMatcher);
        PropagatorNode propagatorNode = new PropagatorNode(sources, targets, new String[0], methodMatcher);
        setInheritable(node, propagatorNode);
        List<String[]> tags = parseTags(node, propagatorNode);
        propagatorNode.setTags(tags.get(0));
        propagatorNode.setUntags(tags.get(1));
        policy.addPropagator(propagatorNode);
    }

    public static void buildSink(Policy policy, PolicyNodeType type, JSONObject node) throws PolicyException {
        SinkNode sinkNode;
        if (!PolicyNodeType.SINK.equals(type)) {
            return;
        }

        MethodMatcher methodMatcher = buildMethodMatcher(node);
        String vulType = parseVulType(node);
        sinkNode = new SinkNode(parseSource(node, type), methodMatcher);
        setInheritable(node, sinkNode);
        sinkNode.setVulType(vulType);
        sinkNode.setStackDenyList(parseStackDenyList(sinkNode));
        policy.addSink(sinkNode);
    }

    private static PolicyNodeType parseNodeType(JSONObject node) throws PolicyException {
        try {
            int type = node.getInt("type");
            PolicyNodeType nodeType = PolicyNodeType.get(Integer.valueOf(type));
            if (nodeType == null) {
                throw new PolicyException("policy node type is invalid: " + node.toString());
            }
            return nodeType;
        } catch (JSONException e) {
            throw new PolicyException("policy node type is invalid: " + node.toString(), e);
        }
    }

    private static Set<TaintPosition> parseSource(JSONObject node, PolicyNodeType type) throws PolicyException {
        try {
            return TaintPosition.parse(node.getString("source"));
        } catch (JSONException e) {
            if (!PolicyNodeType.SOURCE.equals(type) && !PolicyNodeType.FILTER.equals(type)) {
                throw new PolicyException("policy node source is invalid: " + node.toString(), e);
            }
        } catch (TaintPositionException e) {
            if (!PolicyNodeType.SOURCE.equals(type) && !PolicyNodeType.FILTER.equals(type)) {
                throw new PolicyException("policy node source is invalid: " + node.toString(), e);
            }
        }
        return new HashSet<>();
    }

    private static Set<TaintPosition> parseTarget(JSONObject node, PolicyNodeType type) throws PolicyException {
        try {
            return TaintPosition.parse(node.getString("target"));
        } catch (JSONException e) {
            if (!PolicyNodeType.FILTER.equals(type)) {
                throw new PolicyException("policy node target is invalid: " + node.toString(), e);
            }
        } catch (TaintPositionException e) {
            if (!PolicyNodeType.FILTER.equals(type)) {
                throw new PolicyException("policy node target is invalid: " + node.toString(), e);
            }
        }
        return new HashSet<>();
    }

    private static void setInheritable(JSONObject node, PolicyNode policyNode) throws PolicyException {
        try {
            Inheritable inheritable = Inheritable.parse(node.getString("inherit"));
            policyNode.setInheritable(inheritable);
        } catch (JSONException e) {
            throw new PolicyException("policy node inheritable value is invalid: " + node.toString(), e);
        }
    }

    private static String parseVulType(JSONObject node) throws PolicyException {
        try {
            String vulType = node.getString("vul_type");
            if (vulType == null || vulType.isEmpty()) {
                throw new PolicyException("policy sink node vul type is invalid: " + node.toString());
            }
            return vulType;
        } catch (JSONException e) {
            throw new PolicyException("policy sink node vul type is invalid: " + node.toString(), e);
        }
    }

    private static MethodMatcher buildMethodMatcher(JSONObject node) throws PolicyException {
        try {
            String sign = node.getString("signature");
            if (StringUtils.isEmpty(sign)) {
                throw new PolicyException("policy node signature is invalid: " + node.toString());
            }
            Signature signature = Signature.parse(sign);


            return new SignatureMethodMatcher(signature);
        } catch (Exception e) {
            throw new PolicyException("policy node signature is invalid: " + node.toString(), e);
        }


    }


    private static String[] parseStackDenyList(SinkNode node) {
        if (!(node.getMethodMatcher() instanceof SignatureMethodMatcher)) {
            return new String[0];
        }

        String signature = ((SignatureMethodMatcher) node.getMethodMatcher()).getSignature().toString();
        if ("java.lang.Class.forName(java.lang.String)".equals(signature))
            return new String[]{"java.net.URL.getURLStreamHandler"};
        if ("java.lang.Class.forName(java.lang.String,boolean,java.lang.ClassLoader)".equals(signature)) {
            return new String[]{"org.jruby.javasupport.JavaSupport.loadJavaClass"};
        }

        return new String[0];
    }

    private static List<String[]> parseTags(JSONObject node, PolicyNode policyNode) {
        List<String[]> empty = (List) Arrays.asList(new String[][]{{}, {}});
        if (!(policyNode.getMethodMatcher() instanceof SignatureMethodMatcher)) {
            return empty;
        }
        String signature = ((SignatureMethodMatcher) policyNode.getMethodMatcher()).getSignature().toString();


        List<String[]> taintTags = PolicyTag.TAGS.get(signature);
        if (taintTags == null || taintTags.size() != 2) {
            return empty;
        }

        return taintTags;
    }
}

