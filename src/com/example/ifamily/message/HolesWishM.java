package com.example.ifamily.message;

public class HolesWishM {
	Object head;
	String name;
	String wish;
	long account;
	String time;
	int comnum;
	int messageID;

	public  HolesWishM(Object heads,String names,String wishes) {

		super();

		this.head=heads;
		this.wish =wishes;
		this.name=names;

	}
	public  HolesWishM(long account,Object heads,String names,String wishes,String time,int comnum,int messageID) {

		super();
		this.account=account;
		this.time=time;
		this.comnum=comnum;
		this.head=heads;
		this.wish =wishes;
		this.name=names;
		this.messageID = messageID;

	}
	public String getname()
	{
		return name;
	}
	public String getwish ()
	{
		return wish ;
	}
	public Object gethead()
	{
		return head;
	}
	public long getaccount()
	{
		return account;
	}
	public String gettime()
	{
		return time;
	}
	public int getcomnum()
	{
		return comnum;
	}
	public int getmessageId()
	{
		return messageID;
	}
}



