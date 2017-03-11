package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
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

import com.example.ifamily.R;
import com.example.ifamily.adapter.OldObjectCommentAdapter;
import com.example.ifamily.adapter.ZoneCommentAdapter;
import com.example.ifamily.message.HeadM;
import com.example.ifamily.message.OldObjectCommentHero;
import com.example.ifamily.message.OldObjectcommentM;
import com.example.ifamily.mywidget.AutoListView;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OldObjectCommentActivity extends Activity implements OnHeaderRefreshListener, OnClickListener{
    private LinearLayout ll_main;
	private ListView lv;	
	private OldObjectCommentHero hero;
	private TextView title;
	private Button back;
	List<OldObjectcommentM> messages=new ArrayList<OldObjectcommentM>();
	
	private long id;
	private String name;
	private String time;
	private String text;
	private Bitmap icon;
	private Bitmap hisimg;
	private int messageId;
	private List<Map<String,Object>> Commentlist = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> Likelist = new ArrayList<Map<String,Object>>();
	private Map<String,Object> ttmap = new HashMap<String,Object>();
	private File tempFile;
	private FileCache fileCache;
	private PullToRefreshView pv;
	
	private OldObjectCommentAdapter oia;
	 
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			MyApplication.getInstance().addActivity(this);
			setContentView(R.layout.zonecomment);
			Intent intent = getIntent();
			if(intent!=null)
			{
				id = intent.getLongExtra("id", 0);
				name = intent.getStringExtra("name");
				time = intent.getStringExtra("time");
				text = intent.getStringExtra("text");
				icon = intent.getParcelableExtra("icon");
				//hisimg = intent.getParcelableExtra("hisimg");
				messageId = intent.getIntExtra("messageId", 0);
			}
			fileCache = new FileCache(this);
			initview();			
		}
		protected void initview()
		{
			


			pv = (PullToRefreshView)findViewById(R.id.test_Grids);
			pv.setIsPull(false);
			 pv.setOnHeaderRefreshListener(this);
			ll_main=(LinearLayout)findViewById(R.id.ll_main);

			FontManager.changeFonts(ll_main,this);
			lv=(ListView)findViewById(R.id.comlist);
			 title = (TextView)findViewById(R.id.title);
			 back = (Button)findViewById(R.id.back);
			 title.setText("详情");
			 back.setOnClickListener(new OnClickListener()
			 {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
					 startActivity(intent);
					 OldObjectMain.this.finish();*/
					OldObjectCommentActivity.this.finish();
				}
				 
			 });
			initdata();
			
		}
		protected void initdata()
		{
			//List<HeadM> lm=new ArrayList<HeadM>();
			//lm.add(new HeadM(1,"name1fsfdsfdsfdfsfsfsfsfdsf"));
			//lm.add(new HeadM(2,"name2fsdfdsfsdfsfsdfdfs"));
			//lm.add(new HeadM(3,"name3fdsfdsfsfds"));
			//lm.add(new HeadM(4,"name4fdsfdsfsdffdsfsfsf"));
			//public OldObjectCommentHero(int id,Object head,Object hisimg,String name,String time,String content,List< HeadM> names,int num,int comnum)
			//hero=new OldObjectCommentHero(1,R.drawable.defalt_head,R.drawable.aixin,"name","time","contnet",lm,13,15);
			hero=new OldObjectCommentHero(id,icon,name,time,text);
			//OldObjectcommentM oom1=new OldObjectcommentM(1,R.drawable.defalt_head,"name","kkkkk","2014/7/22 23:22:00");
			//OldObjectcommentM oom2=new OldObjectcommentM(2,R.drawable.defalt_head,"name","kkkkk","2014/7/22 23:22:00");
			//OldObjectcommentM oom3=new OldObjectcommentM(3,R.drawable.defalt_head,"name","kkkkk","2014/7/22 23:22:00");
			//OldObjectcommentM oom4=new OldObjectcommentM(4,R.drawable.defalt_head,"name","kkkkk","2014/7/22 23:22:00");
			//OldObjectcommentM oom5=new OldObjectcommentM(5,R.drawable.defalt_head,"name","kkkkk","2014/7/22 23:22:00");
			///messages.add(oom1);
			//messages.add(oom2);
			//messages.add(oom3);
			//messages.add(oom4);
			//messages.add(oom5);
			oia=new OldObjectCommentAdapter(this.getApplicationContext(),messages,hero);
			oia.setpop(ll_main);
			lv.setAdapter(oia);
			onHeaderRefresh(pv);
					
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
	}
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO 自动生成的方法存根
		new Thread(new Runnable()
		{

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				 
				    	
				    	//使用apache HTTP客户端实现
				    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/OldObjectServlet";
				    	HttpPost request = new HttpPost(urlStr);
				    	//如果传递参数多的话，可以对传递的参数进行封装
				    	SharedPreferences sp = OldObjectCommentActivity.this.getSharedPreferences("user",0);
				    	List<NameValuePair> params = new ArrayList<NameValuePair>();
				    	
				    	//添加用户名和密码
				    	params.add(new BasicNameValuePair("uname",sp.getString("username", "")));
				    	//params.add(new BasicNameValuePair("times",String.valueOf(times)));
				    	params.add(new BasicNameValuePair("messageId",String.valueOf(messageId)));
				    	params.add(new BasicNameValuePair("requesttype","3"));
				    	//params.add(new BasicNameValuePair("groupId",groupId));
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
				    			//loginValidate = true;
				    			//获得响应信息
				    			//responseMsg = EntityUtils.toString(response.getEntity());
				    			InputStream is = response.getEntity().getContent();
				    			ObjectInputStream ois = new ObjectInputStream(is);
				    			
				    			ttmap = (Map<String,Object>)ois.readObject();
				    			Commentlist = (List<Map<String,Object>>)ttmap.get("comment");
				    			//Likelist = (List<Map<String,Object>>)ttmap.get("like");
				    			byte[] hisimg = (byte[])ttmap.get("hisimg");
				    			if(hisimg!=null)
				    			{
				    				tempFile = fileCache.getFile(String.valueOf(Math.random()*1000));
				    				tempFile.createNewFile();
				    				byte2File(hisimg,tempFile);
				    				Bitmap bitmapss = decodeFile(tempFile);
				    				ttmap.put("hisimg", bitmapss);
				    			}
				    			for(Map<String,Object> map : Commentlist)
				    			{
				    				
				    				byte[] icon = (byte[])map.get("icon");
				    				
				    				//Log.v("buffer", photo.toString());
				    				tempFile = fileCache.getFile(String.valueOf(Math.random()*1000));
				    				tempFile.createNewFile();
				    				byte2File(icon,tempFile);
				    				Bitmap bitmaps = decodeFile(tempFile);
				    				map.put("icon", bitmaps);
				    				
				    			}
				    			ois.close();
				    			is.close();
				    			fileCache.clear();
				    			getData();
				    			
				    		}
				    		else
				    		{
				    			Toast.makeText(OldObjectCommentActivity.this, "URL验证失败", Toast.LENGTH_SHORT).show();
				    			Message msg = handler1.obtainMessage();
				    			msg.what = 0;
				    			handler1.sendMessage(msg);
				    		}
				    	}catch(Exception e)
				    	{
				    		e.printStackTrace();
				    	}
				    	
			}
			
		}).start();
				
	}
	
	protected void getData() {
		// TODO 自动生成的方法存根
		messages.clear();
		//boolean isliked = false;
		List<HeadM> likes = new ArrayList<HeadM>();
		if (Commentlist.isEmpty())
			;
		else
		{
			for(Map<String, Object>map:Commentlist)
			{
				long id = (Long)map.get("myAcct");
				Bitmap icon = (Bitmap)map.get("icon");
				String text = (String)map.get("text");
				String time = ((Timestamp)map.get("time")).toString();
				String name = (String)map.get("name");
				messages.add(new OldObjectcommentM(id,icon,name,text,time));
			}
		}
		
		
		hero.setComnum(Commentlist.size());
		//hero.setNum(Likelist.size());
		hero.setMessageId(messageId);
		//hero.setIsLiked(isliked);
		hero.setNames(likes);
		if(ttmap.get("hisimg")!=null)
			hero.setHisimg((Bitmap)ttmap.get("hisimg"));
		Message msg = handler1.obtainMessage();
		msg.what =1;
		handler1.sendMessage(msg);
		//pv.onHeaderRefreshComplete();
		
	}
	
	Handler handler1 = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:

				pv.onHeaderRefreshComplete();
				break;
			
			case 1:
				oia.notifyDataSetChanged();
				pv.onHeaderRefreshComplete();
				break;
				
			
			
			}
			
		}
	};
	
	
	
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
	
	
	public PullToRefreshView getPV()
	{
		return pv;
	}
}
