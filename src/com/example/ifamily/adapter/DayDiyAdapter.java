package com.example.ifamily.adapter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifamily.R;
import com.example.ifamily.activity.DayDiyviewActivity;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.message.DayDayM;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.utils.FontManager;

public class DayDiyAdapter extends BaseAdapter {  
    private Context mContext;  
    private List<DayDayM> message = new ArrayList<DayDayM>();  
    
    public DayDiyAdapter(Context c,List<DayDayM> messages) {  
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
    public View getView(int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder = null;
    	
   
    	if(convertView==null)
    	{
        	// if it's not recycled, initialize some attributes  
        holder=new ViewHolder();
        convertView=LayoutInflater.from(mContext).inflate(R.layout.d_day_diy_item, null);
        holder.name=(TextViewM)convertView.findViewById(R.id.name);
        holder.time=(TextView)convertView.findViewById(R.id.time);
        holder.ok=(TextView)convertView.findViewById(R.id.tv_ok);
        convertView.setTag(holder);}
    	else{
    		holder=(ViewHolder)convertView.getTag();
    	}
    	
     /*  LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT,height*4/5);
        holder.item.setLayoutParams(mParams);
        holder.item.setMaxHeight(height*4/5);
        holder.item.setMaxWidth(width*3/8);
        holder.item.setAdjustViewBounds(true);*/
        
    	holder.ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	            Intent	 intent = new Intent(arg0.getContext(),DayDiyviewActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            mContext.startActivity(intent);
			}
    		
    	});
        
    	
		//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//FontManager.changeFonts(all, mContext);
        holder.name.setText(message.get(position).getname());
        if(message.get(position).gettype()==1)
        {
        	 holder.time.setText(message.get(position).gettime()+"À´×Ô");
        	holder.name.setidandtype(message.get(position).getfromaccount(), 1);
       
        }
        else
        {
        	 holder.time.setText(message.get(position).gettime()+"ÔùÓè");
        	 holder.name.setidandtype(message.get(position).gettoaccount(), 1);
        }

        
        return convertView;
    }

    static class ViewHolder {		
		 TextView time,ok;
	     TextViewM name;
	     Long fromaccount,toaccount;
		}


}  
