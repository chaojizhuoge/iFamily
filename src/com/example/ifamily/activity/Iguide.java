package com.example.ifamily.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifamily.R;
import com.example.ifamily.activity.MyPicActivity.ImageViewOnTouchListener;
import com.example.ifamily.mywidget.BadgeView;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.utils.MyApplication;



public class Iguide extends  FragmentActivity{ 


	private ImageView chatImage,moreImage,noteImage,privateImage;
	/**
	 * ��Ϣ���沼��
	 */
	private View chatLayout;

	private LinearLayout all;
	/**
	 * ��ϵ�˽��沼��
	 */
	private View moreLayout;

	/**
	 * ��̬���沼��
	 */
	private View noteLayout;

	/**
	 * ���ý��沼��
	 */
	private View privateLayout;

	/**
	 * ��Tab��������ʾͼ��Ŀؼ�
	 */

	
	private TextView chatText,moreText,noteText,privateText;

	
	
	
	
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	/**
	 * �ֱ�Ϊÿ��TabIndicator����һ��BadgeView
	 */
	private BadgeView mBadgeViewforLiaotian;

	/**
	 * ViewPager�ĵ�ǰѡ��ҳ
	 */
	private int currentIndex;
	/**
	 * ��Ļ�Ŀ��
	 */
	private int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.i_guide);
		MyApplication.getInstance().addActivity(this);
		initView();
		/**
		 * ��ʼ��Adapter
		 */
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}
		};
		
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mAdapter);

		/**
		 * ���ü���
		 */
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{

				clearSelection();
				switch (position) {
				case 0:
					// ���������Ϣtabʱ���ı�ؼ���ͼƬ��������ɫ
					chatImage.setImageResource(R.drawable.message_selected);
					chatText.setTextColor(Color.WHITE);
			
				
					break;
				case 1:
					// ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ
					noteImage.setImageResource(R.drawable.contacts_selected);
					noteText.setTextColor(Color.WHITE);
				
					break;
				case 2:
					// ������˶�̬tabʱ���ı�ؼ���ͼƬ��������ɫ
					moreImage.setImageResource(R.drawable.news_selected);
					moreText.setTextColor(Color.WHITE);
				
					break;
				case 3:
				default:
					// �����������tabʱ���ı�ؼ���ͼƬ��������ɫ
					privateImage.setImageResource(R.drawable.setting_selected);
					privateText.setTextColor(Color.WHITE);
					
					break;
				}
				
				currentIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{


			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});

		mViewPager.setCurrentItem(1);
		int tab=1;
		   Intent intent = getIntent();
		   if(intent!=null)
		   {  
			   
			   
			   tab =intent.getIntExtra("tab", 1);
	        mViewPager.setCurrentItem(tab);}
	}



	/**
	 * ��ʼ���ؼ�����ʼ��Fragment
	 */
	private void initView()
	{

	       
	      
		mViewPager = (ViewPager) findViewById(R.id.vp_old);
		

		
		
		chatImage = (ImageView) findViewById(R.id.message_image);
		noteImage = (ImageView) findViewById(R.id.contacts_image);
		moreImage = (ImageView) findViewById(R.id.news_image);
		privateImage = (ImageView) findViewById(R.id.setting_image);
		chatLayout = findViewById(R.id.message_layout);
		noteLayout = findViewById(R.id.contacts_layout);
		moreLayout = findViewById(R.id.news_layout);
		privateLayout = findViewById(R.id.setting_layout);
		chatText = (TextView) findViewById(R.id.message_text);
		noteText = (TextView) findViewById(R.id.contacts_text);
		moreText = (TextView) findViewById(R.id.news_text);
		privateText = (TextView) findViewById(R.id.setting_text);
		chatLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0);
			}
			
		});
		noteLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1);
			}
			
		});
		moreLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(2);
			}
			
		});
		privateLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(3);
			}
			
		});
		
		
		
		
		
		

		
		ChatActivity hm = new ChatActivity();
		NoteActivity hs = new NoteActivity();
		MoreActivity hn=new MoreActivity();
		PrivateActivity ho=new PrivateActivity();
		mFragments.add(hm);
		mFragments.add(hs);
		mFragments.add(hn);
		mFragments.add(ho);
		//mBadgeViewforLiaotian = new BadgeView(this);
	//	mBadgeViewforLiaotian.setVisibility(View.GONE);
		all=(LinearLayout)findViewById(R.id.all);
		FontManager.changeFonts(all, this);
	}	
	
	
	private void clearSelection() {
		chatImage.setImageResource(R.drawable.message_unselected);
		chatText.setTextColor(Color.parseColor("#82858b"));
		noteImage.setImageResource(R.drawable.contacts_unselected);
		noteText.setTextColor(Color.parseColor("#82858b"));
		moreImage.setImageResource(R.drawable.news_unselected);
		moreText.setTextColor(Color.parseColor("#82858b"));
		privateImage.setImageResource(R.drawable.setting_unselected);
		privateText.setTextColor(Color.parseColor("#82858b"));
	}
	
	@Override  
	 public boolean onKeyDown(int keyCode, KeyEvent event) {  
	  if (keyCode == KeyEvent.KEYCODE_BACK) {  
	   moveTaskToBack(false);  
	   return true;  
	  }  
	  return super.onKeyDown(keyCode, event);  
	 } 
	
	
	public NoteActivity getNote()
	{
		return (NoteActivity) mFragments.get(1);
	}
}
