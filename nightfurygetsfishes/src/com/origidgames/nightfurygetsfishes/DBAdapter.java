/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_Q = "question";
	public static final String KEY_A1 = "answer1";
	public static final String KEY_A2 = "answer2";
	public static final String KEY_A3 = "answer3";
	public static final String KEY_A4 = "answer4";
	public static final String KEY_Aright = "answer_right";
	public static final String KEY_NAME = "name";
	public static final String KEY_TIME = "time";
	private static final String TAG = "DBAdapter";
	
	private static final String DATABASE_NAME = "MyDB";
	private static final String DATABASE_TABLE1 = "questions";
	private static final String DATABASE_TABLE2 = "highscores_easy";
	private static final String DATABASE_TABLE3 = "highscores_normal";
	private static final String DATABASE_TABLE4 = "highscores_hard";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE1 =
			"CREATE TABLE IF NOT EXISTS questions (_id INTEGER PRIMARY KEY autoincrement, " 
			+ "question TEXT not null, answer1 TEXT not null, answer2 TEXT not null, "
			+ "answer3 TEXT not null, answer4 TEXT not null, answer_right NUMERIC not null);";
	
	private static final String DATABASE_CREATE2 =
			"CREATE TABLE IF NOT EXISTS highscores_easy (_id INTEGER PRIMARY KEY autoincrement, "
			+ "name TEXT not null, time NUMERIC not null);";
	private static final String DATABASE_CREATE3 =
			"CREATE TABLE IF NOT EXISTS highscores_normal (_id INTEGER PRIMARY KEY autoincrement, "
			+ "name TEXT not null, time NUMERIC not null);";
	private static final String DATABASE_CREATE4 =
			"CREATE TABLE IF NOT EXISTS highscores_hard (_id INTEGER PRIMARY KEY autoincrement, "
			+ "name TEXT not null, time NUMERIC not null);";
	private final Context context;
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE1);
			db.execSQL(DATABASE_CREATE2);
			db.execSQL(DATABASE_CREATE3);
			db.execSQL(DATABASE_CREATE4);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG,"Upgrading database from version "+ oldVersion + " to " + newVersion + ", which will destroy all old data"); 
			db.execSQL("DROP TABLE questions");
			db.execSQL("DROP TABLE highscores_easy");
			db.execSQL("DROP TABLE highscores_normal");
			db.execSQL("DROP TABLE highscores_hard");
			onCreate(db);
		}
	}
	
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		DBHelper.close();
	}
	
	public long insertQuestion(String question, String answer1, String answer2, String answer3, String answer4,
			int answer_right) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_Q, question);
		initialValues.put(KEY_A1, answer1);
		initialValues.put(KEY_A2, answer2);
		initialValues.put(KEY_A3, answer3);
		initialValues.put(KEY_A4, answer4);
		initialValues.put(KEY_Aright, answer_right);
		return db.insert(DATABASE_TABLE1, null, initialValues);
	}
	
	/**
	 * Insert a new record into Highscore table.
	 * @param name user name
	 * @param score user's score
	 * @return True if user name is existed in the table 
	 */
	public boolean InsertHighscore(GameMode mode, String name, float score)
	{
		/* Table name corresponds to Game mode */
		String sTblName = null;
		switch (mode) {
			case NORMAL:
				sTblName = DATABASE_TABLE3;
				break;
			case EASY:
				sTblName = DATABASE_TABLE2;
				break;
			case HARD:
				sTblName = DATABASE_TABLE4;
				break;
		}
		
		/* Build content values */
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_TIME, Float.toString(score));
		
		/* Find whether name is existed in the table */
		Cursor c = db.query(sTblName, new String[]{KEY_NAME},
				KEY_NAME + "=\"" + name + "\"" ,
				null, null, null, null);
		if (c != null && c.getCount() == 0) {			
			db.insert(sTblName, null, cv);
			return true;
		}
		db.update(sTblName, cv, KEY_NAME + "=\"" + name + "\"", null);
		return false;
	}
	
	/**
	 * Check whether name's score is better than score in table
	 * @param name user name
	 * @param score user's score
	 * @return True if [name] attached with [score] is higher than the latest score in table
	 */
	public boolean IsHighScore(GameMode mode, String name, float score)
	{
		/* Table name corresponds to Game mode */
		String sTblName = null;
		switch (mode) {
			case NORMAL:
				sTblName = DATABASE_TABLE3;
				break;
			case EASY:
				sTblName = DATABASE_TABLE2;
				break;
			case HARD:
				sTblName = DATABASE_TABLE4;
				break;
		}
		
		/* Gets current user's score */
		Cursor c = db.query(sTblName,
					new String[]{KEY_TIME},
					KEY_NAME + "=\"" + name + "\"" ,
					null, null, null, null);
		if (c != null && c.getCount() != 0) {
			c.moveToFirst();
			float fCurrentTime = Float.parseFloat(c.getString(0)); /* 0 is the first column (KEY_TIME) */
			if (fCurrentTime < score) {
				return true;
			}
		}
		return false;
	}
	
	public boolean deleteQuestion(long rowId) {
		return db.delete(DATABASE_TABLE1, KEY_ROWID + "=" + rowId, null)>0;
	}
	
	public Cursor getAllQuestions() {
		return db.query(DATABASE_TABLE1, new String[] {KEY_ROWID,KEY_Q,KEY_A1,KEY_A2,KEY_A3,KEY_A4,KEY_Aright}, null, null, null, null, null);
	}
	
	public void deleteAllQuestions() {
		db.execSQL("DROP TABLE questions");
	}
	
	public Cursor getAllEasy() {
		return db.query(DATABASE_TABLE2,new String[]{KEY_ROWID,KEY_NAME,KEY_TIME}, null, null, null, null, null);
	}
	
	public Cursor getAllNormal() {
		return db.query(DATABASE_TABLE3,new String[]{KEY_ROWID,KEY_NAME,KEY_TIME}, null, null, null, null, null);
	}
	
	public Cursor getAllHard() {
		return db.query(DATABASE_TABLE4,new String[]{KEY_ROWID,KEY_NAME,KEY_TIME}, null, null, null, null, null);
	}
	
	public Cursor getHighscoreEasy(long rowId)  {
		Cursor mCursor = db.query(true, DATABASE_TABLE2, new String[]{KEY_ROWID,KEY_NAME,KEY_TIME}, KEY_ROWID+"="+rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getHighscoreNormal(long rowId)  {
		Cursor mCursor = db.query(true, DATABASE_TABLE3, new String[]{KEY_ROWID,KEY_NAME,KEY_TIME}, KEY_ROWID+"="+rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getHighscoreHard(long rowId)  {
		Cursor mCursor = db.query(true, DATABASE_TABLE4, new String[]{KEY_ROWID,KEY_NAME,KEY_TIME}, KEY_ROWID+"="+rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getQuestion(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE1, new String[]{KEY_ROWID,KEY_Q,KEY_A1,KEY_A2,KEY_A3,KEY_A4,KEY_Aright}, KEY_ROWID+"="+rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean updateQuestion(long rowId, String question, String answer1, String answer2, String answer3, String answer4,
			int answer_right) {
		ContentValues args = new ContentValues();
		args.put(KEY_Q, question);
		args.put(KEY_A1, answer1);
		args.put(KEY_A2, answer2);
		args.put(KEY_A3, answer3);
		args.put(KEY_A4, answer4);
		args.put(KEY_Aright, answer_right);
		return db.update(DATABASE_TABLE1, args, KEY_ROWID+"="+rowId, null)>0;
	}

    public void CopyDB(InputStream inputStream,OutputStream outputStream) throws IOException {
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = inputStream.read(buffer))>0) {
    		outputStream.write(buffer,0,length);
    	}
    	inputStream.close();
    	outputStream.flush();
    	outputStream.close();
    }
}