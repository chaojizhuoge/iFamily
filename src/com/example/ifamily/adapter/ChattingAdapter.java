package com.example.ifamily.adapter;

import java.util.List;

import com.example.ifamily.R;
import com.example.ifamily.message.ChatMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChattingAdapter extends BaseAdapter{
	protected static final String TAG = "ChattingAdapter";
private Context context;
private List<ChatMessage> chatMessages;
public ChattingAdapter(Context context, List<ChatMessage> messages) {
 super();
 this.context = context;
 this.chatMessages = messages;
}
public int getCount() {
 return chatMessages.size();
}
public Object getItem(int position) {
 return chatMessages.get(position);
}
public long getItemId(int position) {
 return position;
}
public View getView(int position, View convertView, ViewGroup parent) {
 ViewHolder holder = null;
 ChatMessage message = chatMessages.get(position);
 if (convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != message.getDirection()) 
 {
  holder = new ViewHolder();
  if (message.getDirection() == ChatMessage.MESSAGE_FROM) {
   holder.flag = ChatMessage.MESSAGE_FROM;
   convertView = LayoutInflater.from(context).inflate(R.layout.list_say_he_item, null);
  } else {
   holder.flag = ChatMessage.MESSAGE_TO;
   convertView = LayoutInflater.from(context).inflate(R.layout.list_say_me_item, null);
  }
  holder.text = (TextView) convertView.findViewById(R.id.messagedetail_row_text);
  convertView.setTag(holder);
 }
 holder.text.setText(message.getContent());
 return convertView;
}
//ÓÅ»¯listviewµÄAdapter
static class ViewHolder {
 TextView text;
 int flag;
}
}
