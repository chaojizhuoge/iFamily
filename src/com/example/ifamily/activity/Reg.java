package com.example.ifamily.activity;

import com.example.ifamily.ClassPathResource;
import com.example.ifamily.R;
import com.example.ifamily.R.id;
import com.example.ifamily.R.layout;
import com.example.ifamily.utils.FontManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class Reg extends Activity implements android.view.View.OnClickListener{
	public static final int REGION_SELECT = 1;
	private TextView tv_Server,tv_region_modify,tv_region_show,tv_top_title;
	private Button btn_title_left,btn_send_code;
	private CheckBox chk_agree;
	private EditText et_mobileNo;
	
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.regist);
		initView();
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		 SharedPreferences sps = this.getSharedPreferences("SP", Context.MODE_PRIVATE);
		  
	  	  
		  if(sps.getBoolean("ok_KEY", true)!=false){		  		  
		  Editor editor = sps.edit();
		  editor.putBoolean("ok_KEY",false);
		  editor.commit();	
				 chk_agree.setChecked(true);
		  }
		  else{
			  chk_agree.setChecked(false);
		  }
		
	}
	private void initView(){
		

		
		
		
		
		
		
		
		LinearLayout all=(LinearLayout)findViewById(R.id.all);//��������
		FontManager.changeFonts(all,this);
		
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("Ifamilyע��");
		
		
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);
		
		tv_Server = (TextView) findViewById(R.id.tv_Server);
	
		tv_Server.setOnClickListener(this);
		tv_region_show = (TextView) findViewById(R.id.tv_region_show);
		
		tv_region_modify = (TextView) findViewById(R.id.tv_region_modify);
		tv_region_modify.setOnClickListener(this);
		
		chk_agree = (CheckBox) findViewById(R.id.chk_agree);
		
		
		
		
	/*	  SharedPreferences sps = this.getSharedPreferences("SP", Context.MODE_PRIVATE);
		  
		  	  
		  if(sps.getBoolean("ok_KEY", true)!=false){		  		  
		  Editor editor = sps.edit();
		  editor.putBoolean("ok_KEY",false);
		  editor.commit();	
				 chk_agree.setChecked(false);
		  }
		  else{
			  chk_agree.setChecked(true);
		  }
		
		*/
		
		
		et_mobileNo = (EditText) findViewById(R.id.et_mobileNo);
		
		sp = getSharedPreferences("regist",0);
	}
	
	/**
	 * ��д��onCreateDialog����������һ���б�Ի���
	 */
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		switch(id){
		case REGION_SELECT:
			final Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("��ѡ�����ڵ�");
			builder.setSingleChoiceItems(//��һ��������Ҫ��ʾ���б�������չʾ���ڶ���������Ĭ��ѡ�е����λ�ã�
					//������������һ���¼����������
					new String[]{"+86�й���½","+853�й�����","+852�й����","+886�й�̨��"},
					0, 
					new OnClickListener() {
						
						@SuppressWarnings("deprecation")
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch(which){
							case 0:
								tv_region_show.setText("+86�й���½");
								
								break;
							case 1:
								tv_region_show.setText("+853�й�����");
								break;
							case 2:
								tv_region_show.setText("+852�й����");
								break;
							case 3:
								tv_region_show.setText("+886�й�̨��");
								break;
							}
							dismissDialog(REGION_SELECT);//��Ի���ر�
						}
					});
			return builder.create();
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.tv_Server:
			Intent it=new Intent(Reg.this,RegServer.class);	
			startActivity(it);
		break;
		
		case R.id.tv_region_modify:
			showDialog(REGION_SELECT);//��ʾ�б�Ի���ķ���
			break;
		case R.id.btn_title_left:
			Reg.this.finish();
			break;
		case R.id.btn_send_code:
			String mobiles = et_mobileNo.getText().toString();
			if(chk_agree.isChecked()== false)//��û��ѡcheckbox�޷���������
				Toast.makeText(this, "��ȷ���Ƿ��Ѿ��Ķ���Ifamily�������", Toast.LENGTH_LONG).show();
			if(ClassPathResource.isMobileNO(mobiles)==false)//���ֻ������ϸ���֤���μ��������е�������ʽ
				Toast.makeText(this, "��ȷ��д�ֻ��ţ����ǽ���������һ����֤�����", Toast.LENGTH_LONG).show();
			if(ClassPathResource.isMobileNO(mobiles)==true&&chk_agree.isChecked()==true){
				//����ѡ���Һ�����ȷ�����������һ������
				Toast.makeText(this, "�Ѿ������ֻ�������֤�룬��鿴(��ǰ����״̬�������˲���)", Toast.LENGTH_LONG).show();
				Editor edit = sp.edit();
				edit.putString("username", mobiles);
				edit.commit();
				Intent intent = new Intent(Reg.this, RegisterConfirmActivity.class);
				startActivity(intent);
			}
		}
		
	}

}

	


