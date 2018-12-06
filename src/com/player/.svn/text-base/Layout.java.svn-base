/**
* Classname : Layout
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;

import org.teleal.cling.model.meta.Device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.player.R;
import com.player.service.CacheDockData;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataAccess;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.widget.CustomMenu;
import com.player.widget.CustomMenu.OnMenuItemSelectedListener;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
//import com.api.layout.util.CacheDockData;
import com.player.widget.OverLay;

/**
 * Layout : This basic class is extended by activities of all the applications 
 *
 * @Version : 1.0
 * @Author  : Lukup
 */
public class Layout extends Activity implements OnMenuItemSelectedListener {
		
	//Layout related
	public CustomLayout customLayout;
	private Display display;
	private int maxX = 0;
	private int maxY = 0;
	public ImageButton bluetooth_indicator;
	protected ImageButton ethernet_indicator;
	private ImageButton battery_indicator;
	public ImageButton alert_indicator;
	private RelativeLayout mCustomStatusbar;
	public LinearLayout mBottomLayout;
	public LinearLayout mContainer;	
	private SeekBar mVolumeControlbar;
	private ImageButton mMuteButton;
	private ImageButton mFullvolumeBtn;
	public static ProgressDialog progressDialog;
	public static  Device<?, ?, ?> mCurrentDevice;
	
	//menu
	protected CustomMenu mMenu;
	protected  String[] customMenuValue = null;
	protected  int[] customMenuIcons = null;
	
	//Activity
	public static Activity mActivity;
	private String TAG = "Layout";
	private Bundle instanceState;
	
	//Volume Level
	int progressVal = 0;
	private AudioManager manager;	
	
	//Dialogs and indicators for battery
	private Dialog mBatteryIndicator_dialog = null;
	
	//Key input related
	public boolean isLukupSelected = false; //lukup button	
	public static boolean isDialogShow = false;
	
	// for IR transmitter
	private static String connectedVendor = "";	
	
	//Messaging related
	protected static ArrayList<HashMap<String,String>> dispatchHashMap = null;
	
	//Global Player Application Object to interface with BTConnectionService
	public static Player player;    

	//Application status related
	public static SharedPreferences state;
	public static SharedPreferences.Editor edit;
	Context HomeContext;
	public static DataAccess mDataAccess;
	
	/**
	 * Callback method - Called when the activity is starting
	 * @param savedInstanceState  Stored instance data
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		instanceState = savedInstanceState;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.player);
		mActivity = this;
		customLayout = new CustomLayout(this);
		display = getWindowManager().getDefaultDisplay();
		maxX = (int)customLayout.getSystemWidth();
		maxY = (int) customLayout.getSystemHeight();
		
		player = Player.getContext();		
		mDataAccess = new DataAccess();
		initUI();
		adjustStreamingVolume();
		
		//for receiving notifications
		IntentFilter ErrorLog = new IntentFilter("ERRORLOG");
		registerReceiver(mLogReceiver, ErrorLog);
		
		//Broadcast receiver to update the BT connection indicator with connection status
		IntentFilter btConnectedMsg = new IntentFilter("com.layout.CONNECTED");
		registerReceiver(btConnectedReceiver, btConnectedMsg);	
		
		//Broadcast receiver to update the BT connection indicator with connection status
		IntentFilter networkconnectedMsg = new IntentFilter("com.layout.NWCONNECTED");
		registerReceiver(networkconnectedReceiver, networkconnectedMsg);
		
		//Broadcast receiver to receive the DVB Messages
		IntentFilter dvbMsg = new IntentFilter("DVBMSG");
		registerReceiver(mDvbMsgReceiver, dvbMsg);	
		
		//Remove Progress Dialog
		IntentFilter RemoveDialogMsg = new IntentFilter("RemoveDialog");
		registerReceiver(removeDialogReceiver, RemoveDialogMsg);	
		
		//for receiving notifications
		IntentFilter updates = new IntentFilter("UPDATES");
		registerReceiver(mNotificationReceiver, updates);
		
	}
		
	/**
	 * Returns the global player application object
	 */
	public static Player getContext() 
	{
	    return player;
	}
	
