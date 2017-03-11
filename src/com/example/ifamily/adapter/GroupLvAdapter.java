package com.example.ifamily.adapter;

import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.adapter.GridHeadsAdapter.ViewHolder;
import com.example.ifamily.message.GroupLMessage;
import com.example.ifamily.message.Zonemessage;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FontManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupLvAdapter extends BaseAdapter{
	private Context context;
	private List<GroupLMessage> Groupmessages;
	public GroupLvAdapter(Context contexts,List<GroupLMessage> messages) {
		 super();
		 this.context = contexts;
		 this.Groupmessages =messages;
		}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return (Groupmessages.size()+1);
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Groupmessages.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(position==0)
		{
			convertView=LayoutInflater.from(context).inflate(R.layout.group_item_sys, null);			
			int i=0;
			i=i+1;
			//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//	FontManager.changeFonts(all, context);
		}else
		{

			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.group_item, null);
			//Zonemessage message = zonemessages.get(position);
			holder.name=(TextView)convertView.findViewById(R.id.tv_g_groupname);
			holder.text=(TextView)convertView.findViewById(R.id.tv_g_content);
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.gi_groupimg);
			holder.time=(TextView)convertView.findViewById(R.id.tv_g_time);
			convertView.setTag(holder);

		
		//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//FontManager.changeFonts(all, context);
		Bitmap bitmap = (Bitmap)Groupmessages.get(position-1).getimg();
			holder.iv_l.setImageBitmap(bitmap);
			holder.name.setText(Groupmessages.get(position-1).getname());
			holder.text.setText(Groupmessages.get(position-1).gettext());
			holder.time.setText(Groupmessages.get(position-1).gettime());
		}
		
		 return convertView;
	}
	
	static class ViewHolder {
		TextView name;
		TextView text;
		TextView time;
		RoundImageView iv_l;
		}

}
