package test1;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 动态新增枚举工具类
 */
public class DynamicEnumUtil {
    private static String pritest1(){
        System.out.println("pritest1 ing");
        return "pritest1";
    }
    private static String pritest1(String arg,String arg2){
        return "qwe";
    }
    private static String pritest1(String arg){
        System.out.println("pritest1 ing");
        return arg+" pritest1";
    }
    private static String pritest(){
        System.out.println("pritest ing");
        return "pritest";
    }

    public static void main(String[] args) throws Exception{
        System.out.println(new java.util.Date());
        System.out.println(DynamicEnumUtil.pritest());
        System.out.println(DynamicEnumUtil.pritest1());
        //getDeclaredMethod(methodName)默认获取无参，没有无参获取其他，多态时不能指定
        //getDeclaredMethod(String name, CtClass[] params)
        //public CtMethod getMethod(String name, String desc)通过desc参数指定多态
        System.out.println(DynamicEnumUtil.pritest1("arg1"));
    }
    public static void main11(String[] args) throws Exception{
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(lQ.RF.getClass().getName());
        ctClass.getField("yO");

//            String fieldType = newFieldMap.get(fieldName).getClass().getName();
            // 创建新字段
            CtField ctField = new CtField(ctClass, "y0", ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);

    }

    public static void main1(String[] args) {
        addlQEnum("Ql", 3, "change success");
        addlQEnum("yO", 3, "change success");

        for (lQ testEnum : lQ.values()) {
            System.out.println(testEnum.name() + "|" + testEnum.toString());
        }

    }

    private static void addlQEnum(String enumName, int code, String name) {
        DynamicEnumUtil.addEnum(lQ.class, enumName, new Class<?>[]
                        {int.class, String.class},
                new Object[]{code, name});
    }

    private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

    private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException,
            IllegalAccessException {

        // let's make the field accessible
        field.setAccessible(true);

        // next we change the modifier in the Field instance to
        // not be final anymore, thus tricking reflection into
        // letting us modify the static final field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);

        // blank out the final bit in the modifiers int
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);

        FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
        fa.set(target, value);
    }

    private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                AccessibleObject.setAccessible(new Field[]{field}, true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }

    private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
        blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
        blankField(enumClass, "enumConstants"); // IBM JDK
    }

    private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
            throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes,
                                   Object[] additionalValues) throws Exception {
        Object[] parms = new Object[additionalValues.length + 2];
        parms[0] = value;
        parms[1] = Integer.valueOf(ordinal);
        System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
        return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
    }

    /**
     * 判断枚举是否已存在
     *
     * @param values
     * @param enumName
     * @param <T>
     * @return
     */
    public static <T extends Enum<?>> boolean contains(List<T> values, String enumName, T newValue) {
        int i = 0;
        for (T value : values) {
            if (value.name().equals(enumName)) {
                values.set(i, newValue);//修改
                return true;
            }
            i += 1;
        }
        return false;
    }


    /**
     * Add an enum instance to the enum class given as argument
     *
     * @param <T>      the type of the enum (implicit)
     * @param enumType the class of the enum to be modified
     * @param enumName the name of the new enum instance to be added to the class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void addEnum(Class<T> enumType, String enumName, Class<?>[] additionalTypes, Object[] additionalValues) {

        // 0. Sanity checks
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException("class " + enumType + " is not an instance of Enum");
        }

        // 1. Lookup "$VALUES" holder in enum class and get previous enum instances
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        }
        AccessibleObject.setAccessible(new Field[]{valuesField}, true);

        try {

            // 2. Copy it
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));

            // 3. build new enum
            T newValue = (T) makeEnum(enumType, enumName, values.size(), additionalTypes, additionalValues);

            if (contains(values, enumName, newValue)) {
                System.out.println("Enum：" + enumName + " 已存在");
//                return;
            } else {
                // 4. add new value
                values.add(newValue);

            }
            // 5. Set new values field
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 6. Clean enum cache
            cleanEnumCache(enumType);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}