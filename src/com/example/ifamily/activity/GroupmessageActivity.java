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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifamily.DIPXPconvert;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.adapter.RelationshipMAdapter;
import com.example.ifamily.adapter.ZoneCommentAdapter;
import com.example.ifamily.message.RelationM;
import com.example.ifamily.message.Relations;
import com.example.ifamily.message.ZonecommentM;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.example.ifamily.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GroupmessageActivity extends Activity implements android.content.DialogInterface.OnClickListener {
	
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	SelectPicPopupWindow menuWindow;
	  private ImageView tishi;
	  boolean isFirstIn = false;
	  private TextView iknow;
	  private RelativeLayout reee;	
	private GridView gv;
	private TextView title,groupName,groupid,groupdetail,grouptime;
	private RoundImageView groupPhoto;
	private Button back;
	
	
	
	private Relations relations;
	private PopupWindow popupWindow;
	private	LinearLayout familymessage,reback;
	private RelativeLayout addf,quit;
	private List<RelationM> males=new ArrayList<RelationM>();
	private List<RelationM> females=new ArrayList<RelationM>();
	private String groupId;
	private Map<String,Object>Map = new HashMap<String,Object>();
	private List<Map<String,Object>>theirObject = new ArrayList<Map<String,Object>>();
	private File tempfile;
	private FileCache fileCache = new FileCache(this);
	private boolean see;
	RelationshipMAdapter oia;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Intent intent = getIntent();
			if(intent != null){
				groupId = intent.getStringExtra("groupID");
				see = intent.getBooleanExtra("see", true);
			}
			MyApplication.getInstance().addActivity(this);
			setContentView(R.layout.familymessage);
			GetInfo();
			initview();			
		}
		private void GetInfo() {
			// TODO 自动生成的方法存根
try {
				
				RequestParams params = new RequestParams();
				//params.put("sex", sex);
				
				SharedPreferences sp = this.getSharedPreferences("user",0);
				String username = sp.getString("username", "");
				//params.put("gname", groupname.getText().toString());
				params.put("groupId", groupId);
				params.put("requesttype", "1");
				//params.put("times",String.valueOf(times));
		    	//params.put("lasttime",lasttime);
				//params.put("gdetail", groupInfo.getText().toString());
				//params.put("hasphoto", String.valueOf(hasphoto));
				String url = "http://103.31.241.201:8080/IFamilyServer/GroupServlet";

				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, params, new AsyncHttpResponseHandler() {
					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						try {
							if (statusCode == 200) {
								InputStream is = new ByteArrayInputStream(responseBody);
								ObjectInputStream ois = new ObjectInputStream(is);
								Map = (Map<String,Object>)ois.readObject();
								byte[] head = (byte[])Map.get("head");
			    				
			    				//Log.v("buffer", photo.toString());
			    				tempfile = fileCache.getFile(String.valueOf(Math.random()*1000));
			    				tempfile.createNewFile();
			    				byte2File(head,tempfile);
			    				Bitmap bitmap = decodeFile(tempfile);
			    				//Map.put("head", bitmap);
			    				groupName.setText((String)Map.get("name"));
			    				groupid.setText("账号："+groupId);
			    				groupdetail.setText((String)Map.get("detail"));
			    				
			    				grouptime.setText(((Timestamp)Map.get("time")).getYear()+1900+"年"+(((Timestamp)Map.get("time")).getMonth()+1)+"月"+(((Timestamp)Map.get("time")).getDate())+"日");
			    				groupPhoto.setImageBitmap(bitmap);
			    				
			    				
								theirObject = (List<Map<String, Object>>) Map.get("member");
				    			
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
				    			for(Map<String,Object> map:theirObject)
				    			{
				    				if(((String)map.get("sex")).equals("男"))
				    					males.add(new RelationM((Long)map.get("myAcct"),map.get("icon"),(String)map.get("name")));
				    				else
				    					females.add(new RelationM((Long)map.get("myAcct"),map.get("icon"),(String)map.get("name")));
				    			}
				    			oia.notifyDataSetChanged();
				    			int i=Math.max(males.size(),females.size());
				    			gv.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,DIPXPconvert.dip2px(GroupmessageActivity.this,60*i)));   
				    			ois.close();
				    			is.close();
				    			fileCache.clear();
				    			
				    			

								//Toast.makeText(OldObjectActivity.this.getActivity(), "性别修改成功！", Toast.LENGTH_SHORT).show();
								//		.show();
								//Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
								//startActivity(intent);
							} else {
								Toast.makeText(GroupmessageActivity.this,
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
						Toast.makeText(GroupmessageActivity.this,
								"网络访问异常，错误码  > " + statusCode, 0).show();

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		protected void initview()
		{
			groupName = (TextView)findViewById(R.id.group_name);
			groupdetail = (TextView)findViewById(R.id.tv_p_deteil);
			groupid = (TextView)findViewById(R.id.group_num);
			groupPhoto = (RoundImageView)findViewById(R.id.iv_pri_img);
			grouptime = (TextView)findViewById(R.id.tv_p_birth);
			 
			 tishi=(ImageView)findViewById(R.id.tishi);
			 iknow=(TextView)findViewById(R.id.iknow);
			 reee=(RelativeLayout)findViewById(R.id.reee);

				// 判断程序与第几次运行
			      
			  SharedPreferences sps = GroupmessageActivity.this.getSharedPreferences("SP", Context.MODE_PRIVATE);
			  
			  if(sps.getBoolean("isfirsgroup", false)!=true){		  		  
			  Editor editor = sps.edit();
			  editor.putBoolean("isfirsgroup",true);
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
			
			
			
			addf=(RelativeLayout)findViewById(R.id.addfriend);
			addf.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(((Integer)PushApplication.getInstance().getUserInfo().get("myID"))==(Integer)Map.get("myID"))
					{
				Intent it=new Intent(GroupmessageActivity.this,EnterGroupAddF.class);
				it.putExtra("groupId", Integer.parseInt(groupId));
				startActivity(it);
					}
					else
						Toast.makeText(GroupmessageActivity.this, "只有群主可以邀请好友哦~", Toast.LENGTH_LONG).show();
				}
				
			});
			quit=(RelativeLayout)findViewById(R.id.quit);
			
			quit.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(((Integer)PushApplication.getInstance().getUserInfo().get("myID"))==(Integer)Map.get("myID"))
					{
						Toast.makeText(GroupmessageActivity.this, "群主不可以退出哦~", Toast.LENGTH_LONG).show();
						return;
					}
					quits(arg0);

				}

				
				
			});
			
			reback=(LinearLayout)findViewById(R.id.re_back);
			reback.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					switch(arg0.getId())
					{
					case R.id.btn_take_photo:
						camera(arg0);
						break;
					case R.id.btn_pick_photo:
						gallery(arg0);
						break;
					default:
					menuWindow = new SelectPicPopupWindow(GroupmessageActivity.this, this);
					//显示窗口
					menuWindow.showAtLocation(GroupmessageActivity.this.findViewById(R.id.familymessage), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
					break;
					}
				}
				
			});
			familymessage=(LinearLayout)findViewById(R.id.familymessage);
			FontManager.changeFonts(familymessage,this);
			title = (TextView)findViewById(R.id.title);
			title.setFocusable(true);
			title.setFocusableInTouchMode(true);
			title.requestFocus();
			 back = (Button)findViewById(R.id.back);
			 title.setText("家庭信息");
			 back.setOnClickListener(new OnClickListener()
			 {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(see){
				Intent intent=new Intent(GroupmessageActivity.this,ChatChatActivity.class);
				intent.putExtra("groupId", groupId);
					 startActivity(intent);
					}
					GroupmessageActivity.this.finish();
				}
				 
			 });
			gv=(GridView)findViewById(R.id.relatives);
			if(!see)
				gv.setVisibility(View.GONE);
			gv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long arg3) {
					// TODO Auto-generated method stub
					switch((position+1)%2)
			    	{
			    	case 0:
			    		if(((position+1)/2)<=females.size()){
							relations=null;
							relations=new Relations(1);
							TextView tv=(TextView)v.findViewById(R.id.name);
							
							popmale(tv,(position-1)/2);
			    		}
			    		break;
					case 1: 
						if((position/2+1)<=males.size()){
							relations=null;
							relations=new Relations(2);
							TextView tv=(TextView)v.findViewById(R.id.name);
							popmale(tv,position/2);
						}
						break;
						default: break;
				}}
				
			});
			
			initdata();			
		}
		
		public void quits(View arg0) {
			// TODO 自动生成的方法存根
			new AlertDialog.Builder(this)
		      .setTitle("Question").setMessage(
		      "确定要退出该家庭？").setPositiveButton("Yes",
		       this 
		     ).setNegativeButton("No",
		    		 this).show();
		}
		protected void initdata()
		{
			
			//males.add(new RelationM(123456,R.drawable.n1,"爷爷"));
			//males.add(new RelationM(123456,R.drawable.n2,"爸爸"));
			//males.add(new RelationM(123456,R.drawable.n3,"大姑爷"));
			//males.add(new RelationM(123456,R.drawable.n4,"小姑爷"));
			//males.add(new RelationM(123456,R.drawable.n5,"叔叔"));
			//males.add(new RelationM(123456,R.drawable.n6,"堂弟新新"));
			//females.add(new RelationM(123456,R.drawable.v1,"奶奶"));
			//females.add(new RelationM(123456,R.drawable.v2,"妈妈"));
			//females.add(new RelationM(123456,R.drawable.v3,"大姑姑"));
			//females.add(new RelationM(123456,R.drawable.v4,"小姑姑"));
			//females.add(new RelationM(123456,R.drawable.v5,"婶婶"));
			//females.add(new RelationM(123456,R.drawable.v6,"我"));
			//females.add(new RelationM(123456,R.drawable.v7,"堂妹淼淼"));
			//females.add(new RelationM(123456,R.drawable.v8,"堂妹cici"));
			oia=new RelationshipMAdapter(this,males,females);
			oia.setpop(familymessage);
			
			gv.setAdapter(oia);		
			
		}
		
		 @SuppressLint("NewApi")
		private void popmale(final TextView name,final int position)
		    {
		    	if (popupWindow!= null && popupWindow.isShowing()) {

		    		popupWindow.dismiss();

		    		popupWindow= null;

		    		}
		    
		    	  final  RelativeLayout  layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.familymessagepop, null);
		    	  LinearLayout relative=(LinearLayout)layout.findViewById(R.id.relatives);
		    	  List<TextView> bt=new ArrayList<TextView>();
		    	  for(int i=1;i<relations.getrelations().size()+1;i++)
		    	  {
		    		  final TextView tv=new TextView(this);
		    		  tv.setHeight(DIPXPconvert.dip2px(this,50));
		    		  tv.setWidth(LayoutParams.MATCH_PARENT);
		    		  tv.setId(i);
		    		  tv.setGravity(Gravity.CENTER);
		    		  tv.setText(relations.getrelations().get(i-1));
		    		  tv.setTextColor(this.getResources().getColor(R.color.blue));
		    		  tv.setBackground(this.getResources().getDrawable(R.drawable.cornerview));
		    		  tv.setOnClickListener(new OnClickListener()
		    		  {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							name.setText(tv.getText());
							//这里写点击事件
							////////////////////////////////////////////////////////////
							popupWindow.dismiss();
						}
		    			  
		    		  });
		    		  relative.addView(tv);
		    		  
		    	  }
		    	  TextView free=new TextView(this);
		    	  free.setHeight(DIPXPconvert.dip2px(this,50));
		    	  free.setWidth(LayoutParams.MATCH_PARENT);
		    	  free.setGravity(Gravity.CENTER);
		    	  free.setText("自定义");
		    	  free.setTextColor(this.getResources().getColor(R.color.blue));
		    	  free.setBackground(this.getResources().getDrawable(R.drawable.cornerview));
	    		  relative.addView(free);
	    		  free.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						popupWindow.dismiss();
						popedit(name);
						
					}
	    			  
	    		  });
		    	  ScrollView sc=( ScrollView)layout.findViewById(R.id.sc);
		    	  ScrollView scall=( ScrollView)findViewById(R.id.sc);
		    	  layout.setOnTouchListener(new OnTouchListener() {  
		              
			            public boolean onTouch(View v, MotionEvent event) {  
			                  
			                int height = layout.findViewById(R.id.sc).getTop();  
			                int bheight=layout.findViewById(R.id.sc).getHeight()+height;  
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
		    	  
		            popupWindow= new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
			        //实例化一个ColorDrawable颜色为半透明  
			        ColorDrawable dw = new ColorDrawable(0xb0000000);  
			        //设置SelectPicPopupWindow弹出窗体的背景  
			        popupWindow.setBackgroundDrawable(dw);  
			 //      popup.showAsDropDown(choosedate);
		            popupWindow.setAnimationStyle(R.style.popupfrombottom);
		            popupWindow.showAtLocation(scall,Gravity.CENTER, 0, 0);

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
		
		
		private void popedit(final TextView name)
		{

				if (popupWindow!= null && popupWindow.isShowing()) {

					popupWindow.dismiss();

					popupWindow= null;

					}
				  final LinearLayout  layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.zoneinputcomment, null);
				  final EditText comments=(EditText)layout.findViewById(R.id.comments);
				  comments.setHint("请输入自定义关系");
				  comments.getText();

				  Button ok=(Button)layout.findViewById(R.id.ok);
				  ok.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
						name.setText(comments.getText());
					}
					  
				    });
			        popupWindow= new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
			  	    popupWindow.setFocusable(true);
			  	    popupWindow.setBackgroundDrawable(new BitmapDrawable());
			  	    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			        popupWindow.setAnimationStyle(R.style.popupfrombottom);
			        popupWindow.showAtLocation(familymessage,Gravity.BOTTOM, 0, 0);

			
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
		public void onClick(DialogInterface dialog, int which) {
			switch(which)
			{
			case DialogInterface.BUTTON_POSITIVE:

				// TODO 自动生成的方法存根
					try {
					
					RequestParams params = new RequestParams();
					
					
					SharedPreferences sp = this.getSharedPreferences("user",0);
					String username = sp.getString("username", "");
					params.put("uname", username);
					params.put("groupId", groupId);
					params.put("requesttype", "2");
					String url = "http://103.31.241.201:8080/IFamilyServer/GroupServlet";

					AsyncHttpClient client = new AsyncHttpClient();
					client.post(url, params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							try {
								if (statusCode == 200) {

									Toast.makeText(GroupmessageActivity.this, "您已经退出该群！", Toast.LENGTH_SHORT).show();

									Intent intent = new Intent(GroupmessageActivity.this,Iguide.class);
									intent.putExtra("tab", 1);
									startActivity(intent);
									GroupmessageActivity.this.finish();
								} else {
									Toast.makeText(GroupmessageActivity.this,
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
							Toast.makeText(GroupmessageActivity.this,
									"网络访问异常，错误码  > " + statusCode, 0).show();

						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
				
			
		        break;
			case DialogInterface.BUTTON_NEGATIVE:
				dialog.cancel();
		       }
		}  
		
		public void gallery(View view) {
			// 激活系统图库，选择一张图片
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		}

		/*
		 * 从相机获取
		 */
		public void camera(View view) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			// 判断存储卡是否可以用，可用进行存储
			/*
			if (hasSdcard()) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
			}
			*/
			startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Bitmap bitmap = null;
			if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
				 ContentResolver resolver = getContentResolver();
					// 得到图片的全路径
					Uri originalUri = data.getData();        //获得图片的uri 
					 
		            try {
						bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}        //显得到bitmap图片
					
					
					menuWindow.dismiss();
				
			} else if (requestCode == PHOTO_REQUEST_CAMERA &&  resultCode == Activity.RESULT_OK && null != data) {
				bitmap = (Bitmap)data.getParcelableExtra("data");
				
				menuWindow.dismiss();

			} 
			

			try{
			File myCaptureFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"IFamily",groupId+1);
			if(myCaptureFile.exists())
				myCaptureFile.delete();
			myCaptureFile.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
                                                 new FileOutputStream(myCaptureFile));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}

			

			super.onActivityResult(requestCode, resultCode, data);
		}
		
}
