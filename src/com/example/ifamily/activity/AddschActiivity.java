package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.adapter.FriendlistAdapter;
import com.example.ifamily.message.AddfriendMessage;
import com.example.ifamily.mywidget.Day;
import com.example.ifamily.mywidget.ExtendedCalendarView;
import com.example.ifamily.mywidget.ExtendedCalendarView.OnDayClickListener;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.example.ifamily.utils.MyHttpRequest;
import com.example.ifamily.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.widget.AdapterView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AddschActiivity extends Activity{
	private TextView title;
	private Button back;
	private PopupWindow popupWindow;  
    private FriendlistAdapter za=null;
    private List<AddfriendMessage> messages = new ArrayList<AddfriendMessage>();
    private LinearLayout all;
    private Button choosedate,inputmess;
    private LinearLayout spinnerlayout;  
    private EditText text,atPlace;
    private String date=new String();
    private String groupId;
    private String sdate = null;
    private List<Long> list = new ArrayList<Long>();
    private List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		if(intent!=null)
        {
            
            groupId = intent.getStringExtra("groupId");
        }
		setContentView(R.layout.a_add_sch);	
		
		
		
		title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("添加日程");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				AddschActiivity.this.finish();
			}
			 
		 });
		
		
		
		
		
		
		
		
		 choosedate= (Button) findViewById(R.id.a_add_sch_seldate);
		 spinnerlayout = (LinearLayout) findViewById(R.id.a_add_spinner); 
		 inputmess=(Button) findViewById(R.id.a_add_sch_ok);
		 atPlace = (EditText) findViewById(R.id.atplace);
		 text = (EditText) findViewById(R.id.a_add_sch_et);
		 init();
			all=(LinearLayout)findViewById(R.id.all);
			FontManager.changeFonts(all,this);
		 spinnerlayout.setOnClickListener(new OnClickListener() {  
			  
	            @Override  
	            public void onClick(View v) {  

	            	if(!messages.isEmpty())
	            	{za.resetstate();
	            	showwindow(); 
	            	}
	  
	            }  
	        }); 
		 
		choosedate.setOnClickListener(new OnClickListener() {  
			  
	            @Override  
	            public void onClick(View v) {  
	            	
	            	selectdate();  
	  
	            }  
	        }); 
		inputmess.setOnClickListener(new OnClickListener() {  
			  
            @Override  
            public void onClick(View v) {  
            	
            	try {
        			
        			RequestParams params = new RequestParams();
        			
        			ByteArrayOutputStream baos = new ByteArrayOutputStream();
        			ObjectOutputStream oos = new ObjectOutputStream(baos);
        			oos.writeObject(list);
        			String Friends = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        			
        			
        			params.put("groupId", groupId);
        			params.put("uname", getSharedPreferences("user",0).getString("username", ""));
        			params.put("friends", Friends);
        			params.put("startTime", sdate);
        			params.put("text", text.getText().toString());
        			params.put("atPlace", atPlace.getText().toString());
        			params.put("requesttype", "2");
        			params.put("addtype", "3");
        			String url = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";

        			AsyncHttpClient client = new AsyncHttpClient();
        			client.post(url, params, new AsyncHttpResponseHandler() {
        				@Override
        				public void onSuccess(int statusCode, Header[] headers,
        						byte[] responseBody) {
        					try {
        						if (statusCode == 200) {

        							//Toast.makeText(Setgroup.this, "家庭成功创建！", Toast.LENGTH_SHORT)
        							//		.show();
        							/*Intent intent = new Intent(AddschActiivity.this,Iguide.class);
        							intent.putExtra("tab", 1);
        							startActivity(intent);
        							MyApplication.getInstance().exit();*/
 							       
      							  SharedPreferences sps = AddschActiivity.this.getSharedPreferences("SP", Context.MODE_PRIVATE);							  							  	  		  
      							  Editor editor = sps.edit();
      							  editor.putBoolean("ifrefresh",true);
      							  editor.commit();		
        							AddschActiivity.this.finish();
        						} else {
        							Toast.makeText(AddschActiivity.this,
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
        					Toast.makeText(AddschActiivity.this,
        							"网络访问异常，错误码  > " + statusCode, 0).show();

        				}
        			});

        		} catch (Exception e) {
        			e.printStackTrace();
        		}
  
            }  
        }); 
		
		
	    }  
		 
		 
		 
	     
	     

	protected void init()
	{
		//AddfriendMessage item1=new AddfriendMessage(1,R.drawable.defalt_head,"fdfdfdfd");
    	//AddfriendMessage item2=new AddfriendMessage(2,R.drawable.defalt_head,"ffsfsd");
    	//AddfriendMessage item3=new AddfriendMessage(3,R.drawable.defalt_head,"fdfsfsdfsdfd");
    	//AddfriendMessage item4=new AddfriendMessage(4,R.drawable.defalt_head,"fdffsvdssfd");
    	//AddfriendMessage item5=new AddfriendMessage(5,R.drawable.defalt_head,"fdffsvdsv");
    	//messages.add(item1);
    	//messages.add(item2);
    	//messages.add(item3);
    	//messages.add(item4);
    	//messages.add(item5);
    	
    	new Thread(new findFriend()).start();
    
	}
	
		
		
		

	private void showwindow()
	{
		
		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

			}
		   LinearLayout  layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.a_add_sch_friendslist, null);  
		   popupWindow = new PopupWindow(spinnerlayout);  
		   final ListView  Lv = (ListView) layout.findViewById(R.id.a_add_sch_list);
		  final TextView fres=(TextView)findViewById(R.id.a_add_sch_selfriend);
		   Lv.setAdapter(za);
	        popupWindow.setWidth(spinnerlayout.getWidth());  
	        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);  
	    
	        popupWindow.setBackgroundDrawable(new BitmapDrawable());  

	        popupWindow.setOutsideTouchable(true);  
	        popupWindow.setFocusable(true);  
	        popupWindow.setContentView(layout);  

	        popupWindow.showAsDropDown(spinnerlayout, 0, 0);  
 
	        Lv.setOnItemClickListener(new OnItemClickListener(){  
	  
	        	
	            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {  
	                // TODO Auto-generated method stub  
	       
	            	//myfriends.setText(messages.get(arg2).getname());
	                // µØøÚœ˚ ß  
	              //  popupWindow.dismiss();  
	               // popupWindow = null;  
	            }  
			
	        });  
	        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

	        	public void onDismiss() {
	        	// TODO Auto-generated method stub
	        		list=  za.getids();
	        		String myfri=new String();
	        		for(int i=0;i<list.size();i++)
	        		{
	        			String temp=" "+String.valueOf(list.get(i))+" ";
	        		  myfri=temp+myfri;
	        			
	        		}
	        		fres.setText(myfri) ;
	        	}
	        	});
	      
	}
	
	


	private void selectdate()
	{
		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

			}
		LinearLayout  layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.a_add_sch_datesel, null);  
		popupWindow=new PopupWindow(layout);
		popupWindow.setWidth(LayoutParams.WRAP_CONTENT);  
	    popupWindow.setHeight(LayoutParams.WRAP_CONTENT); 
	    ExtendedCalendarView cv=(ExtendedCalendarView) layout.findViewById(R.id.a_add_calendar);
      
       TextView  ok=(TextView ) layout.findViewById(R.id.a_add_sch_dateok);
       TextView  cancel=(TextView ) layout.findViewById(R.id.a_add_sch_datecancel);
       date=Utils.getCurrentDateTime();
       cv.setOnDayClickListener(new OnDayClickListener() {


		@Override
		public void onDayClicked(AdapterView<?> adapter, View view,
				int position, long id, Day day) {
			// TODO Auto-generated method stub
			 date = day.getYear() + "年" + (day.getMonth()+1) + "月" + day.getDay() + "日";
			 sdate = day.getYear()+"-"+(day.getMonth()+1)+"-"+day.getDay();
		}
       });
       
       
       ok.setOnClickListener(new OnClickListener()
       {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			choosedate.setText(date);popupWindow.dismiss();
			
			
		}
       
       }
       );
       cancel.setOnClickListener(new OnClickListener()
       {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			popupWindow.dismiss();
		}
       
       }
       );


 //      popup.showAsDropDown(choosedate);
       popupWindow.setOutsideTouchable(true);
       popupWindow.showAtLocation(choosedate, Gravity.CENTER, 0, 0);
       
	}
	
	
	
	
	@Override

	public boolean onTouchEvent(MotionEvent event) {

	// TODO Auto-generated method stub

	if (popupWindow != null && popupWindow.isShowing()) {

	popupWindow.dismiss();

	popupWindow = null;

	}

	return super.onTouchEvent(event);

	}
	
	
	
	
	public class findFriend implements Runnable
	{

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			boolean loginValidate = loginServer();
	    	//System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+responseMsg);
	    	android.os.Message msg = handler.obtainMessage();
	    	if(loginValidate)
	    	{
	    		if(!lists.isEmpty())
	    		{
	    			msg.what = 0;
		    		handler.sendMessage(msg);
	    		}
	    		else
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
	
	private boolean loginServer()
    {
    	boolean loginValidate = false;
    	//使用apache HTTP客户端实现
    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/SetGroupAddF";
    	HttpPost request = new HttpPost(urlStr);
    	//如果传递参数多的话，可以对传递的参数进行封装
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	//添加用户名和密码
    	params.add(new BasicNameValuePair("uname",String.valueOf(PushApplication.getInstance().getUserInfo().get("myID"))));
    	Log.e("TAG", String.valueOf(PushApplication.getInstance().getUserInfo().get("myID")));
    	params.add(new BasicNameValuePair("requesttype","1"));
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
    			loginValidate = true;
    			//获得响应信息
    			InputStream is = response.getEntity().getContent();
    			ObjectInputStream ois = new ObjectInputStream(is);
    			
    			
    			lists = (List<Map<String,Object>>)ois.readObject();
    			ois.close();
    			is.close();
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return loginValidate;
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



Handler handler = new Handler()
{
	public void handleMessage(android.os.Message msg)
	{
		switch(msg.what)
		{
		case 0:
			//mDialog.cancel();

			//Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
			//Intent intent = new Intent();
			//intent.setClass(LoginActivity.this, MainActivity.class);
			//tartActivity(intent);
			//sp=getSharedPreferences("Login",0);
			//sp.edit().putBoolean("ISCHECK", true).commit();
			//sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
			//Intent intent=new Intent(PasswordFActivity.this,Iguide.class);
			//startActivity(intent);
			//PasswordFActivity.this.finish();
			//finish();
			FileCache fileCache = new FileCache(AddschActiivity.this);
			messages.clear();
			for(Map<String,Object> map : lists)
			{
				
				byte[] photo = (byte[])map.get("photo");
				Log.v("buffer", photo.toString());
				File tempfile = fileCache.getFile(photo.toString().substring(1, 5)+".jpg");
				try {
					tempfile.createNewFile();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				byte2File(photo,tempfile);
				Bitmap bitmap = decodeFile(tempfile);
				map.put("photo", bitmap);
			
				messages.add(new AddfriendMessage(((Long)map.get("myAccount")),map.get("photo"),(String) map.get("name")));
			}
			za = new FriendlistAdapter(AddschActiivity.this,messages);
			za.notifyDataSetChanged();
			fileCache.clear();
			//receivedMsg.setHeadIcon(bitmap);
			
			
				
			//fileCache.clear();
			//parseMessage(receivedMsg);
			break;
		case 1:
			//mDialog.cancel();
			//userinfo1=list.get(0);
			//File image = (File)userinfo1.get("img");
			//image.setImageBitmap(bitmap);
			Toast.makeText(getApplicationContext(), "没有发现任何好友", Toast.LENGTH_SHORT).show();
			//parseMessage(receivedMsg);
			break;
		case 2:
			//mDialog.cancel();
			Toast.makeText(getApplicationContext(), "检查网络连接！", Toast.LENGTH_SHORT).show();
			//parseMessage(receivedMsg);
			break;
		
		
		}
		
	}
};
	
	
}
