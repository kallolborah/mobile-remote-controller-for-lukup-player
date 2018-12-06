package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.player.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.player.Layout;
import com.player.Player;
import com.player.action.AuthDialog;
import com.player.action.HelpTip;
import com.player.action.OverlayCancelListener;
import com.player.action.Push;
import com.player.action.Subscribe;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataAccess;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.webservices.EPG;
import com.player.widget.ConnectionSetupAdapter;
import com.player.widget.CustomListView;
import com.player.widget.HelpText;
import com.player.widget.ListViewAdapter;
import com.player.widget.OverLay;
import com.player.widget.SettingsListView;

// TODO: Auto-generated Javadoc
/**
 * The Class Setup.
 *
 * @author abhijeet
 */
public class Setup extends Layout{
	
	/** The mset up instance. */
	private Setup msetUpInstance;
	
	//profile name and ids
	/** The name. */
	private static String name = ""; 
	
	/** The id. */
	private String id = "";
	
	/** The Subscriber id. */
	protected String SubscriberId= "";
	
	//user selected params
	/** The is broadband selected. */
	private static boolean isBroadbandSelected = false;
	
	/** The is pppoe connected. */
	private static boolean isPPPoEConnected = false;
	
	/** The selectedlive tv operator. */
	private String selectedliveTVOperator = "";
	
	/** The distributor id. */
	private String distributorID = "0000";
	
	/** The distributor pwd. */
	private String distributorPwd = "";
	
	/** The network type. */
	private String networkType = ""; //broadband or wifi
	
	/** The broadbandtype. */
	private String broadbandtype = ""; //manual or auto
	
	/** The broadband_ip. */
	private String broadband_ip = ""; 
	
	/** The broadband_subnet. */
	private String broadband_subnet = "";
	
	/** The broadband_gateway. */
	private String broadband_gateway = "";
	
	/** The broadband_dns. */
	private String broadband_dns = "";
	
	/** The Operator. */
	private String Operator = "";
	
	/** The selected wifi. */
	private String selectedWifi = "";
	
	/** The hot spot status. */
	private boolean hotSpotStatus;
	
	/** The bssid. */
	private String bssid = "";
	
	/** The wifi security. */
	private String wifiSecurity = "";
	
	/** The is setup. */
	private static boolean isSetup=false;	
	
	/** The setup finish btn. */
	private Button setupFinishBtn;
	
	/** The is network selected. */
	private boolean isNetworkSelected = false;
	
	/** The setuplist. */
	ArrayList<HashMap<String, String>> setuplist;
	
	/** The about list. */
	ArrayList<HashMap<String, String>> aboutList;
	
	/** The tag. */
	private String TAG = "Setup";
	
	/** The stand params. */
	private LinearLayout.LayoutParams standParams;
	
	/** The Activity name. */
	private String ActivityName = "";
	
	/** The maingrid_select_index. */
	private int maingrid_select_index = -1;
	
	/** The maingrid_focus_index. */
	private int maingrid_focus_index = -1;
	
	/** The maingridview_focus. */
	private View maingridview_focus;
	
	/** The currentlayout main. */
	RelativeLayout currentlayoutMain;
	
	/** The current text view. */
	TextView currentTextView;
	
	/** The current postion. */
	private int currentPostion = -1;
	
	/** The about json data. */
	private JSONObject aboutJsonData;
	
	/** The setup details. */
	private SharedPreferences setupDetails;
	
	/** The setupedit. */
	private SharedPreferences.Editor setupedit;
	private BluetoothAdapter mAdapter;
	
	/**PPPOE related*/
	private EditText mUsername;
	private EditText mPassword;
	
	/** The m bt device list. */
	private ArrayList<String> mBTDeviceList = new ArrayList<String>();
	private ArrayList<String> mBTDeviceName = new ArrayList<String>();
	private ArrayList<String> mBTDeviceBondList = new ArrayList<String>();
	private ConnectionSetupAdapter mConnectionAdapterBTSearchList;
	private ConnectionSetupAdapter mConnectionAdapterWIFISearchList;
	private ConnectionSetupAdapter mConnectionAdapterBTBondList;
	private ConnectionSetupAdapter mConnectionAdapterWIFIBondList;
	private ArrayList<HashMap<String,String>> wifiList = new ArrayList<HashMap<String,String>>();
	private String ConnectionType;

	/* (non-Javadoc)
	 * @see com.layout.Layout#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Constant.DEBUG)  Log.d(TAG, "onCreate() ");
		msetUpInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		Bundle br = this.getIntent().getExtras();
		if (br != null) {
			if (br.containsKey("ActivityName")) {
				ActivityName = br.getString("ActivityName");
			}
		}
		
		setupDetails = getApplication().getSharedPreferences("SetupDetail", MODE_WORLD_WRITEABLE);
		setupedit = setupDetails.edit();
//		Layout.state = getApplicationContext().getSharedPreferences("PlayerState", MODE_WORLD_WRITEABLE);
//		Layout.edit = state.edit();
	}
	
	/* (non-Javadoc)
	 * @see com.layout.Layout#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter setup = new IntentFilter("com.player.apps.Setup");
		registerReceiver(mSetupReceiver,setup); 
		
		IntentFilter connected = new IntentFilter("com.layout.CONNECTED");
		registerReceiver(mSetupReceiver,connected);		
		
		IntentFilter discoverBTDevices = new IntentFilter("DiscoveredBTAddress");
		registerReceiver(btdeviceList,discoverBTDevices);		
		
		IntentFilter pairedBTDevices = new IntentFilter("PairedBTAddress");
		registerReceiver(paireddeviceList,pairedBTDevices);		
		
		IntentFilter discoverwifiDevices = new IntentFilter("LUKUP_WIFI_PEERS");
		registerReceiver(mwifideviceList,discoverwifiDevices);	
		
		IntentFilter btConnectedMsg = new IntentFilter("com.layout.CONNECTED");
		registerReceiver(btConnectedReceiver, btConnectedMsg);
		
		if(mDataAccess.getHelptip().equalsIgnoreCase("false")){
			Player.help = false;
		}else {
			Player.help = true;
		}
		
	}
	//
	
	/* (non-Javadoc)
	 * @see com.layout.Layout#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		DataStorage.setCurrentScreen(ScreenStyles.LUKUPOPTIONSELECTED);

		if(Layout.mDataAccess.getIsSetup().equalsIgnoreCase("true")|| !Layout.mDataAccess.getSubscriberID().equalsIgnoreCase("")){
			isSetup = true;
		}else{
			isSetup = false;
		}
		if (Constant.DEBUG)	     Log.d(TAG, "Is set up done :" + isSetup);
		if (ActivityName.equalsIgnoreCase("AppGuide") || ActivityName.equalsIgnoreCase("Profile")){
			if(!isSetup){
				showSetUpList();
			}else{
				showSettings();
			}
		}
		else{
			if(Constant.DEBUG)  Log.d(TAG,"onResume() isSetup: "+isSetup);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.layout.Layout#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see com.layout.Layout#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mSetupReceiver != null){
			msetUpInstance.unregisterReceiver(mSetupReceiver);//   
			msetUpInstance.unregisterReceiver(btdeviceList);
			msetUpInstance.unregisterReceiver(paireddeviceList);
			msetUpInstance.unregisterReceiver(mwifideviceList);
			msetUpInstance.unregisterReceiver(btConnectedReceiver);
		}
	}	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		super.finish();
	}
	
	/**
	 * *****************************************************************************************.
	 */
	
	
	/**
	 * show List allowing user to set up live TV, broadband and distribution ID
	 */
	private void showSetUpList() {
		try {
			if(Constant.DEBUG)Log.d(TAG,"showSetUpList()");
			if(msetUpInstance != null ){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							mContainer.removeAllViews();
						}
						if(Constant.DEBUG)Log.d(TAG,"showSetUpList() inside thread");
						DataStorage.setCurrentScreen(ScreenStyles.SETUP_SCREEN);
						ListView list = new ListView(msetUpInstance);
						final ArrayList<HashMap<String,String>> setuplist = new ArrayList<HashMap<String,String>>();
						
						if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
							for(int i=0;i<ScreenStyles.REMOTE_SETTINGS_LIST.length;i++){
								HashMap<String, String> setupMap = new HashMap<String, String>();
								setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.REMOTE_SETTINGS_LIST[i]);
								setupMap.put(ScreenStyles.LIST_KEY_THUMB_URL,ScreenStyles.REMOTE_SETTINGS_LIST_ICONS[i]+"");
								setuplist.add(setupMap);
							}
						}else{
							HashMap<String, String> setupMap = new HashMap<String, String>();
							setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.REMOTE_SETTINGS_LIST[0]);
							setupMap.put(ScreenStyles.LIST_KEY_THUMB_URL,ScreenStyles.REMOTE_SETTINGS_LIST_ICONS[0]+"");
							setuplist.add(setupMap);
							setupMap = new HashMap<String, String>();
							setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.REMOTE_SETTINGS_LIST[2]);
							setupMap.put(ScreenStyles.LIST_KEY_THUMB_URL,ScreenStyles.REMOTE_SETTINGS_LIST_ICONS[2]+"");
							setuplist.add(setupMap);
						}
									
						list.setCacheColorHint(Color.TRANSPARENT);
						int selected = 3;
						if(isNetworkSelected){
							selected = 2;
						}
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, setuplist,0,"setup", null,selected);
						list.setAdapter(adapter);
						if(Constant.DEBUG)Log.d(TAG,"showSetUpList()Setadapter");
						if(mContainer != null){
							mContainer.removeAllViews();
							mContainer.addView(list);
							LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(customLayout.getConvertedHeight(160),  customLayout.getConvertedHeight(36));
							if(customLayout != null){
								btnParams.topMargin = customLayout.getConvertedHeight(24);
							}

							btnParams.gravity = Gravity.CENTER_HORIZONTAL;
							btnParams.gravity = Gravity.CENTER|Gravity.CENTER_HORIZONTAL;

							int convertedHeight = customLayout.getConvertedHeight(160);
							btnParams.setMargins(0, convertedHeight, 0, 0);
							if(setupFinishBtn == null){
								setupFinishBtn = new Button(msetUpInstance);
								setupFinishBtn.setText("FINISH");
								setupFinishBtn.setTextSize(16);
								setupFinishBtn.setTextColor(Color.parseColor("#EC108C"));
								setupFinishBtn.setEnabled(false);
								setupFinishBtn.setClickable(true);
								setupFinishBtn.setVisibility(View.INVISIBLE);
								setupFinishBtn.setId(1111);
								setupFinishBtn.setBackgroundColor(Color.WHITE);
								setupFinishBtn.setLayoutParams(btnParams);
								mContainer.addView(setupFinishBtn);
							}
							else{
								if(!mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
									setupFinishBtn.setEnabled(false);
									setupFinishBtn.setVisibility(View.INVISIBLE);
									setupFinishBtn.setId(1111);
									setupFinishBtn.setLayoutParams(btnParams);
									mContainer.addView(setupFinishBtn);
								}else{
									setupFinishBtn.setEnabled(true);
									setupFinishBtn.setClickable(true);
									setupFinishBtn.setVisibility(View.VISIBLE);
									setupFinishBtn.setId(1111);
									setupFinishBtn.setLayoutParams(btnParams);
									mContainer.addView(setupFinishBtn);
								}
							}
							
							setupFinishBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
								//	if(Constant.DEBUG)  Log.d(TAG,"\n networkType : "+networkType+"\n broadbandtype : "+broadbandtype+"\n seletedLivetvnetwork "+selectedliveTVOperator+"\n broadband_ip "+broadband_ip+"\nbroadband_subnet "+broadband_subnet+"\nbroadband_gateway "+broadband_gateway+"\nselectedWifi_network "+selectedWifi+"\nselectedSatelite_name "+selectedliveTVOperator/*+"\nselectedWifi_network "+selectedWifi_network*/);
								
									
									try {
										if(msetUpInstance != null){
											msetUpInstance.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													distributorID = null;
													//HelpTip
													HelpTip.requestForHelp(msetUpInstance.getResources().getString(R.string.SUPPORT_FROM_YOUR_LOCAL_DISTRIBUTOR),
															msetUpInstance.getResources().getString(R.string.SETUP_MSG5),msetUpInstance);
													
													OverLay overlay = new OverLay(msetUpInstance);
													final String title = msetUpInstance.getResources().getString(R.string.DISTRIBUTOR);
													final Dialog dialog = overlay .getOverLay(ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL, title, null,null,null,isLukupSelected);
													if(dialog != null){
														dialog.show();
														Button connectBtn =(Button)dialog.findViewById(R.id.connect);
														Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
														TextView titleView = (TextView) dialog.findViewById(R.id.overlayTitle);
														if(titleView != null){
															titleView.setText(title);
														}
														final EditText textfield = (EditText) dialog.findViewById(R.id.username);
														final EditText passwordfield = (EditText) dialog.findViewById(R.id.password);
														if(textfield != null){
															textfield.setHint(msetUpInstance.getResources().getString(R.string.ENTER_DISTRIBUTOR_ID));
															
														}
														if(passwordfield != null){
															passwordfield.setHint(msetUpInstance.getResources().getString(R.string.ENTER_DISTRIBUTOR_PASSWORD));
															
														}
														if(connectBtn != null){
															connectBtn.setText(msetUpInstance.getResources().getString(R.string.SUBMIT));
															connectBtn.setOnClickListener(new OnClickListener() {
																@Override
																public void onClick(View arg0) {
																	if(textfield != null && passwordfield != null){
//																		String screenid = screenId;
																		String distributionid = textfield.getText().toString().trim();
																		String reenterdistributionid  = passwordfield.getText().toString().trim();
																		if(distributionid == null || distributionid.equalsIgnoreCase("")){
																			HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
																			return;
																		}
																		if(reenterdistributionid == null || reenterdistributionid.equalsIgnoreCase("")){
																			HelpText.showHelpTextDialog(msetUpInstance,msetUpInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
																			return;
																		}
																		
																		if(Constant.DEBUG)  Log.d(TAG,"Sending Detail distribution id "+distributionid);
																		distributorID = distributionid;
																		distributorPwd = reenterdistributionid;
																		try{										
																			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
																			HashMap<String, String> list = new HashMap<String, String>();
																			list.put("distributorid", distributorID);
																			list.put("distributorpwd", distributorPwd);
																			list.put("liveTVOperator", selectedliveTVOperator+"");
																			list.put("consumer", "TV");
																			list.put("network",mDataAccess.getConnectionType());
																			list.put("caller", "com.player.apps.Setup");
																			list.put("called", "startService");
																			dispatchHashMap.add(list);
																			String method = "com.port.apps.settings.Settings.getSubscriberID"; 
																			new AsyncDispatch(method, dispatchHashMap,true).execute();
																		}catch (Exception e){
																			e.printStackTrace();
																		}																		
																		
																		if(dialog != null && dialog.isShowing()){
																			dialog.cancel();
																		}
																	}
																}
															});
														}
														if(cancelBtn != null){
															cancelBtn.setOnClickListener(new OnClickListener() {
																@Override
																public void onClick(View v) {
																	if(dialog != null && dialog.isShowing()){
																		dialog.cancel();
																	}
//																	if(isSetup == false){
//																		showSetUpList();
//																	}else{
//																		showSettings();
//																	}
																}
															});
														}
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
									if(Constant.DEBUG)  Log.d(TAG,"Got All required Information Send to MP");
								}
							});
							
							list.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
									
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									
									HashMap<String, String> map= setuplist.get(position);
									if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
										String val = map.get(ScreenStyles.LIST_KEY_TITLE);
										if(val.equalsIgnoreCase("Setup Network")){
											
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.Setup");
											list.put("called", "startService");
											dispatchHashMap.add(list);
											String method = "com.port.apps.settings.Settings.getNetworkType"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();
										}
										if(val.equalsIgnoreCase("Setup Lukup Player")){
											Setupconnection();
											/*selectedliveTVOperator = "";
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.Setup");
											list.put("called", "startService");
											dispatchHashMap.add(list);
											String method = "com.port.apps.settings.Settings.getTVOperators"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();*/
										}
										if(val.equalsIgnoreCase("Authenticate")){
											//showDistributorId();
											SetPppoe();
										}
									}
								}
							});
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
	
	
//=================================================BT-WIFI Devices list ==========================================	
/**
 * Setupconnection : Provides the functionality to Connect via Bluetooth or  Wifi P2P.
 * 
 */
	private void Setupconnection(){
		
		if(msetUpInstance != null){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						DataStorage.setCurrentScreen("ConnectionSetup");
						mContainer.removeAllViews();
						final ArrayList<HashMap<String,String>> connectionsetuplist = new ArrayList<HashMap<String,String>>();
//						if(player.wifi.getDhcpInfo().gateway!=0){
//							HashMap<String, String> setupMap = new HashMap<String, String>();
//							setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CONNECTION_SETTINGS_LIST[1]);
//						}else{
//							HashMap<String, String> setupMap = new HashMap<String, String>();
//							setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CONNECTION_SETTINGS_LIST[0]);
//						}
						if(Constant.model.equalsIgnoreCase("S")){
							HashMap<String, String> setupMap = new HashMap<String, String>();
							setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CONNECTION_SETTINGS_LIST[0]);
							connectionsetuplist.add(setupMap);
						}else{
							for(int i=0;i<ScreenStyles.CONNECTION_SETTINGS_LIST.length;i++){
								HashMap<String, String> setupMap = new HashMap<String, String>();
								setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CONNECTION_SETTINGS_LIST[i]);
								connectionsetuplist.add(setupMap);
							}
						}
						ListView list = new ListView(msetUpInstance);
						list.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, connectionsetuplist,0,"connectionsetup", null,-1);
						list.setAdapter(adapter);
						if(mContainer != null && list != null){
							mContainer.setOrientation(LinearLayout.VERTICAL);
							mContainer.addView(list);
							list.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
									
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									
									TextView  inflater = (TextView) v.findViewById(R.id.title);
									if(inflater != null){
										String selectedValue = inflater.getText().toString().trim();
										ConnectBTWIFI(selectedValue);
										ConnectionType = selectedValue;
										
									}
									
									
								}
							});
						}
				
					}
				}
			});
		}
	}

	
	/**
	 * Connect btwifi.
	 *
	 * @param type is the connection type selected like Bluetooth or Wifi
	 */
	private void ConnectBTWIFI(final String type){
		if(Constant.DEBUG)Log.d(TAG, "Connection type Selected ==> "+ type);
		
		if(msetUpInstance != null){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						DataStorage.setCurrentScreen("ConnectList");
						mContainer.removeAllViews();
						final ArrayList<HashMap<String,String>> connectionsetuplist = new ArrayList<HashMap<String,String>>();
						for(int i=0;i<ScreenStyles.CONNECT_LIST.length;i++){
							HashMap<String, String> setupMap = new HashMap<String, String>();
							setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CONNECT_LIST[i]);
						//	setupMap.put(ScreenStyles.LIST_KEY_THUMB_URL,ScreenStyles.REMOTE_SETTINGS_LIST_ICONS[i]+"");
							connectionsetuplist.add(setupMap);
						}
						ListView list = new ListView(msetUpInstance);
						list.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, connectionsetuplist,0,"connectionsetup", null,-1);
						list.setAdapter(adapter);
						if(mContainer != null && list != null){
							mContainer.setOrientation(LinearLayout.VERTICAL);
							mContainer.addView(list);
							list.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
									
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									
									TextView  inflater = (TextView) v.findViewById(R.id.title);
									if(inflater != null){
										String selectedValue = inflater.getText().toString().trim();
										if(type.equalsIgnoreCase("Bluetooth")){
											if(Constant.DEBUG)Log.d(TAG, "Bluetooth Selected");
//											mDataAccess.updateSetupDB("ConnectionType", "BT");
											ConnectBT(selectedValue);
										}else if(type.equalsIgnoreCase("Wifi")){
//											mDataAccess.updateSetupDB("ConnectionType", "wifi");
											connectWIFIP2P(selectedValue);
											if(Constant.DEBUG)Log.d(TAG, "WIFI Selected");
										}
										
									}
								}
							});
						}
				
					}
				}
			});
		}
	}
	
	
