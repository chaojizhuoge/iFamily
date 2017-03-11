package com.example.ifamily.message;

public class OldObjectMessage {
	private int messageID;
	private long account;
	private Object img;
	private Object head;
	private String name;
	private String text;
	private int state;
	private String time;
	private int type;
	private int comnum;

	public  OldObjectMessage(Object heads,Object imgs,String names) {

		super();

		this.head=heads;
		this.img =imgs;
		this.name=names;

	}
	public  OldObjectMessage(int widths,int heights,Object heads,Object imgs,String names) {

		super();

		this.head=heads;
		this.img =imgs;
		this.name=names;

	}

	//自己送出的物品 id,头像，图片，名字，内容，时间，状态,
	public  OldObjectMessage(long account,Object heads,Object imgs,String names,String text,String time,int state) {

		super();
		this.account=account;
		this.time=time;
		this.state=state;
		this.head=heads;
		this.img =imgs;
		this.name=names;
		this.text=text;

	}
	/////////实际上用到的 id,头像，图片，名字，内容，时间，状态,评论条数
	public  OldObjectMessage(long account,Object heads,Object imgs,String names,String text,String time,int state,int comnum,int messageID) {

		super();
		this.account=account;
		this.time=time;
		this.state=state;
		this.head=heads;
		this.img =imgs;
		this.name=names;
		this.text=text;
		this.comnum=comnum;
		this.messageID = messageID;

	}
	public void setState(int state)
	{
		this.state = state;
	}
	
	public Object getimg()
	{
		return img;
	}
	public String getname()
	{
		return name;
	}
	public String gettext()
	{
		return text;
	}
	public int gettype()
	{
		return type;
	}
	public Object gethead()
	{
		return head;
	}
	
	public int getstate()
	{
		return state;
	}
	public String gettime()
	{
		return time;
	}
	public long getaccount()
	{
		return account;
	}
	public int getcomnum()
	{
		return comnum;
	}
	public int getMessageID()
	{
		return messageID;
	}
	}

