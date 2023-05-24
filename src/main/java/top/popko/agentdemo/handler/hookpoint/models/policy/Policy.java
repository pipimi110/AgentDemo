package top.popko.agentdemo.handler.hookpoint.models.policy;

import java.util.*;


public class Policy {
    private final List<SourceNode> sources = new ArrayList<>();
    private final List<PropagatorNode> propagators = new ArrayList<>();
    private final List<SinkNode> sinks = new ArrayList<>();
    private final Map<String, PolicyNode> policyNodesMap = new HashMap<>();
    private final Set<String> classHooks = new HashSet<>();
    private final Set<String> ancestorClassHooks = new HashSet<>();

    public List<SourceNode> getSources() {
        return this.sources;
    }

    public void addSource(SourceNode source) {
        this.sources.add(source);
        addPolicyNode(source);
    }

    public List<PropagatorNode> getPropagators() {
        return this.propagators;
    }

    public void addPropagator(PropagatorNode propagator) {
        this.propagators.add(propagator);
        addPolicyNode(propagator);
    }

    public List<SinkNode> getSinks() {
        return this.sinks;
    }

    public void addSink(SinkNode sink) {
        this.sinks.add(sink);
        addPolicyNode(sink);
    }

    public PolicyNode getPolicyNode(String policyKey) {
        return this.policyNodesMap.get(policyKey);
    }

    public Map<String, PolicyNode> getPolicyNodesMap() {
        return this.policyNodesMap;
    }


    public void addPolicyNode(PolicyNode node) {
        if (node.getMethodMatcher() instanceof SignatureMethodMatcher) {
            SignatureMethodMatcher methodMatcher = (SignatureMethodMatcher) node.getMethodMatcher();
            this.policyNodesMap.put(node.toString(), node);
            addHooks(methodMatcher.getSignature().getClassName(), node.getInheritable());
        }
    }

    public void addHooks(String className, Inheritable inheritable) {
        if (Inheritable.ALL.equals(inheritable) || Inheritable.SELF.equals(inheritable)) {
            this.classHooks.add(className);
        }
        if (Inheritable.ALL.equals(inheritable) || Inheritable.SUBCLASS.equals(inheritable)) {
            this.ancestorClassHooks.add(className);
        }
    }

    public String getMatchedClass(String className, Set<String> ancestors) {
        if (this.classHooks.contains(className)) {
            return className;
        }
//     for (String ancestor : ancestors) {
//       if (this.ancestorClassHooks.contains(ancestor)) {
//         return ancestor;
//       }
//     }// todo: 匹配父类继承的方法
        return null;
    }

    public boolean isMatchClass(String className) {
        return (this.classHooks.contains(className) || this.ancestorClassHooks.contains(className));
    }

    public Set<String> getClassHooks() {
        return this.classHooks;
    }

    public Set<String> getAncestorClassHooks() {
        return this.ancestorClassHooks;
    }
}
