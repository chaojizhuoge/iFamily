package com.example.ifamily.client;

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
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.example.ifamily.activity.ChatChatActivity;
import com.example.ifamily.activity.Iguide;
import com.example.ifamily.activity.PasswordFActivity;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.bean.ChatMessage;
import com.example.ifamily.bean.Message;
import com.example.ifamily.bean.User;
import com.example.ifamily.dao.UserDB;
import com.example.ifamily.utils.FileCache;
import com.example.ifamily.utils.MyHttpRequest;
import com.example.ifamily.utils.NetUtil;
import com.example.ifamily.utils.PreUtils;
import com.example.ifamily.utils.SendMsgAsyncTask;
import com.example.ifamily.utils.SharePreferenceUtil;
import com.example.ifamily.utils.T;
import com.example.ifamily.utils.TimeUtil;

public class PushMessageReceiver extends FrontiaPushMessageReceiver
{
	public static final int NOTIFY_ID = 0x000;

	public static int mNewNum = 0;// ֪ͨ������Ϣ��Ŀ����ֻ������һ��ȫ�ֱ�����

	public static final String TAG = PushMessageReceiver.class.getSimpleName();

	public static ArrayList<onNewMessageListener> msgListeners = new ArrayList<onNewMessageListener>();
	public static ArrayList<onNewFriendListener> friendListeners = new ArrayList<onNewFriendListener>();
	public static ArrayList<onNetChangeListener> netListeners = new ArrayList<onNetChangeListener>();
	public static ArrayList<onBindListener> bindListeners = new ArrayList<onBindListener>();

	private Message receivedMsg;
	private Map<String,Object> map = new HashMap<String,Object>();
	
	public static interface onNewMessageListener
	{
		public abstract void onNewMessage(Message message);
	}

	public static interface onNewFriendListener
	{
		public abstract void onNewFriend(User u);
	}

	public static interface onNetChangeListener
	{
		public abstract void onNetChange(boolean isNetConnected);
	}

	public static interface onBindListener
	{
		public abstract void onBind(String userId, int errorCode);
	}

