package com.jebysun.onlinecourse.plugin.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import com.jebysun.onlinecourse.plugin.ApplicationContext;
import com.jebysun.onlinecourse.plugin.action.LoginAction;
import com.jebysun.onlinecourse.plugin.action.LoginAction.LoginActionListener;

/**
 * 登录窗口
 * @author JebySun
 *
 */
public class LoginFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = -2792638911561146803L;
	
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
	private JPasswordField txtPassWord;
	private JTextField txtNumCode;
	private JTextField txtABCCode;
	private JButton btnLogin;
	
	public LoginFrame() {
		containerPanel = new JPanel();
		
		containerPanel.setBackground(new Color(0xFFFFFF));
		containerPanel.setBorder(BorderFactory.createLineBorder(new Color(0x999999)));
		this.setLayout(null);
		containerPanel.setBounds((ApplicationContext.FRAME_WIDTH-W)/2, (ApplicationContext.FRAME_HEIGHT-H)/2, W, H);
		this.add(containerPanel);
		
		initCookie();
		initView();
	}
	
	private void initCookie() {
		Response res = null;
		try {
			res = Jsoup.connect(ApplicationContext.LOGIN_PAGE).timeout(5*1000).execute();
			ApplicationContext.setCookiesMap(res.cookies());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "无法链接到服务器", "网络故障", JOptionPane.ERROR_MESSAGE); 
			e.printStackTrace();
		}
	}
	
	private void initView() {
		this.lablLoginTitle = new JLabel("苏州科技大学学生作业批改程序");
		this.lablLoginTitle.setFont((new Font(Font.SERIF, Font.BOLD, 22)));
		
		this.labluserName = new JLabel("帐    号");
		this.lablpassWord = new JLabel("密    码");
		this.lablImgCode = new JLabel("验证码");
		Font lablFont = new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT, 16);
		this.labluserName.setFont(lablFont);
		this.lablpassWord.setFont(lablFont);
		this.lablImgCode.setFont(lablFont);
		
		this.imgNumCode = new ImageCodePanel(ApplicationContext.NUM_CODE_PATH, ApplicationContext.getCookiesMap());
		this.imgABCCode = new ImageCodePanel(ApplicationContext.ABC_CODE_PATH, ApplicationContext.getCookiesMap());
		this.txtUserName = new JTextField("32016");
		this.txtPassWord = new JPasswordField();
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

		this.setTitle(ApplicationContext.FRAME_TITLE+"v"+ApplicationContext.VERSION);
		this.setBounds(0, 0, ApplicationContext.FRAME_WIDTH, ApplicationContext.FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void doLogin(String userName, String passWord, String numCode, String abcCode) {
		
		LoginAction.login(userName, passWord, numCode, abcCode, new LoginActionListener() {
			@Override
			public void success() {
				//显示学生作业窗口
				MainFrame frame = new MainFrame();
				ApplicationContext.setMainFrame(frame);
				//关闭当前窗口
				LoginFrame.this.dispose();
			}
			
			@Override
			public void failed(String errType) {
				if (errType.equals("密码错误")) {
					JOptionPane.showMessageDialog(LoginFrame.this, "帐号或者密码错误", "登录失败", JOptionPane.ERROR_MESSAGE); 
				} else if (errType.equals("验证码错误")) {
					JOptionPane.showMessageDialog(LoginFrame.this, "验证码错误", "登录失败", JOptionPane.ERROR_MESSAGE); 
				}
			}
			
			@Override
			public void refreshImageCode() {
				LoginFrame.this.imgNumCode.rePaintImage();
				LoginFrame.this.imgABCCode.rePaintImage();
				//解决图片验证码刷新界面重影问题。
				LoginFrame.this.repaint();
			}
			
			@Override
			public void showABCImageCode() {
				LoginFrame.this.imgABCCode.setVisible(true);
				LoginFrame.this.txtABCCode.setVisible(true);
			}
			
		});	
	}
	
	
	
	private void login() {
		String userName = this.txtUserName.getText();
		String passWord = this.txtPassWord.getText();
		String numCode = this.txtNumCode.getText();
		String abcCode = this.txtABCCode.getText();
		
		doLogin(userName, passWord, numCode, abcCode);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnLogin) {
			login();
		}
	}
	
	
	
}





