package com.player.apps;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.player.Layout;
import com.player.Player;
import com.google.android.exoplayer.VideoSurfaceView;
import com.player.R;
import com.player.Layout.AsyncDispatch;
import com.player.action.AuthDialog;
import com.player.action.DlnaRenderer;
import com.player.action.ExoPlay;
import com.player.action.HelpTip;
import com.player.action.Media;
import com.player.action.Play;
import com.player.action.PlayOn;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.FileSystem;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.webservices.Cloudstorage;
import com.player.widget.CustomListView;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
import com.player.widget.ListViewAdapter;
import com.player.widget.ScrollList;

public class Storage extends Layout implements OnItemClickListener,OnItemLongClickListener,OnScrollListener{

	private Storage mUsbInstance;
	
	// for List view click action
	private String filePath;
	private ArrayList<HashMap<String,String>> recordList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> dispatchHashMap = null;
	private String TAG = "Storage";
	
	private int maingrid_select_index = -1;
	private int maingrid_focus_index = -1;
	private View maingridview_focus = null;
	RelativeLayout currentlayoutMain;
	TextView currentTextView;
	private int currentPostion = -1;
	private static String selectedType = "none";
	private String selectedFilePath;
	private String extention ="";
	boolean status = false;
	String USB_BASE_PATH ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUsbInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,"Pre Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
		
		IntentFilter storage = new IntentFilter("com.player.apps.Storage");
		registerReceiver(mStorageReceiver,storage);
		
