package com.example.ifamily.activity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.http.Header;

import android.app.Activity;
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
import com.example.ifamily.utils.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Entergroup extends Activity implements OnClickListener{

	private Button ok;
	private EditText num;
	private TextView title;
	private Button back;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_entergroup);
		initview();
	}
	
	private void initview()
	{
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this);
		
		 title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("加入家庭");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				Entergroup.this.finish();
			}
			 
		 });
		ok=(Button)findViewById(R.id.bt_searchgroup);
		ok.setOnClickListener(this);
		num=(EditText)findViewById(R.id.search_num);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		

		try {
			
			
			RequestParams params = new RequestParams();

			//params.put("user1", num.getText().toString());
			params.put("user2", getSharedPreferences("user",0).getString("username", ""));
			params.put("groupId", num.getText().toString());
			params.put("requesttype", "2");
			//params.put("addtype", "2");

			
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/MessageServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {
							InputStream is = new ByteArrayInputStream(responseBody);
							ObjectInputStream ois = new ObjectInputStream(is);
							
							String s = (String)ois.readObject();
							if(s.equals("failed"))
								Toast.makeText(Entergroup.this,
										"家庭不存在~", Toast.LENGTH_SHORT).show();
							else
							{
								Toast.makeText(Entergroup.this,
										"已经向创建者发出请求~", Toast.LENGTH_SHORT).show();
								Entergroup.this.finish();
							}
							
						} else {
							Toast.makeText(Entergroup.this,
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
					Toast.makeText(Entergroup.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	
	}
}
