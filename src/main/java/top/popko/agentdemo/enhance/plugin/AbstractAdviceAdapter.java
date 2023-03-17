package top.popko.agentdemo.enhance.plugin;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import top.popko.agentdemo.enhance.asm.AsmMethods;
import top.popko.agentdemo.enhance.asm.AsmTypes;

import java.lang.reflect.Modifier;

public abstract class AbstractAdviceAdapter extends AdviceAdapter implements AsmTypes, AsmMethods {
    protected String name;
    protected String desc;
    protected int access;
    protected Label tryLabel;
    protected Label catchLabel;
//    protected ClassContext classContext;
//    protected MethodContext context;
    protected String type;
    protected String signature;
    protected Type returnType;
    protected boolean hasException;

//    public AbstractAdviceAdapter(MethodVisitor mv, int access, String name, String desc, ClassContext context, String type, String signCode) {
    public AbstractAdviceAdapter(MethodVisitor mv, int access, String name, String desc, String type, String signCode) {
        super(Opcodes.ASM5, mv, access, name, desc);
        this.access = access;
        this.name = name;
        this.desc = desc;
//        this.classContext = context;
        this.returnType = Type.getReturnType(desc);
        this.tryLabel = new Label();
        this.catchLabel = new Label();
        this.type = type;
        this.signature = signCode;
        this.hasException = false;
    }

//    public AbstractAdviceAdapter(MethodVisitor mv, int access, String name, String descriptor, String signature, MethodContext context) {
    public AbstractAdviceAdapter(MethodVisitor mv, int access, String name, String descriptor, String signature) {
        super(Opcodes.ASM5, mv, access, name, descriptor);
        this.access = access;
        this.name = name;
        this.desc = descriptor;
        this.signature = signature;
//        this.context = context;
        this.returnType = Type.getReturnType(descriptor);
        this.hasException = false;
    }

    protected void onMethodEnter() {
        this.before();
    }

    protected void onMethodExit(int opcode) {
        if (!this.isThrow(opcode)) {
            this.after(opcode);
        }

    }

    protected abstract void before();

    protected abstract void after(int var1);

    protected void loadThisOrPushNullIfIsStatic() {
        if (Modifier.isStatic(this.access)) {
            this.push((Type)null);
        } else {
            this.loadThis();
        }

    }

//    public void visitMaxs(int maxStack, int maxLocals) {
//        this.mark(this.catchLabel);
//        this.visitTryCatchBlock(this.tryLabel, this.catchLabel, this.mark(), ASM_TYPE_THROWABLE.getInternalName());
//        this.after(191);
//        this.throwException();
//        super.visitMaxs(maxStack, maxLocals);
//    }

    public void visitMaxsNew(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
    }

//    public void trackMethod(int opcode, PolicyNode policyNode, boolean captureRet) {
    public void trackMethod(int opcode,  boolean captureRet) {
        this.newLocal(ASM_TYPE_OBJECT);
        if (captureRet && !this.isThrow(opcode)) {
            this.loadReturn(opcode);
        } else {
            this.pushNull();
        }

        this.storeLocal(this.nextLocal - 1);
        this.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        // collectMethod(Object instance, Object[] parameters, Object retObject,
        //  String policyKey, String className, String matchedClassName, String methodName,
        //  String signature, boolean isStatic)
        this.loadThisOrPushNullIfIsStatic();//instance
        this.loadArgArray();//parameters
        this.loadLocal(this.nextLocal - 1);//retobject
//        this.push(policyNode.toString());//String policyKey
        this.push("test policy");//String policyKey
//        this.push(this.context.getClassName());//String className
        this.push("test classname");//String className
//        this.push(this.context.getMatchedClassName());//String matchedClassName
        this.push("test matchedclassname");//String matchedClassName
        this.push(this.name);//String methodName
        this.push(this.signature);//String signature
        this.push(Modifier.isStatic(this.access));//boolean isStatic
//        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$collectMethod);
        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPYIMPL$collectMethod);
        this.pop();
    }

    public void captureMethodState(int opcode, int hookValue, boolean captureRet) {
//        this.newLocal(ASM_TYPE_OBJECT);
//        if (captureRet && !this.isThrow(opcode)) {
//            this.loadReturn(opcode);
//        } else {
//            this.pushNull();
//        }
//
//        this.storeLocal(this.nextLocal - 1);
//        this.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
//        this.loadThisOrPushNullIfIsStatic();
//        this.loadArgArray();
//        this.loadLocal(this.nextLocal - 1);
//        this.push(this.type);
//        this.push(this.classContext.getClassName());
//        this.push(this.classContext.getMatchedClassName());
//        this.push(this.name);
//        this.push(this.signature);
//        this.push(Modifier.isStatic(this.access));
//        this.push(hookValue);
//        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$collectMethodPool);
//        this.pop();
    }

    protected boolean isThrow(int opcode) {
        return opcode == 191;
    }

    protected void loadReturn(int opcode) {
        switch (opcode) {
            case 173:
            case 175:
                this.dup2();
                this.box(Type.getReturnType(this.methodDesc));
                break;
            case 174:
            default:
                this.dup();
                this.box(Type.getReturnType(this.methodDesc));
                break;
            case 176:
                this.dup();
                break;
            case 177:
                this.pushNull();
        }

    }

    protected final void pushNull() {
        this.push((Type)null);
    }
}
