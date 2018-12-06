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

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.player.R;
import com.player.Layout;
import com.player.util.Constant;
import com.player.util.PropertiesUtil;
import com.player.util.SystemLog;


public class ImagePasswordAction implements OnItemClickListener{
	private Activity mActivity;
	private Dialog mpassOverlay;
	private String method = null;
	private String Id = null;
	private String name = null;
	private String tag = null;
	
	public ImagePasswordAction(Activity activity, Dialog dialog,String method, String Id,String name){
		this.mActivity = activity;
		this.mpassOverlay = dialog;
		this.method = method;
		this.Id = Id;
		this.name = name;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			if(mActivity != null){
				ImageView view = (ImageView) arg1.findViewById(R.id.grid_item_image);
				int parseInt = Integer.parseInt((String) view.getTag());
				String id = mActivity.getResources().getResourceEntryName(parseInt);
				String imagePwd = PropertiesUtil.getPropertiesByValue(mActivity, id);
				if(imagePwd == null || imagePwd.equalsIgnoreCase("")){
					imagePwd ="";
				}else{
					sendDatatoMediaplayer(imagePwd,method,name);
				}				
				if(mActivity != null){
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(mpassOverlay != null){
								mActivity.showDialog(0);
								mpassOverlay.dismiss();
							}
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	/**
	 * @param tempId
	 * @param screenId 
	 */
	private void sendDatatoMediaplayer(String imagePwd, String method, String name) {
		if(Constant.DEBUG)  Log.d("ImagePasswordAction","sendDatatoMediaplayer() name: "+name+", imageid: "+imagePwd);
		if(Constant.DEBUG)  Log.d("ImagePasswordAction","sendDatatoMediaplayer() method: "+method);
		try{
			ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			if(method != null){
				if(Constant.DEBUG)  Log.d("ImagePasswordAction","sendDatatoMediaplayer() method is Not NULL");
				if(method.trim().equalsIgnoreCase("com.port.apps.settings.Profile.createProfile")){
					if(Constant.DEBUG)  Log.d("ImagePasswordAction"," method: "+method);
					list.put("consumer", "TV");
					list.put("network",Layout.mDataAccess.getConnectionType());
					list.put("name", name);// name
					list.put("imageid", imagePwd);
					list.put("type", "register");
					list.put("tag", "createProfile");
					list.put("caller", "com.player.apps.Profile");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
				}else if(method.trim().equalsIgnoreCase("com.port.apps.settings.Profile.switchProfile")){
					if(Constant.DEBUG)  Log.d("ImagePasswordAction"," method: "+method);
					list.put("consumer", "TV");
					list.put("network",Layout.mDataAccess.getConnectionType());
					list.put("id", Id);
					list.put("name", name);		
					list.put("imageid", imagePwd);
					list.put("tag", "switchProfile");
					list.put("caller", "com.player.apps.Profile");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String Methods = "com.port.apps.settings.Profile.authenticate";
					new AsyncDispatchMethod(Methods, dispatchHashMap,false).execute();
				}else if(method.trim().equalsIgnoreCase("com.port.apps.settings.Profile.deleteProfile")){
					if(Constant.DEBUG)  Log.d("ImagePasswordAction"," method: "+method);
					list.put("consumer", "TV");
					list.put("network",Layout.mDataAccess.getConnectionType());
					list.put("id", Id);
					list.put("name", name);
					list.put("imageid", imagePwd);
					list.put("tag", "deleteProfile");
					list.put("caller", "com.player.apps.Profile");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String Methods = "com.port.apps.settings.Profile.authenticate";
					new AsyncDispatchMethod(Methods, dispatchHashMap,false).execute();
				}else if(method.trim().equalsIgnoreCase("editname") || method.trim().equalsIgnoreCase("editpwd")){
					if(Constant.DEBUG)  Log.d("ImagePasswordAction"," method: "+method);
					list.put("consumer", "TV");
					list.put("network",Layout.mDataAccess.getConnectionType());
					list.put("id", Id);
					list.put("name", name);
					list.put("imageid", imagePwd);
					if(method.equalsIgnoreCase("editname")){
						list.put("tag", method);
					}else{
						list.put("tag", method);
					}
					list.put("caller", "com.player.apps.Profile");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					new AsyncDispatchMethod("com.port.apps.settings.Profile.editProfile", dispatchHashMap,false).execute();
				}else if(method.trim().equalsIgnoreCase("lock") || method.trim().equalsIgnoreCase("unlock")){
					if(Constant.DEBUG)  Log.d("ImagePasswordAction"," method: "+method);
					list.put("consumer", "TV");
					list.put("network",Layout.mDataAccess.getConnectionType());
					list.put("id", Id);
					list.put("type", name);
					list.put("state", method);
					list.put("imageid", imagePwd);
					list.put("caller", "com.player.apps.Guide");   // according to Class
					list.put("called", "startService");
					dispatchHashMap.add(list);
					new AsyncDispatchMethod("com.port.apps.epg.Attributes.Lock", dispatchHashMap,false).execute();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
}
