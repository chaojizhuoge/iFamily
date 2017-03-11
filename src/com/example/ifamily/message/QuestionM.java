package com.example.ifamily.message;

public class QuestionM {
	Object head;
	String name;
	String question;
	long account;
	String time;
	int comnum;

	int messageID;
	public  QuestionM(Object heads,String names,String questions) {

		super();

		this.head=heads;
		this.question =questions;
		this.name=names;

	}
	public  QuestionM(long account,Object heads,String names,String time,String questions,int comnum,int messageID) {

		super();
		this.account=account;
		this.time=time;
		this.comnum=comnum;

		this.head=heads;
		this.question =questions;
		this.name=names;
		this.messageID = messageID;
	}
	public String getname()
	{
		return name;
	}
	public String getquestion ()
	{
		return question ;
	}
	public Object gethead()
	{
		return head;
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
	public int getMessageId()
	{
		return messageID;
	}
}



