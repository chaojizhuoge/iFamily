package com.example.ifamily.message;

public class HolesSpeakM {
	Object head;
	String name;
	String mes;
	long account;
	String time;
	Object img;
	int comnum;
	int messageID;
	int state;

	public  HolesSpeakM(Object heads,String names,String mes) {

		super();

		this.head=heads;
		this.mes =mes;
		this.name=names;

	}
	//  �˺ţ�ͷ���ǳƣ���Ϣ��ʱ�䣬ͼƬ��������
	public  HolesSpeakM(long account,Object heads,String names,String mes,String time,int comnum,int messageID,int state) {

		super();
		this.account=account;
		this.comnum=comnum;
		this.img=img;
		this.time=time;

		this.head=heads;
		this.mes =mes;
		this.name=names;
		this.messageID = messageID;
		this.state = state;

	}
	public String getname()
	{
		return name;
	}
	public String getmes ()
	{
		return mes ;
	}
	public Object gethead()
	{
		return head;
	}
	public Object getimg()
	{
		return img;
	}
	public int getcomnum()
	{
		return comnum;
	}
	public String gettime ()
	{
		return time;
	}
	public long getaccount()
	{
		return account;
	}
	public int getmessageID()
	{
		return messageID;
	}
	public int getstate()
	{
		return state;
	}
}


