package com.example.ifamily.mywidget;

import android.graphics.Typeface;

public class Mytext {
private String text;
private int type;//1 ºá 2 ÊúÖ±
private float x=200,y=400;
private int size=100;
private boolean moveable=true;
private int color;
private Typeface tf;
public Mytext(String text,int type)
{
	this.text=text;
	this.type=type;
}
public Mytext(String text,int type,int color,Typeface tf)
{
	this.text=text;
	this.type=type;
	this.color=color;
	this.tf=tf;
}
		
public void setsize(int size)
{
	this.size=size;
}
public void setmoveable(boolean moveable)

{
	this.moveable=moveable;
}
public boolean getmoveable()
{
	return moveable;
}
public void setx(float x)
{
	this.x=x;
}
public float getx()
{
	return x;
}


public void sety(float y)
{
	this.y=y;
}
public float gety()
{
	return y;
}
public int getsize()
{
	return this.size;
}
public int gettype()
{
	return type;
}
public String gettext()
{
	return text;
}
public void setposition(float x,float y)
{
	this.x=x;
	this.y=y;
}

public Typeface gettf()
{
	return tf;
}
public int getcolor()
{
	return color;
}
public void settf(Typeface tf)
{
	this.tf=tf;
	
}
public void setcolor(int color)
{
	this.color=color;
}
public void settype(int type)
{
	this.type=type;
}

}
