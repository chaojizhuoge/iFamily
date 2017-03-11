package com.example.ifamily.activity;

import java.io.InputStream;

import com.example.ifamily.R;
import com.example.ifamily.utils.TxtReader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class RegServer  extends Activity{
	private Button ok,no;
	private TextView text;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reg_server);
		initview();
	}
	private void initview()
	{
		

		
		
		
		ok=(Button)findViewById(R.id.ok);
		no=(Button)findViewById(R.id.no);
		text=(TextView)findViewById(R.id.text);
		InputStream inputStream = getResources().openRawResource(R.raw.server);
		String string = TxtReader.getString(inputStream);
		text.setText(string);
		 SharedPreferences sps = RegServer.this.getSharedPreferences("SP", Context.MODE_PRIVATE);	
		 final Editor editor = sps.edit();
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			  		  		 			  
				  editor.putBoolean("ok_KEY",true);
				  editor.commit();	
				  RegServer.this.finish();
				
			}
			
		});
		no.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				  editor.putBoolean("ok_KEY",false);
				  editor.commit();	
				RegServer.this.finish();
			}
			
		});
		
		
	}
}
