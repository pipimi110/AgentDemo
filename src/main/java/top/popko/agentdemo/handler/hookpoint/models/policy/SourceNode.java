package top.popko.agentdemo.handler.hookpoint.models.policy;

import java.util.Set;

public class SourceNode extends TaintFlowNode {

    private String[] tags;

    public SourceNode(Set<TaintPosition> sources, Set<TaintPosition> targets, MethodMatcher methodMatcher) {
        super(targets, methodMatcher);
        this.sources = sources;
    }

    public PolicyNodeType getType() {
        return PolicyNodeType.SOURCE;
    }


    public String[] getTags() {
        return this.tags;
    }

    public boolean hasTags() {
        return (this.tags != null && this.tags.length > 0);
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
