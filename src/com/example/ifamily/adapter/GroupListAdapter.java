package com.example.ifamily.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ifamily.R;
import com.example.ifamily.message.GroupListM;
import com.example.ifamily.utils.FontManager;

public class GroupListAdapter extends BaseAdapter {
	private Context context;
	private List<GroupListM> Groupmessages;
	public GroupListAdapter(Context contexts,List<GroupListM> messages) {
		 super();
		 this.context = contexts;
		 this.Groupmessages =messages;
		}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return Groupmessages.size();
		
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
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.grouplist_item, null);
			//Zonemessage message = zonemessages.get(position);
			holder.name=(TextView)convertView.findViewById(R.id.item_name);
			convertView.setTag(holder);}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//FontManager.changeFonts(all, context);
			holder.name.setText(Groupmessages.get(position).getname());

		
		 return convertView;
	}
	
	static class ViewHolder {
		TextView name;
		}


}
