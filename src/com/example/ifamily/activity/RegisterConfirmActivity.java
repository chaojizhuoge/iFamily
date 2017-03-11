package com.example.ifamily.activity;

import com.example.ifamily.R;
import com.example.ifamily.R.id;
import com.example.ifamily.R.layout;
import com.example.ifamily.utils.FontManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/**ע����֤����activity*/
public class RegisterConfirmActivity extends Activity implements OnClickListener{
	private Button btn_reg_reget,btn_title_left,btn_reg_ok;
	private TextView tv_reg_reget,tv_top_title;
	private MyCount mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_confirm);
		initView();
	}

	private void initView() {
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//��������
		FontManager.changeFonts(all,this);
		tv_reg_reget = (TextView) findViewById(R.id.tv_reg_reget);
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("Ifamilyע��");
		mc = new MyCount(10000, 1000);
		mc.start();
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		btn_reg_reget = (Button) findViewById(R.id.btn_reg_reget);
		btn_reg_reget.setOnClickListener(this);
		btn_reg_ok=(Button)findViewById(R.id.btn_reg_ok);
		btn_reg_ok.setOnClickListener(this);


		
	}
	
	/**�Զ���һ���̳�CountDownTimer���ڲ��࣬����ʵ�ּ�ʱ���Ĺ���*/
	class MyCount extends CountDownTimer{
		/**
		 * MyCount�Ĺ��췽��
		 * @param millisInFuture Ҫ����ʱ��ʱ��
		 * @param countDownInterval ʱ����
		 */
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {//�ڽ��е���ʱ��ʱ��ִ�еĲ���
			long second = millisUntilFinished /1000;
			tv_reg_reget.setText(second+"���������»����֤��");
			if(second == 10){
				tv_reg_reget.setText(9+"���������»����֤��");
			}
			Log.i("PDA", millisUntilFinished/1000+"");
			
		}

		@Override
		public void onFinish() {//����ʱ������Ҫ��������
			// TODO Auto-generated method stub
			tv_reg_reget.setVisibility(View.GONE);
			btn_reg_reget.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_reg_reget:
			mc.start();
			tv_reg_reget.setVisibility(View.VISIBLE);
			btn_reg_reget.setVisibility(View.GONE);
			break;
		case R.id.btn_title_left:
			RegisterConfirmActivity.this.finish();
			break;
		case R.id.btn_reg_ok:
			Intent intent=new Intent(RegisterConfirmActivity.this,PasswordFActivity.class);
			startActivity(intent);
			RegisterConfirmActivity.this.finish();//��֤����֤
		}
	}



}
