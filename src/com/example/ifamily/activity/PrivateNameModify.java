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

public class PrivateNameModify extends Activity implements OnClickListener{

	private TextView title;
	private Button back;
	private Button send;
	private EditText name;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_privatename_modify);
		initview();
	}
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		uploadName(name.getText().toString());
	}


	private void uploadName(String name) {
		// TODO 自动生成的方法存根



		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			if(name.isEmpty())
				Toast.makeText(this,"请输入新的昵称",Toast.LENGTH_SHORT).show();
			else
			{
				params.put("name", name);
			
				SharedPreferences sp = getSharedPreferences("user",0);
				final String username = sp.getString("username", "");
				//params.put("gname", groupname.getText().toString());
				params.put("uname", username);
				params.put("requesttype", "5");
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

								Toast.makeText(PrivateNameModify.this, "昵称修改成功！", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(PrivateNameModify.this,Privatemessage.class);
								intent.putExtra("privateAcct", Long.parseLong(username));
								startActivity(intent);
								PrivateNameModify.this.finish();
								//		.show();
								//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
								//startActivity(intent);
							} else {
								Toast.makeText(PrivateNameModify.this,
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
						Toast.makeText(PrivateNameModify.this,
								"网络访问异常，错误码  > " + statusCode, 0).show();

					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	
	}


	private void initview()
	{

		LinearLayout all=(LinearLayout)findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this);
		 title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 name = (EditText)findViewById(R.id.p_privatename);
		 send = (Button)findViewById(R.id.p_privatename_input);
		 send.setOnClickListener(this);
		 title.setText("修改昵称");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				PrivateNameModify.this.finish();
			}
			 
		 });
	}
}