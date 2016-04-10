package com.jebysun.onlinecourse.plugin.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JavaUtil {
    /**
     * 遍历打印Map
     * @param map
     */
    public static void printByEntrySet(Map<String, String> map) {
        Set<Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {  
            System.out.println(entry.getKey() + "=" + entry.getValue());  
        }  
    }
}
