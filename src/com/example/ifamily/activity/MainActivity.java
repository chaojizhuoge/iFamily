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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.adapter.GroupListAdapter;
import com.example.ifamily.message.GroupListM;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.MyApplication;
import com.example.ifamily.utils.MyHttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MainActivity extends Activity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 3000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	
	private SharedPreferences sp;
 
	private Map<String,Object> userInfo = new HashMap<String,Object>();
	private File tempFile;
	private FileCache fileCache;
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		fileCache = new FileCache(this);
		sp = getSharedPreferences("Login", 0);
		init();
	}

	private void init() {
		
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		 try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块S
				e.printStackTrace();
			}

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}

	private void goHome() {
		
		
		
		if(sp.getBoolean("AUTO_ISCHECK", false))
		{
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					String urlStr = "http://103.31.241.201:8080/IFamilyServer/LoginServlet";
			    	HttpPost request = new HttpPost(urlStr);
			    	//如果传递参数多的话，可以对传递的参数进行封装
			    	sp = MainActivity.this.getSharedPreferences("user",0);
			    	List<NameValuePair> params = new ArrayList<NameValuePair>();
			    	
			    	//添加用户名和密码
			    	params.add(new BasicNameValuePair("username",sp.getString("username", "")));
			    	params.add(new BasicNameValuePair("password",sp.getString("password", "")));
			    	try
			    	{
			    		
			    		
			    		
			    		//设置请求参数项
			    		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			    		HttpClient client = MyHttpRequest.getHttpClient();
			    		//执行请求返回相应
			    		HttpResponse response = client.execute(request);
			    		//in.close();
			    		//判断是否请求成功
			    		if(response.getStatusLine().getStatusCode()==200)
			    		{

			    			//获得响应信息
			    			//responseMsg = EntityUtils.toString(response.getEntity());
			    			InputStream is = response.getEntity().getContent();
			    			ObjectInputStream ois = new ObjectInputStream(is);
			    			
			    			userInfo = (Map<String,Object>)ois.readObject();
			    			
			    			
			    				
			    			
			    			ois.close();
			    			is.close();
			    			
			    			
			    			
			    			
			 
			    			
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
							
			    		}
			    		else
			    		{
			    			Toast.makeText(MainActivity.this, "URL验证失败", Toast.LENGTH_SHORT).show();
			    		}
			    	}catch(Exception e)
			    	{
			    		e.printStackTrace();
			    	}
			    	//return loginValidate;
				}}).start();
			Intent intent = new Intent(MainActivity.this,Iguide.class);
			intent.putExtra("tab", 1);
			startActivity(intent);
			MainActivity.this.finish();
		}
		else{
			Intent intent = new Intent(MainActivity.this,Login.class);
			startActivity(intent);
			MainActivity.this.finish();
		}
	}

	private void goGuide() {
		
		
		Intent intent = new Intent(MainActivity.this,Viewpager.class);
		startActivity(intent);
		MainActivity.this.finish();
		
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
