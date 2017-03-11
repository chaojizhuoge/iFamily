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
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
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

import com.example.ifamily.AddPopWindow;
import com.example.ifamily.DIPXPconvert;
import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.activity.FamilyzoneActivity.LoginThread;
import com.example.ifamily.adapter.GridHeadsAdapter;
import com.example.ifamily.adapter.GroupListAdapter;
import com.example.ifamily.adapter.GroupLvAdapter;
import com.example.ifamily.adapter.Zonelistadapter;
import com.example.ifamily.message.GroupLMessage;
import com.example.ifamily.message.GroupListM;
import com.example.ifamily.message.HeadM;
import com.example.ifamily.message.Zonemessage;
import com.example.ifamily.mywidget.AutoListView;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.PullToRefreshView.OnFooterRefreshListener;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class NoteActivity extends Fragment implements OnHeaderRefreshListener,OnFooterRefreshListener,OnClickListener{

	//提示内容
	  private ImageView tishi;
	  boolean isFirstIn = false;
	  private TextView iknow;
	  private RelativeLayout reee;	
	  private List<Zonemessage> messages = new ArrayList<Zonemessage>();
	  private Zonelistadapter za;
	  private ListView glv;	
	  private RelativeLayout top;
	  private Button addButton;
	  private LinearLayout ll;
	  private PullToRefreshView pv;
	  private List<HeadM> friends=new ArrayList<HeadM>();
	  private List<GroupListM> gm=new ArrayList<GroupListM>();
	  private GroupListAdapter ga;
	  private List<HeadM> heads=new ArrayList<HeadM>();
	  private PopupWindow  popupWindow;
	  private TextView groupsel;
	  private String groupId = "";
	  private boolean first = true;
		private RelativeLayout all;
	  
	  private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
		private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
		private static final int PHOTO_REQUEST_CUT = 3;// 结果

		
		private Bitmap bitmap;

		/* 头像名称 */
		private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
		private File tempFile;
		
		private ProgressDialog mDialog;
		
		private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		private List<Map<String,Object>> glist = new ArrayList<Map<String,Object>>();;
		private static final int REQUEST_TIMEOUT = 10*1000;//设置请求超时10秒钟  
		private static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟
		
		private int times;
		private SharedPreferences sp;
		private String lasttime;
		private File tempfile;
		private FileCache fileCache;
		SelectPicPopupWindow menuWindow;
		
		//private Button addButton;
	   // private List<Zonemessage> messages = new ArrayList<Zonemessage>();;
	    private AutoListView Lv;
	   // private Zonelistadapter za;
	   // private GridHeadsAdapter ga;
	  //  private List<HeadM> imgs=new ArrayList<HeadM>();
	    private GridView gv;
	    
	    
	    
	    private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				List<Zonemessage> result = (List<Zonemessage>) msg.obj;
				switch (msg.what) {
				case AutoListView.REFRESH:
					//glv.onRefreshComplete();
					pv.onHeaderRefreshComplete();
					messages.clear();
					messages.addAll(result);
					break;
				case AutoListView.LOAD:
					//Lv.onLoadComplete();
					pv.onFooterRefreshComplete();
					messages.addAll(result);
					break;
				}
				//glv.setResultSize(result.size());
				za.notifyDataSetChanged();
			};
		};
		public static int px2dip(Context context, float pxValue){ 
	        final float scale = context.getResources().getDisplayMetrics().density; 
	        return (int)(pxValue / scale + 0.5f); 
	}

	    public static int dip2px(Context context, float dipValue){ 
	        final float scale = context.getResources().getDisplayMetrics().density; 
	        return (int)(dipValue * scale + 0.5f); 
	} 
	  
	  
	  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.grow_note_2,
				container, false);
		return messageLayout;
	}
	
	 public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
		 initview();
	
	     
	 }
	 private void initview()
	 {
		 
		 tishi=(ImageView)getView().findViewById(R.id.tishi);
		 iknow=(TextView)getView().findViewById(R.id.iknow);
		 reee=(RelativeLayout)getView().findViewById(R.id.reee);

			// 判断程序与第几次运行
		  Context ctx = getActivity();       
		  SharedPreferences sps = ctx.getSharedPreferences("SP", Context.MODE_PRIVATE);
		  
		  if(sps.getBoolean("BOOLEAN_KEY", false)!=true){		  		  
		  Editor editor = sps.edit();
		  editor.putBoolean("BOOLEAN_KEY",true);
		  editor.commit();	
		 reee.setVisibility(View.VISIBLE);				 
		  }
		  else{
			  reee.setVisibility(View.GONE);
		  }

			
			
			iknow.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					reee.setVisibility(View.GONE);
					
				}
				
			});
		 ////////
				 
		 pv = (PullToRefreshView)getView().findViewById(R.id.test_Grid);
		 pv.setOnHeaderRefreshListener(this);
		 pv.setOnFooterRefreshListener(this);
		 times = 1;	
		 lasttime="";
		 fileCache = new FileCache(this.getActivity());
		 top=(RelativeLayout)getView().findViewById(R.id.top);
		 ll=(LinearLayout)getView().findViewById(R.id.ll);
		addButton = (Button)getView().findViewById(R.id.btnAdd);
		addButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					addpop();
				}
				
			}
			);
		 glv = (ListView) getView().findViewById(R.id.zonelist);
		 groupsel=(TextView) getView().findViewById(R.id.select_group);		 
	     setAdapterForThis();
	     /*glv.setOnItemClickListener(new OnItemClickListener(){  
	   	  
	        	
	            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {  
	            	Intent intent=new Intent(arg1.getContext(),ZoneCommentActivity.class);
					startActivity(intent);	
	      
	            }  
			
	        }); */
	     groupsel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				reee.setVisibility(View.GONE);
				selectgroup();//选择空间
				
			}
	    	 
	     });
	     
			all=(RelativeLayout)getView().findViewById(R.id.mainView);
			FontManager.changeFonts(all, this.getActivity());
			FontManager.changeFonts(glv, this.getActivity());
	     
	 }
	 
	 
	    @Override  
	    public void onResume() {  
			 super.onResume();
			  Context ctx = getActivity();       
			  SharedPreferences sps = ctx.getSharedPreferences("SP", Context.MODE_PRIVATE);
			  
			  if(sps.getBoolean("ifrefresh", false)!=true){		  		  
			  Editor editor = sps.edit();
			  editor.putBoolean("ifrefresh",false);
			  editor.commit();				 
			  }
			  else{
				  pv.headerRefreshing();
				  Editor editor = sps.edit();
				  editor.putBoolean("ifrefresh",false);
				  editor.commit();	
			  }
			
	    }
	 
	 

	 
	 
	 
	 
	 private void setAdapterForThis() {
		 //initheads();
		 initgroupmessage();
		    try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块S
				e.printStackTrace();
			}
		    //initheads();
		    
		 initzoneMessages();		
		 
		 
		 }
	 
	 
	 private void initheads()
	 {
		 /*
		    Object ob1=R.drawable.h6;
			Object ob2=R.drawable.h1;
			Object ob3=R.drawable.h2;
			Object ob4=R.drawable.h3;
			Object ob5=R.drawable.h4;
			Object ob6=R.drawable.h5;
			Object ob7=R.drawable.v6;
			Object ob8=R.drawable.v7;
			Object ob9=R.drawable.v8;
			Object ob10=R.drawable.v5;
			Object ob11=R.drawable.n3;
			Object ob12=R.drawable.n6;
			Object ob13=R.drawable.n2;
			friends.add(new HeadM(1,"namedfdfdsafsfds1"));
			friends.add(new HeadM(2,"namefdsafsadfsdafd2"));
			friends.add(new HeadM(3,"namefsadfsafdfsd3"));
			friends.add(new HeadM(4,"namefdsafsafsadfd4"));
			heads.add(new HeadM(1,ob1));//id 和头像
			heads.add(new HeadM(2,ob2));
			heads.add(new HeadM(3,ob3));
			heads.add(new HeadM(4,ob4));
			heads.add(new HeadM(5,ob5));
			heads.add(new HeadM(6,ob6));
			heads.add(new HeadM(6,ob7));
			heads.add(new HeadM(6,ob8));
			
			heads.add(new HeadM(6,ob9));
			heads.add(new HeadM(6,ob10));
			heads.add(new HeadM(6,ob11));
			heads.add(new HeadM(6,ob12));
			heads.add(new HeadM(6,ob13));  */
		 

			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					String urlStr = "http://103.31.241.201:8080/IFamilyServer/GroupServlet";
			    	HttpPost request = new HttpPost(urlStr);
			    	//如果传递参数多的话，可以对传递的参数进行封装
			    	
			    	List<NameValuePair> params = new ArrayList<NameValuePair>();
			    	
			    	//添加用户名和密码
			    	params.add(new BasicNameValuePair("groupId",groupId));
			    	params.add(new BasicNameValuePair("requesttype","1"));
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
			    			//loginValidate = true;
			    			//获得响应信息
			    			//responseMsg = EntityUtils.toString(response.getEntity());
			    			InputStream is = response.getEntity().getContent();
			    			ObjectInputStream ois = new ObjectInputStream(is);
			    			Map m = (Map<String,Object>)ois.readObject();
			    			glist = (List<Map<String,Object>>)m.get("member");
			    			heads.clear();
			    			for(Map<String,Object> map : glist)
			    			{
			    				byte[] icon = (byte[])map.get("icon");
			    				
			    				//Log.v("buffer", photo.toString());
			    				tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
			    				tempfile.createNewFile();
			    				byte2File(icon,tempfile);
			    				Bitmap bitmaps = decodeFile(tempfile);
			    				heads.add(new HeadM((Long)map.get("myAcct"),bitmaps));
			    			}
			    			za.notifyDataSetChanged();
			    			ois.close();
			    			is.close();
			    			
			    			
			    		}
			    		else
			    		{
			    			Toast.makeText(NoteActivity.this.getActivity(), "URL验证失败", Toast.LENGTH_SHORT).show();
			    		}
			    	}catch(Exception e)
			    	{
			    		e.printStackTrace();
			    	}
			    	//return loginValidate;
				}}).start();
			
			
			

		 
	 }
	 
	 private void initzoneMessages() {
		 
	      //messages.clear();
	      //id 头像，昵称，内容，赞的数量，评论数量，
		  //messages.add(new Zonemessage(1,"2012 8/31 22:22:22",R.drawable.defalt_head,R.drawable.login_bg,"name","jfidjfidjfidfjfjdi",13,14));
		//  Zonemessage(int id,Object imgs,String names,String time, String texts,String attime,List<HeadM> friends,String atplace,int num)
		  //messages.add(new Zonemessage(2,R.drawable.defalt_head,"name","2012 8/31 22:22:22","jfidjfidjfidfjfjdi","2012 8/31 22:22:22",friends,"武汉大学",3));
		  //messages.add(new Zonemessage(3,R.drawable.defalt_head,"name","jfidjfidjfidfjfjdi",13,14));
		  //messages.add(new Zonemessage(2,R.drawable.defalt_head,"name","2012 8/31 22:22:22","jfidjfidjfidfjfjdi","2012 8/31 22:22:22",friends,"武汉大学",3));
		  //messages.add(new Zonemessage(5,R.drawable.defalt_head,"name","jfidjfidjfidfjfjdi",13,14));
		  //messages.add(new Zonemessage(6,R.drawable.defalt_head,"name","jfidjfidjfidfjfjdi",13,14));
		  za = new Zonelistadapter(this.getActivity(), messages,heads,R.drawable.myhome,groupId);
		  glv.setAdapter(za);///状态
		  //onHeaderRefresh(pv);
		  if(groupId.equals("")){
				return;
		    }
		  pv.headerRefreshing();
		 }
	 private void addpop()
	 {
		 if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
				popupWindow = null;

				}
		 RelativeLayout  layout = (RelativeLayout) LayoutInflater.from(this.getActivity()).inflate(R.layout.add_popup_dialog, null);  
			   popupWindow = new PopupWindow(ll);
			   LinearLayout addtex = (LinearLayout) layout
						.findViewById(R.id.add_tex);
				LinearLayout addpic = (LinearLayout) layout
						.findViewById(R.id.add_pic);
				LinearLayout addsc = (LinearLayout) layout
						.findViewById(R.id.add_sc);
				addtex.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(NoteActivity.this.getActivity(),AddTextActivity.class);
						intent.putExtra("groupId", groupId);
						NoteActivity.this.getActivity().startActivity(intent);
						popupWindow.dismiss();
						
						}
					
				});

				addpic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						switch(arg0.getId())
						{
						case R.id.add_pic:
							popupWindow.dismiss();
							menuWindow = new SelectPicPopupWindow(NoteActivity.this.getActivity(), this);
							//显示窗口
							menuWindow.showAtLocation(NoteActivity.this.getActivity().findViewById(R.id.mainView), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
							popupWindow.dismiss();
							break;
						case R.id.btn_pick_photo:
							gallery(arg0);
							break;
						case R.id.btn_take_photo:
							camera(arg0);
							break;
						}
					}
				});
				addsc.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(NoteActivity.this.getActivity(),AddschActiivity.class);
						intent.putExtra("groupId", groupId);
						NoteActivity.this.getActivity().startActivity(intent);
						popupWindow.dismiss();
					}
				});			   
		        popupWindow.setWidth(LayoutParams.WRAP_CONTENT);  
		        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);  		    
		        popupWindow.setBackgroundDrawable(new BitmapDrawable());  
		        popupWindow.setOutsideTouchable(true);  
		        popupWindow.setFocusable(true);  
		        popupWindow.setContentView(layout);  
		        popupWindow.showAsDropDown(ll, 0, 0);  
	 
	 }
	 
	 
	 private void selectgroup()
	 {
		 if (popupWindow != null && popupWindow.isShowing()) {

				popupWindow.dismiss();

				popupWindow = null;

				}
			   LinearLayout  layout = (LinearLayout) LayoutInflater.from(this.getActivity()).inflate(R.layout.grouplist, null);  
			   popupWindow = new PopupWindow(groupsel);  
			    final ListView  Lv = (ListView) layout.findViewById(R.id.lv_grouplist);
			    
			    Lv.setAdapter(ga);
		        popupWindow.setWidth(LayoutParams.WRAP_CONTENT);  
		        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);  
		    
		        popupWindow.setBackgroundDrawable(new BitmapDrawable());  

		        popupWindow.setOutsideTouchable(true);  
		        popupWindow.setFocusable(true);  
		        popupWindow.setContentView(layout);  

		        popupWindow.showAsDropDown(groupsel, 0, 0);  
	 
		        Lv.setOnItemClickListener(new OnItemClickListener(){  
		  
		        	
		            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {  
		                // TODO Auto-generated method stub  
		                groupsel.setText(gm.get(arg2).getname());
		                groupId = String.valueOf(gm.get(arg2).getid());
		                
		            	//myfriends.setText(messages.get(arg2).getname());
		                popupWindow.dismiss();  
		                popupWindow = null;  
		            }  
				
		        });  
		        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

		        	public void onDismiss() {
		        	// TODO Auto-generated method stub
		        		initgroupmessage();
		        		//initzoneMessages();
		        		//initheads();
		        		
		        		pv.headerRefreshing();
		        		//za.setBG();
		        		
		        		//za.notifyDataSetChanged();
		        	}
		        	});
	 }
	 ///组别
