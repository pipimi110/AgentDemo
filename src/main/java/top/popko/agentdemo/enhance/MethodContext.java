package top.popko.agentdemo.enhance;


import top.popko.agentdemo.handler.hookpoint.models.policy.Signature;

public class MethodContext {
    private ClassContext declaredClass;
    private String signature;
    private String matchedSignature;
    private String methodName;
    private String[] parameters;
    private int modifier;
    private String descriptor;

    public MethodContext(ClassContext declaredClass, String methodName) {
        this.declaredClass = declaredClass;
        this.methodName = methodName;
    }

    public String getInternalClassName() {
        return this.declaredClass.getInternalClassName();
    }

    public String getClassName() {
        return this.declaredClass.getClassName();
    }

    public String getMatchedClassName() {
        return this.declaredClass.getMatchedClassName();
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String[] getParameters() {
        return this.parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public ClassContext getDeclaredClass() {
        return this.declaredClass;
    }

    public void setDeclaredClass(ClassContext declaredClass) {
        this.declaredClass = declaredClass;
    }

    public int getModifier() {
        return this.modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public void updateSignature() {
        this.signature = Signature.normalizeSignature(this.getClassName(), this.methodName, this.parameters);
    }

    public void updateMatchedSignature() {
        this.matchedSignature = Signature.normalizeSignature(this.getMatchedClassName(), this.methodName, this.parameters);
    }

    public String getMatchedSignature() {
        if (this.matchedSignature == null) {
            this.updateMatchedSignature();
        }

        return this.matchedSignature;
    }

    public String toString() {
        if (this.signature == null) {
            this.updateSignature();
        }

        return this.signature;
    }
}
