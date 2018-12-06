package com.player.apps;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.player.R;
import com.parse.ParseAnalytics;
import com.player.Layout;
import com.player.Player;
import com.player.action.HelpTip;
import com.player.network.ir.IRTransmitter;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.OperatorKeys;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.HelpText;

public class AppGuide extends Layout {
	
	private static AppGuide mAppGuideInstance;
	private String TAG = "AppGuide";
	private String ActivityName;
	private String operatorName;
	
	private ImageView setting;
	private ImageView search;
	private ImageView playList;
	private ImageView storage;
	private ImageView tvGuide;
	private ImageView plan;
	private ImageView power;
	private ImageView remote;
	private ImageView game;
	private ImageView homeSecure;
	private ImageView screens;
	private ImageView TV;
	
	private TextView tvText;
	private TextView settingText;
	private TextView screenText;
	private TextView guidtext;
	private TextView searchText;
	private TextView remoteText;
	private TextView storageText;
	@SuppressWarnings("unused")
	private TextView playListText;
	private TextView planText;
	private TextView powerText;
	
	public static SharedPreferences hostDetails;
	public static SharedPreferences.Editor edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Constant.DEBUG) System.out.println("AppGuide showed");
		mAppGuideInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 

//		Player.setActivity("Home");
//		ParseAnalytics.trackAppOpenedInBackground(getIntent());
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG  ,"Pre Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
		Bundle br = this.getIntent().getExtras();
		
		if(mDataAccess.getConnectionType().equalsIgnoreCase("NOTSET")){
			super.connectionStatus("false");
		}
		
		showAppGuideScreen();
