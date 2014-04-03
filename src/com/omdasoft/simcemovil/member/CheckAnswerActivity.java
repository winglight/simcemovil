package com.omdasoft.simcemovil.member;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.omdasoft.simcemovil.nat.R;

import com.omdasoft.simcemovil.model.QuestionModel;
import com.omdasoft.simcemovil.util.SysExitUtil;

/**
 * 查看做题答案页面
 * @author sunny
 */
public class CheckAnswerActivity extends Activity {
	private List<QuestionModel> questionList;//用户回答后的试题list
	private int currentQuestionNo;//用户正在查看的当前题号
	private TextView questionTitleTxt;//题目
	private RadioGroup answerForCheckRadioGroup;
	private TextView explainationTxt;//题目解析
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_answer);
        
        //将加载的activity加入到list，用于退出程序
        SysExitUtil.activityList.add(this);
        
        questionList=DoTestActivity.getQuestionList();
        
        currentQuestionNo=0;
        
        if(questionList.size()>0){
	        //第一次进入，读取第一题
	    	QuestionModel question=questionList.get(currentQuestionNo);
	    	setQuestionLayout(question);
	    	
	    	backButtonManager();
	    	nextButtonManager();
        }
        
    	//关闭按钮
    	TextView closeForCheckBtn=(TextView)findViewById(R.id.closeForCheckBtn);
    	closeForCheckBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SysExitUtil.showExitDialog(CheckAnswerActivity.this);
			}
		});
    }
	
	//设置题目的显示
	public void setQuestionLayout(final QuestionModel question){
		questionTitleTxt=(TextView)findViewById(R.id.questionTitleForCheckTxt);
		answerForCheckRadioGroup=(RadioGroup)findViewById(R.id.answerForCheckRadioGroup);
		explainationTxt=(TextView)findViewById(R.id.explainationTxt);
		
    	//设置题目和选项
    	questionTitleTxt.setText((currentQuestionNo+1)+"."+question.getQuestionTitle());
    	
    	answerForCheckRadioGroup.removeAllViews();
    	String[] options=question.getOptions().split("\\$\\|");
		for (int i = 0; i < options.length; i++) {
			//如果是正确的答案选项则后面显示“right answer”
			if(question.getCorrectAnswer()==i+1){
				
				LinearLayout linearLayout=new LinearLayout(this);
				linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				linearLayout.setOrientation(LinearLayout.HORIZONTAL);
				
				//单选像
				RadioButton radio=new RadioButton(this);
				radio.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				radio.setText(options[i]);
				radio.setId(i+1);
				radio.setEnabled(false);
				//设置用户所选的答案
				if(question.getSelectAnswer()==i+1){
					radio.setChecked(true);
				}
				linearLayout.addView(radio);
				
				//right answer文本框
				TextView rightAnswerTxt=new TextView(this);
				rightAnswerTxt.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				rightAnswerTxt.setText(this.getString(R.string.right_answer));
				linearLayout.addView(rightAnswerTxt);
				
				answerForCheckRadioGroup.addView(linearLayout);
			}else{//否则不显示
				RadioButton radio=new RadioButton(this);
				radio.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				radio.setText(options[i]);
				radio.setId(i+1);
				radio.setEnabled(false);
				//设置用户所选的答案
				if(question.getSelectAnswer()==i+1){
					radio.setChecked(true);
				}
				answerForCheckRadioGroup.addView(radio);
			}
		}
    	
		//设置题目的解析
		explainationTxt.setText(question.getExplaination());
    }
	
	//Back按钮的初始化及点击事件处理
	public void backButtonManager(){
		TextView backForCheckBtn=(TextView)findViewById(R.id.backForCheckBtn);
		backForCheckBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentQuestionNo==0){
					Toast.makeText(getApplicationContext(), "已经是第一题了", Toast.LENGTH_SHORT).show();
				}else{
					QuestionModel question=questionList.get(currentQuestionNo-1);
					currentQuestionNo--;
					setQuestionLayout(question);
				}
			}
		});		
	}
	
	//Next按钮的初始化及点击事件处理
	public void nextButtonManager(){
		TextView nextForCheckBtn=(TextView)findViewById(R.id.nextForCheckBtn);
		nextForCheckBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentQuestionNo==questionList.size()-1){
					Toast.makeText(getApplicationContext(), "已经是最后题了", Toast.LENGTH_SHORT).show();
				}else{
					QuestionModel question=questionList.get(currentQuestionNo+1);
					currentQuestionNo++;
					setQuestionLayout(question);
					
				}
			}
		});
	}
}