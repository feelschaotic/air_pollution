package com.ramo.air.utils;

public class ServerUrlUtil {
	public final static String SERVER_NAMESPACE="http://192.168.191.1/air_pollution_server/";
	/*提交举报*/
	public final static String SUBMIT_REPORT=SERVER_NAMESPACE+"report_addReport";
	//查看举报
	public final static String ALL_REPORT=SERVER_NAMESPACE+"report_getAllFont";
	//我的举报
	public final static String MY_REPORT=SERVER_NAMESPACE+"report_myReport";
	//搜索举报
	public final static String SEARCH_REPORT=SERVER_NAMESPACE+"report_search";
	
	//关注
	public final static String ATTENTION_REPORT=SERVER_NAMESPACE+"report_attentionReport";
	//取消关注
	public final static String CANCEL_ATTENTION_REPORT=SERVER_NAMESPACE+"report_cancelAttentionReport";
	//我的关注
	public final static String MY_ATTENTION_REPORT=SERVER_NAMESPACE+"report_myAttentionReport";
	
	
	
	
	//当前用户是否已经实名验证
	public final static String IS_REALNAME=SERVER_NAMESPACE+"user_checkRealName";
	//实名认证
	public final static String UPDATE_REALNAME=SERVER_NAMESPACE+"user_updateRealName";
	//更新用户的推送id
	public static final String UPDATE_PUSHID = SERVER_NAMESPACE+"user_updatePushId";
	
	//我关注的有更新的举报
	public static final String MY_ATTENTION_NEW = SERVER_NAMESPACE+"report_myAttentionReportNew";
	//看完更新的举报后把关注的isNew状态还原为0
	public static final String UPDATE_ATTENTION_STATE = SERVER_NAMESPACE+"report_updateAttentionState";
	
	
	//查看环保新闻详情
	public static final String NEWS_DETAILS=SERVER_NAMESPACE+"news_details";
}
