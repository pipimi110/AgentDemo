package top.popko.agentdemo.handler.hookpoint.models.policy;

import top.popko.agentdemo.enhance.MethodContext;

import java.util.Arrays;

public class SignatureMethodMatcher
        implements MethodMatcher {
    private final Signature signature;

    public SignatureMethodMatcher(Signature signature) {
        this.signature = signature;
    }

    public boolean match(MethodContext method) {
        if (!this.signature.getClassName().equals(method.getMatchedClassName())) {
            return false;
        }
        if (!this.signature.getMethodName().equals(method.getMethodName())) {
            return false;
        }

        return Arrays.equals((Object[]) this.signature.getParameters(), (Object[]) method.getParameters());
    }

    public Signature getSignature() {
        return this.signature;
    }


    public String toString() {
        return getClass().getName() + "/" + this.signature.toString();
    }
}
