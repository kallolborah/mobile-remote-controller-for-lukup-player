package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.action.HelpTip;
import com.player.action.Lock;
import com.player.action.OverlayCancelListener;
import com.player.service.CacheDockData;
import com.player.service.ChannelInfo;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
import com.player.widget.ListViewAdapter;
import com.player.widget.OverLay;


public class Organise extends Layout{

	private Organise mOrganiserInstance;
	private LinearLayout.LayoutParams standParams = null;
	private String TAG = "Organiser";
	private String ActivityName;
	private String selectedType;
	private String selectedId;
	private ListViewAdapter adapter;
	private OverLay overlay;
	private String mediaUrl;
	private Dialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOrganiserInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,"Pre .CurrentScreen: "+DataStorage.getCurrentScreen());
		
		IntentFilter organise = new IntentFilter("com.player.apps.Organise");
		registerReceiver(mOrganiseReceiver,organise);
		
		Bundle br = this.getIntent().getExtras();
		if (br != null) {
			if (br.containsKey("ActivityName")) {
				ActivityName = br.getString("ActivityName");
			}
			if (br.containsKey("Type")) {
				selectedType = br.getString("Type");
			}
			if (br.containsKey("id")) {
				selectedId = br.getString("id");
			}
			
			if(Constant.DEBUG)  Log.d(TAG ,"selectedType: "+selectedType);
		}
		OrganiseSelected();
	}	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mOrganiseReceiver != null){
			mOrganiserInstance.unregisterReceiver(mOrganiseReceiver);
		}
	}
	
	
	private void OrganiseSelected() {
		try {
			if(mOrganiserInstance != null){
				mOrganiserInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
					DataStorage.setCurrentScreen(ScreenStyles.ORGANAISE);
					ListView list = null;
					list = getOrganiseItemList(selectedType);
					if(mContainer != null){
						mContainer.setOrientation(LinearLayout.VERTICAL);
						mContainer.removeAllViews();
						mContainer.addView(list,getLinearLauoutParams());
						
						//HelpTip
						HelpTip.requestForHelp(mOrganiserInstance.getResources().getString(R.string.IMPORTING_CONTENT_INTO_YOUR_CHANNELS),
								mOrganiserInstance.getResources().getString(R.string.ORGANISE_MSG1),mOrganiserInstance);
					}
					
					list.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View v,int pos, long arg3) {
							TextView  inflater = (TextView) v.findViewById(R.id.title);
							if(Constant.DEBUG) System.out.println("Selected Organaise Item "+inflater.getText().toString().trim());
							if(inflater.getText().toString().trim().equalsIgnoreCase("Create New")){
								CreateNew();
							}else  if(inflater.getText().toString().trim().equalsIgnoreCase("Add to")){
								AddToEvent();
							}else if(inflater.getText().toString().trim().equalsIgnoreCase("Delete")){
								DeleteChannel();
							}else if(inflater.getText().toString().trim().equalsIgnoreCase("Rename")){
								RenameChannel();
							}else if(inflater.getText().toString().trim().equalsIgnoreCase("Fetch Content")){
								FetchContent();
							}
						}
					});
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
	
	private ListView getOrganiseItemList(String SelectedType) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if(Constant.DEBUG)  Log.d("Organaise" , "getOrganiseItemList().SelectedType: "+SelectedType);
		ListView listView = new ListView(mOrganiserInstance);
		if(SelectedType.equalsIgnoreCase("Bouquet")){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.GUIDE_ORGANISE_BOUQUETS[0]);
			map.put(ScreenStyles.LIST_KEY_THUMB_URL,Integer.toString(ScreenStyles.GUIDE_ORGANISE_BOUQUETS_ICONS[0]));
			list.add(map);
		}else if(SelectedType.equalsIgnoreCase("service")){
			for(int i=0;i<ScreenStyles.GUIDE_ORGANISE_PER_CHANNEL.length;i++){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.GUIDE_ORGANISE_PER_CHANNEL[i]);
				map.put(ScreenStyles.LIST_KEY_THUMB_URL,Integer.toString(ScreenStyles.GUIDE_ORGANISE_PER_CHANNEL_ICONS[i]));
				list.add(map);
			}
		}else if(SelectedType.equalsIgnoreCase("event")){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(ScreenStyles.LIST_KEY_TITLE, "Add to");
			map.put(ScreenStyles.LIST_KEY_THUMB_URL,Integer.toString(R.drawable.v13_ico_playlist_01_a));
			list.add(map);
		}
		listView.setCacheColorHint(Color.TRANSPARENT);
		ListViewAdapter adapter = new ListViewAdapter(mOrganiserInstance, list,0,"organaiselist", null,-1);
		listView.setAdapter(adapter);
		return listView;
	}

	private void RenameChannel() {
		if(Constant.DEBUG)  Log.d("Organaise" , "RenameChannel()");
		if(mOrganiserInstance != null){
			mOrganiserInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
				OverLay overlay = new OverLay(mOrganiserInstance);
				dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETVALUE, "Rename", mOrganiserInstance.getResources().getString(R.string.CREATENEW_TITLE),null,null,false);
				if(dialog != null){
					dialog.show();
					Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
					if(cancelBtn != null)
						cancelBtn.setOnClickListener(new  OverlayCancelListener(dialog));
		
					Button sendBtn = (Button) dialog.findViewById(R.id.overlayOkButton);
					final EditText textfield = (EditText) dialog.findViewById(R.id.keyValue);
					
					if(sendBtn != null){
						sendBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								String textdata = null;
								if(textfield != null){
									textdata = textfield.getText().toString().trim();
									if(textdata == null || textdata.equalsIgnoreCase("")){
										HelpText.showHelpTextDialog(mOrganiserInstance, mOrganiserInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
										return;
									}
								}
								try{
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("showid",selectedId);
									list.put("name",textdata);
									list.put("caller", "com.player.apps.Organise");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String method = "com.port.apps.epg.Organise.RenameService"; 
									new AsyncDispatch(method, dispatchHashMap,true).execute();
									
									dialog.dismiss();
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
			});
		}
	}

	private void DeleteChannel() {
		if(Constant.DEBUG)  Log.d("Organaise" , "DeleteChannel()");
		if(mOrganiserInstance != null){
			mOrganiserInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
				OverLay overlay = new OverLay(mOrganiserInstance);
				dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, "Delete", mOrganiserInstance.getResources().getString(R.string.DELETE_TITLE),null,null,false);
				if(dialog != null){
					Button deleteBtn = (Button) dialog.findViewById(R.id.okButton);
					Button cancelBtn = (Button) dialog.findViewById(R.id.overlayCancelButton);
					if(cancelBtn != null)
						cancelBtn.setOnClickListener(new  OverlayCancelListener(dialog));
					
					if(deleteBtn != null){
						deleteBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								try{
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("showid",selectedId);
									list.put("caller", "com.player.apps.Organise");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String method = "com.port.apps.epg.Organise.deleteChannel"; 
									new AsyncDispatch(method, dispatchHashMap,true).execute();
									
									dialog.dismiss();
								}catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						});
					}
					dialog.show();
				}
				}
			});
		}
	}

	private void AddToEvent() {
		if(mOrganiserInstance != null){
			mOrganiserInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
				OverLay overlay = new OverLay(mOrganiserInstance);
				dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETVALUE, "Add to Personal Channel", mOrganiserInstance.getResources().getString(R.string.CREATENEW_TITLE),null,null,false);
				if(dialog != null){
					dialog.show();
					Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
					if(cancelBtn != null)
						cancelBtn.setOnClickListener(new  OverlayCancelListener(dialog));
		
					Button sendBtn = (Button) dialog.findViewById(R.id.overlayOkButton);
					sendBtn.setText("AddTo");
					final EditText textfield = (EditText) dialog.findViewById(R.id.keyValue);
					if(sendBtn != null){
						sendBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								String textdata = null;
								if(textfield != null){
									textdata = textfield.getText().toString().trim();
									if(textdata == null || textdata.equalsIgnoreCase("")){
										HelpText.showHelpTextDialog(mOrganiserInstance,mOrganiserInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
										return;
									}
								}
								try{
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("showid",selectedId);
									list.put("name",textdata);
									list.put("caller", "com.player.apps.Organise");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String method = "com.port.apps.epg.Organise.addTo"; 
									new AsyncDispatch(method, dispatchHashMap,true).execute();
									
									dialog.dismiss();
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
			});
		}
	}
	
	

	private void CreateNew() {
		if(Constant.DEBUG)  Log.d("Organaise" , "CreateNew()");
		if(mOrganiserInstance != null){
			mOrganiserInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					OverLay overlay = new OverLay(mOrganiserInstance);
					dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETVALUE, "Create New", mOrganiserInstance.getResources().getString(R.string.CREATENEW_TITLE),null,null,false);
					if(dialog != null){
						dialog.show();
						Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
						if(cancelBtn != null)
							cancelBtn.setOnClickListener(new  OverlayCancelListener(dialog));
			
						Button sendBtn = (Button) dialog.findViewById(R.id.overlayOkButton);
						final EditText textfield = (EditText) dialog.findViewById(R.id.keyValue);
						
						if(sendBtn != null){
							sendBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									String textdata = null;
									if(textfield != null){
										textdata = textfield.getText().toString().trim();
										if(textdata == null || textdata.equalsIgnoreCase("")){
											HelpText.showHelpTextDialog(mOrganiserInstance, mOrganiserInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
											return;
										}
									}
									try{
										dispatchHashMap  = new ArrayList<HashMap<String,String>>();
										HashMap<String, String> list = new HashMap<String, String>();
										list.put("consumer", "TV");
										list.put("network",mDataAccess.getConnectionType());
										list.put("name",textdata);
										list.put("showid",selectedId);
										list.put("caller", "com.player.apps.Organise");
										list.put("called", "startService");
										dispatchHashMap.add(list);
										String method = "com.port.apps.epg.Organise.createService"; 
										new AsyncDispatch(method, dispatchHashMap,true).execute();
										
										dialog.dismiss();
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
			});
		}
	}
	
	
	private void FetchContent() {
		if(Constant.DEBUG)  Log.d("Organaise" , "FetchContent()");
		DataStorage.setCurrentScreen("fetchcontent");
		if(mOrganiserInstance != null){
			final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
			for(int i=0;i<ScreenStyles.FETCH_CONTENT_FROM_LIST.length;i++){
				HashMap<String,String> map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.FETCH_CONTENT_FROM_LIST[i]);
				list.add(map);
			}
			
			if(list != null && list.size() > 0){
				mOrganiserInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ListView fetchContentList = new ListView(mOrganiserInstance);
						fetchContentList.setCacheColorHint(Color.TRANSPARENT);
						fetchContentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
						ListViewAdapter adapter = new ListViewAdapter(mOrganiserInstance, list,0,"fetchcontent",null ,-1);
						fetchContentList.setAdapter(adapter);
						if(fetchContentList != null){
//							LinearLayout mainLayout = (LinearLayout) mOrganiserInstance.findViewById(R.id.navigationscreen);
//							if(mainLayout != null){
//								mainLayout.removeAllViews();
//								mainLayout.setOrientation(LinearLayout.VERTICAL);
//								mainLayout.addView(fetchContentList);
//							}
							if(mContainer != null){
								mContainer.removeAllViews();
								mContainer.setOrientation(LinearLayout.VERTICAL);
								mContainer.addView(fetchContentList,getLinearLauoutParams());
								fetchContentList.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
										if(Constant.DEBUG) Log.i(TAG,"FetchContent() Item "+ScreenStyles.FETCH_CONTENT_FROM_LIST[position]);
										String tag = ScreenStyles.FETCH_CONTENT_FROM_LIST[position];
										fetchDialogBox(tag);
									}
								});
							}
						}
						
//						fetchContentList.setOnItemClickListener(new OnItemClickListener() {
//							@Override
//							public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
//								overlay = new OverLay(mOrganiserInstance);
//								String tag = ScreenStyles.FETCH_CONTENT_FROM_LIST[position];
//								fetchDialogBox(tag);
//								if(dialog != null){
//									dialog.show();
//								}
//							}
//						});
					}
				});
			}
		}
	}
	
	
	private void fetchDialogBox(final String tag){
		if(Constant.DEBUG)  Log.d(TAG , "fetchDialogBox() tag: "+tag);
		if(mOrganiserInstance != null){
			mOrganiserInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
				OverLay overlay = new OverLay(mOrganiserInstance);
				final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL, tag, null,null,null,isLukupSelected);
				if(dialog != null){
					Button connectBtn =(Button)dialog.findViewById(R.id.connect);
					Button closeBtn =(Button)dialog.findViewById(R.id.overlayCancelButton);
					final EditText textfield = (EditText) dialog.findViewById(R.id.username);
					textfield.setHint(mOrganiserInstance.getResources().getString(R.string.ORGANISE_MSG));
					customLayout = new CustomLayout(mOrganiserInstance);
					final EditText passwordfield = (EditText) dialog.findViewById(R.id.password);
					if(passwordfield != null){
						passwordfield.setVisibility(View.GONE);
					}
					if(connectBtn != null){
						connectBtn.setText(mOrganiserInstance.getResources().getString(R.string.ORGANISE_FETCH_BUTTON));
						connectBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								if(textfield != null){
									mediaUrl = textfield.getText().toString().trim();
									if(mediaUrl == null || mediaUrl.equalsIgnoreCase("")){
										HelpText.showHelpTextDialog(mOrganiserInstance, mOrganiserInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
										return;
									}
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("url",mediaUrl);
									list.put("type",tag);
									list.put("showid",selectedId);
									list.put("caller", "com.player.apps.Organise");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String method = "com.port.apps.epg.Organise.fetchData"; 
									new AsyncDispatch(method, dispatchHashMap,true).execute();
									
									
									if(dialog != null && dialog.isShowing()){
										dialog.cancel();
									}
								}
							}
						});
					}
					closeBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(dialog != null && dialog.isShowing()){
								dialog.cancel();
							}
						}
					});
				}
				dialog.show();
				}
			});
		}
	}
	
	
	public LinearLayout.LayoutParams getLinearLauoutParams(){
		if(Constant.DEBUG)  Log.d(TAG , "getLinearLauoutParams()");
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			return backButtonAction(keyCode, event);
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private boolean backButtonAction(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG,"onBackPressed()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
		if(Constant.DEBUG)  Log.d(TAG ,"backButtonAction().CurrentScreen: "+DataStorage.getCurrentScreen());
			HelpTip.close(mOrganiserInstance);
			if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.ORGANAISE)) {
				mContainer.removeAllViews();
				finish();
			}else if(DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.FETCH_CONTENT)) {
				OrganiseSelected();
			}else{
				mContainer.removeAllViews();
				finish();
			}
		return false;
	}
	
