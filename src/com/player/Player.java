
/**
* Classname : Player
* 
* Version information : 1.0
* 
* Date : 19th Aug 2015
* 
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/
package com.player;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.player.action.Push;
import com.player.network.BluetoothConnectionService;
import com.player.network.BluetoothConnectionService.BTBinder;
import com.player.network.WifiDirectBroadcastReceiver;
import com.player.network.WifiP2PConnectionService;
import com.player.network.WifiP2PListener;
import com.player.network.WifiP2PConnectionService.WifiBinder;
import com.player.util.Constant;
import com.player.util.DataAccess;
import com.player.util.DataStorage;
import com.player.util.GetJSONData;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;

/**
 * Player 	: Application class associated with all the player side applications. 
 * 			  Interacts with BluetoothConnectionService 
 *
 * @Version : 1.0
 * @Author  : Lukup
 */
public class Player extends Application {
		
	//Debugging Related
	private static String TAG = "Player";
	
	//Flag to indicate the helptip status
	public static boolean help = true;

	//Messaging related
	protected static ArrayList<HashMap<String,String>> dispatchHashMap = null;
	private JSONObject jsonData; 
	
	//WifiP2P related
	private WifiP2pManager mManager;
	private Channel mChannel;
	private BroadcastReceiver mReceiver;
	private IntentFilter mIntentFilter;
	public WifiP2pDevice p2pdevice;
	public static boolean mWifiBound;
	public static WifiP2PConnectionService mWifiP2PService;
	private static String p2paddress="";
	public WifiManager wifi;
	private static String macAddress="";
	
	//Wifi related
	ArrayList<HashMap<String,String>> wifiList = new ArrayList<HashMap<String,String>>();
	private String hotspotaddress="";
	
	//Global Application Object 
	public static Player player;
	
	//BT related
	public static BluetoothAdapter mAdapter;
	public static String readBTMessage = "";
	public static BluetoothConnectionService mBluetoothService;
	private static Messenger mMessenger;
	private static String address="";
	public static boolean mBTBound;
	
	//Application status related preferences
	public static SharedPreferences state;
	public static SharedPreferences.Editor edit;
	
	//Hold current Activity associated with the Application Object
	public static DataAccess mDataAccess;
	
	//Parse
	public static ParseInstallation installation;
	
	/**
	 * Callback method - Called when the application starts
	 */
	@Override
	public void onCreate() {
		
		super.onCreate();
		
		player = this; 
		
		if(mAdapter == null)
		{
			Log.d(TAG, "handleMessage : mAdapter is null");
			mAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		if (mAdapter != null) {
			Log.d(TAG, "handleMessage : mAdapter is not null");
			if (!mAdapter.isEnabled()) {
				mAdapter.enable();
			}
		}
		
		try{
			wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			if(!wifi.isWifiEnabled()){
				wifi.setWifiEnabled(true);
			}
		}catch(Exception e){
			if (Constant.DEBUG)	Log.d(TAG, "Error enabling Wifi");
		}	
		
		mBTConnectionReceiver mr = new mBTConnectionReceiver();
        IntentFilter found = new IntentFilter();
        found.addAction(BluetoothDevice.ACTION_FOUND);
        found.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        found.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mr, found);
		
		initializeSetupDB();
		mDataAccess = new DataAccess();
		if( mDataAccess.getHelptip() != null && mDataAccess.getHelptip().equalsIgnoreCase("true"))
			help = true;
		else
			help = false;
		
		if(mDataAccess.getConnectionType().equalsIgnoreCase("BT")){
			if (Constant.DEBUG)	Log.d(TAG, " Starting bluetooth communication from Player");
			startBluetoothCommunication();
		}else if(mDataAccess.getConnectionType().equalsIgnoreCase("Wifi")){
			if (Constant.DEBUG)	Log.d(TAG, " Starting wifi p2p communication from Player");
			startWifiP2PCommunication();
		}
		
		WifiInfo info = wifi.getConnectionInfo();
		macAddress = info.getMacAddress();
		
		Parse.initialize(player, "rjzvMqPaEhZwcNb96exJzieYbh7Iu1UHKpZ7NDOj", "zqrBTyKzoKLjZDRWvFGd54gyAkfsnfuYr4eNW4nI");
		installation = ParseInstallation.getCurrentInstallation();
		if(!mDataAccess.getSubscriberID().equalsIgnoreCase("") && !mDataAccess.getConnectedVendor().equalsIgnoreCase("")){
			ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
			query.whereEqualTo("subscriber", mDataAccess.getSubscriberID());
			query.findInBackground(new FindCallback<ParseInstallation>() {
				@Override
				public void done(List<ParseInstallation> arg0, ParseException arg1) {
					// TODO Auto-generated method stub
					if(arg0!=null && !arg0.isEmpty()){
						if(Constant.DEBUG) Log.d(TAG, "Already saved " + arg0.toString());
					}else{
						if(Constant.DEBUG) Log.d(TAG, "Registering with Parse");
						installation.put("subscriberid", mDataAccess.getSubscriberID());
						installation.put("channel",mDataAccess.getConnectedVendor().replaceAll("\\s", ""));
						installation.put("environment",Constant.parse_url);
						installation.saveInBackground();
					}
				}
			});
		}
//		ParseObject.registerSubclass(Push.class);
	}
	
