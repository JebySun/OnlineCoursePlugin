package com.jebysun.onlinecourse.plugin.ui;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class WorkDetailHTMLFrame extends JFrame {
	private static final long serialVersionUID = 126102845420714542L;
	private boolean isCommented;
	
	public WorkDetailHTMLFrame(String title, String workDetailUrl, boolean isCommented) {
		this.setTitle(title);
		this.setBounds(0, 0,  MainFrame.FRAME_WIDTH, MainFrame.FRAME_HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
		this.setLayout(null);
		
		this.isCommented = isCommented;
		
		studentWorkDetail(workDetailUrl);
	}
	
	private void createHTMLPanel(String infoHTML) {
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false); //请把editorPane设置为只读，不然显示就不整齐 
		editorPane.setContentType("text/html; charset=utf-8");
		String fullHTML = buildHtmlString(infoHTML);
		editorPane.setText(fullHTML);
		editorPane.setBounds(0, 0, MainFrame.FRAME_WIDTH, MainFrame.FRAME_HEIGHT);
		
		JScrollPane jscrollPane = new JScrollPane(editorPane);
		jscrollPane.setBounds(0, 0, MainFrame.FRAME_WIDTH-2, MainFrame.FRAME_HEIGHT-27);
		this.add(jscrollPane);
	}
	
	
	/**
	 * 查看学生作业详情
	 * @param strUrl
	 */
	private void studentWorkDetail(String strUrl) {
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(MainFrame.getCookiesMap())
					.method(Connection.Method.GET)
					.execute();
			
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				System.out.println(htmlContent);
				Document doc = Jsoup.parse(htmlContent);
//				doc.select(".TiMu .fl").get(0).remove();
				if (!isCommented) {
					doc.select(".TiMu .Yj_score").get(0).remove();
				}
				String infoHtml = doc.select(".TiMu").get(0).html();
				createHTMLPanel(infoHtml);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 拼装完整的html
	 * @param html
	 * @return
	 */
	private String buildHtmlString(String html) {
		String fullHTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>作业详情</title></head><body>$</body></html>";
		fullHTML = fullHTML.replaceFirst("\\$", html);
		return fullHTML;
	}

}
