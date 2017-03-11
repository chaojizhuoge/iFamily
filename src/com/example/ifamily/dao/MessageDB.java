package com.example.ifamily.dao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.ifamily.bean.ChatMessage;

public class MessageDB
{
	/**
	 * 常量
	 */
	private static final String COL_MESSAGE = "message";
	// 1：from ; 0:to
	private static final String COL_IS_COMING = "is_coming";
	private static final String COL_GROUP_ID = "group_id";
	private static final String COL_USER_ID = "user_id";
	private static final String COL_ICON = "icon";
	private static final String COL_NICKNAME = "nickname";
	// 1:readed ; 0 unreaded ;
	private static final String COL_READED = "readed";
	private static final String COL_DATE = "date";

	private static final String DB_NAME = "message.db";
	private SQLiteDatabase mDb;

	public MessageDB(Context context)
	{
		mDb = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
	}

	/**
	 * 为每个用户根据其userId创建一张消息表
	 * 
	 * @param userId
	 * @param chatMessage
	 */
	public void add(String groupId, ChatMessage chatMessage)
	{
		createTable(groupId);

		int isComing = chatMessage.isComing() ? 1 : 0;
		int readed = chatMessage.isReaded() ? 1 : 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		((Bitmap)chatMessage.getIcon()).compress(CompressFormat.PNG, 100, baos);
		
		mDb.execSQL(
				"insert into _" + groupId + " (" + COL_USER_ID + "," + COL_ICON
						+ "," + COL_IS_COMING + "," + COL_MESSAGE + ","
						+ COL_NICKNAME + "," + COL_READED + "," + COL_DATE+ "," + COL_GROUP_ID
						+ ") values(?,?,?,?,?,?,?,?)",
				new Object[] { chatMessage.getUserId(), baos.toByteArray(),
						isComing, chatMessage.getMessage(),
						chatMessage.getNickname(), readed,
						chatMessage.getDateStr() ,
						groupId});
	}

	public List<ChatMessage> find(String groupId, int currentPage, int pageSize)
	{
		List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
		createTable(groupId);
		int start = (currentPage - 1) * pageSize;
		int end = start + pageSize;
		// 取最后的10条
		String sql = "select * from _" + groupId + " order by _id  desc limit  "
				+ start + " , " + end;
		Cursor c = mDb.rawQuery(sql, null);
		ChatMessage chatMessage = null;
		while (c.moveToNext())
		{
			String dateStr = c.getString(c.getColumnIndex(COL_DATE));
			byte[] iconbyte = c.getBlob(c.getColumnIndex(COL_ICON));
			int isComingVal = c.getInt(c.getColumnIndex(COL_IS_COMING));
			String message = c.getString(c.getColumnIndex(COL_MESSAGE));
			String nickname = c.getString(c.getColumnIndex(COL_NICKNAME));
			int readedVal = c.getInt(c.getColumnIndex(COL_READED));
			String _userId = c.getString(c.getColumnIndex(COL_USER_ID));
			
			Bitmap icon = BitmapFactory.decodeByteArray(iconbyte, 0, iconbyte.length);

			chatMessage = new ChatMessage(message, isComingVal == 1, _userId,
					icon, nickname, readedVal == 1, dateStr,Integer.parseInt(groupId));

			chatMessages.add(chatMessage);
		}
		Collections.reverse(chatMessages);
		return chatMessages;
	}

	public Map<String, Integer> getUserUnReadMsgs(List<String> groupIds)
	{

		Map<String, Integer> userUnReadMsgs = new HashMap<String, Integer>();
		for (String groupId : groupIds)
		{
			int count = getUnreadedMsgsCountByUserId(groupId);
			userUnReadMsgs.put(groupId, count);
		}

		return userUnReadMsgs;
	}

	private int getUnreadedMsgsCountByUserId(String groupId)
	{
		createTable(groupId);
		String sql = "select count(*) as count from _" + groupId + " where "
				+ COL_IS_COMING + " = 1 and " + COL_READED + " = 0";
		Cursor c = mDb.rawQuery(sql, null);
		int count = 0;
		if (c.moveToNext())
			count = c.getInt(c.getColumnIndex("count"));
		Log.e("getUnreadedMsgsCountByUserId", groupId + " , " + count);
		c.close();
		return count;
	}

	/**
	 * 更新已读标志位
	 */
	public void updateReaded(String groupId)
	{
		createTable(groupId);
		mDb.execSQL("update  _" + groupId + " set " + COL_READED + " = 1 where "
				+ COL_READED + " = 0 ", new Object[] {});
	}

	private void createTable(String groupId)
	{
		mDb.execSQL("CREATE table IF NOT EXISTS _" + groupId
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " //
				+ COL_USER_ID + " TEXT, " //
				+ COL_ICON + " blob, "//
				+ COL_IS_COMING + " integer ,"//
				+ COL_MESSAGE + " text , " //
				+ COL_NICKNAME + " text , " //
				+ COL_DATE + " text , "//
				+ COL_READED + " integer , "
				+ COL_GROUP_ID + " text)");//
	}

	public void close()
	{
		if (mDb != null && mDb.isOpen())
			mDb.close();
	}

}
