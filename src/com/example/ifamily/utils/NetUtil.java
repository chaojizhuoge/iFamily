package com.example.ifamily.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// ����������ӷ���?
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
//			String name = info.getTypeName();
//			L.i("��ǰ������ƣ�? + name);
			isNetConnected = true;
		} else {
			L.i("û�п�������");
			isNetConnected = false;
		}
		return isNetConnected;
	}
}
