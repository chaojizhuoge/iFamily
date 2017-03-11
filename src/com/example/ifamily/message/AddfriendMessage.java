package com.example.ifamily.message;

import com.example.ifamily.PushApplication;

import android.R;
import android.graphics.BitmapFactory;

public class AddfriendMessage {
	Object img;
	String name;
	long id;

	public AddfriendMessage(Object imgs,String names) {

		super();

		this.img =imgs;
		if(imgs.getClass().equals(Integer.class))
			this.img = BitmapFactory.decodeResource( PushApplication.getInstance().getResources(), (Integer)this.img);

		this.name=names;
	}
	public AddfriendMessage(long ids,Object imgs,String names)
	{
	super();
	this.id=ids;
	this.img =imgs;
	this.name=names;
	}
	public Object getimg()
	{
		return img;
	}
	public String getname()
	{
		return name;
	}
	public long getid()
	{
		return id;
	}


}
