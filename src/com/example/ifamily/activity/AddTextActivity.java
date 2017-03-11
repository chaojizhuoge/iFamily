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

import com.example.ifamily.R;
import com.example.ifamily.activity.Login.LoginThread;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddTextActivity extends Activity implements android.view.View.OnClickListener{
	private TextView title;
	private Button back;
	private ProgressDialog mDialog;
	private String responseMsg = "";
	private static final int REQUEST_TIMEOUT = 10*1000;//��������ʱ10����  
	private static final int SO_TIMEOUT = 10*1000;  //���õȴ����ݳ�ʱʱ��10����
	  private LinearLayout all;
	private EditText addText;
	private Button sendText;
	private SharedPreferences sp;
	private String groupId = "";
    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		if(intent!=null)
        {
            
            groupId = intent.getStringExtra("groupId");
        }
		setContentView(R.layout.a_addtext);
		
		init();
	}
	
	private void init() {
		// TODO �Զ����ɵķ������
		
		title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("�����ı�");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				AddTextActivity.this.finish();
			}
			 
		 });
		
		
		addText = (EditText)findViewById(R.id.a_addtext_et);
		sendText = (Button)findViewById(R.id.a_addtext_bt);
		sendText.setOnClickListener(this);
		all=(LinearLayout)findViewById(R.id.all);
		FontManager.changeFonts(all,this);
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		String text = "";
		if((text=addText.getText().toString()).equals(""))
			Toast.makeText(AddTextActivity.this, "������Ҫ���͵�����", Toast.LENGTH_SHORT).show();
		else
		{
			mDialog = new ProgressDialog(AddTextActivity.this);
			//mDialog.setTitle("��¼");
			mDialog.setMessage("���ڷ����У����Ժ�...");
			mDialog.show();
			Thread loginThread = new Thread(new LoginThread());
	
			loginThread.start();
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

	
	 private boolean loginServer()
	    {
	    	boolean loginValidate = false;
	    	//ʹ��apache HTTP�ͻ���ʵ��
	    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";
	    	HttpPost request = new HttpPost(urlStr);
	    	//������ݲ�����Ļ������ԶԴ��ݵĲ������з�װ
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	//����û���������
	    	sp = getSharedPreferences("user",0);
	    	params.add(new BasicNameValuePair("requesttype","2"));
	    	params.add(new BasicNameValuePair("addtype","1"));
	    	params.add(new BasicNameValuePair("uname",sp.getString("username", "")));
	    	params.add(new BasicNameValuePair("text",addText.getText().toString()));
	    	params.add(new BasicNameValuePair("groupId",groupId));
	    	
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
	
	class LoginThread implements Runnable
	    {

			@Override
			public void run() {

				
				
				
				//URL�Ϸ���������һ��������֤�����Ƿ���ȷ*/
				
				
		    	boolean loginValidate = loginServer();
		    	System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+responseMsg);
		    	Message msg = handler.obtainMessage();
		    	if(loginValidate)
		    	{
		    		if(responseMsg.equals("success"))
		    		{
		    			msg.what = 0;
			    		handler.sendMessage(msg);
		    		}else
		    		{
		    			
		    			msg.what = 0;
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

    			Toast.makeText(getApplicationContext(), "���ͳɹ���", Toast.LENGTH_SHORT).show();
    			//Intent intent = new Intent();
    			//intent.setClass(LoginActivity.this, MainActivity.class);
    			//tartActivity(intent);
    			//finish();
    			
    			/*Intent intent=new Intent(AddTextActivity.this,Iguide.class);
    			intent.putExtra("tab", 1);
				startActivity(intent);
				MyApplication.getInstance().exit();*/
			       
			  SharedPreferences sps = AddTextActivity.this.getSharedPreferences("SP", Context.MODE_PRIVATE);							  							  	  		  
			  Editor editor = sps.edit();
			  editor.putBoolean("ifrefresh",true);
			  editor.commit();		
    			AddTextActivity.this.finish();
    			break;
    		case 1:
    			mDialog.cancel();
    			//userinfo1=list.get(0);
    			//File image = (File)userinfo1.get("img");
    			//image.setImageBitmap(bitmap);
    			Toast.makeText(getApplicationContext(), "����ʧ�ܣ�������", Toast.LENGTH_SHORT).show();
    			break;
    		case 2:
    			mDialog.cancel();
    			Toast.makeText(getApplicationContext(), "����ʧ�ܣ�������", Toast.LENGTH_SHORT).show();
    			break;
    		
    		}
    		
    	}
    };

}
