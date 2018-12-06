package com.player;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.player.util.Constant;
import com.player.util.DataAccess;
import com.player.util.DataStorage;
import com.player.util.SystemLog;
import com.player.util.Utils;

public class UpdateService extends IntentService{

	private String TAG = "UpdateService";
	
	
	public UpdateService() {
		super("UpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (Constant.DEBUG)	Log.d(TAG, "onHandleIntent");
		Bundle extras = intent.getExtras();
		String jsondata = "";
		String handler = "";
		
		DataAccess dataAccess = new DataAccess();

		if(extras != null){
			if(extras.containsKey("Params")){
				jsondata = extras.getString("Params");
			}
			if(extras.containsKey("Handler")){
				handler = extras.getString("Handler");
			}
			
			try{
				
				if(Layout.progressDialog.isShowing()){
					Layout.progressDialog.dismiss();
				}
				
				JSONObject jsonData = new JSONObject(jsondata);
				if(handler.equalsIgnoreCase("com.port.apps.epg.Devices.getOutputDeviceList")){
					if (Constant.DEBUG)	Log.d(TAG, "com.port.apps.epg.Devices.getOutputDeviceList");
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							Intent sender=new Intent();
							sender.setAction("pairedA2DP");
							sender.putExtra("Params",jsondata);
							sender.putExtra("Handler","com.port.apps.epg.Devices.getOutputDeviceList");
							sendBroadcast(sender);
						}
					}
				}
				if(handler.equalsIgnoreCase("com.port.apps.epg.A2DPDisconnector.Stop")){
					if(jsonData!=null){
						if(jsonData.has("result")){
							if(Utils.getDataFromJSON(jsonData, "result").equalsIgnoreCase("success")){
								DataStorage.setPlaying("audio","0");
							}
						}
					}					
				}
				if(handler.equalsIgnoreCase("com.port.apps.epg.Play.PlayOn")){ //stopping MP on TV before playing on secondary display
					if(jsonData != null){
						if(jsonData.has("state")){
							if(Utils.getDataFromJSON(jsonData, "state").equalsIgnoreCase("start")){
								try {
									DataStorage.setRunningStatus(true);
									if(Constant.DEBUG)  Log.d(TAG , "Status: "+DataStorage.isRunningStatus());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else if(Utils.getDataFromJSON(jsonData, "state").equalsIgnoreCase("stop")){
								try {
									DataStorage.setRunningStatus(false);
									DataStorage.setPlayingUrl("");
									if(Constant.DEBUG)  Log.d(TAG , "Status: "+DataStorage.isRunningStatus());
								}catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}							
							}
						}
					}
				}
				if(handler.equalsIgnoreCase("com.port.apps.epg.Play.Stop")){
					if(jsonData.has("state")){
						String state = Utils.getDataFromJSON(jsonData, "state");
						if (Constant.DEBUG)	Log.d(TAG, "processUIData() state: "+state);
						if(state.equalsIgnoreCase("stop")){
							DataStorage.setRunningStatus(false);
							DataStorage.setPlayingUrl("");
						}
					}
				}	
				
				if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.getDeviceInfo")){
					if(Constant.DEBUG)  Log.d(TAG," jsonData: "+jsonData);
					
					String PortMacAddress = Utils.getDataFromJSON(jsonData, "PortMac");
					String SubscriberId = Utils.getDataFromJSON(jsonData, "subscriberid");
					String operator  = Utils.getDataFromJSON(jsonData, "operatorname");
					String status  = Utils.getDataFromJSON(jsonData, "videostatus");
					String ip = Utils.getDataFromJSON(jsonData, "IP");
					operator = operator.replaceAll("\\s+","");
					
					if(DataStorage.getBTAddress().equalsIgnoreCase("") || DataStorage.getBTAddress().equalsIgnoreCase(PortMacAddress)){
						if(DataStorage.getSubscriberId()==null || DataStorage.getSubscriberId().equalsIgnoreCase(SubscriberId)){
		
							if(Constant.DEBUG)  Log.d(TAG," onHandleIntent: Updating DeviceType & Vendor");
							DataStorage.setAboutInfo(jsonData);
							String DeviceType = Utils.getDataFromJSON(jsonData, "Model");
							DataStorage.setDeviceType(DeviceType);
							DataStorage.setConnectedVendor(operator);
							DataStorage.setSubscriberId(SubscriberId);
							
							//28th Aug - Updating the ContentDB with the received details from Port
							dataAccess.updateSetupDB("DeviceType",DeviceType);
							dataAccess.updateSetupDB("SubscriberId",SubscriberId);
							dataAccess.updateSetupDB("ConnectedVendor",operator);
							
							DataStorage.setIPAddress(ip);
							DataStorage.setBTAddress(PortMacAddress);
							
							if (SubscriberId!=null && !SubscriberId.equalsIgnoreCase("")){
								DataStorage.SetupDone(true);
							}
							if (status.equalsIgnoreCase("true")) {
								DataStorage.setRunningStatus(true);
							} else if (status.equalsIgnoreCase("false")) {
								DataStorage.setRunningStatus(false);
								DataStorage.setPlayingUrl("");
							}
						}
					}else if(!DataStorage.getBTAddress().equalsIgnoreCase(PortMacAddress)){ 
						if(!DataStorage.getSubscriberId().equalsIgnoreCase(SubscriberId)){						
							//Player.mBluetoothService.stop(); -- 27th July - Sudharsan R - commented. There is no valid mBluetoothService object.						
						}
					}
				}
				if (handler.equalsIgnoreCase("com.port.apps.epg.Devices.playA2dp")){
					try{
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								String state = Utils.getDataFromJSON(jsonData, "state");
								String deviceName = Utils.getDataFromJSON(jsonData, "name");
								if (state.equalsIgnoreCase("stop")){	// Active Stop
									DataStorage.setPlaying("audio", "0");
									DataStorage.setA2dpDevice(deviceName);
								}else if (state.equalsIgnoreCase("start")){ // Active Play On 
									DataStorage.setPlaying("audio", "1");
									String address = Utils.getDataFromJSON(jsonData, "address");
									DataStorage.setConnectedBTDevice(address);
									DataStorage.setA2dpDevice(deviceName);
								}
							}else{
								DataStorage.setPlaying("audio", "0");
								DataStorage.setA2dpDevice("");
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}	
				}
				if (handler.equalsIgnoreCase("com.port.apps.epg.Devices.playRemoteDisplay")){
					try{
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								String state = Utils.getDataFromJSON(jsonData, "state");
								String deviceName = Utils.getDataFromJSON(jsonData, "name");
								if (state.equalsIgnoreCase("stop")){	// Active Stop
									DataStorage.setPlaying("video", "0"); //0 for stop
									DataStorage.setWifiDisplayDevice(deviceName);
								}else if (state.equalsIgnoreCase("start")){ // Active Play On
									DataStorage.setPlaying("video", "1"); //1 for play
									
									String address = Utils.getDataFromJSON(jsonData, "address");
									DataStorage.setConnectedBTDevice(address);
									DataStorage.setWifiDisplayDevice(deviceName);
									
								}
							}else{
								DataStorage.setPlaying("video", "0");
								DataStorage.setWifiDisplayDevice("");
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}	
				}
			}catch(Exception e){
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
	
}
