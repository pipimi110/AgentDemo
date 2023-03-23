package top.popko.agentdemo.handler.hookpoint.models.track;

import java.util.HashMap;
import java.util.HashSet;

public class TaintTrack {
    public static final TaintHashCodes TAINT_HASH_CODES = new TaintHashCodes();
    public static final TaintMap TRACK_MAP = new TaintMap();

    static {
        TRACK_MAP.set(new HashMap(1024));
        TAINT_HASH_CODES.set(new HashSet(2048));
    }

    public static void main(String[] args) {
        System.out.printf("123");
        new TaintTrack();
        HashMap hashMap = new HashMap();
        for(int i=0;i<20;i++){
            hashMap.put(i,"id"+i);
        }
        System.out.println("123");
    }
}
