package com.example.ifamily.activity;

import com.example.ifamily.R;
import com.example.ifamily.mywidget.Mytypeface;
import com.example.ifamily.utils.FontManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

public class MoreActivity extends Fragment{
	private ImageView ll_present,ll_ask,ll_holes,ll_day;
	private TextView title;
	private Button back;
	private LinearLayout all,ll;
	private Button atme;
	private int width,height;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.more_3,
				container, false);
		return messageLayout;
	}
	public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
		 initview();


	         
	 }
	private void initview()
	{
		
		atme=(Button)getView().findViewById(R.id.btnatme);
		
		 atme.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getView().getContext(),AtMeActivity.class);
				 startActivity(intent);
			
			}
			 
		 });
		ll=(LinearLayout)getView().findViewById(R.id.ll);
		
		ll.setVisibility(View.GONE);
		
		all=(LinearLayout)getView().findViewById(R.id.all);
		Mytypeface tf=new Mytypeface(this.getActivity());
	
		//FontManager.changeFonts(all,this.getActivity());
         DisplayMetrics dm = new DisplayMetrics();   
         dm = this.getResources().getDisplayMetrics();   
         width = dm.widthPixels;   
         height = dm.heightPixels; 
         LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams((width*3)/8,(width*3)/8);
		 title = (TextView) getView().findViewById(R.id.title);
			title.setTypeface(tf.gettypeface(2));
		 back = (Button) getView().findViewById(R.id.back);
		 title.setText("¸ü¶à");
		 back.setVisibility(View.GONE);		
		 ll_present = (ImageView) getView().findViewById(R.id.more_pre);
		 ll_present.setLayoutParams(mParams);
		 ll_present.setMaxHeight(height/3);
		 ll_present.setMaxWidth(width/3);
		 ll_present.setAdjustViewBounds(true);
	     ll_ask = (ImageView) getView().findViewById(R.id.more_ask);
	     ll_ask.setLayoutParams(mParams);
	     ll_ask.setLayoutParams(mParams);
	     ll_ask.setMaxHeight(height/3);
	     ll_ask.setMaxWidth(width/3);
	     ll_ask.setAdjustViewBounds(true);
	     ll_holes = (ImageView) getView().findViewById(R.id.ll_holes);
	     ll_holes.setLayoutParams(mParams);
	     ll_holes.setLayoutParams(mParams);
	     ll_holes.setMaxHeight(height/3);
	     ll_holes.setMaxWidth(width/3);
	     ll_holes.setAdjustViewBounds(true);
	     ll_day=(ImageView) getView().findViewById(R.id.more_day);
	     ll_day.setLayoutParams(mParams);
	     ll_day.setLayoutParams(mParams);
	     ll_day.setMaxHeight(height/3);
	     ll_day.setMaxWidth(width/3);
	     ll_day.setAdjustViewBounds(true);
	     ll_day.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 Intent	 intent = new Intent(arg0.getContext(),HolesMain.class);			           		      
			            startActivity(intent);
				}
		     });  
	     ll_holes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 Intent	 intent = new Intent(arg0.getContext(),DayMain.class);	
				            		      
		            startActivity(intent);
			}
	     });  
	     ll_present.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				  Intent	 intent = new Intent(arg0.getContext(),OldObjectMain.class);		           		      
		            startActivity(intent);
			}
	     });
	     ll_ask.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					  Intent	 intent = new Intent(arg0.getContext(),AskHelpActivity.class);		           		      
			            startActivity(intent);
				}
		     });   
 
	}
	
	
}
