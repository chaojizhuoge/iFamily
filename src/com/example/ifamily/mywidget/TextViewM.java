package com.example.ifamily.mywidget;

import com.example.ifamily.R;
import com.example.ifamily.activity.GroupmessageActivity;
import com.example.ifamily.activity.Login;
import com.example.ifamily.activity.Privatemessage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TextViewM extends TextView{

	Context context;
	Intent it;
	long id=0;
	int type=0;
	 public  TextViewM (Context context) {  
	        super(context);
	        this.context=context;
	        setOnclickListener();
	    }  
	  
	    public  TextViewM (Context context, AttributeSet attrs) {  
	        super(context, attrs); 
	        this.context=context;
	        setOnclickListener();
	    }  
	  
	    public  TextViewM (Context context, AttributeSet attrs,  
	            int defStyle) {  
	        super(context, attrs, defStyle);
	        this.context=context;
	        setOnclickListener();
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
	    	setOnClickListener(new OnClickListener()
	    	{

				@Override
				public void onClick(View arg0) {
					switch(type)
					{
					case 0:
						break;
					case 1:
					it=new Intent(context,Privatemessage.class);
					it.putExtra("privateAcct",id);
					it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					context.startActivity(it);
					break;
					case 2:
						it=new Intent(context,GroupmessageActivity.class);
						it.putExtra("groupId",Integer.parseInt(String.valueOf(id)));
						it.putExtra("see", false);
						it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
						context.startActivity(it);
						break;
						default:break;
					}
				}
	    		
	    	}
	    	);
	    }
	   public void setidandtype(long id,int type)//1的时候是个人，2的时候是团
	   {
		   this.id=id;
		   this.type=type;
	   }
	    public int gettype()
	    {
	    	return type;
	    }
  
}
