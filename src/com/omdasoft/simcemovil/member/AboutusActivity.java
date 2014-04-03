package com.omdasoft.simcemovil.member;

import com.omdasoft.simcemovil.util.SysExitUtil;

import com.omdasoft.simcemovil.nat.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 关于我们
 * @author sunny
 */
public class AboutusActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        
        //将加载的activity加入到list，用于退出程序
        SysExitUtil.activityList.add(this);
        
        Button aboutasOkBtn=(Button)findViewById(R.id.aboutasOkBtn);
        aboutasOkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(intent.setClass(AboutusActivity.this, DoTestActivity.class));
				overridePendingTransition(R.anim.transform_in, R.anim.transform_out);
			}
		});
    }
}