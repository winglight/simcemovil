package com.omdasoft.simcemovil.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.omdasoft.simcemovil.db.DataBaseHelper;
import com.omdasoft.simcemovil.model.CodeModel;

public class CodeDAO {
	private SQLiteDatabase db;//db对象
	private final Context context;//上下文对象
	private static CodeDAO instance;//codeDao实例对象
	public DataBaseHelper sdbHelper;//helper类对象
	
	private CodeDAO(Context c){
		this.context = c;
		this.sdbHelper=new DataBaseHelper(c);
	}
	
	/**
	 * 获取dao实例对象
	 * @param c 上下文对象
	 * @return dao实例对象
	 */
	public static CodeDAO getInstance(Context c) {
		if (instance == null) {
			instance = new CodeDAO(c);
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
	
	//获取所有code
	public List<CodeModel> getCodeByType(String codeType){
		List<CodeModel> codeList=new ArrayList<CodeModel>();
		
		openDB();
		Cursor cursor=db.query(DataBaseHelper.CODE_TABLE_NAME, new String[]{DataBaseHelper.ID,DataBaseHelper.CODE_NO,
				DataBaseHelper.CODE_KEY,DataBaseHelper.CODE_VALUE,DataBaseHelper.TYPE}
				, DataBaseHelper.TYPE+"=?", new String[]{codeType}, null, null, DataBaseHelper.CODE_NO+" asc ");
		while(cursor.moveToNext()){
			codeList.add(changeCursorToCodeModel(cursor));
		}
		cursor.close();
		closeDB();
		
		return codeList;
	}
	
	//删除所有试题
	public void deleteAllCode(){
		openDB();
		db.delete(DataBaseHelper.CODE_TABLE_NAME, "1=1", null);
		closeDB();
	}
	
	//插入一条code到db
	public void insertCode(CodeModel model){
		openDB();
		ContentValues newActValue = new ContentValues();
		newActValue.put(DataBaseHelper.CODE_NO, model.getCodeNo());
		newActValue.put(DataBaseHelper.CODE_KEY, model.getCodeKey());
		newActValue.put(DataBaseHelper.CODE_VALUE, model.getCodeValue());
		newActValue.put(DataBaseHelper.TYPE, model.getType());
		db.insert(DataBaseHelper.CODE_TABLE_NAME, null, newActValue);
		closeDB();
	}
	
	//插入多条code到db
	public void insertCodeList(List<CodeModel> list){
		if(list==null)
			return;
		for (int i = 0; i < list.size(); i++) {
			insertCode(list.get(i));
		}
	}
	
	//将cursor转换成question model 对象
	public CodeModel changeCursorToCodeModel(Cursor cursor){
		CodeModel code=new CodeModel();
		if(cursor!=null){
			if(cursor.getColumnIndex(DataBaseHelper.CODE_NO)!=-1){
				code.setCodeNo(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CODE_NO)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.CODE_KEY)!=-1){
				code.setCodeKey(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CODE_KEY)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.CODE_VALUE)!=-1){
				code.setCodeValue(cursor.getString(cursor.getColumnIndex(DataBaseHelper.CODE_VALUE)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.TYPE)!=-1){
				code.setType(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TYPE)));
			}
		}
		return code;
	}
}
