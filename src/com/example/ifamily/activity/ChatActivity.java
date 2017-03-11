package com.example.ifamily.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.baidu.android.pushservice.PushManager;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.activity.MainTabFriends.LoginThread;
import com.example.ifamily.activity.MainTabFriends.OnUnReadMessageUpdateListener;
import com.example.ifamily.activity.MainTabFriends.setTagThread;
import com.example.ifamily.activity.MyPicActivity.ImageViewOnTouchListener;
import com.example.ifamily.adapter.GroupLvAdapter;
//import com.example.ifamily.adapter.GroupLvAdapter.ViewHolder;
import com.example.ifamily.bean.ChatMessage;
import com.example.ifamily.bean.Group;
import com.example.ifamily.bean.Message;
import com.example.ifamily.bean.User;
import com.example.ifamily.client.PushMessageReceiver;
import com.example.ifamily.client.PushMessageReceiver.onNewMessageListener;
import com.example.ifamily.message.GroupLMessage;
import com.example.ifamily.mywidget.PullToRefreshView;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.mywidget.PullToRefreshView.OnFooterRefreshListener;
import com.example.ifamily.mywidget.PullToRefreshView.OnHeaderRefreshListener;
import com.example.ifamily.server.BaiduPush;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyHttpRequest;
import com.example.ifamily.utils.SharePreferenceUtil;
import com.example.ifamily.utils.TimeUtil;
import com.jauker.widget.BadgeView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class ChatActivity extends Fragment implements OnFooterRefreshListener,OnHeaderRefreshListener,onNewMessageListener{
	
	
		public interface OnUnReadMessageUpdateListener
		{
			void unReadMessageUpdate(int count);
		}

		/**
		 * 存储userId-新来消息的个数
		 */
		public Map<String, Integer> mUserMessages = new HashMap<String, Integer>();
		/**
		 * 未读消息总数
		 */
		private int mUnReadedMsgs;

		private String groupId;
		private ListView mFrineds;
		private View mEmptyView;
		/**
		 * 所有的用户
		 */
		private List<User> mDatas;
		private List<Group> mData = new ArrayList<Group>();
		/**
		 * 适配器
		 */
		//private FriendsListAdapter gd;

		private PushApplication mApplication;

		private LayoutInflater mInflater;
		private SharePreferenceUtil mSpUtils;
		
		private SharedPreferences sp;
		private List<Map<String,Object>> list;

		private File tempfile;
		private FileCache fileCache;
		
		private BaiduPush mBaiduPush;
		
		private List<String> groupIDs = new ArrayList<String>();
		  private ImageView tishi;
		  private TextView iknow;
		  private RelativeLayout reee;	
	
	
	
	PullToRefreshView mPullToRefreshView;
	 private GroupLvAdapter gd;
		private LinearLayout all;
	 private TextView title;
	 private Button back;
	 private List<GroupLMessage> messages = new ArrayList<GroupLMessage>();
	 private ListView glv;	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.chat_1,
				container, false);
		return messageLayout;
		
	}
	 public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
		 
		 
		 fileCache = new FileCache(this.getActivity().getApplicationContext());

			mInflater = LayoutInflater.from(getActivity());
			mApplication = (PushApplication) this.getActivity().getApplication();
			//gd = new FriendsListAdapter();
			mDatas = mApplication.getUserDB().getUser();
			mSpUtils = PushApplication.getInstance().getSpUtil();
			mBaiduPush = PushApplication.getInstance().getBaiduPush();
			gd = new GroupLvAdapter();
		 initview();
	    

	 }

	 private void initview()
	 {
		 tishi=(ImageView)getView().findViewById(R.id.tishi);
		 iknow=(TextView)getView().findViewById(R.id.iknow);
		 reee=(RelativeLayout)getView().findViewById(R.id.reee);

		  Context ctx = getActivity();       
		  SharedPreferences sps = ctx.getSharedPreferences("SP", Context.MODE_PRIVATE);
		  
		  if(sps.getBoolean("ischatfir", false)!=true){		  		  
		  Editor editor = sps.edit();
		  editor.putBoolean("ischatfir",true);
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
		 
		 
		 
			all=(LinearLayout)getView().findViewById(R.id.all);
			FontManager.changeFonts(all,this.getActivity());
		 glv = (ListView) getView().findViewById(R.id.lv_chat_grouplist);
		 setAdapterForThis();
		 title = (TextView) getView().findViewById(R.id.title);
		 back = (Button) getView().findViewById(R.id.back);
		 title.setText("聊天");
		 

		 back.setVisibility(View.GONE);	
	     glv.setOnItemClickListener(new OnItemClickListener(){  
		   	  
	        	
	            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
	            	if(arg2==0){
	            Intent	 intent = new Intent(arg1.getContext(),SysMsActivity.class);
	            
	         //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            startActivity(intent);}
	            	else{
	    				String groupId = String.valueOf(mData.get(arg2-1).getGroupId());
	    				//if (userId.equals(mSpUtils.getUserId()))
	    				//{
	    				//	T.showShort(getActivity(), "不能和自己聊天哈！");
	    				//	return;
	    				//}

	    				if (mUserMessages.containsKey(groupId))
	    				{
	    					Integer val = mUserMessages.get(groupId);
	    					mUnReadedMsgs -= val;
	    					mUserMessages.remove(groupId);
	    					gd.notifyDataSetChanged();
	    					notifyUnReadedMsg();

	    				}

	    				Intent intent = new Intent(getActivity(),
	    						ChatChatActivity.class);
	    				intent.putExtra("groupId", String.valueOf(mData.get(arg2-1).getGroupId()));
	    				intent.putExtra("groupNick", String.valueOf(mData.get(arg2-1).getNick()));
	    				startActivity(intent);
	    			}
	            	}
	             
			
	        });  
	 }
	 
	 
	 private void notifyUnReadedMsg()
		{
			if (getActivity() instanceof OnUnReadMessageUpdateListener)
			{
				OnUnReadMessageUpdateListener listener = (OnUnReadMessageUpdateListener) getActivity();
				listener.unReadMessageUpdate(mUnReadedMsgs);
			}
		}

		@Override
		public void onResume()
		{
			super.onResume();
			//Log.e(TAG, "onResume");
			// 回调未读消息个数的更新
			notifyUnReadedMsg();
			// 设置新朋友的监听
			//PushMessageReceiver.friendListeners.add(this);
			// 设置新消息的监听
			PushMessageReceiver.msgListeners.add(this);

			if (!PushManager.isPushEnabled(getActivity()))
				PushManager.resumeWork(getActivity());
			// 更新用户列表
			mDatas = mApplication.getUserDB().getUser();
			gd.notifyDataSetChanged();
		}

		@Override
		public void onPause()
		{
			super.onPause();
			//Log.e(TAG, "onPause");
			/**
			 * 当onPause时，取消监听
			 */
			PushMessageReceiver.friendListeners.remove(this);
			PushMessageReceiver.msgListeners.remove(this);
		}

		/**
		 * 收到新消息时
		 */
		@Override
		public void onNewMessage(Message message)
		{
			// 如果是自己发送的，则直接返回
			if (message.getUserId() == mSpUtils.getUserId())
				return;
			// 如果该用户已经有未读消息，更新未读消息的个数，并通知更新未读消息接口，最后notifyDataSetChanged
			String groupId = String.valueOf(message.getGroupID());
			if (mUserMessages.containsKey(groupId))
			{
				mUserMessages.put(groupId, mUserMessages.get(groupId) + 1);
			} else
			{
				mUserMessages.put(groupId, 1);
			}
			mUnReadedMsgs++;
			notifyUnReadedMsg();
			// 将新来的消息进行存储
			ChatMessage chatMessage = new ChatMessage(message.getMessage(), true,
					message.getUserId(), 
					message.getHeadIcon(),
					message.getNickname(), false,
					TimeUtil.getTime(message.getTimeSamp()),message.getGroupID());
			mApplication.getMessageDB().add(groupId, chatMessage);
			// 通知listview数据改变
			//mApplication.getMessageDB().find(groupId, 1, 1)
			for(GroupLMessage gl : messages)
			{
				if(gl.getid()==message.getGroupID()){
				gl.settext(mApplication.getMessageDB().find(String.valueOf(groupId), 1, 1).get(0).getNickname()+"："+mApplication.getMessageDB().find(groupId, 1, 1).get(0).getMessage());
				gl.settime(mApplication.getMessageDB().find(groupId, 1, 1).get(0).getDateStr());
				break;
				}
			}
			gd.notifyDataSetChanged();
		}



	 private void setAdapterForThis() {
		  //initMessages();
		  
		 mPullToRefreshView = (PullToRefreshView) getView().findViewById(R.id.test_Grid);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		gd = new GroupLvAdapter(ChatActivity.this.getActivity(), messages);
		 glv.setAdapter(gd);
		 
		 mPullToRefreshView.headerRefreshing();
		 
		 
		 
		 
		 }
	 
	 public class GroupLvAdapter extends BaseAdapter{
			private Context context;
			private List<GroupLMessage> Groupmessages;
			public GroupLvAdapter(Context contexts,List<GroupLMessage> messages) {
				 super();
				 this.context = contexts;
				 this.Groupmessages =messages;
				}
			
			public GroupLvAdapter() {
				// TODO 自动生成的构造函数存根
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				 return (Groupmessages.size()+1);
				
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return Groupmessages.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				
				if(position==0)
				{
					convertView=LayoutInflater.from(context).inflate(R.layout.group_item_sys, null);			
					int i=0;
					i=i+1;
					//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
				//	FontManager.changeFonts(all, context);
				}else
				{
					if(!Groupmessages.isEmpty())
					groupId = String.valueOf(Groupmessages.get(position-1).getid());
					holder=new ViewHolder();
					convertView=LayoutInflater.from(context).inflate(R.layout.group_item, null);
					//Zonemessage message = zonemessages.get(position);
					holder.name=(TextView)convertView.findViewById(R.id.tv_g_groupname);
					holder.text=(TextView)convertView.findViewById(R.id.tv_g_content);
					holder.iv_l=(RoundImageView)convertView.findViewById(R.id.gi_groupimg);
					holder.time=(TextView)convertView.findViewById(R.id.tv_g_time);
					holder.mWapper = (RelativeLayout)convertView.findViewById(R.id.re_g_ups);
					holder.mBadgeView = new BadgeView(mApplication);
					convertView.setTag(holder);

				
				//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
				//FontManager.changeFonts(all, context);
				Bitmap bitmap = (Bitmap)Groupmessages.get(position-1).getimg();
					holder.iv_l.setImageBitmap(bitmap);
					holder.name.setText(Groupmessages.get(position-1).getname());
					holder.text.setText(Groupmessages.get(position-1).gettext());
					holder.time.setText(Groupmessages.get(position-1).gettime());
					
					if (mUserMessages.containsKey(groupId))
					{
						if (holder.mBadgeView == null)
							holder.mBadgeView = new BadgeView(mApplication);
						holder.mBadgeView.setTargetView(holder.mWapper);
						holder.mBadgeView.setBadgeGravity(Gravity.CENTER_VERTICAL
								| Gravity.RIGHT);
						holder.mBadgeView.setBadgeMargin(0, 0, 8, 0);
						holder.mBadgeView.setBadgeCount(mUserMessages.get(groupId));
					} else
					{
						if (holder.mBadgeView!=null)
							holder.mBadgeView.setVisibility(View.GONE);
					}
				}
				
				
				
				 return convertView;
			}
			
			
			
			private final class ViewHolder {
				TextView name;
				TextView text;
				TextView time;
				RoundImageView iv_l;
				BadgeView mBadgeView;
				RelativeLayout mWapper;
				}

		}
	 

	 
	 private boolean loginServer()
	    {
	    	boolean loginValidate = false;
	    	//使用apache HTTP客户端实现
	    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/ChatListServlet";
	    	HttpPost request = new HttpPost(urlStr);
	    	//如果传递参数多的话，可以对传递的参数进行封装
	    	sp = this.getActivity().getSharedPreferences("user",0);
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	
	    	//添加用户名和密码
	    	params.add(new BasicNameValuePair("uname",sp.getString("username", "")));
	    	//params.add(new BasicNameValuePair("times",String.valueOf(times)));
	    	//params.add(new BasicNameValuePair("lasttime",lasttime));
	    	//params.add(new BasicNameValuePair("requesttype","1"));
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
	    			//responseMsg = EntityUtils.toString(response.getEntity());
	    			InputStream is = response.getEntity().getContent();
	    			ObjectInputStream ois = new ObjectInputStream(is);
	    			
	    			list = (List<Map<String,Object>>)ois.readObject();
	    			if(!list.isEmpty()){
	    			for(Map<String,Object> map : list)
	    			{
	    				byte[] photo = (byte[])map.get("groupPhoto");
	    				Log.v("buffer", photo.toString());
	    				tempfile = fileCache.getFile(photo.toString().substring(1, 5)+".jpg");
	    				tempfile.createNewFile();
	    				byte2File(photo,tempfile);
	    				Bitmap bitmap = decodeFile(tempfile);
	    				map.put("groupPhoto", bitmap);
	    			}
	    			ois.close();
	    			is.close();
	    			fileCache.clear();
	    			}
	    			else{
	    				Toast.makeText(ChatActivity.this.getActivity(), "您还没加入任何家庭，在个人中心可以创建或加入家庭哦~", Toast.LENGTH_LONG).show();

	    			}
	    		}
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    	return loginValidate;
	    }
	
	 class setTagThread implements Runnable
	 {
		 private String tag;
		 public void setTag(String tag)
		 {
			 this.tag = tag;
		 }

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			PushApplication.getInstance().getBaiduPush().SetTag(tag, mSpUtils.getUserId());
		}
		 
	 }
	 
	class LoginThread implements Runnable
	    {

			

			@Override
			public void run() {
				
				
				//mDialog.dismiss();
				//URL合法，但是这一步并不验证密码是否正确*/
				
				
		    	boolean loginValidate = loginServer();
		    	//System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+list.get(0).get("text"));
		    	android.os.Message msg = handler.obtainMessage();
		    	if(loginValidate)
		    	{
		    		
		    			msg.what = 0;
			    		handler.sendMessage(msg);
		    		
		    		
		    		
		    	}else
		    	{
		    		msg.what = 2;
		    		handler.sendMessage(msg);
		    	}
			}
	    	
	    }
	
	Handler handler = new Handler()
 {
 	public void handleMessage(android.os.Message msg)
 	{
 		switch(msg.what)
 		{
 		case 0:
 			//mDialog.cancel();

 			Toast.makeText(ChatActivity.this.getActivity().getApplicationContext(), "刷新成功！", Toast.LENGTH_SHORT).show();
 			//Intent intent = new Intent();
 			//intent.setClass(LoginActivity.this, MainActivity.class);
 			//tartActivity(intent);
 			//finish();
 			
 			getData(list);
 			
 			gd = new GroupLvAdapter(ChatActivity.this.getActivity(), messages);
 			 glv.setAdapter(gd);
 			// 获取数据库中所有的用户以及未读消息个数
 			mUserMessages = mApplication.getMessageDB().getUserUnReadMsgs(
 					groupIDs);
 			for (Integer val : mUserMessages.values())
 			{
 				mUnReadedMsgs += val;
 			}
 			break;
 		
 		
 			
 		case 2:
 			//mDialog.cancel();
 			//Toast.makeText(ChatActivity.this.getActivity().getApplicationContext(), "URL验证失败", Toast.LENGTH_SHORT).show();
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
 
 
 protected void getData(List<Map<String, Object>> list2) {
		// TODO 自动生成的方法存根
 	Set<String> set = new HashSet<String>();
 	mData.clear();
 	messages.clear();
 	for(Map<String,Object> map : list2)
 	{
 		int groupID = (Integer)map.get("groupID");
 		String groupName = (String)map.get("groupName");
 		Bitmap groupPhoto = (Bitmap)map.get("groupPhoto");
 		Group group = new Group();
 		group.setGroupId(groupID);
 		group.setNick(groupName);
 		group.setHeadIcon(groupPhoto);
 		mData.add(group);
 		String text = "";
 		String time = "";
 		if(!mApplication.getMessageDB().find(String.valueOf(groupID), 1, 1).isEmpty())
 		{
 			text = mApplication.getMessageDB().find(String.valueOf(groupID), 1, 1).get(0).getNickname()+"："+mApplication.getMessageDB().find(String.valueOf(groupID), 1, 1).get(0).getMessage();
 			time = mApplication.getMessageDB().find(String.valueOf(groupID), 1, 1).get(0).getDateStr();
 		}
 			
 		messages.add(new GroupLMessage(groupID,groupPhoto,groupName,text,time));
 		set.add(String.valueOf(groupID));
 		setTagThread st = new setTagThread();
 		st.setTag(String.valueOf(groupID));
 		new Thread(st).start();
 		
 	}
 	PushApplication.getInstance().setGroupIds(set);
 	groupIDs.addAll(set);
 	gd.notifyDataSetChanged();
		mSpUtils.setGroupId(set);
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
	 
	 
	 
	 
		  
	 private void sendMessage(String sendStr) {

		  }
	 public void onFooterRefresh(PullToRefreshView view) {
			mPullToRefreshView.postDelayed(new Runnable() {

				@Override
				public void run() {
					System.out.println("娑擄拷锟斤拷锟斤拷锟斤拷鏉烇拷");
				
					
					mPullToRefreshView.onFooterRefreshComplete();
				}
			}, 1000);
		}

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			mPullToRefreshView.postDelayed(new Runnable() {

				@Override
				public void run() {
	
					Thread thread = new Thread(new LoginThread());
					thread.start();
	
					mPullToRefreshView.onHeaderRefreshComplete();
				}
			}, 1000);

		}

		






		 
	 





}
