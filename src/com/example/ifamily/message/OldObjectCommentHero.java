package com.example.ifamily.message;

import java.util.List;

import android.graphics.Bitmap;

public class OldObjectCommentHero {
	 Object head;
	 Object hisimg;
	 String name;
	 String time;
	 String content;
	 int comnum;//评论的条数
	 int num;//赞的个数
	 long id;
	  List< HeadM> names;// 赞的人
	  int messageId;
	  boolean isliked;

public OldObjectCommentHero(long id,Object head,Object hisimg,String name,String time,String content,int comnum,int messageId){
	 this.head=head;
	 this.id=id;
	 this.name=name;
	 this.time=time;
	 this.hisimg=hisimg;
	 this.content=content;
	 this.comnum=comnum;
	 this.messageId = messageId;
}
public OldObjectCommentHero(long id,Object head,String name,String time,String content){
	 this.id=id;
	 this.head=head;
	 this.name=name;
	 this.time=time;
	 this.content=content;
}
public Object gethead()
{
	return head;
}
public Object gethisimg()
{
	return hisimg;
}
public String gettime()
{
	return time;
}
public String getname()
{
	return name;
}
public String getcontent()
{
	return content;
}
public long getid()
{
	return id;
}
public int getnum()
{
	return num;
}
public int getcomnum()
{
	return comnum;
}
public  List< HeadM> getnames()
{
	return names;
}
public int getMessageId()
{
	return messageId;
}

public boolean getIsLiked()
{
	return isliked;
}

public void setNum(int num)
{
	this.num=num;
}

public void setComnum(int comnum)
{
	this.comnum = comnum;
}

public void setMessageId(int messageId)
{
	this.messageId = messageId;
}

public void setIsLiked(boolean isLiked)
{
	this.isliked = isLiked;
}
public void setNames(List<HeadM> names)
{
	this.names = names;
}
public void setHisimg(Object hisimg)
{
	this.hisimg = hisimg;
}
}