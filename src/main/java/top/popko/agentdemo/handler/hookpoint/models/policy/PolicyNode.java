package top.popko.agentdemo.handler.hookpoint.models.policy;

public abstract class PolicyNode {
    private String hashString;
    private Inheritable inheritable;
    protected MethodMatcher methodMatcher;

    public PolicyNode(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public abstract PolicyNodeType getType();

    public Inheritable getInheritable() {
        return this.inheritable;
    }

    public void setInheritable(Inheritable inheritable) {
        this.inheritable = inheritable;
    }

    public MethodMatcher getMethodMatcher() {
        return this.methodMatcher;
    }


    public String toString() {
        if (this.hashString == null) {
            this.hashString = getType().getName() + "/" + this.methodMatcher.toString();
        }
        return this.hashString;
    }


    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() == obj.getClass()) {
            return toString().equals(obj.toString());
        }
        return false;
    }


    public int hashCode() {
        return toString().hashCode();
    }
}
