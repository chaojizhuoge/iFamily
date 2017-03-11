package com.example.ifamily.activity;

import java.io.ByteArrayOutputStream;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
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

public class Telladvice extends Activity implements OnClickListener{

	private EditText advice;
	private TextView title;
	private Button back,reok;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_telladvice);
		initview();
	}
	
	private void initview()

	{
		advice = (EditText) findViewById(R.id.advice);
		title = (TextView) findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("意见反馈");
		 reok = (Button)findViewById(R.id.bt_recommend);
		 reok.setOnClickListener(this);
		 back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Telladvice.this.finish();
			}
			 
		 });
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		upload(arg0);
	}
	
	public void upload(View view) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			if(advice.getText().toString().trim().equals(""))
			{
				Toast.makeText(Telladvice.this,"亲，请提供宝贵建议哦~",Toast.LENGTH_SHORT).show();
				return;
			}
			
			SharedPreferences sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			params.put("uname", username);
			params.put("text", advice.getText().toString());
			params.put("requesttype", "7");
			String url = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							//Toast.makeText(Setgroup.this, "家庭成功创建！", Toast.LENGTH_SHORT)
									//.show();
							Toast.makeText(Telladvice.this,
									"提交成功！\n 感谢您的宝贵建议！！！" , Toast.LENGTH_LONG).show();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块S
								e.printStackTrace();
							}
							Telladvice.this.finish();
						} else {
							Toast.makeText(Telladvice.this,
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
					Toast.makeText(Telladvice.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}