//=====BroadcastReceiver to receive Bt device from the service
	
	/** The btdevice list. :BroadcastReceiver to receive the BT Devices address sent by BluetoothConnectioService
	 * 
	 * */
	public final BroadcastReceiver btdeviceList = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String address= "";
			String name = "";
			if(Constant.DEBUG)  Log.d(TAG , "btdeviceList");
			if(intent.hasExtra("Address")){
				if(progressDialog.isShowing()){
					progressDialog.cancel();
				}
				if(Constant.DEBUG)  Log.d(TAG , "AddressbtdeviceList === >" + intent.getStringExtra("Address"));
				address = intent.getStringExtra("Address");
				name = intent.getStringExtra("Name");
				mConnectionAdapterBTSearchList.addDevice(address,name);
				mConnectionAdapterBTSearchList.notifyDataSetChanged();
			}			
		}
	};
	
	public final BroadcastReceiver paireddeviceList = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String address= "";
			String name = "";
			if(Constant.DEBUG)  Log.d(TAG , "btdeviceList");
			if(intent.hasExtra("Address")){
				if(progressDialog.isShowing()){
					progressDialog.cancel();
				}
				if(Constant.DEBUG)  Log.d(TAG , "Address paireddeviceList === >" + intent.getStringExtra("Address"));
				address = intent.getStringExtra("Address");
				name = intent.getStringExtra("Name");
				mConnectionAdapterBTSearchList.addDevice(address, name);
				mConnectionAdapterBTSearchList.notifyDataSetChanged();
				
			}
			
		}
	};
	
	public final BroadcastReceiver mwifideviceList = new BroadcastReceiver() {
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.hasExtra("State")){
				String state = intent.getStringExtra("State");
				if(state.equalsIgnoreCase("discovered")){
					if(Constant.DEBUG)  Log.d(TAG , "mwifideviceList discovered");
					if(intent.hasExtra("Devices")){
						if(progressDialog.isShowing()){
							progressDialog.cancel();
						}
						if(Constant.DEBUG)  Log.d(TAG , "Address mwifideviceList === >");
						ArrayList<HashMap<String,String>> wifi = (ArrayList<HashMap<String,String>>)intent.getSerializableExtra("Devices");
						mConnectionAdapterWIFISearchList.addwifiDevice(wifi);
					}
				}else if(state.equalsIgnoreCase("connected")){
					
				}
			}
			
			
		}
	};
	
	public  final BroadcastReceiver btConnectedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "btConnectedReceiver");
			if(intent.getAction().equals("com.layout.CONNECTED"))
			{
				if(Constant.DEBUG)  Log.d(TAG , "com.layout.CONNECTED");
				String RemoteConnected = intent.getStringExtra("RemoteConnected");
				if(RemoteConnected.equalsIgnoreCase("ok")){
					if(mDataAccess.getSubscriberID().equalsIgnoreCase("")){
						 showSetUpList();
					}else{
						 showSettings();
					}
				}else if(RemoteConnected.equalsIgnoreCase("mobile")){
					if(mDataAccess.getSubscriberID().equalsIgnoreCase("")){
						 showSetUpList();
					}else{
						 showSettings();
					}
				}else {
					Toast.makeText(msetUpInstance, getResources().getString(R.string.PAIRING_FAIL), Toast.LENGTH_LONG).show();
					Setupconnection();
				}
				if(Layout.progressDialog.isShowing()){
					Layout.progressDialog.dismiss();
					if(Constant.DEBUG)  Log.d(TAG , "progressDialog dismiss()");
				}
			}
		}
	};		
	
	
	
	
	/**
	 * Sets the up bt.
	 *
	 * @author tomesh
	 * Sets the up BT.: Method to get the List of BT Devices
	 * @param mode : Argument as Mode such as Connect or Disconnect
	 */
	private void ConnectBT(String mode){
		if(mode.equalsIgnoreCase("Connect")){
			if(Constant.DEBUG)Log.d(TAG, "ConnectBT()---Connect");
		
			if(msetUpInstance != null){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							progressDialog = new ProgressDialog(Setup.this,R.style.MyTheme);
							progressDialog.setCancelable(true);
							progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
							progressDialog.show();
							player.findBTDevice();
							if(Constant.DEBUG)Log.d(TAG, "findBTDevice()---Called from Setup");
							DataStorage.setCurrentScreen("BluetoothList");
							mContainer.removeAllViews();
							if(Constant.DEBUG)Log.d(TAG, "setupBT()---Connect");
							ListView list = new ListView(msetUpInstance);
							list.setCacheColorHint(Color.TRANSPARENT);
							mBTDeviceList.clear();
							mBTDeviceName.clear();
							mConnectionAdapterBTSearchList = new ConnectionSetupAdapter(msetUpInstance, mBTDeviceList,mBTDeviceName,"BT");
							list.setAdapter(mConnectionAdapterBTSearchList);
							
							if(mContainer != null && list != null){
								mContainer.setOrientation(LinearLayout.VERTICAL);
								mContainer.addView(list);
								list.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
										if (maingrid_select_index != position){      
											if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
											{
												Log.w(TAG, "OnItemClickListener().position is ::"+position);
												currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
												currentTextView.setTextColor(getResources().getColor(R.color.white));
											}
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
											currentTextView.setTextColor(getResources().getColor(R.color.pink));
										}else if(maingridview_focus != null){
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
										}
										maingrid_focus_index = position;
										currentPostion = position;
										String address = mBTDeviceList.get(position);
										Player.bindBTDevice(mBTDeviceList.get(position));
//										mDataAccess.updateSetupDB("ConnectionType", "BT");
//										mDataAccess.updateSetupDB("BTAddress", address);
										//edit.putString("BTdevice", address);
									}
								});
							}
					
						}
					}
				});
			}
		}else if(mode.equalsIgnoreCase("Disconnect")){
			if(Constant.DEBUG)Log.d(TAG, "ConnectBT()---DisConnect");
			if(msetUpInstance != null){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							//player.findBTDevice();
							String address = "";
							String name = "";
							if(Constant.DEBUG)Log.d(TAG, "findBTDevice()---Called from Setup");
							DataStorage.setCurrentScreen("BluetoothList");
							mContainer.removeAllViews();
							if(Constant.DEBUG)Log.d(TAG, "ConnectBT()---Connect");
							ListView list = new ListView(msetUpInstance);
							list.setCacheColorHint(Color.TRANSPARENT);
							mBTDeviceBondList.clear();
							mBTDeviceName.clear();
							
							mAdapter = BluetoothAdapter.getDefaultAdapter();
							Set<BluetoothDevice> pairedDevice = mAdapter.getBondedDevices();
							if(Constant.DEBUG)  Log.d(TAG, "getPairedDevices : pairedDevice.size()" + pairedDevice.size());
							if(pairedDevice.size()>0) {
								for(BluetoothDevice device : pairedDevice) {
									if(Constant.DEBUG)  Log.d(TAG, "Searched Devices "+device.getAddress() + "Connected Device " + device.getAddress());
									if(device.getName().indexOf(Constant.DeviceName) != -1){
										if(Constant.DEBUG)  Log.d(TAG , "getPairedDevices : Sending Broadcast after getting device address");
										address = device.getAddress();
										name = device.getName();
										mBTDeviceBondList.add(address);
										mBTDeviceName.add(name);
										break;
									}
								}
							}else{
									if(!mDataAccess.getBTAddress().equalsIgnoreCase("")){
										 mBTDeviceBondList.add(mDataAccess.getBTAddress());
										 mBTDeviceName.add("Lukup Player");
									}else{
									 Toast.makeText(msetUpInstance, "No paired Device found", Toast.LENGTH_LONG).show();
									}
							}
							mConnectionAdapterBTBondList = new ConnectionSetupAdapter(msetUpInstance, mBTDeviceBondList,mBTDeviceName,"BT");
							list.setAdapter(mConnectionAdapterBTBondList);
							
							if(mContainer != null && list != null){
								mContainer.setOrientation(LinearLayout.VERTICAL);
								mContainer.addView(list);
								list.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
										
										if (maingrid_select_index != position){      
											if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
											{
												Log.w(TAG, "OnItemClickListener().position is ::"+position);
												currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
												currentTextView.setTextColor(getResources().getColor(R.color.white));
											}
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
											currentTextView.setTextColor(getResources().getColor(R.color.pink));
										}else if(maingridview_focus != null){
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
										}
										maingrid_focus_index = position;
										currentPostion = position;
										String address = mBTDeviceBondList.get(position);
										if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true") && address.equalsIgnoreCase(mDataAccess.getBTAddress())){
											
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("BTAddress", player.mAdapter.getAddress());
											list.put("consumer", "TV");
											list.put("network", mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.Setup");
											list.put("called", "startService");
											dispatchHashMap.add(list);
											String method = "com.port.apps.settings.Settings.doBTUnbind"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();
											
											if(Constant.DEBUG)Log.d(TAG, "BT device to disconnect " + address);
											Player.unbindBTDevice(address, true);
											mDataAccess.updateSetupDB("BTAddress", "");
											
										}else if(!address.equalsIgnoreCase("")){
											
											if(Constant.DEBUG)Log.d(TAG, "BT device to disconnect " + address);
											Player.unbindBTDevice(address, false);
											mDataAccess.updateSetupDB("BTAddress", "");
										}
										
										mBTDeviceBondList.clear();
										mBTDeviceName.clear();
										mConnectionAdapterBTBondList.notifyDataSetChanged();
										
										if(!isSetup){
											 showSetUpList();
										}else{
											 showSettings();
										}
									}
								});
							}
						}
					}
				});
			}
		}
	}
	
	
	/**
	 * Connect wifi. Method to get the List of WIFIP2P Devices
	 *
	 * @param mode the mode :Argument as Mode such as Connect or Disconnect
	 */
	private void connectWIFIP2P(String mode){
		
		if(mode.equalsIgnoreCase("Connect")){
			if(Constant.DEBUG)Log.d(TAG, "connectWIFIP2P()---Connect");
					if(msetUpInstance != null){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							player.findWifiP2PDevices();
							if(Constant.DEBUG)Log.d(TAG, "findWifiP2PDevices()---Called from Setup");
							DataStorage.setCurrentScreen("WIFILIST");
							mContainer.removeAllViews();
							if(Constant.DEBUG)Log.d(TAG, "connectWIFIP2P()---Connect");
							progressDialog = new ProgressDialog(Setup.this,R.style.MyTheme);
							progressDialog.setCancelable(true);
							progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
							progressDialog.show();
							wifiList.clear();
							ListView list = new ListView(msetUpInstance);
							list.setCacheColorHint(Color.TRANSPARENT);
							mConnectionAdapterWIFISearchList = new ConnectionSetupAdapter(msetUpInstance, wifiList,"wifi");
							list.setAdapter(mConnectionAdapterWIFISearchList);
							//ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, wifiList,0,"wifiList", null,-1);
							//list.setAdapter(adapter);
							
							if(mContainer != null && list != null){
								mContainer.setOrientation(LinearLayout.VERTICAL);
								mContainer.addView(list);
								list.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
										if (maingrid_select_index != position){      
											if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
											{
												Log.w(TAG, "OnItemClickListener().position is ::"+position);
												currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
												currentTextView.setTextColor(getResources().getColor(R.color.white));
											}
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
											currentTextView.setTextColor(getResources().getColor(R.color.pink));
										}else if(maingridview_focus != null){
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
										}
										maingrid_focus_index = position;
										currentPostion = position;
										TextView title = (TextView)arg0.findViewById(R.id.title);
										String address = title.getText().toString().trim();
										if(Constant.DEBUG)Log.e("WIFIADDRESSS", address);
										player.bindWifiPeer(address);
//										mDataAccess.updateSetupDB("ConnectionType", "Wifi");
//										mDataAccess.updateSetupDB("WIFIAddress", address);
										//edit.putString("WIFIAddress", address);
									}
								});
							}
					
						}
					}
				});
			}
		}else if(mode.equalsIgnoreCase("Disconnect")){
			if(Constant.DEBUG)Log.d(TAG, "connectWIFIP2P()---DisConnect");
			if(msetUpInstance != null){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							//player.findBTDevice();
							DataStorage.setCurrentScreen("WIFILIST");
							mContainer.removeAllViews();
							ListView list = new ListView(msetUpInstance);
							list.setCacheColorHint(Color.TRANSPARENT);
							mBTDeviceBondList.clear();
							mBTDeviceName.clear();
							if(!Layout.mDataAccess.getWifiAddress().equalsIgnoreCase("")){
								mBTDeviceBondList.add(Layout.mDataAccess.getWifiAddress());
								mBTDeviceName.add("Lukup Player");
							}
							mConnectionAdapterBTBondList = new ConnectionSetupAdapter(msetUpInstance, mBTDeviceBondList,mBTDeviceName,"BT");
							list.setAdapter(mConnectionAdapterBTBondList);
							
							if(mContainer != null && list != null){
								mContainer.setOrientation(LinearLayout.VERTICAL);
								mContainer.addView(list);
								list.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
										
										if (maingrid_select_index != position){      
											if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
											{
												Log.w(TAG, "OnItemClickListener().position is ::"+position);
												currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
												currentTextView.setTextColor(getResources().getColor(R.color.white));
											}
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
											currentTextView.setTextColor(getResources().getColor(R.color.pink));
										}else if(maingridview_focus != null){
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
											maingridview_focus = v;
											currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
											currentTextView = (TextView) v.findViewById(R.id.title);
										}
										maingrid_focus_index = position;
										currentPostion = position;
										
										TextView title = (TextView)arg0.findViewById(R.id.title);
										String address = title.getText().toString().trim();
										
										if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
											if(Constant.DEBUG)Log.d(TAG, "Connected, Wifi P2P address to disconnect " + address);
//											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//											HashMap<String, String> list = new HashMap<String, String>();
//											list.put("consumer", "TV");
//											list.put("network", mDataAccess.getConnectionType());
//											list.put("caller", "com.player.apps.Setup");
//											list.put("called", "startService");
//											dispatchHashMap.add(list);
//											String method = "com.port.apps.settings.Settings.stopWifi"; 
//											new AsyncDispatch(method, dispatchHashMap,true).execute();
//											try {
//												synchronized(this){
//													this.wait(3000);
//												}
//											} catch (InterruptedException e) {
//												// TODO Auto-generated catch block
//												e.printStackTrace();
//											}
											player.unbindWifiPeer();
										}else{
											if(Constant.DEBUG)Log.d(TAG, "Disconnected, Wifi P2P address to disconnect " + address);
											player.unbindWifiPeer();
										}
										mBTDeviceBondList.clear();
										mBTDeviceName.clear();
										mConnectionAdapterBTBondList.notifyDataSetChanged();
										
										if(!isSetup){
											 showSetUpList();
										}else{
											 showSettings();
										}
									}
								});
							}
						}
					}
				});
			}
		}
	}
	
	
	
