package com.example.ifamily.activity;

import java.io.ByteArrayOutputStream;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifamily.R;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PrivateMeModify extends Activity implements OnClickListener{

	private TextView title;
	private Button back;
	private EditText detail;
	private Button send;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_privateintro);
		initview();
	}
	
	

	private void initview()
	{

		LinearLayout all=(LinearLayout)findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this);
		 title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 detail = (EditText)findViewById(R.id.p_privateintro);
		 send = (Button)findViewById(R.id.p_privateintro_ok);
		 send.setOnClickListener(this);
		 title.setText("个人简介");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				PrivateMeModify.this.finish();
			}
			 
		 });
	}



	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		uploadIntro(detail.getText().toString());
	}



	private void uploadIntro(String detail) {
		// TODO 自动生成的方法存根
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			
				params.put("detail", detail);
			
				SharedPreferences sp = getSharedPreferences("user",0);
				final String username = sp.getString("username", "");
				//params.put("gname", groupname.getText().toString());
				params.put("uname", username);
				params.put("requesttype", "6");
				//params.put("gdetail", groupInfo.getText().toString());
				//params.put("hasphoto", String.valueOf(hasphoto));
				String url = "http://103.31.241.201:8080/IFamilyServer/UserInfoServlet";
				
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						try {
							if (statusCode == 200) {

								Toast.makeText(PrivateMeModify.this, "个人简介修改成功~！", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(PrivateMeModify.this,Privatemessage.class);
								intent.putExtra("privateAcct", Long.parseLong(username));
								startActivity(intent);
								PrivateMeModify.this.finish();
								//		.show();
								//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
								//startActivity(intent);
							} else {
								Toast.makeText(PrivateMeModify.this,
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
						Toast.makeText(PrivateMeModify.this,
								"网络访问异常，错误码  > " + statusCode, 0).show();

					}
				});

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}