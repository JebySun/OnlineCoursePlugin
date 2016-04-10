package com.jebysun.onlinecourse.plugin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpUtil {
	
	public static String httpGet(String strUrl, String header, String content) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);// 允许连接提交信息
		connection.setRequestMethod("GET");// 网页提交方式
		if (header != null) {
			connection.setRequestProperty("Cookie", header);
		}
		if (content != null) {
			OutputStream os = connection.getOutputStream();
			os.write(content.toString().getBytes("UTF-8"));
			os.close();
		}
		Map<String, List<String>> keymap = connection.getHeaderFields();
		List<String> listCookie = keymap.get("Set-Cookie");
		for (String s : listCookie) {
			System.out.println(s);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		System.out.println("responseContent=");
		while ((line = br.readLine()) != null) {
		    System.out.println(line);
		}
		
		return "";
	}
	
	public static String httpPost(String strUrl, String header, String content) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);// 允许连接提交信息
		connection.setRequestMethod("POST");// 网页提交方式
		if (header!=null) {
			connection.setRequestProperty("Cookie", header);
		}
		OutputStream os = connection.getOutputStream();
		os.write(content.toString().getBytes("UTF-8"));
		os.close();
		
		String responseCookie = connection.getHeaderField("Set-Cookie");
		System.out.println("responseCookie=");
		System.out.println(responseCookie);
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		System.out.println("responseContent=");
		while ((line = br.readLine()) != null) {
		    System.out.println(line);
		}
		
		return "";
	}
	
}