//========================================================BT-WIFI P2P ===========================================	
	/**
 * Show settings.
 */
	private void showSettings() {
		if(Constant.DEBUG)  Log.d(TAG, "showSettings() ");
		try {
			if(msetUpInstance != null){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mBottomLayout != null){
							mBottomLayout.setVisibility(View.INVISIBLE);
						}
						if(mContainer != null){
							DataStorage.setCurrentScreen(ScreenStyles.NAVIGATION);
							mContainer.removeAllViews();
							//HelpTip
							HelpTip.requestForHelp(msetUpInstance.getResources().getString(R.string.SETTING_UP_THE_LUKUPPLAYER),
									msetUpInstance.getResources().getString(R.string.SETUP_MSG1),msetUpInstance);
							
							DataStorage.setCurrentScreen(ScreenStyles.LUKUPOPTIONSELECTED);
							SettingsListView settings = new SettingsListView(msetUpInstance);
							ListView settingsList = settings.getList();
							mContainer.addView(settingsList,getLinearLayoutParams());
							settingsList.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
									
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									
									TextView  inflater = (TextView) v.findViewById(R.id.title);
									if(inflater != null){
										Typeface font = null;
										if(msetUpInstance != null){
											font = Typeface.createFromAsset(msetUpInstance.getAssets(), "OpenSans-Regular.ttf");  
										}
										if(font != null)
											inflater.setTypeface(font);  
										
										String selectedValue = inflater.getText().toString().trim();
										processRemoteSettingSelection(selectedValue);
									}
								}
							});
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
	
	/**
	 * Gets the linear layout params.
	 *
	 * @return the linear layout params
	 */
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}return standParams;
	}
	
	/**
	 * Process remote setting selection.
	 *
	 * @param headerTitle the header title
	 */
	private void processRemoteSettingSelection(String headerTitle) {
		try {
			DataStorage.setCurrentScreen(ScreenStyles.LUKUPOPTIONSELECTED);
			if(msetUpInstance != null){
				customLayout = new CustomLayout(msetUpInstance);
			}
			if(Constant.DEBUG)  Log.d(TAG,"Selected Item = "+headerTitle);
			if(Utils.checkNullAndEmpty(headerTitle)){
				
				if(headerTitle.equalsIgnoreCase("Setup Lukup Player")){
					Setupconnection();
					return;
				}
				
				if(headerTitle.equalsIgnoreCase("Authenticate")){
					SetPppoe();
					return;
				}
				
				if(headerTitle.equalsIgnoreCase("Setup Network")){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Setup");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String method = "com.port.apps.settings.Settings.getNetworkType"; 
					new AsyncDispatch(method, dispatchHashMap,true).execute();
					return;
				}
				if(headerTitle.equalsIgnoreCase("Live TV")){
					if(Constant.DEBUG)  Log.d(TAG,"live Tv selected");
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Setup");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String method = "com.port.apps.settings.Settings.getTVOperators"; 
					new AsyncDispatch(method, dispatchHashMap,true).execute();
					
					return;
				}
				if(headerTitle.equalsIgnoreCase("Profile")){
					if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
					Intent p = new Intent(Setup.this, Profile.class);
					p.putExtra("ActivityName", "Settings");
					startActivity(p);
					finish();
					return;
				}
				if(headerTitle.equalsIgnoreCase("Account Information")){
					if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("caller", "com.player.apps.Setup");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						String method = "com.port.apps.settings.Settings.getAccountInfo"; 
						new AsyncDispatch(method, dispatchHashMap,true).execute();
						return;
					}else{ //get Account Information from the SMS
						if(CommonUtil.isNetworkConnected(mActivity)){
							if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
								AuthDialog.authDialog(msetUpInstance);
							}
							
							progressDialog = new ProgressDialog(Setup.this,R.style.MyTheme);
							progressDialog.setCancelable(true);
							progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
							progressDialog.show();
							
							Intent account = new Intent(Setup.this, EPG.class);
							account.putExtra("subscriberid", mDataAccess.getSubscriberID());
							account.putExtra("handler", "com.player.webservices.getAccountInfo");
							startService(account);
						}else{
							HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
						}
						
					}
				}
				if(headerTitle.equalsIgnoreCase("About")){
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
					for(int i=0;i<ScreenStyles.ABOUT_TITLE_LIST.length;i++){
						HashMap<String,String> map = new HashMap<String, String>();
						map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.ABOUT_TITLE_LIST[i]);
						String description = Utils.getDataFromJSON(DataStorage.getAboutInfo(), ScreenStyles.ABOUT_TITLE_LIST[i]);
						if(Constant.DEBUG)  Log.d(TAG,ScreenStyles.ABOUT_TITLE_LIST[i]+" : "+ description);
						if(description != null && !description.equalsIgnoreCase("")){
							map.put(ScreenStyles.LIST_KEY_DESCRIPTION,description);
							list.add(map);
						}
						if (ScreenStyles.ABOUT_TITLE_LIST[i].equalsIgnoreCase("Player Version")) {
							map.put(ScreenStyles.LIST_KEY_DESCRIPTION,Constant.APP_VERSION);
							list.add(map);
						}
						
					}
					if(list != null){
						setAboutClickAction(list);
					}
				}
				if(headerTitle.equalsIgnoreCase("Help")){
					setHelpClickAction(headerTitle);
					return;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	/**
	 *  To display the Information about Mediaplayer.
	 *
	 * @param list the list
	 */
	private void setAboutClickAction(final ArrayList<HashMap<String, String>> list) {
		try {
			if(msetUpInstance != null){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(Constant.DEBUG)  Log.d(TAG,"inside setAboutClickAction()"+list.size());
						if(list != null && list.size() > 0){
							DataStorage.setCurrentScreen(ScreenStyles.INSIDE_SETTINGS);
							CustomListView listV = new CustomListView(msetUpInstance);
							listV.setCacheColorHint(Color.TRANSPARENT);
							final ListViewAdapter listViewAdapter = new ListViewAdapter(msetUpInstance, list, 0,ScreenStyles.ABOUT_TITLE_PAGE, null,-1);
							listV.setAdapter(listViewAdapter);
							if(mContainer != null){
								mContainer.removeAllViews();
								mContainer.addView(listV);
							}
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
	
	/**
	 * Sets the help click action.
	 *
	 * @param tag the new help click action
	 */
	private void setHelpClickAction(final String tag) {
		if(Constant.DEBUG)  Log.d(TAG  ,"setHelpClickAction() "+tag);
		if(msetUpInstance != null){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						OverLay overlay = new OverLay(msetUpInstance);
						String message = msetUpInstance.getResources().getString(R.string.HELP_MSG);
						final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, tag, message, null,null,DataStorage.isShowSelected());
						if(dialog != null){
							Button okBtn = (Button) dialog.findViewById(R.id.okButton);
							if(okBtn != null){
								okBtn.setText("YES");
								okBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {//Changed by to map the Layout api
										mDataAccess.updateSetupDB("Helptip", "true");
										if(dialog != null && dialog.isShowing()){
											dialog.dismiss();
										}
									}
								});
							}
							Button cancelBtn = (Button) dialog.findViewById(R.id.overlayCancelButton);
							if(cancelBtn != null){
								cancelBtn.setVisibility(View.VISIBLE);
								cancelBtn.setText("NO");
								cancelBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										mDataAccess.updateSetupDB("Helptip", "false");
										if(dialog != null && dialog.isShowing()){
											dialog.dismiss();
										}
									}
								});
							}
							dialog.show();
						}
					}
				}
			});
		}
	}

	/**
	 *  To display the Information about Mediaplayer.
	 *
	 * @param list the list
	 */
	private void showAccountInfoPage(final ArrayList<HashMap<String, String>> list) {
		try {
			if(msetUpInstance != null){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(list != null && list.size() > 0){
							if(Constant.DEBUG)  Log.d(TAG,"inside showAccountInfoPage()"+list.size());
							DataStorage.setCurrentScreen(ScreenStyles.INSIDE_SETTINGS);
							CustomListView showListview = new CustomListView(msetUpInstance);
							showListview.setCacheColorHint(Color.TRANSPARENT);
							final ListViewAdapter listViewAdapter = new ListViewAdapter(msetUpInstance, list, 0,ScreenStyles.ACCOUNT_INFO_TITLE_PAGE, null,-1);
							showListview.setAdapter(listViewAdapter);
							if(mContainer != null){
								mContainer.removeAllViews();
								mContainer.addView(showListview);
							}
						}						
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	/**
	 * setup network in mediaplayer.
	 */
	private void setUpNetworktomediaplayer() {
		if(msetUpInstance != null ){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						mContainer.removeAllViews();
					}
					DataStorage.setCurrentScreen(ScreenStyles.SETUP_NETWORKSCREEN);
					//HelpTip
					HelpTip.requestForHelp(msetUpInstance.getResources().getString(R.string.CONNECT_YOUR_LUKUPPLAYER),
							msetUpInstance.getResources().getString(R.string.SETUP_MSG2),msetUpInstance);			
					ListView list = new ListView(msetUpInstance);
					final ArrayList<HashMap<String,String>> setuplist = new ArrayList<HashMap<String,String>>();
					int selectedoption =-1;
					
					HashMap<String, String> setupMap;
					if(DataStorage.getDeviceType() != null && (DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S"))){
						setupMap = new HashMap<String, String>();
						setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.NETWORK_ITEM_LIST[0]);
						setupMap.put(ScreenStyles.LIST_KEY_THUMB_URL,null);
						setuplist.add(setupMap);
						if(networkType != null && networkType.equalsIgnoreCase(ScreenStyles.NETWORK_ITEM_LIST[0].trim())){
							selectedoption = 0;
						}
					}else{
						for(int i=0;i<ScreenStyles.NETWORK_ITEM_LIST.length;i++){
							setupMap = new HashMap<String, String>();
							setupMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.NETWORK_ITEM_LIST[i]);
							hotSpotStatus = setupDetails.getBoolean("HotSpot", false);
							if(Constant.DEBUG) Log.w(TAG, "setUpNetworktomediaplayer().hotSpotStatus is ::"+hotSpotStatus);
							if(Constant.DEBUG) Log.w(TAG, "setUpNetworktomediaplayer().isBroadbandSelected is ::"+isBroadbandSelected);
							if(ScreenStyles.NETWORK_ITEM_LIST[i].equalsIgnoreCase("Wi-Fi hotspot")){
								if(hotSpotStatus || mDataAccess.getConnectionType().equalsIgnoreCase("wifi")){
									setupMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Enabled");
								}else{
									setupMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Disabled");
								}
							}else if(ScreenStyles.NETWORK_ITEM_LIST[i].equalsIgnoreCase("Broadband")){
								if(networkType != null && networkType.equalsIgnoreCase("ethernet")){
									setupMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Enabled");
								}else{
									setupMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Disabled");
								}
							}else if(ScreenStyles.NETWORK_ITEM_LIST[i].equalsIgnoreCase("Wireless")){
								if(networkType != null && networkType.equalsIgnoreCase("wifi")){
									setupMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Enabled");
								}else{
									setupMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Disabled");
								}
							}
							
							setuplist.add(setupMap);
							if(networkType != null && networkType.equalsIgnoreCase(ScreenStyles.NETWORK_ITEM_LIST[i].trim())){
								selectedoption = i;
							}
						}
					}
					if (networkType.equalsIgnoreCase("ethernet")) { 
//					if (isBroadbandSelected) {
						list.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, setuplist,0,"networklist", "enable",selectedoption);
						list.setAdapter(adapter);
					}else{
						list.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, setuplist,0,"networklist", "",selectedoption);
						list.setAdapter(adapter);
					}

					if(mContainer != null && list != null){
						mContainer.setOrientation(LinearLayout.VERTICAL);
						mContainer.addView(list);
						list.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
								
								if (maingrid_select_index != position){      
									if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
									{
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
									}
									maingridview_focus = v;
									currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
									currentTextView = (TextView) v.findViewById(R.id.title);
									currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
									currentTextView.setTextColor(getResources().getColor(R.color.pink));
								}else if(maingridview_focus != null){
									Log.w(TAG, "OnItemClickListener().position is ::"+position);
									currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
									currentTextView.setTextColor(getResources().getColor(R.color.white));
									maingridview_focus = v;
									currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
									currentTextView = (TextView) v.findViewById(R.id.title);
								}
								maingrid_focus_index = position;
								currentPostion = position;
								
								HashMap<String, String> map= setuplist.get(position);
								if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
									String val = map.get(ScreenStyles.LIST_KEY_TITLE);
									if(val.equalsIgnoreCase(ScreenStyles.NETWORK_ITEM_LIST[0])){
										showBroadbandAction(val);
									}
									if(val.equalsIgnoreCase(ScreenStyles.NETWORK_ITEM_LIST[1])){
										dispatchHashMap  = new ArrayList<HashMap<String,String>>();
										HashMap<String, String> list = new HashMap<String, String>();
										list.put("consumer", "TV");
										list.put("network",mDataAccess.getConnectionType());
										list.put("caller", "com.player.apps.Setup");
										list.put("called", "startService");
										dispatchHashMap.add(list);
										String method = "com.port.apps.settings.Settings.getWifiNetworks"; 
										new AsyncDispatch(method, dispatchHashMap,true).execute();
									}
									if(val.equalsIgnoreCase(ScreenStyles.NETWORK_ITEM_LIST[2])){
										String enable="";
										hotSpotStatus = setupDetails.getBoolean("HotSpot", false);
										if(Constant.DEBUG) Log.w(TAG, "setUpNetworktomediaplayer().hotSpotStatus is ::"+hotSpotStatus);
										if(hotSpotStatus || mDataAccess.getConnectionType().equalsIgnoreCase("wifi")){
											enable = "false";
										} else {
											enable = "true";
										}
										if (networkType.equalsIgnoreCase("ethernet") || networkType.equalsIgnoreCase("")) { 
//											if(hotSpotStatus || mDataAccess.getConnectionType().equalsIgnoreCase("wifi")){
//												dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//												HashMap<String, String> list = new HashMap<String, String>();
//												list.put("consumer", "TV");
//												list.put("network",mDataAccess.getConnectionType());
//												list.put("hotspotenable", enable);
//												list.put("caller", "com.player.apps.Setup");
//												list.put("called", "startService");
//												dispatchHashMap.add(list);
//												String method = "com.port.apps.settings.Settings.createWifiHotspot"; 
//												new AsyncDispatch(method, dispatchHashMap,true).execute();
//											}else{
//												HotspotPassword(enable);
//											}
											HotspotPassword(enable);
										}else{
											HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.BROADBAND_NOT_SELECTED), 1000);
										}
									}
								}
							}
						});
					}
				}
			});
		}
	}
	
	/**
	 * Hotspot password.
	 *
	 * @param status the status
	 */
	private void HotspotPassword(final String status){
		if(msetUpInstance != null ){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(Constant.DEBUG)  Log.d(TAG,"HotspotPassword: ");
					if(!(setupDetails.contains("Hotspotuname") && setupDetails.contains("Hotspotpwd")))
					{
						setupedit.putString("Hotspotuname", "");
						setupedit.putString("Hotspotpwd", "");
						setupedit.commit();
					}

					final EditText mUsername;
					final EditText mPassword;
					OverLay overlay = new OverLay(msetUpInstance);
					final String title = "HotSpot";
					if(Constant.DEBUG)  Log.d(TAG,"setting Hotspot");
					final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL, title, null,null,null,true);
					
						mUsername = (EditText) dialog.findViewById(R.id.username);
						mUsername.setText(setupDetails.getString("Hotspotuname", "LUKUP PLAYER"));
						mUsername.setHint("Name");
						
						mPassword = (EditText) dialog.findViewById(R.id.password);
						mPassword.setText(setupDetails.getString("Hotspotpwd", ""));
						mPassword.setHint("password");
						mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						
						Button loginButton = (Button) dialog.findViewById(R.id.connect);
						Button cancleButton = (Button) dialog
								.findViewById(R.id.overlayCancelButton);
						loginButton.setText("SAVE");
						cancleButton.setText("CLOSE");
						
						
						// loginButton.setText("Forget");
						if (loginButton != null) {
							loginButton.setVisibility(View.VISIBLE);
							loginButton.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if(Constant.DEBUG)  Log.d(TAG,"Hotspot login() ");
									if (!(( mUsername.getText().toString().equalsIgnoreCase(""))
											&& (mPassword
													.getText().toString().equalsIgnoreCase("")))) {
										setupedit.putString("Hotspotuname", mUsername.getText().toString());
										setupedit.putString("Hotspotpwd", mPassword.getText().toString());
										setupedit.commit();
										
										
										try{
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("hotspotenable", status);
											list.put("username", mUsername.getText().toString());
											list.put("password",mPassword.getText().toString());
											list.put("caller", "com.player.apps.Setup");
											list.put("called", "startService");
											dispatchHashMap.add(list);
											String method = "com.port.apps.settings.Settings.createWifiHotspot"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();
											dialog.dismiss();

											}catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
											}
										
										
										Log.e(TAG, mUsername.getText().toString());
										dialog.cancel();
									}
									else
									{
										if(Constant.DEBUG)  Log.d(TAG,"Hotspot Error Provide Name and Pwd ");
										mUsername.setError("Enter the Hotspot Name");
										mPassword.setError("Enter the  PassWord");
									}
								}
							});
						}
						if (cancleButton != null) {
							cancleButton.setVisibility(View.VISIBLE);
							cancleButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG)  Log.d(TAG,"Hotspot cancel() ");
									dialog.cancel();
									
								}
							});
						}
						
						dialog.show();
					//}
				}
			});
		}
	}
	
	
	/**
	 * Show Wifi list in the remote.
	 *
	 * @param wifiNetworkList the wifi network list
	 */
	private void showAvailableWifiNetwork(final ArrayList<HashMap<String, String>> wifiNetworkList) {
		if(msetUpInstance != null){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						DataStorage.setCurrentScreen("setupwifi");
						mContainer.removeAllViews();
						ListView list = new ListView(msetUpInstance);
						list.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, wifiNetworkList,0,"wifilist", null,-1);
						list.setAdapter(adapter);
						if(mContainer != null && list != null){
							mContainer.setOrientation(LinearLayout.VERTICAL);
							mContainer.addView(list);
							list.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
									
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									
									HashMap<String, String> map = wifiNetworkList.get(position);
									if (map.containsKey("bssid")) {
										bssid = map.get("bssid");
									}
									if (map.containsKey("capabilities")) {
										wifiSecurity = map.get("capabilities");
									}
									if (wifiSecurity.indexOf("WPA") != -1) {
										wifiSecurity = "WPA";
									} else if (wifiSecurity.indexOf("WEP") != -1) {
										wifiSecurity = "WEP";
									} else {
										wifiSecurity = "OPEN";
									}
//									final int networkId = setupDetails.getInt("WifiId", 0);
									if(Constant.DEBUG)  Log.d("Settings","showAvailableWifiNetwork() wifiSecurity "+ wifiSecurity);
									if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
										final String name = map.get(ScreenStyles.LIST_KEY_TITLE);
										final String desc = map.get(ScreenStyles.LIST_KEY_DESCRIPTION);
										if(Constant.DEBUG)  Log.d(TAG,"id: "+name+ ", Desc: "+desc);
										OverLay overlay = new OverLay(msetUpInstance);
										if(desc.equalsIgnoreCase("connected")){
											String message = msetUpInstance.getResources().getString(R.string.WIFI_DISCONNECTED_MSG);
											final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, "Disconnect", message,null,null,false);
											if(dialog != null){
												Button okBtn = (Button) dialog.findViewById(R.id.okButton);
												if(okBtn != null){
													okBtn.setText("YES");
													okBtn.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(View arg0) {
															dispatchHashMap  = new ArrayList<HashMap<String,String>>();
															HashMap<String, String> list = new HashMap<String, String>();
															list.put("consumer", "TV");
															list.put("network",mDataAccess.getConnectionType());
															list.put("caller", "com.player.apps.Setup"); 
															list.put("called", "startService"); 
//															list.put("id", networkId+"");
															dispatchHashMap.add(list);
															String method = "com.port.apps.settings.Settings.disconnect"; 
															new AsyncDispatch(method, dispatchHashMap,true).execute();
															
															if(dialog != null && dialog.isShowing()){
																dialog.dismiss();
															}
														}
													});
												}
												
												Button cancelBtn = (Button) dialog.findViewById(R.id.overlayCancelButton);
												if(cancelBtn != null){
													cancelBtn.setVisibility(View.VISIBLE);
													cancelBtn.setText("NO");
													cancelBtn.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(View arg0) {
															if(dialog != null && dialog.isShowing()){
																dialog.dismiss();
															}
														}
													});
												}
												dialog.show();
												
											}
										}else{
											
											final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_WFILIST, "Set password", name,null,null,false);
											
											if(dialog != null){ 
												dialog.show(); 
												Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton); 
												if(cancelBtn != null) 
													cancelBtn.setOnClickListener(new OverlayCancelListener(dialog)); 

												Button sendBtn = (Button) dialog.findViewById(R.id.overlayOkButton); 
												sendBtn.setText("CONNECT"); 
												final EditText textfield = (EditText) dialog.findViewById(R.id.keyValue); 

												if(sendBtn != null){ 
													sendBtn.setOnClickListener(new OnClickListener() { 
														@Override 
														public void onClick(View arg0) { 
															String textdata = null; 
															if(textfield != null){ 
																textdata = textfield.getText().toString().trim(); 
																if(textdata == null || textdata.equalsIgnoreCase("")){ 
																	HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.EMPTY_INPUT), 2000); 
																	return; 
																} 
															} 
															try{ 
																dispatchHashMap  = new ArrayList<HashMap<String,String>>(); 
																HashMap<String, String> list = new HashMap<String, String>(); 
																list.put("consumer", "TV"); 
																list.put("network",mDataAccess.getConnectionType()); 
																list.put("networktype",wifiSecurity);
																list.put("bssid",bssid);
																list.put("password",textdata); 
																list.put("ssid", name); 
																list.put("caller", "com.player.apps.Setup"); 
																list.put("called", "startService"); 
																dispatchHashMap.add(list); 
																String method = "com.port.apps.settings.Settings.connectToNetwork"; 
																new AsyncDispatch(method, dispatchHashMap,true).execute(); 
																
																if(dialog != null && dialog.isShowing()){
																	dialog.dismiss();
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
											}
										}
									}
								}
							});
							
						}
					}
				}
			});
		}
	}
	
	/**
	 * show Broadband options.
	 *
	 * @param val the val
	 */
	private void showBroadbandAction(final String val) {

		if(msetUpInstance != null){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						DataStorage.setCurrentScreen("setupbroadband");
						mContainer.removeAllViews();
						//HelpTip
						HelpTip.requestForHelp(msetUpInstance.getResources().getString(R.string.CONNECTING_TO_WIRED_BROADBAND),
								msetUpInstance.getResources().getString(R.string.SETUP_MSG3),msetUpInstance);
						
						ListView list = new ListView(msetUpInstance);
						final ArrayList<HashMap<String,String>> networkTypeList = new ArrayList<HashMap<String,String>>();
						int selectedoption =-1;
						
						for (int i = 0; i < ScreenStyles.BROADBAND_LIST.length; i++) {
							HashMap<String, String> broadbandType = new HashMap<String, String>();
							broadbandType.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.BROADBAND_LIST[i]);
							networkTypeList.add(broadbandType);
						}
						
						list.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, networkTypeList,0,"networklist", null,selectedoption);
						list.setAdapter(adapter);
						if(mContainer != null && list != null){
							mContainer.setOrientation(LinearLayout.VERTICAL);
							mContainer.addView(list);
							list.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
									
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									
									HashMap<String, String> map= networkTypeList.get(position);
									if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
										String val = map.get(ScreenStyles.LIST_KEY_TITLE);
										if(val.equalsIgnoreCase("Auto Detect network")){
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("mode", "Auto");
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.Setup");
											list.put("called", "startService");
											dispatchHashMap.add(list);
											String method = "com.port.apps.settings.Settings.setRJ45"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();
											
											if(Constant.DEBUG)  Log.d(TAG,"isSetup: "+isSetup);
											if(isSetup == false){
												showSetUpList();
											}else{
												showSettings();
											}
											
											setupedit.putString("ManualIP", "");
											setupedit.putString("SubnetIP", "");
											setupedit.putString("GatewayIP", "");
											setupedit.putString("DNS", "");
											setupedit.commit();
											
										}
										if(val.equalsIgnoreCase("Select network manually")){
											setManualNetworkIDFromBroadBandOption();
										}
					
										if(val.equalsIgnoreCase("Authenticate")){
											SetPppoe();
										}
									}
								}
							});
						}
					}
				}
			});
		}
	}
	
