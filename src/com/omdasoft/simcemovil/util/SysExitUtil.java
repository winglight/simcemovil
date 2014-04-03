package com.omdasoft.simcemovil.util;

import java.util.ArrayList;
import java.util.List;

import com.omdasoft.simcemovil.nat.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SysExitUtil{
	public static List<Activity> activityList = new ArrayList<Activity>();//加载过的activity对象
	
	//finish所有list中的activity 
	public static void exit() {
		int siz = activityList.size();
		for (int i = 0; i < siz; i++) {
			if (activityList.get(i) != null) {
				((Activity) activityList.get(i)).finish();
			}
		}
	}
	
	//弹出是否退出程序对话框
	public static void showExitDialog(Context c){
		new AlertDialog.Builder(c).setTitle(c.getString(R.string.warm_prompt)).setMessage(c.getString(R.string.exit_warm))
				.setPositiveButton(c.getString(R.string.sure), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						exit();
					}
				})
				.setNegativeButton(c.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).show();
	}
	
	//获取最近一次更新数据的服务器版本
	public static String getLastUpdateTime(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
    	String lastUpdateCode=prefs.getString("LastUpdateCode", "");
    	return lastUpdateCode;
	}
	
	//写入最近一次更新数据的服务器版本
	public static void setLastUpdateTime(Context c,String serverVersionCode){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		prefs.edit().putString("LastUpdateCode", serverVersionCode).commit();
	}
}