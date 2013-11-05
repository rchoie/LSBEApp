package com.yahoo.lsbeapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "bookmarks";
	public static final String ID = "id";
	public static final String GID = "gid";
	public static final String TITLE = "title";
	public static final String STREET = "street";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String PHONE = "phone";
	public static final String RATING = "rating";
	public static final String IMAGE = "image";
	public static final String TIMESTAMP = "timestamp";
	private static final String DATABASE_NAME = "listings.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	public static final String DATABASE_CREATE = "create table " +
		      TABLE_NAME + "(" + 
		      ID + " integer primary key, " + 
		      GID + " text, " +
		      TITLE + " text, " +
		      STREET + " text, " +
		      CITY + " text, " +
		      STATE + " text, " +
		      LAT + " text, " +
		      LON + " text, " +
		      PHONE + " text, " +
		      IMAGE + " text, " +
		      RATING + " text, " +
		      TIMESTAMP + " datetime default current_timestamp" + 
		      ");";
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}
	
}