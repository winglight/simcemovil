package com.omdasoft.simcemovil.service;

import android.content.Context;

import com.omdasoft.simcemovil.dao.UserDAO;
import com.omdasoft.simcemovil.model.AccountModel;

/**
 * 操作本地数据库的service类；目前只操作account表
 * @author sunny
 */

public class LocalService {
	private static LocalService service;
	private final Context context;//上下文对象
	private UserDAO userDao;
	
	private LocalService(Context context){
		this.context=context;
		userDao=UserDAO.getInstance(this.context);
	}
	
	public static LocalService getInstance(Context context){
		if(service==null){
			service=new LocalService(context);
		}
		return service;
	}
	
	//判断用户是否已经注册过
	public boolean checkRegisterState(){
		return userDao.checkRegisterState();
	}
	
	//获取当前用户
	public AccountModel getInfo(){
		return userDao.getInfo();
	}
	
	//新增用户到本地db
	public void insertUser(AccountModel account){
		userDao.insertUser(account);
	}
}
