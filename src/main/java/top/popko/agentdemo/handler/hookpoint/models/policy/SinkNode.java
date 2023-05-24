package top.popko.agentdemo.handler.hookpoint.models.policy;

import java.util.Set;

public class SinkNode extends TaintFlowNode {
    private String vulType;
    private String[] stackDenyList;

    public SinkNode(Set<TaintPosition> sources, MethodMatcher methodMatcher) {
        super(null, methodMatcher);
        this.sources = sources;
    }

    public PolicyNodeType getType() {
        return PolicyNodeType.SINK;
    }

    public String getVulType() {
        return this.vulType;
    }

    public void setVulType(String vulType) {
        this.vulType = vulType;
    }

    public boolean hasDenyStack(StackTraceElement[] stackTraceElements) {
        if (this.stackDenyList == null || this.stackDenyList.length == 0) {
            return false;
        }

        int stackSize = stackTraceElements.length;
        boolean multi = (this.stackDenyList.length > 1);
        String[] cachedStack = null;
        if (multi) {
            cachedStack = new String[stackSize];
        }
        for (int i = 0; i < this.stackDenyList.length; i++) {
            for (int j = 0; j < stackSize; j++) {
                String stack;
                if (multi && i == 0) {
                    stack = stackTraceElements[j].toString();
                    cachedStack[j] = stack;
                } else if (multi) {
                    stack = cachedStack[j];
                } else {
                    stack = stackTraceElements[j].toString();
                }
                if (stack.contains(this.stackDenyList[i])) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setStackDenyList(String[] stackDenyList) {
        this.stackDenyList = stackDenyList;
    }
}