	@Override
	public void onBind(final Context context, int errorCode, String appid,
			String userId, String channelId, String requestId)
	{
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		Log.e(TAG, responseString);

		if (errorCode == 0)
		{
			SharePreferenceUtil util = PushApplication.getInstance()
					.getSpUtil();
			util.setAppId(appid);
			util.setChannelId(channelId);
			util.setUserId(userId);
			util.setTag("��Ů");
		} else
		// �������������������
		{
			if (NetUtil.isNetConnected(context))
			{

				T.showLong(context, "����ʧ�ܣ���������...");
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						PushManager.startWork(context,
								PushConstants.LOGIN_TYPE_API_KEY,
								PushApplication.API_KEY);
					}
				}, 2000);// ��������¿�ʼ��֤
			} else
			{
				T.showLong(context, R.string.net_error_tip);
			}
		}
		// �ص�����
		for (int i = 0; i < bindListeners.size(); i++)
			bindListeners.get(i).onBind(userId, errorCode);
	}

	private void showNotify(Message message)
	{
		mNewNum++;
		// ����֪ͨ��
		PushApplication application = PushApplication.getInstance();

		int icon = R.drawable.icon;
		CharSequence tickerText = message.getNickname() + ":"
				+ message.getMessage();
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;  //֪ͨ�������,���Զ���ʧ  
		notification.defaults |= Notification.DEFAULT_SOUND;  //֪ͨ����ʱ����Ĭ������  
	    notification.defaults |= Notification.DEFAULT_VIBRATE;
		// ����Ĭ������
		// notification.defaults |= Notification.DEFAULT_SOUND;
		// �趨��(���VIBRATEȨ��)
		
		notification.contentView = null;

		Intent intent = new Intent(application, ChatChatActivity.class);
		intent.putExtra("groupId", message.getGroupID());
		// �����֪ͨʱ��������ԭ�е�Activity���٣�����ʵ����һ��
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(application, 0,
				intent, 0);
		notification.setLatestEventInfo(PushApplication.getInstance(),
				message.getNickname() + " (" + mNewNum + "������Ϣ)",
				tickerText, contentIntent);
		
		application.getNotificationManager().notify(NOTIFY_ID, notification);// ֪ͨһ�²Ż���ЧŶ
		
	}

	@Override
	public void onMessage(Context context, String message,
			String customContentString)
	{
		String messageString = "�յ���Ϣ message=\"" + message
				+ "\" customContentString=" + customContentString;
		Log.e(TAG, messageString);
		receivedMsg = PushApplication.getInstance().getGson()
				.fromJson(message, Message.class);
		// �Խ��յ�����Ϣ���д���
		new Thread(new queryPThread()).start();
		

	}

	/**
	 * ��Ϣ��1��Я��hello����ʾ���˼��룬Ӧ���Զ��ظ�һ��world ��Ϣ�� 2����ͨ��Ϣ��
	 * 
	 * @param msg
	 */
	private void parseMessage(Message msg)
	{
		String groupId = String.valueOf(msg.getGroupID());
		// �Լ�����Ϣ
		if (msg.getUserId()
				.equals(PushApplication.getInstance().getApplicationContext().getSharedPreferences("user", 0).getString("username", "")))
			return;
		if (checkHasNewFriend(msg) || checkAutoResponse(msg))
			return;
		// ��ͨ��Ϣ
		//UserDB userDB = PushApplication.getInstance().getUserDB();
		//User user = userDB.selectInfo(userId);
		// ©��֮��
		//if (user == null)
		//{
		//	user = new User(userId, msg.getChannelId(), msg.getNickname(), 0, 0);
		//	userDB.addUser(user);
			// ֪ͨ���������
		//	for (onNewFriendListener listener : friendListeners)
		//		listener.onNewFriend(user);
		//}
		if (msgListeners.size() > 0)
		{// �м�����ʱ�򣬴�����ȥ
			for (int i = 0; i < msgListeners.size(); i++)
				msgListeners.get(i).onNewMessage(msg);
		} else
		// ��ǰû���κμ������������̨״̬
		{
			// ����������Ϣ���д洢
			ChatMessage chatMessage = new ChatMessage(msg.getMessage(), true,
					msg.getUserId(), 
					msg.getHeadIcon(), 
					msg.getNickname(), false,
					TimeUtil.getTime(msg.getTimeSamp()),msg.getGroupID());
			PushApplication.getInstance().getMessageDB()
					.add(groupId, chatMessage);
			showNotify(msg);
		}
	}

	/**
	 * ����Ƿ����Զ��ظ�
	 * 
	 * @param msg
	 */
	private boolean checkAutoResponse(Message msg)
	{
		String world = msg.getWorld();
		String userId = msg.getUserId();
		if (!TextUtils.isEmpty(world))
		{
			User u = new User(userId, msg.getChannelId(), msg.getNickname(),
					(Integer)msg.getHeadIcon(), 0);
			PushApplication.getInstance().getUserDB().addUser(u);// �������º���
			// ֪ͨ���������
			for (onNewFriendListener listener : friendListeners)
				listener.onNewFriend(u);

			return true;
		}
		return false;
	}

	/**
	 * ����Ƿ������˼���
	 * 
	 * @param msg
	 */
	private boolean checkHasNewFriend(Message msg)
	{
		String userId = msg.getUserId();
		String hello = msg.getHello();
		// ���˷��͵���Ϣ
		if (!TextUtils.isEmpty(hello))
		{
			Log.e("checkHasNewFriend", msg.getUserId());

			// ����
			//User u = new User(userId, msg.getChannelId(), msg.getNickname(),
			//		(int)msg.getHeadIcon(), 0);
			//PushApplication.getInstance().getUserDB().addUser(u);// �������º���
			//T.showShort(PushApplication.getInstance(), u.getNick() + "����");

			// �����˻ظ�һ��Ӧ��
			//Message message = new Message(System.currentTimeMillis(), "","12345");
			//message.setWorld("world");
			//new SendMsgAsyncTask(PushApplication.getInstance().getGson()
			//		.toJson(message), userId);
			// ֪ͨ���������
			//for (onNewFriendListener listener : friendListeners)
			//	listener.onNewFriend(u);

			return true;
		}

		return false;
	}

	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString)
	{

		String notifyString = "֪ͨ��� title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.e(TAG, notifyString);

	}

	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId)
	{
		String responseString = "onSetTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.e(TAG, responseString);

	}

	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId)
	{
		String responseString = "onDelTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.e(TAG, responseString);

	}

	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId)
	{
		String responseString = "onListTags errorCode=" + errorCode + " tags="
				+ tags;
		Log.e(TAG, responseString);

	}

	@Override
	public void onUnbind(Context context, int errorCode, String requestId)
	{
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.e(TAG, responseString);

		// ��󶨳ɹ�������δ��flag��
		if (errorCode == 0)
		{
			PreUtils.unbind(context);
		}
	}
	
	public class queryPThread implements Runnable
	{

		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			boolean loginValidate = loginServer();
	    	//System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+responseMsg);
	    	android.os.Message msg = handler.obtainMessage();
	    	if(loginValidate)
	    	{
	    		if(((String)map.get("success")).equals("success"))
	    		{
	    			msg.what = 0;
		    		handler.sendMessage(msg);
	    		}
	    		else
	    		{
	    			msg.what = 1;
	    			handler.sendMessage(msg);
	    		}
	    			
	    		
	    		
	    		
	    	}else
	    	{
	    		msg.what = 2;
	    		handler.sendMessage(msg);
	    	}
			
		}
	}
	
	private boolean loginServer()
    {
    	boolean loginValidate = false;
    	//ʹ��apache HTTP�ͻ���ʵ��
    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";
    	HttpPost request = new HttpPost(urlStr);
    	//������ݲ�����Ļ������ԶԴ��ݵĲ������з�װ
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	//����û���������
    	params.add(new BasicNameValuePair("photopath",(String) receivedMsg.getHeadIcon()));
    	params.add(new BasicNameValuePair("requesttype","3"));
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
    			InputStream is = response.getEntity().getContent();
    			ObjectInputStream ois = new ObjectInputStream(is);
    			
    			
    			map = (Map<String,Object>)ois.readObject();
    			ois.close();
    			is.close();
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return loginValidate;
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



Handler handler = new Handler()
{
	public void handleMessage(android.os.Message msg)
	{
		switch(msg.what)
		{
		case 0:
			//mDialog.cancel();

			//Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_SHORT).show();
			//Intent intent = new Intent();
			//intent.setClass(LoginActivity.this, MainActivity.class);
			//tartActivity(intent);
			//sp=getSharedPreferences("Login",0);
			//sp.edit().putBoolean("ISCHECK", true).commit();
			//sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
			//Intent intent=new Intent(PasswordFActivity.this,Iguide.class);
			//startActivity(intent);
			//PasswordFActivity.this.finish();
			//finish();
			byte[] photoB = (byte[])map.get("photo");
			FileCache fileCache = new FileCache(PushApplication.getInstance());
			File tempfile = fileCache.getFile(photoB.toString().substring(1, 5)+".jpg");
			try {
				tempfile.createNewFile();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			byte2File(photoB,tempfile);
			Bitmap bitmap = decodeFile(tempfile);
			receivedMsg.setHeadIcon(bitmap);
			
			
				
			fileCache.clear();
			parseMessage(receivedMsg);
			break;
		case 1:
			//mDialog.cancel();
			//userinfo1=list.get(0);
			//File image = (File)userinfo1.get("img");
			//image.setImageBitmap(bitmap);
			//Toast.makeText(getApplicationContext(), "ע��ʧ��,�������������⣬���Ժ�����", Toast.LENGTH_SHORT).show();
			parseMessage(receivedMsg);
			break;
		case 2:
			//mDialog.cancel();
			//Toast.makeText(getApplicationContext(), "URL��֤ʧ��", Toast.LENGTH_SHORT).show();
			parseMessage(receivedMsg);
			break;
		
		
		}
		
	}
};

}
