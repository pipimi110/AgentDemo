package top.popko.agentdemo.handler.hookpoint.models.policy;

import java.util.Set;

public abstract class TaintFlowNode extends PolicyNode {
    protected Set<TaintPosition> targets;
    protected Set<TaintPosition> sources;

    public TaintFlowNode(Set<TaintPosition> targets, MethodMatcher methodMatcher) {
        super(methodMatcher);
        this.targets = targets;
    }

    public Set<TaintPosition> getTargets() {
        return this.targets;
    }

    public void setTargets(Set<TaintPosition> targets) {
        this.targets = targets;
    }

    public Set<TaintPosition> getSources() {
        return this.sources;
    }

    public void setSources(Set<TaintPosition> sources) {
        this.sources = sources;
    }
}