//  Added by @Tomesh on 22nd June 2015 for PPPoE option in Broadband 	
	/**
 * Show PPPoE option In remote.
 */
	public void SetPppoe(){
		if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			if(Constant.DEBUG)  Log.d(TAG,"PPPoE: ");
			if(!(setupDetails.contains("username") && setupDetails.contains("password")))
			{
				setupedit.putString("PPPoEusername", "");
				setupedit.putString("PPPoEpassword", "");
				setupedit.commit();
			}
	
			if(Constant.DEBUG)  Log.d(TAG,"show PPPoe ");
			
			OverLay overlay = new OverLay(msetUpInstance);
			final String title = "PPPoE";
			if(Constant.DEBUG)  Log.d(TAG,"show PPPoe ");
			final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL, title, null,null,null,true);
			
			mUsername = (EditText) dialog.findViewById(R.id.username);
			mUsername.setText(setupDetails.getString("PPPoEusername", ""));
			mUsername.setHint("Username");
			
			mPassword = (EditText) dialog.findViewById(R.id.password);
			mPassword.setText(setupDetails.getString("PPPoEpassword", ""));
			mPassword.setHint("password");
			mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			
			Button loginButton = (Button) dialog.findViewById(R.id.connect);
			Button cancleButton = (Button) dialog
					.findViewById(R.id.overlayCancelButton);
			cancleButton.setText("Disconnect");
			
			if(Constant.DEBUG)  Log.d(TAG,"PPPoE:enter ");
			// loginButton.setText("Forget");
			if (loginButton != null) {
				loginButton.setVisibility(View.VISIBLE);
				loginButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (!(( mUsername.getText().toString().equalsIgnoreCase(""))
								&& (mPassword
										.getText().toString().equalsIgnoreCase("")))) {
							dispatchHashMap  = new ArrayList<HashMap<String,String>>();
							HashMap<String, String> list = new HashMap<String, String>();
							list.put("consumer", "TV");
							list.put("network",mDataAccess.getConnectionType());
							list.put("caller", "com.player.apps.Setup");
							list.put("called", "startService");
							list.put("pppoestatus", "connect");
							list.put("username", mUsername.getText().toString());
							list.put("password", mPassword.getText().toString());
							dispatchHashMap.add(list);
							String method = "com.port.apps.settings.Settings.setpppoe";//com.port.apps.settings.Settings.setPppoe
							new AsyncDispatch(method, dispatchHashMap,true).execute();
							Log.e(TAG, mUsername.getText().toString());
							
							if(dialog != null && dialog.isShowing()){
								dialog.dismiss();
							}
						}
						else
						{
//							mUsername.setError("Enter the correct UserName");
//							mPassword.setError("Enter the correct PassWord");
							dispatchHashMap  = new ArrayList<HashMap<String,String>>();
							HashMap<String, String> list = new HashMap<String, String>();
							list.put("consumer", "TV");
							list.put("network",mDataAccess.getConnectionType());
							list.put("caller", "com.player.apps.Setup");
							list.put("called", "startService");
							list.put("pppoestatus", "forget");
							dispatchHashMap.add(list);
							String method = "com.port.apps.settings.Settings.setpppoe";//com.port.apps.settings.Settings.setPppoe
							new AsyncDispatch(method, dispatchHashMap,true).execute();
							
							if(dialog != null && dialog.isShowing()){
								dialog.dismiss();
							}
						}
					}
				});
			}
			if (cancleButton != null) {
				cancleButton.setVisibility(View.VISIBLE);
				cancleButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(dialog != null && dialog.isShowing()){
							dialog.dismiss();
						}					
					}
				});
			}
			dialog.show();
		}else{ //show authentication dialog to verify user on SMS
			AuthDialog.authDialog(msetUpInstance);
		}
	}
	
	
	/**
	 * Show Broadband option In remote.
	 */
	private void setManualNetworkIDFromBroadBandOption() {
		if(msetUpInstance != null){
			msetUpInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					DataStorage.setCurrentScreen("setupmanualbroadband");
					mContainer.removeAllViews();
					LayoutInflater inflater = msetUpInstance.getLayoutInflater();
					View v = inflater.inflate(R.layout.broadband, null);
					if(v != null){
						if(mContainer != null){
							Button saveBtn = (Button) v.findViewById(R.id.location_save);
							mContainer.removeAllViews();
							mContainer.addView(v);
							final EditText ipText = (EditText) v.findViewById(R.id.iptext);
							final EditText subnetText = (EditText) v.findViewById(R.id.subnetText);
							final EditText gatewayText = (EditText) v.findViewById(R.id.gatewayText);
							final EditText dnsText = (EditText) v.findViewById(R.id.dnsText);
							
							String ip = setupDetails.getString("ManualIP", "");
							String subnet = setupDetails.getString("SubnetIP", "");
							String gateway = setupDetails.getString("GatewayIP", "");
							String dns = setupDetails.getString("DNS", "");
							
							saveBtn.setText("SAVE");
							
							if(!ip.trim().equalsIgnoreCase("")){
								ipText.setText(ip);
							}if(!subnet.trim().equalsIgnoreCase("")){
								subnetText.setText(subnet);
							}if(!gateway.trim().equalsIgnoreCase("")){
								gatewayText.setText(gateway);
							}if(!dns.trim().equalsIgnoreCase("")){
								dnsText.setText(dns);
							}

							if(saveBtn != null && saveBtn.getVisibility() == View.VISIBLE){
								InputFilter[] filters = new InputFilter[1];
								filters[0] = new InputFilter() {
									public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
										if (end > start) {
											String destTxt = dest.toString();
											String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
											if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) { 
												return "";
											} else {
												String[] splits = resultingTxt.split("\\.");
												for (int i=0; i<splits.length; i++) {
													if (Integer.valueOf(splits[i]) > 255) {
														return "";
													}
												}
											}
										}
										return null;
									}
								};
								if(ipText != null)
									ipText.setFilters(filters);
								if(subnetText != null)
									subnetText.setFilters(filters);
								if(gatewayText != null)
									gatewayText.setFilters(filters);
								if(dnsText != null)
									dnsText.setFilters(filters);

								saveBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
											if(ipText != null){
												System.out.println(ipText.getText().toString().trim().matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?"));
												if (!ipText.getText().toString().trim().matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?") || !checkValidIp(ipText.getText().toString().trim())) {
													if(Constant.DEBUG)  Log.d(TAG,"enter valid id");
//													HelpText.showHelpTextDialog(msetUpInstance, ScreenStyles.CORRECT_IP, 1000);
													HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.CORRECT_IP), 1000);
													
													return;
												}
												broadband_ip = ipText.getText().toString().trim();
												setupedit.putString("ManualIP", broadband_ip);
												if(Constant.DEBUG)  Log.d(TAG,"broadband_ip: "+broadband_ip);
											}
											if(subnetText != null){
												if (!subnetText.getText().toString().trim().matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")  || !checkValidIp(subnetText.getText().toString().trim())) {
													if(Constant.DEBUG)  Log.d(TAG,"enter subnet address");
													HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.CORRECT_SUBNET), 1000);
													return;
												}
												broadband_subnet = subnetText.getText().toString().trim();
												setupedit.putString("SubnetIP", broadband_subnet);
												if(Constant.DEBUG)  Log.d(TAG,"broadband_subnet: "+broadband_subnet);
											}
											if(gatewayText != null){
												if (!gatewayText.getText().toString().trim().matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")  || !checkValidIp(gatewayText.getText().toString().trim())) {
													if(Constant.DEBUG)  Log.d(TAG,"enter gateway address");
													HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.CORRECT_GATEWAY), 1000);
													return;
												}
												broadband_gateway = gatewayText.getText().toString().trim();
												setupedit.putString("GatewayIP", broadband_gateway);
												if(Constant.DEBUG)  Log.d(TAG,"broadband_gateway: "+broadband_gateway);
											}
											if(dnsText != null){
												if (!dnsText.getText().toString().trim().matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")  || !checkValidIp(dnsText.getText().toString().trim())) {
													if(Constant.DEBUG)  Log.d(TAG,"enter dns address");
													HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.CORRECT_DNS), 1000);
													return;
												}
												broadband_dns = dnsText.getText().toString().trim();
												setupedit.putString("DNS", broadband_dns);
												if(Constant.DEBUG)  Log.d(TAG,"broadband_dns: "+broadband_dns);
											}
											setupedit.commit();
											sendManualNetworkData();
											if(!isSetup){
												showSetUpList();
											}										
										}									

									/**
									 * @param screenId
									 */
									private void sendManualNetworkData() {
										try{
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("manualip", broadband_ip+"");
											list.put("gateway", broadband_gateway+"");
											list.put("subnet", broadband_subnet+"");
											list.put("dns", broadband_dns+"");
											list.put("mode", "Manual");
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.Setup");
											list.put("called", "startService");
											dispatchHashMap.add(list);
											String method = "com.port.apps.settings.Settings.setRJ45"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();
											
											
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
					}
				}
			});
		}
	}
	
	/**
	 * check whether the entering ip(String) is valid or not .
	 *
	 * @param s - ip entered
	 * @return ture/false valid ip
	 */
	private boolean checkValidIp(String s)
	{
		try{
			if(Constant.DEBUG)  Log.d(TAG,"Is Valid IP "+s+"   "+InetAddressUtils.isIPv4Address(s));
			return InetAddressUtils.isIPv4Address(s);
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			return false;
		}
	}
	
	/**
	 * Show distributor id.
	 */
