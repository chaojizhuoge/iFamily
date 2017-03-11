package com.example.ifamily.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.adapter.AtmeAdapter;
import com.example.ifamily.message.AtmeM;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AtMeActivity extends Activity{
	
	private Button back;
	private TextView title;
	private ListView lv;
	private LinearLayout all;
	private List<AtmeM> message=new ArrayList<AtmeM>();
	private AtmeAdapter aa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.atme);
		initview();			
	}
	
	private void initview()
	{
		title = (TextView)findViewById(R.id.title);
		back = (Button)findViewById(R.id.back);
		all=(LinearLayout)findViewById(R.id.all);
		FontManager.changeFonts(all,this);
		title.setText("与我相关");
		///
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				AtMeActivity.this.finish();
			}
			 
		 });
		 initmessage();
		 lv=(ListView)findViewById(R.id.lv);
		 //每个消息的点击事件
		 lv.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent it=new Intent(AtMeActivity.this,Privatemessage.class);
					startActivity(it);
				}
				
			});
		 lv.setAdapter(aa);
	
		 ////
	}
	
	private void initmessage()
	{
		//public AtmeM(long account,String name,String time,int type)
		message.add(new AtmeM(1234,"wowowo","jijijiji",2));
		message.add(new AtmeM(1234,"wowowo","jijijiji",1));
		message.add(new AtmeM(1234,"wowowo","jijijiji",3));
		message.add(new AtmeM(1234,"wowowo","jijijiji",4));
		message.add(new AtmeM(1234,"wowowo","jijijiji",2));
		aa=new AtmeAdapter(this,message);
	}
}
