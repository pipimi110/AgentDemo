package top.popko.agentdemo.enhance;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.objectweb.asm.ClassReader;
public class IastClassDiagram {
    private final Map<String, Set<String>> diagrams = new ConcurrentHashMap();
    private static final Map<String, List<String>> DEFAULT_INTERFACE_LIST_MAP = new HashMap();
    private static final String BASE_CLASS = "java/lang/Object";
    private ClassLoader loader;
    private static IastClassDiagram instance;

    public synchronized void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public static IastClassDiagram getInstance() {
        if (instance == null) {
            instance = new IastClassDiagram();
        }

        return instance;
    }

    public Set<String> getDiagram(String className) {
        return (Set)this.diagrams.get(className);
    }

    public void setDiagram(String className, Set<String> diagram) {
        this.diagrams.put(className, diagram);
    }

    private IastClassDiagram() {
    }

    public synchronized void saveAncestors(String className, String superName, String[] interfaces) {
        Set<String> ancestorSet = this.diagrams.get(className);
        if (ancestorSet == null){
            ancestorSet = new HashSet();
        }
        ancestorSet.add(className);
        if (!"java/lang/Object".equals(superName)) {
            ancestorSet.add(superName.replace("/", "."));
        }

        String[] var5 = interfaces;
        int var6 = interfaces.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String interfaceClazzName = var5[var7];
            ((Set)ancestorSet).add(interfaceClazzName.replace("/", "."));
        }

        this.diagrams.put(className, ancestorSet);
    }

    public synchronized Set<String> getAncestors(String className, String superClassName, String[] interfaces) {
        Set<String> ancestors = (Set)this.diagrams.get(className);
        if (!isNullOrEmpty(superClassName) && !"java/lang/Object".equals(superClassName)) {
            this.addClassToAncestor(superClassName, ancestors);
        }

        List<String> interfaceList = (List)DEFAULT_INTERFACE_LIST_MAP.get(className);
        if (interfaceList != null) {
            ancestors.addAll(interfaceList);
        }

        String[] var6 = interfaces;
        int var7 = interfaces.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String anInterface = var6[var8];
            this.addClassToAncestor(anInterface, ancestors);
        }

        return ancestors;
    }

    private void addClassToAncestor(String className, Set<String> ancestors) {
        Set<String> set = (Set)this.diagrams.get(className.replace("/", "."));
        String interfaceName;
        Iterator var11;
        if (null != set) {
            Iterator var4 = set.iterator();

            while(var4.hasNext()) {
                String subClassName = (String)var4.next();
                if (!ancestors.contains(subClassName)) {
                    ancestors.add(subClassName);
                    this.addClassToAncestor(subClassName.replace(".", "/"), ancestors);
                }
            }
        } else {
            Set<String> tempClassFamily = new HashSet();
            this.scanJarForAncestor(className, tempClassFamily);
            if (!tempClassFamily.isEmpty()) {
                var11 = tempClassFamily.iterator();

                while(var11.hasNext()) {
                    interfaceName = (String)var11.next();
                    ancestors.add(interfaceName);
                    List<String> tempDefaultMap = (List)DEFAULT_INTERFACE_LIST_MAP.get(interfaceName);
                    if (tempDefaultMap != null) {
                        ancestors.addAll(tempDefaultMap);
                    }

                    Set<String> tempClassMap = (Set)this.diagrams.get(interfaceName);
                    if (tempClassMap != null) {
                        ancestors.addAll(tempClassMap);
                    }
                }

                this.diagrams.put(className.replace("/", "."), tempClassFamily);
            }
        }

        List<String> list = (List)DEFAULT_INTERFACE_LIST_MAP.get(className);
        if (null != list) {
            var11 = list.iterator();

            while(var11.hasNext()) {
                interfaceName = (String)var11.next();
                if (!ancestors.contains(className)) {
                    ancestors.add(interfaceName);
                    this.addClassToAncestor(interfaceName, ancestors);
                }
            }
        }

    }

    private void scanJarForAncestor(String className, Set<String> ancestors) {
        if (null == this.loader) {
            this.loader = this.getClass().getClassLoader();
        }

        if (null != this.loader) {
            Queue<String> queue = new LinkedList();
            queue.offer(className);

            while(!queue.isEmpty()) {
                String currentClass = (String)queue.poll();

                try {
                    InputStream inputStream = this.loader.getResourceAsStream(currentClass + ".class");
                    if (inputStream != null) {
                        ClassReader cr = new ClassReader(inputStream);
                        inputStream.close();
                        String[] interfaces = cr.getInterfaces();
                        String superclass = cr.getSuperName();
                        if (!"java/lang/Object".equals(superclass) && null != superclass) {
                            ancestors.add(superclass.replace("/", "."));
                            queue.offer(superclass);
                        }

                        String[] var9 = interfaces;
                        int var10 = interfaces.length;

                        for(int var11 = 0; var11 < var10; ++var11) {
                            String tempInterface = var9[var11];
                            ancestors.add(tempInterface.replace("/", "."));
                            queue.offer(tempInterface);
                        }
                    }
                } catch (Throwable var13) {
//                    DongTaiLog.error(var13);
                }
            }

        }
    }

    public static boolean isNullOrEmpty(String className) {
        return null == className || className.isEmpty();
    }

    public static Set<String> getFamilyFromClass(String className) {
        return instance == null ? null : (Set)instance.diagrams.get(className);
    }

    static {
        DEFAULT_INTERFACE_LIST_MAP.put(" org.apache.jasper.runtime.HttpJspBase".substring(1), Collections.singletonList(" javax.servlet.jsp.JspPage".substring(1)));
        DEFAULT_INTERFACE_LIST_MAP.put(" javax.servlet.http.HttpServletResponse".substring(1), Collections.singletonList(" javax.servlet.ServletResponse".substring(1)));
        DEFAULT_INTERFACE_LIST_MAP.put(" javax.servlet.http.HttpServletRequest".substring(1), Collections.singletonList(" javax.servlet.ServletRequest".substring(1)));
        DEFAULT_INTERFACE_LIST_MAP.put(" weblogic.servlet.internal.ServletRequestImpl".substring(1), Collections.singletonList(" javax.servlet.ServletRequest".substring(1)));
        DEFAULT_INTERFACE_LIST_MAP.put(" weblogic.servlet.jsp.JspBase".substring(1), Collections.singletonList(" javax.servlet.http.HttpServlet".substring(1)));
        DEFAULT_INTERFACE_LIST_MAP.put(" com.mysql.jdbc.Statement".substring(1), Collections.singletonList(" java.sql.Statement".substring(1)));
    }
}
