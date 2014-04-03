package com.omdasoft.simcemovil.model;
/**
 * 试题model
 * @author sunny
 */
public class QuestionModel{
	//JSON Name Map to serverside
	public static final String CODE="code";
	public static final String QUESTION="question";
	public static final String IMAGE="image";
	public static final String OPTIONS="options";
	public static final String RIGHT_ANSWER="rightAnswer";
	public static final String TIPS="tips";
	public static final String TYPE_NAME="typeName";
	public static final String TYPE_CODE="typeCode";
	public static final String LEVEL="level";
	public static final String SUBJECT="subject";
	
	
	private int id;					//id
	private int questionNo;			//题号
	private String questionTitle;	//题目
	private String options;			//答案集合
	private int selectAnswer;		//所选答案
	private int correctAnswer;		//正确答案
	private String explaination;	//题目解析
	private String questionImage;	//题目图片
	private String typeName;		//题目类型名称
	private String typeCode;		//题目类型code
	private String level;			//题目等级
	private String subject;			//题目主题
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getQuestionImage() {
		return questionImage;
	}
	public void setQuestionImage(String questionImage) {
		this.questionImage = questionImage;
	}
	public int getSelectAnswer() {
		return selectAnswer;
	}
	public void setSelectAnswer(int selectAnswer) {
		this.selectAnswer = selectAnswer;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestionNo() {
		return questionNo;
	}
	public void setQuestionNo(int questionNo) {
		this.questionNo = questionNo;
	}
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public String getExplaination() {
		return explaination;
	}
	public void setExplaination(String explaination) {
		this.explaination = explaination;
	}
}