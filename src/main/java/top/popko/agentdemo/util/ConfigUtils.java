package top.popko.agentdemo.util;

import java.io.*;

public class ConfigUtils {
    public static InputStream getResourceAsStreamFromFilename(String filename) {
//        return ConfigUtils.class.getClassLoader().getResourceAsStream(filename);
        InputStream is;
        try {
            if (System.getProperty("configpath") != null) {
                filename = System.getProperty("configpath") + File.separator + filename;
            } else {
                filename = ConfigUtils.class.getResource("/" + filename).getPath();
            }
            is = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return is;
    }
}
