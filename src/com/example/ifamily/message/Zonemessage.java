package com.example.ifamily.message;

import java.util.List;


public class Zonemessage {
	boolean isliked;
Object img;//ͷ��
String name;
String text;//����
Object hisimg;//�����ͼƬ
String time;//�����ʱ��
String attime;//Ҫȥ��ʱ��
List<HeadM> friends;//һ��ȥ����<id,name>
String atplace;//ȥ�ĵط�
int num;//�� �ĸ���
int comnum;//���۵ĸ���
int type;
long id;
int messageID;
public Zonemessage(Object imgs,String names, String texts) {

	super();
	this.img =imgs;
	this.name=names;
	this.text = texts;	//��������������type

}

//�����۵�
public Zonemessage(Object imgs,String names, String texts,int num,int comnum) {

	super();

	this.img =imgs;
	this.type=1;
	this.name=names;
	this.text = texts;	
	this.comnum=comnum;
	this.num=num;
}
public Zonemessage(long id,String time,Object imgs,String names, String texts,int num,int comnum,int messageID,boolean isliked) {

	super();
	this.id=id;
	this.img =imgs;
	this.type=1;
	this.name=names;
	this.text = texts;	
	this.comnum=comnum;
	this.num=num;
	this.time=time;
	this.messageID = messageID;
	this.isliked = isliked;
}
public Zonemessage(long id,String time,Object imgs,Object hisimg,String names, String texts,int num,int comnum,int messageID,boolean isliked) {

	super();
	this.time=time;
	this.id=id;
	this.img =imgs;
	this.type=1;
	this.hisimg=hisimg;
	this.name=names;
	this.text = texts;	
	this.comnum=comnum;
	this.num=num;
	this.messageID = messageID;
	this.isliked = isliked;
}
//û�����۵�
public Zonemessage(Object imgs,String names, String texts,int num) {

	super();
	this.type=2;

	this.img =imgs;
	this.name=names;
	this.text = texts;	
	this.num=num;
}
public Zonemessage(long id,Object imgs,String names, String texts,int num) {

	super();
	this.type=2;
	this.id=id;
	this.img =imgs;
	this.name=names;
	this.text = texts;	
	this.num=num;
}
public Zonemessage(long id,Object imgs,String names,String time, String texts,String attime,List<HeadM> friends,String atplace,int num,int messageID,boolean isliked) {

	super();
	this.type=2;
	this.time=time;
	this.attime=attime;
	this.atplace=atplace;
	this.friends=friends;
	this.id=id;
	this.img =imgs;
	this.name=names;
	this.text = texts;	
	this.num=num;
	this.messageID = messageID;
	this.isliked = isliked;
}
public Zonemessage(String names, String texts)
{
super();
this.name=names;
this.text=texts;
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
public int getnum()
{
	return num;
}
public void setnum(int num)
{
	this.num = num;
}
public int getcomnum()
{
	return comnum;
}
public long getid()
{
	return id;
}
public Object gethisimg()
{
	return hisimg;
}
public String gettime()
{
	return time;
}
public String getattime()
{
	return attime;
}
public String getatplace()
{
	return atplace;
}
public List<HeadM> getfriends()
{
	return friends;
}
public void setfriends(List<HeadM> friends)
{
	this.friends = friends;
}
public int getMessageID()
{
	return messageID;
}

public boolean getIsLiked()
{
	return isliked;
}

public void setIsLiked(boolean isLiked)
{
	this.isliked = isLiked;
}

}
