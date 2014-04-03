package com.omdasoft.simcemovil.service.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.omdasoft.simcemovil.model.CodeModel;
import com.omdasoft.simcemovil.model.JSONBuilder;
import com.omdasoft.simcemovil.model.QuestionModel;

public class CodeModelBuilder extends JSONBuilder<CodeModel>{

	@Override
	public CodeModel build(JSONObject jsonObject) throws JSONException {
		CodeModel code=new CodeModel();
		code.setCodeNo(jsonObject.getInt(CodeModel.CODE_NO));
		code.setCodeKey(jsonObject.getString(CodeModel.CODE_KEY));
		code.setCodeValue(jsonObject.getString(CodeModel.CODE_VALUE));
		code.setType(jsonObject.getString(CodeModel.TYPE));
		return code;
	}

}
