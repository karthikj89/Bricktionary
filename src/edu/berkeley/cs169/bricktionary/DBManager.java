package edu.berkeley.cs169.bricktionary;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager {
	Context context;
	private SQLiteDatabase db;
	private static final String DATABASE_NAME = "bricktionary.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME_USER = "user";
	private static final String COL_NAME_ID = "uid";
	private static final String COL_NAME_NAME= "name";
	private static final String COL_NAME_SCORE = "score";
	
	public DBManager(Context context)
	{
		this.context = context;
		DBHelper helper = new DBHelper(context);
		this.db = helper.getWritableDatabase();
		//reference http://developer.android.com/guide/topics/data/data-storage.html#db
	}
	
	private class DBHelper extends SQLiteOpenHelper
	{

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME_USER + " ("
	                   + COL_NAME_ID + " INTEGER PRIMARY KEY,"
	                   + COL_NAME_NAME + " TEXT,"
	                   + COL_NAME_SCORE + " INTEGER"
	                   + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//needed only to upgrade to database
		}
	    
	}
	
	/**
     * Gets userID of user with given username
     * returns userID for user if successful
     * -1 otherwise
     */
	public long getUserID(String username){
		long userID;
		try {
			Cursor c = db.query
			(
					TABLE_NAME_USER,
					new String[] { COL_NAME_ID }, // The columns to return from the query
					COL_NAME_NAME + "=?", // The columns for the where clause
					new String[] {username} //The arguments for the where clause
					, null, null, null, null
			);
			if (c.getCount()<=0)
				userID = -1;	//return -1 if user doesn't exist
			else {
				c.moveToFirst();
				userID = c.getLong(0);
			}
			c.close();
		
		} catch(Exception e){
			e.printStackTrace();
			userID = -1;
		}
		return userID;
	}
	
	/**
     * Adds user with given username and initial score
     * returns userID for user if successful
     * -1 otherwise
     */
	public long addUser(String username, int score){
		if (username.length() <=0) // check username isn't just empty string
			return -1;
		long userID = getUserID(username);
		if (userID != -1) {
			updateScore(userID, score); // if user with username exists already, just update score
			return userID; //return userID of existing user
		}
		ContentValues values = new ContentValues();
		values.put(COL_NAME_NAME, username);
		values.put(COL_NAME_SCORE, score);
		try {
			return db.insert(TABLE_NAME_USER, null, values);
		} catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
     * Deletes user with given userID
     * returns true if exists
     * false otherwise
     */
	public boolean deleteUser(long userID)
	{
		try {
			return db.delete(TABLE_NAME_USER, COL_NAME_ID + "=" + userID, null) > 0;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
     * Deletes all users
     * returns true if successful
     * false otherwise
     */
	public boolean deleteAll()
	{
		try {
			return db.delete(TABLE_NAME_USER, null, null) > 0;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
     * Update score for user with given userID
     * returns true if exists
     * false otherwise
     */
	public boolean updateScore(long userID, int score)
	{
		ContentValues values = new ContentValues();
		values.put(COL_NAME_SCORE, score);
		try {
			return db.update(TABLE_NAME_USER, values, COL_NAME_ID + "=" + userID, null) > 0;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public int getScore(long userID)
	{
		int score;
		try {
			Cursor c = db.query
			(
				TABLE_NAME_USER,
				new String[] { COL_NAME_SCORE }, // The columns to return from the query
				COL_NAME_ID + "=" + userID, // The columns for the where clause
				null, null, null, null, null
			);
			if (c.getCount()<=0)
				score = 0;	//return 0 if user doesn't exist
			else {
				c.moveToFirst();
				score = c.getInt(0);
			}
			c.close();
			return score;
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public Cursor getScores(){
		try {
			return db.query
			(
				TABLE_NAME_USER,
				new String[] { COL_NAME_NAME, COL_NAME_SCORE }, // The columns to return from the query,
				null, null, null, null, null, null
			);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
