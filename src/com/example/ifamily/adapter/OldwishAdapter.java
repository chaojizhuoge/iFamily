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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.ifamily.R;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.message.OldwishM;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.utils.FontManager;

public class OldwishAdapter extends BaseAdapter {
    private Context mContext;  
    private List<OldwishM> message = new ArrayList<OldwishM>();  
    private RelativeLayout  rr;
    
    public OldwishAdapter(Context c,List<OldwishM> messages) {  
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
        convertView=LayoutInflater.from(mContext).inflate(R.layout.old_wish_item, null);
        holder.hd= (RoundImageView)convertView.findViewById(R.id.head);
        holder.name=(TextViewM)convertView.findViewById(R.id.name);
        holder.time=(TextView)convertView.findViewById(R.id.addtext);
 
        holder.text=(TextView)convertView.findViewById(R.id.text);
  
        rr=(RelativeLayout)convertView.findViewById(R.id.allitem);
        convertView.setTag(holder);}
    	else{
    		holder=(ViewHolder)convertView.getTag();
    	}
    	
     /*  LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT,height*4/5);
        holder.item.setLayoutParams(mParams);
        holder.item.setMaxHeight(height*4/5);
        holder.item.setMaxWidth(width*3/8);
        holder.item.setAdjustViewBounds(true);*/
	//	LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
	//	FontManager.changeFonts(all, mContext);
    	rr.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	            Intent	 intent = new Intent(arg0.getContext(),ZoneCommentActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            mContext.startActivity(intent);
			}
    		
    	});
        
    
        holder.hd.setImageResource((Integer)message.get(position).gethead());
        holder.hd.setidandtype(message.get(position).getaccount(), 1);       
        holder.time.setText(message.get(position).gettime());
        holder.text.setText(message.get(position).gettext());      
        holder.name.setText(message.get(position).getname());
        holder.name.setidandtype(message.get(position).getaccount(), 1);

        
        
        return convertView;
    }

    static class ViewHolder {		
		 RoundImageView hd;
		 TextView time,text;	
		TextViewM name;
		}


}
