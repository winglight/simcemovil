package com.omdasoft.simcemovil.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.omdasoft.simcemovil.db.DataBaseHelper;
import com.omdasoft.simcemovil.model.AccountModel;

/**
 * 用户信息表account表的dao类
 * @author sunny
 *
 */
public class UserDAO {
	private SQLiteDatabase db;//db对象
	private final Context context;//上下文对象
	private static UserDAO instance;//businessDao实例对象
	public DataBaseHelper sdbHelper;//helper类对象
	
	private UserDAO(Context c){
		this.context = c;
		this.sdbHelper=new DataBaseHelper(c);
	}
	
	/**
	 * 获取dao实例对象
	 * @param c 上下文对象
	 * @return dao实例对象
	 */
	public static UserDAO getInstance(Context c) {
		if (instance == null) {
			instance = new UserDAO(c);
		}
		return instance;
	}
	
	/**
	 * 打开数据库方法
	 * @throws SQLiteException
	 */
	public void openDB() throws SQLiteException {
		try {
			db = sdbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			Log.v("Open database exception caught", ex.getMessage());
			db = sdbHelper.getReadableDatabase();
		}
	}
	
	/**
	 * 关闭数据库方法
	 */
	public void closeDB() {
		if(db!=null){
			db.close();
		}	
	}
	
	//新增用户到本地db
	public void insertUser(AccountModel account){
		openDB();
		ContentValues newActValue = new ContentValues();
		newActValue.put(DataBaseHelper.EMAIL, account.getEmail());
		newActValue.put(DataBaseHelper.RUT, account.getRut());
		newActValue.put(DataBaseHelper.REGION, account.getRegion());
		newActValue.put(DataBaseHelper.CIUDAD, account.getCiudad());
		newActValue.put(DataBaseHelper.COLEGIO, account.getColegio());
		newActValue.put(DataBaseHelper.ANDROID_SID, account.getAndroid_sid());
		db.insert(DataBaseHelper.ACCOUNT_TABLE_NAME, null, newActValue);
		closeDB();
	}
	
	/**
	 * 判断用户是否已经注册过
	 * @return false 没有注册过  true已注册过
	 */
	public boolean checkRegisterState(){
		boolean result=false;
		openDB();
		Cursor cursor=db.query(DataBaseHelper.ACCOUNT_TABLE_NAME, new String[]{DataBaseHelper.ID},"", null, null, null, null);
		result=cursor.moveToNext();
		cursor.close();
		closeDB();
		return result;
	}
	
	/**
	 * 获取当前用户
	 * @return AccountModel
	 */
	public AccountModel getInfo(){
		AccountModel account=new AccountModel();
		
		openDB();
		Cursor cursor=db.query(DataBaseHelper.ACCOUNT_TABLE_NAME, new String[]{DataBaseHelper.ID,DataBaseHelper.EMAIL,
				DataBaseHelper.RUT,DataBaseHelper.REGION,DataBaseHelper.CIUDAD,DataBaseHelper.COLEGIO,DataBaseHelper.ANDROID_SID}
				, null, null, null, null, null);
		while(cursor.moveToNext()){
			account=changeCursorToAccountModel(cursor);
		}
		cursor.close();
		closeDB();
		
		return account;
	}
	
	//将cursor转换成account model 对象
	public AccountModel changeCursorToAccountModel(Cursor cursor){
		AccountModel account=new AccountModel();
		if(cursor!=null){
			if(cursor.getColumnIndex(DataBaseHelper.CIUDAD)!=-1){
				account.setCiudad(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CIUDAD)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.EMAIL)!=-1){
				account.setEmail(cursor.getString(cursor.getColumnIndex(DataBaseHelper.EMAIL)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.RUT)!=-1){
				account.setRut(cursor.getString(cursor.getColumnIndex(DataBaseHelper.RUT)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.REGION)!=-1){
				account.setRegion(cursor.getString(cursor.getColumnIndex(DataBaseHelper.REGION)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.COLEGIO)!=-1){
				account.setColegio(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLEGIO)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.ANDROID_SID)!=-1){
				account.setAndroid_sid(cursor.getString(cursor.getColumnIndex(DataBaseHelper.ANDROID_SID)));
			}
		}
		return account;
	}
}
