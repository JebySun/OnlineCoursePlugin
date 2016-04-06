package com.jebysun.onlinecourse.plugin.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.jebysun.onlinecourse.plugin.JavaUtil;

public class WebAPIPicker {
	
	public void say() {
		System.out.println("HI");
	}
	
	
	public void http() {
		
	}
	
	public void htm() {
		try {
			Response res = Jsoup.connect(Config.LOGIN_PAGE).timeout(30000).execute();
			Map<String, String> cookiesMap = res.cookies();
			
			JavaUtil.printByEntrySet(cookiesMap);
			cookiesMap.clear();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("mooc", "1");
			paramMap.put("isdisplaytable", "2");
			paramMap.put("workId", "181277");
			paramMap.put("classId", "306870");
			paramMap.put("courseId", "81300811");
			/////////
//			paramMap.put("uname", "32016");
//			paramMap.put("password", "123456a");
//			paramMap.put("numcode", "8689");
//			paramMap.put("verCode", "");
//			paramMap.put("autoLogin", "true");
			
			cookiesMap.put("__dxca", "8bccc9b0-644f-4bce-9440-2236608ff5d4");
			cookiesMap.put("1944userinfo", "49338af9c419746cd186a727aa1cad290b491c76e8898ecce545bba8773d05e09d4d4e4d04b693ecfe57b46714d653091b0c84420a49077f");
			cookiesMap.put("1944UID", "23655012");
			cookiesMap.put("_uid", "23655012");
			cookiesMap.put("1944enc", "ED717B6FE16C39CFF970705A734F81A1");
			cookiesMap.put("uf", "0d7d2f765ae4287185608d81323f6b582d64313979f76033ec8c92ea1c87ac8cbdd6b93a43158491b20a1d9665cac53750cd2645158a80ac22576bc6e02f2035");
			cookiesMap.put("UID", "23655012");
			cookiesMap.put("vc", "ED717B6FE16C39CFF970705A734F81A1");
			cookiesMap.put("vc2", "5AB1CDE4D376EC8F13E4503C0DCCE6AF");
			cookiesMap.put("DSSTASH_LOG", "C_38-UN_274-US_23655012-T_1459854538263");
			cookiesMap.put("fanyamoocs", "B08E1A7F24E6D6DA11401F839C536D9E");
			cookiesMap.put("fid", "1944");
			cookiesMap.put("isfyportal", "1");
			cookiesMap.put("JSESSIONID", "F1B3862DA4D0BB6EFCC640130EBBDD05.mc_web2_136_8091");
			cookiesMap.put("route", "a973b43c6ad65cf8f2fa634e3f3e0e50");
			
			Document doc = Jsoup.connect("http://mooc1.usts.edu.cn/work/searchMarkList")
					  .data(paramMap)
					  .cookies(cookiesMap)
					  .timeout(3000)
					  .get();
			
			String html = doc.html();
			System.out.println(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
