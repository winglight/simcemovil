package com.omdasoft.simcemovil.member;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.omdasoft.simcemovil.nat.R;

import com.omdasoft.simcemovil.model.AccountModel;
import com.omdasoft.simcemovil.model.CodeModel;
import com.omdasoft.simcemovil.service.LocalService;
import com.omdasoft.simcemovil.service.RemoteService;
import com.omdasoft.simcemovil.util.NetworkHelper;
import com.omdasoft.simcemovil.util.SysExitUtil;
import com.omdasoft.simcemovil.util.WSError;
/**
 * 填写基本资料页面
 * @author sunny
 */
public class InformationActivity extends Activity {	
	private static EditText emailEdt;
	private EditText rutEdt;
	private EditText regionEdt;
	private Spinner ciudadSp;
	private Spinner colegioSp;
	private RemoteService service;
	private LocalService localService;
	private List<CodeModel> ciudadCodeList;
	private List<CodeModel> colegioCodeList;
	private NetworkHelper networkHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        
        localService=LocalService.getInstance(this);
        if(localService.checkRegisterState()){
        	startActivity(new Intent().setClass(InformationActivity.this, AboutusActivity.class));
        	this.finish();
        }
        
    	emailEdt=(EditText)findViewById(R.id.emailEdt);
    	rutEdt=(EditText)findViewById(R.id.rutEdt);
    	regionEdt=(EditText)findViewById(R.id.regionEdt);
    	ciudadSp=(Spinner)findViewById(R.id.ciudadSp);
    	colegioSp=(Spinner)findViewById(R.id.colegioSp);
    	service=RemoteService.getInstance(this);
    	networkHelper=NetworkHelper.getInstance(this);
    	
    	//检查程序是否需要从服务器下载数据
    	checkUpdateFromServer();
        
        //将加载的activity加入到list，用于退出程序
        SysExitUtil.activityList.add(this);
        
        Button accoutOkBtn=(Button)findViewById(R.id.accoutOkBtn);
        accoutOkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//检查网络连接是否正常
				if(networkHelper.checkNetworkConnect()){
					//注册
					AccountModel account=new AccountModel();
					account.setAndroid_sid(Settings.Secure.getString(getContentResolver(),
							Settings.Secure.ANDROID_ID));
					CodeModel ciudad=ciudadCodeList.get((int)ciudadSp.getSelectedItemId());
					account.setCiudad(ciudad.getCodeValue());
					CodeModel colegio=colegioCodeList.get((int)colegioSp.getSelectedItemId());
					account.setColegio(colegio.getCodeValue());
					account.setEmail(emailEdt.getText().toString());
					account.setRegion(regionEdt.getText().toString());
					account.setRut(rutEdt.getText().toString());
					try {
						//新增用户到本地
						localService.insertUser(account);
						//新增用户到服务器
						service.register(account);
					} catch (WSError e1) {
						e1.printStackTrace();
					}
					
					startActivity(new Intent().setClass(InformationActivity.this, AboutusActivity.class));
					overridePendingTransition(R.anim.transform_in, R.anim.transform_out);
				}else{//网络连接异常，提示信息
					InformationActivity.this.toastMsg(InformationActivity.this.getString(R.string.network_exception));
				}
			}
		});
    }
    
    public class downAdapter extends BaseAdapter{
    	private List<CodeModel> codeList;
    	
    	public downAdapter(List<CodeModel> list){
    		this.codeList=list;
    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return codeList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return codeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView showView=new TextView(InformationActivity.this);
			showView.setText(codeList.get(position).getCodeValue());
			return showView;
		}
    	
    }
    
    /**
     * 检查程序是否需要从服务器下载数据
     */
    private void checkUpdateFromServer() {
    	//判断网络连接是否正常
    	if(networkHelper.checkNetworkConnect()){
//	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//	    	boolean firstTime=prefs.getBoolean("firstrun", false);
    		boolean checkUpdate;
			try {
				checkUpdate = service.checkUpdate();
				//需要更新数据
		    	if(checkUpdate){
		    		//从服务器下载数据
		    		DownlondDataTask download=new DownlondDataTask();
		    		download.execute();	    		
		    	}else{//不需要更新数据就给下拉框赋值
		    		//设置ciudad下来选框的键值
		    		ciudadCodeList=service.getCodeByType("ciudad");
		    		ciudadSp.setAdapter(new downAdapter(ciudadCodeList));
		    		//设置colegio下来选框的键值
		    		colegioCodeList=service.getCodeByType("colegio");
		    		colegioSp.setAdapter(new downAdapter(colegioCodeList));
		    	}
			} catch (WSError e) {
				e.printStackTrace();
			}
    	}else{//网络连接异常，提示信息
			InformationActivity.this.toastMsg(InformationActivity.this.getString(R.string.network_exception));
		}
    }
    
    private class DownlondDataTask extends AsyncTask<String, Integer, Boolean>{
		private ProgressDialog loading = null;
		
		public DownlondDataTask(){
			this.loading = new ProgressDialog(InformationActivity.this);
		}

		@Override
		protected Boolean doInBackground(String... url) {
			Boolean downlonded = false;
			try {
				//下载code表数据
				boolean getCodeResult=service.updateRemoteCode();
				//下载问题表数据
				boolean getQuestionResult=service.updateRemoteQuestions();
				if(getCodeResult && getQuestionResult){
					downlonded=true;
				}else{
					downlonded=false;
				}				
				
			} catch (WSError e) {
				e.printStackTrace();
				return downlonded;
			}
			return downlonded;
		}
		

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			InformationActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							loading.setMessage(InformationActivity.this.getString(R.string.data_loading));
							loading.setIndeterminate(true);
							loading.setCancelable(true);
							loading.show();
							loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
								
								@Override
								public void onCancel(DialogInterface dialog) {
									loading.cancel();
									DownlondDataTask.this.cancel(true);
								}
							});
						}
					});
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override  
        protected void onPostExecute(Boolean result) {  
			loading.cancel();
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(InformationActivity.this);

			//如果返回true，显示下载数据成功提示信息
			if(result){
				//设置ciudad下来选框的键值
				ciudadCodeList=service.getCodeByType("ciudad");
				ciudadSp.setAdapter(new downAdapter(ciudadCodeList));
				//设置colegio下来选框的键值
				colegioCodeList=service.getCodeByType("colegio");
				colegioSp.setAdapter(new downAdapter(colegioCodeList));
				InformationActivity.this.toastMsg(InformationActivity.this.getString(R.string.update_data_succeed));
				
				//下载数据成功，记录为正常第一次启动
				prefs.edit().putBoolean("firstrun", true).commit();
			}else{//不为空执行跳转
				InformationActivity.this.toastMsg(InformationActivity.this.getString(R.string.update_data_failed));
				
				//下载数据不成功，记录不是第一次启动
				prefs.edit().putBoolean("firstrun", false).commit();
				SysExitUtil.exit();
			}
        }
		
	}
    
    /**
     * 弹出提示框
     * @param msg
     */
    public void toastMsg(final String msg ) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
    
}