private void initgroupmessage()
{
	new Thread(new Runnable(){

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			String urlStr = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";
	    	HttpPost request = new HttpPost(urlStr);
	    	//如果传递参数多的话，可以对传递的参数进行封装
	    	sp = NoteActivity.this.getActivity().getSharedPreferences("user",0);
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	
	    	//添加用户名和密码
	    	params.add(new BasicNameValuePair("uname",sp.getString("username", "")));
	    	params.add(new BasicNameValuePair("requesttype","4"));
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
	    			//loginValidate = true;
	    			//获得响应信息
	    			//responseMsg = EntityUtils.toString(response.getEntity());
	    			InputStream is = response.getEntity().getContent();
	    			ObjectInputStream ois = new ObjectInputStream(is);
	    			
	    			glist = (List<Map<String,Object>>)ois.readObject();
	    			gm.clear();
	    			for(Map<String,Object> map : glist)
	    			{
	    				gm.add(new GroupListM(((String)map.get("groupName")),((Integer)map.get("groupID"))));
	    			}
	    			ois.close();
	    			is.close();
	    			ga=new GroupListAdapter(NoteActivity.this.getActivity().getApplicationContext(), gm);
	    			if(!glist.isEmpty())
	    			{
	    				if(first){
	    			groupId = String.valueOf((Integer)glist.get(glist.size()-1).get("groupID"));
	    			groupsel.setText((String)glist.get(glist.size()-1).get("groupName"));
	    				
	    			first = false;}
	    			}
	    			else{
	    				groupsel.setText("空间");
	    				Toast.makeText(NoteActivity.this.getActivity(), "您还没加入任何家庭，在个人中心可以创建或加入家庭哦~", Toast.LENGTH_LONG).show();
	    				return;
	    			}
	    			//initheads();
	    		}
	    		else
	    		{
	    			Toast.makeText(NoteActivity.this.getActivity(), "URL验证失败", Toast.LENGTH_SHORT).show();
	    		}
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    	//return loginValidate;
		}}).start();
	
	
	
}








