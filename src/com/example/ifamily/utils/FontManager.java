package com.example.ifamily.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.ifamily.mywidget.*;
public class FontManager {
	public static void changeFonts(ViewGroup root, Activity act) {  
	    
	       Typeface tf = Typeface.createFromAsset (act.getAssets() ,"fonts/FZMWFont.mp3");
	       for (int i = 0; i < root.getChildCount(); i++) {  
	           View v = root.getChildAt(i);  
	           if (v instanceof TextView) {  
	              ((TextView) v).setTypeface(tf);  
	           } else if (v instanceof Button) {  
	              ((Button) v).setTypeface(tf);  
	           } else if (v instanceof TextViewM) {  
		              ((TextViewM) v).setTypeface(tf);  
		           }
	           else if (v instanceof TextViewNameList) {  
		              ((TextViewNameList) v).setTypeface(tf);  
		           }
	           else if (v instanceof EditText) {  
	              ((EditText) v).setTypeface(tf);  
	           } else if (v instanceof ViewGroup) {  
	              changeFonts((ViewGroup) v, act);  
	           }  
	       }  
	    
	    }  
	public static void changeFonts(ViewGroup root, Context act) {  
	    
	       Typeface tf = Typeface.createFromAsset(act.getAssets(),  
	              "fonts/FZMWFont.ttf");  
	    
	       for (int i = 0; i < root.getChildCount(); i++) {  
	           View v = root.getChildAt(i);  
	           if (v instanceof TextView) {  
	              ((TextView) v).setTypeface(tf);  
	           } else if (v instanceof Button) {  
	              ((Button) v).setTypeface(tf);  
	           } else if (v instanceof EditText) {  
	              ((EditText) v).setTypeface(tf);  
	           } else if (v instanceof ViewGroup) {  
	              changeFonts((ViewGroup) v, act);  
	           }  
	       }  
	    
	    }
}
