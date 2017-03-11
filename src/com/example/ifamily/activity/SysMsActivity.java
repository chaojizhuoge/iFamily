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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import com.example.ifamily.R;
import com.example.ifamily.adapter.SysLvAdapter;
import com.example.ifamily.message.QuestionM;
import com.example.ifamily.message.Sysmessage;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.PullToRefreshView.OnFooterRefreshListener;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SysMsActivity extends Activity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	private PullToRefreshView pv;
	private int times = 1;
	private String lasttime = "";
	private File tempfile;
	private FileCache fileCache = new FileCache(this);
	private List<Map<String,Object>>theirObject = new ArrayList<Map<String,Object>>();
	
	private ListView lv;
	private TextView title;
	private Button back;
	 private List<Sysmessage> messages = new ArrayList<Sysmessage>();
	 private SysLvAdapter gd;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.p_sysmessage);
		initView();
	}
	private void initView()
	{
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//整个界面
		pv = (PullToRefreshView)findViewById(R.id.test_Grid);
		 pv.setOnHeaderRefreshListener(this);
		 pv.setOnFooterRefreshListener(this);
		FontManager.changeFonts(all,this);
		lv=(ListView)findViewById(R.id.messages);
		 title = (TextView) findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("系统通知");
		 back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SysMsActivity.this.finish();
			}
			 
		 });
		 initmessage();
		 lv.setAdapter(gd);		
		 pv.headerRefreshing();
	}
	
	private void initmessage()
	{
		String time=Utils.getCurrentTime();
		//public  Sysmessage(String time,String groupname,String hisnames,int groupid,int hisid,int type)
		
		gd=new SysLvAdapter(this,messages);
	}
	
	protected void getList()
	{
		try {
			
			RequestParams params = new RequestParams();
			//params.put("sex", sex);
			
			SharedPreferences sp = this.getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("user1", username);
			params.put("requesttype", "5");
			params.put("times",String.valueOf(times));
	    	params.put("lasttime",lasttime);
			//params.put("gdetail", groupInfo.getText().toString());
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
							
							theirObject = (List<Map<String,Object>>)ois.readObject();
			    			if(theirObject.isEmpty())
			    			{
			    				if(times == 1)
				    			{
				    				messages.clear();
				    				pv.onHeaderRefreshComplete();
				    			}
				    			else
				    			{
				    				pv.onFooterRefreshComplete();
				    			}
			    				return;
			    			}
			    			
			    			ois.close();
			    			is.close();
			    			fileCache.clear();
			    			
			    			if(times == 1)
			    			{
			    				messages.clear();
			    				pv.onHeaderRefreshComplete();
			    			}
			    			else
			    			{
			    				pv.onFooterRefreshComplete();
			    			}
			    			messages.addAll(getData(theirObject));
			    			gd.notifyDataSetChanged();
			    			

							//Toast.makeText(OldObjectActivity.this.getActivity(), "性别修改成功！", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(SysMsActivity.this,
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
					Toast.makeText(SysMsActivity.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private List<Sysmessage> getData(List<Map<String,Object>> list) {
		 List<Sysmessage> result = new ArrayList<Sysmessage>();
		 if(list.isEmpty())
		 {
		 }
		 else{
			 
			 String name,groupname;
			 Bitmap icon;
			 int groupId,type;
			 long hisid;
			 
			 
			 //List<Map<String,Object>> like = new ArrayList<Map<String,Object>>();
			 
			 for(Map<String, Object> map : list)
			 {
				
				 
				 
				
					 
					 
					 hisid = (Long)map.get("user2");
					 groupId = (Integer)map.get("groupId");
					 lasttime = ((Timestamp)map.get("uploadTime")).toString();
					 name = (String)map.get("name");
					groupname = (String)map.get("groupname");
					 type = (Integer)map.get("state");
					
					
					 
					 result.add(new Sysmessage(lasttime,groupname,name,groupId,hisid,type));
				 
				 
			 }

		 }
		  return result;
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
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO 自动生成的方法存根
		times = 2;
		getList();
	}
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO 自动生成的方法存根
		times = 1;
		getList();
	}
}
