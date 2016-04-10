package com.jebysun.onlinecourse.plugin.ui;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 7602027888917954442L;
	
	public static final int FRAME_WIDTH = 1000;
	public static final int FRAME_HEIGHT = 700;
	CardLayout cardLayout;
	LoginPanel loginPanel;
	StudentWorkListPanel stuWorkPanel;
	
	private static Map<String, String> cookiesMap = new HashMap<String, String>();
	
	public MainFrame() {
		intiWindow();
	}

	private void intiWindow() {
		this.setTitle("苏州科技大学学生作业批改程序");
		this.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.cardLayout = new CardLayout();
		this.setLayout(this.cardLayout);
		
		this.loginPanel = new LoginPanel(this, this.cardLayout);
		this.stuWorkPanel = new StudentWorkListPanel(this, this.cardLayout);
		
		this.add(loginPanel, "login");
		this.add(stuWorkPanel, "work_list");
		this.add(new StudentWorkDetailPanel(this, this.cardLayout), "work_detail");
		
		this.stuWorkPanel.test();
		this.cardLayout.show(this.getContentPane(), "work_list");

		this.setVisible(true);
	}

	public static Map<String, String> getCookiesMap() {
		return cookiesMap;
	}

	public static void setCookiesMap(Map<String, String> _cookiesMap) {
		cookiesMap = _cookiesMap;
	}
	
	

}





