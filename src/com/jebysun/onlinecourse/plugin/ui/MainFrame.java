package com.jebysun.onlinecourse.plugin.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.jebysun.onlinecourse.plugin.parser.Config;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 7602027888917954442L;
	
	private ImagePanel imgNumCode;
	private ImagePanel imgABCCode;
	private JTextField txtUserName;
	private JTextField txtPassWord;
	private JTextField txtNumCode;
	private JTextField txtABCCode;
	private JButton btnLogin;
	
	private Map<String, String> cookiesMap;
	
	public MainFrame() {
		
		
		initView();
		
//		try {
//			HttpUtil.httpGet(Config.LOGIN_PAGE, null, null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		this.setTitle("登录");
		this.setBounds(100, 100, 600, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		
	}
	
	private void initView() {
		Response res = null;
		try {
			res = Jsoup.connect(Config.LOGIN_PAGE).timeout(30000).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cookiesMap = res.cookies();
        Set<Entry<String, String>> entrySet = cookiesMap.entrySet();
        String cookiesStr = "";
        for (Map.Entry<String, String> entry : entrySet) {  
            System.out.println(entry.getKey() + "=" + entry.getValue()); 
            cookiesStr = cookiesStr + entry.getKey() + "=" + entry.getValue()+";";
        }  
        cookiesStr = cookiesStr.substring(0, cookiesStr.length()-1);
		this.imgNumCode = new ImagePanel(200, 40, Config.NUM_CODE_PATH, cookiesStr);
		this.imgABCCode = new ImagePanel(200, 40, Config.ABC_CODE_PATH, cookiesStr);
		this.txtUserName = new JTextField("用户名");
		this.txtPassWord = new JTextField("密码");
		this.txtNumCode = new JTextField("数字验证码");
		this.txtABCCode = new JTextField("字母验证码");
		this.btnLogin = new JButton("登录");
		
		this.setLayout(null);
		this.add(this.imgNumCode);
		this.add(this.imgABCCode);
		this.add(this.txtUserName);
		this.add(this.txtPassWord);
		this.add(this.txtNumCode);
		this.add(this.txtABCCode);
		this.add(this.btnLogin);
		
		this.imgNumCode.setBounds(100, 80, 125, 40);
		this.imgABCCode.setBounds(235, 80, 200, 40);
		this.txtUserName.setBounds(100, 130, 120, 30);
		this.txtPassWord.setBounds(100, 170, 120, 30);
		this.txtNumCode.setBounds(100, 210, 120, 30);
		this.txtABCCode.setBounds(230, 210, 120, 30);
		
		this.btnLogin.setBounds(100, 250, 120, 30);
		this.btnLogin.addActionListener(this);
	}
	
	public void login(String numCode, String abcCode) {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("pid", "-1");
			paramMap.put("pidName", "");
			paramMap.put("fid", "1944");
			paramMap.put("fidName", "苏州科技学院");
			paramMap.put("allowJoin", "0");
			paramMap.put("isCheckNumCode", "1");
			paramMap.put("f", "0");
			/////////
			paramMap.put("uname", "32016");
			paramMap.put("password", "123456a");
			paramMap.put("numcode", numCode);
			paramMap.put("verCode", abcCode);
			paramMap.put("autoLogin", "true");
			
			Response response = Jsoup.connect(Config.LOGIN_ACTION)
					.cookies(this.cookiesMap)
					.data(paramMap)
					.method(Connection.Method.POST)
					.execute();
			
			this.cookiesMap = response.cookies();
			String htmlContent = response.body();
			System.out.println(response.statusCode());
			Document doc = Jsoup.parse(htmlContent);
			if ("苏州科技学院网络教学平台".equals(doc.title().trim())) {
				System.out.println("登录成功");
				tempRequest(Config.COURSE_PATH);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tempRequest(String strUrl) {
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(this.cookiesMap)
					.method(Connection.Method.GET)
					.execute();
			
			this.cookiesMap = response.cookies();
			int statusCode = response.statusCode();
			System.out.println(statusCode);
			if (statusCode == 200) {
				studentWorkRequest(Config.STUDENT_WORK_PATH);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void studentWorkRequest(String strUrl) {
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(this.cookiesMap)
					.method(Connection.Method.GET)
					.execute();
			
			this.cookiesMap = response.cookies();
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			System.out.println(statusCode);
			if (statusCode == 200) {
				System.out.println("学生作业列表");
				System.out.println(htmlContent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String numCode = this.txtNumCode.getText();
		String abcCode = this.txtABCCode.getText();
		login(numCode, abcCode);
	}

}
