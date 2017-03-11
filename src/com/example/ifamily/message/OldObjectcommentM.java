package com.example.ifamily.message;

public class OldObjectcommentM {
	Object img;
	String name;
	String text;
	String time;
	long id;
	public OldObjectcommentM(Object imgs,String names, String texts,String time) {

		super();
		this.img =imgs;
		this.time=time;
		this.name=names;
		this.text = texts;	//可以再这里设置type

	}
	public OldObjectcommentM(long id,Object imgs,String names, String texts,String time) {

		super();
		this.img =imgs;
		this.time=time;
		this.name=names;
		this.id=id;
		this.text = texts;	//可以再这里设置type

	}
	public OldObjectcommentM(String names, String texts)
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
	public String gettime()
	{
		return time;
	}
	public long getid()
	{
		return id;
		
	}

}


