package top.popko.agentdemo.util;
import test1.DynamicEnumUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

//import com.alibaba.fastjson.parser.Dserializer.ObjectDeserializer;
//import com.alibaba.fastjson.parser.JSONLexer;
//import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;;
/**
 * 获取Java的方法签名，参考javah -jni 类路径/javap -s 类路径。
 * @author WuJianHua
 * @date 2017年9月5日 下午3:25:51
 * @url http://blog.csdn.net/earbao
 * https://blog.csdn.net/earbao/article/details/77852044
 */
public class ASMUtilsForJavaH {

	public static void main(String[] args) throws Exception {

		System.out.println(ASMUtilsForJavaH.getDesc(System.class));
		System.out.println(ASMUtilsForJavaH.getDesc(String.class));
		System.out.println(ASMUtilsForJavaH.getDesc(Integer.class));
		System.out.println(ASMUtilsForJavaH.getDesc(int.class));

		Method method=ASMUtilsForJavaH.class.getDeclaredMethod("main", String[].class);
		System.out.println("javah -jni");
		System.out.println(ASMUtilsForJavaH.getDesc(method));
//		(Ljava/lang/String;)Ljava/lang/String;
        method = DynamicEnumUtil.class.getDeclaredMethod("pritest1", String.class);
		System.out.println(ASMUtilsForJavaH.getDesc(method));
//		()Ljava/lang/String;
//        method = DynamicEnumUtil.class.getDeclaredMethod("pritest1", null);
		//(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        method = DynamicEnumUtil.class.getDeclaredMethod("pritest1", String.class,String.class);
		System.out.println(ASMUtilsForJavaH.getDesc(method));
		System.out.println(ASMUtilsForJavaH.getType(System.class));
		System.out.println(ASMUtilsForJavaH.getType(ASMUtilsForJavaH.class));
	}

	public static boolean isAndroid(final String vmName) {
		final String lowerVMName = vmName.toLowerCase();
		return lowerVMName.contains("dalvik") || lowerVMName.contains("lemur");
	}

	public static boolean isAndroid() {
		return isAndroid(System.getProperty("java.vm.name"));
	}

	public static String getDesc(final Method method) {
		final StringBuffer buf = new StringBuffer();
		buf.append("(");
		final Class<?>[] types = method.getParameterTypes();
		for (int i = 0; i < types.length; ++i) {
			buf.append(getDesc(types[i]));
		}
		buf.append(")");
		buf.append(getDesc(method.getReturnType()));
		return buf.toString();
	}

	public static String getDesc(final Class<?> returnType) {
		if (returnType.isPrimitive()) {
			return getPrimitiveLetter(returnType);
		}
		if (returnType.isArray()) {
			return "[" + getDesc(returnType.getComponentType());
		}
		return "L" + getType(returnType) + ";";
	}

	public static String getType(final Class<?> parameterType) {
		if (parameterType.isArray()) {
			return "[" + getDesc(parameterType.getComponentType());
		}
		if (!parameterType.isPrimitive()) {
			final String clsName = parameterType.getName();
			return clsName.replaceAll("\\.", "/");
		}
		return getPrimitiveLetter(parameterType);
	}

	public static String getPrimitiveLetter(final Class<?> type) {
		if (Integer.TYPE.equals(type)) {
			return "I";
		}
		if (Void.TYPE.equals(type)) {
			return "V";
		}
		if (Boolean.TYPE.equals(type)) {
			return "Z";
		}
		if (Character.TYPE.equals(type)) {
			return "C";
		}
		if (Byte.TYPE.equals(type)) {
			return "B";
		}
		if (Short.TYPE.equals(type)) {
			return "S";
		}
		if (Float.TYPE.equals(type)) {
			return "F";
		}
		if (Long.TYPE.equals(type)) {
			return "J";
		}
		if (Double.TYPE.equals(type)) {
			return "D";
		}
		throw new IllegalStateException("Type: " + type.getCanonicalName() + " is not a primitive type");
	}

	public static Type getMethodType(final Class<?> clazz, final String methodName) {
		try {
			final Method method = clazz.getMethod(methodName, (Class<?>[]) new Class[0]);
			return method.getGenericReturnType();
		} catch (Exception ex) {
			return null;
		}
	}

	public static Type getFieldType(final Class<?> clazz, final String fieldName) {
		try {
			final Field field = clazz.getField(fieldName);
			return field.getGenericType();
		} catch (Exception ex) {
			return null;
		}
	}

//	public static void parseArray(final Collection collection, final ObjectDeserializer deser,
//			final DefaultJSONParser parser, final Type type, final Object fieldName) {
//		final JSONLexer lexer = parser.getLexer();
//		if (lexer.token() == 8) {
//			lexer.nextToken(16);
//		}
//		parser.accept(14, 14);
//		int index = 0;
//		while (true) {
//			final Object item = deser.deserialze(parser, type, (Object) index);
//			collection.add(item);
//			++index;
//			if (lexer.token() != 16) {
//				break;
//			}
//			lexer.nextToken(14);
//		}
//		parser.accept(15, 16);
//	}
}