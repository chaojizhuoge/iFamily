package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import com.example.ifamily.R;
import com.example.ifamily.adapter.DayDayAdapter;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class DayDiyviewActivity extends Activity{

	private ImageView iv,play;
	private String path;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private int messageID;
	private File tempfile1;
	private File tempfile;
	private FileCache fileCache = new FileCache(this);
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		fileCache.clear();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.d_day_detail);
		Intent intent = getIntent();
		if(intent != null)
		{
			messageID = intent.getIntExtra("messageID", 0);
		}
		
		initview();
		
	}
	private void initview()
	{
		iv=(ImageView)findViewById(R.id.image);
		play=(ImageView)findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				playMusic(path);
			}
			
		});
		getcard();
	}
	
	private void getcard() {


		try {
			
			RequestParams params = new RequestParams();
			//params.put("sex", sex);
			
			//SharedPreferences sp = this.getActivity().getSharedPreferences("user",0);
			//String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("messageID", String.valueOf(messageID));
			params.put("requesttype", "3");
			//params.put("times",String.valueOf(times));
	    	//params.put("lasttime",lasttime);
			//params.put("gdetail", groupInfo.getText().toString());
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/DayServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {
							InputStream is = new ByteArrayInputStream(responseBody);
							ObjectInputStream ois = new ObjectInputStream(is);
							
							Map<String,Object> map = (Map<String,Object>)ois.readObject();
			    			
			    			
							
			    				byte[] photo = (byte[])map.get("image");
			    				if(photo!=null)
			    				{
			    				tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
			    				tempfile.createNewFile();
			    				byte2File(photo,tempfile);
			    				Bitmap bitmap = decodeFile(tempfile);
			    				iv.setImageBitmap(bitmap);
			    				}
			    				byte[] sound = (byte[])map.get("sound");
			    				if(sound!=null){
			    				//Log.v("buffer", photo.toString());
			    				tempfile1 = fileCache.getFile(String.valueOf(Math.random()*1000));
			    				tempfile1.createNewFile();
			    				byte2File(sound,tempfile1);
			    				path = tempfile1.getAbsolutePath();
			    				
			    				
			    				}
			    		
			    			ois.close();
			    			is.close();
			    			
			    			

							//Toast.makeText(OldObjectActivity.this.getActivity(), "性别修改成功！", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(DayDiyviewActivity.this,
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
					Toast.makeText(DayDiyviewActivity.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
		
		
		
	
		
		
		
	}
	private void playMusic(String name) {
		if(name == null)
			return;
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
		
			mMediaPlayer.setDataSource(name); 			
			mMediaPlayer.prepare(); 
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
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
	        final int REQUIRED_SIZE=7000;   
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
