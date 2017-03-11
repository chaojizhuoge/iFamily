package com.example.ifamily.activity;

import com.baidu.android.pushservice.PushManager;
import com.example.ifamily.PushApplication;
import com.example.ifamily.R;
import com.example.ifamily.R.layout;
import com.example.ifamily.utils.FontManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PrivateActivity extends Fragment implements OnClickListener, android.content.DialogInterface.OnClickListener{
private	RelativeLayout prime,enter,out,set,advice,recom,use,logout;
private TextView title;
private Button back;

private LinearLayout all;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.private_z_4,
				container, false);

		return messageLayout;
	}
	public void onActivityCreated(Bundle savedInstanceState) {  
		 super.onActivityCreated(savedInstanceState);
			initview();
	}
	private void initview()
	{
		
		title = (TextView) getView().findViewById(R.id.title);
		 back = (Button) getView().findViewById(R.id.back);
		 title.setText("个人中心");
		 back.setVisibility(View.GONE);		
		
	prime=(RelativeLayout)getView().findViewById(R.id.re_pri);
	enter=(RelativeLayout)getView().findViewById(R.id.re_enter);
	out=(RelativeLayout)getView().findViewById(R.id.re_out);
	out.setVisibility(View.GONE);
	set=(RelativeLayout)getView().findViewById(R.id.re_new);
	advice=(RelativeLayout)getView().findViewById(R.id.re_adv);
	recom=(RelativeLayout)getView().findViewById(R.id.re_tel);
	use=(RelativeLayout)getView().findViewById(R.id.re_use);
	logout=(RelativeLayout)getView().findViewById(R.id.re_logout);
	prime.setOnClickListener(this);
	enter.setOnClickListener(this);
	out.setOnClickListener(this);
	set.setOnClickListener(this);
	advice.setOnClickListener(this);
	recom.setOnClickListener(this);
	use.setOnClickListener(this);
	logout.setOnClickListener(this);
	all=(LinearLayout)getView().findViewById(R.id.all);
	FontManager.changeFonts(all, this.getActivity());
	
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(arg0.getId())
		{case R.id.re_pri:
			 intent = new Intent(this.getActivity(), Privatemessage.class);
			 intent.putExtra("privateAcct", Long.parseLong(PrivateActivity.this.getActivity().getSharedPreferences("user", 0).getString("username", "")));
			startActivity(intent);
			break;
		case R.id.re_enter:
			 intent = new Intent(this.getActivity(), Entergroup.class);
			startActivity(intent);
			break;
		case	R.id.re_out:
			 intent = new Intent(this.getActivity(),Quitgroup.class);
			startActivity(intent);
			break;
		case	R.id.re_new:
			 intent = new Intent(this.getActivity(), Setgroup.class);
			startActivity(intent);
			break;
		case	R.id.re_adv:
			 intent = new Intent(this.getActivity(), Telladvice.class);
			startActivity(intent);
			break;
		case	R.id.re_tel:
		    intent=new Intent(Intent.ACTION_SEND);
		      String text="ifamily这款软件真的不错，你也来用用吧！";
		      intent.setType("text/plain");
		      intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		      intent.putExtra(Intent.EXTRA_TEXT, text);
		      startActivity(Intent.createChooser(intent, "分享"));
			break;
		case	R.id.re_use:
			 intent = new Intent(this.getActivity(), Usehelp.class);
			startActivity(intent);
			break;
		case	R.id.re_logout:
			 new AlertDialog.Builder(this.getActivity())
				      .setTitle("Question").setMessage(
				      "确定要退出登录").setPositiveButton("Yes",
				       this 
				     ).setNegativeButton("No",
				    		 this).show();
			//intent = new Intent(this.getActivity(), )
			break;
			default:
				break;
		
		}
	}
	
	
	
	public void onClick(DialogInterface dialog,
	         int whichButton) {
		switch(whichButton)
		{
		case DialogInterface.BUTTON_POSITIVE:
		    SharedPreferences sp = this.getActivity().getSharedPreferences("Login",0);
		    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
		    sp = this.getActivity().getSharedPreferences("user", 0);
		    sp.edit().putString("username", "");
		    
		    if(PushApplication.getInstance().getgroupIds()!=null)
		    {
		    	
		    for(String groupid : PushApplication.getInstance().getgroupIds())
		    {
		    	deleteTagThread st = new deleteTagThread();
		    	st.setTag(groupid);
		    	new Thread(st).run();
		    }
		    }
		    PushManager.stopWork(this.getActivity().getApplicationContext());
	        Intent intent = new Intent(this.getActivity(),Login.class);
	        startActivity(intent);
	        this.getActivity().finish();
	        break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.cancel();
			break;
	       }
	}
	
	class deleteTagThread implements Runnable
	 {
		 private String tag;
		 public void setTag(String tag)
		 {
			 this.tag = tag;
		 }

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			PushApplication.getInstance().getBaiduPush().DeleteTag(tag, PushApplication.getInstance().getSpUtil().getUserId());
		}
		 
	 }
}
