package com.jebysun.onlinecourse.plugin;

import java.util.HashMap;
import java.util.Map;

import com.jebysun.onlinecourse.plugin.ui.StudentWorkListFrame;

/**
 * 项目公共资源容器
 * @author JebySun
 *
 */
public final class ApplicationContext {
	
	private static Map<String, String> cookiesMap = new HashMap<String, String>();
	private static StudentWorkListFrame worklistFrame;
	
	
	public static Map<String, String> getCookiesMap() {
		return ApplicationContext.cookiesMap;
	}

	public static void setCookiesMap(Map<String, String> _cookiesMap) {
		ApplicationContext.cookiesMap = _cookiesMap;
	}
	
	public static void setWorkListFrame(StudentWorkListFrame worklistFrame) {
		ApplicationContext.worklistFrame = worklistFrame;
	}
	
	public static StudentWorkListFrame getWorkListFrame() {
		return ApplicationContext.worklistFrame;
	}
	
	
	public static final String FRAME_TITLE = "苏州科技大学学生作业批改程序";
	public static final String VERSION = "1.1.0";
	
	//窗口尺寸
	public static final int FRAME_WIDTH = 900;
	public static final int FRAME_HEIGHT = 600;
	
	//用户登录界面
	public static String LOGIN_PAGE = "http://passport2.usts.edu.cn/login?fid=1944&refer=http://wlkt.usts.edu.cn";
	//登录动作执行URL
	public static String LOGIN_ACTION = "http://passport2.usts.edu.cn/login?refer=http%3A%2F%2Fwlkt.usts.edu.cn";
	//登录用户信息
	public static String USER_INFO_PAGE = "http://www.fanya.usts.edu.cn/passport/allHead.shtml?fid=1944";
	
	//数字验证码地址
	public static String NUM_CODE_PATH = "http://passport2.usts.edu.cn/num/code";
	//字母验证码地址
	public static String ABC_CODE_PATH = "http://passport2.usts.edu.cn/img/code";
	
	//课程查看地址（需要先执行查看课程，才能查看学生作业列表）
	public static String COURSE_PAGE = "http://mooc1.usts.edu.cn/mycourse/teachercourse?moocId=81300811&clazzid=306870&edit=true";
	//学生作业列表页面
	public static String STUDENT_WORK_PAGE = "http://mooc1.usts.edu.cn/work/reviewTheList?courseId=81300811&classId=306870&workId=181277&isdisplaytable=2&mooc=1&isWork=true";
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
