package com.example.ifamily.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.activity.MyPicActivity;
import com.example.ifamily.activity.OldObjectCommentActivity;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.TextViewM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ifamily.message.*;
import com.example.ifamily.utils.FontManager;

public class OldItemAdapter extends BaseAdapter {  
    private Context mContext;  
    private List<OldObjectMessage> message = new ArrayList<OldObjectMessage>();  
    private RelativeLayout  rr;
    
    
    public OldItemAdapter(Context c,List<OldObjectMessage> messages) {  
        mContext = c; 
        message=messages;
    }  
  


  
    public int getCount() {  
        return message.size();  
    }  
  
    public Object getItem(int position) {  
        return message.get(position);  
    }  
  
    public long getItemId(int position) {  
        return 0;  
    }  
  
    // create a new ImageView for each item referenced by the Adapter  
    @SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder = null;
    	
   
    	if(convertView==null)
    	{
        	// if it's not recycled, initialize some attributes  
        holder=new ViewHolder();
        convertView=LayoutInflater.from(mContext).inflate(R.layout.o_old_item, null);
        holder.hd= (RoundImageView)convertView.findViewById(R.id.head);
        holder.item=(ImageView)convertView.findViewById(R.id.img);
        holder.name=(TextViewM)convertView.findViewById(R.id.name);
        holder.time=(TextView)convertView.findViewById(R.id.addtext);
        holder.state=(TextView)convertView.findViewById(R.id.tv_state);
        holder.text=(TextView)convertView.findViewById(R.id.text);
        holder.comnum=(TextView)convertView.findViewById(R.id.comnum);
        rr=(RelativeLayout)convertView.findViewById(R.id.allitem);
        convertView.setTag(holder);}
    	else{
    		holder=(ViewHolder)convertView.getTag();
    	}

		//FontManager.changeFonts(rr, mContext);
     /*  LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT,height*4/5);
        holder.item.setLayoutParams(mParams);
        holder.item.setMaxHeight(height*4/5);
        holder.item.setMaxWidth(width*3/8);
        holder.item.setAdjustViewBounds(true);*/
        holder.item.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(mContext,MyPicActivity.class);
				//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
				intent.putExtra("icon",(Bitmap)message.get(position-1).getimg());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
				 mContext.startActivity(intent);	
			}
			
		});
    	rr.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	            Intent	 intent = new Intent(arg0.getContext(),OldObjectCommentActivity.class);
	            intent.putExtra("icon",(Bitmap)message.get(position).gethead());
				intent.putExtra("id", message.get(position).getaccount());
				intent.putExtra("name", message.get(position).getname());
				intent.putExtra("text", message.get(position).gettext());
				intent.putExtra("time", message.get(position).gettime());
				intent.putExtra("messageId", message.get(position).getMessageID());
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
	            mContext.startActivity(intent);
			}
    		
    	});
        
    	holder.comnum.setText(message.get(position).getcomnum()+"条评论");
    	
        holder.hd.setImageBitmap((Bitmap)message.get(position).gethead());
        holder.hd.setidandtype(message.get(position).getaccount(), 1);       
        holder.time.setText(message.get(position).gettime());
        holder.text.setText(message.get(position).gettext());      
        holder.item.setImageBitmap((Bitmap)message.get(position).getimg());
        holder.name.setText(message.get(position).getname());
        holder.name.setidandtype(message.get(position).getaccount(), 1);
        if(message.get(position).getstate()==1)
        {
        holder.state.setBackground(mContext.getResources().getDrawable(R.drawable.cornerview3));
        holder.state.setText("待送出");
        
        }else {
            holder.state.setBackground(mContext.getResources().getDrawable(R.drawable.cornerview));
            holder.state.setText("已送出");
        }
        
        
        return convertView;
    }

    static class ViewHolder {		
		 RoundImageView hd;
		 ImageView item;
		 TextView time,state,text,comnum;	
		TextViewM name;
		}


}  
