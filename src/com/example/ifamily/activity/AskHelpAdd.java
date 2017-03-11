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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.activity.OldAddActivity.findFriend;
import com.example.ifamily.adapter.FamilylistAdapter;
import com.example.ifamily.adapter.FriendlistAdapter;
import com.example.ifamily.message.AddfriendMessage;
import com.example.ifamily.message.GroupLMessage;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.example.ifamily.utils.MyHttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AskHelpAdd  extends Activity{

	private PopupWindow popupWindow;  
	private EditText et_present;
	private TextView title,selfriend;
    private FamilylistAdapter za;
	private Button back;
	private Button btnadd;
    private Button presentok;	
    private List<GroupLMessage> messages = new ArrayList<GroupLMessage>(); 
    private LinearLayout ll_present,presentadd_group,all;  
    private List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
    private int groupID = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.askhelp_add);
		initview();			
	}
	protected void initview()
	{
		et_present = (EditText)findViewById(R.id.wishes);
		selfriend = (TextView)findViewById(R.id.selectfamily);
		ll_present=(LinearLayout)findViewById(R.id.present);//
		all=(LinearLayout)findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this);
		presentadd_group=(LinearLayout)findViewById(R.id.wishselectfamily);
		title = (TextView)findViewById(R.id.title);
		back = (Button)findViewById(R.id.back);
		presentok=(Button)findViewById(R.id.wish_ok);//确定按钮
		title.setText("床头故事");
		initmessage();
		///
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				AskHelpAdd.this.finish();
			}
			 
		 });
		 
		 ////
	
			presentadd_group.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					showwindow(presentadd_group);
				}
				
			});
			///
			presentok.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					upload(arg0);
				}
				
			});
	}
	
	
	
	protected void initmessage()
	{
		messages.clear();
		
    	za = new FamilylistAdapter(this,messages);
    	new Thread(new findFriend()).start();
	}
	
	private void showwindow(LinearLayout spinnerlayout)
	{
		
		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

			}
		   final LinearLayout  layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.a_add_sch_friendslist, null);  
		   popupWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
		   final ListView  Lv = (ListView) layout.findViewById(R.id.a_add_sch_list);
		  
		   Lv.setAdapter(za);
		   
		   Lv.setOnItemClickListener(new OnItemClickListener(){  
				  
	        	
	            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {  
	                // TODO Auto-generated method stub  
	            	groupID = messages.get(arg2).getid();
	            	selfriend.setText(messages.get(arg2).getname());
	                popupWindow.dismiss();
	            	//myfriends.setText(messages.get(arg2).getname());
	              //  popupWindow.dismiss();  
	               // popupWindow = null;  
	            }  
			
	        });  
		   
		   
		   layout.setOnTouchListener(new OnTouchListener() {  
	              
	            public boolean onTouch(View v, MotionEvent event) {  
	                  
	                int height = layout.findViewById(R.id.a_add_sch_list).getTop();  
	                int bheight=layout.findViewById(R.id.a_add_sch_list).getHeight()+height;
	                int y=(int) event.getY();  
	                if(event.getAction()==MotionEvent.ACTION_UP){  
	                    if(y<height||y>bheight){  
	                    	popupWindow.dismiss();
	                    	popupWindow=null;
	                    }  
	                    
	                }                 
	                return true;  
	            }  
	        });
	        //实例化一个ColorDrawable颜色为透明  
	        ColorDrawable dw = new ColorDrawable(0000000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        popupWindow.setBackgroundDrawable(dw);  
	        popupWindow.setFocusable(true);
	       popupWindow.setAnimationStyle(R.style.popupfrombottom);
	       popupWindow.showAtLocation(all, Gravity.CENTER, 0, 0);
 
	       
	        
	        
	/*        
	        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

	        	public void onDismiss() {
	        	}
	        	});*/
	      
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
	
	
	@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK &&(popupWindow != null && popupWindow.isShowing())) { 
    	//监控/拦截/屏蔽返回键
    		popupWindow.dismiss();

    		popupWindow = null;
    		return false;    	

    }
    else {return super.onKeyDown(keyCode, event);}
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
    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/OldObjectServlet";
    	HttpPost request = new HttpPost(urlStr);
    	//如果传递参数多的话，可以对传递的参数进行封装
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	//添加用户名和密码
    	params.add(new BasicNameValuePair("uname",String.valueOf(PushApplication.getInstance().getUserInfo().get("myID"))));
    	//Log.e("TAG", String.valueOf(PushApplication.getInstance().getUserInfo().get("myID")));
    	params.add(new BasicNameValuePair("requesttype","6"));
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
			FileCache fileCache = new FileCache(AskHelpAdd.this);
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
				Log.e("sb",String.valueOf((Integer)map.get("groupID")));
				messages.add(new GroupLMessage(((Integer)map.get("groupID")),map.get("photo"),(String) map.get("name")));
			}
			za = new FamilylistAdapter(AskHelpAdd.this,messages);
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
public void upload(View view) {
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RequestParams params = new RequestParams();
		if(et_present.getText().toString().trim().equals(""))
		{
			Toast.makeText(AskHelpAdd.this,"请输入描述",Toast.LENGTH_SHORT).show();
			return;
		}
		if(groupID == 0)
		{
			Toast.makeText(AskHelpAdd.this, "请选择家庭", Toast.LENGTH_SHORT).show();
			return;
		}
		
		SharedPreferences sp = getSharedPreferences("user",0);
		String username = sp.getString("username", "");
		params.put("groupId", String.valueOf(groupID));
		params.put("uname", username);
		params.put("text", et_present.getText().toString());
		//params.put("hasphoto", String.valueOf(hasphoto));
		params.put("requesttype", "4");
		String url = "http://103.31.241.201:8080/IFamilyServer/StoryServlet";

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					if (statusCode == 200) {

						//Toast.makeText(Setgroup.this, "家庭成功创建！", Toast.LENGTH_SHORT)
								//.show();
						 SharedPreferences sps = AskHelpAdd.this.getSharedPreferences("SP", Context.MODE_PRIVATE);							  							  	  		  
						  Editor editor = sps.edit();
						  editor.putBoolean("isaskhelp",true);
						  editor.commit();		
						  AskHelpAdd.this.finish();
					} else {
						Toast.makeText(AskHelpAdd.this,
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
				Toast.makeText(AskHelpAdd.this,
						"网络访问异常，错误码  > " + statusCode, 0).show();

			}
		});

	} catch (Exception e) {
		e.printStackTrace();
	}
}

}

