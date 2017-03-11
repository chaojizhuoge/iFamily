package com.example.ifamily.bean;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.Expose;
import com.example.ifamily.PushApplication;
import com.example.ifamily.utils.SharePreferenceUtil;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Expose
	private String userId;
	@Expose
	private String channelId;
	@Expose
	private String nickname;
	@Expose
	private Object headIcon;
	@Expose
	private long timeSamp;
	@Expose
	private String message;
	@Expose
	private int groupID;
	/**
	 * ���˵�һ�μ���ʱ����㲥����ֶΣ���ֵΪhello
	 */
	@Expose
	private String hello;
	/**
	 * ���յ����˵�helloʱ���Զ��ظ����ֶ���ֵΪworld
	 */
	@Expose
	private String world;
	


	public Message(long time_samp, String message,String groupID)
	{
		super();
		SharePreferenceUtil util = PushApplication.getInstance().getSpUtil();
		this.userId = PushApplication.getInstance().getApplicationContext().getSharedPreferences("user", 0).getString("username", "");
		this.channelId = util.getChannelId();
		this.nickname = util.getNick();
		//this.headIcon = util.getHeadIcon();
		this.timeSamp = time_samp;
		this.message = message;
		this.groupID = Integer.parseInt(groupID);
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public Object getHeadIcon()
	{
		return headIcon;
	}

	public void setHeadIcon(Object headIcon)
	{
		this.headIcon = headIcon;
	}

	public long getTimeSamp()
	{
		return timeSamp;
	}

	public void setTimeSamp(long timeSamp)
	{
		this.timeSamp = timeSamp;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getHello()
	{
		return hello;
	}

	public void setHello(String hello)
	{
		this.hello = hello;
	}

	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
	}
	
	public void setGroupID(int groupid)
	{
		this.groupID=groupid;
	}
	
	public int getGroupID()
	{
		return groupID;
	}

}
