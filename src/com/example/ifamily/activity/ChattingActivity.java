package com.example.ifamily.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.adapter.ChattingAdapter;
import com.example.ifamily.message.ChatMessage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChattingActivity extends Activity  {

	private ChattingAdapter chatHistoryAdapter;
	 private List<ChatMessage> messages = new ArrayList<ChatMessage>();
	 private ListView chatHistoryLv;
	 private Button sendBtn;
	 private EditText textEditor;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.chattingmain);

		init();
		}
		
		 public void init(){  
			 
			 chatHistoryLv = (ListView) findViewById(R.id.Chattinglist);
			 sendBtn = (Button)findViewById(R.id.ChattingButton);
		     textEditor = (EditText) findViewById(R.id.ChattingText); 
		     sendBtn.setOnClickListener(l);	
		     setAdapterForThis();
		 }


		 private void setAdapterForThis() {
			  initMessages();
			  chatHistoryAdapter = new ChattingAdapter(this.getApplicationContext(), messages);
			  chatHistoryLv.setAdapter(chatHistoryAdapter);
			 }
		 
		 private void initMessages() {
			  messages.add(new ChatMessage(ChatMessage.MESSAGE_FROM, "hello"));
			  messages.add(new ChatMessage(ChatMessage.MESSAGE_TO, "hello"));
			  messages.add(new ChatMessage(ChatMessage.MESSAGE_FROM, "你好吗？"));
			  messages.add(new ChatMessage(ChatMessage.MESSAGE_TO, "非常好!"));
			  messages.add(new ChatMessage(ChatMessage.MESSAGE_FROM, "欢迎光临我的博客，http://hi.csdn.net/lyfi01"));
			  messages.add(new ChatMessage(ChatMessage.MESSAGE_TO, "恩，好的，谢谢"));
			 }
		 
		 private View.OnClickListener l = new View.OnClickListener() {
			  public void onClick(View v) {
			   if (v.getId() == sendBtn.getId()) {
			    String str = textEditor.getText().toString();
			    String sendStr;
			    if (str != null && (sendStr = str.trim().replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "").replaceAll("\f", "")) != "")
			    {
			     sendMessage(sendStr);
			    }
			    textEditor.setText("");
			   }
			  }
		 };
			  
		 private void sendMessage(String sendStr) {
			   messages.add(new ChatMessage(ChatMessage.MESSAGE_TO, sendStr));
			   chatHistoryAdapter.notifyDataSetChanged();
			  }



		
		
}
