package com.example.ifamily.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupDBHelper extends SQLiteOpenHelper{
	public static final String GROUP_DBNAME = "group.db";

	public GroupDBHelper(Context context)
	{
		super(context, GROUP_DBNAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE table IF NOT EXISTS group"
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, groupId TEXT, groupName TEXT, img TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("ALTER TABLE user ADD COLUMN other TEXT");
	}

}
