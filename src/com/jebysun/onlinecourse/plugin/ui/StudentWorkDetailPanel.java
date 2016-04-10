package com.jebysun.onlinecourse.plugin.ui;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StudentWorkDetailPanel extends JPanel {
	private static final long serialVersionUID = 4866331079665681296L;

	public StudentWorkDetailPanel(Container container, CardLayout cardLayout) {
		this.setLayout(null);
		JLabel lab1 = new JLabel("学生作业详情");
		lab1.setBounds(0, 0, 400, 100);
		this.add(lab1);
	}

}
