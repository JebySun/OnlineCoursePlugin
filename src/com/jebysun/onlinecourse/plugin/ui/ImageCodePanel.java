package com.jebysun.onlinecourse.plugin.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.jebysun.onlinecourse.plugin.parser.Config;

public class ImageCodePanel extends JPanel {
	private static final long serialVersionUID = 4204888815709087566L;
	private Image img;
	private int imgWidth;
	private int imgHeight;
	private String strUrl;
	private String requestHeader;
	
	public ImageCodePanel(String url, String requestHeader) {
		this.strUrl = url;
		this.requestHeader = requestHeader;
		this.img = initImage();
	}
	
	private Image initImage() {
		Image img = null;
		try {
			InputStream is = getInputStream(this.requestHeader);
			BufferedImage bfImg = ImageIO.read(is); 
			imgWidth = bfImg.getWidth();  
			imgHeight = bfImg.getHeight();  
			img = bfImg;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	
	/**
     * 获得服务器端数据，以InputStream形式返回
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream(String cookie) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(strUrl);
            if (url != null) {
                httpConn = (HttpURLConnection) url.openConnection();
                // 设置连接网络的超时时间
                httpConn.setConnectTimeout(3000);
                httpConn.setDoInput(true);
                // 设置本次http请求使用get方式请求
                httpConn.setRequestMethod("GET");
                httpConn.setRequestProperty("Cookie", cookie);
                int responseCode = httpConn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    inputStream = httpConn.getInputStream();
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        return inputStream;
    }
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, imgWidth, imgHeight);
		g.drawImage(img, 0, 0, imgWidth, imgHeight, null);
		g.setColor(Color.gray);
		g.drawRect(0, 0, imgWidth-1, imgHeight-1);
	}
	
	public void rePaintImage() {
		this.img = initImage();
		super.repaint();
	}

}




