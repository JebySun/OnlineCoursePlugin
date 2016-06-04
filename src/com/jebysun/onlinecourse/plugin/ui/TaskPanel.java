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
 * 作业列表
 * @author JebySun
 *
 */
public class TaskPanel extends JPanel {
	private static final long serialVersionUID = -4984638876926274072L;
	
	private MainFrame frame;
	private int pW = ApplicationContext.FRAME_WIDTH;
	private int pH = ApplicationContext.FRAME_HEIGHT-40-30;
	private String classId;
	private String courseId;

	public TaskPanel(MainFrame frame, String classId, String courseId) {
		this.frame = frame;
		this.classId = classId;
		this.courseId = courseId;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setSize(pW, pH);
		createTaskPanel();
		this.frame.setCurLocation(MainFrame.LOC_TASK);
	}
	
	
	private List<Map> getTaskData(String url) {
		List<Map> listMap = new ArrayList<Map>();
		try {
			Response response = Jsoup.connect(url)
					.cookies(ApplicationContext.getCookiesMap())
					.method(Connection.Method.GET)
					.execute();
			
			if (response.statusCode() == 200) {
				String htmlContent = response.body();
				Document doc = Jsoup.parse(htmlContent);
				Elements es = doc.select(".ulDiv ul li.lookLi");
				Map<String, String> map = null;
				for (Element e : es) {
					String taskName = e.select(".titTxt p a").get(0).attr("title");
					String taskUrl = e.select(".titOper a").get(0).attr("href");
					
					map = new HashMap<String, String>();
					map.put("taskName", taskName);
					map.put("taskUrl", taskUrl);
					listMap.add(map);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listMap;
	}
	
	private void createTaskPanel() {
		String url = ApplicationContext.TASK_LIST;
		url = url.replaceFirst("\\$", this.classId);
		url = url.replaceFirst("\\$", this.courseId);
		List<Map> taskList = getTaskData(url);
		
		if (taskList.size() == 0) {
			JPanel emptyPanel = new JPanel();
			JLabel labMsg = new JLabel("（暂时没有数据）");
			labMsg.setPreferredSize(new Dimension(pW, pH));
			emptyPanel.add(labMsg);
			this.add(emptyPanel);
		}
		
		for (int i=0; i<taskList.size(); i++) {
			final JPanel p = new JPanel();
			p.setBackground(new Color(0xFFFFFF));
			p.setBorder(BorderFactory.createLineBorder(new Color(0x999999)));
			p.setPreferredSize(new Dimension(180, 120));
			p.putClientProperty("data", taskList.get(i).get("taskUrl"));
			JLabel labCourseName = new JLabel(taskList.get(i).get("taskName").toString());
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
					String path = p.getClientProperty("data").toString();
					String workId = JavaUtil.getUrlParamValue(path, "workId");
					//进入学生作答面板
					showStudentReplyPanel(workId);
				}
			});
		}
	}
	
	
	private void showStudentReplyPanel(String workId) {
		this.frame.replaceContentPanel(new StudentReplyPanel(this.frame, this.classId, this.courseId, workId));
	}


}










