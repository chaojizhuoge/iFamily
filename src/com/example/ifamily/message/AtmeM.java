package com.example.ifamily.message;

public class AtmeM {
String name;
String time;
String typeM;
long account;
int type;//1是空间，2是物品，3是树洞，4是问问，
public AtmeM(long account,String name,String time,int type)
{
	this.account=account;
	this.name=name;
	this.time=time;
	this.type=type;
	
	switch(type)
	{
	case 1:
		typeM="回复了您发表的空间信息";
		break;
	case 2:
		typeM="回复了您发表的物品赠送信息";
		break;
	case 3:
		typeM="回复了您发表的树洞信息";
		break;
	case 4:
		typeM="回复了您发表的问问";
		break;
	}
}


public String getname()
{
	return name;
}


public String gettime()
{
	return time;
}
public long getaccount()
{
	return account;
}

public String gettypeM()
{
	return typeM;
}








}
