/*
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/
package com.player.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.util.Constant;
import com.player.util.SystemLog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


/**
 * @author abhijeet
 *
 */
public class AsyncDispatchMethod extends AsyncTask<String, String, Boolean> {
	
	String handler;
	ArrayList<HashMap<String,String>> bundle;
	Boolean show;
	Activity mActivity;
	private String TAG = "AsyncDispatchMethod";
	
	public AsyncDispatchMethod(String Method, ArrayList<HashMap<String,String>> params, Boolean Show,Activity activity) {
		handler = Method;
		bundle = params;
		show = Show;
		mActivity = activity;
	}
	
	public AsyncDispatchMethod(String Method, ArrayList<HashMap<String,String>> params, Boolean Show) {
		handler = Method;
		bundle = params;
		show = Show;
	}
	
	@Override
	protected void onPreExecute() {
	super.onPreExecute();
		if(show){
			if(!Layout.progressDialog.isShowing()){
				Layout.progressDialog = new ProgressDialog(mActivity,R.style.MyTheme);
				Layout.progressDialog.setCancelable(true);
				Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
				Layout.progressDialog.show();
			}
		}
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try{
			Player.dispatchMethod(handler, bundle);
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return true;
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(result){
			if(Constant.DEBUG)  Log.d(TAG , "Data Dispatched");
		}
	}
	
}