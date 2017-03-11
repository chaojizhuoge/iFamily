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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FileCache;
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
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Privatemessage  extends Activity implements OnClickListener{

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	private Bitmap bitmap;
	private SelectPicPopupWindow menuWindow;
	
	
	private long myAcct;
	private TextView title;
	private Button back;
	private RoundImageView rv;
	private TextView tv_name,tv_sex,tv_birth,tv_detail,tv_account,ll_bottom;
	private LinearLayout ll_name,ll_sex,ll_birth,ll_detail;
	private String date=new String();
	private String Bdate = "";
	private PopupWindow popupWindow;
	private File tempfile;
	private FileCache fileCache = new FileCache(this);
	
	private boolean isYourself;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_privatemessage);
		Intent intent = getIntent();
		if(intent!=null)
			myAcct = intent.getLongExtra("privateAcct", 0);
		if(myAcct == Long.parseLong(getSharedPreferences("user",0).getString("username", "0")))
			isYourself = true;
		else
			isYourself = false;
		GetUserInfo(myAcct);
		initview();
		
	}
	private void GetUserInfo(long myAcct2) {
		// TODO 自动生成的方法存根

		try {
			RequestParams params = new RequestParams();

			params.put("uname", String.valueOf(myAcct2));
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
							Map<String,Object> map = new HashMap<String,Object>();
							map = (Map<String,Object>)oid.readObject();
							byte[] icon = (byte[])map.get("photo");
		    				
		    				//Log.v("buffer", photo.toString());
		    				tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
		    				tempfile.createNewFile();
		    				byte2File(icon,tempfile);
		    				Bitmap bitmaps = decodeFile(tempfile);
		    				//map.put("icon", bitmaps);
		    				
		    				rv.setImageBitmap(bitmaps);
		    				
		    				
		    				
		    				int year = ((Date)map.get("birth")).getYear()+1900;
		    				int month = ((Date)map.get("birth")).getMonth()+1;
		    				@SuppressWarnings("deprecation")
							int day = ((Date)map.get("birth")).getDate();
		    				
		    				
		    				tv_birth.setText(year+"年"+month+"月"+day+"日");
		    				tv_sex.setText((String)map.get("sex"));
		    				tv_detail.setText((String)map.get("detail"));
		    				tv_name.setText((String)map.get("name"));
		    				tv_account.setText(String.valueOf(myAcct));
		    				
		    				
		    				oid.close();
		    				is.close();
		    				fileCache.clear();
							//mDialog.dismiss();

							//Toast.makeText(AddPicActivity.this, "成功上传！", Toast.LENGTH_SHORT)
							//		.show();
							//Intent intent = new Intent(AddPicActivity.this,NoteActivity.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Privatemessage.this,
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
					Toast.makeText(Privatemessage.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
	protected void initview()
	{
		
		

		title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("个人资料");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				Privatemessage.this.finish();
			}
			 
		 });
		
	rv=(RoundImageView)findViewById(R.id.iv_pri_img);
	tv_birth=(TextView)findViewById(R.id.tv_p_birth);
	tv_sex=(TextView)findViewById(R.id.tv_p_sex);
	tv_detail = (TextView)findViewById(R.id.tv_p_deteil);
	tv_name = (TextView)findViewById(R.id.tv_p_name);
	tv_account = (TextView)findViewById(R.id.tv_p_account);
	ll_name=(LinearLayout)findViewById(R.id.li_p_name);
	ll_sex=(LinearLayout)findViewById(R.id.re_pri_sex);
	ll_birth=(LinearLayout)findViewById(R.id.re_birth);
	ll_detail=(LinearLayout)findViewById(R.id.re_pri_detail);
	ll_bottom=(TextView)findViewById(R.id.bottom_tv);
	rv.setOnClickListener(this);
	ll_name.setOnClickListener(this);	
	ll_sex.setOnClickListener(this);	
	ll_birth.setOnClickListener(this);	
	ll_detail.setOnClickListener(this);	
	LinearLayout all=(LinearLayout)findViewById(R.id.p_pri_ll);//整个界面
	FontManager.changeFonts(all,this);
	
	
	
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		if(isYourself)
		{
			switch(v.getId())
			{
			case R.id.iv_pri_img:
				
				menuWindow = new SelectPicPopupWindow(Privatemessage.this, this);
				//显示窗口
				menuWindow.showAtLocation(Privatemessage.this.findViewById(R.id.p_pri_ll), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				break;
			case R.id.li_p_name:
				intent=new Intent(Privatemessage.this,PrivateNameModify.class);
				startActivity(intent);
				
				if (popupWindow!= null && popupWindow.isShowing()) {

					popupWindow.dismiss();

					popupWindow= null;

				}
				Privatemessage.this.finish();
				break;
			case R.id.re_pri_sex:
				selsex();
				break;
			case R.id.re_birth:
				selbirth();
				break;
			case R.id.re_pri_detail:
				intent=new Intent(Privatemessage.this,PrivateMeModify.class);
				startActivity(intent);
				
				if (popupWindow!= null && popupWindow.isShowing()) {

					popupWindow.dismiss();

					popupWindow= null;

				}
				Privatemessage.this.finish();
			
				break;
			case R.id.btn_take_photo:
				camera(v);
				break;
			case R.id.btn_pick_photo:
				gallery(v);
				break;
			default: break;
		
		
			}
		}
		else
			Toast.makeText(this, "你不能修改别人的资料哦~~", Toast.LENGTH_SHORT);
			
		
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
				Toast.makeText(Privatemessage.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				//hasphoto = true;
				bitmap = data.getParcelableExtra("data");
				rv.setImageBitmap(bitmap);
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

							Toast.makeText(Privatemessage.this, "照片修改成功！", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Privatemessage.this,
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
					Toast.makeText(Privatemessage.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

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

							Toast.makeText(Privatemessage.this, "性别修改成功！", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Privatemessage.this,
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
					Toast.makeText(Privatemessage.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

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

							Toast.makeText(Privatemessage.this, "生日修改成功！", Toast.LENGTH_SHORT).show();
							//		.show();
							//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							//startActivity(intent);
						} else {
							Toast.makeText(Privatemessage.this,
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
					Toast.makeText(Privatemessage.this,
							"网络访问异常，错误码  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
	}
	
	

	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("circleCrop","circle");
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
	
	
	
	
	private void selsex()
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
	    	                   //获取变更后的选中项的ID
	    	                   int radioButtonId = arg0.getCheckedRadioButtonId();
	    	                 //根据ID获取RadioButton的实例
	    	                    RadioButton rb = (RadioButton)layout.findViewById(radioButtonId);
	    	                  //更新文本内容，以符合选中项
	    	                  tv_sex.setText(rb.getText());
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
	        //实例化一个ColorDrawable颜色为半透明  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        popupWindow.setBackgroundDrawable(dw);  
	       popupWindow.setAnimationStyle(R.style.popupfrombottom);
	       popupWindow.showAtLocation(ll_bottom, Gravity.BOTTOM, 0, 0);

	
	}
	private void selbirth()
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
				 date =dp.getYear() +"年"+mon+"月"+dp.getDayOfMonth()+"日";
				 Bdate = dp.getYear()+"-"+mon+"-"+dp.getDayOfMonth();
				tv_birth.setText(date);
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



	        //实例化一个ColorDrawable颜色为半透明  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        popupWindow.setBackgroundDrawable(dw);  
	 //      popup.showAsDropDown(choosedate);
	       popupWindow.setAnimationStyle(R.style.popupfrombottom);
	     popupWindow.showAtLocation(ll_bottom, Gravity.BOTTOM, 0, 0);
			
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