//	private void showDistributorId() {
//		try {
//			if(msetUpInstance != null){
//				msetUpInstance.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						distributorID = null;
//						//HelpTip
//						HelpTip.requestForHelp(msetUpInstance.getResources().getString(R.string.SUPPORT_FROM_YOUR_LOCAL_DISTRIBUTOR),
//								msetUpInstance.getResources().getString(R.string.SETUP_MSG5),msetUpInstance);
//						
//						OverLay overlay = new OverLay(msetUpInstance);
//						final String title = msetUpInstance.getResources().getString(R.string.DISTRIBUTOR);
//						final Dialog dialog = overlay .getOverLay(ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL, title, null,null,null,isLukupSelected);
//						if(dialog != null){
//							dialog.show();
//							Button connectBtn =(Button)dialog.findViewById(R.id.connect);
//							Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
//							TextView titleView = (TextView) dialog.findViewById(R.id.overlayTitle);
//							if(titleView != null){
//								titleView.setText(title);
//							}
//							final EditText textfield = (EditText) dialog.findViewById(R.id.username);
//							final EditText passwordfield = (EditText) dialog.findViewById(R.id.password);
//							if(textfield != null){
//								textfield.setHint(msetUpInstance.getResources().getString(R.string.ENTER_DISTRIBUTOR_ID));
//								textfield.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
//							}
//							if(passwordfield != null){
//								passwordfield.setHint(msetUpInstance.getResources().getString(R.string.RE_ENTER_DISTRIBUTOR_ID));
//								passwordfield.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
//							}
//							if(connectBtn != null){
//								connectBtn.setText(msetUpInstance.getResources().getString(R.string.SUBMIT));
//								connectBtn.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View arg0) {
//										if(textfield != null && passwordfield != null){
////											String screenid = screenId;
//											String distributionid = textfield.getText().toString().trim();
//											String reenterdistributionid  = passwordfield.getText().toString().trim();
//											if(distributionid == null || distributionid.equalsIgnoreCase("")){
//												HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
//												return;
//											}
//											if(reenterdistributionid == null || reenterdistributionid.equalsIgnoreCase("")){
//												HelpText.showHelpTextDialog(msetUpInstance,msetUpInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
//												return;
//											}
//											if(Utils.checkNullAndEmpty(distributionid) && !distributionid.equalsIgnoreCase(reenterdistributionid)){
//												HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.DISTRIBUTOR_NOT_MATCHED),2000);
//												return;
//											}
//											if(Constant.DEBUG)  Log.d(TAG,"Sending Detail distribution id "+distributionid);
//											distributorID = distributionid;
//											
//											if(dialog != null && dialog.isShowing()){
//												dialog.cancel();
//											}
//										}
//									}
//								});
//							}
//							if(cancelBtn != null){
//								cancelBtn.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										distributorID = "0000";
////										showSetUpList();
//										if(dialog != null && dialog.isShowing()){
//											dialog.cancel();
//										}
//										if(isSetup == false){
//											showSetUpList();
//										}else{
//											showSettings();
//										}
//									}
//								});
//							}
//						}
//					}
//				});
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
//		}
//	}
	
	/**
	 * Sendlive tv operator.
	 *
	 * @param liveTVOperatorname the live tv operatorname
	 * @param id the id
	 * @throws JSONException the JSON exception
	 */
	private void sendliveTVOperator(String liveTVOperatorname,String id) throws JSONException {
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("liveTVOperator", liveTVOperatorname);
		list.put("liveTVOperatorid", id);
		list.put("consumer", "TV");
		list.put("network",mDataAccess.getConnectionType());
		list.put("caller", "com.player.apps.Setup");
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.settings.Settings.setTVOperator"; 
		new AsyncDispatch(method, dispatchHashMap,true).execute();
	}
	
	
	
	/**
	 * show satellite list .
	 *
	 * @param liveTVOperatorList the live tv operator list
	 * @param selectedPosition the selected position
	 */
	private void showliveTVOperatorList(final ArrayList<HashMap<String, String>> liveTVOperatorList,final int selectedPosition) {
		try {
			if(msetUpInstance != null ){
				msetUpInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							mContainer.removeAllViews();
						}
						//HelpTip
						HelpTip.requestForHelp(msetUpInstance.getResources().getString(R.string.CONTROLLING_YOUR_EXISTING_TV_SET_TOPBOX),
								msetUpInstance.getResources().getString(R.string.SETUP_MSG4),msetUpInstance);
						
						DataStorage.setCurrentScreen(ScreenStyles.SETUP_OPERATOR_LIST);
						ListView list = new ListView(msetUpInstance);
						setuplist = liveTVOperatorList;
						if(liveTVOperatorList == null || liveTVOperatorList.size() == 0){
							return;
						}
						list.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(msetUpInstance, setuplist,0,"liveTVOperatorlist", null,selectedPosition);
						list.setAdapter(adapter);

						if(mContainer != null){
							mContainer.addView(list);
							list.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View v, int position, long arg3) {
									
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "OnItemClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "OnItemClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									
									HashMap<String, String> map= setuplist.get(position);
									String name ="";
									String id = "";
									if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
										name = map.get(ScreenStyles.LIST_KEY_TITLE);
									}
									if(map.containsKey("id")){
										id = map.get("id");
									}
									
									selectedliveTVOperator = name;
									DataStorage.setConnectedVendor(name.toLowerCase().trim());

									try{
										if(!isSetup){
											sendliveTVOperator(name,id);
											showSetUpList();
										}else{
											sendliveTVOperator(name,id);
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
	
		
	/* (non-Javadoc)
	 * @see com.layout.Layout#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			return backButtonAction(keyCode, event);
		}if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
			HelpTip.close(msetUpInstance);
			if (isSetup && Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")) {
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(Setup.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "AppGuide");
				startActivity(lukup);
				finish();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	/** *********************************************************************************************. */
	
	public final BroadcastReceiver mSetupReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "BroadcastReceiver ");
			Bundle extras = intent.getExtras();
			String jsondata = "";
			String method = "";
			JSONObject objData = null;
	
			if(extras != null){
				if(extras.containsKey("Params")){
					jsondata = extras.getString("Params");
					method = extras.getString("Handler");
					try {
						objData = new JSONObject(jsondata);
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
					processUIData(method, objData);
				}
				if(extras.containsKey("RemoteConnected")){
					
					if(extras.getString("RemoteConnected").equalsIgnoreCase("ok")){
						//getting Device Info
						
						String wifiStatus = setupDetails.getString("SelectWifi", "");
						
						if(Constant.DEBUG)  Log.d(TAG, "Fired after Player is connected, fetching DeviceInfo "+System.getProperty("persist.version"));
						
						//Mar 20 2015
//						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//						HashMap<String, String> list1 = new HashMap<String, String>();
//						list1.put("consumer", "TV");
//						list1.put("network", mDataAccess.getConnectionType());
//						list1.put("caller", "com.player.apps.Setup");
//						list1.put("called", "startProject");
//						dispatchHashMap.add(list1);
//						method = "com.port.apps.settings.SettingsActivity.start";
//						new AsyncDispatch(method, dispatchHashMap,true).execute();
						
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("playerapp", Constant.APP_VERSION);
						list.put("playerfirmware", System.getProperty("persist.version"));
						if(wifiStatus != null && !wifiStatus.equalsIgnoreCase("")){
							list.put("wifiName", wifiStatus);
						}else{
							list.put("wifiName", "");
						}
						list.put("caller", "com.player.apps.Setup");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						method = "com.port.apps.settings.Settings.getDeviceInfo";
						new AsyncDispatch(method, dispatchHashMap,true).execute();	
						
						if(msetUpInstance != null ){
							msetUpInstance.runOnUiThread(new Runnable() {
								@Override
								public void run() {
								Drawable mBT_on_drawable = mActivity.getResources().getDrawable(R.drawable.v13_ico_wifi_b_01);
								bluetooth_indicator.setBackgroundDrawable(mBT_on_drawable);
								}
							});
						}
					}
					
				}
				if(extras.containsKey("authenticated")){
					Intent lukup = new Intent(Setup.this, com.player.apps.AppGuide.class);
					lukup.putExtra("ActivityName", "Setup");
					startActivity(lukup);
					finish();
				}
			}
		}
	};
	
	/**
	 * process information from Mediaplayer based on screen id.
	 *
	 * @param handler the handler
	 * @param jsonData the json data
	 */
	private void processUIData(String handler,final JSONObject jsonData){
		try {
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.dismiss();
				if(Constant.DEBUG)  Log.d(TAG , "progressDialog dismiss()");
			}
			
			if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.setRJ45")){
				String mode = Utils.getDataFromJSON(jsonData, "mode");
				if(Constant.DEBUG)  Log.d(TAG,"processUIData().mode: "+mode);
				if(jsonData != null){
					if(jsonData.has("result")){
						if(msetUpInstance != null ){
							
							msetUpInstance.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									String result = "";
									try {
										result = jsonData.getString("result");
									} catch (JSONException e1) {
										e1.printStackTrace();
									}
									if(result.equalsIgnoreCase("success")){
										isNetworkSelected = true;
										isBroadbandSelected = true;
										Drawable mRJ45_drawable = msetUpInstance.getResources().getDrawable(R.drawable.v13_ico_network_02);
						            	ethernet_indicator.setBackgroundDrawable(mRJ45_drawable);
						            	
						            	if(jsonData.has("msg")){
											try {
												HelpText.showHelpTextDialog(msetUpInstance, jsonData.getString("msg"), 2000);
											}catch (Exception e) {
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
											}
										}
									}else {
										HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.ETHENET_CONNECTION_FAILED), 2000);
										Drawable mRJ45_drawable = msetUpInstance.getResources().getDrawable(R.drawable.v13_ico_network_01);
						            	ethernet_indicator.setBackgroundDrawable(mRJ45_drawable);
									}	
								}
							});
						}
					}
				}
				if(!isSetup){
					showSetUpList();
				}else{
					showSettings();
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.connectToNetwork")){
				try {
					if(msetUpInstance != null){
						try{
							if(jsonData.has("result")){
								String tag = jsonData.getString("tag");
								String result = jsonData.getString("result");
								if(Constant.DEBUG)  Log.d(TAG,"result: "+result);
								if(tag.equalsIgnoreCase("connect")){
									if(result.equalsIgnoreCase("success")){
										isNetworkSelected = true;
										isBroadbandSelected = false;
										selectedWifi = jsonData.getString("ssid");
										setupedit.putString("SelectWifi", selectedWifi);
										setupedit.commit();
										HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.WIFI_CONNECTED), 2000);
									}else{
										HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.WIFI_CONNECTED_FAILURE), 2000);
									}
								}else{
									if(result.equalsIgnoreCase("success")){
										setupedit.putString("SelectWifi", "");
										setupedit.commit();
									}else{
										HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.WIFI_DISCONNECTED), 2000);
									}
								}
								
								if(Constant.DEBUG)  Log.d(TAG,"isSetup: "+isSetup);
								if(isSetup == false){
									showSetUpList();
								}else{
									showSettings();
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
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.getWifiNetworks")){
				try {
					if(msetUpInstance != null){
						try {
							if(jsonData != null){
								String result = jsonData.getString("result");
								if(Constant.DEBUG)  Log.d(TAG,"result: "+result);
								if(result.equalsIgnoreCase("success")){
									if(jsonData.has("wifiList")){
										if(Constant.DEBUG)  Log.d(TAG,"wifiList");
										ArrayList<HashMap<String, String>> wifiList = new ArrayList<HashMap<String,String>>();
										JSONArray array = jsonData.getJSONArray("wifiList");
										if(array != null && array.length() >0){
											for(int i=0;i<array.length();i++){
												HashMap<String,String> wifiListmap = new HashMap<String, String>();
												JSONObject obj = array.getJSONObject(i);
												String ssid = "";
												String bssid = "";
												String capabilities = "";
												String frequency = "";
												String level = "";
												if(obj.has("BSSID")){
													bssid = obj.getString("BSSID");
												}
												if(obj.has("SSID")){
													ssid = obj.getString("SSID");
												}
												if(obj.has("capabilities")){
													capabilities = obj.getString("capabilities");
												}
												if(obj.has("frequency")){
													frequency = obj.getString("frequency");
												}
												if(obj.has("level")){
													level = obj.getString("level");
												}
												selectedWifi = setupDetails.getString("SelectWifi", "");
												if(Constant.DEBUG)  Log.d(TAG,"selectedWifi: "+selectedWifi);
												if(ssid != null && !ssid.equalsIgnoreCase("")){
													wifiListmap.put(ScreenStyles.LIST_KEY_TITLE, ssid);
													wifiListmap.put("bssid", bssid);
													wifiListmap.put("capabilities", capabilities);
													wifiListmap.put("frequency", frequency);
													wifiListmap.put("level", level);
													if(ssid.equalsIgnoreCase(selectedWifi)){
														wifiListmap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Connected");
													}else{
														wifiListmap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"");
													}
													wifiList.add(wifiListmap);
												}
												showAvailableWifiNetwork(wifiList);
											}
										}
									}
								}else if(result.equalsIgnoreCase("pending")){
									HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.WIFI_PENDING), 2000);
								}else if(result.equalsIgnoreCase("failure")){
									HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.NO_WIFI_CONNECTION), 2000);
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
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.createWifiHotspot")){
				if(jsonData != null){
					if(jsonData.has("result")){
						String result = jsonData.getString("result");
						if(result.equalsIgnoreCase("enabled")){
							//set checkbox to correct state
							setupedit.putBoolean("HotSpot", true);
							setupedit.commit();
							if(Constant.DEBUG)  Log.d(TAG,"HotSpot Active");
							HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.HOTSPOT_ACTIVE), 2000);
						}else{
							setupedit.putBoolean("HotSpot", false);
							setupedit.commit();
							if(Constant.DEBUG)  Log.d(TAG,"HotSpot Inactive");
							HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.HOTSPOT_DEACTIVE), 2000);
						}
						if(Constant.DEBUG)  Log.d(TAG,"isSetup: "+isSetup);
						if(isSetup == false){
							showSetUpList();
						}else{
							showSettings();
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.getTVOperators")){
				if(Constant.DEBUG)  Log.d(TAG,"Setup list called");
				if(jsonData != null){
					if(jsonData.has("tvOperator")){
						if(Constant.DEBUG)  Log.d(TAG,"got Setup list ");
						int previousSelectedOperator = -1;
						ArrayList<HashMap<String, String>> liveTVOperatorList = new ArrayList<HashMap<String,String>>();
						JSONArray array = jsonData.getJSONArray("tvOperator");
						if(array != null && array.length() >0){
							for(int i=0;i<array.length();i++){
								HashMap<String,String> liveTVOperatormap = new HashMap<String, String>();
								JSONObject obj = array.getJSONObject(i);
								String title = "";
								String id = "";
								if(obj.has("name")){
									title = obj.getString("name");
								}

								if(obj.has("id")){
									id = obj.getString("id");
								}
								Operator = DataStorage.getConnectedVendor();
								if(Constant.DEBUG)  Log.d(TAG,"Operator: "+Operator);
								if(title != null && !title.equalsIgnoreCase("")){
									liveTVOperatormap.put(ScreenStyles.LIST_KEY_TITLE, title);
									liveTVOperatormap.put("id", id);
									if(Utils.checkNullAndEmpty(title) && Operator!=null && title.equalsIgnoreCase(Operator)){
										previousSelectedOperator = i;
										liveTVOperatormap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Connected");
									}else{
										liveTVOperatormap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"");
									}
									liveTVOperatorList.add(liveTVOperatormap);
								}
							}
						}else{
							if(Constant.DEBUG)  Log.d(TAG,"Oops! No live connection. ");
						}

						if(liveTVOperatorList != null && liveTVOperatorList.size() >0){
							showliveTVOperatorList(liveTVOperatorList,previousSelectedOperator);
						}else{
							HelpText.showHelpTextDialog(msetUpInstance,  msetUpInstance.getResources().getString(R.string.NO_LIVE_TV_OPERATOR), 2000);
							if(!isSetup){
								try {
									selectedliveTVOperator = "";
									showSetUpList();
								}catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}else{
								try{
									HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.NO_LIVE_TV_CONNECTION), 2000);
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
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.setTVOperator")){
				if(jsonData != null){
					if(jsonData.has("result")){
						String result = jsonData.getString("result");
						if(result.equalsIgnoreCase("success")){
							String tvOperator = jsonData.getString("tvOperator");
							DataStorage.setConnectedVendor(tvOperator);
							HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.SET_TV_OPERATOR), 2000);
						}
					}
				}//Added for PPPoE on 15 July 2015 @Tomesh
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.setpppoe")){
				if(jsonData != null){if(Constant.DEBUG)  Log.d(TAG,"com.port.apps.settings.Settings.setpppoe");
					if(jsonData.has("result")){
						msetUpInstance.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String result;
								try {
									result = jsonData.getString("result");
									if(Constant.DEBUG)  Log.d(TAG,result);
									String connected = jsonData.getString("pppoestatus");if(Constant.DEBUG)  Log.d(TAG,connected);
									if(result.equalsIgnoreCase("success")){
										if(connected.equalsIgnoreCase("disconnected")){Log.e(TAG, " disconnected ");
											isPPPoEConnected =false;
											
											setupedit.putString("PPPoEusername", "");
											setupedit.putString("PPPoEpassword", "");
											setupedit.commit();

											HelpText.showHelpTextDialog(msetUpInstance, "PPPoE is disconnected" , 4000);
											
										}	
										else if(connected.equalsIgnoreCase("connected")){	if(Constant.DEBUG)  Log.d(TAG,connected);
											isPPPoEConnected =true;Log.e(TAG, "Login Complete");
											
											setupedit.putString("PPPoEusername", mUsername.getText().toString());
											setupedit.putString("PPPoEpassword", mPassword.getText().toString());
											setupedit.commit();
											
											HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.PPPoE), 4000);
										}
										else if(connected.equalsIgnoreCase("WrongPassword")){	if(Constant.DEBUG)  Log.d(TAG,connected);
											isPPPoEConnected =false;Log.e(TAG, "Wrong Password or UserName");
											HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.PPPoEWrong), 4000);
//											SetPppoe();
											}
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
					}
