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
import com.omdasoft.simcemovil.model.QuestionModel;

public class QuestionsDAO{
	private SQLiteDatabase db;//db对象
	private final Context context;//上下文对象
	private static QuestionsDAO instance;//questionDao实例对象
	public DataBaseHelper sdbHelper;//helper类对象
	
	private QuestionsDAO(Context c){
		this.context = c;
		this.sdbHelper=new DataBaseHelper(c);
	}
	
	/**
	 * 获取dao实例对象
	 * @param c 上下文对象
	 * @return dao实例对象
	 */
	public static QuestionsDAO getInstance(Context c) {
		if (instance == null) {
			instance = new QuestionsDAO(c);
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
	
	//获取测试题目
	public List<QuestionModel> getQuestions(){
		List<QuestionModel> questionList=new ArrayList<QuestionModel>();
		
		openDB();
		Cursor cursor=db.query(DataBaseHelper.QUESTION_TABLE_NAME, new String[]{DataBaseHelper.ID,DataBaseHelper.QUESTION_NO,
				DataBaseHelper.QUESTION_TITLE,DataBaseHelper.OPTIONS,DataBaseHelper.SELECT_ANSWER,DataBaseHelper.CORRECT_ANSWER,
				DataBaseHelper.EXPLAINATION,
				DataBaseHelper.QUESTION_IMAGE,DataBaseHelper.TYPE_NAME,DataBaseHelper.TYPE_CODE,DataBaseHelper.LEVEL}
				, null, null, null, null, DataBaseHelper.QUESTION_NO+" asc ");
		while(cursor.moveToNext()){
			questionList.add(changeCursorToQuestionModel(cursor));
		}
		cursor.close();
		closeDB();
		
		return questionList;
	}
	
	//删除所有试题
	public void deleteAllQuestions(){
		openDB();
		db.delete(DataBaseHelper.QUESTION_TABLE_NAME, "1=1", null);
		closeDB();
	}
	
	//插入一条题目到db
	public void insertQuestion(QuestionModel model){
		openDB();
		ContentValues newActValue = new ContentValues();
		newActValue.put(DataBaseHelper.QUESTION_NO, model.getQuestionNo());
		newActValue.put(DataBaseHelper.QUESTION_TITLE, model.getQuestionTitle());
		newActValue.put(DataBaseHelper.OPTIONS, model.getOptions());
		newActValue.put(DataBaseHelper.CORRECT_ANSWER, model.getCorrectAnswer());
		newActValue.put(DataBaseHelper.EXPLAINATION, model.getExplaination());
		newActValue.put(DataBaseHelper.QUESTION_IMAGE, model.getQuestionImage());
		newActValue.put(DataBaseHelper.TYPE_NAME, model.getTypeName());
		newActValue.put(DataBaseHelper.TYPE_CODE, model.getTypeCode());
		newActValue.put(DataBaseHelper.LEVEL, model.getLevel());
		db.insert(DataBaseHelper.QUESTION_TABLE_NAME, null, newActValue);
		closeDB();
	}
	
	//插入多条题目到db
	public void insertQuestionList(List<QuestionModel> list){
		if(list==null)
			return;
		for (int i = 0; i < list.size(); i++) {
			insertQuestion(list.get(i));
		}
	}
	
	//将cursor转换成question model 对象
	public QuestionModel changeCursorToQuestionModel(Cursor cursor){
		QuestionModel question=new QuestionModel();
		if(cursor!=null){
			if(cursor.getColumnIndex(DataBaseHelper.ID)!=-1){
				question.setId(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ID)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.QUESTION_NO)!=-1){
				question.setQuestionNo(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.QUESTION_NO)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.QUESTION_TITLE)!=-1){
				question.setQuestionTitle(cursor.getString(cursor.getColumnIndex(DataBaseHelper.QUESTION_TITLE)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.OPTIONS)!=-1){
				question.setOptions(cursor.getString(cursor.getColumnIndex(DataBaseHelper.OPTIONS)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.SELECT_ANSWER)!=-1){
				question.setSelectAnswer(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.SELECT_ANSWER)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.CORRECT_ANSWER)!=-1){
				question.setCorrectAnswer(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.CORRECT_ANSWER)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.EXPLAINATION)!=-1){
				question.setExplaination(cursor.getString(cursor.getColumnIndex(DataBaseHelper.EXPLAINATION)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.QUESTION_IMAGE)!=-1){
				question.setQuestionImage(cursor.getString(cursor.getColumnIndex(DataBaseHelper.QUESTION_IMAGE)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.TYPE_CODE)!=-1){
				question.setTypeCode(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TYPE_CODE)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.TYPE_NAME)!=-1){
				question.setTypeName(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TYPE_NAME)));
			}
			if(cursor.getColumnIndex(DataBaseHelper.LEVEL)!=-1){
				question.setLevel(cursor.getString(cursor.getColumnIndex(DataBaseHelper.LEVEL)));
			}
		}
		return question;
	}
}