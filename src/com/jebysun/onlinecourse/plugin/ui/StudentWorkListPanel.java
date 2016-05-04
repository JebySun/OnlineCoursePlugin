package com.jebysun.onlinecourse.plugin.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jebysun.onlinecourse.plugin.parser.Config;

public class StudentWorkListPanel extends JPanel {
	private static final long serialVersionUID = 4866331079665681296L;
	private JLabel labLoginedUser;
	private String[] tableHeader = { "姓名", "学号/帐号", "状态", "提交时间", "IP", "批阅时间", "成绩", "操作"};
	private Object[][] cellData = new Object[15][9];

	public StudentWorkListPanel(Container container, CardLayout cardLayout) {
		this.setLayout(null);
		labLoginedUser = new JLabel("学生作业列表");
		labLoginedUser.setBounds(0, 0, 200, 30);
		this.add(labLoginedUser);

	}

	public void init() {
		getLoginedUser(Config.USER_INFO_PAGE);
	}
	
	public void createTable(Object[][] cellData) {
		MyTableModel tableModel = new MyTableModel(cellData, tableHeader);
		JTable table = new JTable(tableModel);
		
		table.getColumnModel().getColumn(7).setCellRenderer(new MyButtonCellRender());
		table.getColumnModel().getColumn(7).setCellEditor(new MyButtonCellEditor(table));
		
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

		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);
		scrollPane.setBounds(0, 30, MainFrame.FRAME_WIDTH, MainFrame.FRAME_HEIGHT);
	}
	
	/**
	 * 获取登录用户信息
	 * @param strUrl
	 */
	public void getLoginedUser(String strUrl) {
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(MainFrame.getCookiesMap())
					.method(Connection.Method.GET)
					.ignoreContentType(true)
					.execute();
			
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				System.out.println("=====================登录用户信息=======================");
				int keyIndex = htmlContent.indexOf("zt_u_name");
				htmlContent = htmlContent.substring(keyIndex, keyIndex+24);
				String userName = htmlContent.substring(11, htmlContent.indexOf("<"));
				System.out.println(userName);
				labLoginedUser.setText("当前登录账户："+userName);
				
				tempRequest(Config.COURSE_PAGE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void tempRequest(String strUrl) {
		try {
			Response response = Jsoup.connect(strUrl)
					.cookies(MainFrame.getCookiesMap())
					.method(Connection.Method.GET)
					.execute();
			
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				MainFrame.getCookiesMap().putAll(response.cookies());
				
//				studentWorkRequest(Config.STUDENT_WORK_PAGE);
				listStudentWork(Config.WORK_QUERY_ACTION);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询学生作业列表
	 * @param strUrl
	 */
	public void listStudentWork(String strUrl) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("workId", "181277");
		paramMap.put("courseId", "81300811");
		paramMap.put("pageNum", "2");
		paramMap.put("classId", "306870");
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
					.cookies(MainFrame.getCookiesMap())
					.data(paramMap)
					.method(Connection.Method.POST)
					.execute();
			
			String htmlContent = response.body();
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				System.out.println("学生作业列表");
				Element table = Jsoup.parse(htmlContent).getElementById("tableId");
				Elements trs = table.select("tr");
				for (int i=0; i<trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.select("td");
					for (int k=0; k<tds.size(); k++) {
						System.out.print(tds.get(k).text()+"\t");
						cellData[i][k] = tds.get(k).text();
					}
					String tempUrl = tds.get(7).getElementsByTag("a").attr("onclick");
					cellData[i][8] = getParmValue(tempUrl, "workAnswerId");
					System.out.println(cellData[i][8]);
				}
				
				createTable(cellData);
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
						workDetailUrl = Config.STUDENT_WORK_DETAIL.replaceFirst("\\$", stuWorkId);
					} else {
						//查看学生作业(已批阅)详情
						workDetailUrl = Config.STUDENT_WORK_COMMENTED_DETAIL.replaceFirst("\\$", stuWorkId);
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










