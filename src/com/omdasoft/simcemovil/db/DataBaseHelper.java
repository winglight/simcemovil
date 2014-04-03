package com.omdasoft.simcemovil.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 用途：建表
 * @author sunny
 */
public class DataBaseHelper extends SQLiteOpenHelper{
	public static final String dbName="questions";
	public static final int versionCode=2; 
	
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}
	
	public DataBaseHelper(Context context) {
		super(context, dbName, null, versionCode);
	}
	
	public static String QUESTION_TABLE_NAME="QUESTIONS";
	public static String ID="ID";
	public static String QUESTION_NO="QUESTION_NO";
	public static String QUESTION_TITLE="QUESTION_TITLE";
	public static String OPTIONS="OPTIONS";
	public static String SELECT_ANSWER="SELECT_ANSWER";
	public static String CORRECT_ANSWER="CORRECT_ANSWER";
	public static String EXPLAINATION="EXPLAINATION";
	public static String QUESTION_IMAGE="QUESTION_IMAGE";
	public static String TYPE_NAME="TYPE_NAME";
	public static String TYPE_CODE="TYPE_CODE";
	public static String LEVEL="LEVEL";
	
	public static String QUESTIONS_TABLE_CREATE="CREATE TABLE "+QUESTION_TABLE_NAME+" ("+ID+" Integer primary key autoincrement,"+
			QUESTION_NO+" integer,"+QUESTION_TITLE+" text,"+OPTIONS+" text,"+SELECT_ANSWER+" integer,"+CORRECT_ANSWER+" integer,"+
			EXPLAINATION+" text,"+QUESTION_IMAGE+" text,"+
			TYPE_NAME+" text,"+TYPE_CODE+" text,"+LEVEL+" text)";
	
	public static String ACCOUNT_TABLE_NAME="ACCOUNT";
	public static String EMAIL="EMAIL";
	public static String RUT="RUT";
	public static String REGION="REGION";
	public static String CIUDAD="CIUDAD";
	public static String COLEGIO="COLEGIO";
	public static String ANDROID_SID="ANDROID_SID";
	
	public static String ACCOUNT_TABLE_CREATE="CREATE TABLE "+ACCOUNT_TABLE_NAME+" ("+ID+" Integer primary key autoincrement,"+
			EMAIL+" text,"+RUT+" text,"+REGION+" text,"+CIUDAD+" text,"+COLEGIO+" text,"+ANDROID_SID+" text)";
	
	public static String CODE_TABLE_NAME="CODE";
	public static String CODE_NO="CODE_NO";
	public static String CODE_KEY="CODE_KEY";
	public static String CODE_VALUE="CODE_VALUE";
	public static String TYPE="TYPE";
	
	public static String CODE_TABLE_CREATE="CREATE TABLE "+CODE_TABLE_NAME+" ("+ID+" Integer primary key autoincrement,"+
			CODE_NO+" Integer,"+CODE_KEY+" text,"+CODE_VALUE+" text,"+TYPE+" text)";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(QUESTIONS_TABLE_CREATE);
		db.execSQL(ACCOUNT_TABLE_CREATE);
		db.execSQL(CODE_TABLE_CREATE);
//		ContentValues values=new ContentValues();
//		values.put(QUESTION_NO, 1);
//		values.put(QUESTION_TITLE, "正常人有多少个手指？");
//		values.put(ANSWER1, "5个");
//		values.put(ANSWER2, "6个");
//		values.put(ANSWER3, "7个");
//		values.put(ANSWER4, "8个");
//		values.put(CORRECT_ANSWER, 4);
//		values.put(EXPLAINATION, "这只是测试题而已，不必认真");
//		db.insert(QUESTION_TABLE_NAME, null, values);
//		values.clear();
//		values.put(QUESTION_NO, 3);
//		values.put(QUESTION_TITLE, "一个月有多少天？");
//		values.put(ANSWER1, "28");
//		values.put(ANSWER2, "29");
//		values.put(ANSWER3, "30");
//		values.put(ANSWER4, "31");
//		values.put(CORRECT_ANSWER, 3);
//		values.put(EXPLAINATION, "其实都对");
//		db.insert(QUESTION_TABLE_NAME, null, values);
//		values.clear();
//		values.put(QUESTION_NO, 2);
//		values.put(QUESTION_TITLE, "一年有多少天？");
//		values.put(ANSWER1, "364");
//		values.put(ANSWER2, "365");
//		values.put(ANSWER3, "366");
//		values.put(ANSWER4, "367");
//		values.put(CORRECT_ANSWER, 2);
//		values.put(EXPLAINATION, "这只是测试题而已，不必认真");
//		db.insert(QUESTION_TABLE_NAME, null, values);
//		values.clear();
//		values.put(QUESTION_NO, 4);
//		values.put(QUESTION_TITLE, "正常人有多少个脚指？");
//		values.put(ANSWER1, "5个");
//		values.put(ANSWER2, "6个");
//		values.put(ANSWER3, "7个");
//		values.put(ANSWER4, "8个");
//		values.put(CORRECT_ANSWER, 4);
//		values.put(EXPLAINATION, "这只是测试题而已，不必认真");
//		db.insert(QUESTION_TABLE_NAME, null, values);
//		values.clear();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CODE_TABLE_NAME);
		onCreate(db);
	}
	
}