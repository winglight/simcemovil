package com.omdasoft.simcemovil.model;

public class CodeModel {
	
	//JSON Name Map to serverside
	public static final String CODE_NO="codeNo";
	public static final String CODE_KEY="codeKey";
	public static final String CODE_VALUE="codeValue";
	public static final String TYPE="type";
	
	private int codeNo;
	private String codeKey;
	private String codeValue;
	private String type;
	public int getCodeNo() {
		return codeNo;
	}
	public void setCodeNo(int codeNo) {
		this.codeNo = codeNo;
	}
	public String getCodeKey() {
		return codeKey;
	}
	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
