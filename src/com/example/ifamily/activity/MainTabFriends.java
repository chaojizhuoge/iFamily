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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.jauker.widget.BadgeView;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.bean.ChatMessage;
import com.example.ifamily.bean.Group;
import com.example.ifamily.bean.Message;
import com.example.ifamily.bean.User;
import com.example.ifamily.client.PushMessageReceiver;
import com.example.ifamily.client.PushMessageReceiver.onNewFriendListener;
import com.example.ifamily.client.PushMessageReceiver.onNewMessageListener;
import com.example.ifamily.mywidget.AutoListView;
import com.example.ifamily.mywidget.AutoListView.OnRefreshListener;
import com.example.ifamily.server.BaiduPush;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.MyHttpRequest;
import com.example.ifamily.utils.SharePreferenceUtil;
import com.example.ifamily.utils.T;
import com.example.ifamily.utils.TimeUtil;

/**
 * 朋友列表界面
 * 
 * @author zhy
 * 
 */
public class MainTabFriends extends Fragment implements onNewFriendListener,
		onNewMessageListener, OnRefreshListener
{

	public static final String TAG = MainTabFriends.class.getSimpleName();

	/**
	 * 提供未读消息更新的回调，比如来了一个新消息或者用户点击查看某个用户的消息
	 * 
	 * @author zhy
	 * 
	 */
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
	private FriendsListAdapter mAdapter;

	private PushApplication mApplication;

	private LayoutInflater mInflater;
	private SharePreferenceUtil mSpUtils;
	
	private SharedPreferences sp;
	private List<Map<String,Object>> list;

	private File tempfile;
	private FileCache fileCache;
	
	private BaiduPush mBaiduPush;
	
	private List<String> groupIDs = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");

		//mData.add(new Group(12,"asdf","sadf",12));
		onRefresh();
		fileCache = new FileCache(this.getActivity().getApplicationContext());

		mInflater = LayoutInflater.from(getActivity());
		mApplication = (PushApplication) this.getActivity().getApplication();
		mAdapter = new FriendsListAdapter();
		mDatas = mApplication.getUserDB().getUser();
		mSpUtils = PushApplication.getInstance().getSpUtil();
		mBaiduPush = PushApplication.getInstance().getBaiduPush();
		
		

	}

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater
				.inflate(R.layout.main_tab_weixin, container, false);
		mFrineds = (ListView) view.findViewById(R.id.id_listview_friends);
		mEmptyView = inflater.inflate(R.layout.no_zuo_no_die, container, false);
		mFrineds.setEmptyView(mEmptyView);
		mFrineds.setAdapter(mAdapter);

		notifyUnReadedMsg();

		mFrineds.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				String groupId = String.valueOf(mData.get(position).getGroupId());
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
					mAdapter.notifyDataSetChanged();
					notifyUnReadedMsg();

				}

				Intent intent = new Intent(getActivity(),
						ChatChatActivity.class);
				intent.putExtra("groupId", String.valueOf(mData.get(position).getGroupId()));
				startActivity(intent);
			}

		});
		return view;
	}

	/**
	 * 回调未读消息个数
	 */
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
		Log.e(TAG, "onResume");
		// 回调未读消息个数的更新
		notifyUnReadedMsg();
		// 设置新朋友的监听
		PushMessageReceiver.friendListeners.add(this);
		// 设置新消息的监听
		PushMessageReceiver.msgListeners.add(this);

		if (!PushManager.isPushEnabled(getActivity()))
			PushManager.resumeWork(getActivity());
		// 更新用户列表
		mDatas = mApplication.getUserDB().getUser();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.e(TAG, "onPause");
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
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 监听新朋友到来的通知
	 */
	@Override
	public void onNewFriend(User u)
	{
		Log.e(TAG, "get a new friend :" + u.getUserId() + " , " + u.getNick());
		mDatas.add(u);
		mAdapter.notifyDataSetChanged();
	}

	private class FriendsListAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return mData.size();
		}

		@Override
		public Object getItem(int position)
		{
			return mData.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			Group group = mData.get(position);
			String groupId = String.valueOf(group.getGroupId());

			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = mInflater.inflate(
						R.layout.main_tab_weixin_info_item, parent, false);
				holder = new ViewHolder();
				holder.mNickname = (TextView) convertView
						.findViewById(R.id.id_nickname);
				holder.mUserId = (TextView) convertView
						.findViewById(R.id.id_userId);
				holder.mWapper = (RelativeLayout) convertView
						.findViewById(R.id.id_item_ly);
				holder.mIcon = (ImageView) convertView.findViewById(R.id.header_icon);
				convertView.setTag(holder);
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			// 如果存在新的消息，则设置BadgeView
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
				if (holder.mBadgeView != null)
					holder.mBadgeView.setVisibility(View.GONE);
			}

			holder.mNickname.setText(mData.get(position).getNick());
			holder.mUserId.setText(groupId);
			holder.mIcon.setImageBitmap((Bitmap) mData.get(position).getHeadIcon());
			return convertView;
		}

		private final class ViewHolder
		{
			TextView mNickname;
			TextView mUserId;
			RelativeLayout mWapper;
			BadgeView mBadgeView;
			ImageView mIcon;
		}

	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		Thread thread = new Thread(new LoginThread());
		thread.start();
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

    			Toast.makeText(MainTabFriends.this.getActivity().getApplicationContext(), "刷新成功！", Toast.LENGTH_SHORT).show();
    			//Intent intent = new Intent();
    			//intent.setClass(LoginActivity.this, MainActivity.class);
    			//tartActivity(intent);
    			//finish();
    			
    			getData(list);
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
    			Toast.makeText(MainTabFriends.this.getActivity().getApplicationContext(), "URL验证失败", Toast.LENGTH_SHORT).show();
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
    		set.add(String.valueOf(groupID));
    		setTagThread st = new setTagThread();
    		st.setTag(String.valueOf(groupID));
    		new Thread(st).start();
    		
    	}
    	groupIDs.addAll(set);
    	mAdapter.notifyDataSetChanged();
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
    
}
