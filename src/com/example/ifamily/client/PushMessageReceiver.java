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

	public static int mNewNum = 0;// 通知栏新消息条目，我只是用了一个全局变量，

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
			util.setTag("美女");
		} else
		// 如果网络正常，则重试
		{
			if (NetUtil.isNetConnected(context))
			{

				T.showLong(context, "启动失败，正在重试...");
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						PushManager.startWork(context,
								PushConstants.LOGIN_TYPE_API_KEY,
								PushApplication.API_KEY);
					}
				}, 2000);// 两秒后重新开始验证
			} else
			{
				T.showLong(context, R.string.net_error_tip);
			}
		}
		// 回调函数
		for (int i = 0; i < bindListeners.size(); i++)
			bindListeners.get(i).onBind(userId, errorCode);
	}

	private void showNotify(Message message)
	{
		mNewNum++;
		// 更新通知栏
		PushApplication application = PushApplication.getInstance();

		int icon = R.drawable.icon;
		CharSequence tickerText = message.getNickname() + ":"
				+ message.getMessage();
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;  //通知被点击后,可自动消失  
		notification.defaults |= Notification.DEFAULT_SOUND;  //通知到达时发出默认音乐  
	    notification.defaults |= Notification.DEFAULT_VIBRATE;
		// 设置默认声音
		// notification.defaults |= Notification.DEFAULT_SOUND;
		// 设定震动(需加VIBRATE权限)
		
		notification.contentView = null;

		Intent intent = new Intent(application, ChatChatActivity.class);
		intent.putExtra("groupId", message.getGroupID());
		// 当点击通知时，我们让原有的Activity销毁，重新实例化一个
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(application, 0,
				intent, 0);
		notification.setLatestEventInfo(PushApplication.getInstance(),
				message.getNickname() + " (" + mNewNum + "条新消息)",
				tickerText, contentIntent);
		
		application.getNotificationManager().notify(NOTIFY_ID, notification);// 通知一下才会生效哦
		
	}

	@Override
	public void onMessage(Context context, String message,
			String customContentString)
	{
		String messageString = "收到消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		Log.e(TAG, messageString);
		receivedMsg = PushApplication.getInstance().getGson()
				.fromJson(message, Message.class);
		// 对接收到的消息进行处理
		new Thread(new queryPThread()).start();
		

	}

	/**
	 * 消息：1、携带hello，表示新人加入，应该自动回复一个world 消息； 2、普通消息；
	 * 
	 * @param msg
	 */
	private void parseMessage(Message msg)
	{
		String groupId = String.valueOf(msg.getGroupID());
		// 自己的消息
		if (msg.getUserId()
				.equals(PushApplication.getInstance().getApplicationContext().getSharedPreferences("user", 0).getString("username", "")))
			return;
		if (checkHasNewFriend(msg) || checkAutoResponse(msg))
			return;
		// 普通消息
		//UserDB userDB = PushApplication.getInstance().getUserDB();
		//User user = userDB.selectInfo(userId);
		// 漏网之鱼
		//if (user == null)
		//{
		//	user = new User(userId, msg.getChannelId(), msg.getNickname(), 0, 0);
		//	userDB.addUser(user);
			// 通知监听的面板
		//	for (onNewFriendListener listener : friendListeners)
		//		listener.onNewFriend(user);
		//}
		if (msgListeners.size() > 0)
		{// 有监听的时候，传递下去
			for (int i = 0; i < msgListeners.size(); i++)
				msgListeners.get(i).onNewMessage(msg);
		} else
		// 当前没有任何监听，即处理后台状态
		{
			// 将新来的消息进行存储
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
	 * 检测是否是自动回复
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
			PushApplication.getInstance().getUserDB().addUser(u);// 存入或更新好友
			// 通知监听的面板
			for (onNewFriendListener listener : friendListeners)
				listener.onNewFriend(u);

			return true;
		}
		return false;
	}

	/**
	 * 检测是否是新人加入
	 * 
	 * @param msg
	 */
	private boolean checkHasNewFriend(Message msg)
	{
		String userId = msg.getUserId();
		String hello = msg.getHello();
		// 新人发送的消息
		if (!TextUtils.isEmpty(hello))
		{
			Log.e("checkHasNewFriend", msg.getUserId());

			// 新人
			//User u = new User(userId, msg.getChannelId(), msg.getNickname(),
			//		(int)msg.getHeadIcon(), 0);
			//PushApplication.getInstance().getUserDB().addUser(u);// 存入或更新好友
			//T.showShort(PushApplication.getInstance(), u.getNick() + "加入");

			// 给新人回复一个应答
			//Message message = new Message(System.currentTimeMillis(), "","12345");
			//message.setWorld("world");
			//new SendMsgAsyncTask(PushApplication.getInstance().getGson()
			//		.toJson(message), userId);
			// 通知监听的面板
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

		String notifyString = "通知点击 title=\"" + title + "\" description=\""
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

		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0)
		{
			PreUtils.unbind(context);
		}
	}
	
	public class queryPThread implements Runnable
	{

		@Override
		public void run() {
			// TODO 自动生成的方法存根
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
    	//使用apache HTTP客户端实现
    	String urlStr = "http://103.31.241.201:8080/IFamilyServer/FamilyZoneServlet";
    	HttpPost request = new HttpPost(urlStr);
    	//如果传递参数多的话，可以对传递的参数进行封装
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	//添加用户名和密码
    	params.add(new BasicNameValuePair("photopath",(String) receivedMsg.getHeadIcon()));
    	params.add(new BasicNameValuePair("requesttype","3"));
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



Handler handler = new Handler()
{
	public void handleMessage(android.os.Message msg)
	{
		switch(msg.what)
		{
		case 0:
			//mDialog.cancel();

			//Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
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
				// TODO 自动生成的 catch 块
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
			//Toast.makeText(getApplicationContext(), "注册失败,服务器出现问题，请稍后再试", Toast.LENGTH_SHORT).show();
			parseMessage(receivedMsg);
			break;
		case 2:
			//mDialog.cancel();
			//Toast.makeText(getApplicationContext(), "URL验证失败", Toast.LENGTH_SHORT).show();
			parseMessage(receivedMsg);
			break;
		
		
		}
		
	}
};

}
