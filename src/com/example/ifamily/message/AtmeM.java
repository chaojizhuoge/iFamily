package com.example.ifamily.message;

public class AtmeM {
String name;
String time;
String typeM;
long account;
int type;//1�ǿռ䣬2����Ʒ��3��������4�����ʣ�
public AtmeM(long account,String name,String time,int type)
{
	this.account=account;
	this.name=name;
	this.time=time;
	this.type=type;
	
	switch(type)
	{
	case 1:
		typeM="�ظ���������Ŀռ���Ϣ";
		break;
	case 2:
		typeM="�ظ������������Ʒ������Ϣ";
		break;
	case 3:
		typeM="�ظ����������������Ϣ";
		break;
	case 4:
		typeM="�ظ��������������";
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