/************************************************************************************************/
	
	public final BroadcastReceiver mOrganiseReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String jsondata = "";
			String handler = "";
			JSONObject objData = null;
			if(extras != null){
				if(extras != null){
					if(extras.containsKey("Params")){
						jsondata = extras.getString("Params");
					}if(extras.containsKey("Handler")){
						handler = extras.getString("Handler");
					}
					try {
						objData = new JSONObject(jsondata);
					} catch (Exception e) {
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
	
	private void processUIData(String handler,final JSONObject jsonData){
		try{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.dismiss();
				if(Constant.DEBUG)  Log.d(TAG , "progressDialog dismiss()");
			}
			if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.  "+handler);
			
			if(handler.equalsIgnoreCase("com.port.apps.epg.Organise.createService")){
				if(jsonData != null){
					try {
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(Utils.checkNullAndEmpty(result)){
							if(result.equalsIgnoreCase("success")){
//								finish();
								selectedId = Utils.getDataFromJSON(jsonData, "serviceid");
								FetchContent();
								HelpText.showHelpTextDialog(mOrganiserInstance, mOrganiserInstance.getResources().getString(R.string.ORGANISE_CREATE), 2000);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Organise.RenameService")){
				if(jsonData != null){
					try {
						String result = Utils.getDataFromJSON(jsonData, "result");
						String name = Utils.getDataFromJSON(jsonData, "name");
						if(Utils.checkNullAndEmpty(result)){
							if(result.equalsIgnoreCase("success")){
								CacheDockData.ServiceList.clear();
								Intent returnIntent = new Intent();
								returnIntent.putExtra("ID",selectedId);
								returnIntent.putExtra("Type",name);
								returnIntent.putExtra("Tag","rename");
								setResult(RESULT_OK,returnIntent);
								finish();
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Organise.deleteChannel")){
				if(jsonData != null){
					try {
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(Utils.checkNullAndEmpty(result)){
							if(result.equalsIgnoreCase("success")){
								ArrayList<ChannelInfo> defaultList = new ArrayList<ChannelInfo>();
								for (int i = 0; i < CacheDockData.ServiceList.size(); i++) {
									if (!CacheDockData.ServiceList.get(i).getId().equalsIgnoreCase(selectedId)) {
										defaultList.add(CacheDockData.ServiceList.get(i));
									}
								}
								CacheDockData.ServiceList.clear();
								CacheDockData.ServiceList = defaultList;
								Intent returnIntent = new Intent();
								returnIntent.putExtra("ID",selectedId);
								returnIntent.putExtra("Type",selectedType);
								returnIntent.putExtra("Tag","delete");
								setResult(RESULT_OK,returnIntent);
								finish();
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Organise.fetchData")){
				if(jsonData != null && jsonData.has("result")){
					if(jsonData.getString("result").equalsIgnoreCase("success")){
						DataStorage.setCurrentScreen(ScreenStyles.FETCH_CONTENT);
						HelpText.showHelpTextDialog(mOrganiserInstance, mOrganiserInstance.getResources().getString(R.string.FETCH_DATA), 2000);
					}else{
						if(jsonData.has("msg")){
							try {
								HelpText.showHelpTextDialog(mOrganiserInstance, jsonData.getString("msg"), 2000);
							}catch (Exception e) {
								e.printStackTrace();
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
							}
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Organise.addTo")){
				if(mOrganiserInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						try{
							if(result.equalsIgnoreCase("success")){
								HelpText.showHelpTextDialog(mOrganiserInstance, mOrganiserInstance.getResources().getString(R.string.ORGANISE_ADDTO), 2000);
							}else{
								HelpText.showHelpTextDialog(mOrganiserInstance, mOrganiserInstance.getResources().getString(R.string.ORGANISE_ADDTO_FAILURE), 2000);
							}
						}catch (Exception e) {
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
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