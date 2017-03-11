package com.example.ifamily.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.ifamily.R;
import com.example.ifamily.adapter.FamilylistAdapter;
import com.example.ifamily.adapter.FriendlistAdapter;
import com.example.ifamily.message.AddfriendMessage;
import com.example.ifamily.message.GroupLMessage;
import com.example.ifamily.utils.FontManager;

public class Quitgroup extends Activity implements OnClickListener{
	private TextView title;
	private Button back;
	private PopupWindow popupWindow;  
    private FamilylistAdapter za;

    private LinearLayout sel;
   private LinearLayout all;
   private List<GroupLMessage> messages = new ArrayList<GroupLMessage>(); 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_quitgroup);
		initview();
	}
	private void initview()
	{
		all=(LinearLayout)findViewById(R.id.all);//整个界面
		FontManager.changeFonts(all,this);
		sel=(LinearLayout)findViewById(R.id.sel);//选择家庭
		 title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("退出家庭");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				Quitgroup.this.finish();
			}
			 
		 });
		 
			sel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					za.resetstate();
					showwindow(sel);
				}
				
			});
		 
		 
		 
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void showwindow(LinearLayout spinnerlayout)
	{
		
		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

			}
		   final LinearLayout  layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.a_add_sch_friendslist, null);  
		   popupWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
		   final ListView  Lv = (ListView) layout.findViewById(R.id.a_add_sch_list);
		  
		   Lv.setAdapter(za);
		   
		   Lv.setOnItemClickListener(new OnItemClickListener(){  
				  
	        	
	            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {  
	                // TODO Auto-generated method stub  
	       
	            	
	            	//myfriends.setText(messages.get(arg2).getname());
	              //  popupWindow.dismiss();  
	               // popupWindow = null;  
	            }  
			
	        });  
		   
		   
		   layout.setOnTouchListener(new OnTouchListener() {  
	              
	            public boolean onTouch(View v, MotionEvent event) {  
	                  
	                int height = layout.findViewById(R.id.a_add_sch_list).getTop();  
	                int bheight=layout.findViewById(R.id.a_add_sch_list).getHeight()+height;
	                int y=(int) event.getY();  
	                if(event.getAction()==MotionEvent.ACTION_UP){  
	                    if(y<height||y>bheight){  
	                    	popupWindow.dismiss();
	                    	popupWindow=null;
	                    }  
	                    
	                }                 
	                return true;  
	            }  
	        });
	        //实例化一个ColorDrawable颜色为透明  
	        ColorDrawable dw = new ColorDrawable(0000000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        popupWindow.setBackgroundDrawable(dw);  
	        popupWindow.setFocusable(true);
	       popupWindow.setAnimationStyle(R.style.popupfrombottom);
	       popupWindow.showAtLocation(all, Gravity.CENTER, 0, 0);
 
	       
	        
	        
	        
	        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

	        	public void onDismiss() {
	        	/*	groupIDs=  za.getids();
	        		String myfri=new String();
	        		if(!za.getnames().isEmpty())
	        		{
	        		for(int i=0;i<za.getnames().size();i++)
	        		{
	        			String temp=String.valueOf(za.getnames().get(i));
	        		  myfri=temp+","+myfri;
	        			
	        		}
	        		myfri = myfri.substring(0, myfri.length()-1);
	        		 selwish.setText(myfri) ;*/
	        	}
	        	});
	      
	}
	
	
	@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK &&(popupWindow != null && popupWindow.isShowing())) { 
    	//监控/拦截/屏蔽返回键
    		popupWindow.dismiss();

    		popupWindow = null;
    		return false;    	

    }
    else {return super.onKeyDown(keyCode, event);}
}
	
	
}