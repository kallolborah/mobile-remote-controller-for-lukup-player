package com.player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;
import com.player.util.AsyncDispatch;
import com.player.util.Constant;
import com.player.util.SystemLog;

public class Updates extends ParsePushBroadcastReceiver {

	private static final String TAG = "Updates";
	
	//Messaging related
	protected static ArrayList<HashMap<String,String>> dispatchHashMap = null;
	
	private Context c;

	@Override
	public void onPushReceive(Context context, Intent intent) {
		if (Constant.DEBUG) Log.d(TAG, "In updates ");
		try {
			String action = intent.getAction();
			c = context;
//			if (action.equals("com.port.UPDATE_STATUS")) {
				String channel = intent.getExtras().getString("com.parse.Channel");
				JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

				if (Constant.DEBUG) Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
				String message = json.getString("message");
				Log.d(TAG, "json message " + json);
				ParseAnalytics.trackAppOpened(intent);	
				try {
					if (message.trim().equalsIgnoreCase("featured")) {
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
//						list.put("Title", "featured");
						list.put("consumer", "TV");
						list.put("network",Player.mDataAccess.getConnectionType());
						list.put("caller", "com.player.Updates");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						String method = "com.port.apps.epg.Catalogue.getFeatured"; 
						new AsyncDispatch(method, dispatchHashMap,false).execute();
						
					} 
					else if (message.trim().equalsIgnoreCase("port-app-updates")) {
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
//						list.put("Title", "port-app-updates");
						list.put("url",json.getString("url"));
						list.put("consumer", "TV");
						list.put("network",Player.mDataAccess.getConnectionType());
						list.put("caller", "com.player.Updates");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						String method = "com.port.apps.epg.Catalogue.portAppUpdate"; 
						new AsyncDispatch(method, dispatchHashMap,false).execute();
						
					} else if (message.trim().equalsIgnoreCase("port-firmware-updates")) {
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
//						list.put("Title", "port-firmware-updates");
						list.put("url",json.getString("url"));
						list.put("consumer", "TV");
						list.put("network",Player.mDataAccess.getConnectionType());
						list.put("caller", "com.player.Updates");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						String method = "com.port.apps.epg.Catalogue.portFirmwareUpdate"; 
						new AsyncDispatch(method, dispatchHashMap,false).execute();
						
					} else if (message.trim().equalsIgnoreCase("player-app-updates")) {
						try {
							Intent notification = new Intent();					
							notification.putExtra("Title", "Application Update");
							notification.putExtra("Message", "Install the new version from Google Play store");
							notification.setAction("UPDATES");
							c.sendBroadcast(notification);						
						} catch (Exception e) {
							e.printStackTrace();
			    			StringWriter errors = new StringWriter();
			    			e.printStackTrace(new PrintWriter(errors));
			    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_UPDATES, errors.toString(), e.getMessage());
						}
//					else if (message.trim().equalsIgnoreCase("player-app-updates")) {
//						if(DownloadFile(json.getString("url"),"playerapp.zip")){
//    	        			unpackZip("/", "playerapp.zip");
//						}
//						
//					} 
//					else if (message.trim().equalsIgnoreCase("player-firmware-updates")) {
//						if(DownloadFile(json.getString("url"),"playerfirmware.zip")){
//    	        			unpackZip("/", "playerfirmware.zip");
//						}
						
					} else if (message.trim().equalsIgnoreCase("epg-updates")) {
						if (Constant.DVB) {	//DVB middleware
							if (Constant.DEBUG) Log.d(TAG, "Not in DVB Module");
						} else {
							dispatchHashMap  = new ArrayList<HashMap<String,String>>();
							HashMap<String, String> list = new HashMap<String, String>();
//							list.put("Title", "epg-updates");
							list.put("consumer", "TV");
							list.put("network",Player.mDataAccess.getConnectionType());
							list.put("caller", "com.player.Updates");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Catalogue.getVODUpdates"; 
							new AsyncDispatch(method, dispatchHashMap,false).execute();
						}
						
					} 
//					else if (message.trim().equalsIgnoreCase("vod-updates")) {
//						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//						HashMap<String, String> list = new HashMap<String, String>();
////						list.put("Title", "vod-updates");
//						list.put("consumer", "TV");
//						list.put("network",Player.mDataAccess.getConnectionType());
//						list.put("caller", "com.player.Updates");
//						list.put("called", "startService");
//						dispatchHashMap.add(list);
//						String method = "com.port.api.epg.service.Catalogue.getVODUpdates"; 
//						new AsyncDispatch(method, dispatchHashMap,false).execute();
//						
//					} 
					else if (message.trim().equalsIgnoreCase("subscription")) {
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
//						list.put("Title", "subscription");
						if(json.has("programid") && Integer.parseInt(json.getString("programid")) > 0){
							list.put("Id", json.getString("programid"));
							list.put("type", "event");
							list.put("consumer", "TV");
							list.put("network",Player.mDataAccess.getConnectionType());
							list.put("caller", "com.player.Updates");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Catalogue.subscription"; 
							new AsyncDispatch(method, dispatchHashMap,false).execute();
						}else if(json.has("channelid") && Integer.parseInt(json.getString("channelid")) > 0){
							list.put("Id", json.getString("channelid"));
							list.put("type", "service");
							list.put("consumer", "TV");
							list.put("network",Player.mDataAccess.getConnectionType());
							list.put("caller", "com.player.Updates");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Catalogue.subscription"; 
							new AsyncDispatch(method, dispatchHashMap,false).execute();
						}else if(json.has("packageid") && Integer.parseInt(json.getString("packageid")) > 0){
							list.put("Id", json.getString("packageid"));
							list.put("type", "package");
							list.put("consumer", "TV");
							list.put("network",Player.mDataAccess.getConnectionType());
							list.put("caller", "com.player.Updates");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Catalogue.subscription"; 
							new AsyncDispatch(method, dispatchHashMap,false).execute();
						}
						
					} else if (message.trim().equalsIgnoreCase("unsubscription")) {
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
//						list.put("Title", "unsubscription");
						if(json.has("programid") && Integer.parseInt(json.getString("programid")) > 0){
							list.put("Id", json.getString("programid"));
							list.put("type", "event");
							list.put("consumer", "TV");
							list.put("network",Player.mDataAccess.getConnectionType());
							list.put("caller", "com.player.Updates");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Catalogue.unsubscription"; 
							new AsyncDispatch(method, dispatchHashMap,false).execute();
						}else if(json.has("channelid") && Integer.parseInt(json.getString("channelid")) > 0){
							list.put("Id", json.getString("channelid"));
							list.put("type", "service");
							list.put("consumer", "TV");
							list.put("network",Player.mDataAccess.getConnectionType());
							list.put("caller", "com.player.Updates");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Catalogue.unsubscription"; 
							new AsyncDispatch(method, dispatchHashMap,false).execute();
						}else if(json.has("packageid") && Integer.parseInt(json.getString("packageid")) > 0){
							list.put("Id", json.getString("packageid"));
							list.put("type", "package");
							list.put("consumer", "TV");
							list.put("network",Player.mDataAccess.getConnectionType());
							list.put("caller", "com.player.Updates");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Catalogue.unsubscription"; 
							new AsyncDispatch(method, dispatchHashMap,false).execute();
						}
					}
					
				} catch(Exception e){
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_UPDATES, errors.toString(), e.getMessage());
				}
//			}

		} catch(Exception e){
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_UPDATES, errors.toString(), e.getMessage());
		}

	}
	
}
