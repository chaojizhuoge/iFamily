package com.example.ifamily.mywidget;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.ifamily.activity.Login;
import com.example.ifamily.activity.Privatemessage;
import com.example.ifamily.message.HeadM;

public class ClickspanIntent extends ClickableSpan implements ParcelableSpan {  
    private Intent it; 
    private int type;
    private HeadM names;
    
    public ClickspanIntent(int type,HeadM names) {  
        this.type=type;
        this.names=names;
    }       
    @Override  
    public void onClick(View sourceView) {  
        Context context = sourceView.getContext(); 
        switch(type)
		{
		case 1:
		it=new Intent(context,Privatemessage.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
		it.putExtra("privateAcct", names.getid());
		/////‘⁄’‚¿ÔÃÓ–¥id
		break;
		case 2:
			it=new Intent(context,Login.class);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
			context.startActivity(it);
			break;
			default:break;
		}
        context.startActivity(it);  
    }  
      
    @Override  
    public int getSpanTypeId() {  
        return 100;  
    }  
      
    @Override  
    public int describeContents() {  
        return 0;  
    }  
      
    public Intent getIntent() {  
        return it;  
    }

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}  
}  
