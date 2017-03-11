package com.example.ifamily;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;


public class NLPullRefreshView extends LinearLayout {
	private static final String TAG = "LILITH";

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	enum Status {
		NORMAL, DRAGGING, REFRESHING,
	}

	private Status status = Status.NORMAL;

	private final static String REFRESH_RELEASE_TEXT = "那你呢";
	private final static String REFRESH_DOWN_TEXT = "涓";
	private final static float MIN_MOVE_DISTANCE = 5.0f;

	private Scroller scroller;
	private View refreshView;
	private ImageView refreshIndicatorView;
	private int refreshTargetTop = -50;
	private ProgressBar bar;
	private TextView downTextView;
	private TextView timeTextView;

	private RefreshListener refreshListener;

	// ���瑕���ㄥ�扮�����瀛�寮����
	private String downCanRefreshText;
	private String releaseCanRefreshText;

	private String refreshTime ;
	private int lastY;
	private Context mContext;

	public NLPullRefreshView(Context context) {
		super(context);
		mContext = context;
	}

	public NLPullRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}


	private void init() {
		// TODO Auto-generated method stub
		refreshTargetTop = getPixelByDip(mContext, refreshTargetTop);
		
		scroller = new Scroller(mContext);
		
		refreshView = LayoutInflater.from(mContext).inflate(
				R.layout.refresh_top_item, null);
		
		refreshIndicatorView = (ImageView) refreshView
				.findViewById(R.id.indicator);
	
		bar = (ProgressBar) refreshView.findViewById(R.id.progress);
		
		downTextView = (TextView) refreshView.findViewById(R.id.refresh_hint);
	
		timeTextView = (TextView) refreshView.findViewById(R.id.refresh_time);
		LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, -refreshTargetTop);
		lp.topMargin = refreshTargetTop;
		lp.gravity = Gravity.CENTER;
		addView(refreshView, lp);
	
		downCanRefreshText = REFRESH_DOWN_TEXT;
		releaseCanRefreshText = REFRESH_RELEASE_TEXT;
		refreshTime = "1989-12-24 12:12:12";
		if (refreshTime != null) {
			setRefreshTime(refreshTime);
		}
	}

	
	private void setRefreshText(String time) {
		Log.i(TAG, "------>setRefreshText");
		timeTextView.setText(time);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (status == Status.REFRESHING)
			return false;
		
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "MotionEvent.ACTION_DOWN");
			
			lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "MotionEvent.ACTION_MOVE");
			
			int m = y - lastY;
			doMovement(m);
		
			this.lastY = y;
			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "MotionEvent.ACTION_UP");
			fling();
			break;
		}
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		
		
		int action = e.getAction();
		int y = (int) e.getRawY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
		
			int m = y - lastY;
			
			this.lastY = y;
			if (m > MIN_MOVE_DISTANCE && canScroll()) {
				Log.i(TAG, "canScroll");
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	
	private void fling() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LayoutParams) refreshView
				.getLayoutParams();

		if (lp.topMargin > 0) {
			status = Status.REFRESHING;
			Log.i(TAG, "fling ----->REFRESHING");
			refresh();
		} else {
			Log.i(TAG, "fling ----->NORMAL");
			status = Status.NORMAL;
			returnInitState();
		}
	}

	private void returnInitState() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		Log.i(TAG, "returnInitState top = "+i);
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	}


	private void refresh() {
		// TODO Auto-generated method stub
		Log.i(TAG, " ---> refresh()");
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		refreshIndicatorView.setVisibility(View.GONE);
		bar.setVisibility(View.VISIBLE);
		downTextView.setVisibility(View.GONE);
		scroller.startScroll(0, i, 0, 0 - i);
		invalidate();
		if (refreshListener != null) {
			refreshListener.onRefresh(this);
		}
	}

	/**
	 * 
	 */
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			Log.i(TAG, "----->computeScroll()");
			int i = this.scroller.getCurrY();
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
					.getLayoutParams();
			int k = Math.max(i, refreshTargetTop);
			lp.topMargin = k;
			this.refreshView.setLayoutParams(lp);
			postInvalidate();
		}
	}

	/**
	 * 涓����move浜�浠跺�����
	 * 
	 * @param moveY
	 */
	private void doMovement(int moveY) {
		status = Status.DRAGGING;
		LinearLayout.LayoutParams lp = (LayoutParams) refreshView
				.getLayoutParams();
		float f1 = lp.topMargin;
		float f2 = moveY * 0.3F;
		int i = (int) (f1 + f2);
		
		lp.topMargin = i;
		
		refreshView.setLayoutParams(lp);
		refreshView.invalidate();
		invalidate();

		timeTextView.setVisibility(View.VISIBLE);
		downTextView.setVisibility(View.VISIBLE);
		refreshIndicatorView.setVisibility(View.VISIBLE);
		bar.setVisibility(View.GONE);
		if (lp.topMargin > 0) {
			downTextView.setText(releaseCanRefreshText);
			refreshIndicatorView.setImageResource(R.drawable.refresh_arrow_up);
		} else {
			downTextView.setText(downCanRefreshText);
			refreshIndicatorView
					.setImageResource(R.drawable.refresh_arrow_down);
		}

	}

	
	public void setRefreshTime(String refreshTime){
		timeTextView.setText("浏览:"+refreshTime);
	}

	
	public void setRefreshListener(RefreshListener listener) {
		this.refreshListener = listener;
	}

	
	private void refreshTimeBySystem() {
		String dateStr = dateFormat.format(new Date());
		this.setRefreshText("存颁:"+dateStr);
		
	}

	
	public void finishRefresh() {
		Log.i(TAG, "------->finishRefresh()");
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		refreshIndicatorView.setVisibility(View.VISIBLE);
		timeTextView.setVisibility(View.VISIBLE);
		downTextView.setVisibility(VISIBLE);
		refreshTimeBySystem();
		bar.setVisibility(GONE);
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
		status = Status.NORMAL;
	}

	
	private boolean canScroll() {
		// TODO Auto-generated method stub
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView instanceof ListView) {
				int top = ((ListView) childView).getChildAt(0).getTop();
				int pad = ((ListView) childView).getListPaddingTop();
				if ((Math.abs(top - pad)) < 3
						&& ((ListView) childView).getFirstVisiblePosition() == 0) {
					return true;
				} else {
					return false;
				}
			} else if (childView instanceof ScrollView) {
				if (((ScrollView) childView).getScrollY() == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	public static int getPixelByDip(Context c, int pix) {
		float f1 = c.getResources().getDisplayMetrics().density;
		float f2 = pix;
		return (int) (f1 * f2 + 0.5F);
	}

	
	public interface RefreshListener {
		public void onRefresh(NLPullRefreshView view);
	}
	
	
}
