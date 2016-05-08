package com.jebysun.onlinecourse.plugin.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.jebysun.onlinecourse.plugin.parser.Config;

/**
 * 登录面板
 * @author JebySun
 *
 */
public class LoginPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -2792638911561146803L;
	
	MainFrame frameContainer;
	CardLayout cardLayout;
	
	private JPanel containerPanel;
	
	private final int W = 600;
	private final int H = 400;
	
	private JLabel lablLoginTitle;
	private JLabel labluserName;
	private JLabel lablpassWord;
	private JLabel lablImgCode;
	
	
	private ImageCodePanel imgNumCode;
	private ImageCodePanel imgABCCode;
	private JTextField txtUserName;
	private JTextField txtPassWord;
	private JTextField txtNumCode;
	private JTextField txtABCCode;
	private JButton btnLogin;
	
	public LoginPanel(MainFrame frameContainer, CardLayout cardLayout) {
		this.frameContainer = frameContainer;
		this.cardLayout = cardLayout;
		containerPanel = new JPanel();
		
		containerPanel.setBackground(new Color(0xFFFFFF));
		containerPanel.setBorder(BorderFactory.createLineBorder(new Color(0x999999)));
		this.setLayout(null);
		containerPanel.setBounds((MainFrame.FRAME_WIDTH-W)/2, (MainFrame.FRAME_HEIGHT-H)/2, W, H);
		this.add(containerPanel);
		
		initView();
	}
	
	private void initView() {
		Response res = null;
		try {
			res = Jsoup.connect(Config.LOGIN_PAGE).timeout(30000).execute();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this, "无法链接到服务器", "网络故障", JOptionPane.ERROR_MESSAGE); 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainFrame.setCookiesMap(res.cookies());
        Set<Entry<String, String>> entrySet = MainFrame.getCookiesMap().entrySet();
        String cookiesStr = "";
        for (Map.Entry<String, String> entry : entrySet) {  
            cookiesStr = cookiesStr + entry.getKey() + "=" + entry.getValue()+";";
        }  
        cookiesStr = cookiesStr.substring(0, cookiesStr.length()-1);
		this.lablLoginTitle = new JLabel("苏州科技大学学生作业批改程序");
		this.lablLoginTitle.setFont((new Font(Font.SERIF, Font.BOLD, 22)));
		
		this.labluserName = new JLabel("帐    号");
		this.lablpassWord = new JLabel("密    码");
		this.lablImgCode = new JLabel("验证码");
		Font lablFont = new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT, 16);
		this.labluserName.setFont(lablFont);
		this.lablpassWord.setFont(lablFont);
		this.lablImgCode.setFont(lablFont);
		
		this.imgNumCode = new ImageCodePanel(Config.NUM_CODE_PATH, cookiesStr);
		this.imgABCCode = new ImageCodePanel(Config.ABC_CODE_PATH, cookiesStr);
		this.txtUserName = new JTextField("32016");
		this.txtPassWord = new JTextField("123456a");
		this.txtNumCode = new JTextField();
		this.txtABCCode = new JTextField();
		this.btnLogin = new JButton("登  录");
		
		containerPanel.setLayout(null);
		
		containerPanel.add(this.lablLoginTitle);
		containerPanel.add(this.labluserName);
		containerPanel.add(this.lablpassWord);
		containerPanel.add(this.lablImgCode);
		containerPanel.add(this.txtUserName);
		containerPanel.add(this.txtPassWord);
		containerPanel.add(this.txtNumCode);
		containerPanel.add(this.txtABCCode);
		containerPanel.add(this.imgNumCode);
		containerPanel.add(this.imgABCCode);
		containerPanel.add(this.btnLogin);
		
		
		this.lablLoginTitle.setBounds((W-340)/2, 10, 340, 40);
		
		this.labluserName.setBounds(60, 80, 100, 40);
		this.lablpassWord.setBounds(60, 130, 100, 40);
		this.lablImgCode.setBounds(60, 180, 100, 40);
		
		this.txtUserName.setBounds(120, 80, 140, 40);
		
		this.txtPassWord.setBounds(120, 130, 140, 40);
		
		this.txtNumCode.setBounds(120, 180, 140, 40);
		this.txtABCCode.setBounds(270, 180, 150, 40);
		
		this.imgNumCode.setBounds(120, 230, 300, 40);
		this.imgABCCode.setBounds(270, 230, 300, 40);
		
		this.btnLogin.setBounds(120, 300, 140, 40);
		
		
		this.imgABCCode.setVisible(false);
		this.txtABCCode.setVisible(false);
		
		this.btnLogin.addActionListener(this);
	}
	
	public void login(String userName, String passWord, String numCode, String abcCode) {
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
			
			Response response = Jsoup.connect(Config.LOGIN_ACTION)
					.cookies(MainFrame.getCookiesMap())
					.data(paramMap)
					.method(Connection.Method.POST)
					.execute();
			
			String htmlContent = response.body();
			System.out.println(htmlContent);
			Document doc = Jsoup.parse(htmlContent);
			if ("苏州科技学院网络教学平台".equals(doc.title().trim())) {
				MainFrame.setCookiesMap(response.cookies());
				//显示学生作业面板
				this.frameContainer.stuWorkListPanel.init();
				this.cardLayout.show(this.frameContainer.getContentPane(), "work_list");
			} else {
				this.imgNumCode.rePaintImage();
				this.imgABCCode.rePaintImage();
				String errMsg = doc.getElementById("show_error").text().trim();
				if (errMsg.indexOf("密码错误") != -1) {
					JOptionPane.showMessageDialog(this, "帐号或者密码错误", "登录失败", JOptionPane.ERROR_MESSAGE); 
				} else if (errMsg.indexOf("验证码错误") != -1) {
					JOptionPane.showMessageDialog(this, "验证码错误", "登录失败", JOptionPane.ERROR_MESSAGE); 
				}
				//需要输入英文验证码
				if (htmlContent.indexOf("$(\"#verCode_tr\").removeClass(\"zl_tr_td_hide\");") != -1) {
					this.imgABCCode.setVisible(true);
					this.txtABCCode.setVisible(true);
					System.out.println("需要增加输入字母验证码");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String userName = this.txtUserName.getText();
		String passWord = this.txtPassWord.getText();
		String numCode = this.txtNumCode.getText();
		String abcCode = this.txtABCCode.getText();
		login(userName, passWord, numCode, abcCode);
	}
	
}
