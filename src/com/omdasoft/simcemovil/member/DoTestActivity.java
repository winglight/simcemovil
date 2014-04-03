package com.omdasoft.simcemovil.member;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.omdasoft.simcemovil.nat.R;

import com.omdasoft.simcemovil.dao.QuestionsDAO;
import com.omdasoft.simcemovil.model.QuestionModel;
import com.omdasoft.simcemovil.service.RemoteService;
import com.omdasoft.simcemovil.util.DownloadUtil;
import com.omdasoft.simcemovil.util.ImageCallback;
import com.omdasoft.simcemovil.util.SysExitUtil;

/**
 * 做题页面
 * @author sunny
 */
public class DoTestActivity extends Activity {
	private RemoteService service;
	private static List<QuestionModel> questionList;//试题list
	private int currentQuestionNo;//用户正在做得当前题号
	private TextView questionTitleTxt;//题目
//	private RadioButton answerRadioBtn1;//答案选择1
//	private RadioButton answerRadioBtn2;//答案选择2
//	private RadioButton answerRadioBtn3;//答案选择3
//	private RadioButton answerRadioBtn4;//答案选择4
	private RadioGroup answerRadioGroup;
	private ImageView questionImage;	//题目图片
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.do_test);
        
        //将加载的activity加入到list，用于退出程序
        SysExitUtil.activityList.add(this);
        
        currentQuestionNo=0;
        
        if(service==null){
        	service=RemoteService.getInstance(this);
    		questionList=service.getRandomQuestions();
    	}
        
        
    	if(questionList.size()>0){
	    	//第一次进入，读取第一题
	    	QuestionModel question=questionList.get(0);
	    	setQuestionLayout(question);
	        
	        submitButtonManager();
	        backButtonManager();
	        nextButtonManager();
    	}
        
        //关闭按钮
    	TextView closeBtn=(TextView)findViewById(R.id.closeBtn);
    	closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SysExitUtil.showExitDialog(DoTestActivity.this);
			}
		});
        
    }
	
	//Back按钮的初始化及点击事件处理
	public void backButtonManager(){
		TextView backBtn=(TextView)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new OnClickListener() {
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
		TextView nextBtn=(TextView)findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentQuestionNo==questionList.size()-1){
					Toast.makeText(getApplicationContext(), "已经是最后一题了", Toast.LENGTH_SHORT).show();
				}else{
					QuestionModel question=questionList.get(currentQuestionNo+1);
					currentQuestionNo++;
					setQuestionLayout(question);
				}
			}
		});
	}
	
	//提交按钮的初始化及点击事件处理
	public void submitButtonManager(){
        TextView submitBtn=(TextView)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				double rightCount=0;//答对题目的总数
				double wrongCount=0;//答错题目的总数
				//计算答对与答错题目的总数
				for (int i = 0; i < questionList.size(); i++) {
					QuestionModel question=questionList.get(i);
					if(question.getSelectAnswer()==question.getCorrectAnswer()){
						rightCount++;
					}else{
						wrongCount++;
					}
				}
				//传入数据，跳转到饼分析图页面
				double[] values = new double[] { rightCount, wrongCount};
			    int[] colors = new int[] { Color.GREEN, Color.RED};
			    DefaultRenderer renderer = buildCategoryRenderer(values,colors);
			    renderer.setZoomButtonsVisible(true);
			    renderer.setZoomEnabled(true);
			    renderer.setChartTitleTextSize(20);
			    Intent intent=ChartFactory.getPieChartIntent(DoTestActivity.this, buildCategoryDataset("Project budget", values),
			        renderer, "Test result");
			    
			    intent.setClass(getApplicationContext(), GraphicalActivity.class);
			    startActivity(intent);
			    overridePendingTransition(R.anim.transform_in, R.anim.transform_out);
			}
		});
	}
	
	//设置饼分析图的值和显示颜色
	protected DefaultRenderer buildCategoryRenderer(double[] values,int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		
		if(values[0]!=0){
			SimpleSeriesRenderer r1 = new SimpleSeriesRenderer();
			r1.setColor(colors[0]);
			renderer.addSeriesRenderer(r1);
		}
		
		SimpleSeriesRenderer r2 = new SimpleSeriesRenderer();
		r2.setColor(colors[1]);
		renderer.addSeriesRenderer(r2);
		
		return renderer;
	}

	//给饼分析图加入元素
	protected CategorySeries buildCategoryDataset(String title, double[] values) {
		CategorySeries series = new CategorySeries(title);
		
		if(values[0]!=0){
			series.add("Right", values[0]);
		}
		series.add("Wrong", values[1]);

		return series;
	}
	
	//设置题目的显示
	public void setQuestionLayout(final QuestionModel question){
		questionTitleTxt=(TextView)findViewById(R.id.questionTitleTxt);
		answerRadioGroup=(RadioGroup)findViewById(R.id.answerRadioGroup);
		answerRadioGroup.removeAllViews();
		
		String optionsStr=question.getOptions();
		String[] options=optionsStr.split("\\$\\|");
		for (int i = 0; i < options.length; i++) {
			RadioButton radio=new RadioButton(this);
			radio.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			radio.setText(options[i]);
			radio.setId(i+1);
			//设置用户选择的项
			if(question.getSelectAnswer()==i+1){
				radio.setChecked(true);
			}
			answerRadioGroup.addView(radio);
		}
		
		//单选框组选择事件
		answerRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				QuestionModel questionModel=questionList.get(currentQuestionNo);
				questionModel.setSelectAnswer(checkedId);
				questionList.set(currentQuestionNo, questionModel);
			}
		});
		
		questionImage=(ImageView)findViewById(R.id.questionImage);
    	
    	questionTitleTxt.setText((currentQuestionNo+1)+"."+question.getQuestionTitle());
		
		//显示试题图片
		String image=question.getQuestionImage();
		if(image!=null && !"".equals(image)){
			questionImage.setImageBitmap(DownloadUtil.decodeFile(image, new ImageCallback(questionImage)));
		}else{
			questionImage.setImageBitmap(null);
			questionImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,0));
		}
		
//		int selectNo=question.getSelectAnswer();
//		if(selectNo!=0){
//			RadioButton radio=(RadioButton)answerRadioGroup.getChildAt(selectNo-1);
//			radio.setChecked(true);
//		}
		
    }
	
	public static List<QuestionModel> getQuestionList(){
		return questionList;
	}
}