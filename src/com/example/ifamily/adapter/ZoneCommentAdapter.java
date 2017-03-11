package com.example.ifamily.adapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.activity.AddPicActivity;
import com.example.ifamily.activity.FamilyzoneActivity;
import com.example.ifamily.activity.MyPicActivity;
import com.example.ifamily.activity.ZoneCommentActivity;
import com.example.ifamily.message.HeadM;
import com.example.ifamily.message.ZoneCommentHero;
import com.example.ifamily.message.ZonecommentM;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.mywidget.TextViewNameList;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ZoneCommentAdapter extends BaseAdapter{
private Context context;
private List<ZonecommentM> messages;
private ZoneCommentHero user1;
private RoundImageView hd;
private ImageView iv1,iv2;
private TextView tv2,tv3,num,comnum;
private TextViewM tv1;
private TextViewNameList tn;
private ImageView iv;
private PopupWindow popupWindow;
private View v;
public ZoneCommentAdapter(Context contexts,List<ZonecommentM> messages) {
	 super();
	 this.context = contexts;
	 this.messages =messages;
	}
public ZoneCommentAdapter(Context contexts,List<ZonecommentM> messages,ZoneCommentHero hero) {
	 super();
	 this.context = contexts;
	 this.messages =messages;
	 this.user1=hero;
	}
public void setpop(View v)
{
	this.v=v;
}
@Override
public int getCount() {
	// TODO Auto-generated method stub
	 return (messages.size()+1);
	
}

@Override
public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	return messages.get(arg0);
}

@Override
public long getItemId(int arg0) {
	// TODO Auto-generated method stub
	return arg0;
}

@Override
public View getView(final int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
	if(position==0)
	{
		convertView=LayoutInflater.from(context).inflate(R.layout.zonecommenthero, null);
		//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//FontManager.changeFonts(all, context);
		num=(TextView)convertView.findViewById(R.id.num);
		comnum=(TextView)convertView.findViewById(R.id.comnum);
		tn=(TextViewNameList)convertView.findViewById(R.id.namelist);
		hd=(RoundImageView)convertView.findViewById(R.id.head);
		iv1=(ImageView)convertView.findViewById(R.id.com);
		iv2=(ImageView)convertView.findViewById(R.id.zan);
		tv1=(TextViewM)convertView.findViewById(R.id.name);
		tv2=(TextView)convertView.findViewById(R.id.time);
		tv3=(TextView)convertView.findViewById(R.id.content);
		iv=(ImageView)convertView.findViewById(R.id.hisimg);
		iv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,MyPicActivity.class);
				//intent.putExtra("hisimg", (Bitmap)zonemessages.get(position-1).gethisimg());
				intent.putExtra("icon",(Bitmap)messages.get(position-1).getimg());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
				 context.startActivity(intent);	
			}
			
		});
		tn.setendstring("觉得赞");
		if(user1.getnames()!=null)
		tn.setnamelist(user1.getnames());
		
		
		if(user1.gethisimg()!=null){
			iv.setImageBitmap((Bitmap)user1.gethisimg());
		}
		if(user1.getIsLiked())
			iv2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
		
		
		num.setText(user1.getnum()+"个赞");
		comnum.setText(user1.getcomnum()+"条评论");
		hd.setImageBitmap((Bitmap)user1.gethead());
		hd.setidandtype(user1.getid(), 1);
		tv1.setText(user1.getname());
		tv1.setidandtype(user1.getid(), 1);
		tv2.setText(user1.gettime());
		tv3.setText(user1.getcontent());	
		
		iv1.setOnClickListener(new OnClickListener(){//点击评论按钮

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pop(v);//弹出输入框
				
			}
			
		});
		iv2.setOnClickListener(new OnClickListener(){//点击赞按钮

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int LikeState;
				if(!user1.getIsLiked())
				{
					LikeState = 1;
				}
				else
				{
					LikeState = 2;
				}
				
				  
            	
            	try {
        			
        			RequestParams params = new RequestParams();
        			
        			params.put("messageId", String.valueOf(user1.getMessageId()));
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
        							if(user1.getIsLiked())
        							{
        								iv2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like));
        								user1.setIsLiked(false);
        								num.setText(user1.getnum()-1+"个赞");
        								user1.setNum(user1.getnum()-1);
        								List<HeadM> fri = user1.getnames();
        								List<HeadM> temp = new ArrayList<HeadM>();
        								for(HeadM hm:fri)
        								{
        									if( hm.getid()==Long.parseLong(context.getSharedPreferences("user",0).getString("username", "")))
        										temp.add(hm);
        								}
        								fri.removeAll(temp);
        								user1.setNames(fri);
        								tn.setnamelist(user1.getnames());
        							}
        							else
        							{
        								iv2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.like1));
        								user1.setIsLiked(true);
        								num.setText(user1.getnum()+1+"个赞");
        								user1.setNum(user1.getnum()+1);
        								List<HeadM> fri = user1.getnames();
        								
        								fri.add(new HeadM(Long.parseLong(context.getSharedPreferences("user",0).getString("username", "")),PushApplication.getInstance().getUserInfo().get("name")));
        								user1.setNames(fri);
        								tn.setnamelist(user1.getnames());
        								
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
	}else
	{
		holder=new ViewHolder();	
		convertView=LayoutInflater.from(context).inflate(R.layout.zonecomment_item, null);
	//	LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
	//	FontManager.changeFonts(all, context);
		holder.name=(TextViewM)convertView.findViewById(R.id.name);		
		holder.iv_l=(RoundImageView)convertView.findViewById(R.id.head);
		holder.time=(TextView)convertView.findViewById(R.id.time);	
		holder.wish=(TextView)convertView.findViewById(R.id.content);	
		holder.iv_l.setImageBitmap((Bitmap)messages.get(position-1).getimg());
		holder.iv_l.setidandtype(messages.get(position-1).getid(), 1);
		holder.wish.setText(messages.get(position-1).gettext());
		holder.name.setText(messages.get(position-1).getname());
		holder.name.setidandtype(messages.get(position-1).getid(), 1);
		holder.time.setText(messages.get(position-1).gettime());}
	 return convertView;
}


