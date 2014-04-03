package com.omdasoft.simcemovil.model;

public class ResultModel {
	private String email;
	private String questionCodes;
	private String questionAnswerCodes;
	private long submitTime;
	private double rightPercent;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQuestionCodes() {
		return questionCodes;
	}
	public void setQuestionCodes(String questionCodes) {
		this.questionCodes = questionCodes;
	}
	public String getQuestionAnswerCodes() {
		return questionAnswerCodes;
	}
	public void setQuestionAnswerCodes(String questionAnswerCodes) {
		this.questionAnswerCodes = questionAnswerCodes;
	}
	public long getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}
	public double getRightPercent() {
		return rightPercent;
	}
	public void setRightPercent(double rightPercent) {
		this.rightPercent = rightPercent;
	}
}
