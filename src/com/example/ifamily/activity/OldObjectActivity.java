package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicNameValuePair;

import com.example.ifamily.R;
import com.example.ifamily.adapter.OldItemAdapter;
import com.example.ifamily.adapter.OlditemIadapter;
import com.example.ifamily.message.HeadM;
import com.example.ifamily.message.HolesAtmeM;
import com.example.ifamily.message.OldObjectMessage;
import com.example.ifamily.message.Zonemessage;
import com.example.ifamily.mywidget.MyGridView;
import com.example.ifamily.mywidget.Mylistview;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.PullToRefreshView.OnFooterRefreshListener;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioGroup;

public class OldObjectActivity extends Fragment implements OnHeaderRefreshListener,OnFooterRefreshListener{


	private RadioGroup rg;
	private Mylistview gv;
	private List<Map<String,Object>> theirObject = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> myObject = new ArrayList<Map<String,Object>>();
	private PullToRefreshView pv;
	private File tempfile;
	private FileCache fileCache = new FileCache(this.getActivity());
	private int radioButtonId = R.id.addtext;
	private int times = 1;
	private SharedPreferences sp;
	private String lasttime = "";
	private OldItemAdapter oia;
	private OlditemIadapter oiia;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater
				.inflate(R.layout.o_oldobject, container, false);
		return view;}
		 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
		
		 initview();	
	}

	
	
	List<OldObjectMessage> messages=new ArrayList<OldObjectMessage>();
	 
		
		protected void initview()
		{
			RelativeLayout all=(RelativeLayout)getView().findViewById(R.id.all);//整个界面
			FontManager.changeFonts(all,this.getActivity());
			pv = (PullToRefreshView)getView().findViewById(R.id.test_Grid2);
			 pv.setOnHeaderRefreshListener(this);
			 pv.setOnFooterRefreshListener(this);
			
			
			rg=(RadioGroup)getView().findViewById(R.id.radioGroup1);
			rg.setFocusable(true);
			rg.setFocusableInTouchMode(true);
			rg.requestFocus();
			
			
			
			gv=(Mylistview)getView().findViewById(R.id.o_old_objects);
			oia=new OldItemAdapter(this.getActivity(),messages);
			oiia=new OlditemIadapter(this.getActivity(),messages);
			
			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	             @Override
	               public void onCheckedChanged(RadioGroup arg0, int arg1) {
	                   //获取变更后的选中项的ID
	            	 times = 1;
	                   radioButtonId = arg0.getCheckedRadioButtonId();
	                 //根据ID获取RadioButton的实例
	                    RadioButton rb = (RadioButton)OldObjectActivity.this.getActivity().findViewById(radioButtonId);
	                  //更新文本内容，以符合选中项
	                 switch(radioButtonId)
	                 {
	                 case R.id.mine:
	                		//initdata();
	                	 messages.clear();
	                	 oia.notifyDataSetChanged();
	                	 gv.setAdapter(oiia);
	            			//getmyadapter();
	                	 pv.headerRefreshing();
	            			break;
	                 case R.id.addtext:
	         			//initdata();
	                	 messages.clear();
	                	 oiia.notifyDataSetChanged();
	                	 gv.setAdapter(oia);
	        			//gettheiradapter();
	                	 pv.headerRefreshing();
	                 }
	
	                  
	              }
	         });
			
			
			
			gv.setFocusable(true);
            gv.setOnItemClickListener(new OnItemClickListener(){  
	   	  
	        	
	            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {  

	            }  
			
	        });  
 			//initdata();
			gv.setAdapter(oia);
			//gettheiradapter();
			pv.headerRefreshing();

		}

		protected void  initdata()
		{
			messages.clear();
	/////////实际上用到的 id,头像，图片，名字，内容，时间，状态（评论条数可有可无）
			//public  OldObjectMessage(long account,Object heads,Object imgs,String names,String text,String time,int state)
			OldObjectMessage oom1=new OldObjectMessage(1234,R.drawable.defalt_head,R.drawable.login_bg,"cc","书本","2007 8/29",1,12,1);
			OldObjectMessage oom2=new OldObjectMessage(1234,R.drawable.defalt_head,R.drawable.gauss0,"cc","书本","2007 8/29",2,13,1);
			OldObjectMessage oom3=new OldObjectMessage(1234,R.drawable.defalt_head,R.drawable.gauss0,"cc","书本","2007 8/29",1,14,1);
			OldObjectMessage oom4=new OldObjectMessage(1234,R.drawable.defalt_head,R.drawable.login_bg,"cc","书本","2007 8/29",2,15,1);
			OldObjectMessage oom5=new OldObjectMessage(1234,R.drawable.defalt_head,R.drawable.gauss0,"cc","书本","2007 8/29",1,16,1);
			messages.add(oom1);
			messages.add(oom2);
			messages.add(oom3);
			messages.add(oom4);
			messages.add(oom5);					
		}
		
		
		 
	    @Override  
	    public void onResume() {  
			 super.onResume();
		
			 Context ctx = getActivity();       
			  SharedPreferences sps = ctx.getSharedPreferences("SP", Context.MODE_PRIVATE);
			  
			  if(sps.getBoolean("isoldadd", false)!=true){		  		  
			  Editor editor = sps.edit();
			  editor.putBoolean("isoldadd",false);
			  editor.commit();				 
			  }
			  else{
				  pv.headerRefreshing();
				  Editor editor = sps.edit();
				  editor.putBoolean("isoldadd",false);
				  editor.commit();	
			  }
			
	    }
		protected void gettheiradapter()
		{


			try {
				
				RequestParams params = new RequestParams();
				//params.put("sex", sex);
				
				SharedPreferences sp = this.getActivity().getSharedPreferences("user",0);
				String username = sp.getString("username", "");
				//params.put("gname", groupname.getText().toString());
				params.put("uname", username);
				params.put("requesttype", "1");
				params.put("times",String.valueOf(times));
		    	params.put("lasttime",lasttime);
				//params.put("gdetail", groupInfo.getText().toString());
				//params.put("hasphoto", String.valueOf(hasphoto));
				String url = "http://103.31.241.201:8080/IFamilyServer/OldObjectServlet";

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
				    				byte[] photo = (byte[])map.get("photo");
				    				if(photo!=null)
				    				{
				    				tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
				    				tempfile.createNewFile();
				    				byte2File(photo,tempfile);
				    				Bitmap bitmap = decodeFile(tempfile);
				    				map.put("photo", bitmap);
				    				}
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
								Toast.makeText(OldObjectActivity.this.getActivity(),
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
						Toast.makeText(OldObjectActivity.this.getActivity(),
								"网络访问异常，错误码  > " + statusCode, 0).show();

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		
		
			
			
			
		}
		
		
		private List<OldObjectMessage> getData(List<Map<String,Object>> list) {
			 List<OldObjectMessage> result = new ArrayList<OldObjectMessage>();
			 if(list.isEmpty())
			 {
			 }
			 else{
				 
				 String name,text;
				 Bitmap hisimg,icon;
				 int messageID,comnum,state;
				 long id;
				 
				 
				 List<Map<String,Object>> like = new ArrayList<Map<String,Object>>();
				 
				 for(Map<String, Object> map : list)
				 {
					
					 
					 
					
						 hisimg = (Bitmap)map.get("photo");
						 icon = (Bitmap)map.get("icon");
						 id = (Long)map.get("myAcct");
						 text = (String)map.get("text");
						 lasttime = ((Timestamp)map.get("uploadTime")).toString();
						 name = (String)map.get("name");
						 messageID = (Integer)map.get("messageID");
						 state = (Integer)map.get("state");
						
						 comnum = (Integer)map.get("comnum");
						 
						 result.add(new OldObjectMessage(id,icon,hisimg,name,text,lasttime,state,comnum,messageID));
					 
					 
				 }

			 }
			  return result;
		  }
		
		
		
		
		
		protected void getmyadapter()
		{
			try {
				
				RequestParams params = new RequestParams();
				//params.put("sex", sex);
				
				SharedPreferences sp = this.getActivity().getSharedPreferences("user",0);
				String username = sp.getString("username", "");
				//params.put("gname", groupname.getText().toString());
				params.put("uname", username);
				params.put("requesttype", "2");
				params.put("times",String.valueOf(times));
		    	params.put("lasttime",lasttime);
				//params.put("gdetail", groupInfo.getText().toString());
				//params.put("hasphoto", String.valueOf(hasphoto));
				String url = "http://103.31.241.201:8080/IFamilyServer/OldObjectServlet";

				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						try {
							if (statusCode == 200) {
								InputStream is = new ByteArrayInputStream(responseBody);
								ObjectInputStream ois = new ObjectInputStream(is);
								
								myObject = (List<Map<String,Object>>)ois.readObject();
				    			
				    			for(Map<String,Object> map : myObject)
				    			{
				    				byte[] photo = (byte[])map.get("photo");
				    				if(photo!=null)
				    				{
				    				tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
				    				tempfile.createNewFile();
				    				byte2File(photo,tempfile);
				    				Bitmap bitmap = decodeFile(tempfile);
				    				map.put("photo", bitmap);
				    				}
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
				    			messages.addAll(getData(myObject));
				    			oiia.notifyDataSetChanged();
				    			

								//Toast.makeText(OldObjectActivity.this.getActivity(), "性别修改成功！", Toast.LENGTH_SHORT).show();
								//		.show();
								//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
								//startActivity(intent);
							} else {
								Toast.makeText(OldObjectActivity.this.getActivity(),
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
						Toast.makeText(OldObjectActivity.this.getActivity(),
								"网络访问异常，错误码  > " + statusCode, 0).show();

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			// TODO 自动生成的方法存根
			times = 2;
			switch(radioButtonId)
			{
			case R.id.mine:
				getmyadapter();
				break;
			case R.id.addtext:
				gettheiradapter();
				break;
			}
		}

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			// TODO 自动生成的方法存根
			times = 1;
			switch(radioButtonId)
			{
			case R.id.mine:
				getmyadapter();
				break;
			case R.id.addtext:
				gettheiradapter();
				break;
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
