package com.example.ifamily.adapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.example.ifamily.DIPXPconvert;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.SelectPicPopupWindow;
import com.example.ifamily.activity.AddschActiivity;
import com.example.ifamily.activity.GroupmessageActivity;
import com.example.ifamily.activity.Iguide;
import com.example.ifamily.activity.MyPicActivity;
import com.example.ifamily.activity.NoteActivity;
import com.example.ifamily.activity.Regover;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.adapter.DayDayAdapter.ViewHolder;
import com.example.ifamily.message.HeadM;
import com.example.ifamily.message.Zonemessage;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Zonelistadapter extends BaseAdapter{
	private Context context;
	private List<Zonemessage> zonemessages;

	private List<HeadM> imgs;
	private SelectPicPopupWindow menuWindow;
	private RelativeLayout all ;
	private Object bg;
	private String groupId;
	final int VIEW_TYPE = 3;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	final int TYPE_3 = 2;
	public Zonelistadapter(Context contexts,List<Zonemessage> messages) {
		 super();
		 this.context = contexts;
		 this.zonemessages =messages;
		}
	public Zonelistadapter(Context contexts,List<Zonemessage> messages,List<HeadM>heads) {
		 super();
		 this.context = contexts;
		 this.zonemessages =messages;
		 this.imgs=heads;
		}
	public Zonelistadapter(Context contexts,List<Zonemessage> messages,List<HeadM>heads,Object bg,String groupId) {
		 super();
		 this.context = contexts;
		 this.zonemessages =messages;
		 this.imgs=heads;
		 this.bg=bg;
		 this.groupId= groupId;
		}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return (zonemessages.size()+1);
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return zonemessages.get(arg0-1);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public int getItemViewType(int position) {

		// TODO Auto-generated method stub

		int p = position;

		if(p == 0)

		return TYPE_1;

		else if(zonemessages.get(position-1).gettype()==1)

		return TYPE_2;

		else if(zonemessages.get(position-1).gettype()==2)

		return TYPE_3;

		else

		return TYPE_1;



		}

	
	@Override

	public int getViewTypeCount() {

	// TODO Auto-generated method stub

	return VIEW_TYPE;

	}
	
	
	
	
	
	
	
	
	
	
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//final ViewHolder holder = null;
		int type = getItemViewType(position);
		ViewHolder3 holder3=null;
		 ViewHolder2 holder2=null;
		ViewHolder holder=null;
		//ViewHolder3 holder3=null;
		
		if(convertView == null)

		{ 

		Log.e("convertView = ", " NULL");



		//按当前所需的样式，确定new的布局

		switch(type)

		{case 0:

			holder3=new ViewHolder3();			
			convertView=LayoutInflater.from(context).inflate(R.layout.familyzoneheads, null);
			holder3.gv=(GridView)convertView.findViewById(R.id.gd_head);
			holder3.ll_bg=(LinearLayout)convertView.findViewById(R.id.background);
						
			convertView.setTag(holder3);
			Log.e("type = ","0");
			break;
		case 1:	
			holder= new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.mixed_feed_activity_item, null);
			/* all=(RelativeLayout)convertView.findViewById(R.id.all);
			FontManager.changeFonts(all, context);*/
			//Zonemessage message = zonemessages.get(position);
			holder.re=(RelativeLayout)convertView.findViewById(R.id.clickfield);
			holder.name=(TextViewM)convertView.findViewById(R.id.mixed_feed_authorname);
			holder.text=(TextView)convertView.findViewById(R.id.thought_main);
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.mixed_feed_author_photo);
			holder.num=(TextView)convertView.findViewById(R.id.num);
			holder.comnum=(TextView)convertView.findViewById(R.id.comnum);
			holder.comment=(ImageView)convertView.findViewById(R.id.iv_comment);
			holder.good=(ImageView)convertView.findViewById(R.id.iv_good);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.hisimg =(ImageView)convertView.findViewById(R.id.hisimg);
			convertView.setTag(holder);
			Log.e("type = ","1");
			break;
		case 2:
			holder2=new ViewHolder2();
			convertView=LayoutInflater.from(context).inflate(R.layout.familyzone_item_sch, null);
			/*	all=(RelativeLayout)convertView.findViewById(R.id.all);
				FontManager.changeFonts(all, context);*/
				//Zonemessage message = zonemessages.get(position);
				holder2.name=(TextViewM)convertView.findViewById(R.id.familyzone_feed_authorname);
				holder2.text=(TextView)convertView.findViewById(R.id.familyzone_sch_main);
				holder2.iv_l=(RoundImageView)convertView.findViewById(R.id.familyzone_sch_head);
				holder2.num=(TextView)convertView.findViewById(R.id.num);
				holder2.iwant=(ImageView)convertView.findViewById(R.id.zan);
				
				holder2.atplace=(TextView)convertView.findViewById(R.id.atplace);
				
				holder2.attime=(TextView)convertView.findViewById(R.id.attime);
				
				holder2.time=(TextView)convertView.findViewById(R.id.time);
				
				holder2.friends=(LinearLayout)convertView.findViewById(R.id.friends);
				convertView.setTag(holder2);
				Log.e("type = ","2");
			break;
		}
		
		}
		else

		{

		//有convertView，按样式，取得不用的布局

		switch(type)

		{

		case 0:
			
			holder3 = (ViewHolder3) convertView.getTag();
		break;
		case 1:
			holder = (ViewHolder) convertView.getTag();
			break
			;
		case 2:
			holder2 = (ViewHolder2) convertView.getTag();
			break
			;
		
		}
		}
		
		
		switch(type)

		{

		case 0:
			final LinearLayout aa=holder3.ll_bg;
			File myCaptureFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"IFamily",groupId+2);
			if (myCaptureFile.exists())
				holder3.ll_bg.setBackground(new BitmapDrawable(decodeFile(myCaptureFile)));
			else
				holder3.ll_bg.setBackgroundResource((Integer)bg);
			holder3.ll_bg.setOnClickListener(((Iguide)Zonelistadapter.this.context).getNote());
			holder3.gv.setNumColumns(imgs.size()+1);
			holder3.gv.setLayoutParams(new TableRow.LayoutParams(DIPXPconvert.dip2px(this.context,60*(imgs.size()+1)),DIPXPconvert.dip2px(this.context, 60)));
			GridHeadsAdapter gh=new GridHeadsAdapter(this.context,imgs);
			holder3.gv.setAdapter(gh);
			
			
			
			break;
		case 1:
			
			final ImageView good=holder.good;
			final TextView num=holder.num;
			holder.hisimg.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,MyPicActivity.class);
					//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
					intent.putExtra("icon",(Bitmap)zonemessages.get(position-1).gethisimg());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					 context.startActivity(intent);	
				}
				
			});
			
			
			
			Log.v("sb", zonemessages.get(position-1).gettime());
			holder.time.setText(zonemessages.get(position-1).gettime());
			if(zonemessages.get(position-1).gethisimg()!=null)
			{
			holder.hisimg.setImageBitmap((Bitmap)zonemessages.get(position-1).gethisimg());
			}

			if(zonemessages.get(position-1).getIsLiked())
				holder.good.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
			//holder.good.setBackgroundColor(android.graphics.Color.BLUE);
		
			holder.num.setText(zonemessages.get(position-1).getnum()+"个赞");
			holder.comnum.setText(zonemessages.get(position-1).getcomnum()+"条评论");
			holder.iv_l.setImageBitmap((Bitmap)zonemessages.get(position-1).getimg());
			holder.iv_l.setidandtype(zonemessages.get(position-1).getid(),1);
			holder.name.setText(zonemessages.get(position-1).getname());
			holder.name.setidandtype(zonemessages.get(position-1).getid(),1);
			holder.text.setText(zonemessages.get(position-1).gettext());
			
			holder.comment.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,ZoneCommentActivity.class);
					//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
					intent.putExtra("icon",(Bitmap)zonemessages.get(position-1).getimg());
					intent.putExtra("id", zonemessages.get(position-1).getid());
					intent.putExtra("name", zonemessages.get(position-1).getname());
					intent.putExtra("text", zonemessages.get(position-1).gettext());
					intent.putExtra("time", zonemessages.get(position-1).gettime());
					intent.putExtra("messageId", zonemessages.get(position-1).getMessageID());
					
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					 context.startActivity(intent);					
					
				} 
			});
			holder.re.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,ZoneCommentActivity.class);
					//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
					intent.putExtra("icon",(Bitmap)zonemessages.get(position-1).getimg());
					intent.putExtra("id", zonemessages.get(position-1).getid());
					intent.putExtra("name", zonemessages.get(position-1).getname());
					intent.putExtra("text", zonemessages.get(position-1).gettext());
					intent.putExtra("time", zonemessages.get(position-1).gettime());
					intent.putExtra("messageId", zonemessages.get(position-1).getMessageID());
					
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					 context.startActivity(intent);					
					
				} 
			});
			holder.good.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int LikeState;
					if(!zonemessages.get(position-1).getIsLiked())
					{
						LikeState = 1;
					}
					else
					{
						LikeState = 2;
					}
					
					  
	            	
	            	try {
	        			
	        			RequestParams params = new RequestParams();
	        			
	        			params.put("messageId", String.valueOf(zonemessages.get(position-1).getMessageID()));
	        			params.put("uname", context.getSharedPreferences("user",0).getString("username", ""));
	        			//params.put("friends", Friends);
	        			//params.put("startTime", sdate);
	        			//params.put("text", text.getText().toString());
	        			//params.put("atPlace", atPlace.getText().toString());
	        			params.put("requesttype", "5");
	        			params.put("LikeState", String.valueOf(LikeState));
	        			//params.put("addtype", "3");
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
	        							if(zonemessages.get(position-1).getIsLiked())
	        							{
	        								good.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like));
	        								zonemessages.get(position-1).setIsLiked(false);
	        								num.setText(zonemessages.get(position-1).getnum()-1+"个赞");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()-1);
	        							}
	        							else
	        							{
	        								good.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
	        								zonemessages.get(position-1).setIsLiked(true);
	        								num.setText(zonemessages.get(position-1).getnum()+1+"个赞");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()+1);
	        							}
	        							//Zonelistadapter.this.notifyDataSetChanged();
	        							
	        						} else {
	        							Toast.makeText(context,
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
	        					Toast.makeText(context,
	        							"网络访问异常，错误码  > " + statusCode, 0).show();

	        				}
	        			});

	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	  
	            
					
				} 
			});
			
			
			break;
		case 2:
			final LinearLayout friends=holder2.friends;
			final ImageView iwant=holder2.iwant;
			final TextView nums=holder2.num;
			holder2.num.setText("我也想去");
			if(zonemessages.get(position-1).getIsLiked())
			{
				holder2.iwant.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
				holder2.num.setText("我不想去了");
			}			
			holder2.atplace.setText("目的地："+zonemessages.get(position-1).getatplace());
			
			holder2.attime.setText("出行时间："+zonemessages.get(position-1).getattime());
			
			holder2.time.setText(zonemessages.get(position-1).gettime());
			holder2.friends.removeAllViews();
			initfriends(holder2.friends,zonemessages.get(position-1).getfriends());/////动态添加好友姓名
			
			
			
			
			holder2.iv_l.setImageBitmap((Bitmap)zonemessages.get(position-1).getimg());
			holder2.iv_l.setidandtype(zonemessages.get(position-1).getid(),1);
			holder2.name.setText(zonemessages.get(position-1).getname());
			holder2.name.setidandtype(zonemessages.get(position-1).getid(),1);
			holder2.text.setText(zonemessages.get(position-1).gettext());
			holder2.iwant.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					int LikeState;
					if(!zonemessages.get(position-1).getIsLiked())
					{
						LikeState = 1;
					}
					else
					{
						LikeState = 2;
					}
					
					  
	            	
	            	try {
	        			
	        			RequestParams params = new RequestParams();
	        			
	        			params.put("messageId", String.valueOf(zonemessages.get(position-1).getMessageID()));
	        			params.put("uname", context.getSharedPreferences("user",0).getString("username", ""));
	        			//params.put("friends", Friends);
	        			//params.put("startTime", sdate);
	        			//params.put("text", text.getText().toString());
	        			//params.put("atPlace", atPlace.getText().toString());
	        			params.put("requesttype", "5");
	        			params.put("LikeState", String.valueOf(LikeState));
	        			//params.put("addtype", "3");
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
	        							if(zonemessages.get(position-1).getIsLiked())
	        							{
	        								iwant.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like));
	        								zonemessages.get(position-1).setIsLiked(false);
	        								nums.setText("我也想去");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()-1);
	        								List<HeadM> fri = zonemessages.get(position-1).getfriends();
	        								List<HeadM> temp = new ArrayList<HeadM>();
	        								for(HeadM hm:fri)
	        								{
	        									if( hm.getid()==Long.parseLong(context.getSharedPreferences("user",0).getString("username", "")))
	        										temp.add(hm);
	        								}
	        								fri.removeAll(temp);
	        								zonemessages.get(position-1).setfriends(fri);
	        							    friends.removeAllViews();
	        								initfriends(friends,zonemessages.get(position-1).getfriends());
	        								
	        							}
	        							else
	        							{
	        								iwant.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
	        								zonemessages.get(position-1).setIsLiked(true);
	        								nums.setText("我不想去了");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()+1);
	        								List<HeadM> fri = zonemessages.get(position-1).getfriends();
	        								fri.add(new HeadM(Long.parseLong(context.getSharedPreferences("user",0).getString("username", "")),PushApplication.getInstance().getUserInfo().get("name")));
	        								zonemessages.get(position-1).setfriends(fri);
	        								friends.removeAllViews();
	        								initfriends(friends,zonemessages.get(position-1).getfriends());
	        							}
	        							//Zonelistadapter.this.notifyDataSetChanged();
	        							
	        						} else {
	        							Toast.makeText(context,
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
	        					Toast.makeText(context,
	        							"网络访问异常，错误码  > " + statusCode, 0).show();

	        				}
	        			});

	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	  
	            
					
				
				} 
			});

					
			
			break;
			default: break;}

			

		 return convertView;
		//return convertView;
		
		
		
		
		
		
		
		/*if(position==0){
			holder3=new ViewHolder3();
			convertView=LayoutInflater.from(context).inflate(R.layout.familyzoneheads, null);
			holder3.gv=(GridView)convertView.findViewById(R.id.gd_head);
			final LinearLayout ll_bg=(LinearLayout)convertView.findViewById(R.id.background);
			ll_bg.setBackgroundResource((Integer)bg);
			ll_bg.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					menuWindow = new SelectPicPopupWindow(context,this);
					//显示窗口
					menuWindow.showAtLocation(ll_bg, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				}
				
			});
			holder3.gv.setNumColumns(imgs.size()+1);
			holder3.gv.setLayoutParams(new TableRow.LayoutParams(DIPXPconvert.dip2px(this.context,60*(imgs.size()+1)),DIPXPconvert.dip2px(this.context, 60)));
			//GridHeadsAdapter gh=new GridHeadsAdapter(this.context,imgs);
			//holder3.gv.setAdapter(gh);
			
		}else{
		switch(zonemessages.get(position-1).gettype())
		{case 1:

			
			convertView=LayoutInflater.from(context).inflate(R.layout.mixed_feed_activity_item, null);
			/* all=(RelativeLayout)convertView.findViewById(R.id.all);
			FontManager.changeFonts(all, context);*/
			//Zonemessage message = zonemessages.get(position);
			/*holder.re=(RelativeLayout)convertView.findViewById(R.id.clickfield);
			holder.name=(TextViewM)convertView.findViewById(R.id.mixed_feed_authorname);
			holder.text=(TextView)convertView.findViewById(R.id.thought_main);
			holder.iv_l=(RoundImageView)convertView.findViewById(R.id.mixed_feed_author_photo);
			holder.num=(TextView)convertView.findViewById(R.id.num);
			holder.comnum=(TextView)convertView.findViewById(R.id.comnum);
			holder.comment=(ImageView)convertView.findViewById(R.id.iv_comment);
			holder.good=(ImageView)convertView.findViewById(R.id.iv_good);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.hisimg =(ImageView)convertView.findViewById(R.id.hisimg);
		
			
			
			
	
			
			holder.hisimg.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,MyPicActivity.class);
					//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
					intent.putExtra("icon",(Bitmap)zonemessages.get(position-1).getimg());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					 context.startActivity(intent);	
				}
				
			});
			
			
			
			Log.v("sb", zonemessages.get(position-1).gettime());
			holder.time.setText(zonemessages.get(position-1).gettime());
			if(zonemessages.get(position-1).gethisimg()!=null)
			{
			holder.hisimg.setImageBitmap((Bitmap)zonemessages.get(position-1).gethisimg());
			}

			if(zonemessages.get(position-1).getIsLiked())
				holder.good.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
			//holder.good.setBackgroundColor(android.graphics.Color.BLUE);
		
			holder.num.setText(zonemessages.get(position-1).getnum()+"个赞");
			holder.comnum.setText(zonemessages.get(position-1).getcomnum()+"条评论");
			holder.iv_l.setImageBitmap((Bitmap)zonemessages.get(position-1).getimg());
			holder.iv_l.setidandtype(zonemessages.get(position-1).getid(),1);
			holder.name.setText(zonemessages.get(position-1).getname());
			holder.name.setidandtype(zonemessages.get(position-1).getid(),1);
			holder.text.setText(zonemessages.get(position-1).gettext());
			
			holder.comment.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,ZoneCommentActivity.class);
					//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
					intent.putExtra("icon",(Bitmap)zonemessages.get(position-1).getimg());
					intent.putExtra("id", zonemessages.get(position-1).getid());
					intent.putExtra("name", zonemessages.get(position-1).getname());
					intent.putExtra("text", zonemessages.get(position-1).gettext());
					intent.putExtra("time", zonemessages.get(position-1).gettime());
					intent.putExtra("messageId", zonemessages.get(position-1).getMessageID());
					
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					 context.startActivity(intent);					
					
				} 
			});
			holder.re.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,ZoneCommentActivity.class);
					//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
					intent.putExtra("icon",(Bitmap)zonemessages.get(position-1).getimg());
					intent.putExtra("id", zonemessages.get(position-1).getid());
					intent.putExtra("name", zonemessages.get(position-1).getname());
					intent.putExtra("text", zonemessages.get(position-1).gettext());
					intent.putExtra("time", zonemessages.get(position-1).gettime());
					intent.putExtra("messageId", zonemessages.get(position-1).getMessageID());
					
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					 context.startActivity(intent);					
					
				} 
			});
			holder.good.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int LikeState;
					if(!zonemessages.get(position-1).getIsLiked())
					{
						LikeState = 1;
					}
					else
					{
						LikeState = 2;
					}
					
					  
	            	
	            	try {
	        			
	        			RequestParams params = new RequestParams();
	        			
	        			params.put("messageId", String.valueOf(zonemessages.get(position-1).getMessageID()));
	        			params.put("uname", context.getSharedPreferences("user",0).getString("username", ""));
	        			//params.put("friends", Friends);
	        			//params.put("startTime", sdate);
	        			//params.put("text", text.getText().toString());
	        			//params.put("atPlace", atPlace.getText().toString());
	        			params.put("requesttype", "5");
	        			params.put("LikeState", String.valueOf(LikeState));
	        			//params.put("addtype", "3");
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
	        							if(zonemessages.get(position-1).getIsLiked())
	        							{
	        								holder.good.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like));
	        								zonemessages.get(position-1).setIsLiked(false);
	        								holder.num.setText(zonemessages.get(position-1).getnum()-1+"个赞");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()-1);
	        							}
	        							else
	        							{
	        								holder.good.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
	        								zonemessages.get(position-1).setIsLiked(true);
	        								holder.num.setText(zonemessages.get(position-1).getnum()+1+"个赞");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()+1);
	        							}
	        							//Zonelistadapter.this.notifyDataSetChanged();
	        							
	        						} else {
	        							Toast.makeText(context,
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
	        					Toast.makeText(context,
	        							"网络访问异常，错误码  > " + statusCode, 0).show();

	        				}
	        			});

	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	  
	            
					
				} 
			});
			break;
		case 2:
			//holder2=new ViewHolder2();

			convertView=LayoutInflater.from(context).inflate(R.layout.familyzone_item_sch, null);
		/*	all=(RelativeLayout)convertView.findViewById(R.id.all);
			FontManager.changeFonts(all, context);*/
			//Zonemessage message = zonemessages.get(position);
			/*holder2.name=(TextViewM)convertView.findViewById(R.id.familyzone_feed_authorname);
			holder2.text=(TextView)convertView.findViewById(R.id.familyzone_sch_main);
			holder2.iv_l=(RoundImageView)convertView.findViewById(R.id.familyzone_sch_head);
			holder2.num=(TextView)convertView.findViewById(R.id.num);
			holder2.iwant=(ImageView)convertView.findViewById(R.id.zan);
			
			holder2.atplace=(TextView)convertView.findViewById(R.id.atplace);
			
			holder2.attime=(TextView)convertView.findViewById(R.id.attime);
			
			holder2.time=(TextView)convertView.findViewById(R.id.time);
			
			holder2.friends=(LinearLayout)convertView.findViewById(R.id.friends);

			
				
			holder2.num.setText("我也想去");
			if(zonemessages.get(position-1).getIsLiked())
			{
				holder2.iwant.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
				holder2.num.setText("我不想去了");
			}			
			holder2.atplace.setText("目的地："+zonemessages.get(position-1).getatplace());
			
			holder2.attime.setText("出行时间："+zonemessages.get(position-1).getattime());
			
			holder2.time.setText(zonemessages.get(position-1).gettime());
			
			initfriends(holder2.friends,zonemessages.get(position-1).getfriends());/////动态添加好友姓名
			
			
			
			
			holder2.iv_l.setImageBitmap((Bitmap)zonemessages.get(position-1).getimg());
			holder2.iv_l.setidandtype(zonemessages.get(position-1).getid(),1);
			holder2.name.setText(zonemessages.get(position-1).getname());
			holder2.name.setidandtype(zonemessages.get(position-1).getid(),1);
			holder2.text.setText(zonemessages.get(position-1).gettext());
			holder2.iwant.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					int LikeState;
					if(!zonemessages.get(position-1).getIsLiked())
					{
						LikeState = 1;
					}
					else
					{
						LikeState = 2;
					}
					
					  
	            	
	            	try {
	        			
	        			RequestParams params = new RequestParams();
	        			
	        			params.put("messageId", String.valueOf(zonemessages.get(position-1).getMessageID()));
	        			params.put("uname", context.getSharedPreferences("user",0).getString("username", ""));
	        			//params.put("friends", Friends);
	        			//params.put("startTime", sdate);
	        			//params.put("text", text.getText().toString());
	        			//params.put("atPlace", atPlace.getText().toString());
	        			params.put("requesttype", "5");
	        			params.put("LikeState", String.valueOf(LikeState));
	        			//params.put("addtype", "3");
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
	        							if(zonemessages.get(position-1).getIsLiked())
	        							{
	        								holder2.iwant.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like));
	        								zonemessages.get(position-1).setIsLiked(false);
	        								holder2.num.setText("我也想去");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()-1);
	        								List<HeadM> fri = zonemessages.get(position-1).getfriends();
	        								List<HeadM> temp = new ArrayList<HeadM>();
	        								for(HeadM hm:fri)
	        								{
	        									if( hm.getid()==Long.parseLong(context.getSharedPreferences("user",0).getString("username", "")))
	        										temp.add(hm);
	        								}
	        								fri.removeAll(temp);
	        								zonemessages.get(position-1).setfriends(fri);
	        								holder2.friends.removeAllViews();
	        								initfriends(holder2.friends,zonemessages.get(position-1).getfriends());
	        								
	        							}
	        							else
	        							{
	        								holder2.iwant.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
	        								zonemessages.get(position-1).setIsLiked(true);
	        								holder2.num.setText("我不想去了");
	        								zonemessages.get(position-1).setnum(zonemessages.get(position-1).getnum()+1);
	        								List<HeadM> fri = zonemessages.get(position-1).getfriends();
	        								fri.add(new HeadM(Long.parseLong(context.getSharedPreferences("user",0).getString("username", "")),PushApplication.getInstance().getUserInfo().get("name")));
	        								zonemessages.get(position-1).setfriends(fri);
	        								holder2.friends.removeAllViews();
	        								initfriends(holder2.friends,zonemessages.get(position-1).getfriends());
	        							}
	        							//Zonelistadapter.this.notifyDataSetChanged();
	        							
	        						} else {
	        							Toast.makeText(context,
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
	        					Toast.makeText(context,
	        							"网络访问异常，错误码  > " + statusCode, 0).show();

	        				}
	        			});

	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	  
	            
					
				
				} 
			});

					
			
			break;
			default: break;}

		
		}*/
		
	}
	
	private void initfriends(LinearLayout ll,List <HeadM> friends)
	{
		for(int i=0;i<friends.size();i++)
		{
			TextViewM tv=new TextViewM(context);
			tv.setSingleLine(true);
			tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tv.setText(friends.get(i).getname());
			tv.setidandtype(friends.get(i).getid(), 1);
			ll.addView(tv);		
		}
		
	}
		
	static class ViewHolder {
		TextView text,comnum,num,time;
		TextViewM name;
		ImageView hisimg;
		RoundImageView iv_l;
		ImageView comment,good;
		RelativeLayout re;
		
		}
	static class ViewHolder2 {
		TextView text,num,time,attime,atplace;
		TextViewM name;
		RoundImageView iv_l;
		ImageView iwant;
		LinearLayout  friends;
		
		}
	static class ViewHolder3
	{
		LinearLayout ll_bg;
		GridView gv;
	}
	
	public void setBG()
	{
		File myCaptureFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"IFamily",groupId+2);
		if (myCaptureFile.exists())
			LayoutInflater.from(context).inflate(R.layout.familyzoneheads, null).findViewById(R.id.background).setBackground(new BitmapDrawable(decodeFile(myCaptureFile)));
		else
			LayoutInflater.from(context).inflate(R.layout.familyzoneheads, null).findViewById(R.id.background).setBackgroundResource((Integer)bg);
	}
	
	public void setBg(File Bg)
	{
		LayoutInflater.from(context).inflate(R.layout.familyzoneheads, null).findViewById(R.id.background).setBackground(new BitmapDrawable(decodeFile(Bg)));
	}
	
	@SuppressLint("NewApi")
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
