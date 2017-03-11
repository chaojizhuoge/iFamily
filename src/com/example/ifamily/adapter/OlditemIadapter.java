package com.example.ifamily.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

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
import android.widget.Toast;

import com.example.ifamily.message.*;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OlditemIadapter extends BaseAdapter {  
    private Context mContext;  
    private List<OldObjectMessage> message = new ArrayList<OldObjectMessage>();  
    private RelativeLayout  rr;
    
    public OlditemIadapter(Context c,List<OldObjectMessage> messages) {  
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
    	final ViewHolder holder;
    	
   
    	if(convertView==null)
    	{
        	// if it's not recycled, initialize some attributes  
        holder=new ViewHolder();
        convertView=LayoutInflater.from(mContext).inflate(R.layout.old_object_i, null);
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
    	
     /*  LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT,height*4/5);
        holder.item.setLayoutParams(mParams);
        holder.item.setMaxHeight(height*4/5);
        holder.item.setMaxWidth(width*3/8);
        holder.item.setAdjustViewBounds(true);*/
		//FontManager.changeFonts(rr, mContext);
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
    	holder.state.setOnClickListener(new OnClickListener(){

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(message.get(position).getstate()==1)
		        {

	            	
	            	try {
	        			
	        			RequestParams params = new RequestParams();
	        			
	        			params.put("messageId", String.valueOf(message.get(position).getMessageID()));
	        			//params.put("uname", context.getSharedPreferences("user",0).getString("username", ""));
	        			//params.put("friends", Friends);
	        			//params.put("startTime", sdate);
	        			//params.put("text", text.getText().toString());
	        			//params.put("atPlace", atPlace.getText().toString());
	        			params.put("requesttype", "5");
	        			//params.put("LikeState", String.valueOf(LikeState));
	        			//params.put("addtype", "3");
	        			String url = "http://103.31.241.201:8080/IFamilyServer/OldObjectServlet";

	        			AsyncHttpClient client = new AsyncHttpClient();
	        			client.post(url, params, new AsyncHttpResponseHandler() {
	        				@Override
	        				public void onSuccess(int statusCode, Header[] headers,
	        						byte[] responseBody) {
	        					try {
	        						if (statusCode == 200) {

	        							//Toast.makeText(Setgroup.this, "家庭成功创建！", Toast.LENGTH_SHORT)
	        							//		.show();
	        							holder.state.setBackground(mContext.getResources().getDrawable(R.drawable.cornerview));
	        				            holder.state.setText("已送出");
	        				            message.get(position).setState(2);
	        							//Zonelistadapter.this.notifyDataSetChanged();
	        							
	        						} else {
	        							Toast.makeText(mContext,
	        									"网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();

	        						}
	        					} catch (Exception e) {
	        						// TODO Auto-generated catch block
	        						e.printStackTrace();
	        					}
	        				}

	        				@Override
	        				public void onFailure(int statusCode, Header[] headers,
	        						byte[] responseBody, Throwable error) {
	        					Toast.makeText(mContext,
	        							"网络访问异常，错误码  > " + statusCode, 0).show();

	        				}
	        			});

	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	  
	            
					
				
		        
		        }else {
		            
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

			}
    		
    	});
    	
        holder.hd.setImageBitmap((Bitmap)message.get(position).gethead());
        holder.hd.setidandtype(message.get(position).getaccount(), 1);       
        holder.time.setText(message.get(position).gettime());
        holder.text.setText(message.get(position).gettext());      
        holder.item.setImageBitmap((Bitmap)message.get(position).getimg());
        holder.name.setText(message.get(position).getname());
        holder.name.setidandtype(message.get(position).getaccount(), 1);
        holder.comnum.setText(message.get(position).getcomnum()+"条评论");
        if(message.get(position).getstate()==1)
        {
        holder.state.setBackground(mContext.getResources().getDrawable(R.drawable.cornerview3));
        
        holder.state.setText("确认送出");
        
        }else {
            holder.state.setBackground(mContext.getResources().getDrawable(R.drawable.cornerview));
            holder.state.setText("已送出");
            
        }
        
        
        return convertView;
    }

    static class ViewHolder {		
		 public TextView comnum;
		RoundImageView hd;
		 ImageView item;
		 TextView time,state,text;	
		TextViewM name;
		}


}  
