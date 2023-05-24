package top.popko.agentdemo.handler.hookpoint.controller.scope;

public class ScopeTracker extends ThreadLocal<ScopeAggregator> {
    public ScopeTracker() {
    }

    protected ScopeAggregator initialValue() {
        return new ScopeAggregator();
    }

    public GeneralScope getHttpRequestScope() {
        return this.get().getHttpRequestScope();
    }
}
