package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.player.Layout;
import com.player.R;
import com.player.service.BouquetInfo;
import com.player.service.CacheDockData;
import com.player.service.ChannelInfo;
import com.player.service.ProgramInfo;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.HelpText;

public class GuideService extends IntentService{

	private String TAG = "GuideService";
	
	public GuideService() {
		super("GuideService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		String jsondata = "";
		String handler = "";
		JSONObject objData = null;
		if(extras != null){
			if(extras.containsKey("Params")){
				jsondata = extras.getString("Params");
			}if(extras.containsKey("Handler")){
				handler = extras.getString("Handler");
			}
			
			try{
			JSONObject jsonData = new JSONObject(jsondata);
			if(Constant.DEBUG)  Log.d(TAG  , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Home.sendFeaturedList")){
				if(jsonData != null){
					try {
						if(jsonData.has("result")){
							if(jsonData.getString("result").equalsIgnoreCase("success")){
								CacheDockData.fragmentFeaturedList = Home.getFeaturedItemList(jsonData, null, "featuredList");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendBouquetList")){
				if(jsonData != null){
					try {
						if(jsonData.has("result")){
							if(jsonData.getString("result").equalsIgnoreCase("success")){
								JSONArray jsonArray = Utils.getListFromJSON(jsonData, "bouquetList");
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										HashMap<String,String> showMap = new HashMap<String, String>();
										JSONObject obj;
										try {
											obj = jsonArray.getJSONObject(i);
											String id = "";
											String mName = "";
											String mImage = "";
											if(obj.has("id")){
												id= obj.getString("id");
											}
											if(obj.has("image")){
												mImage= obj.getString("image");
											}
											if(obj.has("name")){
												mName= obj.getString("name");
											}
											showMap.put(ScreenStyles.LIST_KEY_ID, id);
											if(mImage != null && !mImage.equalsIgnoreCase("")){
												showMap.put(ScreenStyles.LIST_KEY_HTTP_URL, mImage);
											}else{
												showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, Integer.toString(R.drawable.v13_ico_folder_01));
											}
											showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
											CacheDockData.BouquetList.add(new BouquetInfo(id, mName, ""));
											
										} catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										}
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){
				if(jsonData != null){
					try {
						if(jsonData.has("result")){
							if(jsonData.getString("result").equalsIgnoreCase("success")){
								boolean ishasVal = jsonData.has("serviceList");
								if(ishasVal){
									Guide.getServiceData(jsonData, "serviceList");
									if(Constant.DEBUG)  Log.d(TAG , "ServiceList Size: "+CacheDockData.ServiceList.size());
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
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
				if(jsonData != null){
					try {
						if(jsonData.has("result")){
							if(jsonData.getString("result").equalsIgnoreCase("success")){
								boolean ishasVal = jsonData.has("eventList");
								if(ishasVal){
									Guide.getEventData(jsonData, "eventList");
									if(Constant.DEBUG)  Log.d(TAG , "EventList Size: "+CacheDockData.EventList.size());
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
			}else if(handler.equalsIgnoreCase("com.port.service.Catalogue.epg-update")){
				if(jsonData != null){
					try {
						if(jsonData.has("update")){
							if(jsonData.getString("update").equalsIgnoreCase("VOD") || jsonData.getString("update").equalsIgnoreCase("EPG")){
								CacheDockData.BouquetList.clear();
								CacheDockData.ServiceList.clear();
								CacheDockData.EventList.clear();
							}
						}
						if(Constant.DEBUG)  Log.d(TAG , "EventList Size: "+CacheDockData.EventList.size());
						if(Constant.DEBUG)  Log.d(TAG , "ServiceList Size: "+CacheDockData.ServiceList.size());
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.service.Catalogue.subscription")){
				try {
					if(jsonData != null){
						if(jsonData.has("result")){
							if(jsonData.getString("result").equalsIgnoreCase("success")){
								String Id = jsonData.getString("id");
								String type = jsonData.getString("type");
								cleanList(Id, type);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}else if(handler.equalsIgnoreCase("com.port.service.Catalogue.unsubscription")){
				try {
					if(jsonData != null){
						if(jsonData.has("result")){
							if(jsonData.getString("result").equalsIgnoreCase("success")){
								String Id = jsonData.getString("id");
								String type = jsonData.getString("type");
								cleanList(Id, type);
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
			}catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	}
	
	private void cleanList(String id, String type){
		if(type.equalsIgnoreCase("service")){
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.ServiceList :" + CacheDockData.ServiceList.size());
			ArrayList<ChannelInfo> defaultList = new ArrayList<ChannelInfo>();
			for (int i = 0; i < CacheDockData.ServiceList.size(); i++) {
				if (!CacheDockData.ServiceList.get(i).getId().equalsIgnoreCase(id)) {
					defaultList.add(CacheDockData.ServiceList.get(i));
				}
			}
			CacheDockData.ServiceList.clear();
			CacheDockData.ServiceList = defaultList;
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.ServiceList :" + CacheDockData.ServiceList.size());
		}else if(type.equalsIgnoreCase("event")){
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.EventList :" + CacheDockData.EventList.size());
			ArrayList<ProgramInfo> defaultList = new ArrayList<ProgramInfo>();
			for (int i = 0; i < CacheDockData.EventList.size(); i++) {
				if (!CacheDockData.EventList.get(i).getEventId().equalsIgnoreCase(id)) {
					defaultList.add(CacheDockData.EventList.get(i));
				}
			}
			CacheDockData.EventList.clear();
			CacheDockData.EventList = defaultList;
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.EventList :" + CacheDockData.EventList.size());
		}
	}
	
}
