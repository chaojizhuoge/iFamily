package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import org.apache.http.util.EntityUtils;

import com.example.ifamily.AddPopWindow;
import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.activity.Login.LoginThread;
import com.example.ifamily.adapter.GridHeadsAdapter;
import com.example.ifamily.adapter.Zonelistadapter;
import com.example.ifamily.message.HeadM;
import com.example.ifamily.message.Zonemessage;
import com.example.ifamily.mywidget.AutoListView;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.AutoListView.OnLoadListener;
import com.example.ifamily.mywidget.AutoListView.OnRefreshListener;
import com.example.ifamily.utils.FileCache;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

public class FamilyzoneActivity extends Activity implements OnClickListener,OnRefreshListener,
OnLoadListener{
	
	private static final int PHOTO_REQUEST_CAMERA = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���

	
	private Bitmap bitmap;

	/* ͷ������ */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	
	private ProgressDialog mDialog;
	
	private List<Map<String,Object>> list;
	private static final int REQUEST_TIMEOUT = 10*1000;//��������ʱ10����  
	private static final int SO_TIMEOUT = 10*1000;  //���õȴ����ݳ�ʱʱ��10����
	
	private int times;
	private SharedPreferences sp;
	private String lasttime;
	private File tempfile;
	private FileCache fileCache;
	SelectPicPopupWindow menuWindow;
	
	private Button addButton;
    private List<Zonemessage> messages = new ArrayList<Zonemessage>();;
    private AutoListView Lv;
    private Zonelistadapter za;
    private GridHeadsAdapter ga;
    private List<HeadM> imgs=new ArrayList<HeadM>();
    private GridView gv;
    private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<Zonemessage> result = (List<Zonemessage>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				Lv.onRefreshComplete();
				messages.clear();
				messages.addAll(result);
				break;
			case AutoListView.LOAD:
				Lv.onLoadComplete();
				messages.addAll(result);
				break;
			}
			Lv.setResultSize(result.size());
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



    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.familyzone);
		za = new Zonelistadapter(this.getApplicationContext(), messages);
		addButton = (Button) findViewById(R.id.btnAdd);
		addButton.setOnClickListener(this);
		Lv = (AutoListView) findViewById(R.id.zonelist);
		Lv.setOnRefreshListener(this);
		Lv.setOnLoadListener(this);
		 Lv.setAdapter(za);
		 gv=(GridView)findViewById(R.id.gd_head);
		 gvinitdata();
		 gv.setAdapter(ga);
		 gv.setNumColumns(ga.getCount());  
		 times = 1;	
		 lasttime="";
		 fileCache = new FileCache(this.getApplicationContext());
		 //sp = getSharedPreferences("user",0);
		 initData();
	}

	private void initData() {
		onRefresh();
		//loadData(AutoListView.REFRESH);
		
	}

	private void gvinitdata()
	{
		Object ob1=R.drawable.defalt_head;
		Object ob2=R.drawable.defalt_head;
		Object ob3=R.drawable.defalt_head;
		Object ob4=R.drawable.defalt_head;
		Object ob5=R.drawable.defalt_head;
		Object ob6=R.drawable.defalt_head;
		//imgs.add((HeadM) ob1);
		//imgs.add(ob2);
		//imgs.add(ob3);
		//imgs.add(ob4);
		//imgs.add(ob5);
		//imgs.add(ob6);
		//gv.setLayoutParams(new TableRow.LayoutParams(dip2px(this.getApplicationContext(),80*6),dip2px(this.getApplicationContext(), 60)));
		//gv.setGravity(Gravity.CENTER);
		//ga= new GridHeadsAdapter(this.getApplicationContext(),imgs);
		
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
	public void onRefresh() {
		times = 1;
		mDialog = new ProgressDialog(FamilyzoneActivity.this);
		mDialog.setTitle("��¼");
		mDialog.setMessage("���ڵ�¼�����������Ժ�...");
		mDialog.show();
		Thread loginThread = new Thread(new LoginThread());

		loginThread.start();
		//loadData(AutoListView.REFRESH);
	}

	@Override
	public void onLoad() {
		times = 2;
		Thread thread = new Thread(new LoginThread());
		thread.start();
		//loadData(AutoListView.LOAD);
	}

	 private List<Zonemessage> getData() {
		 List<Zonemessage> result = new ArrayList<Zonemessage>();
		 if(list==null)
		 {result.add(new Zonemessage(R.drawable.action_comment,"df","fd",1));
		  //
			 result.add(new Zonemessage(R.drawable.action_comment,"wo","wo",2));
	 }
		 else{
			 String name,text;
			 Bitmap bitmap;
		 for(Map<String, Object> map : list)
		 {
			 bitmap = (Bitmap)map.get("photo");
			 name = String.valueOf((Integer)map.get("myID"));
			 text = (String)map.get("text");
			 lasttime = ((Timestamp)map.get("uploadTime")).toString();
			 result.add(new Zonemessage(bitmap,name,text,1));
		 }

		 }
		  return result;
		 }
	 
	
	@Override
	public void onClick(View v) {
		AddPopWindow addPopWindow = new AddPopWindow(FamilyzoneActivity.this);
		switch (v.getId()) {
		case R.id.btnAdd:
			
			addPopWindow.showPopupWindow(addButton);
			break;
		case R.id.btn_take_photo:
			camera(v);
			break;
		case R.id.btn_pick_photo:
			gallery(v);
			break;
			
			case R.id.add_pic:
				addPopWindow.dismiss();
				
				menuWindow = new SelectPicPopupWindow(FamilyzoneActivity.this, this);
				//��ʾ����
				menuWindow.showAtLocation(this.findViewById(R.id.mainView), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
			    break;
		default:
			break;
		}
	}	
	
	public void gallery(View view) {
		// ����ϵͳͼ�⣬ѡ��һ��ͼƬ
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/*
	 * �������ȡ
	 */
	public void camera(View view) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// �жϴ洢���Ƿ�����ã����ý��д洢
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// �õ�ͼƬ��ȫ·��
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(this, "δ�ҵ��洢�����޷��洢��Ƭ��", Toast.LENGTH_SHORT).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				//hasphoto = true;
				bitmap = data.getParcelableExtra("data");
				//mFace.setImageBitmap(bitmap);
				boolean delete = tempFile.delete();
				System.out.println("delete = " + delete);
				Intent intent = new Intent(FamilyzoneActivity.this,AddPicActivity.class);
				intent.putExtra("bitmap", bitmap);
				startActivity(intent);
				FamilyzoneActivity.this.finish();
				
				menuWindow.dismiss();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����ͼƬ
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	private void crop(Uri uri) {
		// �ü�ͼƬ��ͼ
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// �ü���ı�����1��1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// �ü������ͼƬ�ĳߴ��С
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// ͼƬ��ʽ
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// ȡ������ʶ��
		intent.putExtra("return-data", true);// true:������uri��false������uri
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
	    	//ʹ��apache HTTP�ͻ���ʵ��
	    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";
	    	HttpPost request = new HttpPost(urlStr);
	    	//������ݲ�����Ļ������ԶԴ��ݵĲ������з�װ
	    	sp = getSharedPreferences("user",0);
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	
	    	//����û���������
	    	params.add(new BasicNameValuePair("uname",sp.getString("username", "")));
	    	params.add(new BasicNameValuePair("times",String.valueOf(times)));
	    	params.add(new BasicNameValuePair("lasttime",lasttime));
	    	params.add(new BasicNameValuePair("requesttype","1"));
	    	try
	    	{
	    		
	    		
	    		
	    		//�������������
	    		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	    		HttpClient client = getHttpClient();
	    		//ִ�����󷵻���Ӧ
	    		HttpResponse response = client.execute(request);
	    		//in.close();
	    		//�ж��Ƿ�����ɹ�
	    		if(response.getStatusLine().getStatusCode()==200)
	    		{
	    			loginValidate = true;
	    			//�����Ӧ��Ϣ
	    			//responseMsg = EntityUtils.toString(response.getEntity());
	    			InputStream is = response.getEntity().getContent();
	    			ObjectInputStream ois = new ObjectInputStream(is);
	    			
	    			list = (List<Map<String,Object>>)ois.readObject();
	    			
	    			for(Map<String,Object> map : list)
	    			{
	    				byte[] photo = (byte[])map.get("photo");
	    				Log.v("buffer", photo.toString());
	    				tempfile = fileCache.getFile(photo.toString().substring(1, 5)+".jpg");
	    				tempfile.createNewFile();
	    				byte2File(photo,tempfile);
	    				Bitmap bitmap = decodeFile(tempfile);
	    				map.put("photo", bitmap);
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
				//URL�Ϸ���������һ��������֤�����Ƿ���ȷ*/
				
				
		    	boolean loginValidate = loginServer(lasttime, times);
		    	System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+list.get(0).get("text"));
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

    			Toast.makeText(getApplicationContext(), "���سɹ���", Toast.LENGTH_SHORT).show();
    			//Intent intent = new Intent();
    			//intent.setClass(LoginActivity.this, MainActivity.class);
    			//tartActivity(intent);
    			//finish();
    			
    			loadData(AutoListView.REFRESH);
				
    			break;
    		
    		case 1:
    			Toast.makeText(getApplicationContext(), "���سɹ���", Toast.LENGTH_SHORT).show();
    			loadData(AutoListView.LOAD);
    			break;
    			
    		case 2:
    			mDialog.cancel();
    			Toast.makeText(getApplicationContext(), "URL��֤ʧ��", Toast.LENGTH_SHORT).show();
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
}