package top.popko.agentdemo.handler.hookpoint.controller.scope;

public class ScopeAggregator {
    private final GeneralScope httpRequestScope = new GeneralScope();
//    private final GeneralScope httpEntryScope = new GeneralScope();
//    private final PolicyScope policyScope = new PolicyScope();

    public ScopeAggregator() {
    }

    public GeneralScope getHttpRequestScope() {
        return this.httpRequestScope;
    }

//    public GeneralScope getHttpEntryScope() {
//        return this.httpEntryScope;
//    }

//    public PolicyScope getPolicyScope() {
//        return this.policyScope;
//    }
}