	/**
	 * Call back method - called after OnCreate()
	 */
	@Override
	protected void onStart(){
		super.onStart();
		if(bluetooth_indicator != null){
			bluetooth_indicator.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
						if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("false"))
						{
							if(mDataAccess.getConnectionType().equalsIgnoreCase("BT") && !mDataAccess.getBTAddress().equalsIgnoreCase("")){
								Player.bindBTDevice(mDataAccess.getBTAddress());
							}else if(mDataAccess.getConnectionType().equalsIgnoreCase("Wifi") && !mDataAccess.getWifiAddress().equalsIgnoreCase("")){
								player.bindWifiPeer(mDataAccess.getWifiAddress());
							}else{
								HelpText.showHelpTextDialog(getParent(), "Go to Settings to set up network", 5000);
							}
						}
				}
			});

		}
		
		//Thread to update Bluetooth Indicator Icon with Connection Status
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {				
				if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
					if(Constant.DEBUG)  Log.d(TAG, "Bluethooth Indicator to ON");
					Drawable mBT_on_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_wifi_b_01);
					bluetooth_indicator.setBackgroundDrawable(mBT_on_drawable);
				}else{
					if(Constant.DEBUG)  Log.d(TAG, "Bluethooth Indicator OFF");
					Drawable mBT_off_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_wifi_a_01);
					bluetooth_indicator.setBackgroundDrawable(mBT_off_drawable);
				}	
			}
		});
			
		//Thread to update  Network Indicator Icon with Connection Status
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {					
				if(mDataAccess.isNetworkConnected().equalsIgnoreCase("true")){
				   	Drawable mRJ45_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_network_02);
	            	ethernet_indicator.setBackgroundDrawable(mRJ45_drawable);
				} else {
					Drawable mRJ45_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_network_01);
	            	ethernet_indicator.setBackgroundDrawable(mRJ45_drawable);
				}
			}
		});

	//	Pickup Volume from SetupDB
		
		progressVal = mDataAccess.getVolume();
		if(Constant.DEBUG)  Log.d(TAG, "onStart : progressVal" + progressVal);
	
		//Initializing the Volume bar with the volume from the Shared Preference
		setVolumeForComponents();		
		
		//Reading ConnectedVendor from ContentProvider - 12th Aug 15 - Sudharsan R 
