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

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.player.R;
import com.player.Layout;
import com.player.Layout.AsyncDispatch;
import com.player.apps.AppGuide;
import com.player.service.CacheDockData;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.HelpText;
import com.player.widget.OverLay;


/**
 * @author abhijeete
 *
 */
public class PlayOn {
	
	private static Activity mActivity;
	private static ArrayList<HashMap<String, String>> dispatchHashMap;
	private static ArrayList<HashMap<String, String>> connectedDeviceList;
	private static JSONArray jsonArray = null;
	private static Dialog listoverlay;
	
	
	public static void getConnectedDevices(String selectedItem_id, String eventType,String caller,Activity activity){
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("id", selectedItem_id);
		list.put("type", eventType);
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Devices.getOutputDeviceList";
		new AsyncDispatchMethod(method, dispatchHashMap,true,activity).execute();
	}
	
	
	public static void requestForStop(String caller){
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("consumer", "TV");
		list.put("network", "a2dp");
		if(DataStorage.getConnectedBTDevice() != null){
			list.put("address", DataStorage.getConnectedBTDevice());
		}
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Devices.stopCommandToDevice";
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}
	
	public static void stopWifiDisplay(String caller){
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Devices.stopRemoteDisplay";
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}
	
	public static void getRemoteDisplays(String eventId,String caller){
		
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("id", eventId);
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Devices.getRemoteDisplays";
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}
	
	
	public static void showConnectedDevices(final JSONObject deviceList,final String selectedItem_Id, final Activity act, final CustomLayout clayout, final String network, final String caller){
		mActivity = act;
		if(mActivity != null){
			if(deviceList != null){
				try {
					if(deviceList.has("result")){
						if(deviceList.getString("result").equalsIgnoreCase("success")){
							if(network.equalsIgnoreCase("BT")){
								JSONArray jsonArray = Utils.getListFromJSON(deviceList, "connectedList");
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										HashMap<String,String> showMap = new HashMap<String, String>();
										JSONObject obj = jsonArray.getJSONObject(i);
										String mName = "";
										String mAddress = "";
										if(obj.has("name")){
											mName= obj.getString("name");
										}
										if(obj.has("address")){
											mAddress= obj.getString("address");
										}
										showMap.put(ScreenStyles.LIST_KEY_ID, mAddress);
										showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
										
										if(CacheDockData.connectedDeviceList.size()>0){
											int count = 0;
											for (int j = 0; j < CacheDockData.connectedDeviceList.size(); j++) {
												if(CacheDockData.connectedDeviceList.get(j).get("id").equalsIgnoreCase(mAddress)){
													count++;
												}
											}
											if(count == 0){
												CacheDockData.connectedDeviceList.add(showMap);
											}
										}else{
											CacheDockData.connectedDeviceList.add(showMap);
										}
									}
									mActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (listoverlay != null && listoverlay.isShowing()) {
												listoverlay.cancel();
												showDevice(CacheDockData.connectedDeviceList,selectedItem_Id,"BT",act, clayout, caller);
											}else{
												showDevice(CacheDockData.connectedDeviceList,selectedItem_Id,"BT",act, clayout, caller);
											}
										}
									});
								}
							}else if(network.equalsIgnoreCase("Wifi")){
								final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
								JSONArray jsonArray = Utils.getListFromJSON(deviceList, "connectedList");
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										HashMap<String,String> showMap = new HashMap<String, String>();
										JSONObject obj = jsonArray.getJSONObject(i);
										String mName = "";
										String mAddress = "";
										if(obj.has("name")){
											mName= obj.getString("name");
										}
										if(obj.has("id")){
											mAddress= obj.getString("id");
										}
										showMap.put(ScreenStyles.LIST_KEY_ID, mAddress);
										showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
										list.add(showMap);
									}
									mActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (listoverlay != null && listoverlay.isShowing()) {
												listoverlay.cancel();
												showDevice(list,selectedItem_Id,"Wifi", act, clayout, caller);
											}else{
												showDevice(list,selectedItem_Id,"Wifi", act, clayout, caller);
											}
										}
									});
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
				
		}		
		
	}
	
	private static void showDevice(final ArrayList<HashMap<String,String>> list, final String selectItemId, final String tag, final Activity mInstance, final CustomLayout clayout, final String caller){
		if(mInstance != null){
			mInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						if(list.size() > 0){
							OverLay overlay = new OverLay(mInstance);
							listoverlay= overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_LIST, "Connected Devices", null, list,null,false);
							
							listoverlay.show(); 
							ListView ovelaylistItem = (ListView) listoverlay.findViewById(R.id.overlayList);
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
							if(ovelaylistItem != null){
								ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) ovelaylistItem.getLayoutParams();
								params.height = clayout.getConvertedHeight(200);
								ovelaylistItem.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
										HashMap<String, String> gettinglist = list.get(arg2);
										String name = "";
										String address = "";
										if(gettinglist.containsKey(ScreenStyles.LIST_KEY_TITLE)){
											name = gettinglist.get(ScreenStyles.LIST_KEY_TITLE);
										}
										if(gettinglist.containsKey(ScreenStyles.LIST_KEY_ID)){
											address = gettinglist.get(ScreenStyles.LIST_KEY_ID);
										}
										try{
											if(tag.equalsIgnoreCase("BT")){
												dispatchHashMap  = new ArrayList<HashMap<String,String>>();
												HashMap<String, String> list = new HashMap<String, String>();
												list.put("consumer", "TV");
												list.put("network", "a2dp");
												list.put("id", selectItemId);
												list.put("address", address);
												list.put("caller", caller);
												list.put("called", "startService");
												dispatchHashMap.add(list);
												String method = "com.port.apps.epg.Devices.stream"; 
												new AsyncDispatchMethod(method, dispatchHashMap,true,mInstance).execute();
											}else if(tag.equalsIgnoreCase("Wifi")){
												dispatchHashMap  = new ArrayList<HashMap<String,String>>();
												HashMap<String, String> list = new HashMap<String, String>();
												list.put("consumer", "TV");
												list.put("network", "Miracast");
												list.put("id", selectItemId);
												list.put("displayID", address);
												list.put("caller", caller);
												list.put("called", "startService");
												dispatchHashMap.add(list);
												String method = "com.port.apps.epg.Devices.playRemoteDisplay"; 
												new AsyncDispatchMethod(method, dispatchHashMap,true,mInstance).execute();
											}
//											else{
//												dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//												HashMap<String, String> list = new HashMap<String, String>();
//												list.put("consumer", "TV");
//												list.put("network", "DLNA");
//												list.put("id", selectItemId);
//												list.put("displayID", address);
//												list.put("caller", caller);
//												list.put("called", "startService");
//												dispatchHashMap.add(list);
//												String method = "com.port.apps.epg.Devices.playOnDLNA"; 
//												new AsyncDispatchMethod(method, dispatchHashMap,true,mInstance).execute();	
//											}
											
											if(listoverlay != null && listoverlay.isShowing()){
												listoverlay.dismiss();
												if(Constant.DEBUG)  Log.d("PlayOn", "showDevice: "+DataStorage.getCurrentActivity());
												if(caller.equalsIgnoreCase("com.player.apps.PlayBack")){
													if(!DataStorage.getCurrentActivity().equalsIgnoreCase("Navigator")){
														mInstance.finish();
													}else{
														Intent i = new Intent(mInstance, AppGuide.class);
														i.putExtra("ActivityName", "PlayBack");
														mInstance.startActivity(i);
													}
												}
											}
										}catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										}
									}
								});
							}
							if(closeBtn != null){
								closeBtn.setOnClickListener(new OverlayCancelListener(listoverlay));
							}
						}else{
							HelpText.showHelpTextDialog(mInstance, mInstance.getResources().getString(R.string.NO_CONNECTED_DEVICE), 2000);
						}
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			});
		}
	}
	
	
}
