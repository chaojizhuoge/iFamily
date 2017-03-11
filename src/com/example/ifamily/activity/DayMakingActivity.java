package com.example.ifamily.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.ifamily.DIPXPconvert;
import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.SoundMeter;
import com.example.ifamily.mywidget.ColorPickerView;
import com.example.ifamily.mywidget.Moveable;
import com.example.ifamily.mywidget.Mytext;
import com.example.ifamily.mywidget.Mytypeface;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;
import com.example.ifamily.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DayMakingActivity extends Activity implements OnClickListener{
	private static final int PHOTO_REQUEST_CAMERA = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���
	
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	
	private Bitmap bitmap;
	private boolean hassound = false;
	
	
	private Moveable card;
	private SoundMeter mSensor=new SoundMeter();
	private int width,height;
	private int left,right,top,bottom;
	private String voiceName;//¼��������
	private long startVoiceT, endVoiceT;//¼����ʼʱ���ֹͣʱ��
	private int ver;//��ʾ����  1�Ǻ���2������
	private boolean canclick=false;
	private Mytypeface my;
	private File file;
	private Typeface tf;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private int currenttext=0;
	private PopupWindow popupWindow;
	private ImageView iv_play;
	private TextView tv_ziti,tv_daxiao,tv_yanse,tv_kaishi,tv_tingzhi,tv_fangxiang,tv_wenzi,tv_shengyin,tv_beijing,tv_fangqi,tv_rekaishi;
	private LinearLayout ll_wenzi,ll_shengyin,ll_select;
	private  List<Mytext> mytext = new ArrayList<Mytext>();
	private long friend;
	SelectPicPopupWindow menuWindow;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		if(intent!=null)
			friend = intent.getLongExtra("friend", 0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.d_day_making);
		my=new Mytypeface(this);
		initview();
	}
	@SuppressLint("NewApi")
	private void initview()
	
	{
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//��������
		

		FontManager.changeFonts(all,this);
			
		tv_wenzi=(TextView)findViewById(R.id.addtext);////�������
		tv_shengyin=(TextView)findViewById(R.id.addvoice);////�������
		tv_beijing=(TextView)findViewById(R.id.addbg);////�޸ı���
		tv_fangqi=(TextView)findViewById(R.id.back);////�����༭		
		tv_ziti=(TextView)findViewById(R.id.ziti);////ѡ������		
		tv_daxiao=(TextView)findViewById(R.id.daxiao);//ѡ���С
		tv_yanse=(TextView)findViewById(R.id.yanse);//ѡ����ɫ
		tv_fangxiang=(TextView)findViewById(R.id.fangxiang);//ѡ���ı�����
		tv_kaishi=(TextView)findViewById(R.id.kaishi);//��ʼ¼��
		tv_rekaishi=(TextView)findViewById(R.id.rekaishi);//���¿�ʼ¼��
		
		iv_play=(ImageView)findViewById(R.id.play);
		tv_tingzhi=(TextView)findViewById(R.id.tingzhi);//ֹͣ¼��
		ll_wenzi=(LinearLayout)findViewById(R.id.wenzi);//����ѡ���
		ll_shengyin=(LinearLayout)findViewById(R.id.shengyin);//����ѡ���
		ll_select=(LinearLayout)findViewById(R.id.selectview);//���ֺ�����ѡ���
		
		tv_wenzi.setOnClickListener(this);
		tv_rekaishi.setOnClickListener(this);
		iv_play.setOnClickListener(this);
		tv_shengyin.setOnClickListener(this);
		tv_beijing.setOnClickListener(this);
		tv_fangqi.setOnClickListener(this);		
		tv_ziti.setOnClickListener(this);
		tv_daxiao.setOnClickListener(this);
		tv_yanse.setOnClickListener(this);
		tv_kaishi.setOnClickListener(this);
		tv_tingzhi.setOnClickListener(this);
		tv_fangxiang.setOnClickListener(this);
	
		tv_fangxiang.setText("����");
		
		
		DisplayMetrics DM = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(DM);
		height =DM.heightPixels;		
		width=DM.widthPixels;
		left=width/3;
		right=2*right;
		top=height/3;
		bottom=top*2;

		
		card=(Moveable)findViewById(R.id.mycard);

		//card.setMaxHeight(DIPXPconvert.px2dip(this, height*2/3));
		//card.setMaxWidth(DIPXPconvert.px2dip(this,width));
	card.setBackground(this.getResources().getDrawable(R.drawable.login_bg));
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.ziti:    //ѡ������
			selectziti();
			break;
		case R.id.daxiao://ѡ�������С
			selectsize();
			break;
		case R.id.yanse://ѡ��������ɫ
			selectcolor();
			break;
		case R.id.fangxiang ://ѡ����
			switch(mytext.get(currenttext).gettype())
			{case 1:
			tv_fangxiang.setText("����");
			//mytext.get(currenttext).settype(2);
			card.settype(2);
			card.invalidate();
			break;
			case 2:
			tv_fangxiang.setText("����");
			//mytext.get(currenttext).settype(1);
			card.settype(1);
			card.invalidate();
			break;
			}
			break;
		case R.id.addtext ://�������
     	   ll_select.setVisibility(View.GONE);
     	   ll_wenzi.setVisibility(View.GONE);
     	   ll_shengyin.setVisibility(View.GONE);
     	   if(mytext.size()!=0&&currenttext+1==mytext.size())
     	   {  card.save();
     	      currenttext++;
     	   }
     	   
     	   inputtext();
			break;
		case R.id.addvoice://�������
     	   ll_select.setVisibility(View.VISIBLE);
     	   ll_wenzi.setVisibility(View.GONE);
     	   ll_shengyin.setVisibility(View.VISIBLE);
			break;
		case R.id.addbg :       //��������ͼƬ
			gallery(v);
			break;
		case R.id.btn_take_photo:
			camera(v);
			break;
		case R.id.btn_pick_photo:
			gallery(v);
			break;
			
		case R.id.back ://�����༭
			upload(v,card);
			DayMakingActivity.this.finish();
			break;
		case R.id.kaishi://��ʼ¼��
			tv_kaishi.setVisibility(View.GONE);
			tv_tingzhi.setVisibility(View.VISIBLE);	
			startrec();	
			break;
		case R.id.tingzhi://ֹͣ¼��
			tv_tingzhi.setVisibility(View.GONE);
			tv_rekaishi.setVisibility(View.VISIBLE);
			iv_play.setVisibility(View.VISIBLE);
			stoprec();
			break;
		case R.id.rekaishi://����¼��
			tv_rekaishi.setVisibility(View.GONE);
			tv_tingzhi.setVisibility(View.VISIBLE);	
			startrec();	
			break;

		case R.id.play://���ŵ�ǰ����
			playMusic(android.os.Environment.getExternalStorageDirectory()+"/"+voiceName) ;
			break;			
		}				
	}	
	
	
	public void upload(View view,View card) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();

			Bitmap cardbitmap = createViewBitmap(card);
			
			File image = new File(android.os.Environment.getExternalStorageDirectory()+"/DIY");
			image.createNewFile();

			//Convert bitmap to byte array

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			cardbitmap.compress(CompressFormat.JPEG, 100, bos);
			byte[] bitmapdata = bos.toByteArray();

			//write the bytes in file
			FileOutputStream fos = new FileOutputStream(image);
			fos.write(bitmapdata);
			fos.close();
			bos.close();

			SharedPreferences sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			//params.put("groupId", String.valueOf(groupID));
			params.put("from", username);
			params.put("to", String.valueOf(friend));
			params.put("image", image);
			if(hassound)
			params.put("sound", file);
			//params.put("text", et_present.getText().toString());
			//params.put("hassound", String.valueOf(hassound));
			//params.put("requesttype", "1");
			String url = "http://103.31.241.201:8080/IFamilyServer/DayServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							//Toast.makeText(Setgroup.this, "��ͥ�ɹ�������", Toast.LENGTH_SHORT)
									//.show();
							Intent intent = new Intent(DayMakingActivity.this,DayMain.class);
							startActivity(intent);
							DayMakingActivity.this.finish();
						} else {
							Toast.makeText(DayMakingActivity.this,
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
					Toast.makeText(DayMakingActivity.this,
							"sb��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public Bitmap createViewBitmap(View v) {
	    Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
	            Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    v.draw(canvas);
	    return bitmap;
	}
	
	//�����������ı�
	 @SuppressLint("CutPasteId")
	private void inputtext()
	    {
	    
	    	  final  RelativeLayout   layout = ( RelativeLayout ) LayoutInflater.from(this).inflate(R.layout.d_daymaking_text, null);
	    	  final EditText  ll_input=(EditText)layout.findViewById(R.id.texts);
	    	  TextView ok=(TextView)layout.findViewById(R.id.ok);
	    	  ok.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
             	   ll_select.setVisibility(View.VISIBLE);
             	   ll_wenzi.setVisibility(View.VISIBLE);
             	   ll_shengyin.setVisibility(View.GONE);
				   mytext.add(new Mytext(ll_input.getText().toString(),1,R.color.blue,my.gettypeface(5)));
				   card.addtext(mytext.get(currenttext));
				   if(currenttext!=0) card.setcurrent(card.getcurrent()+1);
				   card.invalidate();
				   popupWindow.dismiss();
                   popupWindow=null;
                	
				}	    		  
	    	  });
	    	
	    	  ScrollView scall=(ScrollView)layout.findViewById(R.id.sc);
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
	            popupWindow.setFocusable(true);
		        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
		        ColorDrawable dw = new ColorDrawable(0xb0000000);  
		        //����SelectPicPopupWindow��������ı���  
		        popupWindow.setBackgroundDrawable(dw);  
		 //      popup.showAsDropDown(choosedate);
	            popupWindow.setAnimationStyle(R.style.popupfrombottom);
	            popupWindow.showAtLocation(scall,Gravity.CENTER, 0, 0);
	    }
	
	 //������ ѡ������
	 private void selectziti()
	    {

	    	  final  RelativeLayout  layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.d_daymaking_pop_ziti, null);
	    	  final TextView  ll_input=(TextView)layout.findViewById(R.id.thismain);
	    	  tf=my.gettypeface(5);
	    	  ll_input.setTypeface(tf);
	    	  RadioGroup radiogroup=(RadioGroup)layout.findViewById(R.id.select);
	    	  radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	              @Override
	                public void onCheckedChanged(RadioGroup arg0, int arg1) {
	                   // TODO Auto-generated method stub
	                    //��ȡ������ѡ�����ID
	                    int radioButtonId = arg0.getCheckedRadioButtonId();
	                    switch(radioButtonId)
	                    {
	                    case R.id.caoshu:
	                    	tf=my.gettypeface(1);
	                    	ll_input.setText("����һ��ʾ�� ���ȷ��ʹ��");
	                    	ll_input.setTypeface(tf);
	                    	
	                    	canclick=true;
	                 	   break;
	                    case R.id.miaowu:
	                    	tf=my.gettypeface(2);
	                    	ll_input.setText("����һ��ʾ�� ���ȷ��ʹ��");
	                    	ll_input.setTypeface(tf);
	                    	canclick=true;

	                 	   break;
	                    case R.id.gete:
	                    	tf=my.gettypeface(3);
	                    	ll_input.setText("This is a test.Click to use");
	                    	ll_input.setTypeface(tf);
	                    	canclick=true;
	                 	   
	                 	   break;
	                    case R.id.huati:
	                    	tf=my.gettypeface(4);
	                    	ll_input.setText("����һ��ʾ�� ���ȷ��ʹ��");
	                    	ll_input.setTypeface(tf);
	                    	canclick=true;
	                 	   break;
	                    case R.id.kaiti:
	                    	tf=my.gettypeface(5);
	                    	ll_input.setText("����һ��ʾ�� ���ȷ��ʹ��");
	                    	ll_input.setTypeface(tf);
	                    	canclick=true;
		                 	   break;
	                    case R.id.xingkai:
	                    	tf=my.gettypeface(6);
	                    	ll_input.setText("����һ��ʾ�� ���ȷ��ʹ��");
	                    	ll_input.setTypeface(tf);
	                    	canclick=true;
		                 	   break;
	                    }
	                    card.settf(tf);
	                  //  mytext.get(currenttext).settf(tf);
	                    card.invalidate();

	               }
	          });
	    	
	    	  
	    	  ll_input.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(canclick==true){
						popupWindow.dismiss();
						popupWindow=null;
					}
				}
	    		  
	    	  });
	    	      	 
	    	  ScrollView scall=( ScrollView)layout.findViewById(R.id.sc);
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
		        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
		        ColorDrawable dw = new ColorDrawable(0xb0000000);  
		        //����SelectPicPopupWindow��������ı���  
		        popupWindow.setBackgroundDrawable(dw);  
		 //      popup.showAsDropDown(choosedate);
	            popupWindow.setAnimationStyle(R.style.popupfrombottom);
	            popupWindow.showAtLocation(scall,Gravity.CENTER, 0, 0);
	    }
	 
	 //������ ѡ�������С
		private void selectsize()
	    {
	    
	    	  final  RelativeLayout   layout = ( RelativeLayout ) LayoutInflater.from(this).inflate(R.layout.d_day_makingpop_size, null);
	    	  final EditText  ll_input=(EditText)layout.findViewById(R.id.texts);
	    	  TextView  ok=(TextView)layout.findViewById(R.id.ok);
	    	  final TextView  testfield=(TextView)layout.findViewById(R.id.testfield);
	    	  testfield.setText("��");
	    	  testfield.setTextSize(12);
	    	  TextView test=(TextView)layout.findViewById(R.id.tes);
	    	 test.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
					testfield.setTextSize(Integer.valueOf(ll_input.getText().toString()).intValue());	                	
					}	    		  
		    	  });
	    	  ok.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				
					if(Integer.valueOf(ll_input.getText().toString()).intValue()>0)
					{
						//���������С
					//mytext.get(currenttext).setsize(DIPXPconvert.sp2px(DayMakingActivity.this, Integer.valueOf(ll_input.getText().toString()).intValue()));
					card.setsize(DIPXPconvert.sp2px(DayMakingActivity.this, Integer.valueOf(ll_input.getText().toString()).intValue()));
					card.invalidate();
					}
					popupWindow.dismiss();
                	popupWindow=null;
                	
				}	    		  
	    	  });
	    	
	    	  ScrollView scall=(ScrollView)layout.findViewById(R.id.sc);
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
	            popupWindow.setFocusable(true);
		        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
		        ColorDrawable dw = new ColorDrawable(0xb0000000);  
		        //����SelectPicPopupWindow��������ı���  
		        popupWindow.setBackgroundDrawable(dw);  
		 //      popup.showAsDropDown(choosedate);
	            popupWindow.setAnimationStyle(R.style.popupfrombottom);
	            popupWindow.showAtLocation(scall,Gravity.CENTER, 0, 0);
	    }
	 	 
		 //������ ѡ��������ɫ
		private void selectcolor()
	    {
	    
	    	  final  RelativeLayout   layout = ( RelativeLayout ) LayoutInflater.from(this).inflate(R.layout.d_day_makingpopcolor, null);
	    	  final TextView  ok=(TextView)layout.findViewById(R.id.ok);
	    	  final ColorPickerView cp=(ColorPickerView)layout.findViewById(R.id.pickcolor);


	    	    	cp.setFocusable(true);
	    	    	cp.setOnTouchListener(new OnTouchListener(){

	    				@Override
	    				public boolean onTouch(View arg0, MotionEvent arg1) {
	    					// TODO Auto-generated method stub
	    					mytext.get(currenttext).setcolor(cp.getcolor());
	    					card.invalidate();
	    					ok.setTextColor(cp.getcolor());
	    					return false;
	    				}
	    	    		
	    	    	});

	    	    	ok.setOnClickListener(new OnClickListener(){
	    				@Override
	    				public void onClick(View arg0) {
	    					// TODO Auto-generated method stub
	    					
	    					//mytext.get(currenttext).setcolor(cp.getcolor());
	    					card.setcolor(cp.getcolor());
	    					card.invalidate();
	    					popupWindow.dismiss();
	                    	popupWindow=null;
	                    	
	    				}	    		  
	    	    	  });
	    	
	    	  ScrollView scall=(ScrollView)layout.findViewById(R.id.sc);
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
	            popupWindow.setFocusable(true);
		        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
		        ColorDrawable dw = new ColorDrawable(0xb0000000);  
		        //����SelectPicPopupWindow��������ı���  
		        popupWindow.setBackgroundDrawable(dw);  
		 //      popup.showAsDropDown(choosedate);
	            popupWindow.setAnimationStyle(R.style.popupfrombottom);
	            popupWindow.showAtLocation(scall,Gravity.CENTER, 0, 0);
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
	
		public void startrec()
		{
			if (!Environment.getExternalStorageDirectory().exists()) {
				Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();			
			}
			startVoiceT = SystemClock.currentThreadTimeMillis();
			voiceName = Utils.getCurrentTime() + ".amr";	
			voiceName = voiceName.replaceAll(":", "");
			mSensor.start(voiceName);
		}
		public void stoprec()
		{
			mSensor.stop();
			file = new File(android.os.Environment.getExternalStorageDirectory()+"/"+voiceName);
			hassound = true;
		}
		
		
		private void playMusic(String name) {
			try {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
				}
				mMediaPlayer.reset();
			
				mMediaPlayer.setDataSource(name); 			
				mMediaPlayer.prepare(); 
				mMediaPlayer.start();
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	
		public void gallery(View view) {
			// ����ϵͳͼ�⣬ѡ��һ��ͼƬ
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		}

		/*
		 * �������ȡ
		 */
		public void camera(View view) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			// �жϴ洢���Ƿ�����ã����ý��д洢
			/*
			if (hasSdcard()) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
			}
			*/
			startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
		}

		@SuppressLint("NewApi")
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
				 ContentResolver resolver = getContentResolver();
					// �õ�ͼƬ��ȫ·��
					//hasphoto = true;
					Uri originalUri = data.getData();        //���ͼƬ��uri 
					 
		            try {
						bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					} catch (FileNotFoundException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}        //�Եõ�bitmapͼƬ
		            card.setBackgroundDrawable(new BitmapDrawable(bitmap));
					//card.setBackground(new BitmapDrawable(bitmap));
					//menuWindow.dismiss();
				
			} else if (requestCode == PHOTO_REQUEST_CAMERA &&  resultCode == Activity.RESULT_OK && null != data) {
				//hasphoto = true;
				bitmap = (Bitmap)data.getParcelableExtra("data");
				card.setBackgroundDrawable(new BitmapDrawable(bitmap));
				//card.setBackground(new BitmapDrawable(bitmap));
				menuWindow.dismiss();

			} else if (requestCode == PHOTO_REQUEST_CUT && resultCode == Activity.RESULT_OK && null != data) {
				try {
					//hasphoto = true;
					bitmap = data.getParcelableExtra("data");
					//pic.setImageBitmap(bitmap);
					if(tempFile != null)
					{boolean delete = tempFile.delete();
					System.out.println("delete = " + delete);
					}menuWindow.dismiss();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	
	
	
	
	
	
	
}
