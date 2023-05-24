package top.popko.agentdemo.util;

import org.objectweb.asm.Type;

public class AsmUtils {
    public static Integer api = 589824;
    public static Type voidType;
    public static Type stringArrayType;

    public AsmUtils() {
    }

    public static String[] buildParameterTypes(String desc) {
        Type[] argTypes = Type.getArgumentTypes(desc);
        if (argTypes.length == 0) {
            return new String[0];
        } else {
            String[] args = new String[argTypes.length];

            for(byte index = 0; index < argTypes.length; ++index) {
                args[index] = argTypes[index].getClassName();
            }

            return args;
        }
    }

    public static String buildSignature(String className, String methodName, String desc) {
        Type[] argTypes = Type.getArgumentTypes(desc);
        StringBuilder sb = new StringBuilder();
        sb.append(className);
        sb.append(".");
        sb.append(methodName);
        sb.append("(");

        for(byte index = 0; index < argTypes.length; ++index) {
            sb.append(argTypes[index].getClassName());
            if (index != argTypes.length - 1) {
                sb.append(",");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    static {
        voidType = Type.getType(Void.TYPE);
        stringArrayType = Type.getType(String[].class);
    }
}