//		if (br != null) {
//			if (br.containsKey("ActivityName")) {
//				ActivityName = br.getString("ActivityName");
//				if(ActivityName.equalsIgnoreCase("Notification")){
//					if(br.getString("Update").equalsIgnoreCase("firmware")){
//						unpackZip("/", "playerfirmware.zip");
//					}else if(br.getString("Update").equalsIgnoreCase("app")){
//						unpackZip("/", "playerapp.zip");
//					}					
//				}
//			}
//		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.DEBUG)	Log.d(TAG, "onResume()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
	}
	
	
	@Override
	public void finish() {
		super.finish();
	}

	private void showAppGuideScreen() {
		try {
			if(mAppGuideInstance != null ){
				mAppGuideInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hideMenu();
						if(mContainer != null){
							if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
								mContainer.removeAllViews();
								LayoutInflater  inflater = mAppGuideInstance.getLayoutInflater();
								View appLayout = inflater.inflate(R.layout.appguide,null);
								mContainer.addView(appLayout);
								
								setting = (ImageView) appLayout.findViewById(R.id.imageView3);
								settingText = (TextView) appLayout.findViewById(R.id.set4);
								
								search = (ImageView) appLayout.findViewById(R.id.imageView2);
								searchText =(TextView) appLayout.findViewById(R.id.set2);
								
								tvGuide = (ImageView) appLayout.findViewById(R.id.imageView5);
								guidtext = (TextView) appLayout.findViewById(R.id.set5);
								
								storage = (ImageView) appLayout.findViewById(R.id.imageView4);
								storageText = (TextView) appLayout.findViewById(R.id.set3);

								playList = (ImageView) appLayout.findViewById(R.id.imageView1);
								playListText = (TextView) appLayout.findViewById(R.id.set1);
								
								plan = (ImageView) appLayout.findViewById(R.id.imageView6);
								planText = (TextView) appLayout.findViewById(R.id.set6);
								
								screens = (ImageView) appLayout.findViewById(R.id.imageView9);
								screenText = (TextView) appLayout.findViewById(R.id.set7);
								tvText = (TextView) appLayout.findViewById(R.id.set10);
								
								remote = (ImageView) appLayout.findViewById(R.id.imageView7);
								remoteText = (TextView)appLayout.findViewById(R.id.set10);
								
								power = (ImageView) appLayout.findViewById(R.id.imageView8);
								powerText = (TextView) appLayout.findViewById(R.id.set8);
								
								if(Constant.DVB){
									remote.setVisibility(View.INVISIBLE);
									remoteText.setVisibility(View.INVISIBLE);
								}else{
									remote.setVisibility(View.INVISIBLE);
									remoteText.setVisibility(View.INVISIBLE);
									power.setVisibility(View.INVISIBLE);
									powerText.setVisibility(View.INVISIBLE);
								}
								
							}else{ //if app is not connected to the Player
								
								mContainer.removeAllViews();
								LayoutInflater  inflater = mAppGuideInstance.getLayoutInflater();
								View appLayout = inflater.inflate(R.layout.appguide,null);
								mContainer.addView(appLayout);
								setting = (ImageView) appLayout.findViewById(R.id.imageView1);
								settingText = (TextView) appLayout.findViewById(R.id.set4);
								
								search = (ImageView) appLayout.findViewById(R.id.imageView2);
								searchText =(TextView) appLayout.findViewById(R.id.set2);
								
								tvGuide = (ImageView) appLayout.findViewById(R.id.imageView5);
								guidtext = (TextView) appLayout.findViewById(R.id.set5);
								
								storage = (ImageView) appLayout.findViewById(R.id.imageView4);
								storageText = (TextView) appLayout.findViewById(R.id.set3);
								
								playList = (ImageView) appLayout.findViewById(R.id.imageView1);
								playListText = (TextView) appLayout.findViewById(R.id.set1);
								playList.setVisibility(View.INVISIBLE);
								playListText.setVisibility(View.INVISIBLE);
								
								plan = (ImageView) appLayout.findViewById(R.id.imageView6);
								planText = (TextView) appLayout.findViewById(R.id.set6);
								plan.setVisibility(View.INVISIBLE);
								planText.setVisibility(View.INVISIBLE);
								
								screenText = (TextView) appLayout.findViewById(R.id.set7);
								screens = (ImageView) appLayout.findViewById(R.id.imageView9);
								screens.setVisibility(View.INVISIBLE);
								tvText = (TextView) appLayout.findViewById(R.id.set10);
								screenText.setVisibility(View.INVISIBLE);
								tvText.setVisibility(View.INVISIBLE);
								
								remote = (ImageView) appLayout.findViewById(R.id.imageView7);
								remoteText = (TextView)appLayout.findViewById(R.id.set10);
								remote.setVisibility(View.INVISIBLE);
								remoteText.setVisibility(View.INVISIBLE);
								
								power = (ImageView) appLayout.findViewById(R.id.imageView8);
								powerText = (TextView) appLayout.findViewById(R.id.set8);
								power.setVisibility(View.INVISIBLE);
								powerText.setVisibility(View.INVISIBLE);
							}
						
							//HelpTip
							HelpTip.requestForHelp(mAppGuideInstance.getResources().getString(R.string.APPGUIDE),
									mAppGuideInstance.getResources().getString(R.string.APPGUIDE_MSG1),mAppGuideInstance);
						}
					}
				});
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	public void OnSettings(View V){
		setting.setImageResource(R.drawable.v13_ico_settings_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent intent = new Intent(AppGuide.this,Setup.class);
			intent.putExtra("ActivityName", "AppGuide");
	    	startActivity(intent);
	    	finish();
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		try{
	        if(Constant.DEBUG)  Log.d(TAG ,"OnSettings:");
	        setting.setImageResource(R.drawable.v13_ico_settings_01_a);
	    }catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
		
	}
	
	@SuppressWarnings("unused")
	public void OnTVGuide(View V){
		tvGuide.setImageResource(R.drawable.v13_ico_tvguide_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent intent = new Intent(AppGuide.this,Guide.class);
			intent.putExtra("ActivityName", "AppGuide");
	    	startActivity(intent);
	    	finish();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		try{
	        if(Constant.DEBUG)  Log.d(TAG ,"OnTVGuide:");
	        tvGuide.setImageResource(R.drawable.v13_ico_tvguide_01_a);
	    }catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
		
	}	

	@SuppressWarnings("unused")
	public void OnSearch(View V){
		search.setImageResource(R.drawable.v13_ico_search_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent intent = new Intent(AppGuide.this,Search.class);
			intent.putExtra("ActivityName", "AppGuide");
	    	startActivity(intent);
	    	finish();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		try{
	        if(Constant.DEBUG)  Log.d(TAG ,"OnSearch:");
	        search.setImageResource(R.drawable.v13_ico_search_01_a);
	    }catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}	
	
	public void OnPlaylist(View V){
		playList.setImageResource(R.drawable.v13_ico_playlist_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent playlist = new Intent(AppGuide.this,PlayList.class);
			playlist.putExtra("ActivityName", "AppGuide");
	    	startActivity(playlist);
	    	finish();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		try{
	        if(Constant.DEBUG)  Log.d(TAG ,"OnPlaylist:");
	        playList.setImageResource(R.drawable.v13_ico_playlist_01_a);
	    }catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	@SuppressWarnings("unused")
	public void OnStorage(View V){
		storage.setImageResource(R.drawable.v13_ico_cloud_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent s = new Intent(AppGuide.this,Storage.class);
			s.putExtra("ActivityName", "AppGuide");
	    	startActivity(s);
	    	finish();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		try{
	        if(Constant.DEBUG)  Log.d(TAG ,"OnStorage:");
	        storage.setImageResource(R.drawable.v13_ico_cloud_01_a);
	    }catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnPlan(View V){
		plan.setImageResource(R.drawable.v13_ico_plan_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent s = new Intent(AppGuide.this, Plan.class);
			s.putExtra("ActivityName", "AppGuide");
			startActivity(s);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		try{
	        if(Constant.DEBUG)  Log.d(TAG ,"OnPlan:");
	        plan.setImageResource(R.drawable.v13_ico_plan_01_a);
	    }catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnRemote(View V){
		remote.setImageResource(R.drawable.v13_ico_remote_01_b);
		HelpTip.close(mAppGuideInstance);
		if(Constant.DVB){
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent s = new Intent(AppGuide.this, DVBRemote.class);
			s.putExtra("ActivityName", "AppGuide");
			startActivity(s);
			finish();
		}else{
			try {
				if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent s = new Intent(AppGuide.this, TVRemote.class);
				s.putExtra("ActivityName", "AppGuide");
				s.putExtra("operatorName", operatorName);
				startActivity(s);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
		}
        remote.setImageResource(R.drawable.v13_ico_remote_01_a);
	}
	
	public void onScreens(View V){
		screens.setImageResource(R.drawable.v13_ico_playon_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
			if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent intent = new Intent(AppGuide.this,Screens.class);
			intent.putExtra("ActivityName", "AppGuide");
	    	startActivity(intent);
	    	finish();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		try{
	        if(Constant.DEBUG)  Log.d(TAG ,"onScreens:");
	        screens.setImageResource(R.drawable.v13_ico_playon_01_a);
	    }catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}	
	
	public void OnPower(View V) throws InterruptedException{
		
		power.setImageResource(R.drawable.v13_ico_powerswitch_01_b);
		HelpTip.close(mAppGuideInstance);
		try {
				if(Constant.DEBUG)  Log.d(TAG ," OnPower PowerStatus: "+DataStorage.getPowerStatus());
//			if(DataStorage.getPowerStatus()){
//				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//				HashMap<String, String> list = new HashMap<String, String>();
//				list.put("consumer", "TV");
//				list.put("network",mDataAccess.getConnectionType());
//				list.put("caller", "com.player.apps.AppGuide");
//				list.put("called", "startService");
//				dispatchHashMap.add(list);
//				String method = "com.port.apps.settings.Settings.powerOff"; 
//				new AsyncDispatch(method, dispatchHashMap,false).execute();
//				
//				Thread.sleep(3000);
//				
//				DataStorage.setPowerStatus(false);
//				Player.readBTMessage = "";
////				Player.isRemoteConnected = false;
//				mDataAccess.updateSetupDB("RemoteConnected", "false");
//				Player.mBluetoothService.stop();
//				
//				int irHandler = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, "broadcom");
//				IRTransmitter.IRInput(OperatorKeys.BROADCOM_POWER, irHandler, "broadcom");
//				
//			}else{
//				int irHandler = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, "broadcom");
//				IRTransmitter.IRInput(OperatorKeys.BROADCOM_POWER, irHandler, "broadcom");
//				
//				Thread.sleep(3000);
//				
//				DataStorage.setPowerStatus(true);
//			}
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.AppGuide");
			list.put("called", "messageActivity");
			dispatchHashMap.add(list);
			String method = "com.port.apps.epg.Home.power"; 
			new AsyncDispatch(method, dispatchHashMap,false).execute();

//			HelpText.showHelpTextDialog(mAppGuideInstance, mAppGuideInstance.getResources().getString(R.string.POWER_BUTTON_MSG), 3000);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	    if(Constant.DEBUG)  Log.d(TAG ," OnPower changed PowerStatus: "+DataStorage.getPowerStatus());
	    power.setImageResource(R.drawable.v13_ico_powerswitch_01_a);
	}

	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if(!Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				if(Constant.DEBUG)  Log.d(TAG ,"App in Mobile mode");
				finish();
			} else {
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				finish();
			}
		}	
		return super.onKeyUp(keyCode, event);
	}	
	
	
}
