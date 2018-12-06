/*
 * Copyright (c) Lukup Media Pvt Limited, India.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the licence agreement you entered into with Lukup Media Pvt Limited.
 *
 */
package com.player.widget;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.player.Layout;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ListView;


/**
 * class used to display settings list 
 * @author Yuvaraja
 *
 */
public class SettingsListView {
	private Activity activity;
	private SettingListAdapter adapter;
	public SettingsListView(Activity activity){
		this.activity = activity;
	}
	
	/**
	 * 
	 * get Settings menu list
	 * @param id - id of settings type
	 * @param headerTitle - list title
	 * @return Settings list options
	 */
	public ListView  getList(){
		ListView list = new ListView(activity);
			try {
				ArrayList<HashMap<String, String>> settingslist = new ArrayList<HashMap<String,String>>();
				
				if (Constant.Mobile) {
					if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player X")){
						for(int i=0;i<ScreenStyles.PUBLIC_MOBILE_SETTINGS_LIST.length;i++){
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.PUBLIC_MOBILE_SETTINGS_LIST[i]);
							String imageId = Integer.toString(ScreenStyles.PUBLIC_MOBILE_SETTINGS_LIST_ICONS[i]);
							map.put(ScreenStyles.LIST_KEY_THUMB_URL, imageId);
							settingslist.add(map);
						}
					}else{
						for(int i=0;i<ScreenStyles.PUBLIC_MOBILE_SETTINGS_LIST.length;i++){
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.PUBLIC_MOBILE_SETTINGS_LIST[i]);
							String imageId = Integer.toString(ScreenStyles.PUBLIC_MOBILE_SETTINGS_LIST_ICONS[i]);
							map.put(ScreenStyles.LIST_KEY_THUMB_URL, imageId);
							settingslist.add(map);
						}
					}
				} else {
					if(DataStorage.getDeviceType() != null && Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						for(int i=0;i<ScreenStyles.PUBLIC_REMOTE_SETTINGS_LIST.length;i++){
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.PUBLIC_REMOTE_SETTINGS_LIST[i]);
							String imageId = Integer.toString(ScreenStyles.PUBLIC_REMOTE_SETTINGS_LIST_ICONS[i]);
							map.put(ScreenStyles.LIST_KEY_THUMB_URL, imageId);
							settingslist.add(map);
						}
					}else{
						for(int i=0;i<ScreenStyles.PUBLIC_REMOTE_SETTINGS_LIST.length;i++){
							if(i!=1 && i!=3){
								HashMap<String, String> map = new HashMap<String, String>();
								map.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.PUBLIC_REMOTE_SETTINGS_LIST[i]);
								String imageId = Integer.toString(ScreenStyles.PUBLIC_REMOTE_SETTINGS_LIST_ICONS[i]);
								map.put(ScreenStyles.LIST_KEY_THUMB_URL, imageId);
								settingslist.add(map);
							}
						}
					}
				}
				list.setCacheColorHint(Color.TRANSPARENT);
				adapter = new SettingListAdapter(activity, settingslist);
				list.setAdapter(adapter);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				return list;
			}
		
	}
	
	
}
