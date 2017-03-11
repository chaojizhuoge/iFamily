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
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.example.ifamily.adapter.Zonelistadapter;
import com.example.ifamily.message.AddfriendMessage;
import com.example.ifamily.message.Zonemessage;
import com.example.ifamily.mywidget.AutoListView;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.example.ifamily.utils.MyHttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterGroupAddF extends Activity implements OnClickListener {

	private RoundImageView rv;
	private TextView title;
	private Button back;
	private EditText num;
	private TextView name,info;
	private Button search,ok;
	private LinearLayout hismessage;
    private int groupId;
    private boolean get = false;
    private boolean pri;
    Map<String,Object> map = new HashMap<String,Object>();

    private List<AddfriendMessage> messages = new ArrayList<AddfriendMessage>();
    private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    @Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.p_setgroup_addfriends);	
		Intent intent = getIntent();
		if(intent!=null){
			groupId = intent.getIntExtra("groupId", 0);
			pri = intent.getBooleanExtra("pri", false);
		}
		rv = (RoundImageView)findViewById(R.id.head);
		num=(EditText)findViewById(R.id.num);//����
		name=(TextView)findViewById(R.id.name);//�ǳ�
		info=(TextView)findViewById(R.id.info);//���˼��
		search=(Button)findViewById(R.id.search);//����
		ok=(Button)findViewById(R.id.ok);//����
		hismessage=(LinearLayout)findViewById(R.id.result);// ������Ϣ���Ͽ�ʼ����
		hismessage.setVisibility(View.GONE);
		
		search.setOnClickListener(this);
		ok.setOnClickListener(this);
		
		
		
		
		
		
		title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("��Ӻ���");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(true){
				Intent intent=new Intent(EnterGroupAddF.this,Iguide.class);
				intent.putExtra("tab", 3);
				 startActivity(intent);
				}
				EnterGroupAddF.this.finish();
			}
			 
		 });
		
		
		
		
		
		
		
		
		
		ListView Lv = (ListView) findViewById(R.id.list_myfriend);
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//��������
		FontManager.changeFonts(all,this);
		//initmessage();
		//Lv.setAdapter(za);
		
	}
    public void initmessage()
    {
  
    	AddfriendMessage item1=new AddfriendMessage(R.drawable.defalt_head,"fdfdfdfd");
    	AddfriendMessage item2=new AddfriendMessage(R.drawable.defalt_head,"ffsfsd");
    	AddfriendMessage item3=new AddfriendMessage(R.drawable.defalt_head,"fdfsfsdfsdfd");
    	AddfriendMessage item4=new AddfriendMessage(R.drawable.defalt_head,"fdffsvdssfd");
    	AddfriendMessage item5=new AddfriendMessage(R.drawable.defalt_head,"fdffsvdsv");
    	messages.add(item1);
    	messages.add(item2);
    	messages.add(item3);
    	messages.add(item4);
    	messages.add(item5);
    	//za = new FriendlistAdapter(this,messages);

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
	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch(v.getId())
		{
		case R.id.search:
			hismessage.setVisibility(View.GONE);
			findUser(num.getText().toString());
			break;
		case R.id.ok:
			invite();
			break;
		}
	}
	private void invite() {
		// TODO �Զ����ɵķ������

		try {
			if(!get)
				return;
			
			RequestParams params = new RequestParams();

			params.put("user1", num.getText().toString());
			params.put("user2", getSharedPreferences("user",0).getString("username", ""));
			params.put("groupId", String.valueOf(groupId));
			params.put("requesttype", "1");
			//params.put("addtype", "2");

			
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/MessageServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {
							Toast.makeText(EnterGroupAddF.this,
									"�Ѿ�����ѷ�������~", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(EnterGroupAddF.this,
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
					Toast.makeText(EnterGroupAddF.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
	private void findUser(String string) {
		// TODO �Զ����ɵķ������

		try {
			RequestParams params = new RequestParams();

			params.put("uname", string);
			params.put("requesttype", "1");
			//params.put("addtype", "2");

			
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/UserInfoServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {
							InputStream is = new ByteArrayInputStream(responseBody);
							ObjectInputStream oid = new ObjectInputStream(is);
							
							map = (Map<String,Object>)oid.readObject();
							
							if(!map.isEmpty()){
							byte[] icon = (byte[])map.get("photo");
		    				
		    				//Log.v("buffer", photo.toString());
							FileCache fileCache = new FileCache(EnterGroupAddF.this);
		    				File tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
		    				tempfile.createNewFile();
		    				byte2File(icon,tempfile);
		    				Bitmap bitmaps = decodeFile(tempfile);
		    				//map.put("icon", bitmaps);
		    				hismessage.setVisibility(View.VISIBLE);
		    				
		    				rv.setImageBitmap(bitmaps);
		    				
		    				
		    				
		    				int year = ((Date)map.get("birth")).getYear()+1900;
		    				int month = ((Date)map.get("birth")).getMonth()+1;
		    				@SuppressWarnings("deprecation")
							int day = ((Date)map.get("birth")).getDate();
		    				
		    				
		    				name.setText((String)map.get("name"));
		    				info.setText("���գ�"+year+"��"+month+"��"+day+"��"+"\n"
		    							+"�Ա�"+(String)map.get("sex")+"\n"
		    							+"���˼�飺"+(String)map.get("detail"));
		    				
		    				get = true;
		    				oid.close();
		    				is.close();
		    				fileCache.clear();
							}
							else
							{Toast.makeText(EnterGroupAddF.this, "���û�������", Toast.LENGTH_LONG).show();}
							//mDialog.dismiss();

							//Toast.makeText(AddPicActivity.this, "�ɹ��ϴ���", Toast.LENGTH_SHORT)
							//		.show();
							//Intent intent = new Intent(AddPicActivity.this,NoteActivity.class);
							//startActivity(intent);
						} else {
							Toast.makeText(EnterGroupAddF.this,
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
					Toast.makeText(EnterGroupAddF.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	} 




    	

    	
}

