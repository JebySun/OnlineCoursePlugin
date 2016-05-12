package com.jebysun.onlinecourse.plugin.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import com.jebysun.onlinecourse.plugin.ApplicationContext;

/**
 * 学生作业详情
 * @author Administrator
 *
 */
public class WorkDetailHTMLFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 126102845420714542L;

	private boolean isCommented;

	private JRadioButton jRbtnOK;
	private JRadioButton jRbtnError;
	private JRadioButton jRbtnSoso;
	
	private JTextField jTxtScore;
	private JTextField jTxtComment;
	private JButton btnSubmit;
	
	private String answerwqbid;
	private String courseId;
	private String classId;
	private String workAnswerId;
	private String workId;
	private String enc;
	private String pageNum;
	
	private String commentType;
	
	
	public WorkDetailHTMLFrame(String title, String workDetailUrl, boolean isCommented) {
		this.setTitle(title);
		this.setBounds(40, 40,  ApplicationContext.FRAME_WIDTH, ApplicationContext.FRAME_HEIGHT);
		this.setResizable(false);
		this.setLayout(null);
		
		this.isCommented = isCommented;
		studentWorkDetail(workDetailUrl);
		this.setVisible(true);
	}
	
	private void initView() {
		this.jRbtnOK = new JRadioButton("回答正确");
		this.jRbtnError = new JRadioButton("回答错误");
		this.jRbtnSoso = new JRadioButton("回答基本正确");
		this.add(this.jRbtnOK);
		this.add(this.jRbtnError);
		this.add(this.jRbtnSoso);
		this.jRbtnOK.setBounds(10, ApplicationContext.FRAME_HEIGHT-60, 80, 30);
		this.jRbtnError.setBounds(100, ApplicationContext.FRAME_HEIGHT-60, 80, 30);
		this.jRbtnSoso.setBounds(190, ApplicationContext.FRAME_HEIGHT-60, 110, 30);
		ButtonGroup commentBtnGroup = new ButtonGroup();
		commentBtnGroup.add(this.jRbtnOK);
		commentBtnGroup.add(this.jRbtnError);
		commentBtnGroup.add(this.jRbtnSoso);
		this.jRbtnOK.addActionListener(this);
		this.jRbtnError.addActionListener(this);
		this.jRbtnSoso.addActionListener(this);

		JLabel scoreLabel = new JLabel("分数");
		this.add(scoreLabel);
		scoreLabel.setBounds(310, ApplicationContext.FRAME_HEIGHT-60, 40, 30);
		
		this.jTxtScore = new JTextField();
		this.add(this.jTxtScore);
		this.jTxtScore.setBounds(340,  ApplicationContext.FRAME_HEIGHT-60, 60, 30);
		
		
		JLabel labelScore = new JLabel("评语");
		this.add(labelScore);
		labelScore.setBounds(420, ApplicationContext.FRAME_HEIGHT-60, 40, 30);
		
		this.jTxtComment = new JTextField();
		this.add(this.jTxtComment);
		this.jTxtComment.setBounds(450,  ApplicationContext.FRAME_HEIGHT-60, 380, 30);
		
		this.btnSubmit = new JButton("提交");
		this.add(this.btnSubmit);
		this.btnSubmit.setBounds(830,  ApplicationContext.FRAME_HEIGHT-60, 60, 30);
		this.btnSubmit.addActionListener(this);
	}
	
	/**
	 * 已批复信息显示
	 * @param score
	 * @param comment
	 */
	private void initCommentedView(String score, String comment) {
		JLabel scoreLabel = new JLabel("分数："+score);
		this.add(scoreLabel);
		scoreLabel.setBounds(10, ApplicationContext.FRAME_HEIGHT-60, 80, 30);
		
		JLabel commentLabel = new JLabel("评语："+comment);
		this.add(commentLabel);
		commentLabel.setBounds(100, ApplicationContext.FRAME_HEIGHT-60, 790, 30);
	}
	
	private void createHTMLPanel(String infoHTML, String score, String comment) {
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false); //editorPane设置为只读，不然显示就不整齐 
		editorPane.setContentType("text/html; charset=utf-8");
		String fullHTML = buildHtmlString(infoHTML);
		editorPane.setText(fullHTML);
		JScrollPane jscrollPane = new JScrollPane(editorPane);
		jscrollPane.setBounds(0, 0, ApplicationContext.FRAME_WIDTH-4, ApplicationContext.FRAME_HEIGHT-60);
		this.add(jscrollPane);
		
		if (isCommented) {
			initCommentedView(score, comment);
		} else {
			initView();
		}
	}
	
	
	/**
	 * 查看学生作业详情
	 * @param strUrl
	 */
	private void studentWorkDetail(String strUrl) {
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(ApplicationContext.getCookiesMap())
					.method(Connection.Method.GET)
					.execute();
			
			ApplicationContext.getCookiesMap().putAll(response.cookies());
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				Document doc = Jsoup.parse(htmlContent);
//				doc.select(".TiMu .fl").get(0).remove();
				
				//去除img节点
				doc.select(".zyTop img").get(0).remove();
				String title = doc.select(".zyTop").get(0).text().trim();
				//设置frame标题
				this.setTitle(title);
				
				String score = null;
				String comment = null;
				if (isCommented) {
					score = title.substring(title.lastIndexOf("：")+1);
					comment = doc.select(".pingyu").get(0).text();
				} else {
					this.answerwqbid = doc.getElementById("moreScore").attr("data");
					this.courseId = doc.getElementById("courseId").attr("value");
					this.classId = doc.getElementById("classId").attr("value");
					this.workAnswerId = doc.getElementById("workAnswerId").attr("value");
					this.workId = doc.getElementById("workId").attr("value");
					this.enc = doc.getElementById("enc").attr("value");
					this.pageNum = doc.getElementById("pageNum").attr("value");
					doc.select(".TiMu .Yj_score").get(0).remove();
				}
				String infoHtml = doc.select(".TiMu").get(0).html();
				//显示学生作业详情
				createHTMLPanel(infoHtml, score, comment);
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
	
	
	/**
	 * 批复学生作业
	 */
	public void commentStudentWork(String score, String commentType, String comment) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mooc", "1");
		paramMap.put("isRework", "false");
		paramMap.put("isWork", "true");
		paramMap.put("isdisplaytable", "2");
		paramMap.put("firstHeader", "2");
		paramMap.put("average", "100.0");

		paramMap.put("courseId", this.courseId);
		paramMap.put("classId", this.classId);
		paramMap.put("workAnswerId", this.workAnswerId);
		paramMap.put("workId", this.workId);
		paramMap.put("enc", this.enc);
		paramMap.put("answerwqbid", this.answerwqbid+",");
		
		paramMap.put("score", score);
		paramMap.put("fastPy1", commentType);
		paramMap.put("answer"+this.answerwqbid, comment);
		paramMap.put("pageNum", this.pageNum);

//		paramMap.put("score54001435", "55");
		try {
			Response response = Jsoup.connect(ApplicationContext.COMMENT_STU_WORK_ACTION)
					.cookies(ApplicationContext.getCookiesMap())
					.data(paramMap)
					.method(Connection.Method.POST)
					.execute();
			
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				//关闭窗口
				this.dispose();
				//刷新列表
				ApplicationContext.getWorkListFrame().listPage(Integer.parseInt(this.pageNum));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.jRbtnOK) {
			this.commentType = this.jRbtnOK.getText();
			this.jTxtComment.setText(this.commentType);
		} else if (e.getSource() == this.jRbtnError) {
			this.commentType = this.jRbtnError.getText();
			this.jTxtComment.setText(this.commentType);
		} else if (e.getSource() == this.jRbtnSoso) {
			this.commentType = this.jRbtnSoso.getText();
			this.jTxtComment.setText(this.commentType);
		}else if (e.getSource() == this.btnSubmit) {
			String score = this.jTxtScore.getText();
			String commentType = this.commentType;
			String comment = this.jTxtComment.getText();
			if (StringUtil.isBlank(commentType) || StringUtil.isBlank(score) || StringUtil.isBlank(comment)) {
				return;
			}
			commentStudentWork(score, commentType, comment);
		}
	}

}









