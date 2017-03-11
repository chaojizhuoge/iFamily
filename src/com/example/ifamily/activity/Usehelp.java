package com.example.ifamily.activity;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifamily.R;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.TxtReader;

public class Usehelp extends Activity{
	
private	Button up,down;
private	TextView text;
private	int currentpage=0;
private int allpage;
private boolean ifre=false;
private List<String> texts=new ArrayList<String>();
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.p_usehelp);
        initview();
	}
	private void initview()
	{
		LinearLayout all=(LinearLayout)findViewById(R.id.all);
		FontManager.changeFonts(all, this);
		up=(Button)findViewById(R.id.pageup);
		up.setVisibility(View.GONE);
		down=(Button)findViewById(R.id.pagedown);
		text=(TextView)findViewById(R.id.text);
		InputStream inputStream = getResources().openRawResource(R.raw.test);
		String string = TxtReader.getString(inputStream);
		final String[] strs=string.split("\\~");
	
		text.setText(strs[0]);
		allpage=strs.length;
		currentpage=0;
		up.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(currentpage<=strs.length-1&&currentpage>0)
				{currentpage--;
				text.setText(strs[currentpage]);}
				if(currentpage==0)
				{
					up.setVisibility(View.GONE);
				}
				if(currentpage==strs.length-2)
				{
					down.setText("返回");
					ifre=true;
				}else{
					down.setText("上一页");
					ifre=false;
				}
				
			}
			
		});
		down.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ifre)
				{	
					Usehelp.this.finish();
				}
				if(currentpage<strs.length-2&&currentpage>=0)
				{currentpage++;
				text.setText(strs[currentpage]);}
				if(currentpage>=1)up.setVisibility(View.VISIBLE);
				if(currentpage==strs.length-2)
				{
					down.setText("返回");
					ifre=true;
				}else{
					down.setText("下一页");
					ifre=false;
				}
				

			}
			
		});
	}
	
}