private void loadData(final int what) {
	new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = handler.obtainMessage();
			msg.what = what;
			msg.obj = getData();				
			handler.sendMessage(msg);
		}
	}).start();
}


@Override
public void onFooterRefresh(PullToRefreshView view) {
	// TODO 自动生成的方法存根
	//view.setOnFooterRefreshListener(this);
	times = 2;
	//Thread thread = new Thread(new LoginThread());
	//thread.start();
	getlist();
}

@Override
public void onHeaderRefresh(PullToRefreshView view) {
	// TODO 自动生成的方法存根
	//view.setOnHeaderRefreshListener(NoteActivity.this);
	times = 1;
	mDialog = new ProgressDialog(this.getActivity());
	mDialog.setTitle("登录");
	mDialog.setMessage("正在登录服务器，请稍后...");
	mDialog.show();
	//Thread loginThread = new Thread(new LoginThread());

	//loginThread.start();
	getlist();
}
	
private void getlist() {
	// TODO 自动生成的方法存根

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
    	params.put("groupId",groupId);
		//params.put("gdetail", groupInfo.getText().toString());
		//params.put("hasphoto", String.valueOf(hasphoto));
		String url = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					if (statusCode == 200) {
						mDialog.dismiss();
						InputStream is = new ByteArrayInputStream(responseBody);
						ObjectInputStream ois = new ObjectInputStream(is);
						
						list = (List<Map<String,Object>>)ois.readObject();
		    			
		    			for(Map<String,Object> map : list)
		    			{
		    				byte[] photo = (byte[])map.get("photo");
		    				if(photo!=null)
		    				{Log.v("buffer", photo.toString());
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
		    			messages.addAll(getData());
		    			za.notifyDataSetChanged();
		    			
		    			initheads();
		    			

						//Toast.makeText(OldObjectActivity.this.getActivity(), "性别修改成功！", Toast.LENGTH_SHORT).show();
						//		.show();
						//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
						//startActivity(intent);
					} else {
						mDialog.dismiss();
						Toast.makeText(NoteActivity.this.getActivity(),
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
				Toast.makeText(NoteActivity.this.getActivity(),
						"网络访问异常，错误码  > " + statusCode, 0).show();

			}
		});

	} catch (Exception e) {
		e.printStackTrace();
	}


}

