package com.example.ifamily.bean;

import java.io.Serializable;

public class Group implements Serializable {
	private int GroupId;//
	private String channelId;
	private String nick;//
	private Object headIcon;//
	private int group ; 

	public Group(int GroupId, String nick, Object headIcon,
			int group)
	{
		// TODO Auto-generated constructor stub
		this.GroupId = GroupId;
		//this.channelId = channelId;
		this.nick = nick;
		this.headIcon = headIcon;
		this.group = group;
	}
	public Group()
	{}


	public int getGroupId()
	{
		return GroupId;
	}

	public void setGroupId(int GroupId)
	{
		this.GroupId = GroupId;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getNick()
	{
		return nick;
	}

	public void setNick(String nick)
	{
		this.nick = nick;
	}

	public Object getHeadIcon()
	{
		return headIcon;
	}

	public void setHeadIcon(Object headIcon)
	{
		this.headIcon = headIcon;
	}

	public int getGroup()
	{
		return group;
	}

	public void setGroup(int group)
	{
		this.group = group;
	}

}
