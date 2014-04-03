package com.omdasoft.simcemovil.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Michael
 *
 * @param <T>
 */
public abstract class JSONBuilder<T> {
	
	protected String root;
	
	public JSONBuilder(){
		root = "";
	}
	
	/**
	 * Sets root of the JSON params, useful when building object in joins
	 * 
	 * @param root
	 */
	public void setRoot(String root){
		this.root = root;
	}
	
	public abstract T build(JSONObject jsonObject) throws JSONException;
}
