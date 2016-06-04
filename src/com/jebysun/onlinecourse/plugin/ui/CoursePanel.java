package com.jebysun.onlinecourse.plugin.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jebysun.onlinecourse.plugin.ApplicationContext;
import com.jebysun.onlinecourse.plugin.util.JavaUtil;

/**
 * 课程列表
 * @author JebySun
 *
 */
public class CoursePanel extends JPanel {
	private static final long serialVersionUID = 3101657794710387165L;
	
	private MainFrame frame;
	private int pW = ApplicationContext.FRAME_WIDTH;
	private int pH = ApplicationContext.FRAME_HEIGHT-40-30;

	public CoursePanel(MainFrame frame) {
		this.frame = frame;  
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setSize(pW, pH);
		createCoursePanel();
		this.frame.setCurLocation(MainFrame.LOC_COURSE);
	}
	
	private List<Map> getCourseData(String url) {
		List<Map> listMap = new ArrayList<Map>();
		try {
			Response response = Jsoup.connect(url)
					.cookies(ApplicationContext.getCookiesMap())
					.method(Connection.Method.GET)
					.execute();
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				ApplicationContext.getCookiesMap().putAll(response.cookies());
				Document doc = Jsoup.parse(htmlContent);
				Elements es = doc.select(".Mconright");
				Map<String, String> map = null;
				for (Element e : es) {
					String courseName = e.select("h3 a").get(0).text();
					String courseUrl = e.select("h3 a").get(0).attr("href");
					String techName = e.select("p").get(0).text();
					String courseDes = e.select("p").get(1).text();
					
					map = new HashMap<String, String>();
					map.put("courseName", courseName);
					map.put("courseUrl", courseUrl);
					map.put("techName", techName);
					map.put("courseDes", courseDes);
					listMap.add(map);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listMap;
	}
	
	private void createCoursePanel() {
		List<Map> courseList = getCourseData(ApplicationContext.getData("CourseUrl"));
		if (courseList.size() == 0) {
			JPanel emptyPanel = new JPanel();
			JLabel labMsg = new JLabel("（暂时没有数据）");
			labMsg.setPreferredSize(new Dimension(pW, pH));
			emptyPanel.add(labMsg);
			this.add(emptyPanel);
		}
		
		for (int i=0; i<courseList.size(); i++) {
			final JPanel p = new JPanel();
			p.setBackground(new Color(0xFFFFFF));
			p.setBorder(BorderFactory.createLineBorder(new Color(0x999999)));
			p.setPreferredSize(new Dimension(180, 120));
			p.putClientProperty("data", courseList.get(i).get("courseUrl"));
			JLabel labCourseName = new JLabel(courseList.get(i).get("courseName").toString());
			labCourseName.setBounds(0, 0, 180, 30);
			p.add(labCourseName);
			this.add(p);
			p.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					String url = "http://mooc1.usts.edu.cn" + p.getClientProperty("data").toString();
					url = getTaskRealUrl(url);
					if (url != null) {
						String clazzid = JavaUtil.getUrlParamValue(url, "clazzid");
						String moocId = JavaUtil.getUrlParamValue(url, "moocId");
						//进入作业面板
						showTaskPanel(clazzid, moocId);
					}
				}
			});
		}
	}
	
	private void showTaskPanel(String classId, String courseId) {
		ApplicationContext.putData("classId", classId);
		ApplicationContext.putData("courseId", courseId);
		this.frame.replaceContentPanel(new TaskPanel(this.frame, classId, courseId));
	}
	
	/**
	 * 获取作业列表真实URL
	 * @param url
	 * @return
	 */
	private String getTaskRealUrl(String url) {
		try {
			Response response = Jsoup.connect(url)
					.cookies(ApplicationContext.getCookiesMap())
					.method(Connection.Method.GET)
					.execute();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				ApplicationContext.getCookiesMap().putAll(response.cookies());
				return response.url().toString();
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


}










