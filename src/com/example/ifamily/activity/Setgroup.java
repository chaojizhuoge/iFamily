package com.example.ifamily.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifamily.R;
import com.example.ifamily.activity.Login.LoginThread;
import com.example.ifamily.mywidget.RoundImageView;
import com.example.ifamily.utils.FontManager;
import com.example.ifamily.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Setgroup extends Activity implements OnClickListener{

	private static final int PHOTO_REQUEST_CAMERA = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���

	private RoundImageView mFace;
	private Bitmap bitmap;
	private TextView title;
	private Button back;
	/* ͷ������ */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	
	SelectPicPopupWindow menuWindow;
	
	
	private SharedPreferences sp;
	private Button bt_setG,bt_seleGP;
	private EditText groupname;
	private EditText groupInfo;
	private boolean hasphoto;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.p_setgroup);
		hasphoto = false;
		
		
		title = (TextView)findViewById(R.id.title);
		 back = (Button)findViewById(R.id.back);
		 title.setText("������ͥ");
		 back.setOnClickListener(new OnClickListener()
		 {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(OldObjectMain.this,Iguide.class);
				 startActivity(intent);
				 OldObjectMain.this.finish();*/
				Setgroup.this.finish();
			}
			 
		 });
		bt_setG = (Button)findViewById(R.id.bt_setgroup);
		bt_setG.setOnClickListener(this);
		bt_seleGP = (Button)findViewById(R.id.bt_selectgrouppic);
		bt_seleGP.setOnClickListener(this);
		groupname = (EditText)findViewById(R.id.et_groupname);
		mFace = (RoundImageView)findViewById(R.id.iv_grouppic);
		groupInfo = (EditText)findViewById(R.id.et_groupinfo);
		LinearLayout all=(LinearLayout)findViewById(R.id.main);//��������
		FontManager.changeFonts(all,this);
	}
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.bt_setgroup:
			if(!hasphoto)
				Toast.makeText(getApplicationContext(), "��δ�ϴ�ͼƬ���Ժ����ڼ�ͥ��Ϣ�м����ͥͷ��~", Toast.LENGTH_SHORT).show();
			if(!groupname.getText().toString().equals(""))
				upload(arg0);
			else
			{
				Toast.makeText(getApplicationContext(), "�����봴���ļ�ͥ���ƣ�", Toast.LENGTH_SHORT).show();

			}

			break;
		case R.id.bt_selectgrouppic:
			menuWindow = new SelectPicPopupWindow(Setgroup.this, this);
			//��ʾ����
			menuWindow.showAtLocation(Setgroup.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
			break;
		case R.id.btn_take_photo:
			camera(arg0);
			break;
		case R.id.btn_pick_photo:
			gallery(arg0);
			break;
		}
	}
	
	public void upload(View view) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RequestParams params = new RequestParams();
			if(hasphoto)
			{
				
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				byte[] buffer = out.toByteArray();

				byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
				String photo = new String(encode);

				
				params.put("photo", photo);
			}
			sp = getSharedPreferences("user",0);
			String username = sp.getString("username", "");
			params.put("gname", groupname.getText().toString());
			params.put("uname", username);
			params.put("gdetail", groupInfo.getText().toString());
			params.put("hasphoto", String.valueOf(hasphoto));
			String url = "http://103.31.241.201:8080/IFamilyServer/SetGroupServlet";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {
							InputStream is = new ByteArrayInputStream(responseBody);
							ObjectInputStream ois = new ObjectInputStream(is);
							Map map = (Map<String,Object>)ois.readObject();
							
							int groupId = (Integer)map.get("groupId");

							Toast.makeText(Setgroup.this, "��ͥ�ɹ�������", Toast.LENGTH_SHORT)
									.show();
							Intent intent = new Intent(Setgroup.this,EnterGroupAddF.class);
							intent.putExtra("pri", true);
							intent.putExtra("groupId", groupId);
							startActivity(intent);
						} else {
							Toast.makeText(Setgroup.this,
									"��������쳣�������룺" + statusCode, Toast.LENGTH_SHORT).show();

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					Toast.makeText(Setgroup.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * ������ȡ
	 */
	public void gallery(View view) {
		// ����ϵͳͼ�⣬ѡ��һ��ͼƬ
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/*
	 * �������ȡ
	 */
	public void camera(View view) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// �жϴ洢���Ƿ�����ã����ý��д洢
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// �õ�ͼƬ��ȫ·��
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(Setgroup.this, "δ�ҵ��洢�����޷��洢��Ƭ��", Toast.LENGTH_SHORT).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				hasphoto = true;
				bitmap = data.getParcelableExtra("data");
				mFace.setImageBitmap(bitmap);
				if(tempFile != null)
				{boolean delete = tempFile.delete();
				System.out.println("delete = " + delete);
				}menuWindow.dismiss();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����ͼƬ
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	private void crop(Uri uri) {
		// �ü�ͼƬ��ͼ
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// �ü���ı�����1��1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// �ü������ͼƬ�ĳߴ��С
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// ͼƬ��ʽ
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// ȡ������ʶ��
		intent.putExtra("return-data", true);// true:������uri��false������uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}