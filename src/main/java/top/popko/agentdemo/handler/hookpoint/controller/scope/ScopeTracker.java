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

//    public GeneralScope getHttpEntryScope() {
//        return ((ScopeAggregator)this.get()).getHttpEntryScope();
//    }
//
//    public boolean inEnterEntry() {
//        return ((ScopeAggregator)this.get()).getHttpEntryScope().in();
//    }
//
//    public PolicyScope getPolicyScope() {
//        return ((ScopeAggregator)this.get()).getPolicyScope();
//    }

//    public boolean inAgent() {
//        return ((ScopeAggregator)this.get()).getPolicyScope().inAgent();
//    }
}