private List<Zonemessage> getData() {
	 List<Zonemessage> result = new ArrayList<Zonemessage>();
	 if(list.isEmpty())
	 {
	 }
	 else{
		 
		 String name,text,atTime,atPlace;
		 Bitmap hisimg,icon;
		 int type,messageID,likenum,comnum;
		 long id;
		 
		 
		 List<Map<String,Object>> like = new ArrayList<Map<String,Object>>();
		 List<HeadM> likes = new ArrayList<HeadM>();
		 for(Map<String, Object> map : list)
		 {
			 boolean isliked = false;
			 type = (Integer)map.get("type");
			 if(type==1)
			 {
				 //bitmap = (Bitmap)map.get("photo");
				 icon = (Bitmap)map.get("icon");
				 id = (Long)map.get("myAcct");
				 text = (String)map.get("text");
				 lasttime = ((Timestamp)map.get("uploadTime")).toString();
				 name = (String)map.get("name");
				 messageID = (Integer)map.get("messageID");
				 like = (List<Map<String,Object>>)map.get("friends");
				 likenum = like.size();
				 comnum = (Integer)map.get("comnum");
				 for(Map<String,Object> sb :like)
				 {
					 if((Long)sb.get("myAcct")==Long.parseLong(this.getActivity().getSharedPreferences("user",0).getString("username","")))
						 isliked = true;
				 }
				 result.add(new Zonemessage(id,lasttime,icon,name,text,likenum,comnum,messageID,isliked));
				 
			 }
			 else if(type==2)
			 {
				 hisimg = (Bitmap)map.get("photo");
				 icon = (Bitmap)map.get("icon");
				 id = (Long)map.get("myAcct");
				 text = (String)map.get("text");
				 lasttime = ((Timestamp)map.get("uploadTime")).toString();
				 name = (String)map.get("name");
				 messageID = (Integer)map.get("messageID");
				 like = (List<Map<String,Object>>)map.get("friends");
				 likenum = like.size();
				 comnum = (Integer)map.get("comnum");
				 for(Map<String,Object> sb :like)
				 {
					 if((Long)sb.get("myAcct")==Long.parseLong(this.getActivity().getSharedPreferences("user",0).getString("username","")))
						 isliked = true;
				 }
				 result.add(new Zonemessage(id,lasttime,icon,hisimg,name,text,likenum,comnum,messageID,isliked));
			 }
			 else
			 {
				 likes.clear();
				 atTime = ((Date)map.get("startTime")).toString();
				 atPlace = (String)map.get("atPlace");
				 icon = (Bitmap)map.get("icon");
				 id = (Long)map.get("myAcct");
				 text = (String)map.get("text");
				 lasttime = ((Timestamp)map.get("uploadTime")).toString();
				 name = (String)map.get("name");
				 messageID = (Integer)map.get("messageID");
				 like = (List<Map<String,Object>>)map.get("friends");
				 likenum = like.size();
				 for(Map<String,Object> aa : like)
				 {
					 likes.add(new HeadM((Long)aa.get("myAcct"),aa.get("name")));
					 if((Long)aa.get("myAcct")==Long.parseLong(this.getActivity().getSharedPreferences("user",0).getString("username","")))
						 isliked = true;
				 }
				 try{
				 ByteArrayOutputStream bo=new ByteArrayOutputStream();
				 ObjectOutputStream oo=new ObjectOutputStream(bo);
				 oo.writeObject(likes);
				 //从流里读出来
				 ByteArrayInputStream bi=new ByteArrayInputStream(bo.toByteArray());
				 ObjectInputStream oi=new ObjectInputStream(bi);
				 result.add(new Zonemessage(id,icon,name,lasttime,text,atTime,(List<HeadM>)oi.readObject(),atPlace,likenum,messageID,isliked));
				 }catch(Exception e)
				 {e.printStackTrace();}
				 
				 
			 }
		 }

	 }
	  return result;
  }

