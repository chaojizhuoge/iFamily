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
	private static final int REQUEST_TIMEOUT = 10*1000;//设置请求超时10秒钟  
	private static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟
	
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
	    	//使用apache HTTP客户端实现
	    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/RegistryServlet";
	    	HttpPost request = new HttpPost(urlStr);
	    	//如果传递参数多的话，可以对传递的参数进行封装
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	//添加用户名和密码
	    	params.add(new BasicNameValuePair("username",username));
	    	params.add(new BasicNameValuePair("password",password));
	    	try
	    	{
	    		
	    		
	    		
	    		//设置请求参数项
	    		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	    		HttpClient client = getHttpClient();
	    		//执行请求返回相应
	    		HttpResponse response = client.execute(request);
	    		//in.close();
	    		//判断是否请求成功
	    		if(response.getStatusLine().getStatusCode()==200)
	    		{
	    			loginValidate = true;
	    			//获得响应信息
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
				
				
				
				
				//URL合法，但是这一步并不验证密码是否正确
				
				
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

    			Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
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
    			Toast.makeText(getApplicationContext(), "注册失败,服务器出现问题，请稍后再试", Toast.LENGTH_SHORT).show();
    			break;
    		case 2:
    			mDialog.cancel();
    			Toast.makeText(getApplicationContext(), "URL验证失败", Toast.LENGTH_SHORT).show();
    			break;
    		case 3:
    			mDialog.cancel();
    			Toast.makeText(getApplicationContext(), "用户名已经被注册", Toast.LENGTH_SHORT).show();
    		
    		}
    		
    	}
    };

}
*/
