

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HTMLPanel extends JFrame {
	
	String filePath = "C:\\Users\\JebySun\\Desktop\\test.html";
	
	public HTMLPanel() {
		this.setBounds(100, 100, 900, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		JEditorPane editorPane = new JEditorPane();  
		editorPane.setEditable(false); //请把editorPane设置为只读，不然显示就不整齐 
//			editorPane.setPage(path);
			editorPane.setContentType("text/html; charset=utf-8");
			String html = getHtmlStringFromFile(filePath);
			editorPane.setText(html);
		editorPane.setBounds(0, 0, 880, 500);
		
		JScrollPane jscrollPane = new JScrollPane(editorPane);
		jscrollPane.setBounds(0, 0, 900, 600);
		this.add(jscrollPane);
	}
	
	private String getHtmlStringFromFile(String path) {
		String contentStr = "";
		File f = new File(path);
		FileReader fr = null;
		String tempStr = null;
		try {
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			while ((tempStr = br.readLine()) != null) {
				contentStr += tempStr;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contentStr;
	}

	public static void main(String[] args) {
		new HTMLPanel();
	}

}
