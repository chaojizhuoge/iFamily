package com.example.ifamily.activity;

import java.io.ByteArrayOutputStream;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifamily.R;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class AddPicActivity extends Activity implements android.view.View.OnClickListener{
	
	private ProgressDialog mDialog;
	private String responseMsg = "";
	private static final int REQUEST_TIMEOUT = 10*1000;//设置请求超时10秒钟  
	private static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟
	private TextView title;
	private Button back;
	private EditText addText;
	private Button send;
	private ImageView addPhoto;
	private SharedPreferences sp;
	private LinearLayout all;
	private Bitmap bitmap;
	private String groupID;
    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.a_add_photo);
		init();
	}
	
	private void init() {
		// TODO 自动生成的方法存根
		title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("发表信息");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				AddPicActivity.this.finish();
			}
			 
		 });
		
		
		
		
		addText = (EditText)findViewById(R.id.a_addphoto_et);
		addPhoto = (ImageView)findViewById(R.id.a_add_photo_iv);
		send = (Button)findViewById(R.id.a_addphoto_bt);
		
		all=(LinearLayout)findViewById(R.id.all);
		FontManager.changeFonts(all,this);
		send.setOnClickListener(this);
		Intent intent = getIntent();
		if(intent!=null)
        {
            bitmap=intent.getParcelableExtra("bitmap");
            addPhoto.setImageBitmap(bitmap);
            groupID = intent.getStringExtra("groupId");
        }
	}
 
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		String text = "";
		if((text=addText.getText().toString()).equals(""))
			Toast.makeText(this, "请输入要发送的内容", Toast.LENGTH_SHORT).show();
		else
		{
			mDialog = new ProgressDialog(this);
			mDialog.setTitle("登录");
			mDialog.setMessage("正在登录服务器，请稍后...");
			mDialog.show();
			upload(v);
			//Thread loginThread = new Thread(new LoginThread());
	
			//loginThread.start();
		}
	}
	
	
	public void upload(View view) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
		
				
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				byte[] buffer = out.toByteArray();

				byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
				String photo = new String(encode);

				
				params.put("photo", photo);
			
			sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			params.put("text", addText.getText().toString());
			params.put("uname", username);
			params.put("requesttype", "2");
			params.put("addtype", "2");
			params.put("groupId", groupID);
			
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {
							mDialog.dismiss();

							Toast.makeText(AddPicActivity.this, "成功上传！", Toast.LENGTH_SHORT)
									.show();
						/*	Intent intent = new Intent(AddPicActivity.this,Iguide.class);
							intent.putExtra("tab", 1);
							startActivity(intent);
							MyApplication.getInstance().exit();*/
							
							       
							  SharedPreferences sps = AddPicActivity.this.getSharedPreferences("SP", Context.MODE_PRIVATE);							  							  	  		  
							  Editor editor = sps.edit();
							  editor.putBoolean("ifrefresh",true);
							  editor.commit();				 																																																								
							AddPicActivity.this.finish();
		
						} else {
							Toast.makeText(AddPicActivity.this,
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
					Toast.makeText(AddPicActivity.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
