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
 * �����б����
 * 
 * @author zhy
 * 
 */
public class MainTabFriends extends Fragment implements onNewFriendListener,
		onNewMessageListener, OnRefreshListener
{

	public static final String TAG = MainTabFriends.class.getSimpleName();

	/**
	 * �ṩδ����Ϣ���µĻص�����������һ������Ϣ�����û�����鿴ĳ���û�����Ϣ
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnUnReadMessageUpdateListener
	{
		void unReadMessageUpdate(int count);
	}

	/**
	 * �洢userId-������Ϣ�ĸ���
	 */
	public Map<String, Integer> mUserMessages = new HashMap<String, Integer>();
	/**
	 * δ����Ϣ����
	 */
	private int mUnReadedMsgs;

	private ListView mFrineds;
	private View mEmptyView;
	/**
	 * ���е��û�
	 */
	private List<User> mDatas;
	private List<Group> mData = new ArrayList<Group>();
	/**
	 * ������
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
				//	T.showShort(getActivity(), "���ܺ��Լ��������");
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
	 * �ص�δ����Ϣ����
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
		// �ص�δ����Ϣ�����ĸ���
		notifyUnReadedMsg();
		// ���������ѵļ���
		PushMessageReceiver.friendListeners.add(this);
		// ��������Ϣ�ļ���
		PushMessageReceiver.msgListeners.add(this);

		if (!PushManager.isPushEnabled(getActivity()))
			PushManager.resumeWork(getActivity());
		// �����û��б�
		mDatas = mApplication.getUserDB().getUser();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.e(TAG, "onPause");
		/**
		 * ��onPauseʱ��ȡ������
		 */
		PushMessageReceiver.friendListeners.remove(this);
		PushMessageReceiver.msgListeners.remove(this);
	}

	/**
	 * �յ�����Ϣʱ
	 */
	@Override
	public void onNewMessage(Message message)
	{
		// ������Լ����͵ģ���ֱ�ӷ���
		if (message.getUserId() == mSpUtils.getUserId())
			return;
		// ������û��Ѿ���δ����Ϣ������δ����Ϣ�ĸ�������֪ͨ����δ����Ϣ�ӿڣ����notifyDataSetChanged
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
		// ����������Ϣ���д洢
		ChatMessage chatMessage = new ChatMessage(message.getMessage(), true,
				message.getUserId(), 
				message.getHeadIcon(),
				message.getNickname(), false,
				TimeUtil.getTime(message.getTimeSamp()),message.getGroupID());
		mApplication.getMessageDB().add(groupId, chatMessage);
		// ֪ͨlistview���ݸı�
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * ���������ѵ�����֪ͨ
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

			// ��������µ���Ϣ��������BadgeView
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
		// TODO �Զ����ɵķ������
		Thread thread = new Thread(new LoginThread());
		thread.start();
	}

	
	

	
	 private boolean loginServer()
	    {
	    	boolean loginValidate = false;
	    	//ʹ��apache HTTP�ͻ���ʵ��
	    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/ChatListServlet";
	    	HttpPost request = new HttpPost(urlStr);
	    	//������ݲ�����Ļ������ԶԴ��ݵĲ������з�װ
	    	sp = this.getActivity().getSharedPreferences("user",0);
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	
	    	//����û���������
	    	params.add(new BasicNameValuePair("uname",sp.getString("username", "")));
	    	//params.add(new BasicNameValuePair("times",String.valueOf(times)));
	    	//params.add(new BasicNameValuePair("lasttime",lasttime));
	    	//params.add(new BasicNameValuePair("requesttype","1"));
	    	try
	    	{
	    		
	    		
	    		
	    		//�������������
	    		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	    		HttpClient client = MyHttpRequest.getHttpClient();
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
			// TODO �Զ����ɵķ������
			PushApplication.getInstance().getBaiduPush().SetTag(tag, mSpUtils.getUserId());
		}
		 
	 }
	 
	class LoginThread implements Runnable
	    {

			

			@Override
			public void run() {
				
				
				//mDialog.dismiss();
				//URL�Ϸ���������һ��������֤�����Ƿ���ȷ*/
				
				
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

    			Toast.makeText(MainTabFriends.this.getActivity().getApplicationContext(), "ˢ�³ɹ���", Toast.LENGTH_SHORT).show();
    			//Intent intent = new Intent();
    			//intent.setClass(LoginActivity.this, MainActivity.class);
    			//tartActivity(intent);
    			//finish();
    			
    			getData(list);
    			// ��ȡ���ݿ������е��û��Լ�δ����Ϣ����
    			mUserMessages = mApplication.getMessageDB().getUserUnReadMsgs(
    					groupIDs);
    			for (Integer val : mUserMessages.values())
    			{
    				mUnReadedMsgs += val;
    			}
    			break;
    		
    		
    			
    		case 2:
    			//mDialog.cancel();
    			Toast.makeText(MainTabFriends.this.getActivity().getApplicationContext(), "URL��֤ʧ��", Toast.LENGTH_SHORT).show();
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
		// TODO �Զ����ɵķ������
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
