package com.example.ifamily.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.message.HeadM;
import com.example.ifamily.mywidget.RoundImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class GridHeadsAdapter extends BaseAdapter {  
    private Context mContext;  
  private List<HeadM> heads;
    public GridHeadsAdapter(Context c,List<HeadM> img) {  
        mContext = c; 
        heads=img;
    }  
  


  
    public int getCount() {  
        return heads.size();  
    }  
  
    public Object getItem(int position) {  
        return heads.get(position);  
    }  
  
    public long getItemId(int position) {  
        return 0;  
    }  
  
            // create a new ImageView for each item referenced by the Adapter  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder = null;
    	
        if (convertView == null) {
        	// if it's not recycled, initialize some attributes  
        holder=new ViewHolder();
        convertView=LayoutInflater.from(mContext).inflate(R.layout.familyzone_heads, null);
        holder.rv= (RoundImageView)convertView.findViewById(R.id.ri_familyzone_head);
       
        convertView.setTag(holder);
        } else {  
        	
				holder = (ViewHolder) convertView.getTag();
			
        }  
        holder.rv.setImageBitmap((Bitmap)heads.get(position).getimg());
        holder.rv.setidandtype(heads.get(position).getid(), 1);
        return convertView;  
    }

    static class ViewHolder {		
		 RoundImageView rv;		
		}


}  
