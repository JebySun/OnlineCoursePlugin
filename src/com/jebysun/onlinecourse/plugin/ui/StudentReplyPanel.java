package com.jebysun.onlinecourse.plugin.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jebysun.onlinecourse.plugin.ApplicationContext;
import com.jebysun.onlinecourse.plugin.util.JavaUtil;

/**
 * 学生作答列表
 * @author JebySun
 *
 */
public class StudentReplyPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -6556383206783751918L;
	
	private MainFrame frame;
	private int pW = ApplicationContext.FRAME_WIDTH;
	private int pH = ApplicationContext.FRAME_HEIGHT-40-30;
	private String[] tableHeader = { "姓名", "学号/帐号", "状态", "提交时间", "IP", "批阅时间", "成绩", "操作"};
	private Object[][] cellData = new Object[0][0];
	
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnExportScore;
	private JButton btnNext;
	private JButton btnPrev;
	private JLabel labPageInfo;
	
	private String courseId;
	private String classId;
	private String workId;
	
	private int pageCount = 3;
	private int pageIndex = 1;
	
	public StudentReplyPanel(MainFrame frame, String classId, String courseId, String workId) {
		this.frame = frame;
		this.classId = classId;
		this.courseId = courseId;
		this.workId = workId;
		
		this.setLayout(null);
		this.setBackground(new Color(0xFFFF00));
		this.setSize(pW, pH);
		
		this.btnExportScore = new JButton("导出成绩xls文件");
		this.btnPrev = new JButton("上一页");
		this.btnNext = new JButton("下一页");
		this.btnExportScore.setBounds(10, ApplicationContext.FRAME_HEIGHT-110, 140, 30);
		this.btnPrev.setBounds(320, ApplicationContext.FRAME_HEIGHT-110, 100, 30);
		this.btnNext.setBounds(440, ApplicationContext.FRAME_HEIGHT-110, 100, 30);
		this.btnExportScore.addActionListener(this);
		this.btnPrev.addActionListener(this);
		this.btnNext.addActionListener(this);
		this.add(btnExportScore);
		this.add(btnPrev);
		this.add(btnNext);
		
		this.labPageInfo = new JLabel(pageIndex + "/" + pageCount);
		this.labPageInfo.setBounds(550, ApplicationContext.FRAME_HEIGHT-110, 100, 30);
		this.add(labPageInfo);

		createTable();
		init();
		this.frame.setCurLocation(MainFrame.LOC_REPLY);
	}

	
	
	public void init() {
		listStudentReply(pageIndex);
	}
	
	public void createTable() {
		MyTableModel tableModel = new MyTableModel(cellData, tableHeader);
		table = new JTable(tableModel);
		
		// 不可编辑(不推荐)
//		table.setEnabled(false);
		// 不可整列移动
		table.getTableHeader().setReorderingAllowed(false);
		// 表格内容居中
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, render);
		// 设置表头高度
		table.getTableHeader().setPreferredSize(new Dimension(1, 30));
		// 设置行高
		table.setRowHeight(30);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 0, ApplicationContext.FRAME_WIDTH, ApplicationContext.FRAME_HEIGHT-30);
		this.add(scrollPane);
	}
	
	public void updateTableData() {
		MyTableModel tableModel = new MyTableModel(cellData, tableHeader);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(7).setCellRenderer(new MyButtonCellRender());
		table.getColumnModel().getColumn(7).setCellEditor(new MyButtonCellEditor(table));
	}

	
	/**
	 * 查询学生作业列表
	 * @param strUrl
	 */
	private void listStudentReply(int pageIndex) {
		String strUrl = ApplicationContext.WORK_QUERY_ACTION;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("workId", this.workId);
		paramMap.put("courseId", this.courseId);
		paramMap.put("pageNum", String.valueOf(pageIndex));
		paramMap.put("classId", this.classId);
		paramMap.put("evaluation", "0");
		paramMap.put("isdisplaytable", "2");
		paramMap.put("mooc", "1");
		paramMap.put("isWork", "true");
		paramMap.put("tempClassId", "306870");
		paramMap.put("dengji", "0");
		paramMap.put("firstHeader", "2");
		paramMap.put("schoolId", "1944");
		
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(ApplicationContext.getCookiesMap())
					.data(paramMap)
					.method(Connection.Method.POST)
					.execute();
			
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				Document doc = Jsoup.parse(htmlContent);
				Element table = doc.getElementById("tableId");
				Elements trs = table.select("tr");
				cellData = new Object[15][9];
				for (int i=0; i<trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.select("td");
					for (int k=0; k<tds.size(); k++) {
						cellData[i][k] = tds.get(k).text();
					}
					String tempUrl = tds.get(7).getElementsByTag("a").attr("onclick");
					cellData[i][8] = getParmValue(tempUrl, "workAnswerId");
				}
				
				cellData = JavaUtil.trimArrBlank(cellData);
				updateTableData();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getParmValue(String url, String paramName) {
		Pattern p = Pattern.compile(paramName + "=([^&]*)(&|$)");  
		Matcher m = p.matcher(url);  
	    if (m.find()) {  
	       return m.group(1);  
	    }
	    return null;
	}
	
	public void listPage(int i) {
		if (i<1) {
			pageIndex = 1;
			return;
		} else if (i>3) {
			pageIndex = 3;
			return;
		}
		this.pageIndex = i;
		this.labPageInfo.setText(pageIndex + "/" + pageCount);
		listStudentReply(i);
	}
	
	private void exportScoreInXLS() {
		String url = ApplicationContext.EXPORT_STU_SCORE;
		url = url.replaceFirst("\\$", courseId);
		url = url.replaceFirst("\\$", classId);
		url = url.replaceFirst("\\$", workId);
//		url = url.replaceFirst("\\$", mooc);
		
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle("请文件保存路径");
		fileChooser.setApproveButtonText("保存");
		FileNameExtensionFilter fileNameFilter = new FileNameExtensionFilter("xls表格文件", "xls", "XLS");
		fileChooser.setFileFilter(fileNameFilter);
    	int returnVal = fileChooser.showOpenDialog(fileChooser);
    	if (JFileChooser.APPROVE_OPTION == returnVal) {
    		String filepath = fileChooser.getSelectedFile().getAbsolutePath();
    		if (!filepath.endsWith(".xls") && !filepath.endsWith(".XLS")) {
    			filepath += ".xls";
    		}
    		//根据文件URL下载文件
    		fileDownload(url, filepath);
    	}
		
	}
	
	private void fileDownload(String fileUrl, String fileLocation) {
		Response response = null;
		try {
			response = Jsoup.connect(fileUrl)
					.cookies(ApplicationContext.getCookiesMap())
					.ignoreContentType(true)
					.method(Connection.Method.GET)
					.execute();
			
			byte[] file = response.bodyAsBytes();
			FileOutputStream out = (new FileOutputStream(new File(fileLocation)));
			out.write(file);           
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnNext) {
			listPage(++pageIndex);
		} else if (e.getSource() == this.btnPrev) {
			listPage(--pageIndex);
		} else if (e.getSource() == this.btnExportScore) {
			exportScoreInXLS();
		}
	}
	
	
	
	/////////////////////////////////////////////////////////////
	
	/**
	 * 表格模式
	 * @author JebySun
	 *
	 */
	private class MyTableModel extends DefaultTableModel {
		
		public MyTableModel(Object[][] cellData, String[] tableHeader) {
			super(cellData, tableHeader);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			// 带有按钮列的功能这里必须要返回true不然按钮点击时不会触发编辑效果，也就不会触发事件。
			if (column == 7) {
				return true;
			} else {
				return false;
			}
		}
	}
	

	
	/**
	 * 单元格渲染器
	 * @author JebySun
	 *
	 */
	private class MyButtonCellRender implements TableCellRenderer {
		private JPanel panel;
		private JButton button;

		public MyButtonCellRender() {
			this.initButton();
			this.initPanel();
			this.panel.add(this.button);
		}

		private void initButton() {
			this.button = new JButton();
			// 设置按钮的大小及位置。
			this.button.setBounds(0, 0, 120, 30);

			// 在渲染器里边添加按钮的事件是不会触发的
//			this.button.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					System.out.println(22222);
//				}
//			});

		}

		private void initPanel() {
			this.panel = new JPanel();
			// panel使用绝对定位，这样button就不会充满整个单元格。
			this.panel.setLayout(null);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			this.button.setText("查看");
			return this.panel;
		}
		
	}
	
	
	/**
	 * 表格单元格编辑器
	 * @author JebySun
	 *
	 */
	private class MyButtonCellEditor extends DefaultCellEditor {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -6546334664166791132L;

		private JPanel panel;

		private JButton button;
		
		JTable table;

		public MyButtonCellEditor(JTable table) {
			// DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
			super(new JTextField());
			
			this.table = table;

			// 设置点击几次激活编辑。
			this.setClickCountToStart(1);

			this.initButton();

			this.initPanel();

			// 添加按钮。
			this.panel.add(this.button);
		}

		private void initButton() {
			this.button = new JButton();

			// 设置按钮的大小及位置。
			this.button.setBounds(0, 0, 120, 30);

			// 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。
			this.button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 可以将table传入，通过getSelectedRow,getSelectColumn方法获取到当前选择的行和列及其它操作等。
//					System.out.println("table.getSelectedRow()="+table.getSelectedRow()+":getSelectedColumn()="+table.getSelectedColumn());
					
					String stuWorkId = (String)cellData[table.getSelectedRow()][8];
					String score = (String)cellData[table.getSelectedRow()][6];
					String title = (String)cellData[table.getSelectedRow()][0];
					String workDetailUrl = null;
					boolean isCommented = false;
					if (score.trim().equals("")) {
						//查看学生作业(未批阅)详情
						workDetailUrl = ApplicationContext.STUDENT_WORK_DETAIL.replaceFirst("\\$", stuWorkId).replaceFirst("\\$", String.valueOf(pageIndex));
					} else {
						//查看学生作业(已批阅)详情
						workDetailUrl = ApplicationContext.STUDENT_WORK_COMMENTED_DETAIL.replaceFirst("\\$", stuWorkId).replaceFirst("\\$", String.valueOf(pageIndex));
						isCommented = true;
					}
					new WorkDetailHTMLFrame(title, workDetailUrl, isCommented);
					
					// 触发取消编辑的事件，不会调用tableModel的setValue方法。
					MyButtonCellEditor.this.fireEditingCanceled();
				}
			});

		}

		private void initPanel() {
			this.panel = new JPanel();
			// panel使用绝对定位，这样button就不会充满整个单元格。
			this.panel.setLayout(null);
		}

		/**
		 * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
		 */
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			this.button.setText("查看");
			return this.panel;
		}

		/**
		 * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
		 */
		@Override
		public Object getCellEditorValue() {
			return this.button.getText();
		}
	}

}










