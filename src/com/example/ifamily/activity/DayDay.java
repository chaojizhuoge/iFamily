package com.example.ifamily.activity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import com.example.ifamily.R;
import com.example.ifamily.adapter.DayDayAdapter;
import com.example.ifamily.message.DayDayM;
import com.example.ifamily.mywidget.Mylistview;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.PullToRefreshView.OnFooterRefreshListener;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class DayDay  extends Fragment implements OnHeaderRefreshListener, OnFooterRefreshListener{
	private Mylistview lv;
	private List<DayDayM> messages=new ArrayList<DayDayM>();
	private LinearLayout all;
	private List<Map<String,Object>> theirObject = new ArrayList<Map<String,Object>>();
	private int times = 1;
	private String lasttime = "";
	private File tempfile;
	private FileCache fileCache = new FileCache(this.getActivity());
	private DayDayAdapter da;
	private PullToRefreshView pv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater
				.inflate(R.layout.d_day_day, container, false);
		return view;}
		 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
		initview();
		
	}
	

	protected void initview()
	{
		pv = (PullToRefreshView)getView().findViewById(R.id.test_Grid2);
		 pv.setOnHeaderRefreshListener(this);
		 pv.setOnFooterRefreshListener(this);
		all=(LinearLayout)getView().findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this.getActivity());
		
		lv=(Mylistview)getView().findViewById(R.id.d_day_day);			
		lv.setFocusable(true);
        lv.setOnItemClickListener(new OnItemClickListener(){  
   	  
        	
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {  
            	

            }  
		
        });  
		gettoadapter();
		da=new DayDayAdapter(this.getActivity(),messages);
		lv.setAdapter(da);

	}

	protected void  initdata()
	{
		messages.clear();

		//messages.add(new DayDayM("2012 3/23","ccc",123456,1));
		//messages.add(new DayDayM("2012 3/23","ccc",123456,2));
		//messages.add(new DayDayM("2012 3/23","ccc",123456,3));
		//messages.add(new DayDayM("2012 3/23","ccc",123456,4));
		//messages.add(new DayDayM("2012 3/23","ccc",123456,5));					
	}
	
	protected void gettoadapter()
	{


		try {
			
			RequestParams params = new RequestParams();
			//params.put("sex", sex);
			
			SharedPreferences sp = this.getActivity().getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("uname", username);
			params.put("requesttype", "4");
			params.put("times",String.valueOf(times));
	    	params.put("lasttime",lasttime);
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
							
							theirObject = (List<Map<String,Object>>)ois.readObject();
			    			
			    			
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
			    			da.notifyDataSetChanged();
			    			

							//Toast.makeText(OldObjectActivity.this.getActivity(), "性别修改成功！", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(DayDay.this.getActivity(),
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
					Toast.makeText(DayDay.this.getActivity(),
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
		
		
		
	
		
		
		
	}
	
	private List<DayDayM> getData(List<Map<String,Object>> list) {
		 List<DayDayM> result = new ArrayList<DayDayM>();
		 if(list.isEmpty())
		 {
		 }
		 else{
			 
			 String name,text,birthday;
			 Bitmap hisimg,icon;
			 int messageID,comnum,state;
			 long id;
			 
			 
			 
			 for(Map<String, Object> map : list)
			 {
				
				 
				 
				
					
					 
					 id = (Long)map.get("myAcct");
					 
					 birthday = ((Date)map.get("birthday")).toString();
					 name = (String)map.get("name");
					
					 state = (Integer)map.get("state");
					
					 //comnum = (Integer)map.get("comnum");
					 
					 result.add(new DayDayM(birthday,name,id,state));
					 
				 
				 
			 }

		 }
		 return result;
	  }

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO 自动生成的方法存根
		times = 2;
		gettoadapter();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO 自动生成的方法存根
		times = 1;
		gettoadapter();
	}
	
	
}
