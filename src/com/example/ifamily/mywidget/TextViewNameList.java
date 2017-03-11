package com.example.ifamily.mywidget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.ifamily.R;
import com.example.ifamily.message.HeadM;

public class TextViewNameList extends TextView{

	private Context context;
	private List<HeadM> names;
	private SpannableString temp;
	private String endstring="";
	 public  TextViewNameList (Context context) {  
	        super(context);
	        this.context=context;
	    }  
	 public  TextViewNameList (Context context,List<HeadM> names) {  
	        super(context);
	        this.context=context;
	        this.names=names;
	    }  
	    public  TextViewNameList (Context context, AttributeSet attrs) {  
	        super(context, attrs); 
	        this.context=context;
	      
	    }  
	  
	    public TextViewNameList (Context context, AttributeSet attrs,  
	            int defStyle) {  
	        super(context, attrs, defStyle);
	        this.context=context;
	        
	    }  
	  
	    @Override  
	    public boolean isFocused() {  
	        return true;  
	    }  

	    @Override 
        protected void onDraw(Canvas canvas) { 
                // TODO Auto-generated method stub 
                super.onDraw(canvas); 
               setTextColor(getResources().getColor(R.color.blue));
        } 
	    protected void setOnclickListener()
	    {
	    	
	    }

	    public void setnamelist(List<HeadM> names)
	    {
	    	this.names=names;
	    	String alltext = "";
	        for(int i=0;i<names.size();i++)
	        {
	        	String temp=new String(names.get(i).getname());
	        	alltext=alltext+temp+",";
	        }
	        temp=null;
	        temp= new SpannableString(alltext+endstring);  
	        int start,end,length;
	        start=0;
	        end=0;
	        length=0;
	        for(int i=0;i<names.size();i++)
	        {
	        	if(i==0){
	        	start=start+length;}
	        	else{start=start+length+1;}
	        	length=names.get(i).getname().length();
	        	end=length+start;
	        	temp.setSpan(new ClickspanIntent(1,names.get(i)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	        	temp.setSpan(new ForegroundColorSpan(R.color.blue), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	        	if(i==names.size()-1){
	        		temp.setSpan(new ForegroundColorSpan(R.color.black), end+1,end+1+endstring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	        	}
	        }
	        setText(temp);
	        setMovementMethod(LinkMovementMethod.getInstance());  
	    	
	    }
	    public void setendstring(String endstring)
	    {
	    	this.endstring=endstring;
	    }
  
}