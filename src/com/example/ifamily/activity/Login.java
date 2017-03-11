package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.ifamily.R;
import com.example.ifamily.client.PushMessageReceiver.onBindListener;
import com.google.gson.Gson;
import com.example.ifamily.PushApplication;
import com.example.ifamily.bean.User;
import com.example.ifamily.client.PushMessageReceiver;
import com.example.ifamily.dao.UserDB;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.example.ifamily.utils.SendMsgAsyncTask;
import com.example.ifamily.utils.SharePreferenceUtil;
import com.example.ifamily.utils.T;
import com.example.ifamily.utils.SendMsgAsyncTask.OnSendScuessListener;
import com.example.ifamily.bean.Message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class Login extends Activity implements OnClickListener,OnCheckedChangeListener,onBindListener{
	private ProgressDialog mDialog;
	private String responseMsg = "";
	private static final int REQUEST_TIMEOUT = 10*1000;//设置请求超时10秒钟  
	private static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟
	
	private Map<String,Object> userInfo;
	private FileCache fileCache;
	private File tempFile;
	private LinearLayout all;
	private SharedPreferences sp;
	private Button btlogin,btreg;
	private CheckBox cb,cbauto;
	private EditText inputUsername;
	private EditText inputPassword;
	 
	
	private SharePreferenceUtil mSpUtil;
	private UserDB mUserDB;
	private SendMsgAsyncTask task;
	private Gson mGson;

	private Handler mHandler = new Handler();
	private Runnable mConnTimeoutCallback = new Runnable()
	{
		@Override
		public void run()
		{
			
			if (task != null)
			{
				task.stop();
			}
			//T.showShort(Login.this, "登录超时，请重试");
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		inputUsername = (EditText)findViewById(R.id.et_qqNum);
		inputPassword = (EditText)findViewById(R.id.et_qqPwd);
		cb=(CheckBox)findViewById(R.id.cb_remb); 
		cb.setOnCheckedChangeListener(this); 
		cbauto=(CheckBox)findViewById(R.id.cb_autolog);
		cbauto.setOnCheckedChangeListener(this);
		btlogin=(Button)findViewById(R.id.btn_login);
		btlogin.setOnClickListener(this);
		btreg=(Button)findViewById(R.id.btn_login_regist);
		btreg.setOnClickListener(this);
		mSpUtil = PushApplication.getInstance().getSpUtil();
		mUserDB = PushApplication.getInstance().getUserDB();
		mGson = PushApplication.getInstance().getGson();

		userInfo = new HashMap<String,Object>();
		fileCache = new FileCache(this);
		
		all=(LinearLayout)findViewById(R.id.all);
		FontManager.changeFonts(all,this);
		PushMessageReceiver.bindListeners.add(this);
		sp = getSharedPreferences("Login",0);
		
		if(sp.getBoolean("ISCHECK", false))  
        {  
          //设置默认是记录密码状态  
          cb.setChecked(true);  
          inputUsername.setText(sp.getString("username", ""));  
          inputPassword.setText(sp.getString("password", ""));  
          //判断自动登录多选框状态  
          if(sp.getBoolean("AUTO_ISCHECK", false))  
          {  
                //设置默认是自动登录状态  
                cbauto.setChecked(true);  
                mDialog = new ProgressDialog(Login.this);
 				mDialog.setTitle("登录");
 				mDialog.setMessage("正在登录服务器，请稍后...");
 				mDialog.show();
 				Thread loginThread = new Thread(new LoginThread());
 		
 				loginThread.start();
          }  
        } 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_login:
			if(!(inputUsername.getText().toString().equals(""))&&!(inputPassword.getText().toString().equals("")))
			{
				
				mDialog = new ProgressDialog(Login.this);
				mDialog.setTitle("登录");
				mDialog.setMessage("正在登录服务器，请稍后...");
				mDialog.show();
				Thread loginThread = new Thread(new LoginThread());
		
				loginThread.start();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_login_regist:
			Intent intent = new Intent();
			intent.setClass(Login.this, Reg.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {
		// TODO Auto-generated method stub
		
		Editor editor = sp.edit();
        switch(buttonView.getId())
        {
        case R.id.cb_remb:
        
        	if(cb.isChecked())
        	{
        		//editor.putBoolean("ISCHECK", true);
        		//editor.commit();
        		
        		
        	}else
        	{
        		editor.putBoolean("ISCHECK", false);
        		cbauto.setChecked(false);
        		editor.putString("username", "");
        		editor.putString("password", "");
        		editor.commit();
        	}
        	break;
        case R.id.cb_autolog:
        	if (cbauto.isChecked()) {  
                cb.setChecked(true);
                System.out.println("自动登录已选中");  
                //editor.putBoolean("AUTO_ISCHECK", true);
                //editor.commit();  
                  
            }else {  
                  
                System.out.println("自动登录没有选中");  
                editor.putBoolean("AUTO_ISCHECK", false);
                editor.commit();  
                
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
	    	//使用apache HTTP客户端实现
	    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/LoginServlet";
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
	    			//responseMsg = EntityUtils.toString(response.getEntity());
	    			InputStream is = response.getEntity().getContent();
	    			ObjectInputStream ois = new ObjectInputStream(is);
	    			
	    			userInfo = (Map<String,Object>)ois.readObject();
	    			
	    			
	    				
	    			
	    			ois.close();
	    			is.close();
	    			
	    			
	    			
	    			
	    			
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
				String username = inputUsername.getText().toString();
				String password = inputPassword.getText().toString();	
				
				
				
				//URL合法，但是这一步并不验证密码是否正确*/
				
				
		    	boolean loginValidate = loginServer(username, password);
		    	System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+responseMsg);
		    	android.os.Message msg = handler.obtainMessage();
		    	if(loginValidate)
		    	{
		    		if(((String)userInfo.get("success")).equals("success"))
		    		{
		    			msg.what = 0;
			    		handler.sendMessage(msg);
		    		}else
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
    	public void handleMessage(android.os.Message msg)
    	{
    		switch(msg.what)
    		{
    		case 0:
    			mDialog.cancel();

    			Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
    			//Intent intent = new Intent();
    			//intent.setClass(LoginActivity.this, MainActivity.class);
    			//tartActivity(intent);
    			//finish();
    			byte[] photo = (byte[])userInfo.get("photo");
				//Log.v("buffer", photo.toString());
				tempFile = fileCache.getFile(photo.toString().substring(1, 5)+".jpg");
				try {
					tempFile.createNewFile();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				byte2File(photo,tempFile);
				Bitmap bitmap = decodeFile(tempFile);
				userInfo.put("photo", bitmap);
				fileCache.clear();
				PushApplication.getInstance().setUserInfo(userInfo);
				
    			
    			if(cbauto.isChecked())
    			{
    				sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
    				sp.edit().putBoolean("ISCHECK", true).commit();
    			}
    			if(cb.isChecked())
				{
					Editor editor = sp.edit();
					editor.putString("username", inputUsername.getText().toString());
					editor.putString("password", inputPassword.getText().toString());
					editor.putBoolean("ISCHECK", true);
					editor.commit();
				}
    			sp = getSharedPreferences("user",0);
    			sp.edit().putString("username", inputUsername.getText().toString()).commit();
    			sp.edit().putString("password", inputPassword.getText().toString()).commit();
    			
    			mHandler.postDelayed(mConnTimeoutCallback, 20000);
    			
    			mSpUtil.setNick(inputUsername.getText().toString());
    			mSpUtil.setHeadIcon(R.drawable.h17);
    			PushManager.startWork(getApplicationContext(),
    					PushConstants.LOGIN_TYPE_API_KEY, PushApplication.API_KEY);
    			//Intent intent=new Intent(Login.this,Iguide.class);
				//startActivity(intent);
				//Login.this.finish();
    			try {
    				Thread.sleep(3000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			finish();
    			Intent intent = new Intent(Login.this,
    					Iguide.class);
    			intent.putExtra("tab", 1);
    			startActivity(intent);
    			break;
    		case 1:
    			mDialog.cancel();
    			//userinfo1=list.get(0);
    			//File image = (File)userinfo1.get("img");
    			//image.setImageBitmap(bitmap);
    			Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
    			break;
    		case 2:
    			mDialog.cancel();
    			Toast.makeText(getApplicationContext(), "URL验证失败", Toast.LENGTH_SHORT).show();
    			break;
    		
    		}
    		
    	}
    };
    
    
    
    
    @Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (task != null)
			task.stop();
		PushMessageReceiver.bindListeners.remove(this);// 注销推送的消息
	}

	@Override
	public void onBind(String userId, int errorCode)
	{
		Log.e("TAG", "Login Activity onBind ");
		if (errorCode == 0)
		{
			Log.e("TAG", "Login Activity onBind success ");
			// 如果绑定账号成功，由于第一次运行，给同一tag的人推送一条新人消息
			//User u = new User(mSpUtil.getUserId(), mSpUtil.getChannelId(),
			//		mSpUtil.getNick(), mSpUtil.getHeadIcon(), 0);
			//mUserDB.addUser(u);// 把自己添加到数据库
			//Message firstSendMsg = new Message(System.currentTimeMillis(), "","123");
			//firstSendMsg.setHello("hello");
			//task = new SendMsgAsyncTask(mGson.toJson(firstSendMsg), "");
			/*
			task.setOnSendScuessListener(new OnSendScuessListener()
			{
				@Override
				public void sendScuess()
				{
					
					mHandler.removeCallbacks(mConnTimeoutCallback);
					finish();
					Intent intent = new Intent(Login.this,
							Iguide.class);
					startActivity(intent);
				}
			});
			task.send();
			*/
			mHandler.removeCallbacks(mConnTimeoutCallback);
			
		}
	}
	
	public static void byte2File(byte[] buf, File file)  
    {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;   
        try  
        {  
             
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(buf);  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        finally  
        {  
            if (bos != null)  
            {  
                try  
                {  
                    bos.close();  
                }  
                catch (IOException e)  
                {  
                    e.printStackTrace();  
                }  
            }  
            if (fos != null)  
            {  
                try  
                {  
                    fos.close();  
                }  
                catch (IOException e)  
                {  
                    e.printStackTrace();  
                }  
            } 
            
        }  
    }  
    
    
    private Bitmap decodeFile(File f){   
        try {   
            //解码图像大小  
            BitmapFactory.Options o = new BitmapFactory.Options();   
            o.inJustDecodeBounds = true;   
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);   
    
            //找到正确的刻度值，它应该是2的幂。  
            final int REQUIRED_SIZE=70;   
            int width_tmp=o.outWidth, height_tmp=o.outHeight;   
            int scale=1;   
            while(true){   
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)   
                    break;   
                width_tmp/=2;   
                height_tmp/=2;   
                scale*=2;   
            }   
    
            BitmapFactory.Options o2 = new BitmapFactory.Options();   
            o2.inSampleSize=scale;   
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);   
        } catch (FileNotFoundException e) {}   
        return null;   
    } 
}

