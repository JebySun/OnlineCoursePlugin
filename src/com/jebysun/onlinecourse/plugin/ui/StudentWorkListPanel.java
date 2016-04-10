package com.jebysun.onlinecourse.plugin.ui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StudentWorkListPanel extends JPanel {
	private static final long serialVersionUID = 4866331079665681296L;

	public StudentWorkListPanel(Container container, CardLayout cardLayout) {
		this.setLayout(null);
//		JLabel lab1 = new JLabel("学生作业列表");
//		lab1.setBounds(0, 0, 200, 10);
//		this.add(lab1);
		
		
	}
	
	public void test() {
//		JavaUtil.printByEntrySet(MainFrame.getCookiesMap());
		
		Object[][] cellData = {{"Jeby", "89", "99"}, 
				{"孙春", "55", "76", "55"},
				{"孙春", "55", "76", "55"},
				{"孙春", "55", "76", "55"},
				{"孙春", "55", "76", "55"},
				{"孙春", "55", "76", "55"},
				{"孙春", "55", "76", "55"},
				{"孙春", "55", "76"}};
		String[] tableHeader = {"姓名", "语文", "数学", "英语"};
		DefaultTableModel tableModel = new DefaultTableModel(cellData, tableHeader);
		
//		JTable table = new JTable(cellData, tableHeader);
		JTable table = new JTable(tableModel);
		//不可编辑
		table.setEnabled(false);
		//不可整列移动 
		table.getTableHeader().setReorderingAllowed(false); 
		//表格内容居中
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();   
		render.setHorizontalAlignment(JLabel.CENTER); 
		table.setDefaultRenderer(Object.class, render);
		//设置表头高度
		table.getTableHeader().setPreferredSize(new Dimension(1, 30));
		//设置行高
		table.setRowHeight(30);

		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);
		scrollPane.setBounds(10, 10, 600, 200);
		
		
		
	}

}
