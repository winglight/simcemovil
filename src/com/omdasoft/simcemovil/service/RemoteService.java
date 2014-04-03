package com.omdasoft.simcemovil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.omdasoft.simcemovil.dao.CodeDAO;
import com.omdasoft.simcemovil.dao.QuestionsDAO;
import com.omdasoft.simcemovil.nat.R;
import com.omdasoft.simcemovil.model.AccountModel;
import com.omdasoft.simcemovil.model.CodeModel;
import com.omdasoft.simcemovil.model.QuestionModel;
import com.omdasoft.simcemovil.model.ResultModel;
import com.omdasoft.simcemovil.service.api.CodeModelFunctions;
import com.omdasoft.simcemovil.service.api.QuestionModelFunctions;
import com.omdasoft.simcemovil.util.Caller;
import com.omdasoft.simcemovil.util.SysExitUtil;
import com.omdasoft.simcemovil.util.WSError;

public class RemoteService {
//	private static final String url="http://10.3.3.204:7777/";
	private static final String url="http://simcemovilchy.appspot.com/";
	private static RemoteService service;
	private final Context context;//上下文对象
	private QuestionsDAO questionsDao;
	private CodeDAO codeDao;
	
	private RemoteService(Context context){
		this.context=context;
		questionsDao=QuestionsDAO.getInstance(this.context);
		codeDao=CodeDAO.getInstance(this.context);
	}
	
	public static RemoteService getInstance(Context context){
		if(service==null){
			service=new RemoteService(context);
		}
		return service;
	}
	
	/**
	 * 获取远程题库里面的题目
	 * @return
	 * @throws WSError
	 */
	public List<QuestionModel> getRemoteQuestions() throws WSError{
		List<QuestionModel> questionCollection=new ArrayList<QuestionModel>();
		String subject=context.getString(R.string.subject);
		String requestUrl=url+"questions?subject="+subject;
		
		String jsonString=Caller.doGet(requestUrl);
		
		if(jsonString==null){
			return new ArrayList<QuestionModel>();
		}
		
		try {
			JSONObject jsonObjectAlbums = new JSONObject(jsonString);
			JSONArray jsonArrayAlbums = jsonObjectAlbums.getJSONArray("questions");
			questionCollection= QuestionModelFunctions.getPlaylists(jsonArrayAlbums);
			
			//写入服务器数据版本号
			SysExitUtil.setLastUpdateTime(context, jsonObjectAlbums.getString("lastUpdateCode"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return questionCollection;
	}
	
	/**
	 * 将获取到的远程题库的题目插入到本地db
	 * @param questionsList
	 */
	public void insertRemoteQuestionsInLocalDB(List<QuestionModel> questionsList){
		questionsDao.insertQuestionList(questionsList);
	}
	
	/**
	 * 删除本地db试题，重新从远程获取
	 * @throws WSError
	 * @return false获取数据出错 true获取数据成功
	 */
	public boolean updateRemoteQuestions() throws WSError{
		List list=questionsDao.getQuestions();
		List<QuestionModel> newQuestionsList=getRemoteQuestions();
		if(newQuestionsList.size()==0){
			return false;
		}else{
			if(list!=null &&list.size()>0){
				questionsDao.deleteAllQuestions();
				insertRemoteQuestionsInLocalDB(newQuestionsList);
			}else{
				insertRemoteQuestionsInLocalDB(newQuestionsList);
			}
			return true;
		}
	}
	
	//注册user
	public void register(AccountModel account) throws WSError{
		String requestUrl=url+"register?android_sid="+account.getAndroid_sid()+"&ciudad="+account.getCiudad()+"&colegio="+
				account.getColegio()+"&email="+account.getEmail()+"&region="+account.getRegion()+"&rut="+account.getRut();
		Caller.doGet(requestUrl);
	}
	
	//提交测试结果
	public void submitResult(ResultModel result) throws WSError{
		String requestUrl=url+"submit_result?email="+result.getEmail()+"&questionAnswerCodes="+result.getQuestionAnswerCodes()+
				"&questionCodes="+result.getQuestionCodes()+"&rightPercent="+result.getRightPercent();
		Caller.doGet(requestUrl);
	}
	
	/**
	 * 获取远程code表数据
	 * @return
	 */
	public List<CodeModel> getRemoteCodeData() throws WSError{
		List<CodeModel> codeCollection=new ArrayList<CodeModel>();
		String requestUrl=url+"get_code";
		
		String jsonString=Caller.doGet(requestUrl);
		
		if(jsonString==null){
			return new ArrayList<CodeModel>();
		}
		
		try {
			JSONObject jsonObjectAlbums = new JSONObject(jsonString);
			JSONArray jsonArrayAlbums = jsonObjectAlbums.getJSONArray("code");
			codeCollection= CodeModelFunctions.getPlaylists(jsonArrayAlbums);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return codeCollection;
	}
	
	/**
	 * 检查是否要从服务器更新本地数据
	 * @return false:不更新   true:更新
	 */
	public boolean checkUpdate() throws WSError{
		String requestUrl=url+"check_update?lastUpdateCode="+SysExitUtil.getLastUpdateTime(context);
		
		String jsonString=Caller.doGet(requestUrl);
		
		boolean checkUpdate=false;
		
		List list=questionsDao.getQuestions();
		if(list==null || list.size()==0){
			return true;
		}
		
		if(jsonString==null){
			return false;
		}
		
		try {
			JSONObject jsonObjectAlbums = new JSONObject(jsonString);
			String result = jsonObjectAlbums.getString("result");
			if("true".equals(result))
				checkUpdate=true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return checkUpdate;
	}
	
	/**
	 * 删除本地code表，重新从远程获取
	 * @throws WSError
	 * @return false获取数据出错 true获取数据成功
	 */
	public boolean updateRemoteCode() throws WSError{
		codeDao.deleteAllCode();
		List<CodeModel> newCodeList=getRemoteCodeData();
		for (int i = 0; i < newCodeList.size(); i++) {
			codeDao.insertCode(newCodeList.get(i));
		}
		if(newCodeList.size()==0){
			return false;
		}
		return true;
	}
	
	/*
	 * 根据code类型获取code model list
	 */
	public List<CodeModel> getCodeByType(String codeType){
		return codeDao.getCodeByType(codeType);
	}
	
	/**
	 * 获取固定数据的随机题目
	 * @return 题目list
	 */
	public List<QuestionModel> getRandomQuestions(){
		//返回的随机题目list
		List<QuestionModel> resultList=new ArrayList<QuestionModel>();
		//所有题目的list
		List<QuestionModel> allQuestionList=questionsDao.getQuestions();
		int randomQuestionCount=Integer.parseInt(context.getString(R.string.randomQuestionCount));
		
		int questionCount=allQuestionList.size()>randomQuestionCount ? randomQuestionCount:allQuestionList.size();
		
		for (int i = 0; i < questionCount; i++) {
			int num=new Random().nextInt(allQuestionList.size());
			resultList.add(allQuestionList.get(num));
			allQuestionList.remove(num);
		}
		
		return resultList;
	}	
}
