package com.example.ifamily.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ifamily.R;
import com.example.ifamily.message.HolesAtmeM;
import com.example.ifamily.message.HolesWishM;
import com.example.ifamily.mywidget.RoundImageView;

public class HolesAtmeAdapter extends BaseAdapter{
	private Context context;
	private List<HolesAtmeM> messages;
	public HolesAtmeAdapter(Context contexts,List<HolesAtmeM> messages) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.holes_atme_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.ask_name);		
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.ask_head);
			holder.mes=(TextView)convertView.findViewById(R.id.ask_questions);	
			convertView.setTag(holder);}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.iv_l.setImageResource((Integer)messages.get(position).gethead());
			holder.mes.setText(messages.get(position).getmes());
			holder.name.setText(messages.get(position).getname());
		 return convertView;
	}

	
	static class ViewHolder {
		 TextView name;
		 TextView mes;	
		 RoundImageView iv_l;		
		}
}
