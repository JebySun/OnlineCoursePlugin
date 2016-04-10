

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LayoutContainer {
	JFrame frame;
	
	public LayoutContainer() {
		frame = new JFrame("Frame Container");
		frame.setBounds(0, 0, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel p1 = new JPanel();
		p1.setLayout(null);
		JLabel lab1 = new JLabel("1111111111");
		lab1.setBounds(0, 0, 100, 100);
		p1.add(lab1);
		
		JPanel p2 = new JPanel();
		p2.setLayout(null);
		JLabel lab2 = new JLabel("22222222222");
		lab2.setBounds(0, 0, 100, 100);
		p2.add(lab2);
		
		
		CardLayout layout = new CardLayout();
		
		frame.setLayout(layout);
		frame.add(p1, "p1");
		frame.add(p2, "p2");
//		layout.show(frame.getContentPane(), "p2");
	}
	

	public static void main(String[] args) {
		new LayoutContainer();

	}

}