//		String ConnectedVendor = null;
//		connectedVendor = mDataAccess.getConnectedVendor();
		
		//check battery charging or not
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		filter.addAction(Intent.ACTION_BATTERY_LOW);
		filter.addAction(Intent.ACTION_BATTERY_OKAY);
		registerReceiver(BatteryReceiver, filter);
		
	}
	
	/**
	 * Call back method - Called as part of the activity lifecycle when an activity is going into the background
	 */
	@Override
	protected void onPause() {
		super.onPause();

		if (Constant.DEBUG)	Log.d(TAG, "onPause : Volume setting " + progressVal);

		
		mDataAccess.updateSetupDBWithInt("Volume", progressVal);
	}
	
	/**
	 * Call back method - Called after OnPause to start interaction with user
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume(){
		super.onResume();
		if(instanceState!=null){
			CacheDockData.notificationList = (ArrayList<HashMap<String, String>>) instanceState.getSerializable("notificationList");
		}
		if(CacheDockData.notificationList.size()>0){
			showNotification();
		}else{
			alert_indicator.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle instanceState){
		if(Constant.DEBUG)  Log.d(TAG , "saving state of notifications and updates");
		instanceState.putSerializable("notificationList", CacheDockData.notificationList);
		super.onSaveInstanceState(instanceState);
	}

	/**
	 * Call back method - Called when the activity is destroyed
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (Constant.DEBUG)	Log.d(TAG, "onDestroy");
		
		//Unregistering the DVB Message Broadcast Receiver
		if(mDvbMsgReceiver!=null){
			unregisterReceiver(mDvbMsgReceiver);
		}
		
		//Unregistering the Battery Status Receiver
		if(BatteryReceiver!=null){
			unregisterReceiver(BatteryReceiver);
		}
		
		//Unregistering the Log notification receiver
		if(mLogReceiver!=null){
			unregisterReceiver(mLogReceiver);
		}
		
		//Unregistering the BT connection status receiver
		if(btConnectedReceiver != null)
		{
			if (Constant.DEBUG)	Log.d(TAG, "onDestroy : btConnectedReceiver is not null");
			unregisterReceiver(btConnectedReceiver);
		}
		
		//Unregistering the Network connection status receiver
		if(networkconnectedReceiver != null)
		{
			unregisterReceiver(networkconnectedReceiver);
		}
		
		if(removeDialogReceiver != null)
		{
			unregisterReceiver(removeDialogReceiver);
		}
		
		if(mNotificationReceiver!=null){
			unregisterReceiver(mNotificationReceiver);
		}
	
		//Destroy the progress Dialog if it exists
		if(progressDialog != null && progressDialog.isShowing()){
			if (Constant.DEBUG)	Log.d(TAG, "onDestroy : progressDialog is not null, removing");
			progressDialog.dismiss();
		}
	}

	/***********************************MENUS and NAVIGATION*****************************************/

	/** 
	 * For ContextMenu - Callback to handle onClick
	 * @param selection - menu item which is selected
	 */
	@Override
	public void onClickMenuItem(CustomMenuItem selection) {
		if (Constant.DEBUG)	Log.d(TAG, " onClickMenuItem() ");
		try {
			if(mBottomLayout != null){
				mBottomLayout.setVisibility(View.INVISIBLE);
			}
			if(Constant.DEBUG)  Log.d("Menu", selection.getCaption());
			processMenuActions(selection);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
		

	/**
	 * set CustomMenu for action
	 * 
	 */
	public void showCustomMenu() {
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {	
				showMenuItems();
				doMenu();
			}
		});
	}
	
	/**
	 * Display custom menu
	 * 
	 */
	public void doMenu() {
		try {
			if (mMenu.isShowing()) {
				if(Constant.DEBUG) Log.d(TAG, "Hiding Menu called ");
				mMenu.hide();
			} else {
				if(Constant.DEBUG) Log.d(TAG, "Showing Menu called ");
				if(customMenuValue == null){
					return;
				}
				if(mBottomLayout != null){
					mBottomLayout.setVisibility(View.INVISIBLE);
				}
				if(mContainer != null)
					mMenu.show(mContainer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
		
		
	/**
	 * Hide Menu from the screen.
	 */
	public void hideMenu() {
		try {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {	
					if(mMenu != null){
						if(mMenu.isShowing()){
							mMenu.hide();
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	/**
	 * Display menu items
	 * @param key - menu item which is selected
	 */
	public void showMenuItems() { 
		if(Constant.DEBUG)  Log.d(TAG , "showMenuItems() ");
		//to be implemented in activities
	}
	
	/**
	 * process ContextMenu Actions
	 * @param key - menu item which is selected
	 */
	public void processMenuActions(CustomMenuItem key) {
		
	}
		
	/************************************UI****************************************************/
	/**
	 * Initializing the custom layout
	 * 
	 */
	private void initUI() {
		if(Constant.DEBUG)  Log.d(TAG , "initUI ");
		try {
			mMenu = new CustomMenu(mActivity, this, getLayoutInflater());
			mCustomStatusbar = (RelativeLayout) findViewById(R.id.statusBar);
			mContainer = (LinearLayout) findViewById(R.id.navigationscreen);
			mBottomLayout = (LinearLayout) findViewById(R.id.bottomlayout);
			battery_indicator = (ImageButton) findViewById(R.id.sb_battry_button);
			bluetooth_indicator = (ImageButton) findViewById(R.id.sb_bluetooth_button);
			ethernet_indicator = (ImageButton) findViewById(R.id.sb_ethernet_button);
			alert_indicator = (ImageButton) findViewById(R.id.sb_alert_button);
			alert_indicator.setVisibility(View.INVISIBLE);
			mVolumeControlbar = (SeekBar) findViewById(R.id.whiteBalanceSeek);
			mMuteButton = (ImageButton) findViewById(R.id.seekbarSound_mute);
			mFullvolumeBtn = (ImageButton) findViewById(R.id.seekbarSound_loud);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	/**
	 * When volume is set to Mute in Volume Seek Bar
	 * @param v View
	 * 
	 */
	public void OnMuteBtn(View v) {
		if(Constant.DEBUG)  Log.d(TAG,"OnMuteBtn()");
		hideMenu();
		if(mVolumeControlbar != null){
			mVolumeControlbar.setProgress(0);
		}
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("position",0+"");
		list.put("consumer", "TV");
		list.put("network",mDataAccess.getConnectionType());
		list.put("caller", "com.player.apps.Layout");
		list.put("called", "startService");
		dispatchHashMap.add(list);

		String method = "com.port.apps.settings.Settings.VolumeControl"; 
		new AsyncDispatch(method, dispatchHashMap,false).execute();
	}
	
	/**
	 * When volume is set to Louder in Volume Seek Bar
	 * @param v View
	 * 
	 */
	public void OnLoudBtn(View v) {
		if(Constant.DEBUG)  Log.d(TAG,"OnLoudBtn()");
		hideMenu();
		if(mVolumeControlbar != null){
			mVolumeControlbar.setProgress(100);
		}

		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("position",100+"");
		list.put("consumer", "TV");
		list.put("network",mDataAccess.getConnectionType());
		list.put("caller", "com.player.apps.Layout");
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.settings.Settings.VolumeControl"; 
		new AsyncDispatch(method, dispatchHashMap,false).execute();
				
	}
	
	/**
	 * Adjust the streaming volume
	 */	
	public void adjustStreamingVolume()
	{
		manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		if(manager != null)
			manager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				ex.printStackTrace();
				if(ex instanceof Exception || ex instanceof RemoteException){
					if(Constant.DEBUG) Log.d(TAG, "Handled Exception Called");
				}else{
					System.exit(2);
					android.os.Process.killProcess(android.os.Process.myPid()); 
				}
			}			
		});
	}
	
	/**
	 * Update Volume level in Volume Progress Bar
	 * 
	 */
	private void setVolumeForComponents() {
		
		if(mVolumeControlbar != null){
			mVolumeControlbar.setProgress(progressVal);
			if (Constant.DEBUG)	Log.d(TAG, "Volume getting " + progressVal);
			
			mVolumeControlbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {					
					int arg1 = seekBar.getProgress();
					if(arg1 < 101){
						progressVal = arg1;
						if (Constant.DEBUG)	Log.d(TAG, "onStopTrackingTouch : Volume getting " + progressVal);
						new Thread(new Runnable() {
							@Override
							public void run() {
								
								dispatchHashMap  = new ArrayList<HashMap<String,String>>();
								HashMap<String, String> list = new HashMap<String, String>();
								list.put("position",progressVal+"");
								list.put("consumer", "TV");
								list.put("network",mDataAccess.getConnectionType());
								list.put("caller", "com.player.apps.Layout");
								list.put("called", "startService");
								dispatchHashMap.add(list);
								
								String method = "com.port.apps.settings.Settings.VolumeControl"; 
								new AsyncDispatch(method, dispatchHashMap,false).execute();
							}
						}).start();
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					try {
						if(Constant.DEBUG) Log.d(TAG, "On Touch Seek Bar");
						hideMenu();
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
					if(progress < 101){
						progressVal = progress;
						if (Constant.DEBUG)	Log.d(TAG, "onProgressChanged : Volume getting " + progressVal);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try{
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("position",progressVal+"");
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("caller", "com.player.apps.Layout");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String method = "com.port.apps.settings.Settings.VolumeControl"; 
									new AsyncDispatch(method, dispatchHashMap,false).execute();
									
								}catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						}).start();						
					}
				}
			});
		}
		
	}
				
	/**************************************NAVIGATION*********************************************************/
	
	/**
	 * Call back to handle when the key is released
	 * @ param keyCode - Key which is pressed
	 * @ param event - Description of the key event
	 * 
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) { 
		return super.onKeyUp(keyCode, event);
	}
		
		
		
	/********************************************Battery************************************************************/
	
	
	/**
	 * Display the Battery Charge Status
	 * @ param mess - Message to be displayed
	 * 
	 */
	private void showBatteryDryIndicator(final String mess) {
		if(mActivity != null){
			mActivity.runOnUiThread(new Runnable() {
				@SuppressLint("InflateParams")
				@Override
				public void run() {
					try{
						LayoutInflater inflater = mActivity.getLayoutInflater();
						mBatteryIndicator_dialog = new Dialog(mActivity,R.style.ThemeDialogCustom);
						View messageDialog = inflater.inflate(R.layout.overlay_message, null);
						TextView messageView = (TextView) messageDialog.findViewById(R.id.messageView);
						Button okBtn = (Button) messageDialog.findViewById(R.id.messageOkbtn);
						if(okBtn != null){
							okBtn.setVisibility(View.VISIBLE);
							okBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									if(mBatteryIndicator_dialog != null && mBatteryIndicator_dialog.isShowing()){
										mBatteryIndicator_dialog.cancel();
										isDialogShow = false;
									}
								}
							});
						}
						messageView.setText(mess);
						mBatteryIndicator_dialog.setContentView(messageDialog);
						mBatteryIndicator_dialog.show();
						isDialogShow = true;
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			});
		}
	}
	
	/**
	 * Broadcast receiver to intimate the battery charging status
	 * 
	 */
	public BroadcastReceiver BatteryReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, final Intent intent) { 
	    	if(Constant.DEBUG)  Log.d(TAG, "PowerConnectionReceiver() BroadcastReceiver " +BatteryManager.EXTRA_PLUGGED);
	        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
	        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
	        if(Constant.DEBUG)  Log.d(TAG, "PowerConnectionReceiver() status "+status);
	        if(Constant.DEBUG)  Log.d(TAG, "PowerConnectionReceiver() isCharging "+isCharging);
	        
	        if(isCharging){
	        	if(mActivity != null){
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(Constant.DEBUG)  Log.d(TAG, "PowerConnectionReceiver() Dock Mode" );
							Drawable mBat_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_battery_charging);
							battery_indicator.setBackgroundDrawable(mBat_drawable);
						}
					});
				}
	        }else{
	        	if(mActivity != null){
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// for Status bar Battery
							if(Constant.DEBUG)  Log.d(TAG, "PowerConnectionReceiver() Player Mode" );
							
							int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				        	int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				        	
				        	float pers = (level / (float)scale) * 100;
				        	if(Constant.DEBUG)  Log.d(TAG, "PowerConnectionReceiver() batteryPct "+pers);
							if(pers < 10){
								if(Constant.DEBUG)  Log.d(TAG,"Battery status Low: "+isDialogShow);
								if (!isDialogShow) {
									showBatteryDryIndicator("Low battery !");
								}
							}
				            if(battery_indicator != null && battery_indicator.getVisibility() == View.VISIBLE){
								Drawable mBat_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_battery_small_a_01);
								if(pers > 95){
									mBat_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_battery_small_e_01);
								}else if(pers > 80){
									mBat_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_battery_small_d_01);
								}else if(pers >60){
									mBat_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_battery_small_c_01);
								}else if(pers > 40){
									mBat_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_battery_small_b_01);
								}else if(pers > 10){
									mBat_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_battery_small_a_01);
								}
								battery_indicator.setBackgroundDrawable(mBat_drawable);
							}
						}
					});
				}
	        }
	        
	    }
	};
		
	/************************************* DISPATCH ******************************************************/	
	/**
	 * Updating Network Connection Indicator with current connection status
	 * @param  status indicating the current status
	 */
	private void networkStatus(String status){
		
		if(status.equalsIgnoreCase("ok")){
			Drawable mRJ45_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_network_02);
        	ethernet_indicator.setBackgroundDrawable(mRJ45_drawable);
        	mDataAccess.updateSetupDB("NetworkConnected", "true");
		}else if (status.equalsIgnoreCase("false")){
			Drawable mRJ45_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_network_01);
        	ethernet_indicator.setBackgroundDrawable(mRJ45_drawable);
        	mDataAccess.updateSetupDB("NetworkConnected", "false");
		}			
	}

	/**
	 * Updating Bluetooth Connection Indicator with current connection status
	 * @param  status indicating the current status
	 */
	protected void connectionStatus(String status){
		
		if(status.equalsIgnoreCase("ok")){
//			isRemoteConnected = true;

//			mDataAccess.updateSetupDB("RemoteConnected", "true");
			Drawable mBT_on_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_wifi_b_01);
			bluetooth_indicator.setBackgroundDrawable(mBT_on_drawable);
			
			if(progressDialog!=null){
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
			}
			
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("playerapp", Constant.APP_VERSION);
			list.put("playerfirmware", System.getProperty("persist.version"));
			list.put("playerID", mDataAccess.getBTAddress());
			list.put("wifiName", "");
			list.put("caller", "com.player.UpdateService");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.settings.Settings.getDeviceInfo", dispatchHashMap,false).execute();
			
		}else if(status.equalsIgnoreCase("false") && !DataStorage.isMobile()){
//			isRemoteConnected = false;
//			mDataAccess.updateSetupDB("RemoteConnected", "false");
			Drawable mBT_off_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_wifi_a_01);
			bluetooth_indicator.setBackgroundDrawable(mBT_off_drawable);
			
			if(mActivity != null){
				mActivity.runOnUiThread(new Runnable() {
					@SuppressLint("InflateParams")
					@Override
					public void run() {
//						LayoutInflater inflater = Layout.this.getLayoutInflater();
//						final Dialog dialog = new Dialog(Layout.this,R.style.ThemeDialogCustom);
//						View messageDialog = inflater.inflate(R.layout.overlay_message, null);
//						TextView messageView = (TextView) messageDialog.findViewById(R.id.messageView);
//						messageView.setText(R.string.BTDISCONNECT);
//						Button okBtn = (Button) messageDialog.findViewById(R.id.messageOkbtn);
//						if(okBtn !=null){
//							okBtn.setVisibility(View.VISIBLE);
//							okBtn.setOnClickListener(new OnClickListener() {
//								
//								@SuppressLint("InflateParams")
//								@Override
//								public void onClick(View v) {
//									if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"))
//									{
//										if(mDataAccess.getConnectionType().equalsIgnoreCase("BT") && !mDataAccess.getBTAddress().equalsIgnoreCase("")){
//											Player.bindBTDevice(mDataAccess.getBTAddress());
//										}else if(mDataAccess.getConnectionType().equalsIgnoreCase("Wifi") && !mDataAccess.getWifiAddress().equalsIgnoreCase("")){
//											player.bindWifiPeer(mDataAccess.getWifiAddress());
//										}
//									}
//									if(dialog.isShowing() && dialog!=null){
//										dialog.dismiss();
//									}
////									if(!mDataAccess.getBTAddress().equalsIgnoreCase("")){
////										Player.bindBTDevice(mDataAccess.getBTAddress());
////										if(dialog.isShowing()){
////											dialog.dismiss();
////										}
////									}else{
////										LayoutInflater inflater = Layout.this.getLayoutInflater();
////										final Dialog dialog = new Dialog(Layout.this,R.style.ThemeDialogCustom);
////										View messageDialog = inflater.inflate(R.layout.overlay_message, null);
////										TextView messageView = (TextView) messageDialog.findViewById(R.id.messageView);
////										messageView.setText(R.string.BTDISCONNECT);
////										Button okBtn = (Button) messageDialog.findViewById(R.id.messageOkbtn);
////										if(okBtn !=null){
////											okBtn.setVisibility(View.VISIBLE);
////											okBtn.setOnClickListener(new OnClickListener() {
////												@Override
////												public void onClick(View v) {
////													if(dialog.isShowing()){
////														dialog.dismiss();
////													}
////												}
////											});
////										}
//									}
//								});
//						}
//						Button cancelBtn = (Button) messageDialog.findViewById(R.id.overlayCancelButton);
//						if(cancelBtn != null){
//							cancelBtn.setVisibility(View.VISIBLE);
//							cancelBtn.setText("CANCEL");
//							cancelBtn.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View arg0) {
//									DataStorage.setMobile(true);
//									if(dialog != null && dialog.isShowing()){
//										dialog.dismiss();
//									}
//								}
//							});
//						}
//						dialog.setContentView(messageDialog);
//						dialog.show();
					}
				});
			}
		}			
	}
	
	/**
	 * AsyncDispatch: Async task to dispatch the requests from applications to Port. 
	 *
	 * @Version 	: 1.0
	 * @Author  	: Lukup
	 */
	public class AsyncDispatch extends AsyncTask<String, String, Boolean> {
		String method;
		ArrayList<HashMap<String,String>> bundle;
		Boolean show;
		
		
	/**
	 * Constructor 
	 * @param  Method method to be executed on the port
	 * @param  params data to be passed port
	 * @param  Show   whether to display progress dialog or not
	 */
	public AsyncDispatch(String Method, ArrayList<HashMap<String,String>> params, Boolean Show) {
		method = Method;
		bundle = params;
		show = Show;
	}
		
	/**
	 * Initialization on UI thread - Displaying of Progress Dialog
	 */
	@Override
	protected void onPreExecute() {
	super.onPreExecute();
		if(show){
			progressDialog = new ProgressDialog(Layout.this,R.style.MyTheme);
			progressDialog.setCancelable(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
			progressDialog.show();
		}
	}
		
	/**
	 * Dispatch method to send data to port
	 * @param  params data to be sent to port
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		if(Constant.DEBUG)  Log.d(TAG, "doInBackground ");
		Player.dispatchMethod(method, bundle);
		return true;
	}
		
	/**
	 * Result of dispatch task & update to UI thread
	 * @param  result result of the dispatch task
	 */		
	@Override
	protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				if(Constant.DEBUG)  Log.d(TAG, "Data Dispatched "+result);
			}
		}
	}
	
	/**
	 * Broadcast receiver to create the system log
	 */	
	public final BroadcastReceiver mLogReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "mLogReceiver");
			if(intent.getAction().equals("com.layout.ErrorLog"))
			{
				Bundle extras = intent.getExtras();
				String type = intent.getStringExtra("type");
				String category = intent.getStringExtra("category");
				String errorname = intent.getStringExtra("errorname");
				String msg = intent.getStringExtra("msg");
				SystemLog.createErrorLogXml(type, category, errorname, msg);
			}
		}
	};		
	
	/**
	 * Broadcast receiver to update the BT connection indicator with connection status
	 */	
	public  final BroadcastReceiver btConnectedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d("Layout" , "btConnectedReceiver");
			if(intent.getAction().equals("com.layout.CONNECTED"))
			{
				if(Constant.DEBUG)  Log.d(TAG , "com.layout.CONNECTED");
				Bundle extras = intent.getExtras();
				String RemoteConnected = intent.getStringExtra("RemoteConnected");
				connectionStatus(RemoteConnected);
			}
		}
	};		
	
	/**
	 * Broadcast receiver to update the network connection indicator with connection status
	 */	
	public final BroadcastReceiver networkconnectedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "networkconnectedReceiver");
			if(intent.getAction().equals("com.layout.NWCONNECTED"))
			{
				Bundle extras = intent.getExtras();
				String NetworkConnected = intent.getStringExtra("NetworkConnected");
				networkStatus(NetworkConnected);
			}
		}
	};		
	
	protected boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
	    if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	/************************************DVB NOTIFICATIONS***************************************************/
	/**
	 * Broadcast receiver to receive the DVB Messages
	 */		
	public final BroadcastReceiver mDvbMsgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "mDvbMsgReceiver");
			Bundle extras = intent.getExtras();
			if(extras != null){
				if(extras.containsKey("Message")){
					HelpText.showHelpTextDialog(mActivity, extras.getString("Message"), 3000);
				}
			}
		}
	};
	
	/**
	 * Removing the progress dialog shown when method dispatched. This is only to handle response in UpdateService as 
	 * there is no handle
	 */	
	public final BroadcastReceiver removeDialogReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "removeDialogReceiver");
			if(intent.getAction().equals("RemoveDialog"))
			{
				if(Constant.DEBUG)  Log.d(TAG , "removeDialogReceiver : RemoveDialog Action is not Null. Removing ...");
				if(progressDialog!= null && progressDialog.isShowing()){
					if(Constant.DEBUG)  Log.d(TAG , "removeDialogReceiver : Layout.progressDialog is not null Removing ...");
					progressDialog.dismiss();
				}
				else
					if(Constant.DEBUG)  Log.d(TAG , "removeDialogReceiver : Remove Dialog is NULL");
			}
		}
	};	
	
