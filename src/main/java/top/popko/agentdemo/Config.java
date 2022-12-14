package top.popko.agentdemo;

import test1.lQ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Config {
    private HashMap<String, HashMap> blackClassMap = new HashMap();

    public Config() {
        String config = "blackClasses.txt";
        Scanner scanner = new Scanner(Config.class.getResourceAsStream("/" + config));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.equals("")) {
                String[] blackConfig = line.split("@@");
                HashMap<String, String> blackMethodMap = blackClassMap.get(blackConfig[0]);
                if (blackMethodMap == null) {
                    HashMap<String, String> tmpMap = new HashMap();
                    tmpMap.put(blackConfig[1], blackConfig[2]);
                    blackClassMap.put(blackConfig[0], tmpMap);
                } else {
                    blackMethodMap.put(blackConfig[1], blackConfig[2]);
                }
            }
        }
    }

    public HashMap<String, HashMap> getBlackClassMap() {
        return blackClassMap;
    }


    public static void main(String[] args) {

    }
    public static void main1(String[] args) {
//        System.loadLibrary("exp");
        System.out.println(lQ.RF);
        System.out.println(new java.util.Date().toString());
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis()/1000);
        Config config = new Config();
        Iterator it2 = config.getBlackClassMap().keySet().iterator();
        while (it2.hasNext()) {
            String key = (String) it2.next();
            System.out.println(config.getBlackClassMap().get(key));
        }
    }

}

