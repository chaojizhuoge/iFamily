package com.example.ifamily.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ifamily.DIPXPconvert;
import com.example.ifamily.R;
import com.example.ifamily.message.RelationM;
import com.example.ifamily.message.Relations;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FontManager;




public class RelationshipMAdapter extends BaseAdapter {  
  private Context mContext;  
  private List<RelationM> male,female;
  private PopupWindow popupWindow;
  private View v;
  private Relations relations;
		  
    public RelationshipMAdapter(Context c,List<RelationM> male,List<RelationM> female) {  
        this.mContext = c; 
        this.male=male;
        this.female=female;
    }  
  
  
    public int getCount() {  
    	
        return 2*Math.max(male.size(),female.size());  
    }
  
    public Object getItem(int position) {  
    	switch((position+1)%2)
    	{
    	case 0:
    		if(((position+1)/2)<=female.size())
    		{return female.get((position-1)/2);}
    		else return null;
		case 1: 
			if((position/2+1)<=male.size())
			{return male.get(position/2+1);  } 
			else return null;
    	default: 
    		return null;
    		
    	}    	
    }  
  
    public long getItemId(int position) {  
        return 0;  
    }  
  
            // create a new ImageView for each item referenced by the Adapter  
    public View getView(final int position, View convertView, ViewGroup parent) {  
    	final ViewHolder holder = new ViewHolder();    
        	// if it's not recycled, initialize some attributes  
        convertView=LayoutInflater.from(mContext).inflate(R.layout.familymessage_relation, null);
        holder.rv= (RoundImageView)convertView.findViewById(R.id.head);
        holder.name=(TextView)convertView.findViewById(R.id.name);  
        holder.re=(RelativeLayout)convertView.findViewById(R.id.re);  
        convertView.setTag(holder);

	//	LinearLayout all=(LinearLayout)convertView.findViewById(R.id.all);
	//	FontManager.changeFonts(all, mContext);
        switch((position+1)%2)
    	{
    	case 0:
    		if(((position+1)/2)<=female.size()){
    			holder.rv.setImageBitmap((Bitmap)female.get((position-1)/2).gethead());
    		    holder.name.setText(female.get((position-1)/2).getrelationship());
    		    holder.rv.setidandtype(female.get((position-1)/2).getaccount(), 1);
    		    
    		   /* holder.re.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						relations=null;
						relations=new Relations(1);
						popmale(v,holder.name,(position-1)/2);
					}
    		    	
    		    });*/
    		
    		}
    		else holder.re.setVisibility(View.GONE);
    		break;
		case 1: 
			if((position/2+1)<=male.size())
			{holder.rv.setImageBitmap((Bitmap)male.get(position/2).gethead());
		    holder.name.setText(male.get(position/2).getrelationship());
		    holder.rv.setidandtype(male.get(position/2).getaccount(), 1);	
		    
		 /*   holder.re.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					relations=null;
					relations=new Relations(2);
					popmale(v,holder.name,position/2);
				}
		    	
		    });*/
		    
			}
			else holder.re.setVisibility(View.GONE);
			break;
    	default: 
    		break;
    		
    	}    	
        return convertView;  
    }

    static class ViewHolder {		
		 RoundImageView rv;	
		 TextView name;
		 RelativeLayout re;
		}

/*    private void popmale(View v,final TextView name,final int position)
    {
    	if (popupWindow!= null && popupWindow.isShowing()) {

    		popupWindow.dismiss();

    		popupWindow= null;

    		}
    
    	  final  RelativeLayout  layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.familymessagepop, null);
    	  LinearLayout relative=(LinearLayout)layout.findViewById(R.id.relatives);
    	  List<TextView> bt=new ArrayList<TextView>();
    	  for(int i=1;i<relations.getrelations().size()+1;i++)
    	  {
    		  final TextView tv=new TextView(v.getContext());
    		  tv.setHeight(DIPXPconvert.dip2px(v.getContext(),50));
    		  tv.setWidth(LayoutParams.MATCH_PARENT);
    		  tv.setId(i);
    		  tv.setGravity(Gravity.CENTER);
    		  tv.setText(relations.getrelations().get(i-1));
    		  tv.setTextColor(v.getContext().getResources().getColor(R.color.blue));
    		  tv.setBackground(v.getContext().getResources().getDrawable(R.drawable.cornerview));
    		  tv.setOnClickListener(new OnClickListener()
    		  {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					name.setText(tv.getText());
					//这里写点击事件
					////////////////////////////////////////////////////////////
					popupWindow.dismiss();
				}
    			  
    		  });
    		  relative.addView(tv);
    	  }
    	
    	  ScrollView sc=( ScrollView)layout.findViewById(R.id.sc);
    	  layout.setOnTouchListener(new OnTouchListener() {  
              
	            public boolean onTouch(View v, MotionEvent event) {  
	                  
	                int height = layout.findViewById(R.id.sc).getTop();  
	                int y=(int) event.getY();  
	                if(event.getAction()==MotionEvent.ACTION_UP){  
	                    if(y<height){  
	                    	popupWindow.dismiss();
	                    	popupWindow=null;
	                    }  
	                    
	                }                 
	                return true;  
	            }  
	        });
    	  v.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				return false;
			}
    		  
    	  });
    	  
            popupWindow= new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
      	    popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setAnimationStyle(R.style.popupfrombottom);
            popupWindow.showAtLocation(v,Gravity.CENTER, 0, 0);

    }
    */
    public void setpop(View v)
    {
    	this.v=v;
    }
    

}  
