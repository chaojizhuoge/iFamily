package com.example.ifamily.mywidget;

import java.util.ArrayList;
import java.util.List;

import com.example.ifamily.utils.Mytypeface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class Moveable extends TextView{

	private float x=100,y=100,width,height;
	private Paint p=new Paint();
	private boolean canmove=false;
	private List<Mytext> textlist=new ArrayList<Mytext>();
	private String text="abcabc";
	private int currenttext=0;
	private int type;
	private int textsize;
	private Typeface tf;
	private Context mContext;
	public void settext(String text,int type)//1是横，2是竖
	{
		this.text=text;
		this.type=type;
	}
	//1是草书，2是方正喵呜，3是哥特体（英文），4是花体，5是楷体，6是行楷
	public void settypeface(int type)
	{
		Mytypeface my=new Mytypeface(mContext);
		tf=my.gettypeface(type);
	}
	public void addtext(Mytext text)//向画布中添加text
	{
		textlist.add(text);
	}
	
	public void settype(int type)
	{
		textlist.get(currenttext).settype(type);
	}
	public void settf(Typeface tf)
	{
		textlist.get(currenttext).settf(tf);
		
	}
	public void setcolor(int color)
	{
		textlist.get(currenttext).setcolor(color);
	}
	
	public void setsize(int size)
	{
		textlist.get(currenttext).setsize(size);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getcurrent()
	{
		return currenttext;
	}
	public void setcurrent(int index)
	{
		this.currenttext=index;
	}
	public void save()//保存最新的text
	{
		textlist.get(currenttext).setmoveable(false);
		//currenttext++;
	}

	public Moveable(Context context) {
		super(context);
		this.mContext=context;
		// TODO Auto-generated constructor stub
	}
	public Moveable(Context context,String text,int type) {
		super(context);
		
		// TODO Auto-generated constructor stub
		this.mContext=context;
		this.text=text;
		this.type=type;
	}
	
	public Moveable(Context context,AttributeSet set) {
		super(context,set);
		this.mContext=context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
	

        if(textlist.size()!=0)
        {

        for(int i=0;i<currenttext+1;i++)
        {
        	p.setColor(textlist.get(i).getcolor());
            p.setAntiAlias(true);//去除锯齿  
            p.setFilterBitmap(true);//对位图进行滤波处理  
            p.setTextSize(textlist.get(i).getsize());
            p.setTypeface(textlist.get(i).gettf());
        if(textlist.get(i).gettype()==1)
        {
		canvas.drawText(textlist.get(i).gettext(),textlist.get(i).getx(),textlist.get(i).gety(),p);
		width=p.measureText(textlist.get(i).gettext());
        }
        else
        {
        	String temp="";
        	int start=0,end=0;
        	
        	for(int k=0;k<textlist.get(i).gettext().length();k++)
        	{
        		start=k;
        		end=k+1;
        		temp=textlist.get(i).gettext().substring(start, end);
        		canvas.drawText(temp,textlist.get(i).getx(),textlist.get(i).gety()+k*textlist.get(i).getsize(),p);
        	}
        	height=textlist.get(i).gettext().length()*textlist.get(i).getsize();
        }
        }
        }
    	super.onDraw(canvas);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
	if(textlist.size()!=0)
	{
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(textlist.get(currenttext).getmoveable()==true){
			if(textlist.get(currenttext).gettype()==1){
				textlist.get(currenttext).setposition(event.getX()-(width/2-textsize/2),event.getY()); 
			}
			if(textlist.get(currenttext).gettype()==2){
				textlist.get(currenttext).setposition(event.getX(),event.getY()-(height/2-textsize/2)); }
			invalidate();}
			break;
		case MotionEvent.ACTION_MOVE:
			
		if(textlist.get(currenttext).getmoveable()==true){
			if(textlist.get(currenttext).gettype()==1){
				textlist.get(currenttext).setposition(event.getX()-(width/2-textsize/2),event.getY()); 
			}
			if(textlist.get(currenttext).gettype()==2){
				textlist.get(currenttext).setposition(event.getX(),event.getY()-(height/2-textsize/2)); }
			invalidate();
	   } 
	    break;
		case MotionEvent.ACTION_UP:
			break;
	    }
		
		
	}
		return true;
	}
}
