package com.example.ifamily.mywidget;

import android.content.Context;
import android.graphics.Typeface;

public class Mytypeface {
	private Context mContext;
	private Typeface caoshu;
	private Typeface miaowu;
	private Typeface gete;
	private Typeface huati;
	private Typeface kaiti;
	private Typeface xingkai;
	
	public Mytypeface(Context context)
	{
		this.mContext=context;
		caoshu=Typeface.createFromAsset (mContext.getAssets() ,"fonts/caoshu.ttf");
		miaowu= Typeface.createFromAsset (mContext.getAssets() ,"fonts/FZMWFont.ttf");
		gete= Typeface.createFromAsset (mContext.getAssets() ,"fonts/geteti.ttf");
		huati= Typeface.createFromAsset (mContext.getAssets() ,"fonts/huati.ttf");
		kaiti= Typeface.createFromAsset (mContext.getAssets() ,"fonts/kaiti.ttf");
		xingkai= Typeface.createFromAsset (mContext.getAssets() ,"fonts/xingkai.TTF");
	}
	public Typeface gettypeface(int i)
	{
		switch(i){
		case 1:
			return caoshu;
		case 2:
			return miaowu;

		case 3:
			return gete;

		case 4:
			return huati;

		case 5:
			return kaiti;

		case 6:
			return xingkai;

			default: return kaiti;
		}
	}
}
