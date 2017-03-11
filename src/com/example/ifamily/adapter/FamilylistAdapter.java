package com.example.ifamily.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifamily.R;
import com.example.ifamily.adapter.Zonelistadapter.ViewHolder;
import com.example.ifamily.message.AddfriendMessage;
import com.example.ifamily.message.GroupLMessage;
import com.example.ifamily.message.Zonemessage;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FontManager;

public class FamilylistAdapter extends BaseAdapter{
	private Context context;
	private List<GroupLMessage> messages;
	private Object[][] ids;
	private boolean fuck = false;
	public FamilylistAdapter(Context contexts,List<GroupLMessage> messages) {
		 super();
		 this.context = contexts;
		 this.messages =messages;
		 this.ids=new Object[messages.size()][3];
		 for(int i=0;i<messages.size();i++)
		 {
		 ids[i][2]=messages.get(i).getname();
		 ids[i][0]=messages.get(i).getid();
		 if(ids[i][0].getClass().equals(Integer.class))
			 ids[i][0] = Long.parseLong(ids[i][0].toString());
		 ids[i][1]=0;
		 }
		}
	public FamilylistAdapter(Context contexts,List<GroupLMessage> messages,boolean fuck) {
		 super();
		 this.context = contexts;
		 this.messages =messages;
		 this.ids=new Object[messages.size()][3];
		 for(int i=0;i<messages.size();i++)
		 {
		 ids[i][2]=messages.get(i).getname();
		 ids[i][0]=messages.get(i).getid();
		 if(ids[i][0].getClass().equals(Integer.class))
			 ids[i][0] = Long.parseLong(ids[i][0].toString());
		 ids[i][1]=0;
		 }
		 this.fuck = fuck;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.p_setgroup_friends_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.tv_p_setgroup_names);		
			holder.check = (CheckBox)convertView.findViewById(R.id.cb_che);	
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.gi_friendimg);			
			convertView.setTag(holder);}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
		if(!fuck)
		holder.check.setVisibility(View.GONE);
		holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	            @Override
	            public void onCheckedChanged(CompoundButton buttonView, 
	                    boolean isChecked) { 
	                // TODO Auto-generated method stub 
	                if(isChecked){ 
	                	ids[position][1]=1;	                		                    
	                }else{ 
	                 ids[position][1]=0;
	                } 
	            } 
	        }); 
	//	LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//FontManager.changeFonts(all, context);
			holder.iv_l.setImageBitmap((Bitmap)messages.get(position).getimg());
			holder.name.setText(messages.get(position).getname());
		 return convertView;
	}

	
	static class ViewHolder {
		 TextView name;	
		 CheckBox check;
		 RoundImageView iv_l;		
		}
	public List<Long> getids()
	{
		List<Long> allids=new ArrayList<Long>();
		for(int i=0;i<messages.size();i++)
		{
			if((Integer)ids[i][1]==1) allids.add((Long)ids[i][0]);
		}
	
		return allids;
	}
	
	public List<String> getnames()
	{
		List<String> allids=new ArrayList<String>();
		for(int i=0;i<messages.size();i++)
		{
			if((Integer)ids[i][1]==1) allids.add((String)ids[i][2]);
		}
	
		return allids;
	}
	
	
	public void resetstate()
	{
	
		for(int i=0;i<messages.size();i++)
		{
			ids[i][1]=0;
		}
	
		
	}
}
