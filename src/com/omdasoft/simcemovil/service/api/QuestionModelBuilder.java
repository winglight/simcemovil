package com.omdasoft.simcemovil.service.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.omdasoft.simcemovil.model.JSONBuilder;
import com.omdasoft.simcemovil.model.QuestionModel;


public class QuestionModelBuilder extends JSONBuilder<QuestionModel>{

	@Override
	public QuestionModel build(JSONObject jsonObject) throws JSONException {
		QuestionModel model=new QuestionModel();
		//题号
		model.setQuestionNo(jsonObject.getInt(QuestionModel.CODE));
		//题目
		model.setQuestionTitle(jsonObject.getString(QuestionModel.QUESTION));
		//图片
		model.setQuestionImage(jsonObject.getString(QuestionModel.IMAGE));
		//答案
		model.setOptions(jsonObject.getString(QuestionModel.OPTIONS));
		//正确答案选项
		model.setCorrectAnswer(jsonObject.getInt(QuestionModel.RIGHT_ANSWER));
		//题目解析
		model.setExplaination(jsonObject.getString(QuestionModel.TIPS));
		//题目类型名称
		model.setTypeName(jsonObject.getString(QuestionModel.TYPE_NAME));
		//题目类型code
		model.setTypeCode(jsonObject.getString(QuestionModel.TYPE_CODE));
		//题目级别
		model.setLevel(jsonObject.getString(QuestionModel.LEVEL));
		return model;
	}

}
