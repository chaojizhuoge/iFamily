package com.example.ifamily.adapter;

import java.util.List;

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
import android.widget.TextView;

import com.example.ifamily.R;
import com.example.ifamily.activity.TcCommentActivity;
import com.example.ifamily.activity.WishCommentActivity;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.message.HolesSpeakM;
import com.example.ifamily.message.HolesWishM;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.utils.FontManager;

public class HolesSpeakAdapter extends BaseAdapter{
	private Context context;
	private List<HolesSpeakM> messages;
	public HolesSpeakAdapter(Context contexts,List<HolesSpeakM> messages) {
		 super();
		 this.context = contexts;
		 this.messages =messages;
		}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return messages.size();
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return messages.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {  
			holder=new ViewHolder();	
			convertView=LayoutInflater.from(context).inflate(R.layout.holes_speak_item, null);
			holder.name=(TextViewM)convertView.findViewById(R.id.name);		
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.head);
			holder.mes=(TextView)convertView.findViewById(R.id.text);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.comnum=(TextView)convertView.findViewById(R.id.comnum);
			holder.iv=(ImageView)convertView.findViewById(R.id.img);
			holder.comment=(ImageView)convertView.findViewById(R.id.iv_comment);
			convertView.setTag(holder);}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
		
		if(messages.get(position).getstate()==1)
		{
			holder.iv_l.setVisibility(View.GONE);
			holder.name.setText("匿名用户");
			
		}
		else{
			holder.iv_l.setImageBitmap((Bitmap)messages.get(position).gethead());
			holder.iv_l.setidandtype(messages.get(position).getaccount(), 1);
			holder.name.setText(messages.get(position).getname());
		}
		
	//	LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
	//	FontManager.changeFonts(all, context);
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent	 intent = new Intent(arg0.getContext(),TcCommentActivity.class);
				intent.putExtra("state",messages.get(position).getstate());
	            intent.putExtra("icon",(Bitmap)messages.get(position).gethead());
				intent.putExtra("id", messages.get(position).getaccount());
				intent.putExtra("name", messages.get(position).getname());
				intent.putExtra("text", messages.get(position).getmes());
				intent.putExtra("time", messages.get(position).gettime());
				intent.putExtra("messageId", messages.get(position).getmessageID());
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
	            context.startActivity(intent);
			}
			
		});
			holder.time.setText(messages.get(position).gettime());
			holder.comnum.setText(messages.get(position).getcomnum()+"条评论");
			//holder.iv.setImageResource((Integer)messages.get(position).getimg());
			holder.comment.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent	 intent = new Intent(arg0.getContext(),TcCommentActivity.class);
					intent.putExtra("state",messages.get(position).getstate());
		            intent.putExtra("icon",(Bitmap)messages.get(position).gethead());
					intent.putExtra("id", messages.get(position).getaccount());
					intent.putExtra("name", messages.get(position).getname());
					intent.putExtra("text", messages.get(position).getmes());
					intent.putExtra("time", messages.get(position).gettime());
					intent.putExtra("messageId", messages.get(position).getmessageID());
		            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
		            context.startActivity(intent);
				}
				
			});
			holder.mes.setText(messages.get(position).getmes());
			
		
		 return convertView;
	}

	
	static class ViewHolder {
		 TextView mes,time,comnum;
		 TextViewM name;
		 RoundImageView iv_l;	
		 ImageView iv,comment;
		}
}
