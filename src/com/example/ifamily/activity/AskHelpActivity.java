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
import com.example.ifamily.adapter.AskhelpAdapter;
import com.example.ifamily.adapter.OldItemAdapter;
import com.example.ifamily.message.HolesWishM;
import com.example.ifamily.message.OldObjectMessage;
import com.example.ifamily.message.QuestionM;
import com.example.ifamily.mywidget.Mylistview;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.PullToRefreshView.OnFooterRefreshListener;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AskHelpActivity extends Activity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	Mylistview lv;
	private TextView title;
	private Button back;
	private Button btnadd;
	private Button atme;
	List<QuestionM> messages=new ArrayList<QuestionM>();
	private List<Map<String,Object>> theirObject = new ArrayList<Map<String,Object>>();
	private int times = 1;
	private String lasttime = "";
	private File tempfile;
	private FileCache fileCache = new FileCache(this);
	private PullToRefreshView pv;
	private RelativeLayout all;
	private AskhelpAdapter oia;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			MyApplication.getInstance().addActivity(this);
			setContentView(R.layout.askhelp);
			initview();			
		}
		protected void initview()
		{
			pv = (PullToRefreshView)findViewById(R.id.o_old_pullview);
			 pv.setOnHeaderRefreshListener(this);
			 pv.setOnFooterRefreshListener(this);
			
			all=(RelativeLayout)findViewById(R.id.mainView);
			FontManager.changeFonts(all,this);
			atme=(Button)findViewById(R.id.btnatme);
			
			 atme.setOnClickListener(new OnClickListener()
			 {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(AskHelpActivity.this,AtMeActivity.class);
					 startActivity(intent);
				
				}
				 
			 });
			
			
			
			btnadd=(Button)findViewById(R.id.btnAdd);
			 title = (TextView)findViewById(R.id.title);
			 back = (Button)findViewById(R.id.back);
			 title.setText("床头故事");
			 back.setOnClickListener(new OnClickListener()
			 {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
					 startActivity(intent);
					 OldObjectMain.this.finish();*/
					AskHelpActivity.this.finish();
				}
				 
			 });
			 
			 btnadd.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub添加加号点击事件
						
						Intent it=new Intent(AskHelpActivity.this,AskHelpAdd.class);
						startActivity(it);
					
						
					}
					
				});
			
			 
			 
			 
			lv=(Mylistview)findViewById(R.id.ah_list);	
			
			initdata();
			
		}
		
		 @Override  
		    public void onResume() {  
				 super.onResume();
				
				  SharedPreferences sps = AskHelpActivity.this.getSharedPreferences("SP", Context.MODE_PRIVATE);
				  
				  if(sps.getBoolean("isaskhelp", false)!=true){		  		  
				  Editor editor = sps.edit();
				  editor.putBoolean("isaskhelp",false);
				  editor.commit();				 
				  }
				  else{
					  pv.headerRefreshing();
					  Editor editor = sps.edit();
					  editor.putBoolean("isaskhelp",false);
					  editor.commit();	
				  }
				
		    }

		protected void initdata()
		{
			//public  QuestionM(long account,Object heads,String names,String time,String questions,int comnum)
			//QuestionM oom2=new QuestionM(1234,R.drawable.n1,"爷爷","2014 7/29","昨天去坐公交车，一个年轻的小伙子看见我进去就让了座。虽然现在很多人都对这种事情很冷漠，但是世上还是有很不错的人的，",21);
			//QuestionM oom3=new QuestionM(1234,R.drawable.v1,"奶奶","2014 7/29","前两天有人和我抱怨睡不着觉，想当年三年自然灾害的时候，一天到晚都得外面做工，眼睛一闭就能睡着，旁边得车轰轰作响都能睡着。",24);
			//QuestionM oom4=new QuestionM(1234,R.drawable.n1,"爷爷","2014 7/28","最近睡不着觉，爬山屋顶夜观天象，感觉近期会有陨石砸过来，我们是不是应该屯粮了",21);
			//messages.add(oom2);
			//messages.add(oom3);
			//messages.add(oom4);
			oia=new AskhelpAdapter(this.getApplicationContext(),messages);
			lv.setAdapter(oia);
			pv.headerRefreshing();
			
		}
		
		
		protected void getList()
		{
			try {
				
				RequestParams params = new RequestParams();
				//params.put("sex", sex);
				
				SharedPreferences sp = this.getSharedPreferences("user",0);
				String username = sp.getString("username", "");
				//params.put("gname", groupname.getText().toString());
				params.put("uname", username);
				params.put("requesttype", "1");
				params.put("times",String.valueOf(times));
		    	params.put("lasttime",lasttime);
				//params.put("gdetail", groupInfo.getText().toString());
				//params.put("hasphoto", String.valueOf(hasphoto));
				String url = "http://103.31.241.201:8080/IFamilyServer/StoryServlet";

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
				    			
				    			for(Map<String,Object> map : theirObject)
				    			{
				    				
				    				byte[] icon = (byte[])map.get("icon");
				    				
				    				//Log.v("buffer", photo.toString());
				    				tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
				    				tempfile.createNewFile();
				    				byte2File(icon,tempfile);
				    				Bitmap bitmaps = decodeFile(tempfile);
				    				map.put("icon", bitmaps);
				    				
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
				    			oia.notifyDataSetChanged();
				    			

								//Toast.makeText(OldObjectActivity.this.getActivity(), "性别修改成功！", Toast.LENGTH_SHORT).show();
								//		.show();
								//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
								//startActivity(intent);
							} else {
								Toast.makeText(AskHelpActivity.this,
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
						Toast.makeText(AskHelpActivity.this,
								"网络访问异常，错误码  > " + statusCode, 0).show();

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		private List<QuestionM> getData(List<Map<String,Object>> list) {
			 List<QuestionM> result = new ArrayList<QuestionM>();
			 if(list.isEmpty())
			 {
			 }
			 else{
				 
				 String name,text;
				 Bitmap icon;
				 int messageID,comnum;
				 long id;
				 
				 
				 List<Map<String,Object>> like = new ArrayList<Map<String,Object>>();
				 
				 for(Map<String, Object> map : list)
				 {
					
					 
					 
					
						 
						 icon = (Bitmap)map.get("icon");
						 id = (Long)map.get("myAcct");
						 text = (String)map.get("text");
						 lasttime = ((Timestamp)map.get("uploadTime")).toString();
						 name = (String)map.get("name");
						 messageID = (Integer)map.get("messageID");
						 //state = (Integer)map.get("state");
						
						 comnum = (Integer)map.get("comnum");
						 
						 result.add(new QuestionM(id,icon,name,lasttime,text,comnum,messageID));
					 
					 
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
