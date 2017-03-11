package com.example.ifamily.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifamily.R;
import com.example.ifamily.activity.EnterGroupAddF;
import com.example.ifamily.activity.Iguide;
import com.example.ifamily.activity.MyPicActivity;
import com.example.ifamily.activity.SysMsActivity;
import com.example.ifamily.message.Sysmessage;
import com.example.ifamily.mywidget.TextViewM;
import com.example.ifamily.utils.FontManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SysLvAdapter extends BaseAdapter {  
    private Context mContext;  
    private List<Sysmessage> message = new ArrayList<Sysmessage>();      
    public SysLvAdapter(Context c,List<Sysmessage> messages) {  
        mContext = c; 

        message=messages;
    }  
  


  
    public int getCount() {  
   
        return message.size();  
    }  
  
    public Object getItem(int position) {  
        return message.get(position);  
    }  
  
    public long getItemId(int position) {  
        return 0;  
    }  
  
    // create a new ImageView for each item referenced by the Adapter  
    public View getView(final int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder = null;
    	
        if (convertView == null) {
        	// if it's not recycled, initialize some attributes  
        holder=new ViewHolder();
        convertView=LayoutInflater.from(mContext).inflate(R.layout.p_sysmessage_item, null);
        holder.hisname= (TextViewM)convertView.findViewById(R.id.p_sys_hisname);
        holder.time= (TextView)convertView.findViewById(R.id.time);
        holder.groupname= (TextViewM)convertView.findViewById(R.id.p_sys_family);
        holder.tv=(TextView)convertView.findViewById(R.id.text);
        holder.ok= (TextView)convertView.findViewById(R.id.p_sys_ok);
        holder.no=(TextView)convertView.findViewById(R.id.p_sys_no);
        convertView.setTag(holder);
        } else {  
        	
				holder = (ViewHolder) convertView.getTag();			
        }  
       // if(message.get(position).gettype()==1){
       // 	holder.tv.setText("接受您的邀请，已加入family:");
       // 	
       // }else{
       // 	holder.tv.setText("邀请您加入family：");
       // }
       final TextView ok=holder.ok;
       final TextView no=holder.no;
		//LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
		//FontManager.changeFonts(all, mContext);
        holder.hisname.setidandtype(message.get(position).gethisid(), 1);
        holder.groupname.setidandtype(message.get(position).getgroupid(), 2);
        holder.time.setText(message.get(position).gettime());
        holder.groupname.setText(message.get(position).getgroupname());
        holder.hisname.setText(message.get(position).gethisname());
        holder.ok.setOnClickListener(new OnClickListener(){
    		
    		@Override
    		public void onClick(View arg0) {
    			// TODO Auto-generated method stub
    			Log.e("123", String.valueOf(position));
    			if(message.get(position).gettype()==1)
    			{
    			try {
    				
    				
    				RequestParams params = new RequestParams();

    				params.put("user1", String.valueOf(message.get(position).gethisid()));
    				params.put("user2", mContext.getSharedPreferences("user",0).getString("username", ""));
    				params.put("groupId", String.valueOf(message.get(position).getgroupid()));
    				params.put("requesttype", "4");
    				//params.put("addtype", "2");

    				
    				//params.put("hasphoto", String.valueOf(hasphoto));
    				String url = "http://103.31.241.201:8080/IFamilyServer/MessageServlet";

    				AsyncHttpClient client = new AsyncHttpClient();
    				client.post(url, params, new AsyncHttpResponseHandler() {
    					
    					@Override
    					public void onSuccess(int statusCode, Header[] headers,
    							byte[] responseBody) {
    						try {
    							if (statusCode == 200) {
    								Toast.makeText(mContext,
    										"恭喜您已经加入该家庭~", Toast.LENGTH_SHORT).show();
    								no.setVisibility(View.GONE);
    			        			ok.setText("已接受");
    			        			ok.setEnabled(false);
    								Intent intent=new Intent(mContext,Iguide.class);
    								intent.putExtra("tab", 1);
    								
    								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
    								 mContext.startActivity(intent);	
    								 ((SysMsActivity)mContext).finish();
    							} else {
    								Toast.makeText(mContext,
    										"网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();

    							}
    						} catch (Exception e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    					}

    					@Override
    					public void onFailure(int statusCode, Header[] headers,
    							byte[] responseBody, Throwable error) {
    						Toast.makeText(mContext,
    								"网络访问异常，错误码  > " + statusCode, 0).show();

    					}
    				});

    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			
    			
    		}
    			else if(message.get(position).gettype()==2)
    			{

        			// TODO Auto-generated method stub
        			try {
        				
        				
        				RequestParams params = new RequestParams();

        				params.put("user1", String.valueOf(message.get(position).gethisid()));
        				params.put("user2", mContext.getSharedPreferences("user",0).getString("username", ""));
        				params.put("groupId", String.valueOf(message.get(position).getgroupid()));
        				params.put("requesttype", "3");
        				//params.put("addtype", "2");

        				
        				//params.put("hasphoto", String.valueOf(hasphoto));
        				String url = "http://103.31.241.201:8080/IFamilyServer/MessageServlet";

        				AsyncHttpClient client = new AsyncHttpClient();
        				client.post(url, params, new AsyncHttpResponseHandler() {
        					
        					@Override
        					public void onSuccess(int statusCode, Header[] headers,
        							byte[] responseBody) {
        						try {
        							if (statusCode == 200) {
        								Toast.makeText(mContext,
        										"已经接受对方加入家庭！~", Toast.LENGTH_SHORT).show();
        								no.setVisibility(View.GONE);
        			        			ok.setText("已接受");
        			        			ok.setEnabled(false);
        								
        							} else {
        								Toast.makeText(mContext,
        										"网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();

        							}
        						} catch (Exception e) {
        							// TODO Auto-generated catch block
        							e.printStackTrace();
        						}
        					}

        					@Override
        					public void onFailure(int statusCode, Header[] headers,
        							byte[] responseBody, Throwable error) {
        						Toast.makeText(mContext,
        								"网络访问异常，错误码  > " + statusCode, 0).show();

        					}
        				});

        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        			
        		
    			}
    			
    		}
    	});
    	holder.no.setOnClickListener(new OnClickListener(){
    		
    		@Override
    		public void onClick(View arg0) {
    			// TODO Auto-generated method stub
    			if(message.get(position).gettype()==1){
    			try {
    				
    				
    				RequestParams params = new RequestParams();

    				params.put("user1", String.valueOf(message.get(position).gethisid()));
    				params.put("user2", mContext.getSharedPreferences("user",0).getString("username", ""));
    				params.put("groupId", String.valueOf(message.get(position).getgroupid()));
    				params.put("requesttype", "6");
    				//params.put("addtype", "2");

    				
    				//params.put("hasphoto", String.valueOf(hasphoto));
    				String url = "http://103.31.241.201:8080/IFamilyServer/MessageServlet";

    				AsyncHttpClient client = new AsyncHttpClient();
    				client.post(url, params, new AsyncHttpResponseHandler() {
    					
    					@Override
    					public void onSuccess(int statusCode, Header[] headers,
    							byte[] responseBody) {
    						try {
    							if (statusCode == 200) {
    								Toast.makeText(mContext,
    										"已经拒绝~", Toast.LENGTH_SHORT).show();
    								ok.setVisibility(View.GONE);
    			        			no.setText("已接受");
    			        			no.setEnabled(false);
    								
    							} else {
    								Toast.makeText(mContext,
    										"网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();

    							}
    						} catch (Exception e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    					}

    					@Override
    					public void onFailure(int statusCode, Header[] headers,
    							byte[] responseBody, Throwable error) {
    						Toast.makeText(mContext,
    								"网络访问异常，错误码  > " + statusCode, 0).show();

    					}
    				});

    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			
    			
    		}
    			else if(message.get(position).gettype()==2)
    			{

        			// TODO Auto-generated method stub
        			try {
        				
        				
        				RequestParams params = new RequestParams();

        				params.put("user1", String.valueOf(message.get(position).gethisid()));
        				params.put("user2", mContext.getSharedPreferences("user",0).getString("username", ""));
        				params.put("groupId", String.valueOf(message.get(position).getgroupid()));
        				params.put("requesttype", "6");
        				//params.put("addtype", "2");

        				
        				//params.put("hasphoto", String.valueOf(hasphoto));
        				String url = "http://103.31.241.201:8080/IFamilyServer/MessageServlet";

        				AsyncHttpClient client = new AsyncHttpClient();
        				client.post(url, params, new AsyncHttpResponseHandler() {
        					
        					@Override
        					public void onSuccess(int statusCode, Header[] headers,
        							byte[] responseBody) {
        						try {
        							if (statusCode == 200) {
        								Toast.makeText(mContext,
        										"已经拒绝~", Toast.LENGTH_SHORT).show();
        								ok.setVisibility(View.GONE);
        			        			no.setText("已接受");
        			        			no.setEnabled(false);
        								
        							} else {
        								Toast.makeText(mContext,
        										"网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();

        							}
        						} catch (Exception e) {
        							// TODO Auto-generated catch block
        							e.printStackTrace();
        						}
        					}

        					@Override
        					public void onFailure(int statusCode, Header[] headers,
        							byte[] responseBody, Throwable error) {
        						Toast.makeText(mContext,
        								"网络访问异常，错误码  > " + statusCode, 0).show();

        					}
        				});

        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        			
        		
    			}
    			
    		}
    		
    	});
        if(message.get(position).gettype()==1)
        {
        	holder.tv.setText("邀请您加入family：");
        	
        }
        else if(message.get(position).gettype()==2)
        {
        	holder.tv.setText("申请加入family：");

        }
        else if(message.get(position).gettype()==3)
        {
        	holder.tv.setText("已经同意你加入family：");
        	holder.ok.setVisibility(View.GONE);
        	holder.no.setVisibility(View.GONE);
        }
        else if(message.get(position).gettype()==4)
        {
        	holder.tv.setText("同意加入family：");
        	holder.ok.setVisibility(View.GONE);
        	holder.no.setVisibility(View.GONE);
        }
        else if(message.get(position).gettype()==5)
        {
        	holder.tv.setText("拒绝加入family：");
        	holder.ok.setVisibility(View.GONE);
        	holder.no.setVisibility(View.GONE);
        }
        
        return convertView;
    }

    static class ViewHolder {		
    	TextView ok,no;
		 TextViewM groupname;
		 TextViewM hisname;
		 TextView tv;
		 TextView time;
		 
		}


}  