/************************************NOTIFICATIONS***************************************************/
	
	public final BroadcastReceiver mNotificationReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String title = "";
			String message = "";
			String action = "";
			String details = "";
			if(extras != null){
				if(extras.containsKey("Title")){
					title = extras.getString("Title");
				}
				if(extras.containsKey("Message")){
					message = extras.getString("Message");
				}
				if(extras.containsKey("Action")){
					action = extras.getString("Action");
				}
				if(Constant.DEBUG)  Log.d(TAG , "Notification recd in Layout with title " + title + ", with Message " + message + " with Action " + action + " and Details " + details);
				HashMap<String,String> notifications = new HashMap<String, String>();
				notifications.put("title", title);
				notifications.put("desc", message);
				notifications.put("action",action);
				CacheDockData.notificationList.add(notifications);
				showNotification();
			}
		}
	};
	
	private void showNotification() {
		alert_indicator.setVisibility(View.VISIBLE);
		alert_indicator.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (Constant.DEBUG) Log.d(TAG, "On Click ");
				Intent lukup = new Intent(Layout.this, com.player.apps.Notification.class);
				startActivity(lukup);
			}
		});
	}
	
//	private void showNotification(final String title, final String message, final String action, final String details){
//		alert_indicator.setVisibility(View.VISIBLE);
//		if(Constant.DEBUG)  Log.d(TAG , "Showing the alert button for notifications ?");
//		alert_indicator.setOnClickListener(new OnClickListener() {
//          public void onClick(View arg0) {
//              CustomNotification(title, message, action, details);
//          }
//	    });
//	}
//	
//	public void CustomNotification(String title, String message, String action, String details) {
//		// Using RemoteViews to bind custom layouts into Notification
//		RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.customnotification);
//		alert_indicator.setVisibility(View.INVISIBLE);
//		// Open NotificationView Class on Notification Click
//		Intent intent = null;
//		try {
//			intent = new Intent(this, Class.forName(action));
//			if(!details.isEmpty()){
//				intent.putExtra("Details", details);
//			}			
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		// Open NotificationView.java Activity
//		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
//		if(Constant.DEBUG)  Log.d(TAG , "Created pending intent");
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//		// Set Icon
//		// .setSmallIcon(R.drawable.logosmall)
//				.setContentTitle(title).setContentText(message)
//				// Set Ticker Message
//				// .setTicker(message)
//				// Dismiss Notification
//				.setAutoCancel(true)
//				// Set PendingIntent into Notification
//				.setContentIntent(pIntent)
//				// Set RemoteViews into Notification
//				.setContent(remoteViews);
//
//		// Locate and set the Image into customnotificationtext.xml ImageViews
//		remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_launcher);
//		remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.v13_ico_alert_01);
//
//		// Locate and set the Text into customnotificationtext.xml TextViews
//		remoteViews.setTextViewText(R.id.title, title);
//		remoteViews.setTextViewText(R.id.text, message);
//
//		// Create Notification Manager
//		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		// Build Notification with Notification Manager
//		notificationmanager.notify(0, builder.build());
//
//	}
	
}
