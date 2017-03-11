package com.example.ifamily.message;

public class GroupLMessage {
	Object img;
	String name;
	int id;
	String text;
	String time;

	public GroupLMessage(Object imgs,String names) {

		super();

		this.img =imgs;

		this.name=names;
	}
	public GroupLMessage(int ids,Object imgs,String names) {

		super();
		
		this.id = ids;

		this.img =imgs;

		this.name=names;
	}
	public GroupLMessage(int ids,Object imgs,String names,String texts)
	{
	super();
	this.id=ids;
	this.img =imgs;
	this.name=names;
	this.text=texts;
	
	}
	public GroupLMessage(int ids,Object imgs,String names,String texts,String times)
	{
	super();
	this.id=ids;
	this.img =imgs;
	this.name=names;
	this.text=texts;
	this.time=times;
	
	}
	public Object getimg()
	{
		return img;
	}
	public String getname()
	{
		return name;
	}
	public int getid()
	{
		return id;
	}
	public String gettext()
	{
		return text;
		
	}
	public String gettime()
	{
	
		return time;
	}
	
	public void settext(String text)
	{
		this.text = text;
	}
	
	public void settime(String time)
	{
		this.time = time;
	}


}
