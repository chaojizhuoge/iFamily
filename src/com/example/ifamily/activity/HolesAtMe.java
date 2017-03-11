package com.example.ifamily.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.adapter.HolesAtmeAdapter;
import com.example.ifamily.adapter.HolesWishAdapter;
import com.example.ifamily.message.HolesAtmeM;
import com.example.ifamily.message.HolesWishM;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HolesAtMe extends Fragment{


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater
				.inflate(R.layout.holes_atme, container, false);
		return view;}
	
	
	 ListView lv;
		
	List<HolesAtmeM> messages=new ArrayList<HolesAtmeM>();
		 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
		 initview();
	
	}
	
	protected void initview()
	{
		lv=(ListView)getView().findViewById(R.id.h_lv);						
		initdata();
		
	}
	protected void initdata()
	{
		HolesAtmeM oom1=new HolesAtmeM(R.drawable.defalt_head,"kkkkk","1111");
		HolesAtmeM oom2=new HolesAtmeM(R.drawable.defalt_head,"kkkkk","2111");
		HolesAtmeM oom3=new HolesAtmeM(R.drawable.defalt_head,"kkkkk","3111");
		HolesAtmeM oom4=new HolesAtmeM(R.drawable.defalt_head,"kkkkk","4111");
		HolesAtmeM oom5=new HolesAtmeM(R.drawable.defalt_head,"kkkkk","5111");
		messages.add(oom1);
		messages.add(oom2);
		messages.add(oom3);
		messages.add(oom4);
		messages.add(oom5);
		HolesAtmeAdapter oia=new HolesAtmeAdapter(this.getActivity(),messages);
		lv.setAdapter(oia);
		
		
	}
	
	
	
	
	
}


