package com.example.ifamily.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.adapter.ChatMessageAdapter;
import com.example.ifamily.bean.ChatMessage;
import com.example.ifamily.bean.Message;
import com.example.ifamily.bean.User;
import com.example.ifamily.client.PushMessageReceiver;
import com.example.ifamily.client.PushMessageReceiver.onNewMessageListener;
import com.example.ifamily.dao.UserDB;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.NetUtil;
import com.example.ifamily.utils.SendMsgAsyncTask;
import com.example.ifamily.utils.SharePreferenceUtil;
import com.example.ifamily.utils.T;

public class ChatChatActivity extends Activity implements onNewMessageListener
{

	//private TextView mNickName;
	private EditText mMsgInput;
	private Button mMsgSend;
	private TextView title;
	private Button back;
	private ListView mChatMessagesListView;
	private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();
	private ChatMessageAdapter mAdapter;
	private PushApplication mApplication;

	private Button ll4;
	
	
	private User mFromUser;
	private UserDB mUserDB;
	private Gson mGson;
	private SharePreferenceUtil mSpUtil;
	  private ImageView tishi;
	  boolean isFirstIn = false;
	  private TextView iknow;
	  private RelativeLayout reee;	
	private String groupId,groupName;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_chatting);

		Intent intent = getIntent();
		groupId = intent.getStringExtra("groupId");
		groupName=intent.getStringExtra("groupNick");
		initView();
		initEvent();

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initView()
	{
		
		 tishi=(ImageView)findViewById(R.id.tishi);
		 iknow=(TextView)findViewById(R.id.iknow);
		 reee=(RelativeLayout)findViewById(R.id.reee);

		 
		  SharedPreferences sps = ChatChatActivity.this.getSharedPreferences("SP", Context.MODE_PRIVATE);
		  
		  if(sps.getBoolean("ischatingfir", false)!=true){		  		  
		  Editor editor = sps.edit();
		  editor.putBoolean("ischatingfir",true);
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
		 
		
		
		

		ll4=(Button)findViewById(R.id.btnatme);
		
		ll4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it=new Intent(ChatChatActivity.this,GroupmessageActivity.class);
				it.putExtra("groupID", groupId);
				startActivity(it);
				ChatChatActivity.this.finish();
			}
			
		});
		
		RelativeLayout all=(RelativeLayout)findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this);
		mChatMessagesListView = (ListView) findViewById(R.id.id_chat_listView);
		File bg = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"IFamily",groupId+1);
		if(bg.exists())
		{
			mChatMessagesListView.setBackground(new BitmapDrawable(decodeFile(bg)));
		}
		mMsgInput = (EditText) findViewById(R.id.id_chat_msg);
		mMsgSend = (Button) findViewById(R.id.id_chat_send);
		//mNickName = (TextView) findViewById(R.id.id_nickname);

		mApplication = (PushApplication) getApplication();
		mUserDB = mApplication.getUserDB();
		mGson = mApplication.getGson();
		mSpUtil = mApplication.getSpUtil();

		
		//Log.e("TAG", groupId);

		if (TextUtils.isEmpty(groupId))
		{
			finish();
		}

		//mFromUser = mUserDB.getUser(userId);
		//未读消息更新为已经读取
		mApplication.getMessageDB().updateReaded(groupId);
		
		//Log.e("TAG", mFromUser.toString());

		//mNickName.setText("test");
		// 获取10条聊天记录
		
		
		title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText(groupName);
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				ChatChatActivity.this.finish();
			}
			 
		 });
		mDatas = mApplication.getMessageDB().find(groupId, 1, 10);
		mAdapter = new ChatMessageAdapter(this, mDatas);
		mChatMessagesListView.setAdapter(mAdapter);
		mChatMessagesListView.setSelection(mDatas.size() - 1);

		PushMessageReceiver.msgListeners.add(this);
	}

	private void initEvent()
	{
		mMsgSend.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String msg = mMsgInput.getText().toString();
				if (TextUtils.isEmpty(msg))
				{
					T.showShort(mApplication, "您还未填写消息呢!");
					return;
				}

				if (!NetUtil.isNetConnected(mApplication))
				{
					T.showShort(mApplication, "当前无网络连接！");
					return;
				}
				Message message = new Message(System.currentTimeMillis(), msg,groupId);
				message.setNickname((String)PushApplication.getInstance().getUserInfo().get("name"));
				message.setHeadIcon((String)PushApplication.getInstance().getUserInfo().get("photopath"));
				Log.e("chaojizhuoge", (String)PushApplication.getInstance().getUserInfo().get("name"));
				new SendMsgAsyncTask(mGson.toJson(message), groupId,""
						).send();

				ChatMessage chatMessage = new ChatMessage();
				chatMessage.setComing(false);
				chatMessage.setDate(new Date());
				chatMessage.setMessage(msg);
				chatMessage.setNickname((String)PushApplication.getInstance().getUserInfo().get("name"));
				chatMessage.setUserId(getSharedPreferences("user",0).getString("username", ""));
				chatMessage.setGroupId(Integer.parseInt(groupId));
				chatMessage.setIcon((Bitmap)PushApplication.getInstance().getUserInfo().get("photo"));
				//消息存入数据库
				mApplication.getMessageDB().add(groupId, chatMessage);
				
				mDatas.add(chatMessage);
				mAdapter.notifyDataSetChanged();
				mChatMessagesListView.setSelection(mDatas.size() - 1);
				mMsgInput.setText("");

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				// 得到InputMethodManager的实例
				if (imm.isActive())
				{
					// 如果开启
					imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
							InputMethodManager.HIDE_NOT_ALWAYS);
					// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
				}

			}
		});
	}

	@Override
	protected void onDestroy()
	{
		PushMessageReceiver.msgListeners.remove(this);
		super.onDestroy();

	}

	@Override
	public void onNewMessage(Message message)
	{
		Log.e("TAG", "getMsg in chatActivity" + message.getNickname());

		// 获得回复的消息
		if (groupId.equals(String.valueOf(message.getGroupID())))
		{
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setComing(true);
			chatMessage.setDate(new Date(message.getTimeSamp()));
			chatMessage.setIcon((Bitmap)message.getHeadIcon());
			chatMessage.setMessage(message.getMessage());
			chatMessage.setUserId(message.getUserId());
			chatMessage.setNickname(message.getNickname());
			chatMessage.setReaded(true);
			chatMessage.setGroupId(Integer.parseInt(groupId));
			mDatas.add(chatMessage);
			mAdapter.notifyDataSetChanged();
			mChatMessagesListView.setSelection(mDatas.size() - 1);
			// 存入数据库，当前聊天记录
			mApplication.getMessageDB().add(groupId, chatMessage);
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
