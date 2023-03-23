package top.popko.agentdemo.enhance.plugin.spring;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.AbstractAdviceAdapter;
import top.popko.agentdemo.handler.hookpoint.controller.HookType;

public class SpringApplicationAdviceAdapter extends AbstractAdviceAdapter {
    public SpringApplicationAdviceAdapter(MethodVisitor mv, int access, String name, String desc, ClassContext context, String type, String signCode) {
        super(mv, access, name, desc, context, type, signCode);
    }

    protected void before() {
//        this.mark(this.tryLabel);
//        this.mark(this.catchLabel);
    }

    protected void after(int opcode) {
        if (!this.isThrow(opcode)) {
//            Label endLabel = new Label();
            this.captureMethodState(opcode, HookType.SPRINGAPPLICATION.getValue(), true);
//            this.mark(endLabel);
        }

    }

//    public void visitMaxs(int maxStack, int maxLocals) {
//        super.visitMaxs(maxStack, maxLocals);
//    }
}
