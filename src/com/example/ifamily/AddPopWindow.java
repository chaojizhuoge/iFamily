package com.example.ifamily;

import java.io.File;

import com.example.ifamily.activity.AddPicActivity;
import com.example.ifamily.activity.AddTextActivity;
import com.example.ifamily.activity.FamilyzoneActivity;
import com.example.ifamily.activity.Login;
import com.example.ifamily.activity.MainActivity;
import com.example.ifamily.activity.Setgroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;


public class AddPopWindow extends PopupWindow {
	private View conentView;

	public AddPopWindow(final Activity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.add_popup_dialog, null);
		@SuppressWarnings("deprecation")
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		@SuppressWarnings("deprecation")
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		// ����SelectPicPopupWindow��View
		this.setContentView(conentView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(w / 2 + 50);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// ˢ��״̬
		this.update();
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0000000000);
		// ��back���������ط�ʹ����ʧ,������������ܴ���OnDismisslistener �����������ؼ��仯�Ȳ���
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.AnimationPreview);
		LinearLayout addtex = (LinearLayout) conentView
				.findViewById(R.id.add_tex);
		LinearLayout addpic = (LinearLayout) conentView
				.findViewById(R.id.add_pic);
		LinearLayout addsc = (LinearLayout) conentView
				.findViewById(R.id.add_sc);
		addtex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,AddTextActivity.class);
				context.startActivity(intent);
				context.finish();
				}
			
		});

		addpic.setOnClickListener((OnClickListener) context
				//new OnClickListener() {
/*
			@Override
			public void onClick(View arg0) {
				switch (arg0.getId())
				{
				case R.id.add_pic:
					dismiss();
					
					SelectPicPopupWindow menuWindow = new SelectPicPopupWindow(context, (OnClickListener) context);
					//��ʾ����
					menuWindow.showAtLocation(context.findViewById(R.id.mainView), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
				    break;
				
				}
			}
			
		*/	
			
		//}
	);
		
		
		addsc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,Login.class);
				context.startActivity(intent);
				context.finish();
			}
		});
	}

	/**
	 * ��ʾpopupWindow
	 * 
	 * @param parent
	 */
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			// ��������ʽ��ʾpopupwindow
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
		} else {
			this.dismiss();
		}
	}
}