	/**
	 * Returns global player application object
	 */
	public static Player getContext() 
	{
	    return player;
	}
	
	/***********************************SERVICE CONNECTION AND DISPATCHES****************************/
		
	
	/**
	 * The Handler that gets information back from the BluetoothService
	 */		
	public class mBluetoothHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case ScreenStyles.CONNECTION_STATE_CHANGE :
				switch(msg.arg1){
				case WifiP2PConnectionService.STATE_CONNECTED :
//					if(mDataAccess.getConnectionType().equalsIgnoreCase("")){
						mDataAccess.updateSetupDB("RemoteConnected", "true");
						mDataAccess.updateSetupDB("ConnectionType", "Wifi");
						mDataAccess.updateSetupDB("WIFIAddress", p2paddress);
						DataStorage.setMobile(false);
						intent.setAction("com.layout.CONNECTED"); //connected by WifiP2P
						intent.putExtra("RemoteConnected", "ok");
						sendBroadcast(intent);
//					}	
					if(Constant.DEBUG)  Log.d(TAG, "WIFI_STATE_CHANGE: STATE_CONNECTED " + mDataAccess.getConnectionType());
					break;
				case WifiP2PConnectionService.STATE_NONE :
					if(Constant.DEBUG)  Log.d(TAG, "WIFI_STATE_CHANGE: STATE_DISCONNECTED");
					mDataAccess.updateSetupDB("RemoteConnected", "false");
                    mDataAccess.updateSetupDB("ConnectionType", "");
					mDataAccess.updateSetupDB("WIFIAddress", "");
                    intent.setAction("com.layout.CONNECTED"); //connected by WifiP2P
                    intent.putExtra("RemoteConnected", "false");
                    sendBroadcast(intent);
                    break;
				}
				break;
			case ScreenStyles.MESSAGE_STATE_CHANGE:
				if(Constant.DEBUG)  Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothConnectionService.STATE_CONNECTED:
//					if(mDataAccess.getConnectionType().equalsIgnoreCase("")){
						mDataAccess.updateSetupDB("RemoteConnected", "true");
						mDataAccess.updateSetupDB("ConnectionType", "BT");
						mDataAccess.updateSetupDB("BTAddress", address);
						DataStorage.setMobile(false);					
						intent.setAction("com.layout.CONNECTED"); //connected by BT
						intent.putExtra("RemoteConnected", "ok");
						sendBroadcast(intent);	
//					}
					if(Constant.DEBUG)  Log.d(TAG, "MESSAGE_STATE_CHANGE: STATE_CONNECTED " + mDataAccess.getConnectionType());
					break;
				case BluetoothConnectionService.STATE_CONNECTING:
					if(Constant.DEBUG)  Log.d(TAG, "MESSAGE_STATE_CHANGE: STATE_CONNECTING");
					break;
				case BluetoothConnectionService.STATE_LISTEN:
					if(Constant.DEBUG)  Log.d(TAG, "MESSAGE_STATE_CHANGE: STATE_LISTEN");
					break;
				case BluetoothConnectionService.STATE_NONE:
					if(Constant.DEBUG)  Log.d(TAG, "MESSAGE_STATE_CHANGE: STATE_NONE");
//					readBTMessage = "";
//					mDataAccess.updateSetupDB("RemoteConnected", "false");
//					mDataAccess.updateSetupDB("ConnectionType", "");
//					mDataAccess.updateSetupDB("BTAddress", "");
//					intent.setAction("com.layout.CONNECTED"); //connected by BT
//					intent.putExtra("RemoteConnected", "false");
//					sendBroadcast(intent);	
					break;
				}
				break;
			case ScreenStyles.MESSAGE_WRITE: 
				if(Constant.DEBUG)  Log.d(TAG, "MESSAGE_WRITE: STATE_CONNECTED");
				byte[] writeBuf = (byte[]) msg.obj;
				String writeMessage = new String(writeBuf);
				readBTMessage = "";
				break;
			case ScreenStyles.MESSAGE_READ:
				try{
					if(Constant.DEBUG)  Log.d(TAG, "Handler : MESSAGE_READ:");
					if(Constant.DEBUG)  Log.d(TAG, "Handler : MESSAGE_READ:in bytes"+ msg.obj.toString());
					Bundle b = new Bundle(); 
					b = (Bundle)msg.obj;
					byte[] readBuf = b.getByteArray("portmsg");
					String readMessage = new String(readBuf, 0, msg.arg1);
					writeMessage(readMessage.trim());
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				}
				break;
			case ScreenStyles.MESSAGE_DEVICE_NAME:
				msg.getData().getString(ScreenStyles.DEVICE_NAME);
				break;
			case ScreenStyles.MESSAGE_TOAST:
				readBTMessage = "";
				mDataAccess.updateSetupDB("RemoteConnected", "false");
				mDataAccess.updateSetupDB("ConnectionType", "");
				mDataAccess.updateSetupDB("BTAddress", "");
				if (Constant.DEBUG) Log.d(TAG, "Player:mBluetoothHandler:MESSAGE_TOAST");
				
				//Broadcasting the BT connection Status as Disconnected
				intent.setAction("com.layout.CONNECTED"); //connected by BT
				intent.putExtra("RemoteConnected", "false");
				sendBroadcast(intent);
				break;				
			}
		}
	}
		
		
	/**
	 * Frame the message read from the port
	 * @param - readMessage Message to be processed
	 */	
	private void writeMessage(String readMessage){	
		try{
			if(Constant.DEBUG)  Log.v("writeMessage","Read Message " + readMessage); 
			if(readBTMessage==""){
				readBTMessage = readMessage;
				if(!checkValidJson(readBTMessage.trim())){
					return;
				}
			}else{
				readBTMessage = readBTMessage + readMessage;
				if(!checkValidJson(readBTMessage.trim())){
					return;
				}
			}
			if(readBTMessage != null && !readBTMessage.equalsIgnoreCase("")){
				processMessage(readBTMessage);
				readBTMessage = "";
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
		}
	}

	/**
	 * Validate the JSON data whether it is valid or not
	 * @param - data  JSON data received from Port
	 */	
	private boolean checkValidJson(String data){		
		try{
			new JSONObject(data);
			return true;
		}catch (JSONException e) {
			return false;
		}
	}
	
	/**
	 * Process the message received from Port
	 * @param - message Information received from Port
	 */	
	public void processMessage(Object message) {
		if(Constant.DEBUG)  Log.d(TAG,"Getting info from Dock "+message);
		if((String)message != null){
		try {
			String pname = "";
			String id = "";
			String cname = "";
			String network = "";
			String handler = "";
			String called = "";
			String caller ="";
			if(message!=null && !(message.toString().trim().equalsIgnoreCase(""))) {
				JSONObject objData = new JSONObject(message.toString());
				JSONObject jsonObject = objData.getJSONObject("data");
				if(jsonObject != null){
					if(jsonObject.has("Producer")){
						pname = jsonObject.getString("Producer");
					}
					if(jsonObject.has("macID")){
						id = jsonObject.getString("macID");
					}
					if(jsonObject.has("Consumer")){
						cname = jsonObject.getString("Consumer");
					}
					if(jsonObject.has("Network")){
						network = jsonObject.getString("Network");
					}
					if(jsonObject.has("Handler")){
						handler = jsonObject.getString("Handler");	
					}	
					if(jsonObject.has("Called")){
						called = jsonObject.getString("Called");	
					}
					if(jsonObject.has("Caller")){
						caller = jsonObject.getString("Caller");	
					}
					}if(jsonObject.has("params")){
						jsonData = GetJSONData.getJsonData((String)message,"params");	
					}						
					
					if(Constant.DEBUG)  Log.d(TAG , " jsonData : "+jsonData.toString());
					if(jsonData != null){
						if(jsonData.has("netvalue")){
							String Msg = jsonData.getString("netvalue");
							Intent intent = new Intent();
							intent.setAction("com.layout.NWCONNECTED"); //connected by BT
							if(Msg.equalsIgnoreCase("100")){
								intent.putExtra("NetworkConnected", "ok");
								
							} else if(Msg.equalsIgnoreCase("0")){
								intent.putExtra("NetworkConnected", "false");
								
							}
							sendBroadcast(intent);	
						}
					}						
				
					if(id.equalsIgnoreCase(macAddress) || id.equalsIgnoreCase("")){
						if(Constant.DEBUG)  Log.d(TAG , "Producer: "+pname+", Network: "+network+", Consumer: "+cname);
						if(Constant.DEBUG)  Log.d(TAG , "Handler: "+handler);
						//push msg to Activity or Service
						if(called.equalsIgnoreCase("startActivity")){
							Intent intent = new Intent(this, Class.forName(caller));					
							intent.putExtra("Producer", pname);
							intent.putExtra("Network", network);
							intent.putExtra("Consumer", cname);
							intent.putExtra("Handler", handler);
							intent.putExtra("Params", jsonData.toString());
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
							getApplicationContext().startActivity(intent);
						}else if (called.equalsIgnoreCase("messageActivity")){
							Intent intent = new Intent();					
							intent.putExtra("Producer", pname);
							intent.putExtra("Network", network);
							intent.putExtra("Consumer", cname);
							intent.putExtra("Handler", handler);
							intent.putExtra("Params", jsonData.toString());
							intent.setAction(caller);
							sendBroadcast(intent);
						}else if (called.equalsIgnoreCase("startService")){
							if(Constant.DEBUG)  Log.d(TAG , "called: "+called);
							Intent intent = new Intent(this, Class.forName(caller));					
							intent.putExtra("Producer", pname);
							intent.putExtra("Network", network);
							intent.putExtra("Consumer", cname);
							intent.putExtra("Handler", handler);
							intent.putExtra("Params", jsonData.toString());
							startService(intent);
						}else if (called.equalsIgnoreCase("messageService")){
							Intent intent = new Intent();					
							intent.putExtra("Producer", pname);
							intent.putExtra("Network", network);
							intent.putExtra("Consumer", cname);
							intent.putExtra("Handler", handler);
							intent.putExtra("Params", jsonData.toString());
							intent.setAction(caller);
							sendBroadcast(intent);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
    			StringWriter errors = new StringWriter();
    			e.printStackTrace(new PrintWriter(errors));
    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
    			StringWriter errors = new StringWriter();
    			e.printStackTrace(new PrintWriter(errors));
    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
		}		
	}
	
	/**
	 * Dispatch the method to be executed & data to be passed to the port
	 * @param - handler method to be executed on the port
	 * @param - params data to be sent to the port
	 */		
	public static void dispatchMethod(String handler,ArrayList<HashMap<String, String>> params){
		try{
			JSONObject sendResponse = new JSONObject();
			JSONObject parentData = new JSONObject();
			JSONObject childData = new JSONObject();

			if(params != null && params.size() > 0){
				for(HashMap<String, String> map : params){
				     for(Entry<String, String> mapEntry : map.entrySet()){
				        String key = mapEntry.getKey();
				        String value = mapEntry.getValue();
				        if(key.equalsIgnoreCase("consumer")){
				        	 parentData.put("consumer", value);
				        }else if(key.equalsIgnoreCase("network")){
				        	parentData.put("network", value);
				        }else if(key.equalsIgnoreCase("caller")){
				        	parentData.put("caller", value);
				        }else if(key.equalsIgnoreCase("called")){
				        	parentData.put("called", value);
				        }else {
				        	childData.put(key, value);
				        }
				     }
				}
			}
			
			try {
				parentData.put("handler", handler);
			    parentData.put("producer", "Player");
			    parentData.put("macID", macAddress);
			    parentData.put("params", childData);
			    sendResponse.put("data", parentData);

			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}

			if(mDataAccess.getConnectionType().equalsIgnoreCase("BT")){
				if(Constant.DEBUG)  Log.d(TAG, "Sending data through BT " + sendResponse.toString());
				try {
					mBluetoothService.write(sendResponse.toString().getBytes());
//					// Create a Message
//			        Message msg = Message.obtain(null, BluetoothConnectionService.MSG_WRITE, 0, 0);
//			        
//			        // Create a bundle with the data
//			        Bundle bundle = new Bundle();
//			        bundle.putString("message", sendResponse.toString());
//			 
//			        // Set the bundle data to the Message
//			        msg.setData(bundle);
//			 
//			        // Send the Message to the Service (in another process)
//			        try {
//			        	if(mMessenger != null)
//			        	{
//			        		if(Constant.DEBUG)  Log.d("Player","dispatchMethod is not null");
//			        		mMessenger.send(msg);
//			        	}
//			        	else
//			        		if(Constant.DEBUG)  Log.d("Player","dispatchMethod is null");
//			        } catch (RemoteException e) {
//			            e.printStackTrace();
//			        }
				} catch (Exception e) {
					e.printStackTrace();
	    			StringWriter errors = new StringWriter();
	    			e.printStackTrace(new PrintWriter(errors));
	    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				}
			}else if(mDataAccess.getConnectionType().equalsIgnoreCase("Wifi")){
				if(Constant.DEBUG)  Log.d(TAG, "Sending data through Wifi" + sendResponse.toString());
				try{
					mWifiP2PService.write(sendResponse.toString().getBytes());
					
				}catch(Exception e){
					
				}			
				
			}
			
		}catch (Exception e){
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	/**********************************Wifi P2P*****************************************************/
	
	private void startWifiP2PCommunication(){
		if(Constant.DEBUG)  Log.d("Player","In Wifi P2P communication");
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		
		getApplicationContext();
		mManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel,new WifiP2PListener().peerListListener, this);
		
		registerReceiver(mReceiver, mIntentFilter);
		
		Intent intent = new Intent(this, WifiP2PConnectionService.class);
		
		if(!isRemoteProcess(this, "wifi")){
			startService(intent);
			
		}
		
		mManager.discoverPeers(mChannel, new ActionListener(){
			
			@Override
			public void onSuccess(){
				if(Constant.DEBUG)  Log.d("Player","Discovering Wifi P2P peers");
			}
			
			@Override
			public void onFailure(int reason){
				if(Constant.DEBUG)  Log.d("Player","Wifi P2P peer discovery failed, reason " + reason);
			}
		});
		
	}
	
	public ServiceConnection wConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className,IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance	
			WifiBinder binder = (WifiBinder) service;
			mWifiP2PService = binder.getService();
			try {
				mWifiP2PService.start(new mBluetoothHandler(), hotspotaddress);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Constant.DEBUG)  Log.d(TAG , "Wifi hotspot connection initialized");
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Player.mWifiBound = false;
			if(Constant.DEBUG)  Log.d(TAG , "Wifi hotspot Disconnected");
		}
	};	
	
	public void findWifiP2PDevices(){
		
		if(wifi.getDhcpInfo().gateway!=0){
			HashMap<String,String> map = new HashMap<String, String>();
			wifiList.clear();
			String name = "LUKUP PLAYER";
			hotspotaddress = intToIP(wifi.getDhcpInfo().gateway);
			map.put("name", name);
			map.put("address", hotspotaddress);
			wifiList.add(map);
			if(Constant.DEBUG)  Log.d(TAG,"Name: "+name+", address: "+hotspotaddress);
						
			if (wifiList.size() == 0) {
				Log.d(TAG, "No wifi devices found");
			}else{
				if(Constant.DEBUG) Log.d(TAG, "wifiList.size(): "+wifiList.size());
				Intent peerBroadcast = new Intent();
				peerBroadcast.setAction("LUKUP_WIFI_PEERS");
				peerBroadcast.putExtra("State", "discovered");
				peerBroadcast.putExtra("Devices", wifiList);
				getApplicationContext().sendBroadcast(peerBroadcast);
			}
		}else{
			if(mManager==null){
				startWifiP2PCommunication();
			}
			else{
			
				mManager.discoverPeers(mChannel, new ActionListener(){
					
					@Override
					public void onSuccess(){
						if(Constant.DEBUG)  Log.d("Player","Discovering Wifi P2P peers");
					}
					
					@Override
					public void onFailure(int reason){
						if(Constant.DEBUG)  Log.d("Player","Wifi P2P peer discovery failed, reason " + reason);
					}
				});
			}
		}
	}

	
	public void bindWifiPeer(String device){
		if(Constant.DEBUG)  Log.d(TAG,"In bind Wifi Peer function" + device);
		
		p2paddress = device;
		if(device.equalsIgnoreCase(hotspotaddress)){
			if(mWifiP2PService!=null){
				if(mWifiP2PService.mState!=mWifiP2PService.STATE_CONNECTED){
					try {
						mWifiP2PService.start(new mBluetoothHandler(), hotspotaddress);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				if(Constant.DEBUG)  Log.d(TAG,"Binding to hotspot " + hotspotaddress);
				Intent intent = new Intent(Player.getContext(), WifiP2PConnectionService.class);
	            getApplicationContext().bindService(intent, wConnection, Context.BIND_AUTO_CREATE);
			}
		}else{
			WifiP2pConfig config = new WifiP2pConfig();
			config.deviceAddress = device;
			config.groupOwnerIntent = 0;
			config.wps.setup = WpsInfo.PBC;
			
			mManager.connect(mChannel, config, new ActionListener(){
	
				@Override
				public void onFailure(int arg0) {
					if(Constant.DEBUG)  Log.d(TAG, "Wifi P2P : STATE_DISCONNECTED");
				}
	
				@Override
				public void onSuccess() {
					if(Constant.DEBUG)  Log.d(TAG, "Wifi P2P : STATE_CONNECTED");
				}
				
			});
		}
	}
	
	public void unbindWifiPeer(){
		if(!hotspotaddress.equalsIgnoreCase("")){
			wifiList.clear();
			mWifiP2PService.connectionLost();
//			if(Constant.DEBUG)  Log.d(TAG, "WIFI_STATE_CHANGE: STATE_DISCONNECTED");
//			mDataAccess.updateSetupDB("RemoteConnected", "false");
//            mDataAccess.updateSetupDB("ConnectionType", "");
//			mDataAccess.updateSetupDB("WIFIAddress", "");
//			Intent intent = new Intent();
//            intent.setAction("com.layout.CONNECTED"); //connected by WifiP2P
//            intent.putExtra("RemoteConnected", "false");
//            sendBroadcast(intent);
		}else{
			if(p2pdevice.status==WifiP2pDevice.CONNECTED){
		         
		        mManager.removeGroup(mChannel, new ActionListener(){
		
		            @Override
		            public void onFailure(int reason) {
		                if(Constant.DEBUG)  Log.d(TAG, "Wifi direct disconnect failed, reason " + reason);
		               
		            }
		
		            @Override
		            public void onSuccess() {
		                if(Constant.DEBUG)  Log.d(TAG, "Wifi direct disconnect success");
		//                    mDataAccess.updateSetupDB("RemoteConnected", "false");
		//                    mDataAccess.updateSetupDB("ConnectionType", "");
		//					mDataAccess.updateSetupDB("WIFIAddress", "");
		//					
		//                    Intent intent = new Intent();                   
		//                    intent.setAction("com.layout.CONNECTED"); //connected by WifiP2P
		//                    intent.putExtra("RemoteConnected", "false");
		//                    sendBroadcast(intent);
		            }
		           
		           
		        });
		           
		    }else if(p2pdevice.status==WifiP2pDevice.INVITED || p2pdevice.status==WifiP2pDevice.AVAILABLE){
			
				mManager.cancelConnect(mChannel, new ActionListener(){
					
					@Override
					public void onSuccess() {
						if(Constant.DEBUG)  Log.d(TAG, "Wifi P2P : STATE_DISCONNECTED");
		//					mDataAccess.updateSetupDB("RemoteConnected", "false");
		//					mDataAccess.updateSetupDB("ConnectionType", "");
		//					mDataAccess.updateSetupDB("WIFIAddress", "");
		//					
		//					Intent intent = new Intent();					
		//					intent.setAction("com.layout.CONNECTED"); //connected by WifiP2P
		//					intent.putExtra("RemoteConnected", "false");
		//					sendBroadcast(intent);
					}
		
					@Override
					public void onFailure(int reason) {
						// TODO Auto-generated method stub
						if(Constant.DEBUG)  Log.d(TAG, "Unable to disconnect Wifi peer, reason " + reason);
					}
					
				});
		    }
		}
	}
	
	public String intToIP(int i) {
		return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF));
	}
	/***********************************BT**********************************************************/
	/**
	 * Function to start the Bluetooth Service
	 */	
	public void startBluetoothCommunication(){			
		//Start bluetooth listener
		Intent intent = new Intent(this, BluetoothConnectionService.class);
		boolean flag = false;
		
		if(Constant.DEBUG)  Log.d(TAG, "Player : Invoking Service");
		
		if(!isRemoteProcess(this, "bluetooth"))
		{
			startService(intent);
			if(Constant.DEBUG)  Log.d(TAG, "started Service");			
		}
		
		flag = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		if(Constant.DEBUG)  Log.d(TAG, "Player : flag" + flag);
	}
	
	
	/**
	 * ServiceConnection Object to indicate Bluetooth Service is connected or not
	 */		
	private ServiceConnection serviceConnection = new ServiceConnection() {
	    
		@Override
	    public void onServiceConnected(ComponentName name, IBinder service) {
			
            // Create the Messenger object
//	        mMessenger = new Messenger(service);
//	        final Messenger mHandler = new Messenger(new mBluetoothHandler());
	        
	        if(BluetoothConnectionService.mState!=BluetoothConnectionService.STATE_CONNECTED && BluetoothConnectionService.mState!=BluetoothConnectionService.STATE_CONNECTING){
	        	if(Constant.DEBUG)  Log.d(TAG , "Going to initialize BT since mState is " + BluetoothConnectionService.mState);
//	        	// Create a Message
//		        Message msg = Message.obtain(null, BluetoothConnectionService.MSG_START, 0, 0);
//		        msg.replyTo = mHandler;
//		        
//		        // Send the Message to the Service (in another process)
//		        try {
//		            mMessenger.send(msg);
//		        } catch (RemoteException e) {
//		            e.printStackTrace();
//		        }
	        	
	        	BTBinder binder = (BTBinder) service;
	        	mBluetoothService = binder.getService(new mBluetoothHandler());
				try {
					mBluetoothService.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        if(mDataAccess.getBTAddress().equalsIgnoreCase("")){
		        	getPairedDevices();
		        }
//		        else{
//		        	bindBTDevice(mDataAccess.getBTAddress());
//		        }
	        }
	    }
	 
		/**
		 * Callback method : When the client breaks the connection,resetting the flags.
		 * @param name  
		 */	
	    @Override
	    public void onServiceDisconnected(ComponentName name) {	    	
	    	
	        // unbind or process might have crashes
	    	if(Constant.DEBUG)  Log.d(TAG , "onServiceDisconnected : Making Messenger null and mBTBound false");
//	        mMessenger = null;
	        mBTBound = false;
	    }
	};	
	
	public void findBTDevice(){		
		if(Constant.DEBUG)Log.d(TAG, "FindBtdevice ==== Called from player " );
		
//		if(mMessenger==null){
		if(!mBTBound){
			startBluetoothCommunication();
		}else {	
			getPairedDevices();
		}
	}
	
	
	public static void bindBTDevice(String device){
//		Message msg = Message.obtain(null,BluetoothConnectionService.MSG_BIND);
//		 // Create a bundle with the data
//        Bundle bundle = new Bundle();
//        bundle.putString("device", device);
//        address = device.toString();
//        // Set the bundle data to the Message
//        msg.setData(bundle);
// 
//        // Send the Message to the Service (in another process)
//        try {
//        	if(mMessenger != null)
//        	{
//        		if(Constant.DEBUG)  Log.d("Player","Sending message to bind device");
//        		mMessenger.send(msg);
//        	}
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
		mBluetoothService.bindDevice(device.toString());
	}
	
	
	public static void unbindBTDevice(String device, boolean currentlyBound){
//		Message msg = Message.obtain(null,BluetoothConnectionService.MSG_UNBIND);
//		 // Create a bundle with the data
//       Bundle bundle = new Bundle();
//       bundle.putString("device", device);
//       bundle.putBoolean("currentlybound", currentlyBound);
//       // Set the bundle data to the Message
//       msg.setData(bundle);
//
//       // Send the Message to the Service (in another process)
//       try {
//	       	if(mMessenger != null)
//	       	{
//	       		if(Constant.DEBUG)  Log.d("Player","Sending message to unbind device");
//	       		mMessenger.send(msg);
//	       	}
//       } catch (RemoteException e) {
//           e.printStackTrace();
//       }
		mBluetoothService.unbindDevice(device, currentlyBound);
	}

	public static void stopBTService(){
		mBluetoothService.stop();
//		 Message msg = Message.obtain(null,BluetoothConnectionService.MSG_STOP);
//	
//	     // Send the Message to the Service (in another process)
//	     try {
//		      	if(mMessenger != null)
//		      	{
//		       		if(Constant.DEBUG)  Log.d("Player","Sending message to stop service");
//		       		mMessenger.send(msg);
//		       	}
//	     } catch (RemoteException e) {
//	          e.printStackTrace();
//	     }
	}
	
	/**
	 * To check whether the current process is from BT Service or from any other application
	 * @param context Current application context
	 * 
	 */
	private boolean isRemoteProcess(Context context, String servicename)
	{
	    Context applicationContext = context.getApplicationContext();
	    long myPid = (long) Process.myPid();
	    if(Constant.DEBUG)  Log.d(TAG,"isRemoteProcess : myPid" + myPid);
	    List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) applicationContext.getSystemService("activity")).getRunningAppProcesses();
	    if (runningAppProcesses != null && runningAppProcesses.size() != 0)
	    {
	        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
	        {
//	        	if(Constant.DEBUG)  Log.d(TAG,"isRemoteProcess : runningAppProcessInfo.processName" + runningAppProcessInfo.processName);
	            //if (((long) runningAppProcessInfo.pid) == myPid && "com.lukupplayer:bt_service".equals(runningAppProcessInfo.processName))
	        	if (((long) runningAppProcessInfo.pid) == myPid && runningAppProcessInfo.processName.contains(servicename))
	            {
	            	if(Constant.DEBUG)  Log.d(TAG,"isRemoteProcess : runningAppProcessInfo.processName matching :"+servicename);
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	
/*****************************CONTENT RESOLVER FOR PLAYER **********************************************/
	
	/**
	 * Initialize the SetupDB content Provider if not initialized
	 */		
	public void initializeSetupDB()
	{
		DataAccess dataAccess = new DataAccess();
	    Cursor countCursor = getContentResolver().query(DataAccess.CONTENT_URI,
		         new String[] {"count(*) AS count"},
		         null,
		         null,
		         null);

	    int nRecords = 0;
	    if(countCursor != null)
	    {
	    	countCursor.moveToFirst();
	    	nRecords = countCursor.getInt(0);
	    }
	    else
	    	nRecords = 0;
		
		if(Constant.DEBUG)  Log.d(TAG,"initializeSetupDB : nRecords" + nRecords);
		
		if( nRecords <= 0)
		{
			ContentValues values = new ContentValues();
			values.put("SubscriberID", "");
			values.put("DeviceType", "");
			values.put("ConnectedVendor", "");
			values.put("CurrentUserId", "1000");
			values.put("RemoteConnected", "false");
			values.put("Helptip", "true");
			values.put("Volume", 0);
			values.put("ConnectionType","NotSet");
			values.put("BTAddress", "");
			values.put("WIFIAddress", "");
			values.put("NetworkConnected", "");//
			values.put("IsSetup", "false");
			values.put("IsDlnaPlaying", "false");
			values.put("DlnaDeviceName","");
			Uri uri = getContentResolver().insert(DataAccess.CONTENT_URI, values);
			if(Constant.DEBUG)  Log.d(TAG,"initializeSetupDB : Insert uri" + uri);
		}
	}
	
	private class mBTConnectionReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(mAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
				if(Constant.DEBUG)  Log.d(TAG, "Bluetooth Device Discovery started ");
			}
			if(mAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				if(Constant.DEBUG)  Log.d(TAG, "Bluetooth Device Discovery finished ");
				if(address==""){
					Toast.makeText(getApplicationContext(), "No bluetooth devices found. Please try again.", Toast.LENGTH_LONG).show();
				}
				Intent rd = new Intent();
				rd.setAction("RemoveDialog");
				sendBroadcast(rd);
			}
			if (BluetoothDevice.ACTION_FOUND.equals(action)) { //after discovery has found some devices
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed already
				if(Constant.DEBUG)  Log.d(TAG, "Bluetooth Device Found "+device.getName());
				
					if(device.getName().indexOf(Constant.DeviceName) != -1){
						if(Constant.DEBUG)  Log.d(TAG, "Going to connect to "+device.getName());
						address = device.getAddress();
						intent = new Intent();					
						intent.setAction("DiscoveredBTAddress"); //connected by BT
						intent.putExtra("Name", device.getName());
						intent.putExtra("Address", address);
						sendBroadcast(intent);	
						if(Constant.DEBUG)  Log.d(TAG, "Sent msg to connect to "+ address);
					}
			} 
		}    	
    	
    }
    
    /**
    * Get the list of paired devices 
    */
    public void getPairedDevices(){
    	if(Constant.DEBUG)  Log.d(TAG, "getPairedDevices");
    	if(mAdapter != null)
    	{
    		if(Constant.DEBUG)  Log.d(TAG, "getPairedDevices : mAdapter is not null");
	    	 Set<BluetoothDevice> pairedDevice = mAdapter.getBondedDevices();
	    	 if(Constant.DEBUG)  Log.d(TAG, "getPairedDevices : pairedDevice.size()" + pairedDevice.size());
				if(pairedDevice.size()>0) {
					for(BluetoothDevice device : pairedDevice) {
						if(Constant.DEBUG)  Log.d(TAG, "Searched Devices "+device.getAddress() + "Connected Device " + device.getAddress());
						if(device.getName().indexOf(Constant.DeviceName) != -1){
							if(Constant.DEBUG)  Log.d(TAG , "getPairedDevices : Sending Broadcast after getting device address");
							address = device.getAddress();
							Intent intent = new Intent();					
							intent.setAction("PairedBTAddress"); //connected by BT
							intent.putExtra("Name", device.getName());
							intent.putExtra("Address", address);
							sendBroadcast(intent);	
							break;
						}
					}
				} 
				if(!mAdapter.isEnabled()){
					mAdapter.enable();
				}
				mAdapter.startDiscovery();
    	}
    }
    
}
