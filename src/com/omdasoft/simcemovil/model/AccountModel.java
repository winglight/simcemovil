package com.omdasoft.simcemovil.model;
/**
 * 账户明细model
 * @author sunny
 */
public class AccountModel{
	private String email;			//邮箱地址
	private String rut;				//
	private String region;			//
	private String ciudad;			//
	private String colegio;			//
	private String android_sid;		//android手机sid
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getColegio() {
		return colegio;
	}
	public void setColegio(String colegio) {
		this.colegio = colegio;
	}
	public String getAndroid_sid() {
		return android_sid;
	}
	public void setAndroid_sid(String android_sid) {
		this.android_sid = android_sid;
	}
}