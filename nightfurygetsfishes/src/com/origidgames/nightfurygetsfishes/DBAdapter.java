package com.origidgames.nightfurygetsfishes;

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
	private static final String TAG = "DBAdapter";
	
	private static final String DATABASE_NAME = "MyDB";
	private static final String DATABASE_TABLE = "questions";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE =
			"CREATE TABLE IF NOT EXISTS questions (_id INTEGER PRIMARY KEY autoincrement, " 
			+ "question TEXT not null, answer1 TEXT not null, answer2 TEXT not null, "
			+ "answer3 TEXT not null, answer4 TEXT not null, answer_right NUMERIC not null);";
	
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
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG,"Upgrading database from version "+ oldVersion + " to " + newVersion + ", which will destroy all old data"); 
			db.execSQL("DROP TABLE contacts");
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
	
	public long insertContact(String question, String answer1, String answer2, String answer3, String answer4,
			int answer_right) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_Q, question);
		initialValues.put(KEY_A1, answer1);
		initialValues.put(KEY_A2, answer2);
		initialValues.put(KEY_A3, answer3);
		initialValues.put(KEY_A4, answer4);
		initialValues.put(KEY_Aright, answer_right);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean deleteContact(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null)>0;
	}
	
	public Cursor getAllContacts() {
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_Q,KEY_A1,KEY_A2,KEY_A3,KEY_A4,KEY_Aright}, null, null, null, null, null);
	}
	
	public void deleteAllContacts() {
		db.execSQL("DROP TABLE contacts");
	}
	
	public Cursor getContact(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,KEY_Q,KEY_A1,KEY_A2,KEY_A3,KEY_A4,KEY_Aright}, KEY_ROWID+"="+rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean updateContact(long rowId, String question, String answer1, String answer2, String answer3, String answer4,
			int answer_right) {
		ContentValues args = new ContentValues();
		args.put(KEY_Q, question);
		args.put(KEY_A1, answer1);
		args.put(KEY_A2, answer2);
		args.put(KEY_A3, answer3);
		args.put(KEY_A4, answer4);
		args.put(KEY_Aright, answer_right);
		return db.update(DATABASE_TABLE, args, KEY_ROWID+"="+rowId, null)>0;
	}
}