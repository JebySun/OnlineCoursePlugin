package com.jebysun.onlinecourse.plugin.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;

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

public class StudentWorkListPanel extends JPanel {
	private static final long serialVersionUID = 4866331079665681296L;

	public StudentWorkListPanel(Container container, CardLayout cardLayout) {
		this.setLayout(null);
		// JLabel lab1 = new JLabel("学生作业列表");
		// lab1.setBounds(0, 0, 200, 10);
		// this.add(lab1);

	}

	public void test() {
		Object[][] cellData = { { "Jeby", "89", "99", "33", ""},
				{ "孙春", "55", "76", "55" , ""}, { "孙春", "55", "76", "55" , ""},
				{ "孙春", "55", "76", "55" , ""}, { "孙春", "55", "76", "55" , ""},
				{ "孙春", "55", "76", "55" , ""}, { "孙春", "55", "76", "55" , ""},
				{ "孙春", "55", "76" , "33" , ""} };
		String[] tableHeader = { "姓名", "语文", "数学", "英语", "操作"};
		
		MyTableModel tableModel = new MyTableModel(cellData, tableHeader);
		JTable table = new JTable(tableModel);
		
		table.getColumnModel().getColumn(4).setCellRenderer(new MyButtonCellRender());
		table.getColumnModel().getColumn(4).setCellEditor(new MyButtonCellEditor(table));
		
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
		scrollPane.setBounds(0, 0, MainFrame.FRAME_WIDTH, 200);

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
			if (column == 4) {
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
			this.button.setText("批阅");
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
					// 这里可以做其它操作。
					// 可以将table传入，通过getSelectedRow,getSelectColumn方法获取到当前选择的行和列及其它操作等。
					
					System.out.println("table.getSelectedRow()="+table.getSelectedRow()+":getSelectedColumn()="+table.getSelectedColumn());
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
			this.button.setText("批阅");
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










