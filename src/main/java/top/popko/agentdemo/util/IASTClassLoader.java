package top.popko.agentdemo.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarFile;

public class IASTClassLoader extends URLClassLoader {
    private final String toString;

    public IASTClassLoader(String jarFilePath) throws MalformedURLException {
        super(new URL[]{new URL("file:" + jarFilePath)});
        this.toString = String.format("IastClassLoader[path=%s;]", jarFilePath);
    }

    public URL getResource(String name) {
        URL url = this.findResource(name);
        if (null != url) {
            return url;
        } else {
            url = super.getResource(name);
            return url;
        }
    }

    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL> urls = this.findResources(name);
        if (null != urls) {
            return urls;
        } else {
            urls = super.getResources(name);
            return urls;
        }
    }

    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = this.findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        } else if (!name.startsWith("top.popko.agentdemo")) {
            return super.loadClass(name, resolve);
        } else {
            try {
                Class<?> aClass = this.findClass(name);
                if (resolve) {
                    this.resolveClass(aClass);
                }

                return aClass;
            } catch (Throwable var5) {
                return super.loadClass(name, resolve);
            }
        }
    }

    public String toString() {
        return this.toString;
    }

//    public void closeIfPossible() {
//        if (this instanceof Closeable) {
//            try {
//                this.close();
//            } catch (Throwable var8) {
//            }
//
//        } else {
//            try {
//                Object sun_misc_URLClassPath = this.forceGetDeclaredFieldValue(URLClassLoader.class, "ucp", this);
//                Object java_util_Collection = this.forceGetDeclaredFieldValue(sun_misc_URLClassPath.getClass(), "loaders", sun_misc_URLClassPath);
//                Object[] var3 = ((Collection)java_util_Collection).toArray();
//                int var4 = var3.length;
//
//                for(int var5 = 0; var5 < var4; ++var5) {
//                    Object sun_misc_URLClassPath_JarLoader = var3[var5];
//
//                    try {
//                        JarFile java_util_jar_JarFile = (JarFile)this.forceGetDeclaredFieldValue(sun_misc_URLClassPath_JarLoader.getClass(), "jar", sun_misc_URLClassPath_JarLoader);
//                        java_util_jar_JarFile.close();
//                    } catch (Throwable var9) {
//                    }
//                }
//            } catch (Throwable var10) {
//            }
//
//        }
//    }

//    private <T> T forceGetDeclaredFieldValue(Class<?> clazz, String name, Object target) throws NoSuchFieldException, IllegalAccessException {
//        Field field = clazz.getDeclaredField(name);
//        field.setAccessible(true);
//        return field.get(target);
//    }
}
