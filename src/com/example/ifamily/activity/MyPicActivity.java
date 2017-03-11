
package com.example.ifamily.activity;


import com.example.ifamily.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
public class MyPicActivity extends Activity {



    public static final String TAG = "ImgDisplayActivity";
   private ImageView iv;

    public static final int NONE = 0;
    public static final int DRAG = 1;
    public static final int ZOOM = 2;

    private int mode = NONE;                                                                                                          
    private Matrix matrix; 
    private Matrix currMatrix; 
    private PointF starPoint;
    private PointF midPoint;
    private float startDistance;
	private Bitmap icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_picture);

        
        Intent intent = getIntent();
        if(intent!=null)
		{
        icon = intent.getParcelableExtra("icon");}
       iv = (ImageView) findViewById(R.id.iv);
       iv.setImageBitmap(icon);
        matrix = new Matrix(); 
       currMatrix = new Matrix();
       starPoint = new PointF();
       iv.setOnTouchListener(new ImageViewOnTouchListener());
    }
    final class ImageViewOnTouchListener implements OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:  
               currMatrix.set(matrix);
                starPoint.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: 
           
                startDistance = distance(event);
                Log.i(TAG, startDistance+"");
                if(startDistance > 5f) {  
                    mode = ZOOM;
                    currMatrix.set(matrix);
                    midPoint = getMidPoint(event); 
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mode == DRAG) { 
                    float dx =event.getX()- starPoint.x  ;
                    float dy = event.getY()-starPoint.y;
                    matrix.set(currMatrix);
                    matrix.postTranslate(dx, dy);
                } else if(mode == ZOOM) { 
             
                    float distance = distance(event);
                    if(distance > 5f) {
                        matrix.set(currMatrix);
                        float cale = distance / startDistance;
                      //matrix.preScale(cale, cale, midPoint.x, midPoint.y); 
                        matrix.preScale(cale, cale, iv.getX(), iv.getX());
                    }
                }
                break;
            case MotionEvent.ACTION_UP: 
            case MotionEvent.ACTION_POINTER_UP: 
               
                mode = NONE;
                break;
            default:
                break;
            }
            iv.setImageMatrix(matrix);
            
            return true;

        }

}
  

    private float distance(MotionEvent e) {
       float eX = e.getX(1) - e.getX(0);  
        float eY = e.getY(1) - e.getY(0);
        return FloatMath.sqrt(eX * eX + eY * eY);
    }




    private PointF getMidPoint(MotionEvent event) {
       float x = (event.getX(1) - event.getX(0)) / 2;
       float y = (event.getY(1) - event.getY(0)) / 2;
       return new PointF(x,y);
    }
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK ) { 
	    
	    		MyPicActivity.this.finish();
	    		return false;    	

	    }
	    else {return super.onKeyDown(keyCode, event);}
	}
	
    
    
    
}