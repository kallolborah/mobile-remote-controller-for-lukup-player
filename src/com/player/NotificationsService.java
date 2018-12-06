package com.player;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.player.util.Constant;
import com.player.util.SystemLog;
import com.player.util.Utils;

public class NotificationsService extends IntentService{
	
	private String TAG = "NotificationsService";	

	public NotificationsService() {
		super("NotificationsService");
	}	

	@Override
	protected void onHandleIntent(Intent intent) {
		if(Constant.DEBUG)  Log.d(TAG , "onHandleIntent: ");
		Bundle extras = intent.getExtras();
		String jsondata = "";
		String handler = "";

		if(extras != null){
			if(extras.containsKey("Params")){
				jsondata = extras.getString("Params");
			}
			if(extras.containsKey("Handler")){
				handler = extras.getString("Handler");
			}
			if(Constant.DEBUG)  Log.d(TAG  , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Recordings.sendRecordDetail")){
				if(jsondata != null){
					try {
						JSONObject jsonData = new JSONObject(jsondata);
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								String name = Utils.getDataFromJSON(jsonData, "name");
								String msg = Utils.getDataFromJSON(jsonData, "msg");
								if(name.equalsIgnoreCase("start")){
									Intent notification = new Intent();					
									notification.putExtra("Title", "New Record");
									notification.putExtra("Message", msg);
									notification.putExtra("Action", "com.player.apps.Plan");
									notification.setAction("UPDATES");
									sendBroadcast(notification);
								}else if(name.equalsIgnoreCase("end")){
									Intent notification = new Intent();					
									notification.putExtra("Title", "Recording done.");
									notification.putExtra("Message", msg);
									notification.putExtra("Action", "com.player.apps.Storage");
									notification.setAction("UPDATES");
									sendBroadcast(notification);
								}
							}else{
								String msg = Utils.getDataFromJSON(jsonData, "msg");
								Intent notification = new Intent();					
								notification.putExtra("Title", "Recording Failed.");
								notification.putExtra("Message", msg);
								notification.putExtra("Action", "com.player.apps.Plan");
								notification.setAction("UPDATES");
								sendBroadcast(notification);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
		    			StringWriter errors = new StringWriter();
		    			e.printStackTrace(new PrintWriter(errors));
		    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Reminders.sendReminderDetail")){
				if(jsondata != null){
					try {
						String name = Utils.getDataFromJSON(new JSONObject(jsondata), "name");
						Intent notification = new Intent();					
						notification.putExtra("Title", "New Reminder");
						notification.putExtra("Message", "Reminder for " + name +".");
//						notification.putExtra("Action", "com.player.apps.Plan");
						notification.setAction("UPDATES");
						sendBroadcast(notification);						
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.service.Catalogue.port-app-updates")){
				if(jsondata != null){
					try {
						String message = Utils.getDataFromJSON(new JSONObject(jsondata), "message");
						Intent notification = new Intent();					
						notification.putExtra("Title", "Player Software Update");
						notification.putExtra("Message", message);
						notification.setAction("UPDATES");
						sendBroadcast(notification);						
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_UPDATES, errors.toString(), e.getMessage());
					}
				}
			} else if(handler.equalsIgnoreCase("com.port.service.Catalogue.port-firmware-updates")){
				if(jsondata != null){
					try {
						String message = Utils.getDataFromJSON(new JSONObject(jsondata), "message");
						Intent notification = new Intent();					
						notification.putExtra("Title", "Player Software Update");
						notification.putExtra("Message", message);
						notification.setAction("UPDATES");
						sendBroadcast(notification);						
					} catch (Exception e) {
						e.printStackTrace();
		    			StringWriter errors = new StringWriter();
		    			e.printStackTrace(new PrintWriter(errors));
		    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_UPDATES, errors.toString(), e.getMessage());
					}
				}
			} else if(handler.equalsIgnoreCase("com.port.service.Catalogue.player-app-updates")){
				if(jsondata != null){
					try {
						String message = Utils.getDataFromJSON(new JSONObject(jsondata), "message");
						Intent notification = new Intent();					
						notification.putExtra("Title", "Player Application Update");
						notification.putExtra("Message", message);
						notification.putExtra("Action", "com.player.apps.AppGuide");
						notification.setAction("UPDATES");
						sendBroadcast(notification);						
					} catch (Exception e) {
						e.printStackTrace();
		    			StringWriter errors = new StringWriter();
		    			e.printStackTrace(new PrintWriter(errors));
		    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_UPDATES, errors.toString(), e.getMessage());
					}
				}
			} else if(handler.equalsIgnoreCase("com.port.service.Catalogue.player-firmware-updates")){
				if(jsondata != null){
					try {
						String message = Utils.getDataFromJSON(new JSONObject(jsondata), "message");
						Intent notification = new Intent();					
						notification.putExtra("Title", "Player Firmware Update");
						notification.putExtra("Message", message);
						notification.putExtra("Action", "com.player.apps.AppGuide");
						notification.setAction("UPDATES");
						sendBroadcast(notification);						
					} catch (Exception e) {
						e.printStackTrace();
		    			StringWriter errors = new StringWriter();
		    			e.printStackTrace(new PrintWriter(errors));
		    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_UPDATES, errors.toString(), e.getMessage());
					
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.DvbMsg.sendStatusMsg")){
				if(jsondata != null){
					try {
						String message = Utils.getDataFromJSON(new JSONObject(jsondata), "msg");
						Intent notification = new Intent();					
						notification.putExtra("Message", message);
						notification.setAction("DVBMSG");
						sendBroadcast(notification);						
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

}