static class ViewHolder {
	 TextViewM name;
	 TextView wish;
	 TextView time;
	 RoundImageView iv_l;		
	}
private void pop(View v)
{
	if (popupWindow!= null && popupWindow.isShowing()) {

		popupWindow.dismiss();

		popupWindow= null;

		}
	  final LinearLayout  layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.zoneinputcomment, null);
	  final EditText comments=(EditText)layout.findViewById(R.id.comments);
	  comments.getText();

	  Button ok=(Button)layout.findViewById(R.id.ok);
	  ok.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			popupWindow.dismiss();

			try {
				
				RequestParams params = new RequestParams();
			
				
				final String username = context.getSharedPreferences("user",0).getString("username", "");
				params.put("text", comments.getText().toString());
				params.put("uname", username);
				params.put("requesttype", "2");
				params.put("addtype", "4");
				params.put("messageId", String.valueOf(user1.getMessageId()));
				//params.put("groupId", groupID);
				
				//params.put("hasphoto", String.valueOf(hasphoto));
				String url = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";

				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						try {
							if (statusCode == 200) {
								//mDialog.dismiss();

								Toast.makeText(context, "评论成功！", Toast.LENGTH_SHORT)
										.show();
								messages.add(new ZonecommentM(Long.parseLong(username),
										PushApplication.getInstance().getUserInfo().get("photo"),
										(String)PushApplication.getInstance().getUserInfo().get("name"),
										comments.getText().toString(),
										Utils.getCurrentTime()));
								
								ZoneCommentAdapter.this.notifyDataSetChanged();
								//((ZoneCommentActivity)context).onHeaderRefresh(((ZoneCommentActivity)context).getPV());
								//Intent intent = new Intent(AddPicActivity.this,FamilyzoneActivity.class);
								//startActivity(intent);
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
        popupWindow= new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
  	    popupWindow.setFocusable(true);
  	    popupWindow.setBackgroundDrawable(new BitmapDrawable());
  	    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.popupfrombottom);
        popupWindow.showAtLocation(v,Gravity.BOTTOM, 0, 0);

}
}
