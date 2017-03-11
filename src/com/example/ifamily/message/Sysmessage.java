package com.example.ifamily.message;

public class Sysmessage {
String groupname;
int groupid=-1;
String hisname;
long hisid=-1;
int type=0;
String time;
public  Sysmessage(String groupname,String hisnames,int groupid,long hisid,int type) {

	super();
	this.groupname =groupname;
	this.hisname=hisnames;
	this.groupid=groupid;
	this.hisid=hisid;

}
public  Sysmessage(String groupname,String hisnames,int type) {

	super();
	this.groupname =groupname;
	this.hisname=hisnames;
	this.type=type;
	

}
public  Sysmessage(String time,String groupname,String hisnames,int groupid,long hisid,int type) {

	super();
	this.time=time;
	this.groupname =groupname;
	this.hisname=hisnames;
	this.groupid=groupid;
	this.hisid=hisid;
	this.type = type;

}
public String gethisname()
{
	return hisname;
}
public String getgroupname()
{
	return groupname ;
}
public int getgroupid()
{
	return groupid;
	
}
public long gethisid()
{
	return hisid;
	
}
public int gettype()
{
	return type;
	
}
public String gettime()
{
	return time;
}
}



