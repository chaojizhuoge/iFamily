package com.example.ifamily;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.baidu.frontia.FrontiaApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.ifamily.client.PushMessageReceiver;
import com.example.ifamily.dao.MessageDB;
import com.example.ifamily.dao.UserDB;
import com.example.ifamily.server.BaiduPush;
import com.example.ifamily.utils.SharePreferenceUtil;

public class PushApplication extends FrontiaApplication
{
	/**
	 * API_KEY
	 */
	public final static String API_KEY = "nBk08l3TWMuGUu0fA1kU7H75";
	/**
	 * SECRET_KEY
	 */
	public final static String SECRIT_KEY = "PTkDmpenUzPNbiUvU6GSqmgBe6GypMKd";
	public static final String SP_FILE_NAME = "push_msg_sp";
	/**
	 * 预定义的头像
	 */
	public static final int[] heads = { R.drawable.h1,
			R.drawable.h2, R.drawable.h3, R.drawable.h4, R.drawable.h5,
			R.drawable.h6, R.drawable.h7, R.drawable.h8, R.drawable.h9,
			R.drawable.h10, R.drawable.h11, R.drawable.h12, R.drawable.h13,
			R.drawable.h14, R.drawable.h15, R.drawable.h16, R.drawable.h17,
			R.drawable.h18 };
	/**
	 * 表情页数
	 */

	private static PushApplication mApplication;

	private BaiduPush mBaiduPushServer;

	private SharePreferenceUtil mSpUtil;

	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private Gson mGson;

	private UserDB userDB;
	private MessageDB messageDB;
	private Map<String,Object> userInfo = new HashMap<String,Object>();
	
	private Set<String> groupIds;

	public synchronized static PushApplication getInstance()
	{
		return mApplication;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		mApplication = this;
		initData();
	}

	private void initData()
	{
		mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
				SECRIT_KEY, API_KEY);
		// 不转换没有 @Expose 注解的字段
		mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		userDB = new UserDB(this);
		messageDB = new MessageDB(this);
	}

	public synchronized BaiduPush getBaiduPush()
	{
		if (mBaiduPushServer == null)
			mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
					SECRIT_KEY, API_KEY);
		return mBaiduPushServer;
	}

	public synchronized Gson getGson()
	{
		if (mGson == null)
			// 不转换没有 @Expose 注解的字段
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
		return mGson;
	}

	public NotificationManager getNotificationManager()
	{
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}

	public synchronized SharePreferenceUtil getSpUtil()
	{
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		return mSpUtil;
	}

	/**
	 * 创建挂机图标
	 */


	public MessageDB getMessageDB()
	{
		return messageDB;
	}

	public UserDB getUserDB()
	{
		return userDB;
	}
	
	public Map<String,Object> getUserInfo()
	{
		return userInfo;
	}
	
	public void setUserInfo(Map<String,Object> userInfo)
	{
		this.userInfo = userInfo;
	}
	
	public Set<String> getgroupIds()
	{
		return groupIds;
	}
	
	public void setGroupIds(Set<String> groupIds)
	{
		this.groupIds = groupIds;
	}

}
