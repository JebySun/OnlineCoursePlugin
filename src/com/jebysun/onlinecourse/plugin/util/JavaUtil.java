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
    
    /**
     * 去除数组尾部的空白
     * @param objArr
     * @return
     */
    public static Object[][] trimArrBlank(Object[][] objArr) {
    	int length = 0;
    	for (int i=0; i<objArr.length; i++) {
    		if (objArr[i][0] == null) {
    			length = i;
    			break;
    		}
    		length = objArr.length;
    	}
    	Object[][] desArr = new Object[length][objArr[0].length];
    	System.arraycopy(objArr, 0, desArr, 0, length);
    	return desArr;
    }
    
    /**
     * 获取url的指定参数值
     * @param url
     * @param key
     * @return
     */
    public static String getUrlParamValue(String url, String key) {
    	url = url.substring(url.indexOf("?")+1);
    	String[] paramArr = url.split("&");
    	for (String param : paramArr) {
    		String[] keyValueArr = param.split("=");
    		if (keyValueArr[0].equals(key)) {
    			return keyValueArr[1];
    		}
    	}
    	return null;
    }
    
}




