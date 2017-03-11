package com.example.ifamily.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Home������װ
 * 
 * @author way
 * 
 */
public class HomeWatcher
{

	static final String TAG = "HomeWatcher";
	private Context mContext;
	private IntentFilter mFilter;
	private OnHomePressedListener mListener;
	private InnerRecevier mRecevier;

	// �ص��ӿ�
	public interface OnHomePressedListener
	{
		public void onHomePressed();

		public void onHomeLongPressed();
	}

	public HomeWatcher(Context context)
	{
		mContext = context;
		mRecevier = new InnerRecevier();
		mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	}

	/**
	 * ���ü���
	 * 
	 * @param listener
	 */
	public void setOnHomePressedListener(OnHomePressedListener listener)
	{
		mListener = listener;
	}

	/**
	 * ��ʼ����ע���?	 */
	public void startWatch()
	{
		if (mRecevier != null)
		{
			mContext.registerReceiver(mRecevier, mFilter);
		}
	}

	/**
	 * ֹͣ����ע���?	 */
	public void stopWatch()
	{
		if (mRecevier != null)
		{
			mContext.unregisterReceiver(mRecevier);
		}
	}

	/**
	 * �㲥������
	 */
	class InnerRecevier extends BroadcastReceiver
	{
		final String SYSTEM_DIALOG_REASON_KEY = "reason";
		final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
		final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
			{
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				if (reason != null)
				{
					L.i(TAG, "action:" + action + ",reason:" + reason);
					if (mListener != null)
					{
						if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY))
						{
							// �̰�home��
							mListener.onHomePressed();
						} else if (reason
								.equals(SYSTEM_DIALOG_REASON_RECENT_APPS))
						{
							// ����home��
							mListener.onHomeLongPressed();
						}
					}
				}
			}
		}
	}
}