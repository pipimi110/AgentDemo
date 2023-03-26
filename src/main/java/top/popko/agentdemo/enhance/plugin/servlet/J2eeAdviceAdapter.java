package top.popko.agentdemo.enhance.plugin.servlet;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.AbstractAdviceAdapter;
import top.popko.agentdemo.handler.hookpoint.controller.HookType;

public class J2eeAdviceAdapter extends AbstractAdviceAdapter {
    boolean isJakarta;

    public J2eeAdviceAdapter(MethodVisitor mv, int access, String name, String desc, String signature, ClassContext context, boolean isJakarta) {
        super(mv, access, name, desc, context, "j2ee", signature);
        this.isJakarta = isJakarta;
    }

    protected void before() {
//        this.mark(this.tryLabel);
//        Label elseLabel = new Label();
        this.enterHttp();
//        this.isFirstLevelHttp();
//        this.mv.visitJumpInsn(153, elseLabel);
        this.cloneHttpServletRequest();
        this.captureMethodState(-1, HookType.HTTP.getValue(), false);
        this.cloneHttpServletResponse();
//        this.mark(elseLabel);
    }

    protected void after(int opcode) {
        this.leaveHttp();
    }

    private void enterHttp() {
        this.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$enterHttp);
    }

    private void leaveHttp() {
        this.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        this.loadArg(0);
        this.loadArg(1);
        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$leaveHttp);
    }

    private void isFirstLevelHttp() {
        this.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$isFirstLevelHttp);
    }

    protected void cloneHttpServletRequest() {
        this.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        this.loadArg(0);
        this.push(this.isJakarta);
        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$cloneRequest);
        this.storeArg(0);
    }

    protected void cloneHttpServletResponse() {
        this.invokeStatic(ASM_TYPE_SPY_HANDLER, SPY_HANDLER$getDispatcher);
        this.loadArg(1);
        this.push(this.isJakarta);
        this.invokeInterface(ASM_TYPE_SPY_DISPATCHER, SPY$cloneResponse);
        this.storeArg(1);
    }
}