public void gallery(View view) {
	// 激活系统图库，选择一张图片
	Intent intent = new Intent(Intent.ACTION_PICK);
	intent.setType("image/*");
	startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
}

/*
 * 从相机获取
 */
public void camera(View view) {
	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	// 判断存储卡是否可以用，可用进行存储
	if (hasSdcard()) {
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
	}
	startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
}

public void gallerys(View view) {
	// 激活系统图库，选择一张图片
	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	intent.setType("image/*");
	startActivityForResult(intent, 100);
}

/*
 * 从相机获取
 */
public void cameras(View view) {
	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

	startActivityForResult(intent, 200);
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	Bitmap Bitmaps = null;
	if (requestCode == PHOTO_REQUEST_GALLERY) {
		if (data != null) {
			// 得到图片的全路径
			Uri uri = data.getData();
			crop(uri);
		}

	} else if (requestCode == PHOTO_REQUEST_CAMERA) {
		if (hasSdcard()) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					PHOTO_FILE_NAME);
			crop(Uri.fromFile(tempFile));
		} else {
			Toast.makeText(this.getActivity(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
		}

	} else if (requestCode == PHOTO_REQUEST_CUT) {
		try {
			//hasphoto = true;
			bitmap = data.getParcelableExtra("data");
			//mFace.setImageBitmap(bitmap);
			if (tempFile != null)
			{boolean delete = tempFile.delete();
			System.out.println("delete = " + delete);
			}Intent intent = new Intent(this.getActivity(),AddPicActivity.class);
			intent.putExtra("bitmap", bitmap);
			intent.putExtra("groupId", groupId);
			startActivity(intent);
			//this.getActivity().finish();
			
			menuWindow.dismiss();

		} 
		
		
		catch (Exception e) {
			e.printStackTrace();
		}

	}else if((requestCode == 100 || requestCode == 200) &&  resultCode == Activity.RESULT_OK && null != data)
	{
		if(requestCode == 100)
		{
		 ContentResolver resolver =this.getActivity().getContentResolver();
			// 得到图片的全路径
			Uri originalUri = data.getData();        //获得图片的uri 
			 
           try {
				Bitmaps = MediaStore.Images.Media.getBitmap(resolver, originalUri);
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}        //显得到bitmap图片
			
			
			menuWindow.dismiss();
		}else{

			Bitmaps = (Bitmap)data.getParcelableExtra("data");
			
			menuWindow.dismiss();

		
		}
		try{
			File myCaptureFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"IFamily",groupId+2);
			if(myCaptureFile.exists())
				myCaptureFile.delete();
			myCaptureFile.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
                                                 new FileOutputStream(myCaptureFile));
			Bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			za.setBg(myCaptureFile);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
		
	
	}
	

	super.onActivityResult(requestCode, resultCode, data);
}

