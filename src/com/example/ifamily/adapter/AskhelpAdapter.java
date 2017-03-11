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
import com.example.ifamily.activity.StorycommentActivity;
import com.example.ifamily.activity.WishCommentActivity;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.message.QuestionM;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.utils.FontManager;

public class AskhelpAdapter  extends BaseAdapter{
	private Context context;
	private List<QuestionM> messages;
	public AskhelpAdapter(Context contexts,List<QuestionM> messages) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.askhelp_item, null);
			holder.name=(TextViewM)convertView.findViewById(R.id.name);		
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.head);
			holder.question=(TextView)convertView.findViewById(R.id.text);	
			holder.iv=(ImageView)convertView.findViewById(R.id.iv_comment);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.comnum=(TextView)convertView.findViewById(R.id.comnum);
		    holder.all=(LinearLayout)convertView.findViewById(R.id.all);
			convertView.setTag(holder);}
			else {
				holder = (ViewHolder) convertView.getTag();
			}

		
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent	 intent = new Intent(arg0.getContext(),StorycommentActivity.class);
	            intent.putExtra("icon",(Bitmap)messages.get(position).gethead());
				intent.putExtra("id", messages.get(position).getaccount());
				intent.putExtra("name", messages.get(position).getname());
				intent.putExtra("text", messages.get(position).getquestion());
				intent.putExtra("time", messages.get(position).gettime());
				intent.putExtra("messageId", messages.get(position).getMessageId());
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
	            context.startActivity(intent);
			}
			
		});
		   // FontManager.changeFonts(holder.all, context);
			holder.iv_l.setImageBitmap((Bitmap)messages.get(position).gethead());
			holder.time.setText(messages.get(position).gettime());
			holder.comnum.setText(messages.get(position).getcomnum()+"Ìõ»Ø¸´");
			holder.iv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent	 intent = new Intent(arg0.getContext(),StorycommentActivity.class);
		            intent.putExtra("icon",(Bitmap)messages.get(position).gethead());
					intent.putExtra("id", messages.get(position).getaccount());
					intent.putExtra("name", messages.get(position).getname());
					intent.putExtra("text", messages.get(position).getquestion());
					intent.putExtra("time", messages.get(position).gettime());
					intent.putExtra("messageId", messages.get(position).getMessageId());
		            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
		            context.startActivity(intent);
				}
				
			});
			holder.question.setText(messages.get(position).getquestion());
			holder.name.setText(messages.get(position).getname());
		 return convertView;
	}

	
	static class ViewHolder {
		 TextViewM name;
		 TextView question,time,comnum;	
		 LinearLayout all;
		 RoundImageView iv_l;	
		 ImageView iv;
		}
}
