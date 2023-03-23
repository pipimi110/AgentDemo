package top.popko.agentdemo.util;

public class HashCode {
    public HashCode() {
    }

    public static int calc(Object obj) {
        return obj instanceof String ? obj.hashCode() : System.identityHashCode(obj);
    }

    public static void main(String[] args) {
        byte aa = 5;
        byte bb = 'c';
        byte cc = 5;//不是对象
//        new Byte(aa)//是对象
        char[] aaa = {'a','b','c'};//java中的数组也是对象
        char[] bbb = {'a','b','c'};
        String a = "qwe";
        String b = "qwe";
        String c = new String("qwe".getBytes());
        System.out.println(calc(a));//String.hashCode()
        System.out.println(System.identityHashCode(a));//Object.hashCode()和String结果不同
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(c));//char[]对象不一致导致结果不同
        System.out.println(calc(c));//结果相同//String.hashCode()算法与字符有关 与对象无关
    }
}
