package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;

import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Regover extends Activity implements OnClickListener{
	private static final int PHOTO_REQUEST_CAMERA = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	private Bitmap bitmap;
	private boolean hasphoto = false;
	
	
	
	private Button ok,selpic,back;
	EditText name,detail;
	private SelectPicPopupWindow menuWindow;
	private PopupWindow popupWindow;
	RoundImageView iv;
	LinearLayout ll_selsex,ll_selbirth,all;
	TextView birth,sex,title;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registcomplete);
		initView();
	}
	private void initView() {
		iv = (RoundImageView)findViewById(R.id.head);
		back = (Button) findViewById(R.id.back);
		back.setVisibility(View.GONE);
		title=(TextView)findViewById(R.id.title);
		title.setText("ifamilyע��");
		birth=(TextView)findViewById(R.id.birth);
		sex=(TextView)findViewById(R.id.sex);
		selpic = (Button) findViewById(R.id.selpic);
		selpic.setOnClickListener(this);
		back.setOnClickListener(this);
		ok=(Button)findViewById(R.id.ok);
		ok.setOnClickListener(this);
		name=(EditText)findViewById(R.id.name);
		detail=(EditText)findViewById(R.id.detail);
		all=(LinearLayout)findViewById(R.id.all);
		FontManager.changeFonts(all,this);
		ll_selsex=(LinearLayout)findViewById(R.id.ll_selsex);
		ll_selsex.setOnClickListener(this);
		ll_selbirth=(LinearLayout)findViewById(R.id.ll_selbirth);
		ll_selbirth.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_take_photo:
			camera(v);
			break;
		case R.id.btn_pick_photo:
			gallery(v);
			break;
		case R.id.ok:
			
			Finalupload();
			break;
		case R.id.back:
			Regover.this.finish();
			break;
		case R.id.ll_selsex:
			selectsex();
			break;
		case R.id.ll_selbirth:
			selectbirth();
			break;
		case R.id.selpic:
			
			menuWindow = new SelectPicPopupWindow(Regover.this, this);
			//��ʾ����
			menuWindow.showAtLocation(Regover.this.findViewById(R.id.all), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
			break;
	}

		
}
	
	
private void Finalupload() {
		// TODO �Զ����ɵķ������



	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RequestParams params = new RequestParams();
		if(name.getText().toString().trim().isEmpty()){
			Toast.makeText(this,"�������ǳ�",Toast.LENGTH_SHORT).show();
			return;
		}
		if(detail.getText().toString().trim().isEmpty()){
			Toast.makeText(this,"��������˼��",Toast.LENGTH_SHORT).show();
			return;
		}
		if(sex.getText().toString().trim().isEmpty()){
			Toast.makeText(this,"��ѡ���Ա�",Toast.LENGTH_SHORT).show();
			return;
		}
		if(birth.getText().toString().trim().isEmpty()){
			Toast.makeText(this,"��ѡ������",Toast.LENGTH_SHORT).show();
			return;
		}
		else
		{
			params.put("name", name.getText().toString());
			params.put("detail", detail.getText().toString());
		
			SharedPreferences sp = getSharedPreferences("user",0);
			final String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("uname", username);
			params.put("requesttype", "7");
			//params.put("gdetail", groupInfo.getText().toString());
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/UserInfoServlet";
			
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							Toast.makeText(Regover.this, "������Ϣ�޸���ɣ�\n��������������", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(Regover.this,Iguide.class);
							intent.putExtra("tab", 1);
							startActivity(intent);
							Regover.this.finish();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Regover.this,
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
					Toast.makeText(Regover.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	



	}
	///////ѡ���Ա�
	private void selectsex()
	{

		
		
		
		if (popupWindow!= null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow= null;

			}
		  final RelativeLayout  layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.sex_select, null);  
	      popupWindow= new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 	
	       RadioGroup rg=(RadioGroup)layout.findViewById(R.id.radioGroup1);
	       rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    	             @Override
	    	               public void onCheckedChanged(RadioGroup arg0, int arg1) {
	    	                  // TODO Auto-generated method stub
	    	                   //��ȡ������ѡ�����ID
	    	                   int radioButtonId = arg0.getCheckedRadioButtonId();
	    	                 //����ID��ȡRadioButton��ʵ��
	    	                    RadioButton rb = (RadioButton)layout.findViewById(radioButtonId);
	    	                  //�����ı����ݣ��Է���ѡ����
	    	                  sex.setText(rb.getText());
	    	                  uploadsex(rb.getText().toString());
	    	                  popupWindow.dismiss();
	    	              }

						
	    	         });
	       layout.setOnTouchListener(new OnTouchListener() {  
	              
	            public boolean onTouch(View v, MotionEvent event) {  
	                  
	                int height = layout.findViewById(R.id.radioGroup1).getTop();  
	                int y=(int) event.getY();  
	                if(event.getAction()==MotionEvent.ACTION_UP){  
	                    if(y<height){  
	                    	popupWindow.dismiss();
	                    	popupWindow=null;
	                    }  
	                    
	                }                 
	                return true;  
	            }  
	        });
	        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //����SelectPicPopupWindow��������ı���  
	        popupWindow.setBackgroundDrawable(dw);  
	       popupWindow.setAnimationStyle(R.style.popupfrombottom);
	       popupWindow.showAtLocation(all, Gravity.BOTTOM, 0, 0);

	
	
	}
	//ѡ���������
	private void selectbirth()
	{

		if (popupWindow!= null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

			}	
		
		final RelativeLayout  layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.birth_sel, null);  
	      popupWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
	       Button ok=(Button) layout.findViewById(R.id.b_bt_selbirth);	
		   final DatePicker dp=(DatePicker)layout.findViewById(R.id.birth_picker);
		   layout.setFocusable(true);
	       ok.setOnClickListener(new OnClickListener()
	       {

			@Override
			public void onClick(View arg0) {
				int mon=dp.getMonth()+1;
				String  date =dp.getYear() +"��"+mon+"��"+dp.getDayOfMonth()+"��";
				String Bdate = dp.getYear()+"-"+mon+"-"+dp.getDayOfMonth();
				birth.setText(date);
				uploadBirth(Bdate);
			
				popupWindow.dismiss();
				
			}
	       
	       }
	       );
	       
	       layout.setOnTouchListener(new OnTouchListener() {  
	              
	            public boolean onTouch(View v, MotionEvent event) {  
	                  
	                int height = layout.findViewById(R.id.pop).getTop();  
	                int y=(int) event.getY();  
	                if(event.getAction()==MotionEvent.ACTION_UP){  
	                    if(y<height){  
	                    	popupWindow.dismiss();
	                    	popupWindow=null;
	                    }  
	                    
	                }                 
	                return true;  
	            }  
	        });
	       ok.setOnKeyListener(new OnKeyListener() {  
				@Override
				public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
					 if (keyCode == KeyEvent.KEYCODE_BACK) {  
		    	          popupWindow.dismiss();  
		    	          popupWindow = null;  
		    	            return true;  
		    	        }  
		    	        return false;  
				}  
	    	});  



	        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //����SelectPicPopupWindow��������ı���  
	        popupWindow.setBackgroundDrawable(dw);  
	 //      popup.showAsDropDown(choosedate);
	       popupWindow.setAnimationStyle(R.style.popupfrombottom);
	     popupWindow.showAtLocation(all, Gravity.BOTTOM, 0, 0);
			
		
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
				hasphoto = true;
				bitmap = data.getParcelableExtra("data");
				iv.setImageBitmap(bitmap);
				if(tempFile!=null)
				{boolean delete = tempFile.delete();
				System.out.println("delete = " + delete);
				}upload();
				menuWindow.dismiss();
				

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void upload() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			
				
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				byte[] buffer = out.toByteArray();

				byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
				String photo = new String(encode);

				
				params.put("icon", photo);
			
			SharedPreferences sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("uname", username);
			params.put("requesttype", "2");
			//params.put("gdetail", groupInfo.getText().toString());
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/UserInfoServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							Toast.makeText(Regover.this, "��Ƭ��ӳɹ���", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Regover.this,
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
					Toast.makeText(Regover.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadsex(String sex)
	{

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			params.put("sex", sex);
			
			SharedPreferences sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("uname", username);
			params.put("requesttype", "3");
			//params.put("gdetail", groupInfo.getText().toString());
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/UserInfoServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							Toast.makeText(Regover.this, "�Ա���ӳɹ���", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Regover.this,
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
					Toast.makeText(Regover.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	public void uploadBirth(String birth)
	{


		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			params.put("birth", birth);
			
			SharedPreferences sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			//params.put("gname", groupname.getText().toString());
			params.put("uname", username);
			params.put("requesttype", "4");
			//params.put("gdetail", groupInfo.getText().toString());
			//params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/UserInfoServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							Toast.makeText(Regover.this, "������ӳɹ���", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Regover.this,
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
					Toast.makeText(Regover.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
	}
	
	private void crop(Uri uri) {
		// �ü�ͼƬ��ͼ
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("circleCrop","circle");
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