/**
 * 剪切图片
 * 
 * @function:
 * @author:Jerry
 * @date:2013-12-30
 * @param uri
 */
private void crop(Uri uri) {
	// 裁剪图片意图
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	intent.putExtra("crop", "true");
	// 裁剪框的比例，1：1
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	// 裁剪后输出图片的尺寸大小
	intent.putExtra("outputX", 250);
	intent.putExtra("outputY", 250);
	// 图片格式
	intent.putExtra("outputFormat", "JPEG");
	intent.putExtra("noFaceDetection", true);// 取消人脸识别
	intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
	startActivityForResult(intent, PHOTO_REQUEST_CUT);
}

private boolean hasSdcard() {
	if (Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED)) {
		return true;
	} else {
		return false;
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


 private boolean loginServer(String lasttime, int times)
    {
    	boolean loginValidate = false;
    	//使用apache HTTP客户端实现
    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";
    	HttpPost request = new HttpPost(urlStr);
    	//如果传递参数多的话，可以对传递的参数进行封装
    	sp = this.getActivity().getSharedPreferences("user",0);
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	//添加用户名和密码
    	params.add(new BasicNameValuePair("uname",sp.getString("username", "")));
    	params.add(new BasicNameValuePair("times",String.valueOf(times)));
    	params.add(new BasicNameValuePair("lasttime",lasttime));
    	params.add(new BasicNameValuePair("requesttype","1"));
    	params.add(new BasicNameValuePair("groupId",groupId));
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
    			
    			list = (List<Map<String,Object>>)ois.readObject();
    			
    			for(Map<String,Object> map : list)
    			{
    				byte[] photo = (byte[])map.get("photo");
    				if(photo!=null)
    				{Log.v("buffer", photo.toString());
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
				
			
			
			mDialog.dismiss();
			//URL合法，但是这一步并不验证密码是否正确*/
			
			
	    	boolean loginValidate = loginServer(lasttime, times);
	    	//System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+list.get(0).get("text"));
	    	Message msg = handler.obtainMessage();
	    	if(loginValidate)
	    	{
	    		if(times==1){
	    			msg.what = 0;
		    		handler1.sendMessage(msg);
	    		}
	    		else{
	    			msg.what=1;
	    			handler1.sendMessage(msg);
	    		}
	    	}else
	    	{
	    		msg.what = 2;
	    		handler1.sendMessage(msg);
	    	}
		}
    	
    }

Handler handler1 = new Handler()
{
	public void handleMessage(Message msg)
	{
		switch(msg.what)
		{
		case 0:
			mDialog.cancel();

			//Toast.makeText(NoteActivity.this.getActivity(), "加载成功！", Toast.LENGTH_SHORT).show();
			//Intent intent = new Intent();
			//intent.setClass(LoginActivity.this, MainActivity.class);
			//tartActivity(intent);
			//finish();
			
			loadData(AutoListView.REFRESH);
			
			break;
		
		case 1:
			Toast.makeText(NoteActivity.this.getActivity(), "加载成功！", Toast.LENGTH_SHORT).show();
			loadData(AutoListView.LOAD);
			break;
			
		case 2:
			mDialog.cancel();
			Toast.makeText(NoteActivity.this.getActivity(), "URL验证失败", Toast.LENGTH_SHORT).show();
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

@Override
public void onClick(View v) {
	// TODO 自动生成的方法存根
	switch(v.getId())
	{
	case R.id.btn_take_photo:
		cameras(v);
		break;
	case R.id.btn_pick_photo:
		gallerys(v);
		break;
	default:
			menuWindow = new SelectPicPopupWindow(this.getActivity(), this);
				//显示窗口
			menuWindow.showAtLocation(this.getView().findViewById(R.id.mainView), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				
				
			
			
		
	}
}  
	
}
