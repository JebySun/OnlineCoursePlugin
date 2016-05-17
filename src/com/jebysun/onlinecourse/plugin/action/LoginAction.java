package com.jebysun.onlinecourse.plugin.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.jebysun.onlinecourse.plugin.ApplicationContext;

public class LoginAction {
	
	private static LoginActionListener loginListener;
	
	
	public static void login(String userName, String passWord, String numCode, String abcCode, LoginActionListener loginListener) {
		LoginAction.loginListener = loginListener;
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("pid", "-1");
			paramMap.put("pidName", "");
			paramMap.put("fid", "1944");
			paramMap.put("fidName", "苏州科技学院");
			paramMap.put("allowJoin", "0");
			paramMap.put("isCheckNumCode", "1");
			paramMap.put("f", "0");
			paramMap.put("uname", userName);
			paramMap.put("password", passWord);
			paramMap.put("numcode", numCode);
			paramMap.put("verCode", abcCode);
			paramMap.put("autoLogin", "true");
			
			Response response = Jsoup.connect(ApplicationContext.LOGIN_ACTION)
					.cookies(ApplicationContext.getCookiesMap())
					.data(paramMap)
					.method(Connection.Method.POST)
					.execute();
			
			String htmlContent = response.body();
			Document doc = Jsoup.parse(htmlContent);
			if ("苏州科技学院网络教学平台".equals(doc.title().trim())) {
				ApplicationContext.setCookiesMap(response.cookies());
				LoginAction.loginListener.success();
			} else {
				String errMsg = doc.getElementById("show_error").text().trim();
				if (errMsg.indexOf("密码错误") != -1) {
					LoginAction.loginListener.failed("密码错误");
				} else if (errMsg.indexOf("验证码错误") != -1) {
					LoginAction.loginListener.failed("验证码错误");
				}
				LoginAction.loginListener.refreshImageCode();
				//是否需要输入英文验证码
				if (htmlContent.indexOf("$(\"#verCode_tr\").removeClass(\"zl_tr_td_hide\");") != -1) {
					LoginAction.loginListener.showABCImageCode();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public interface LoginActionListener {
		public void success();
		public void failed(String errType);
		public void refreshImageCode();
		public void showABCImageCode();
	}
	


}