		if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			showStorageScreen();
		}else{
//			requestForUSB();
			if(CommonUtil.isNetworkConnected(mActivity)){
				showStorageScreen();
				if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
					AuthDialog.authDialog(mUsbInstance);
				}
			}else{
				HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(Constant.DEBUG)  Log.d(TAG ,"Pre Activity.CurrentScreen: "+DataStorage.getCurrentScreen()+", list: "+recordList.size());
		if (recordList.size()>0) {
			showRecording(recordList);
		}
	}
	
	private void requestForUSB() {
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Storage");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			String method = "com.port.apps.storage.Storage.sendUSBStatus"; 
			new AsyncDispatch(method, dispatchHashMap,true).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showStorageScreen(){	
		try {
			if(mUsbInstance != null ){
				//HelpTip
				HelpTip.requestForHelp(mUsbInstance.getResources().getString(R.string.ACCESS_STORED_CONTENT),
						mUsbInstance.getResources().getString(R.string.STORAGE_MSG1),mUsbInstance);
				
				DataStorage.setCurrentScreen("storage");
				selectedType = "none";
				
				mUsbInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							mContainer.removeAllViews();
						}
						ListView list = new ListView(mUsbInstance);
						final ArrayList<HashMap<String,String>> storageList = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> storageHashMap;
						
						if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
							if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player X1")){
								if(Constant.DEBUG)  Log.d(TAG ,"Checking X1 storage ");
								for(int i=0;i<ScreenStyles.CLOUD_ITEM_LIST.length;i++){
									storageHashMap = new HashMap<String, String>();
									storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[i]);
									//23 feb 2015
									if (status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
										storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Connected");
									} else if (!status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
										storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Disconnected");
									}else{
										storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"");
									}
									storageList.add(storageHashMap);
								}
							}else{
								if(Constant.DEBUG)  Log.d(TAG ,"Checking X storage ");
								for(int i=0;i<ScreenStyles.CLOUD_ITEM_LIST.length-1;i++){
									storageHashMap = new HashMap<String, String>();
									storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[i]);
									//23 feb 2015
									if (status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
										storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Connected");
									} else if (!status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
										storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Disconnected");
									}else{
										storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"");
									}
									storageList.add(storageHashMap);
								}
							}
							
							list.setCacheColorHint(Color.TRANSPARENT);
							ListViewAdapter adapter = new ListViewAdapter(mUsbInstance, storageList,0,"storage", null,-1);
							list.setAdapter(adapter);
						
							if(mContainer != null && list != null){
								mContainer.setOrientation(LinearLayout.VERTICAL);
								mContainer.addView(list);
								list.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
										HashMap<String, String> map= storageList.get(arg2);
										if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
											String val = map.get(ScreenStyles.LIST_KEY_TITLE);
											if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[0])){
												dispatchHashMap  = new ArrayList<HashMap<String,String>>();
												HashMap<String, String> list = new HashMap<String, String>();
												list.put("consumer", "TV");
												list.put("network",mDataAccess.getConnectionType());
												list.put("userid",mDataAccess.getCurrentUserId());
												list.put("caller", "com.player.apps.Storage");
												list.put("called", "startService");
												dispatchHashMap.add(list);
												String method = "com.port.apps.epg.Plan.showRecording";
												new AsyncDispatch(method, dispatchHashMap,true).execute();
											}
											if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[1])){
												if(DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player X1")){
													USB_BASE_PATH="/mnt/sda/";
												}else{
													USB_BASE_PATH="/mnt/sdcard/";
												}
												requestforUSBAction(ScreenStyles.CLOUD_ITEM_LIST[1]);											
											}
											if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[2])){
												USB_BASE_PATH="/mnt/mmcblk1/mmcblk1p1/";
												requestforUSBAction(ScreenStyles.CLOUD_ITEM_LIST[2]);											
											}
										}
									}
								});
							}
						}else{ //if app is not connected to the Player
							storageHashMap = new HashMap<String, String>();
							storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[0]);
							storageList.add(storageHashMap);
							storageHashMap = new HashMap<String, String>();
							storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[2]);
							storageList.add(storageHashMap);
							list.setCacheColorHint(Color.TRANSPARENT);
							ListViewAdapter adapter = new ListViewAdapter(mUsbInstance, storageList,0,"storage", null,-1);
							list.setAdapter(adapter);
						
							if(mContainer != null && list != null){
								mContainer.setOrientation(LinearLayout.VERTICAL);
								mContainer.addView(list);
								list.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
										HashMap<String, String> map= storageList.get(arg2);
										if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
											String val = map.get(ScreenStyles.LIST_KEY_TITLE);
											if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[0])){
												Layout.progressDialog = new ProgressDialog(Storage.this,R.style.MyTheme);
												Layout.progressDialog.setCancelable(true);
												Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
												Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
												Layout.progressDialog.show();
												
												Intent intent = new Intent(Storage.this , Cloudstorage.class);
												intent.putExtra("handler", "com.port.apps.epg.Plan.showRecording");
												startService(intent);
											}
											if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[2])){
												//to do
												retrieve(new File(Environment.getExternalStorageDirectory().getPath()));
											}
										}
									}
								});
							}
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		
	private void requestforUSBAction(String type) {
		try {
			selectedType = "usb";
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Storage");
			list.put("called", "startService");
			list.put("fstype", type);	//Added by @Tomesh for Local Storage on 18 AUG 2015
			dispatchHashMap.add(list);
			String method = "com.port.apps.storage.Storage.fileBrowser"; 
			new AsyncDispatch(method, dispatchHashMap,true).execute();
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	/**
	 * @param jsonData
	 */
	private void showFileBrowser(final JSONObject jsonData) {
		try {
			String result = Utils.getDataFromJSON(jsonData, "result");
			if(Utils.checkNullAndEmpty(result)){
				if(result.equalsIgnoreCase("success")){
					if(mUsbInstance != null){
						try {
							//HelpTip
							HelpTip.requestForHelp(mUsbInstance.getResources().getString(R.string.BROWSE_CONTENT_ON_USB),
									mUsbInstance.getResources().getString(R.string.STORAGE_MSG3),mUsbInstance);
							
							filePath = Utils.getDataFromJSON(jsonData, "filepath");
							if(Constant.DEBUG)  Log.d(TAG, "File Path "+filePath);
							final ArrayList<HashMap<String,String>> fileList = new ArrayList<HashMap<String,String>>();
							JSONArray mfolder_list = Utils.getListFromJSON(jsonData, "folderList");
							if(mfolder_list != null && mfolder_list.length() >0){
								for(int i=0;i<mfolder_list.length();i++){
									try {
										HashMap<String,String> folderMap = new HashMap<String, String>();
										JSONObject obj = mfolder_list.getJSONObject(i);
										String mName = null;
										String mId = null;
										String mPath = null;
										if(obj.has("name")){
											mName= obj.getString("name");
										}

										if(obj.has("id")){
											mId= obj.getString("id");
										}
										
										if(obj.has("path")){
											mPath= obj.getString("path");
										}
										folderMap.put(ScreenStyles.LIST_KEY_ID, mId);
										folderMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
										folderMap.put("path", mPath);
										fileList.add(folderMap);
									}catch (Exception e) {
										e.printStackTrace();
										StringWriter errors = new StringWriter();
										e.printStackTrace(new PrintWriter(errors));
										SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										continue;
									}
								}
							}
							JSONArray mfile_list = Utils.getListFromJSON(jsonData, "fileList");
							if(mfile_list != null && mfile_list.length() >0){
								for(int i=0;i<mfile_list.length();i++){
									try {
										HashMap<String,String> folderMap = new HashMap<String, String>();
										JSONObject obj = mfile_list.getJSONObject(i);
										String mName = null;
										String mId = null;
										String mPath = null;
										if(obj.has("name")){
											mName= obj.getString("name");
										}
										if(obj.has("id")){
											mId= obj.getString("id");
										}
										if(obj.has("path")){
											mPath= obj.getString("path");
										}
										folderMap.put(ScreenStyles.LIST_KEY_ID, mId);
										folderMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
										folderMap.put("path", mPath);
										fileList.add(folderMap);
									}catch (Exception e) {
										e.printStackTrace();
										StringWriter errors = new StringWriter();
										e.printStackTrace(new PrintWriter(errors));
										SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										continue;
									}
								}
							}
							mUsbInstance.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if(fileList != null && fileList.size() > 0){
										DataStorage.setCurrentScreen(ScreenStyles.LUKUPOPTIONSELECTED);
										showFileBrowserInList(jsonData,fileList,filePath);
									}else{
										HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_CONTENT), 3000);
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
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	private void showFileBrowserInList(JSONObject jsonData,final ArrayList<HashMap<String, String>> deviceList,final String filepath) {
		try {
			if(mUsbInstance != null){
				mUsbInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(deviceList != null && deviceList.size() > 0){
							DataStorage.setCurrentScreen(ScreenStyles.INSIDE_FILE_BROWSER);
							CustomListView view = new CustomListView(mUsbInstance);
							view.setCacheColorHint(Color.TRANSPARENT);
							final LinearLayout listlayout = new LinearLayout(mUsbInstance);
							final ListViewAdapter listViewAdapter = new ListViewAdapter(mUsbInstance, deviceList, 0,filepath, null,-1);
							view.setAdapter(listViewAdapter);
							view.setVerticalScrollBarEnabled(true);
			
							view.setLongClickable(true);
							view.setOnItemLongClickListener(new OnItemLongClickListener() {
								@Override
								public boolean onItemLongClick(AdapterView<?> parent, View v, int position,long id) {
									hideMenu();
									if (maingrid_select_index != position){      
										if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
										{
											Log.w(TAG, "setOnItemLongClickListener().position is ::"+position);
											currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
											currentTextView.setTextColor(getResources().getColor(R.color.white));
										}
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
										currentTextView.setTextColor(getResources().getColor(R.color.pink));
									}else if(maingridview_focus != null){
										Log.w(TAG, "setOnItemLongClickListener().position is ::"+position);
										currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
										currentTextView.setTextColor(getResources().getColor(R.color.white));
										maingridview_focus = v;
										currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
										currentTextView = (TextView) v.findViewById(R.id.title);
									}
									maingrid_focus_index = position;
									currentPostion = position;
									setLongClickAction(position);
									return true;
								}
			
								private void setLongClickAction(int position) {
									if(Constant.DEBUG)  Log.d(TAG ,"Long click");
									HashMap<String, String> map = deviceList.get(position);
//									if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
//										selectItemId = map.get(ScreenStyles.LIST_KEY_TITLE);
//									}
									if(map.containsKey("eventid")){
										selectItemId = map.get("eventid");
									}
									if(map.containsKey("path")){
										objectId = map.get("path");
									}
									showCustomMenu();
								}
							});
			
							view.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
									hideMenu();
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
									setListItemClickAction(position);
								}
								/**
								 * @param position
								 */
								private void setListItemClickAction(int position) {
									HashMap<String, String> map = deviceList.get(position);
									String mSelectedFileName = "";
									if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
										mSelectedFileName = map.get(ScreenStyles.LIST_KEY_TITLE);
									}
									selectedFilePath = filepath+mSelectedFileName;
									String action = "forward";
									if(Constant.DEBUG)  Log.d(TAG ,"File path " + filepath + " File Name " + mSelectedFileName + " Full path " + selectedFilePath);
									if(selectedFilePath.indexOf(".") != -1){
										extention = selectedFilePath.trim().substring(selectedFilePath.trim().lastIndexOf("."));
										if(!extention.trim().equalsIgnoreCase("") && extention.trim() != null){
											if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
												if(extention.trim().equalsIgnoreCase(".mp4") || extention.trim().equalsIgnoreCase(".mpg") || extention.trim().equalsIgnoreCase(".mpeg")
													|| extention.trim().equalsIgnoreCase(".mov") || extention.trim().equalsIgnoreCase(".m4v") || extention.trim().equalsIgnoreCase(".mkv")
													 || extention.trim().equalsIgnoreCase(".avi")){
													Intent play = new Intent(Storage.this, PlayBack.class);
													play.putExtra("ActivityName", "Storage");
													play.putExtra("SubType", "recorded");
													play.putExtra("EventId", "");
													play.putExtra("EventUrl", selectedFilePath);
													startActivity(play);
												}else if(extention.equalsIgnoreCase(".mp3")  || extention.trim().equalsIgnoreCase(".wav")  || extention.trim().equalsIgnoreCase(".wma")){
				//										PlayOn.getConnectedDevices(selectedFilePath,"audio","com.player.apps.Storage");
													Intent play = new Intent(Storage.this, PlayBack.class);
													play.putExtra("ActivityName", "Storage");
													play.putExtra("SubType", "music");
													play.putExtra("EventId", "");
													play.putExtra("EventUrl", selectedFilePath);
													startActivity(play);
												}else if(extention.equalsIgnoreCase(".jpeg") || extention.equalsIgnoreCase(".jpg") || extention.equalsIgnoreCase(".png")){
													dispatchHashMap  = new ArrayList<HashMap<String,String>>();
													HashMap<String, String> list = new HashMap<String, String>();
	//												list.put("type", type);
													list.put("subtype", "image");
													list.put("id", "");
													list.put("url", selectedFilePath);
													list.put("consumer", "TV");
													list.put("network",mDataAccess.getConnectionType());
													list.put("caller", "com.player.apps.Storage");
													list.put("called", "startActivity");
													dispatchHashMap.add(list);
													String method = "com.port.apps.epg.Play.PlayOn"; 
													new AsyncDispatch(method, dispatchHashMap,true).execute();
												}
											}else{
												Media.playOnClient(selectedFilePath, mContainer, mActivity,"recorded","","","");
											}
										}
									}else{
										if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.Storage");
											list.put("called", "startService");
											list.put("filename", mSelectedFileName);
											list.put("filepath", selectedFilePath);
											list.put("action", action);
											dispatchHashMap.add(list);
											String method = "com.port.apps.storage.Storage.processFolders"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();
										}else{
											//to do
											processFolders(mSelectedFileName, selectedFilePath, action);
										}
									}
								}
							});
							if(mContainer != null){
								mContainer.removeAllViews();
								mContainer.setOrientation(LinearLayout.VERTICAL);
								LinearLayout.LayoutParams listParams = customLayout.getLinearLayoutParams(ScreenStyles.LIST_TAB_SCREEN_WIDTH, ScreenStyles.LIST_TAB_SCREEN_HEIGHT);
								listParams = customLayout.getLinearLayoutParams(ScreenStyles.CONTAINER_SCREEN_WIDTH,ScreenStyles.CONTAINER_SCREEN_HEIGHT);
								listlayout.setLayoutParams(listParams);
								listlayout.addView(view,new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
								mContainer.addView(listlayout);
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

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mStorageReceiver != null){
			mUsbInstance.unregisterReceiver(mStorageReceiver);
		}
	}
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			return backButtonAction(keyCode, event);
		}
		
		if (keyCode == KeyEvent.KEYCODE_MENU ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			if (DataStorage.getCurrentScreen() != null && !DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN)) {
				showCustomMenu();
			}
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				HelpTip.close(mUsbInstance);
				Intent lukup = new Intent(Storage.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Storage");
				startActivity(lukup);
				unmountUSB();
				finish();
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private boolean backButtonAction(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG,"backButtonAction()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
		if(Constant.DEBUG)  Log.d(TAG ,"backButtonAction().CurrentScreen: "+DataStorage.getCurrentScreen());
		if(mMenu.isShowing()){
			hideMenu();
			return true;
		}
		HelpTip.close(mUsbInstance);
		if (DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase("storage")) {
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				unmountUSB();
			}
			mContainer.removeAllViews();
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent lukup = new Intent(Storage.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "Storage");
			startActivity(lukup);
			finish();
			return true;
		}else if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INSIDE_FILE_BROWSER)) {
			if(Utils.checkNullAndEmpty(filePath) && !filePath.equalsIgnoreCase(USB_BASE_PATH)){
				String action = "back";
				String path = filePath.substring(0, filePath.lastIndexOf("/"));
				if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Storage");
					list.put("called", "startService");
					list.put("filename", "");
					list.put("filepath", path);
					list.put("action", action);
					dispatchHashMap.add(list);
					String method = "com.port.apps.storage.Storage.processFolders"; 
					new AsyncDispatch(method, dispatchHashMap,true).execute();
				}else{
					processFolders("",path,action);
				}
			}else{
				if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
					unmountUSB();
					mContainer.removeAllViews();
					requestForUSB();
				}else{
					mContainer.removeAllViews();
					showStorageScreen();
				}
			}
			return true;
		}else if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN) 
				&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.LUKUPOPTIONSELECTED)) {
			mContainer.removeAllViews();
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				unmountUSB();
			}
			if(mContainer != null){
				mContainer.removeAllViews();
			}
			ListView list = new ListView(mUsbInstance);
			final ArrayList<HashMap<String,String>> storageList = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> storageHashMap;
			
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player X1")){
					for(int i=0;i<ScreenStyles.CLOUD_ITEM_LIST.length;i++){
						storageHashMap = new HashMap<String, String>();
						storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[i]);
						//23 feb 2015
						if (status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
							storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Connected");
						} else if (!status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
							storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Disconnected");
						}else{
							storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"");
						}
						storageList.add(storageHashMap);
					}
				}else if(DataStorage.getDeviceType() != null){
					for(int i=0;i<ScreenStyles.CLOUD_ITEM_LIST.length-1;i++){
						storageHashMap = new HashMap<String, String>();
						storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[i]);
						//23 feb 2015
						if (status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
							storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Connected");
						} else if (!status && ScreenStyles.CLOUD_ITEM_LIST[i].equalsIgnoreCase("USB")) {
							storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"Disconnected");
						}else{
							storageHashMap.put(ScreenStyles.LIST_KEY_DESCRIPTION,"");
						}
						storageList.add(storageHashMap);
					}
				}
				list.setCacheColorHint(Color.TRANSPARENT);
				ListViewAdapter adapter = new ListViewAdapter(mUsbInstance, storageList,0,"storage", null,-1);
				list.setAdapter(adapter);
			
				if(mContainer != null && list != null){
					mContainer.setOrientation(LinearLayout.VERTICAL);
					mContainer.addView(list);
					list.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
							HashMap<String, String> map= storageList.get(arg2);
							if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
								String val = map.get(ScreenStyles.LIST_KEY_TITLE);
								if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[0])){
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("userid",mDataAccess.getCurrentUserId());
									list.put("caller", "com.player.apps.Storage");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String method = "com.port.apps.epg.Plan.showRecording";
									new AsyncDispatch(method, dispatchHashMap,true).execute();
								}
								if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[1])){
									USB_BASE_PATH="/mnt/sda/";
									requestforUSBAction(ScreenStyles.CLOUD_ITEM_LIST[1]);											
								}
								if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[2])){
									USB_BASE_PATH="/mnt/mmcblk1/mmcblk1p1/";
									requestforUSBAction(ScreenStyles.CLOUD_ITEM_LIST[2]);											
								}
							}
						}
					});
				}
