package com.jebysun.onlinecourse.plugin.parser;

public final class Config {
	
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
	//学生作业列表
	public static String STUDENT_WORK_PAGE = "http://mooc1.usts.edu.cn/work/reviewTheList?courseId=81300811&classId=306870&workId=181277&isdisplaytable=2&mooc=1&isWork=true";
	//查看某个学生作业详情
	public static String WORK_QUERY_URL = "http://mooc1.usts.edu.cn/work/searchMarkList";
	//批复学生作业动作执行URL
	public static String COMMENT_STU_WORK_URL = "http://mooc1.usts.edu.cn/work/saveReviewTheContentNew";
	
}
