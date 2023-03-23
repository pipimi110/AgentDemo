package top.popko.agentdemo.util;

//import io.dongtai.iast.core.utils.matcher.structure.ClassStructure;
//import io.dongtai.iast.core.utils.matcher.structure.ClassStructureFactory;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.Type;
import java.io.InputStream;

public class AsmUtils {
    public static Integer api = 589824;
    public static Type voidType;
    public static Type stringArrayType;

    public AsmUtils() {
    }

//    public static String getCommonSuperClass(String type1, String type2, ClassLoader loader) {
//        return getCommonSuperClassImplByAsm(type1, type2, loader);
//    }

//    private static String getCommonSuperClassImplByAsm(String type1, String type2, ClassLoader targetClassLoader) {
//        InputStream inputStreamOfType1 = null;
//        InputStream inputStreamOfType2 = null;
//
//        String var5;
//        try {
//            if (null == targetClassLoader) {
//                targetClassLoader = ClassLoader.getSystemClassLoader();
//            }
//
//            if (null == targetClassLoader) {
//                var5 = "java/lang/Object";
//                return var5;
//            }
//
//            inputStreamOfType1 = targetClassLoader.getResourceAsStream(type1 + ".class");
//            if (null != inputStreamOfType1) {
//                inputStreamOfType2 = targetClassLoader.getResourceAsStream(type2 + ".class");
//                if (null == inputStreamOfType2) {
//                    var5 = "java/lang/Object";
//                    return var5;
//                }
//
//                ClassStructure classStructureOfType1 = ClassStructureFactory.createClassStructure(inputStreamOfType1, targetClassLoader);
//                ClassStructure classStructureOfType2 = ClassStructureFactory.createClassStructure(inputStreamOfType2, targetClassLoader);
//                String var7;
//                if (classStructureOfType2.getFamilyTypeClassStructures().contains(classStructureOfType1)) {
//                    var7 = type1;
//                    return var7;
//                }
//
//                if (classStructureOfType1.getFamilyTypeClassStructures().contains(classStructureOfType2)) {
//                    var7 = type2;
//                    return var7;
//                }
//
//                if (!classStructureOfType1.getAccess().isInterface() && !classStructureOfType2.getAccess().isInterface()) {
//                    ClassStructure classStructure = classStructureOfType1;
//
//                    String var8;
//                    do {
//                        classStructure = classStructure.getSuperClassStructure();
//                        if (null == classStructure) {
//                            var8 = "java/lang/Object";
//                            return var8;
//                        }
//                    } while(!classStructureOfType2.getFamilyTypeClassStructures().contains(classStructure));
//
//                    var8 = SandboxStringUtils.toInternalClassName(classStructure.getJavaClassName());
//                    return var8;
//                }
//
//                var7 = "java/lang/Object";
//                return var7;
//            }
//
//            var5 = "java/lang/Object";
//        } finally {
//            IOUtils.closeQuietly(inputStreamOfType1);
//            IOUtils.closeQuietly(inputStreamOfType2);
//        }
//
//        return var5;
//    }

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
