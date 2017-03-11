package com.example.ifamily.adapter;

import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.message.AtmeM;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.utils.FontManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AtmeAdapter extends BaseAdapter{
	private Context context;
	private List<AtmeM> messages;
	public AtmeAdapter(Context contexts,List<AtmeM> messages) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.atme_item, null);
			holder.name=(TextViewM)convertView.findViewById(R.id.name);		
			holder.time=(TextView)convertView.findViewById(R.id.time);		
			holder.typeM=(TextView)convertView.findViewById(R.id.type);	
			convertView.setTag(holder);}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
	//	LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//FontManager.changeFonts(all, context);
	
		    holder.time.setText(messages.get(position).gettime());
			holder.typeM.setText(messages.get(position).gettypeM());
			holder.name.setText(messages.get(position).getname());
		 return convertView;
	}

	
	static class ViewHolder {
		 TextViewM name;
		 TextView typeM;
		 TextView time;	
		}
}