//					else if(jsonData.has("result")){
//							msetUpInstance.runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//								String result;
//								try {
//									result = jsonData.getString("result");
//									if(result.equalsIgnoreCase("failure")){Log.e(TAG, "Login Fail");
//									HelpText.showHelpTextDialog(msetUpInstance, msetUpInstance.getResources().getString(R.string.PPPoEFailure), 2000);
//								}
//								
//								} catch (JSONException e) {
//									e.printStackTrace();}
//							}
//						});
//					}
//					if(isPPPoEConnected){
//						if(Constant.DEBUG)  Log.d(TAG,"isSetup: "+isSetup);
						if(isSetup == false){
							showSetUpList();
						}else{
							showSettings();
						}
//					}
					
				}
				
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.getSubscriberID")){
				if(Constant.DEBUG)  Log.d(TAG,"getSubscriberID.jsonData  -  "+jsonData);
				if(msetUpInstance != null){
					try {
						if(jsonData.has("result")){
							String result = jsonData.getString("result");
							if(Constant.DEBUG)  Log.d(TAG,"result: "+result);
								
							if(result.equalsIgnoreCase("registered")){
								isSetup = true;	
								DataStorage.SetupDone(isSetup);
								msetUpInstance.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {
											String distributorId = "";
											String distributorPwd= "";
											if(jsonData.has("subscriberid")){
												SubscriberId = jsonData.getString("subscriberid");
												Layout.mDataAccess.updateSetupDB("SubscriberID", SubscriberId);
												Layout.mDataAccess.updateSetupDB("IsSetup", "true");
												Layout.mDataAccess.updateSetupDB("ConnectedVendor", jsonData.getString("Model"));
												sendToParse(SubscriberId,jsonData.getString("Model"));
											}
											if(jsonData.has("distributorId")){
												 distributorId = jsonData.getString("distributorId");
												 setupedit.putString("distributorid", distributorId);
												 setupedit.commit();
											}
											if(jsonData.has("distributorPwd")){
												 distributorPwd =jsonData.getString("distributorPwd");
											}
											//Added by @Tomesh For Auto Download of Data after Setup
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> listF = new HashMap<String, String>();
											listF.put("consumer", "TV");
											listF.put("network", mDataAccess.getConnectionType());
											listF.put("caller", "com.player.apps.Setup");
											listF.put("called", "startService");
											listF.put("Title", "featured");
											listF.put("subscriberid", SubscriberId);
											listF.put("distributorId",distributorId);
											listF.put("distributorPwd",distributorPwd);
											dispatchHashMap.add(listF);
											String	method = "com.port.apps.epg.Catalogue.getFeatured";
											new AsyncDispatch(method, dispatchHashMap,false).execute();
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
								
								showSettings(); //show settings screen if set up is done
								
								
							}else if(result.equalsIgnoreCase("created")){
								if(jsonData.has("subscriberid")){
									msetUpInstance.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											try {
												String distributorId = "";
												String distributorPwd= "";
												if(jsonData.has("subscriberid")){
													SubscriberId = jsonData.getString("subscriberid");
													Layout.mDataAccess.updateSetupDB("SubscriberID", SubscriberId);
													Layout.mDataAccess.updateSetupDB("IsSetup", "true");
													Layout.mDataAccess.updateSetupDB("ConnectedVendor", jsonData.getString("Model"));
													sendToParse(SubscriberId,jsonData.getString("Model"));
												}
												if(jsonData.has("distributorId")){
													 distributorId = jsonData.getString("distributorId");
													 setupedit.putString("distributorid", distributorId);
													 setupedit.commit();
												}
												if(jsonData.has("distributorPwd")){
													 distributorPwd =jsonData.getString("distributorPwd");
												}
												//Added by @Tomesh For Auto Download of Data after Setup
												dispatchHashMap  = new ArrayList<HashMap<String,String>>();
												HashMap<String, String> listF = new HashMap<String, String>();
												listF.put("consumer", "TV");
												listF.put("network", mDataAccess.getConnectionType());
												listF.put("caller", "com.player.apps.Setup");
												listF.put("called", "startService");
												listF.put("Title", "featured");
												listF.put("subscriberid", SubscriberId);
												listF.put("distributorId",distributorId);
												listF.put("distributorPwd",distributorPwd);
												dispatchHashMap.add(listF);
												String	method = "com.port.apps.epg.Catalogue.getFeatured";
												new AsyncDispatch(method, dispatchHashMap,false).execute();
												
											} catch (JSONException e) {
												e.printStackTrace();
											}
											String message = msetUpInstance.getResources().getString(R.string.PLEASE_REGISTER_SUBSCRIBERID)+SubscriberId+" in www.lukup.com";
											LayoutInflater inflater = msetUpInstance.getLayoutInflater();
											final Dialog dialog = new Dialog(msetUpInstance,R.style.ThemeDialogCustom);
											View messageDialog = inflater.inflate(R.layout.overlay_message, null);
											TextView messageView = (TextView) messageDialog.findViewById(R.id.messageView);
											Button okBtn = (Button) messageDialog.findViewById(R.id.messageOkbtn);
											if(okBtn != null){
												okBtn.setVisibility(View.VISIBLE);
												okBtn.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View arg0) {
														if(dialog != null && dialog.isShowing()){
															DataStorage.SetupDone(true);
															HelpTip.close(msetUpInstance);
															if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
															Intent p = new Intent(Setup.this, com.player.apps.Profile.class);
															p.putExtra("ActivityName", "Setup");
															startActivity(p);
															finish();
															dialog.cancel();
														}
													}
												});
											}
											messageView.setText(message);
											dialog.setContentView(messageDialog);
											dialog.show();
										}
									});
								}
								
							}else if(result.equalsIgnoreCase("failure")){
								Layout.mDataAccess.updateSetupDB("IsSetup", "false");
								String msg = jsonData.getString("msg");
								if(Constant.DEBUG)  Log.d(TAG ,"Failed creating subscriber ID : "+ msg);
								showSetUpList();
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.getDeviceInfo")){
				try {
					if(msetUpInstance != null){
						if(Constant.DEBUG)  Log.d(TAG," ActivityName: "+ActivityName);
						aboutJsonData = jsonData;
						DataStorage.setAboutInfo(aboutJsonData);
						DataStorage.setDeviceType(Utils.getDataFromJSON(jsonData, "Model"));
						SubscriberId = Utils.getDataFromJSON(jsonData, "subscriberid");
						String operator  = Utils.getDataFromJSON(jsonData, "operatorname");
						String ip = Utils.getDataFromJSON(jsonData, "IP");
						String mac = Utils.getDataFromJSON(jsonData, "MAC Address");
						operator = operator.replaceAll("\\s+","");
						DataStorage.setMacAddress(mac);
						DataStorage.setIPAddress(ip);
						DataStorage.setConnectedVendor(operator);
						DataStorage.setSubscriberId(SubscriberId);
						if (SubscriberId==null || SubscriberId.equalsIgnoreCase("")){
							if(Constant.DEBUG)  Log.d(TAG, "Setup not done "+isSetup);
							showSetUpList(); //show set up screen if set up is not done
						} else if ((SubscriberId!=null && !SubscriberId.equalsIgnoreCase("")) && (ActivityName!=null && !ActivityName.equalsIgnoreCase(""))){
							if(Constant.DEBUG)  Log.d(TAG, "Setup done "+isSetup + " " + ActivityName);
							isSetup = true;	
							DataStorage.SetupDone(isSetup);
							sendToParse(SubscriberId,Utils.getDataFromJSON(jsonData, "Model"));
							Layout.mDataAccess.updateSetupDB("ConnectedVendor", jsonData.getString("Model"));
							showSettings(); //show settings screen if set up is done									
						} else if ((SubscriberId!=null && !SubscriberId.equalsIgnoreCase(""))){			
							if(Constant.DEBUG)  Log.d(TAG, "Going to home "+isSetup);
							isSetup = true;	
							DataStorage.SetupDone(isSetup);
							sendToParse(SubscriberId,Utils.getDataFromJSON(jsonData, "Model"));
							Layout.mDataAccess.updateSetupDB("ConnectedVendor", jsonData.getString("Model"));
							//Intent guide = new Intent(Setup.this, Home.class);
							Intent guide = new Intent(Setup.this, AppGuide.class);
							guide.putExtra("GValue", "setup");
							guide.putExtra("ActivityName", "Settings");
							startActivity(guide);
							finish();	
							
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.getAccountInfo")){
				try {
					if(msetUpInstance != null){
						final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
						HashMap<String,String> map = new HashMap<String, String>();
						map.put(ScreenStyles.LIST_KEY_TITLE,"Subscriber ID");
						String accoundInfo = Utils.getDataFromJSON(jsonData, "Subscriber ID");
						if(Constant.DEBUG)  Log.d(TAG,"accoundInfo "+accoundInfo);
						if(accoundInfo != null && !accoundInfo.equalsIgnoreCase("")){
							map.put(ScreenStyles.LIST_KEY_DESCRIPTION,accoundInfo);
							list.add(map);
						}
						
						map = new HashMap<String, String>();
						map.put(ScreenStyles.LIST_KEY_TITLE,"Balance");
						accoundInfo = Utils.getDataFromJSON(jsonData, "balance");
						if(Constant.DEBUG)  Log.d(TAG,"accoundInfo "+accoundInfo);
						if(accoundInfo != null && !accoundInfo.equalsIgnoreCase("")){
							map.put(ScreenStyles.LIST_KEY_DESCRIPTION,accoundInfo);
							list.add(map);
						}
						msetUpInstance.runOnUiThread(new Runnable() {
							@Override
							public void run() {	
								if(list != null){
									showAccountInfoPage(list);
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
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.getNetworkType")){
				try{
					if(msetUpInstance != null){
						msetUpInstance.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								networkType = Utils.getDataFromJSON(jsonData, "type");
								if(Constant.DEBUG)  Log.d(TAG,"networkType "+networkType);
								setUpNetworktomediaplayer();
							}
						});
					}
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.settings.Settings.doUnBind")){
				if(jsonData != null){
					if(jsonData.has("result")){
						String result = jsonData.getString("result");
						if(result.equalsIgnoreCase("success")){
							//Player.unbindBTDevice(Player.btAddress);
							mDataAccess.updateSetupDB("BTAddress", "");
							mBTDeviceBondList.clear();
							mBTDeviceName.clear();
							mConnectionAdapterBTBondList.notifyDataSetChanged();
							
						}else{
							Toast.makeText(msetUpInstance, getResources().getString(R.string.PAIRING_FAIL), Toast.LENGTH_LONG).show();
						}
						if(Constant.DEBUG)  Log.d(TAG,"isSetup: "+isSetup);
						if(isSetup == false){
							showSetUpList();
						}else{
							showSettings();
						}
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
	
	
	private void sendToParse(final String SubscriberId, final String deviceModel){
//		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
//		query.whereEqualTo("subscriber", SubscriberId);
//		query.findInBackground(new FindCallback<ParseInstallation>() {
//			@Override
//			public void done(List<ParseInstallation> arg0, ParseException arg1) {
//				// TODO Auto-generated method stub
//				if(arg0!=null && !arg0.isEmpty()){
//					if(Constant.DEBUG) Log.d(TAG, "Already saved " + arg0.toString());
//				}else{
//					if(Constant.DEBUG) Log.d(TAG, "Saving now, subscriberid : " + SubscriberId + " model : " + deviceModel);
//					Player.installation.put("subscriberid", SubscriberId);
//					Player.installation.put("channel",deviceModel.replaceAll("\\s", ""));
//					Player.installation.put("environment",Constant.parse_url);
//					Player.installation.saveEventually();
//				}
//			}
//		});
//		if(Constant.DEBUG) Log.d(TAG, "In send to Parse");
//		ParseQuery<Push> query = ParseQuery.getQuery(Push.class);
//		query.whereEqualTo("subscriber", SubscriberId);
//		query.findInBackground(new FindCallback<Push>() {
//			@Override
//			public void done(List<Push> arg0, ParseException arg1) {
//				// TODO Auto-generated method stub
//				if(arg0!=null && !arg0.isEmpty()){
//					if(Constant.DEBUG) Log.d(TAG, "Already saved " + arg0.toString());
//				}else{
//					if(Constant.DEBUG) Log.d(TAG, "Saving now, subscriberid : " + SubscriberId + " model : " + deviceModel);
//					Push t = new Push();
//					t.setUser(SubscriberId);
//					t.setDevice(deviceModel.replaceAll("\\s", ""));
//					t.setEnvironment(Constant.parse_url);
//					t.setCompleted(false);
//					t.saveEventually();
//				}
//			}
//		});
	}

	/**
	 * Back button action.
	 *
	 * @param keyCode the key code
	 * @param event the event
	 * @return true, if successful
	 */
	private boolean backButtonAction(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG,"onBackPressed()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
		if(Constant.DEBUG)  Log.d(TAG ,"backButtonAction().CurrentScreen: "+DataStorage.getCurrentScreen());
		HelpTip.close(msetUpInstance);
		
		if(Layout.mDataAccess.getIsSetup().equalsIgnoreCase("true")|| !Layout.mDataAccess.getSubscriberID().equalsIgnoreCase("")){
			isSetup = true;
		}else{
			isSetup = false;
		}
		
		if(isSetup){
			if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INSIDE_SETTINGS))) {
				mContainer.removeAllViews();
				showSettings();
				return true;
			}
			if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_OPERATOR_LIST))) {
				mContainer.removeAllViews();
				showSettings();
				return true;
			}
			if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_NETWORKSCREEN))) {
				mContainer.removeAllViews();
				showSettings();
				return true;
			}
			if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase("ConnectList"))) {
				mContainer.removeAllViews();
				Setupconnection();
				return true;//ConnectionSetup
			}
			
			if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase("ConnectionSetup"))) {
				mContainer.removeAllViews();
				showSettings();
				return true;
			}
			
			if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_BROADBAND) || DataStorage.getCurrentScreen().equalsIgnoreCase(
					ScreenStyles.SETUP_WIFI_LIST) || DataStorage.getCurrentScreen().equalsIgnoreCase("setupwifi"))) {
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Setup");
				list.put("called", "startService");
				dispatchHashMap.add(list);
				String method = "com.port.apps.settings.Settings.getNetworkType"; 
				new AsyncDispatch(method, dispatchHashMap,true).execute();
				return true;
			}
			if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_BROADBAND_MANUALSCREEN)) {
				showBroadbandAction("Broadband");
				return true;
			}
			if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase("setupwifi")) {
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Setup");
				list.put("called", "startService");
				dispatchHashMap.add(list);
				String method = "com.port.apps.settings.Settings.getWifiNetworks"; 
				new AsyncDispatch(method, dispatchHashMap,true).execute();
				return true;
			}
			if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.LUKUPOPTIONSELECTED)) {
				if(ActivityName != null && (ActivityName.equalsIgnoreCase("AppGuide") || ActivityName.equalsIgnoreCase("Settings")  || ActivityName.equalsIgnoreCase("Profile"))){
					if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
					Intent lukup = new Intent(Setup.this, com.player.apps.AppGuide.class);
					lukup.putExtra("ActivityName", "Setup");
					startActivity(lukup);
					finish();
				}
				return true;
			}
		}else if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			if(Constant.DEBUG)  Log.d(TAG,"Current Screen Id :" + DataStorage.getCurrentScreen());
				if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_BROADBAND) || DataStorage.getCurrentScreen().equalsIgnoreCase(
								ScreenStyles.SETUP_WIFI_LIST) || DataStorage.getCurrentScreen().equalsIgnoreCase("setupwifi"))) {
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Setup");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String method = "com.port.apps.settings.Settings.getNetworkType"; 
					new AsyncDispatch(method, dispatchHashMap,true).execute();
					return true;
				}
				if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_NETWORKSCREEN))) {
					if(!isSetup){
						showSetUpList();
					}else{
						showSettings();
					}
					return true;
				}
				if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_BROADBAND_MANUALSCREEN)) {
					showBroadbandAction("Broadband");
					return true;
				}
				
			if (DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SETUP_OPERATOR_LIST)) {
				if(!isSetup){
					showSetUpList();
				}else{
					showSettings();
				}
				return true;
			}
					
		}
		if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase("ConnectList"))) {
			mContainer.removeAllViews();
			if(Constant.DEBUG)  Log.d(TAG ,"Back Connect List CurrentScreen: "+DataStorage.getCurrentScreen());
			Setupconnection();
			return true;//ConnectionSetup 
		}
		if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase("WIFILIST"))) {
			mContainer.removeAllViews();
			if(Constant.DEBUG)  Log.d(TAG ,"Back Connect List CurrentScreen: "+DataStorage.getCurrentScreen());
			Setupconnection();
			return true;
		}
		
		if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase("BluetoothList"))) {
			if(progressDialog.isShowing()){
				progressDialog.cancel();
			}
			mContainer.removeAllViews();
			if(Constant.DEBUG)  Log.d(TAG ,"Back Connecttion Setup CurrentScreen: "+DataStorage.getCurrentScreen());
			ConnectBTWIFI(ConnectionType);
			return true;
		}//
		
		if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase("ConnectionSetup"))) {
			mContainer.removeAllViews();
			if(Constant.DEBUG)  Log.d(TAG ,"Back Connecttion Setup CurrentScreen: "+DataStorage.getCurrentScreen());
			if(!isSetup){
				showSetUpList();
			}else{
				showSettings();
			}
			return true;
		}//BluetoothList
		if (DataStorage.getCurrentScreen() != null&& (DataStorage.getCurrentScreen().equalsIgnoreCase("setup"))) {
			mContainer.removeAllViews();
			if(Constant.DEBUG)  Log.d(TAG ,"Back Connecttion Setup CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent lukup = new Intent(Setup.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "Setup");
			startActivity(lukup);
			finish();
			return true;
		}
		
		
		return false;
	}	

}
