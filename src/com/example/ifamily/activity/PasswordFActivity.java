package com.example.ifamily.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordFActivity extends Activity implements OnClickListener{

	private ProgressDialog mDialog;
	private String responseMsg = "";
	private static final int REQUEST_TIMEOUT = 10*1000;//��������ʱ10����  
	private static final int SO_TIMEOUT = 10*1000;  //���õȴ����ݳ�ʱʱ��10����
	
	private String username;
	private String password;
	
	private Button btn_password_ok,back;
	private TextView tv_top_title;
	private EditText et_password1,et_password2;
	private String password1,password2;
	private SharedPreferences sp;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.passwordf);
		initView();
	}
	
	
	private void initView() {
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//��������
		FontManager.changeFonts(all,this);
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("Ifamilyע��");
		btn_password_ok=(Button) findViewById(R.id.btn_passsword_ok);
		btn_password_ok.setOnClickListener(this);
		et_password1=(EditText) findViewById(R.id.password1);
		et_password2=(EditText) findViewById(R.id.password2);
        back=(Button)findViewById(R.id.btn_title_left);
        back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PasswordFActivity.this.finish();
			}
        	
        });
        sp=getSharedPreferences("regist",0);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_passsword_ok:      //�����ж��Լ��洢��Ϣ
			password1=et_password1.getText().toString();
	        password2=et_password2.getText().toString();
			if(!(password1.equals(""))&&(!password2.equals("")))
			{
				if(password1.equals(password2))
				{
					if(password1.length()>5&&password1.length()<19){
						Editor edit = sp.edit();
						edit.putString("password", password1);
						edit.commit();
					
						username = sp.getString("username", "");
						password = sp.getString("password", "");
						mDialog = new ProgressDialog(PasswordFActivity.this);
						mDialog.setTitle("ע��");
						mDialog.setMessage("���ڵ�¼�����������Ժ�...");
						mDialog.show();
						Thread registThread = new Thread(new registThread());
			
						registThread.start();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "���볤�ȱ�����6��18λ��", Toast.LENGTH_LONG).show();
						et_password1.setText("");
						et_password2.setText("");
					}
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), "�����������벻ͬ,���ٴ�����", Toast.LENGTH_LONG).show();
					et_password1.setText("");
					et_password2.setText("");
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "���벻��Ϊ�գ�", Toast.LENGTH_LONG).show();
				et_password1.setText("");
				et_password2.setText("");
			}
			break;
		
		}
	
	}
	
	public HttpClient getHttpClient()
    {
    	BasicHttpParams httpParams = new BasicHttpParams();
    	HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
    	HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
    	HttpClient client = new DefaultHttpClient(httpParams);
    	return client;
    }

	
	 private boolean loginServer(String username, String password)
	    {
	    	boolean loginValidate = false;
	    	//ʹ��apache HTTP�ͻ���ʵ��
	    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/RegistryServlet";
	    	HttpPost request = new HttpPost(urlStr);
	    	//������ݲ�����Ļ������ԶԴ��ݵĲ������з�װ
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	//����û���������
	    	params.add(new BasicNameValuePair("username",username));
	    	params.add(new BasicNameValuePair("password",password));
	    	try
	    	{
	    		
	    		
	    		
	    		//�������������
	    		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	    		HttpClient client = getHttpClient();
	    		//ִ�����󷵻���Ӧ
	    		HttpResponse response = client.execute(request);
	    		//in.close();
	    		//�ж��Ƿ�����ɹ�
	    		if(response.getStatusLine().getStatusCode()==200)
	    		{
	    			loginValidate = true;
	    			//�����Ӧ��Ϣ
	    			responseMsg = EntityUtils.toString(response.getEntity());
	    		}
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    	return loginValidate;
	    }
	
	class registThread implements Runnable
	    {

			@Override
			public void run() {
				
				
				
				
				//URL�Ϸ���������һ��������֤�����Ƿ���ȷ*/
				
				
		    	boolean loginValidate = loginServer(username, password);
		    	System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+responseMsg);
		    	Message msg = handler.obtainMessage();
		    	if(loginValidate)
		    	{
		    		if(responseMsg.equals("success"))
		    		{
		    			msg.what = 0;
			    		handler.sendMessage(msg);
		    		}else if(responseMsg.equals("failed"))
		    		{
		    			
		    			msg.what = 3;
			    		handler.sendMessage(msg);
		    		}
		    		else
		    		{
		    			msg.what = 1;
			    		handler.sendMessage(msg);
		    		}
		    		
		    	}else
		    	{
		    		msg.what = 2;
		    		handler.sendMessage(msg);
		    	}
			}
	    	
	    }
	
	Handler handler = new Handler()
    {
    	public void handleMessage(Message msg)
    	{
    		switch(msg.what)
    		{
    		case 0:
    			mDialog.cancel();

    			Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_SHORT).show();
    			//Intent intent = new Intent();
    			//intent.setClass(LoginActivity.this, MainActivity.class);
    			//tartActivity(intent);
    			sp=getSharedPreferences("Login",0);
    			sp.edit().putBoolean("ISCHECK", true).commit();
    			sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
    			sp.edit().putString("username", username).commit();
    			sp.edit().putString("password", password).commit();
    			
    			
    			sp=getSharedPreferences("user",0);
    			sp.edit().putString("username", username).commit();
    			sp.edit().putString("password", password).commit();
    			PushManager.startWork(getApplicationContext(),
    					PushConstants.LOGIN_TYPE_API_KEY, PushApplication.API_KEY);
    		
    			Intent intent=new Intent(PasswordFActivity.this,Regover.class);
				startActivity(intent);
				PasswordFActivity.this.finish();
    			//finish();
    			break;
    		case 1:
    			mDialog.cancel();
    			//userinfo1=list.get(0);
    			//File image = (File)userinfo1.get("img");
    			//image.setImageBitmap(bitmap);
    			Toast.makeText(getApplicationContext(), "ע��ʧ��,�������������⣬���Ժ�����", Toast.LENGTH_SHORT).show();
    			break;
    		case 2:
    			mDialog.cancel();
    			Toast.makeText(getApplicationContext(), "URL��֤ʧ��", Toast.LENGTH_SHORT).show();
    			break;
    		case 3:
    			mDialog.cancel();
    			Toast.makeText(getApplicationContext(), "�û����Ѿ���ע��", Toast.LENGTH_SHORT).show();
    		
    		}
    		
    	}
    };
}

