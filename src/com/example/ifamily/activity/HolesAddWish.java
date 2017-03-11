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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HolesAddWish extends Activity{
	
	private PopupWindow popupWindow;  
    private FamilylistAdapter za;
    private List<GroupLMessage> messages = new ArrayList<GroupLMessage>(); 
	private TextView title,selwish;
	private Button back;
    private LinearLayout presentadd_group,all;  
    private Button presentok;
    private CheckBox wuzhi,feiwuzhi;
    private EditText detail;
	private List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();	
	private List<Long> groupIDs = new ArrayList<Long>();
	private boolean f = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.holes_add_wish);
		initview();
		initmessage();
		}
	
	
	private void initview()
	{
		all=(LinearLayout)findViewById(R.id.all);//��������
		FontManager.changeFonts(all,this);
		selwish = (TextView)findViewById(R.id.selectfamily);
		presentadd_group=(LinearLayout)findViewById(R.id.wishselectfamily);
		title = (TextView)findViewById(R.id.title);
		back = (Button)findViewById(R.id.back);
		presentok=(Button)findViewById(R.id.wish_ok);//presentȷ����ť
		wuzhi = (CheckBox)findViewById(R.id.cb_wuzhi);
		wuzhi.setChecked(true);
		feiwuzhi = (CheckBox)findViewById(R.id.cb_feiwuzhi);
		feiwuzhi.setChecked(false);
		title.setText("��Ҫ��Ը");
		detail = (EditText)findViewById(R.id.wishes);
		///
		wuzhi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	            @Override
	            public void onCheckedChanged(CompoundButton buttonView, 
	                    boolean isChecked) { 
	                // TODO Auto-generated method stub 
	                if(isChecked){ 
	                	//wuzhi.setChecked(false);
	                	feiwuzhi.setChecked(false);
	                }else{ 
	                	//wuzhi.setChecked(true);
	                	feiwuzhi.setChecked(true);
	                } 
	            } 
	        }); 
		
		feiwuzhi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	wuzhi.setChecked(false);
                	//feiwuzhi.setChecked(false);
                }else{ 
                	wuzhi.setChecked(true);
                	//feiwuzhi.setChecked(true);
                } 
            } 
        }); 
		
		
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				Intent intent = new Intent(HolesAddWish.this,HolesMain.class);
				intent.putExtra("tab", 0);
				startActivity(intent);
				HolesAddWish.this.finish();
			}
			 
		 });
		 
		 ////
	
			presentadd_group.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(f){
					za.resetstate();
					showwindow(presentadd_group);
					}
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
	
	public void upload(View view) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			if(detail.getText().toString().trim().equals(""))
			{
				Toast.makeText(HolesAddWish.this,"������Ը��",Toast.LENGTH_SHORT).show();
				return;
			}
			if(groupIDs.isEmpty())
			{
				Toast.makeText(HolesAddWish.this, "��ѡ���ͥ", Toast.LENGTH_SHORT).show();
				return;
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(groupIDs);
			String groupIds = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
			

			SharedPreferences sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			String state = (wuzhi.isChecked())?"1":"0";
			params.put("state", state);
			params.put("groupIDs", groupIds);
			params.put("uname", username);
			params.put("text", detail.getText().toString());
			//params.put("hasphoto", String.valueOf(hasphoto));
			params.put("requesttype", "7");
			String url = "http://103.31.241.201:8080/IFamilyServer/HolesServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							//Toast.makeText(Setgroup.this, "��ͥ�ɹ�������", Toast.LENGTH_SHORT)
									//.show();
							if(wuzhi.isChecked())
							{
								Intent intent = new Intent(HolesAddWish.this,HolesMain.class);
								intent.putExtra("tab", 0);
								startActivity(intent);
								MyApplication.getInstance().exit();
							}
							else{
								Intent intent = new Intent(HolesAddWish.this,HolesMain.class);
								intent.putExtra("tab", 0);
								startActivity(intent);
								MyApplication.getInstance().exit();
							}
							HolesAddWish.this.finish();
						} else {
							Toast.makeText(HolesAddWish.this,
									"��������쳣�������룺" + statusCode, Toast.LENGTH_SHORT).show();

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					Toast.makeText(HolesAddWish.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void initmessage()
	{
		
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
	        //ʵ����һ��ColorDrawable��ɫΪ͸��  
	        ColorDrawable dw = new ColorDrawable(0000000000);  
	        //����SelectPicPopupWindow��������ı���  
	        popupWindow.setBackgroundDrawable(dw);  
	        popupWindow.setFocusable(true);
	       popupWindow.setAnimationStyle(R.style.popupfrombottom);
	       popupWindow.showAtLocation(all, Gravity.CENTER, 0, 0);
 
	       
	        
	        
	        
	        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

	        	public void onDismiss() {
	        		groupIDs=  za.getids();
	        		String myfri=new String();
	        		if(!za.getnames().isEmpty())
	        		{
	        		for(int i=0;i<za.getnames().size();i++)
	        		{
	        			String temp=String.valueOf(za.getnames().get(i));
	        		  myfri=temp+","+myfri;
	        			
	        		}
	        		myfri = myfri.substring(0, myfri.length()-1);
	        		 selwish.setText(myfri) ;
	        	}
	        	}
	        	});
	      
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
    	//���/����/���η��ؼ�
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
			// TODO �Զ����ɵķ������
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
    	//ʹ��apache HTTP�ͻ���ʵ��
    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/OldObjectServlet";
    	HttpPost request = new HttpPost(urlStr);
    	//������ݲ�����Ļ������ԶԴ��ݵĲ������з�װ
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	//����û���������
    	params.add(new BasicNameValuePair("uname",String.valueOf(PushApplication.getInstance().getUserInfo().get("myID"))));
    	//Log.e("TAG", String.valueOf(PushApplication.getInstance().getUserInfo().get("myID")));
    	params.add(new BasicNameValuePair("requesttype","6"));
    	try
    	{
    		
    		
    		
    		//�������������
    		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
    		HttpClient client = MyHttpRequest.getHttpClient();
    		//ִ�����󷵻���Ӧ
    		HttpResponse response = client.execute(request);
    		//in.close();
    		//�ж��Ƿ�����ɹ�
    		if(response.getStatusLine().getStatusCode()==200)
    		{
    			loginValidate = true;
    			//�����Ӧ��Ϣ
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
            //����ͼ���С  
            BitmapFactory.Options o = new BitmapFactory.Options();   
            o.inJustDecodeBounds = true;   
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);   
    
            //�ҵ���ȷ�Ŀ̶�ֵ����Ӧ����2���ݡ�  
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

			//Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_SHORT).show();
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
			FileCache fileCache = new FileCache(HolesAddWish.this);
			messages.clear();
			for(Map<String,Object> map : lists)
			{
				
				byte[] photo = (byte[])map.get("photo");
				Log.v("buffer", photo.toString());
				File tempfile = fileCache.getFile(photo.toString().substring(1, 5)+".jpg");
				try {
					tempfile.createNewFile();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				byte2File(photo,tempfile);
				Bitmap bitmap = decodeFile(tempfile);
				map.put("photo", bitmap);
				Log.e("sb",String.valueOf((Integer)map.get("groupID")));
				messages.add(new GroupLMessage(((Integer)map.get("groupID")),map.get("photo"),(String) map.get("name")));
			}
			za = new FamilylistAdapter(HolesAddWish.this,messages,true);
			za.notifyDataSetChanged();
			f=true;
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
			Toast.makeText(getApplicationContext(), "û�з����κκ���", Toast.LENGTH_SHORT).show();
			//parseMessage(receivedMsg);
			break;
		case 2:
			//mDialog.cancel();
			Toast.makeText(getApplicationContext(), "����������ӣ�", Toast.LENGTH_SHORT).show();
			//parseMessage(receivedMsg);
			break;
		
		
		}
		
	}
};
	
}
