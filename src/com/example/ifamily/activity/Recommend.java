package com.example.ifamily.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import com.example.ifamily.R;

public class Recommend extends Activity implements OnClickListener{

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_recommend);
		
	}
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}