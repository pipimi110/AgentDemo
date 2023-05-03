package top.popko.agentdemo.util;

public class PropertyUtils {
    private static PropertyUtils instance;

    public static PropertyUtils getInstance() {
        if (instance == null) {
            instance = new PropertyUtils();
        }
        return instance;
    }
    public String getBlackExtFilePath() {
        return System.getProperty("blackext");
    }
}
