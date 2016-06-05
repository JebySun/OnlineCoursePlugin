package com.jebysun.onlinecourse.plugin;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
/**
 * 项目公共资源容器
 * @author JebySun
 *
 */
public final class ApplicationContext {
	
	private static Map<String, String> cookiesMap = new HashMap<String, String>();
	private static Map<String, String> dataMap = new HashMap<String, String>();
	private static JFrame worklistFrame;
	
	
	public static Map<String, String> getCookiesMap() {
		return ApplicationContext.cookiesMap;
	}

	public static void setCookiesMap(Map<String, String> _cookiesMap) {
		ApplicationContext.cookiesMap = _cookiesMap;
	}
	
	public static void putData(String key, String value) {
		ApplicationContext.dataMap.put(key, value);
	}
	
	public static String getData(String key) {
		return ApplicationContext.dataMap.get(key);
	}

	public static void setMainFrame(JFrame worklistFrame) {
		ApplicationContext.worklistFrame = worklistFrame;
	}
	
	public static JFrame getMainFrame() {
		return ApplicationContext.worklistFrame;
	}
	
	
	public static final String FRAME_TITLE = "苏州科技大学学生作业批改程序";
	public static final String VERSION = "2.0.0";
	
	//窗口尺寸
	public static final int FRAME_WIDTH = 900;
	public static final int FRAME_HEIGHT = 600;
	
	//用户登录界面
	public static String LOGIN_PAGE = "http://passport2.usts.edu.cn/login?fid=1944&refer=http://wlkt.usts.edu.cn";
	//登录动作执行URL
	public static String LOGIN_ACTION = "http://passport2.usts.edu.cn/login?refer=http%3A%2F%2Fwlkt.usts.edu.cn";
	//教学空间首页
	public static String TECH_ZONE = "http://i.mooc.usts.edu.cn/space/index.shtml";
	
	//数字验证码地址
	public static String NUM_CODE_PATH = "http://passport2.usts.edu.cn/num/code";
	//字母验证码地址
	public static String ABC_CODE_PATH = "http://passport2.usts.edu.cn/img/code";
	
	//课程下作业列表
	public static String TASK_LIST = "http://mooc1.usts.edu.cn/work/getAllWork?classId=$&courseId=$&isdisplaytable=2&mooc=1&ut=t";
	
	//学生作业列表实际动作执行URL
	public static String WORK_QUERY_ACTION = "http://mooc1.usts.edu.cn/work/searchMarkList";
	
	//查看某个学生作业详情
	public static String STUDENT_WORK_DETAIL = "http://mooc1.usts.edu.cn/work/reviewTheContentNew?workId=181277&workAnswerId=$&courseId=81300811&classId=306870&isdisplaytable=2&mooc=1&isWork=true&firstHeader=2&pageNum=$";
	//查看已批复的某个学生作业详情
	public static String STUDENT_WORK_COMMENTED_DETAIL = "http://mooc1.usts.edu.cn/work/selectWorkQuestionYiPiYue?courseId=81300811&classId=306870&workId=181277&workAnswerId=$&isdisplaytable=2&mooc=1&isWork=true&firstHeader=2&ut=t&pageNum=$&enc=6c7f8918a09df7ba754aa6ced1708030";
	//批复学生作业动作执行URL
	public static String COMMENT_STU_WORK_ACTION = "http://mooc1.usts.edu.cn/work/saveReviewTheContentNew";
	//导出学生成绩xls文件
	public static String EXPORT_STU_SCORE = "http://mooc1.usts.edu.cn/work/exportScore?courseId=$&classId=$&workId=$&mooc=$";
	
	
}
