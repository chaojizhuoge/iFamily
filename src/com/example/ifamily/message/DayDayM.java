package com.example.ifamily.message;

public class DayDayM {
private String time;// ���������������ջ��߽��գ���diy�����ͳ�������
private String name;
private long fromaccount;
private long toaccount;
private int type;//1���յ���2���ͳ�  ���������������ֱ���պ����յ�
private int messageID;
//�����еĹ��캯��
public DayDayM(String time,String name,long fromaccount,int type,int messageID)
{
	this.time=time;
	this.name=name;
	this.fromaccount=fromaccount;
	this.type=type;
	this.messageID = messageID;
	
}
//diy�еĹ��캯��
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