//				showStorageScreen();
//				requestForUSB();
				return true;
			}else{ //if app is not connected to the Player
				
				storageHashMap = new HashMap<String, String>();
				storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[0]);
				storageList.add(storageHashMap);
				storageHashMap = new HashMap<String, String>();
				storageHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.CLOUD_ITEM_LIST[2]);
				storageList.add(storageHashMap);
				list.setCacheColorHint(Color.TRANSPARENT);
				ListViewAdapter adapter = new ListViewAdapter(mUsbInstance, storageList,0,"storage", null,-1);
				list.setAdapter(adapter);
				if(mContainer != null && list != null){
					mContainer.setOrientation(LinearLayout.VERTICAL);
					mContainer.addView(list);
					list.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
							HashMap<String, String> map= storageList.get(arg2);
							if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
								String val = map.get(ScreenStyles.LIST_KEY_TITLE);
								if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[0])){
									Layout.progressDialog = new ProgressDialog(Storage.this,R.style.MyTheme);
									Layout.progressDialog.setCancelable(true);
									Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
									Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
									Layout.progressDialog.show();
									
									Intent intent = new Intent(Storage.this , Cloudstorage.class);
									intent.putExtra("handler", "com.port.apps.epg.Plan.showRecording");
									startService(intent);
								}
								if(val.equalsIgnoreCase(ScreenStyles.CLOUD_ITEM_LIST[2])){
									//to do
									retrieve(new File("LukupPlayer/"));
								}
							}
						}
					});
				
				}
				return true;				
			}		

		}else if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN)) {
			mContainer.removeAllViews();
			showStorageScreen();
//			requestForUSB();
			return true;
		}
		return false;
	}
	
	//unmount USB
	private void unmountUSB(){
		
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("consumer", "TV");
		list.put("network",mDataAccess.getConnectionType());
		list.put("caller", "com.player.apps.Storage");
		list.put("called", "messageActivity");
		dispatchHashMap.add(list);
		String method = "com.port.apps.storage.VideoPlayer.backToHome"; 
		new AsyncDispatch(method, dispatchHashMap,false).execute();
		if(progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		
	}
	
/************************************************************************************************/
	
	public final BroadcastReceiver mStorageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String jsondata = "";
			String handler = "";
			JSONObject objData = null;
			if(extras != null){
				if(Constant.DEBUG)  Log.d(TAG, "BroadcastReceiver Storage>>>");
				if(extras.containsKey("Params")){
					jsondata = extras.getString("Params");
					if(extras.containsKey("Handler")){
						handler = extras.getString("Handler");
					}
					try {
						objData = new JSONObject(jsondata);
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
					if(Constant.DEBUG)  Log.d(TAG , "jsondata  "+jsondata+", handler  "+handler);
					processUIData(handler, objData);
				}
			}
		}
	};
	
	public void processUIData(String handler,final JSONObject jsonData){

		try {
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.dismiss();
			}
			if(Constant.DEBUG)  Log.d(TAG, "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.storage.sendUSBStatus")){
				if(jsonData.has("result")){
					String result = Utils.getDataFromJSON(jsonData, "result");
					if(Constant.DEBUG)  Log.d(TAG,"result: "+result);
					if(result.equalsIgnoreCase("success")){
						status = true;
					}else{
						status = false;
					}
					showStorageScreen();
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.storage.fileBrowser")){
				if(jsonData.has("result")){
					String result = Utils.getDataFromJSON(jsonData, "result");
					if(Constant.DEBUG)  Log.d(TAG,"fileBrowser result: "+result);
					if(result.equalsIgnoreCase("success")){
						showFileBrowser(jsonData);
					}
					else{
						if(jsonData.has("msg")){
							HelpText.showHelpTextDialog(mUsbInstance,jsonData.getString("msg") , 2000);
						}
					}
				}else{
					if(jsonData.has("msg")){
						HelpText.showHelpTextDialog(mUsbInstance,jsonData.getString("msg") , 2000);
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.storage.processFolders")){
				if(jsonData.has("result")){
					String result = Utils.getDataFromJSON(jsonData, "result");
					if(Constant.DEBUG)  Log.d(TAG,"fileBrowser result: "+result);
					if(result.equalsIgnoreCase("success")){
						showFileBrowser(jsonData);
					}else if(result.equalsIgnoreCase("failure")){
						if(jsonData.has("msg")){
							HelpText.showHelpTextDialog(mUsbInstance,jsonData.getString("msg") , 2000);
						}
					}
					else{
						if(jsonData.has("msg")){
							HelpText.showHelpTextDialog(mUsbInstance,jsonData.getString("msg") , 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.showRecording")){
				if(mUsbInstance != null){
					if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								JSONArray jsonArray = Utils.getListFromJSON(jsonData, "recordList");
								recordList.clear();
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										HashMap<String,String> showMap = new HashMap<String, String>();
										JSONObject obj;
										try {
											obj = jsonArray.getJSONObject(i);
											String mPath = "";
											String mName = "";
											String mSize = "";
											String mEvent = "";
											if(obj.has("eventid")){
												mEvent= obj.getString("eventid");
											}
											if(obj.has("path")){
												mPath= obj.getString("path");
											}
											if(obj.has("size")){
												mSize= obj.getString("size");
											}
											if(obj.has("name")){
												mName= obj.getString("name");
											}
											showMap.put("path", mPath);
											showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
											showMap.put("size", mSize);
											showMap.put("eventid", mEvent);
											recordList.add(showMap);
											if(recordList.size()>0){
												showRecording(recordList);
											}
										}catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										}
									}
								}
							}else{
								HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_RECORDED_CONTENT), 2000);
							}
						}
					}else{
						if(jsonData.has("result")){
							if(Constant.DEBUG)  Log.d(TAG,"Going to show stored content");
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								if(Constant.DEBUG)  Log.d(TAG,"Successfully got stored content ");
								String resp = Utils.getDataFromJSON(jsonData, "data");
								JSONObject response = new JSONObject(resp);
								if(response.has("List")){
									JSONArray jsonArray = Utils.getListFromJSON(response, "List");
									recordList.clear();
									if(jsonArray != null && jsonArray.length() >0){
										if(Constant.DEBUG)  Log.d(TAG,"More than one recorded item");
										for(int i= 0;i<jsonArray.length();i++){
											HashMap<String,String> showMap = new HashMap<String, String>();
											JSONObject obj;
											try {
												obj = jsonArray.getJSONObject(i);
												String mPath = "";
												String mName = "";
												String mSize = "";
												String mEvent = "";
												if(obj.has("eventid")){
													mEvent= obj.getString("eventid");
												}
												if(obj.has("objectid")){
													mPath= obj.getString("objectid");
												}
												if(obj.has("size")){
													mSize= obj.getString("size");
												}
												if(obj.has("name")){
													mName= obj.getString("name");
												}
												if(Constant.DEBUG)  Log.d(TAG,"Path : "+mPath + " Name : " + mName + " Size : " + mSize + " Event id : " + mEvent);
												showMap.put("path", mPath);
												showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
												showMap.put("size", mSize);
												showMap.put("eventid", mEvent);
												recordList.add(showMap);
												if(recordList.size()>0){
													showRecording(recordList);
												}
											}catch (Exception e) {
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
											}
										}
									}
								}else{
									HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_RECORDED_CONTENT), 2000);
								}
							}else{
								HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_RECORDED_CONTENT), 2000);
							}
						}else{
							HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_RECORDED_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.deleteRecording")){
				if(jsonData.has("result")){
					String result = Utils.getDataFromJSON(jsonData, "result");
					if(result.equalsIgnoreCase("success")){
						if(jsonData.has("msg")){
							HelpText.showHelpTextDialog(mUsbInstance,jsonData.getString("msg") , 2000);
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
	
	private ListView lukupListView;
	private LinearLayout.LayoutParams standParams;
	private ScrollList scrollAdapter = null;
	private String selectItemId;
	private String objectId;
	
	private void showRecording(final ArrayList<HashMap<String, String>> recordList) {
		if(Constant.DEBUG)  Log.d(TAG , "showRecording() ");
		if(mUsbInstance != null){
			mUsbInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					selectedType = "cloud";
					mContainer.removeAllViews();
					//HelpTip
					HelpTip.requestForHelp(mUsbInstance.getResources().getString(R.string.WELCOME_TO_YOUR_CLOUD_DVR),
							mUsbInstance.getResources().getString(R.string.STORAGE_MSG2),mUsbInstance);
					
					DataStorage.setCurrentScreen(ScreenStyles.INITIAL_SCREEN);
					LayoutInflater  inflater = mUsbInstance.getLayoutInflater();
					View listView = inflater.inflate(R.layout.guidelist,null);
					mContainer.addView(listView,getLinearLayoutParams());
					lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
					scrollAdapter = new ScrollList(mUsbInstance, recordList, "record","Storage");
					lukupListView.setAdapter(scrollAdapter);
					refreshListView(lukupListView);		
					
					lukupListView.setOnItemClickListener(mUsbInstance);
					lukupListView.setOnItemLongClickListener(mUsbInstance);
				}
			});
		}
	}
	
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}
	
	private void refreshListView(final ListView lukupListView) {
		if(Constant.DEBUG)  Log.d(TAG , "showRecording() ");
		try {
			if(mUsbInstance != null){
				mUsbInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						RelativeLayout listParent = (RelativeLayout) lukupListView.getParent();
						lukupListView.invalidateViews();
						if(listParent != null){
							listParent.removeAllViews();
							listParent.addView(lukupListView);
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,long arg3) {
		if(Constant.DEBUG)  Log.d(TAG, "onItemLongClick(). ");
		if (maingrid_select_index != position){      
			if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
			{
				Log.w(TAG, "onItemLongClick().position is ::"+position);
				currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
				currentTextView.setTextColor(getResources().getColor(R.color.white));
			}
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
			currentTextView.setTextColor(getResources().getColor(R.color.pink));
		}else if(maingridview_focus != null){
			if(Constant.DEBUG)  Log.d(TAG, "onItemLongClick().position is ::"+position);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
			currentTextView.setTextColor(getResources().getColor(R.color.white));
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
		}
		maingrid_focus_index = position;
		currentPostion = position;
		
		hideMenu();
		if(Constant.DEBUG)  Log.d(TAG ,"Long click");
		HashMap<String, String> map = recordList.get(position);
//		if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
//			selectItemId = map.get(ScreenStyles.LIST_KEY_TITLE);
//		}
		if(map.containsKey("eventid")){
			selectItemId = map.get("eventid");
		}
		if(map.containsKey("path")){
			objectId = map.get("path");
		}
		showCustomMenu();
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		if(Constant.DEBUG)  Log.d(TAG, "OnItemClickListener(). ");
		if (maingrid_select_index != position){      
			if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
			{
				Log.w(TAG, "OnItemClickListener().position is ::"+position);
				currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
				currentTextView.setTextColor(getResources().getColor(R.color.white));
			}
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
			currentTextView.setTextColor(getResources().getColor(R.color.pink));
		}else if(maingridview_focus != null){
			if(Constant.DEBUG)  Log.d(TAG, "OnItemClickListener().position is ::"+position);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
			currentTextView.setTextColor(getResources().getColor(R.color.white));
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
		}
		maingrid_focus_index = position;
		currentPostion = position;
		String path = "";
		String name = "";
		HashMap<String, String> map = recordList.get(position);
		if(map.containsKey("path")){
			path = map.get("path");
		}
		if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
			name = map.get(ScreenStyles.LIST_KEY_TITLE);
		}
		
		String extention = path.trim().substring(path.trim().lastIndexOf("."));
		if(!extention.trim().equalsIgnoreCase("") && extention.trim() != null){
			HelpTip.close(mUsbInstance);
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				Intent play = new Intent(Storage.this, PlayBack.class);
				play.putExtra("ActivityName", "Storage");
				play.putExtra("SubType", "recorded");
				play.putExtra("EventId", "");
				play.putExtra("EventUrl", path);
				startActivity(play);
			}else{ //if app is not connected, play file on the client itself
				Media.playOnClient(path, mContainer, mUsbInstance,"recorded","","","");
			}
		}
		
	}
	
	public void showMenuItems() { 
		if(Constant.DEBUG)  Log.d(TAG , "showMenuItems() ");
		getContextMenu();
	}
	
	private void getContextMenu(){
		try {
			if(mMenu != null){
				mMenu.setMenuItems(null);
				mMenu.setItemsPerLineInPortraitOrientation(3);
			}
			customMenuValue = null;
			customMenuIcons = null;
			if (selectedType.equalsIgnoreCase("usb")) {
				customMenuValue = ScreenStyles.USB_MENU_ITEMS;
				customMenuIcons = ScreenStyles.USB_MENU_ICONS;
			} else if (selectedType.equalsIgnoreCase("cloud")) {
//				if(recordList.size()>0){
				customMenuValue = ScreenStyles.CLOUD_CONTEXTMENU_ITEMS;
				customMenuIcons = ScreenStyles.CLOUD_MENU_ICONS;
//				}
			} else if(selectedType.equalsIgnoreCase("local")){
				customMenuValue = ScreenStyles.LOCAL_STORAGE_ITEMS;
				customMenuIcons = ScreenStyles.STORAGE_MENU_ICONS;
			}
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				setMenuBasedOnPlayon();
			}
			getMenuItemsArray(customMenuValue, customMenuIcons);
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	private void setMenuBasedOnPlayon(){
		try {
			if(selectedFilePath.indexOf(".") != -1){
				extention = selectedFilePath.trim().substring(selectedFilePath.trim().lastIndexOf("."));
				String playStatus = DataStorage.isPlaying();
				if(Constant.DEBUG)  Log.d(TAG ,"Before setMenuBasedOnPlayon().isPlaying(): "+ playStatus +", extention: "+extention);
				
				if(extention.trim().equalsIgnoreCase(".mp3")) {
					if (playStatus.equalsIgnoreCase("00") || playStatus.equalsIgnoreCase("01")) { //none + only video
						if(customMenuValue != null){
							for(int i=0;i<customMenuValue.length;i++){
								if(customMenuValue[i].equalsIgnoreCase("Stop")){
									customMenuValue[i] = "Play On";
									customMenuIcons[i] = R.drawable.v13_ico_playon_01;
									break;
								}
							}
						}
					}else if (playStatus.equalsIgnoreCase("11") || playStatus.equalsIgnoreCase("10")) { //both + only audio
						if (customMenuValue != null) {
							for(int i=0;i<customMenuValue.length;i++){
								if(customMenuValue[i].equalsIgnoreCase("Play On")){
									customMenuValue[i] = "Stop";
									customMenuIcons[i] = R.drawable.v13_ico_list_remove;
									break;
								}
							}
						} 
					}
				} else { //if url extension is video
					if (playStatus.equalsIgnoreCase("00") || playStatus.equalsIgnoreCase("10")) { //none + only audio
						if(customMenuValue != null){
							for(int i=0;i<customMenuValue.length;i++){
								if(customMenuValue[i].equalsIgnoreCase("Stop")){
									customMenuValue[i] = "Play On";
									customMenuIcons[i] = R.drawable.v13_ico_playon_01;
									break;
								}
							}
						}
					}else if (playStatus.equalsIgnoreCase("11") || playStatus.equalsIgnoreCase("01")) { //both + only video
						if (customMenuValue != null) {
							for(int i=0;i<customMenuValue.length;i++){
								if(customMenuValue[i].equalsIgnoreCase("Play On")){
									customMenuValue[i] = "Stop";
									customMenuIcons[i] = R.drawable.v13_ico_list_remove;
									break;
								}
							}
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
	
	private void getMenuItemsArray(String[] customMenuValue,int[] customMenuIcons){
		if(Constant.DEBUG)  Log.d(TAG , "getMenuItemsArray() ");
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		if(customMenuValue != null && customMenuValue.length >0 && customMenuIcons != null && customMenuIcons.length >0){
			for(int i=0;i<customMenuValue.length;i++){
				CustomMenuItem cmi = new CustomMenuItem();
				cmi.setCaption(customMenuValue[i]);
				cmi.setImageResourceId(customMenuIcons[i]);
				cmi.setId(i);
				menuItems.add(cmi);
				if(Constant.DEBUG)  Log.d(TAG , "getMenuItemsArray().isShowing(): "+mMenu.isShowing());
				if (!mMenu.isShowing() && menuItems.size() > 0){
					try {
						mMenu.setMenuItems(menuItems);
					} catch (Exception ex) {
						try {
							HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.SOMTHING_WRONG), 3000);
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
	
	public void processMenuActions(CustomMenuItem key) {
		if(Constant.DEBUG)  Log.d(TAG , "processMenuActions() ");
		if(key.getCaption().toString().trim().equalsIgnoreCase("Remove")){
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				if(selectedType.equalsIgnoreCase("cloud")){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("item", selectItemId);
					list.put("ObjectId", objectId);
					list.put("userid",mDataAccess.getCurrentUserId());
					list.put("caller", "com.player.apps.Storage");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String method = "com.port.apps.epg.Plan.deleteRecording";
					new AsyncDispatch(method, dispatchHashMap,true).execute();
					return;
				}else if(selectedType.equalsIgnoreCase("local")){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("filepath", selectedFilePath);
					list.put("userid",mDataAccess.getCurrentUserId());
					list.put("caller", "com.player.apps.Storage");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String method = "com.port.apps.storage.deleteStorage";
					new AsyncDispatch(method, dispatchHashMap,true).execute();
					return;
				}
			}else{ //if app is not connected, then send request to remove using rest api 
				if(selectedType.equalsIgnoreCase("cloud")){
					Layout.progressDialog = new ProgressDialog(Storage.this,R.style.MyTheme);
					Layout.progressDialog.setCancelable(true);
					Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
					Layout.progressDialog.show();
					
					Intent intent = new Intent(Storage.this , Cloudstorage.class);
					intent.putExtra("handler", "com.port.apps.epg.Plan.deleteRecording");
					intent.putExtra("item", selectItemId);
					intent.putExtra("ObjectId", objectId);
					startService(intent);
				}else if(selectedType.equalsIgnoreCase("local")){
					//to do
					remove(selectedFilePath);
				}
			}
		}if(key.getCaption().toString().trim().equalsIgnoreCase("TV Guide")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Storage");
			list.put("called", "startActivity");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
			
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Play On")){
			if(selectedFilePath.indexOf(".") != -1){
				String extention = selectedFilePath.trim().substring(selectedFilePath.trim().lastIndexOf("."));
				if(!extention.trim().equalsIgnoreCase("") && extention.trim() != null){
					if(extention.trim().equalsIgnoreCase(".mp4") || extention.trim().equalsIgnoreCase(".mpg") || extention.trim().equalsIgnoreCase(".mpeg")
						|| extention.trim().equalsIgnoreCase(".mov") || extention.trim().equalsIgnoreCase(".m4v") || extention.trim().equalsIgnoreCase(".mkv")
						 || extention.trim().equalsIgnoreCase(".avi")){
//						PlayOn.getRemoteDisplays(selectItemId,"com.player.apps.Storage");
						Intent intent = new Intent(Storage.this,DlnaRenderer.class);
						intent.putExtra("sourceUrl", selectedFilePath);
						startActivity(intent);
					}else if(extention.equalsIgnoreCase(".mp3")  || extention.trim().equalsIgnoreCase(".wav")  || extention.trim().equalsIgnoreCase(".wma")){
						PlayOn.getConnectedDevices(selectedFilePath,"audio","com.player.apps.Storage",mUsbInstance);
					}
				}
			}else{
				try {
					HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.UNABLETOPLAY), 3000);
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
		}
	}
	
	private void remove(String filepath){
		if(Constant.DEBUG)  Log.d(TAG ,"Removing from file path : "+ filepath);
		File file = new File(filepath);
		boolean deleted = file.delete();
	}
	
	private void retrieve(File baseUSBFile){
		if(Constant.DEBUG)  Log.d(TAG ,"Retrieving file :" + baseUSBFile + " from file path : "+ selectedFilePath);
		selectedType = "local";
		if(baseUSBFile != null && !baseUSBFile.isHidden() && baseUSBFile.canRead()){
			File[] files = baseUSBFile.listFiles();
			if(files.length > 0){
				FileSystem mFileSys = new FileSystem();
				HashMap<String, ArrayList<String>> filemap = (HashMap<String, ArrayList<String>>) mFileSys.getFile(selectedFilePath);
				if(filemap != null && filemap.size() >0){
					Set<HashMap.Entry<String, ArrayList<String>>> entry = filemap.entrySet();
					Iterator<HashMap.Entry<String, ArrayList<String>>> itr = entry.iterator();
					while (itr.hasNext()) {
						HashMap.Entry<String, ArrayList<String>> string =  itr.next();
						if(Constant.DEBUG) Log.d(TAG,"Data : "+string.getKey()+" Value :"+string.getValue().toString());
					}
					ArrayList<String> folderList = filemap.get("folder");
					if(Constant.DEBUG)  Log.d(TAG, folderList+"");
					ArrayList<String> fileList = filemap.get("files");
					if(Constant.DEBUG)  Log.d(TAG, fileList+"");
					try {
						JSONObject sendResponse = new JSONObject();
						JSONObject data = new JSONObject();
						data.put("filepath", selectedFilePath);
						JSONArray folderJsonArray = new JSONArray();
						if(folderList != null && folderList.size() >0){
							for(int i=0;i<folderList.size();i++){
								if (!folderList.get(i).equalsIgnoreCase("transcode")) {
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("id", i);
									jsonObject.put("name", folderList.get(i));
									jsonObject.put("path", selectedFilePath+folderList.get(i));
									folderJsonArray.put(jsonObject);
								}
							}
						}
						data.put("folderList", folderJsonArray);
						JSONArray fileJsonArray = new JSONArray();
						if(fileList != null && fileList.size() >0){
							for(int i=0;i<fileList.size();i++){
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("id", i);
								jsonObject.put("name", fileList.get(i));
								jsonObject.put("path", selectedFilePath+fileList.get(i));
								folderJsonArray.put(jsonObject);
							}
						}
						data.put("fileList", fileJsonArray);
						if((fileJsonArray != null && fileJsonArray.length() == 0) && (folderJsonArray != null && folderJsonArray.length() == 0)){
							data.put("result", "failure");
							boolean isSDCard = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
							if(Constant.DEBUG) Log.d(TAG,"fileBrowser().isSDCard : "+isSDCard);
							if (isSDCard) {
								data.put("msg", this.getResources().getString(R.string.NO_CONTENT));
							}else{
								data.put("msg", this.getResources().getString(R.string.NO_CONTENT));
							}
						}else{
								data.put("result", "success");
								showFileBrowser(data);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_STORAGE, errors.toString(), e.getMessage());
					}
				}
			}else{
				HelpText.showHelpTextDialog(mUsbInstance, this.getResources().getString(R.string.NO_CONTENT), 2000);
			}
		}
	}
	
	private void processFolders(String fileName,String filePath,String action){
		if(Constant.DEBUG)  Log.d(TAG ,"Processing file :" + fileName + " from file path : "+ filePath + " for action : " + action);
		try {
			File selectedFile = new File(filePath);
			if(action != null && !action.equalsIgnoreCase("") && action.equalsIgnoreCase("back")){
				if(Constant.DEBUG)  Log.d(TAG, "Actual file path "+selectedFile.getAbsolutePath());
				selectedFile = selectedFile.getParentFile();
				if(Constant.DEBUG)  Log.d(TAG, "Parent file path "+selectedFile.getAbsolutePath());
				filePath = selectedFile.getAbsolutePath();
			}
			if(selectedFile != null && !selectedFile.isHidden() && selectedFile.canRead()){
				if(selectedFile != null && selectedFile.isDirectory()){
					File[] files = selectedFile.listFiles();
					if(files.length > 0){
						FileSystem mFileSys = new FileSystem();
						HashMap<String, ArrayList<String>> filemap = (HashMap<String, ArrayList<String>>) mFileSys.getFile(filePath);
						if(filemap != null && filemap.size() >0){
							Set<HashMap.Entry<String, ArrayList<String>>> entry = filemap.entrySet();
							Iterator<HashMap.Entry<String, ArrayList<String>>> itr = entry.iterator();
							while (itr.hasNext()) {
								HashMap.Entry<String, ArrayList<String>> string =  itr.next();
								if(Constant.DEBUG) Log.d(TAG,"Data : "+string.getKey()+" Value :"+string.getValue().toString());
							}
							ArrayList<String> folderList = filemap.get("folder");
							if(Constant.DEBUG)  Log.d(TAG, folderList+"");
							ArrayList<String> fileList = filemap.get("files");
							if(Constant.DEBUG)  Log.d(TAG, fileList+"");
							try {
								JSONObject sendResponse = new JSONObject();
								JSONObject data = new JSONObject();
								data.put("filepath", filePath+"/");
								JSONArray folderJsonArray = new JSONArray();
								if(folderList != null && folderList.size() >0){
									for(int i=0;i<folderList.size();i++){
										JSONObject jsonObject = new JSONObject();
										jsonObject.put("id", i);
										jsonObject.put("name", folderList.get(i));
										jsonObject.put("path", filePath+"/"+folderList.get(i));
										folderJsonArray.put(jsonObject);
									}
								}
								data.put("folderList", folderJsonArray);
								JSONArray fileJsonArray = new JSONArray();
								if(fileList != null && fileList.size() >0){
									for(int i=0;i<fileList.size();i++){
										JSONObject jsonObject = new JSONObject();
										jsonObject.put("id", i);
										jsonObject.put("name", fileList.get(i));
										jsonObject.put("path", filePath+"/"+fileList.get(i));
										folderJsonArray.put(jsonObject);
									}
								}
								data.put("fileList", fileJsonArray);
								if((fileJsonArray != null && fileJsonArray.length() == 0) && (folderJsonArray != null && folderJsonArray.length() == 0)){
									data.put("result", "failure");
//									data.put("msg", this.getResources().getString(R.string.NO_CONTENT));
									HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}else{
									data.put("result", "success");
									showFileBrowser(data);
								}
								
							} catch (JSONException e) {
								e.printStackTrace();
								StringWriter errors = new StringWriter();
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_STORAGE, errors.toString(), e.getMessage());
							}
						}
					}else{
						HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_CONTENT), 2000);
					}
				}
			}else{
				HelpText.showHelpTextDialog(mUsbInstance, mUsbInstance.getResources().getString(R.string.NO_CONTENT), 2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_STORAGE, errors.toString(), e.getMessage());
		}
	}
	
}
