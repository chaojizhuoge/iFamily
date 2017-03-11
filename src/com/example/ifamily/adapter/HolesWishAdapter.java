package com.example.ifamily.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifamily.R;
import com.example.ifamily.activity.OldObjectCommentActivity;
import com.example.ifamily.activity.TcCommentActivity;
import com.example.ifamily.activity.WishCommentActivity;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.message.HolesWishM;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FontManager;

public class HolesWishAdapter extends BaseAdapter{
	private Context context;
	private List<HolesWishM> messages;
	public HolesWishAdapter(Context contexts,List<HolesWishM> messages) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.holes_wish_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);		
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.head);
			holder.wish=(TextView)convertView.findViewById(R.id.text);	
			holder.time=(TextView)convertView.findViewById(R.id.time);	
			holder.comnum=(TextView)convertView.findViewById(R.id.comnum);	
			holder.comment=(ImageView)convertView.findViewById(R.id.iv_comment);	
			convertView.setTag(holder);}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
	//	LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
	//	FontManager.changeFonts(all, context);
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent	 intent = new Intent(arg0.getContext(),WishCommentActivity.class);
	            intent.putExtra("icon",(Bitmap)messages.get(position).gethead());
				intent.putExtra("id", messages.get(position).getaccount());
				intent.putExtra("name", messages.get(position).getname());
				intent.putExtra("text", messages.get(position).getwish());
				intent.putExtra("time", messages.get(position).gettime());
				intent.putExtra("messageId", messages.get(position).getmessageId());
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
	            context.startActivity(intent);
			}
			
		});
		holder.iv_l.setImageBitmap((Bitmap)messages.get(position).gethead());
		holder.iv_l.setidandtype(messages.get(position).getaccount(), 1);
		holder.time.setText(messages.get(position).gettime());
		holder.comnum.setText(messages.get(position).getcomnum()+"Ãı∆¿¬€");
		holder.comment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent	 intent = new Intent(arg0.getContext(),WishCommentActivity.class);
	            intent.putExtra("icon",(Bitmap)messages.get(position).gethead());
				intent.putExtra("id", messages.get(position).getaccount());
				intent.putExtra("name", messages.get(position).getname());
				intent.putExtra("text", messages.get(position).getwish());
				intent.putExtra("time", messages.get(position).gettime());
				intent.putExtra("messageId", messages.get(position).getmessageId());
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
	            context.startActivity(intent);
			}
			
		});
		holder.wish.setText(messages.get(position).getwish());
		holder.name.setText(messages.get(position).getname());
		 return convertView;
	}

	
	static class ViewHolder {
		 TextView name,wish,time,comnum;
		 ImageView comment;
		 RoundImageView iv_l;
		 
		}
}
