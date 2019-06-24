package net.qyq.graduation_project.palmtop_campus.utils;

public class Constant {
    //判断是否登录
    public static final String LOGIN = "login";
    public static final String NO_LOGIN = "no_login";
    //升级检测
    public static final String CHECK_UPDATE_URL = "http://palmtopcampus.wicp.vip:50649/update.json";
    //登录检测
    public static final String USER_LOGIN_URL = "http://palmtopcampus.wicp.vip:50649/androidlogin/login";
    //激活链接
    public static final String UPDATE_TEACHER_URL = "http://palmtopcampus.wicp.vip:50649/android_s_t_friends/updateTeacher";
    public static final String UPDATE_STUDENT_URL = "http://palmtopcampus.wicp.vip:50649/android_s_t_friends/updateStudent";
    //所有院系师生
    public static final String S_T_FRIENDS_URL = "http://palmtopcampus.wicp.vip:50649/android_s_t_friends/friendsST";
    //成绩查询
    public static final String SEARCH_SCORE_URL = "http://palmtopcampus.wicp.vip:50649/androidscore/searchScore";
    //通知
    public static final String NOTICES_URL = "http://palmtopcampus.wicp.vip:50649/androidnotices/noticesPush";
    //新闻
    public static final String NEWS_URL = "http://palmtopcampus.wicp.vip:50649/androidnews/getNews";
    //咨询
    public static final String ADVISORY_URL = "http://palmtopcampus.wicp.vip:50649/androidadvisory/advisoryShow";
    //好友
    public static final String FRIENDS_URL = "http://palmtopcampus.wicp.vip:50649/androidfrends/addFriend";
    //	//升级检测
    //	public static final String CHECK_UPDATE_URL = "http://192.168.1.109:8080/update.json";
    //	//登录检测
    //	public static final String USER_LOGIN_URL = "http://192.168.1.109:8080/androidlogin/login";
    //	//所有院系师生
    //	public static final String S_T_FRIENDS_URL = "http://192.168.1.102:8080/android_s_t_friends/friendsST";
    //	//成绩查询
    //	public static final String SEARCH_SCORE_URL = "http://192.168.1.102:8080/androidscore/searchScore";
    //	//通知
    //	public static final String NOTICES_URL = "http://192.168.1.102:8080/androidnotices/noticesPush";
    //	//新闻
    //	public static final String NEWS_URL = "http://192.168.1.102:8080/androidnews/getNews";
    //	//咨询
    //	public static final String ADVISORY_URL = "http://192.168.1.102:8080/androidadvisory/advisoryShow";
    //	//好友
    //	public static final String FRIENDS_URL = "http://192.168.1.102:8080/androidfrends/addFriend";


    //对应的返回码
    public static final int CODE_SUCCESSFUL = 0;
    public static final int CODE_TEACHER = 1;
    public static final int CODE_STUDENT = 2;
    public static final int CODE_NO_USER = 3;
    public static final int CODE_ERROR = 4;
    public static final int CODE_USER_IS_FRIEND = 5;


    //高德地图使用的单位
    public static final String Kilometer = "\u516c\u91cc";// "公里";
    public static final String Meter = "\u7c73";// "米";
    public static final String type = "\u7c7b\u522b"; // 类别
}
