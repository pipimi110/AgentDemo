package top.popko.agentdemo.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;

public class ConfigUtils {
    public static InputStream getResourceAsStreamFromFilename(String filename) {
//        return ConfigUtils.class.getClassLoader().getResourceAsStream(filename);
        InputStream is;
        try {
            if(System.getProperty("configpath")!=null){
                filename = System.getProperty("configpath")+File.separator+filename;
            }else{
                filename = ConfigUtils.class.getResource("/"+filename).getPath();
            }
            is = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return is;
    }
    public static String[] loadExtConfigFromFile(String filename) {
        InputStream fis = null;
        String[] extStringArray = null;

//        try {
            fis = getResourceAsStreamFromFilename(filename);

            String exts;
            for(LineIterator lineIterator = IOUtils.lineIterator(fis, (String)null); lineIterator.hasNext(); extStringArray = exts.split(",")) {
                exts = lineIterator.nextLine().trim();
            }
//        } catch (IOException var5) {
//            DongTaiLog.error("读取后缀配置文件：{} 失败，错误信息：{}", new Object[]{filename, var5});
//            DongTaiLog.error(var5);

//        }

        return extStringArray;
    }
}
