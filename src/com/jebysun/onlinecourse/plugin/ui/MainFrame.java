package com.jebysun.onlinecourse.plugin.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.jebysun.onlinecourse.plugin.ApplicationContext;
/**
 * 主窗口
 * @author JebySun
 *
 */
public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 4866331079859255525L;

	public static final String LOC_COURSE = "course";
	public static final String LOC_TASK = "task";
	public static final String LOC_REPLY = "reply";
	
	private JLabel lblCurrentUser;
	private JButton btnBack;
	private JPanel containerPanel;
	private String curLocation;

	public MainFrame() {
		initData();
		this.setLayout(null);
		
		this.btnBack = new JButton("返回");
		this.btnBack.setBounds(10, 5, 80, 30);
		this.add(this.btnBack);
		this.btnBack.addActionListener(this);
		
		this.lblCurrentUser = new JLabel("当前登录账户："+ApplicationContext.getData("UserName"));
		this.lblCurrentUser.setBounds(ApplicationContext.FRAME_WIDTH-140, 0, 140, 40);
		this.add(lblCurrentUser);
		//添加课程面板
		initCoursePanel();
		
		this.setTitle(ApplicationContext.FRAME_TITLE+"v"+ApplicationContext.VERSION);
		this.setBounds(0, 0, ApplicationContext.FRAME_WIDTH, ApplicationContext.FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void initCoursePanel() {
		this.replaceContentPanel(new CoursePanel(this));
	}
	
	/**
	 * 替换内容面板
	 * @param panel
	 */
	public void replaceContentPanel(JPanel panel) {
		if (this.containerPanel != null) {
			this.remove(this.containerPanel);
		}
		this.containerPanel = panel;
		this.containerPanel.setLocation(0, 40);
		this.add(this.containerPanel);
		this.containerPanel.updateUI();
		this.containerPanel.repaint();
		this.repaint();
	}

	public void initData() {
		getLoginedData(ApplicationContext.TECH_ZONE);
	}
	
	/**
	 * 获取登录后的必要信息
	 * @param strUrl
	 */
	public void getLoginedData(String strUrl) {
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(ApplicationContext.getCookiesMap())
					.method(Connection.Method.GET)
					.execute();
			
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				Document doc = Jsoup.parse(htmlContent);
				String userName = doc.getElementById("space_nickname").text();
				String courseUrl = doc.getElementsByAttributeValue("class", "currentpage").attr("href");
				courseUrl = courseUrl.substring(courseUrl.indexOf("http:"));
				ApplicationContext.putData("UserName", userName);
				ApplicationContext.putData("CourseUrl", courseUrl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void setCurLocation(String curLocation) {
		this.curLocation = curLocation;
		if (this.curLocation.equals(LOC_COURSE)) {
			this.btnBack.setVisible(false);
		} else {
			this.btnBack.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnBack) {
			if (curLocation.equals(LOC_COURSE)) {
				
			} else if (curLocation.equals(LOC_TASK)) {
				JPanel p = new CoursePanel(this);
				this.replaceContentPanel(p);
			} else if (curLocation.equals(LOC_REPLY)) {
				String classId = ApplicationContext.getData("classId");
				String courseId = ApplicationContext.getData("courseId");
				JPanel p = new TaskPanel(this, classId, courseId);
				this.replaceContentPanel(p);
			}
		}
	}
	
	
	

}










