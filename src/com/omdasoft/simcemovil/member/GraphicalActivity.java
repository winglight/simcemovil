package com.omdasoft.simcemovil.member;

import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.omdasoft.simcemovil.nat.R;

import com.omdasoft.simcemovil.model.QuestionModel;
import com.omdasoft.simcemovil.model.ResultModel;
import com.omdasoft.simcemovil.service.LocalService;
import com.omdasoft.simcemovil.service.RemoteService;
import com.omdasoft.simcemovil.util.SysExitUtil;
import com.omdasoft.simcemovil.util.WSError;

/*
 * 饼分析图
 */
public class GraphicalActivity extends Activity {
	private LocalService localService;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//将加载的activity加入到list，用于退出程序
        SysExitUtil.activityList.add(this);
        
        localService=LocalService.getInstance(this);
		
	    Bundle extras = getIntent().getExtras();
	    AbstractChart mChart = (AbstractChart) extras.getSerializable(ChartFactory.CHART);
	    GraphicalView mView = new GraphicalView(this, mChart);
	    mView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 600));
	    String title = extras.getString(ChartFactory.TITLE);
	    if (title == null) {
	      requestWindowFeature(Window.FEATURE_NO_TITLE);
	    } else if (title.length() > 0) {
	      setTitle(title);
	    }
	    //滚动View
	    ScrollView scrollView=new ScrollView(this);
	    scrollView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
	    
	    //主的LinearLayout
	    LinearLayout main=new LinearLayout(this);
	    main.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
	    main.setOrientation(LinearLayout.VERTICAL);
	    
	    //将饼图layout加入主的LinearLayout
	    main.addView(mView);
	    
	    //send/save按钮
	    Button sentBtn=new Button(this);
	    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	    lp.setMargins(0, 20, 0, 0);
	    lp.gravity=Gravity.CENTER_HORIZONTAL;
	    sentBtn.setText(getString(R.string.sent_button_text));
	    sentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//提交测试结果
				RemoteService service=RemoteService.getInstance(GraphicalActivity.this);
				
				//用户答过的试题list
				List<QuestionModel> list=DoTestActivity.getQuestionList();
				String questionAnswerCodes="";
				String questionCodes="";
				
				double rightCount=0;//答对题目的总数
				double wrongCount=0;//答错题目的总数
				//提取出回答过的题号和答案
				for (int i = 0; i < list.size(); i++) {
					QuestionModel model=list.get(i);
					questionAnswerCodes+=("".equals(questionAnswerCodes) ? "":",")+model.getSelectAnswer();
					questionCodes+=("".equals(questionCodes) ? "":",")+model.getQuestionNo();
					//计算对和错的个数
					if(model.getSelectAnswer()==model.getCorrectAnswer()){
						rightCount++;
					}else{
						wrongCount++;
					}
				}
				
				ResultModel result=new ResultModel();
				result.setEmail(localService.getInfo().getEmail());
				result.setQuestionAnswerCodes(questionAnswerCodes);
				result.setQuestionCodes(questionCodes);
				result.setRightPercent(rightCount/(rightCount+wrongCount));
//				result.setSubmitTime(new Date().getTime());
				try {
					service.submitResult(result);
				} catch (WSError e) {
					e.printStackTrace();
				}
				
				Intent intent=new Intent();
//				intent.putExtras(bundle);
				startActivity(intent.setClass(GraphicalActivity.this, CheckAnswerActivity.class));
				overridePendingTransition(R.anim.transform_in, R.anim.transform_out);
			}
		});
	    
	    //将按钮加入主的LinearLayout
	    main.addView(sentBtn,lp);
	    
	    scrollView.addView(main);
	    
	    setContentView(scrollView);
        
    }
}