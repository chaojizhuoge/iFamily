package com.example.ifamily.message;

public class DayDayM {
private String time;// 在日子里面是生日或者节日，在diy里是送出的日期
private String name;
private long fromaccount;
private long toaccount;
private int type;//1是收到，2是送出  在日子中是用来分辨节日和生日的
private int messageID;
//日子中的构造函数
public DayDayM(String time,String name,long fromaccount,int type,int messageID)
{
	this.time=time;
	this.name=name;
	this.fromaccount=fromaccount;
	this.type=type;
	this.messageID = messageID;
	
}
//diy中的构造函数
public DayDayM(String time,String name,long fromaccount,int type)
{
	this.time=time;
	this.name=name;
	this.fromaccount=fromaccount;
	this.type=type;

	
}

public String gettime()
{
	return time;
}
public String getname()
{
	return name ;
}
public long getfromaccount()
{
	return fromaccount;
}
public long gettoaccount()
{
	return toaccount;
}

public int gettype()
{
	return type;
}
public int getmessageID()
{
	return messageID;}


}
