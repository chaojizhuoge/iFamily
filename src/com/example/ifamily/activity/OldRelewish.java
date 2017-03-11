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
import com.example.ifamily.adapter.HolesWishAdapter;
import com.example.ifamily.message.HolesWishM;
import com.example.ifamily.message.OldObjectMessage;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.PullToRefreshView.OnFooterRefreshListener;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class OldRelewish extends Fragment implements OnHeaderRefreshListener,OnFooterRefreshListener{

	private int times = 1;

	private String lasttime = "";
	
	private List<Map<String, Object>> theirObject;
	
	private File tempfile;
	private FileCache fileCache = new FileCache(this.getActivity());
	private HolesWishAdapter oia;
	private PullToRefreshView pv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
	
		View view = inflater
				.inflate(R.layout.holes_wish, container, false);
		
		return view;
		}
	 ListView lv;
		
		List<HolesWishM> messages=new ArrayList<HolesWishM>();

		
		 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
		 initview();
	
	}
	
	protected void initview()
	{
		LinearLayout all=(LinearLayout)getView().findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this.getActivity());
		lv=(ListView)getView().findViewById(R.id.h_lv);			
		pv = (PullToRefreshView)getView().findViewById(R.id.test_Grid);
		 pv.setOnHeaderRefreshListener(this);
		 pv.setOnFooterRefreshListener(this);
		initdata();
		
	}
	protected void initdata()
	{
		//public  HolesWishM(long account,Object heads,String names,String wishes,String time,int comnum)

		oia=new HolesWishAdapter(this.getActivity(),messages);
		lv.setAdapter(oia);
		pv.headerRefreshing();
		
	}
	
	protected void getList()
	{
		try {
			
			RequestParams params = new RequestParams();
			//params.put("sex", sex);
			
			SharedPreferences sp = this.getActivity().getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("uname", username);
			params.put("requesttype", "9");
			params.put("times",String.valueOf(times));
	    	params.put("lasttime",lasttime);
			//params.put("gdetail", groupInfo.getText().toString());
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/HolesServlet";

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
							Toast.makeText(OldRelewish.this.getActivity(),
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
					Toast.makeText(OldRelewish.this.getActivity(),
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private List<HolesWishM> getData(List<Map<String,Object>> list) {
		 List<HolesWishM> result = new ArrayList<HolesWishM>();
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
					 
					 result.add(new HolesWishM(id,icon,name,text,lasttime,comnum,messageID));
				 
				 
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
