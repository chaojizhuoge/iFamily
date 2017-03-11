package com.example.ifamily.utils;

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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.ifamily.activity.Iguide;
import com.example.ifamily.activity.PasswordFActivity;

public class MyHttpRequest {
	
	private ProgressDialog mDialog;
	private String responseMsg = "";
	private static final int REQUEST_TIMEOUT = 10*1000;//��������ʱ10����  
	private static final int SO_TIMEOUT = 10*1000;  //���õȴ����ݳ�ʱʱ��10����
	
	public static HttpClient getHttpClient()
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
	
}
	 /*
	class registThread implements Runnable
	    {

			@Override
			public void run() {
				
				
				
				
				//URL�Ϸ���������һ��������֤�����Ƿ���ȷ
				
				
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
    			Intent intent=new Intent(PasswordFActivity.this,Iguide.class);
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
*/
