package com.example.ifamily.activity;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.example.ifamily.R;
import com.example.ifamily.adapter.FriendlistAdapter;
import com.example.ifamily.message.AddfriendMessage;
import com.example.ifamily.mywidget.SwitchButton;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HolesAddTc extends Activity{
	
    private LinearLayout ll_present,all;  
	private TextView title;
	private Button back;
	private SwitchButton sb_niming;
    private Button presentok;	
    private EditText wishes;
    private boolean niming = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.holes_add_tc);	
		initview();
	}
	
	
private void initview()
{
	

	
	
	wishes = (EditText)findViewById(R.id.wishes);
	sb_niming=(SwitchButton)findViewById(R.id.cb_niming);
	sb_niming.setChecked(true);
	sb_niming.setOnCheckedChangeListener(new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			niming=arg1;
		}
		
	});
	ll_present=(LinearLayout)findViewById(R.id.present);//添加物品的界面
	all=(LinearLayout)findViewById(R.id.all);//整个界面
	FontManager.changeFonts(all,this);
	
	
	title = (TextView)findViewById(R.id.title);
	presentok=(Button)findViewById(R.id.wish_ok);//present确定按钮
	back = (Button)findViewById(R.id.back);
	title.setText("我要吐槽");
	///
	 back.setOnClickListener(new OnClickListener()
	 {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(HolesAddTc.this,HolesMain.class);
			intent.putExtra("tab", 1);
			 startActivity(intent);
			 MyApplication.getInstance().exit();
		}
		 
	 });
	 
	 ////

		
		///
		presentok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				upload(arg0);
			}
			
		});
}

public void upload(View view) {
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RequestParams params = new RequestParams();
		if(wishes.getText().toString().trim().equals(""))
		{
			Toast.makeText(HolesAddTc.this,"请输入内容",Toast.LENGTH_SHORT).show();
			return;
		}



		SharedPreferences sp = getSharedPreferences("user",0);
		String username = sp.getString("username", "");
		String state = (niming)?"1":"0";
		params.put("state", state);
		params.put("uname", username);
		params.put("text", wishes.getText().toString());
		//params.put("hasphoto", String.valueOf(hasphoto));
		params.put("requesttype", "6");
		String url = "http://103.31.241.201:8080/IFamilyServer/HolesServlet";

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					if (statusCode == 200) {

						//Toast.makeText(Setgroup.this, "家庭成功创建！", Toast.LENGTH_SHORT)
								//.show();
						Intent intent=new Intent(HolesAddTc.this,HolesMain.class);
						intent.putExtra("tab", 1);
						 startActivity(intent);
						 MyApplication.getInstance().exit();
					} else {
						Toast.makeText(HolesAddTc.this,
								"网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Toast.makeText(HolesAddTc.this,
						"网络访问异常，错误码  > " + statusCode, 0).show();

			}
		});

	} catch (Exception e) {
		e.printStackTrace();
	}
}


	
}
