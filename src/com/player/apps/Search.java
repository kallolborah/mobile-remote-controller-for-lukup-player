package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.exoplayer.VideoSurfaceView;
import com.player.R;
import com.player.Layout;
import com.player.Player;
import com.player.action.AuthDialog;
import com.player.action.DlnaRenderer;
import com.player.action.ExoPlay;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.action.Like;
import com.player.action.Media;
import com.player.action.OverlayCancelListener;
import com.player.action.PlanRequest;
import com.player.action.Play;
import com.player.action.PlayOn;
import com.player.action.Playlist;
import com.player.action.Subscribe;
import com.player.network.ir.IRTransmitter;
import com.player.service.CacheDockData;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.OperatorKeys;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.webservices.SearchService;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
import com.player.widget.OverLay;
import com.player.widget.ScrollList;

public class Search extends Layout implements OnItemClickListener,OnItemLongClickListener{
	
	private Search mSearchInstance;
	private static String TAG = "SEARCH";
	private EditText searchField = null;
	private Button searchBtn = null;
	private RelativeLayout searchRL;
	private String category = "";
	private ListView lukupListView;
	private LinearLayout.LayoutParams standParams;
	private String islike;
	private String isSubscribe;
	private String isRecord;
	private String isLock;
	ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
	private ScrollList scrollAdapter = null;
	private int index = 0;
	private int limit = 20;
	private JSONObject searchResults;
	private String selectedUniqueId = "";
	private String selectedItem_Id = "";
	private String channelId = "";
	private String channelPrice = "";
	private String pricingModel;
	private String price;
	private String channelType = "";
	private String channelname = "";
	private String title = "";
	private String sourceUrl = "";
	private String startTime = "";
	private String startDate = "";
	private String duration = "";
	private HashMap<String, String> map;
	private JSONObject jsonInfoDate;
	private String desc ="";
	private String image = "";
	
	private int maingrid_select_index = -1;
	private int maingrid_focus_index = -1;
	private View maingridview_focus = null;
	RelativeLayout currentlayoutMain;
	TextView currentTextView;
	private String click="";
	private String extention;
	private int position;
	
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Constant.DEBUG) System.out.println("Search called");
		mSearchInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG  ,"Previous Screen: "+DataStorage.getCurrentScreen());
		
		IntentFilter search = new IntentFilter("com.player.apps.Search");
		registerReceiver(mSearchReceiver,search); 
		
		IntentFilter device = new IntentFilter("pairedA2DP");
		registerReceiver(mSearchReceiver,device);
		
		Bundle br = this.getIntent().getExtras();
		if (br != null) {
			if (br.containsKey("ActivityName")) {
				br.getString("ActivityName");
			}
		}
		
		//Mar 20 2015
//		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//		HashMap<String, String> list1 = new HashMap<String, String>();
//		list1.put("consumer", "TV");
//		list1.put("network", "BT");
//		list1.put("caller", "com.player.apps.Search");
//		list1.put("called", "startProject");
//		dispatchHashMap.add(list1);
//		String method1 = "com.port.apps.search.SettingsActivity.start";
	//	new AsyncDispatch(method1, dispatchHashMap,true).execute();
		
		//if application is connected to Player			
		if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("start", index+"");
			list.put("limit", limit+"");
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Search");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			String method = "com.port.apps.search.Search.getGenre";
			new AsyncDispatch(method, dispatchHashMap,true).execute();
			
			SearchResult();
		}else{
			if(CommonUtil.isNetworkConnected(mActivity)){
				SearchResult();
			}else{
				HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
			}
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(itemList != null && itemList.size() > 0 ){
			lukupListView.invalidate();
			scrollAdapter = new ScrollList(mSearchInstance, itemList, "event","Search");
			lukupListView.setAdapter(scrollAdapter);
			refreshListView(lukupListView);	
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mSearchReceiver != null){
			mSearchInstance.unregisterReceiver(mSearchReceiver);
		}
	}

	public void SearchResult(){
		try {
			if(mSearchInstance != null ){
				mSearchInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hideMenu();
						if(mContainer != null){
							mContainer.removeAllViews();
							
							//Helptip
							HelpTip.requestForHelp(mSearchInstance.getResources().getString(R.string.SEARCHING_CONTENT),
									mSearchInstance.getResources().getString(R.string.SEARCH_MSG1),mSearchInstance);
							
							DataStorage.setCurrentScreen("searchscreen");
							LayoutInflater  inflater = mSearchInstance.getLayoutInflater();
							View appLayout = inflater.inflate(R.layout.searchscreen,null);
							searchField = (EditText) appLayout.findViewById(R.id.search_field);
							searchBtn = (Button) appLayout.findViewById(R.id.search_Btn);
							if(searchBtn != null){
								searchBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										hideMenu();
										if(searchField != null){
											String searchKey = searchField.getText().toString().trim();
											if(Utils.checkNullAndEmpty(searchKey)){
												if(!Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){ //mobile app is not connected
													Layout.progressDialog = new ProgressDialog(Search.this,R.style.MyTheme);
													Layout.progressDialog.setCancelable(true);
													Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
													Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
													Layout.progressDialog.show();
													Intent intent = new Intent(Search.this,SearchService.class);
													intent.putExtra("handler", "com.port.apps.search.Search.getSearchData");
													intent.putExtra("keyword", searchKey);
													startService(intent);
												}
												else{
													showSearchFilterOptions(searchKey);	
												}	
											}else{
												HelpText.showHelpTextDialog(mSearchInstance,mSearchInstance.getResources().getString(R.string.EMPTY_INPUT), 3000);
											}
										}
									}
								});
							}
							
							searchRL = (RelativeLayout) appLayout.findViewById(R.id.searchRL);
							lukupListView = (ListView) appLayout.findViewById(R.id.lukuplist);
							mContainer.addView(appLayout,getLinearLauoutParams());
							
							lukupListView.setOnItemClickListener(mSearchInstance);
							lukupListView.setOnItemLongClickListener(mSearchInstance);
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
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,long arg3) {
		click = "longclick";
		this.position = position;
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
			Log.w(TAG, "onItemLongClick().position is ::"+position);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
			currentTextView.setTextColor(getResources().getColor(R.color.white));
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
		}
		maingrid_focus_index = position;
		hideMenu();
		map = itemList.get(position);
		if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
			selectedUniqueId = map.get(ScreenStyles.LIST_KEY_ID); //program id
		}		
		if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
			title = map.get(ScreenStyles.LIST_KEY_TITLE);
		}
		if(map.containsKey("servicetype")){
			channelType = map.get("servicetype");
		}
		if(map.containsKey("description")){
			desc = map.get("description");
		}
		if(map.containsKey("image")){
			image = map.get("image");
		}
		if(map.containsKey("pricingmodel")){
			pricingModel = map.get("pricingmodel");
		}
		if(map.containsKey("price")){
			price = map.get("price");
		}
		if(map.containsKey("channelprice")){
			channelPrice = map.get("channelprice");
		}
		if(map.containsKey("serviceid")){
			channelId = map.get("serviceid");
		}
		if(map.containsKey("channelname")){
			channelname = map.get("channelname");
		}
		if(map.containsKey("starttime")){
			startTime = map.get("starttime");
		}
		if(map.containsKey("duration")){
			map.get("duration");
		}
		if(map.containsKey("urllink")){
			sourceUrl = map.get("urllink");
		}
		if (channelType.equalsIgnoreCase("live")) {
			getLiveEventId(channelId, title, startTime);
		}else{
			setMenuBasedOnEvent();
		}
		
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		click = "itemclick";
		try {
			if (maingrid_select_index != position){      
				if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
				{
					Log.w(TAG, "onItemClick().position is ::"+position);
					currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
					currentTextView.setTextColor(getResources().getColor(R.color.white));
				}
				maingridview_focus = v;
				currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
				currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
				currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
				currentTextView.setTextColor(getResources().getColor(R.color.pink));
			}else if(maingridview_focus != null){
				Log.w(TAG, "onItemClick().position is ::"+position);
				currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
				currentTextView.setTextColor(getResources().getColor(R.color.white));
				maingridview_focus = v;
				currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
				currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
			}
			maingrid_focus_index = position;
			map = itemList.get(position);
			if(map != null){
				
				if(Constant.DEBUG) System.out.println("\n"+map);
				if(map.containsKey("id")){
					selectedUniqueId =map.get("id");
				}
				if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
					title = map.get(ScreenStyles.LIST_KEY_TITLE);
				}
				if(map.containsKey("servicetype")){
					channelType = map.get("servicetype");
				}
				if(map.containsKey("serviceid")){
					channelId = map.get("serviceid");
				}
				if(map.containsKey("description")){
					desc = map.get("description");
				}
				if(map.containsKey("image")){
					image = map.get("image");
				}
				if(map.containsKey("category")){
				}
				if(map.containsKey("urllink")){
					sourceUrl  = map.get("urllink");
				}
				if(map.containsKey("starttime")){
					startTime = map.get("starttime");
				}
				if(map.containsKey("pricingmodel")){
					pricingModel = map.get("pricingmodel");
				}
				if(map.containsKey("price")){ 
					price = map.get("price"); 
				}
				if(map.containsKey("channelname")){
					channelname = map.get("channelname");
				}
				
				if (channelType.equalsIgnoreCase("live")) {
					getLiveEventId(channelId, title, startTime);
				}else{
					setMenuBasedOnEvent();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	private void getLiveEventId(String id,String eventName,String starttime){
		if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("id", id);
			list.put("name", eventName);
			list.put("starttime", starttime);
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Search");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			String method = "com.port.apps.search.Search.getLiveEventId";
			new AsyncDispatch(method, dispatchHashMap,true).execute();
		}else{
			
			if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
				AuthDialog.authDialog(mSearchInstance);
			}			
			Layout.progressDialog = new ProgressDialog(Search.this,R.style.MyTheme);
			Layout.progressDialog.setCancelable(true);
			Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
			Layout.progressDialog.show();
			
			Intent intent = new Intent(Search.this,SearchService.class);
			intent.putExtra("handler", "com.port.apps.search.Search.getChannelStatus");
			intent.putExtra("channelid", channelId);
			startService(intent);
		}
	}
	
	
	public LinearLayout.LayoutParams getLinearLauoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}
	
	public void showSearchFilterOptions(final String searchKey) {
		if(Constant.DEBUG)  Log.d(TAG , "showSerachFilterOptions() filterList Size: "+filterList.size());
		if(mSearchInstance != null)		{
			mSearchInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					OverLay overlay = new OverLay(mSearchInstance);
					final Dialog listoverlay= overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_LIST, "Category", null, filterList,null,isLukupSelected);
					listoverlay.show();
					ListView ovelaylistItem = (ListView) listoverlay.findViewById(R.id.overlayList);
					ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
					if(ovelaylistItem != null){
						ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) ovelaylistItem.getLayoutParams();
						if(customLayout ==null){
							customLayout = new CustomLayout(mSearchInstance);
						}
						params.height = customLayout.getConvertedHeight(300);
						ovelaylistItem.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
								HashMap<String, String> gettinglist = filterList.get(arg2);
								if(gettinglist.containsKey(ScreenStyles.LIST_KEY_TITLE)){
									category = gettinglist.get(ScreenStyles.LIST_KEY_TITLE);
									category = category.replaceAll("\\s+","");
								}
								try{
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("type", "search");
									list.put("category", category);
									list.put("keyword", searchKey);
									list.put("caller", "com.player.apps.Search");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String method = "com.port.apps.search.Search.getSearchData"; 
									new AsyncDispatch(method, dispatchHashMap,true).execute();
									if(listoverlay != null && listoverlay.isShowing())
										listoverlay.dismiss();
								}catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						});
					}
					if(closeBtn != null){
						closeBtn.setOnClickListener(new OverlayCancelListener(listoverlay));
					}
					
					//Helptip
					HelpTip.requestForHelp(mSearchInstance.getResources().getString(R.string.ORGANIZING_CONTENT),
							mSearchInstance.getResources().getString(R.string.SEARCH_MSG2),mSearchInstance);
					
				}
			});
		}
	}
	
	
	private void showSearchList(final JSONObject jsonData){
		if(Constant.DEBUG)  Log.d(TAG , "showSearchList() ");
		if(mSearchInstance != null){
			if(jsonData != null){
				try {
					itemList = new ArrayList<HashMap<String,String>>();
					JSONArray searchJSONArray = Utils.getListFromJSON(jsonData, "eventdata");
					itemList = getSearchList(searchJSONArray);
					mSearchInstance.runOnUiThread(new Runnable() {
						@Override
						public void run() {						
							if(itemList != null && itemList.size() > 0 ){
								scrollAdapter = new ScrollList(mSearchInstance, itemList, "event","Search");
								lukupListView.setAdapter(scrollAdapter);
								refreshListView(lukupListView);	
								
								//Helptip
								HelpTip.requestForHelp(mSearchInstance.getResources().getString(R.string.YOUR_SEARCH_RESULTS),
										mSearchInstance.getResources().getString(R.string.SEARCH_MSG3),mSearchInstance);
								
							}else{
								HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.NO_RESULT), 2000);
							}
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
		}
	}
	
	private void refreshListView(final ListView lukupListView) {
		try {
			if(mSearchInstance != null){
				mSearchInstance.runOnUiThread(new Runnable() {
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
	
	private ArrayList<HashMap<String, String>> getSearchList(JSONArray array) {
		if(Constant.DEBUG) System.out.println("getSearchList()");
		HashMap<String, String> map = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		try{
			if(array != null && array.length() >0){
				for(int i= 0;i<array.length();i++){
					JSONObject obj = array.getJSONObject(i);
					String id = "";
					String name = "";
					String language = "";
					String category = "";
					String genre = "";
					String source = "";
					String channelPrice= "";
					String channelName = "";
					String desc = "";
					String actor = "";
					String pricingModel = "";
					String director = "";
					String price = "";
					String musicdirector = "";
					String producer = "";
					String duration = "";
					String channelType = "";
					String channelId = "";
					String starttime = "";
					
					if(obj.has("programid")){
						id = obj.getString("programid");
					}
					if(obj.has("name")){
						name = obj.getString("name");
					}
					if(obj.has("genre")){
						genre = obj.getString("genre");
					}
					if(obj.has("source")){
						source = obj.getString("source");
					}
					if(obj.has("category")){
						category = obj.getString("category");
					}
					if(obj.has("source")){
						source = obj.getString("source");
					}
					if(obj.has("channelprice")){
						channelPrice = obj.getString("channelprice");
					}
					if(obj.has("channelname")){
						channelName = obj.getString("channelname");
					}
					if(obj.has("description")){
						desc = obj.getString("description");
					}
					if(obj.has("actors")){
						actor = obj.getString("actors");
					}
					if(obj.has("pricingmodel")){
						pricingModel = obj.getString("pricingmodel");
					}
					if(obj.has("director")){
						director = obj.getString("director");
					}
					if(obj.has("price")){
						price = obj.getString("price");
					}
					if(obj.has("musicdirector")){
						musicdirector = obj.getString("musicdirector");
					}
					if(obj.has("producer")){
						producer = obj.getString("producer");
					}
					if(obj.has("duration")){
						duration = obj.getString("duration");
					}
					if(obj.has("channeltype")){
						channelType = obj.getString("channeltype");
					}
					if(obj.has("serviceid")){
						channelId = obj.getString("serviceid");
					}
					if(obj.has("starttime")){
						starttime = obj.getString("starttime");
					}
					
					map = new HashMap<String, String>();
					map.put("id", id);
					map.put(ScreenStyles.LIST_KEY_TITLE,name);
					map.put(ScreenStyles.LIST_KEY_THUMB_URL,R.drawable.defaultimage+"");
					map.put("language", language);
					map.put("urllink", source);
					map.put("category", category);
					map.put("type", "search");
					map.put("channelprice", channelPrice);
					map.put("channelname", channelName);
					map.put("description", desc);
					map.put("pricingmodel", pricingModel);
					map.put("director", director);
					map.put("price", price);
					map.put("event", "true");
					map.put("musicdirector", musicdirector);
					map.put("producer", producer);
					map.put("duration", duration);
					map.put("servicetype", channelType);
					map.put("serviceid", channelId);
					map.put("starttime", starttime);
					list.add(map);
				}
				if(Constant.DEBUG) System.out.println("getSearchList().list "+list.size());
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			return null;
		}
		return null;
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
			if(Constant.DEBUG)  Log.d(TAG , "Showing context menu with channeltype : " + channelType);			
			if(channelType != null && !channelType.equalsIgnoreCase("")){
				if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
					if(channelType.equalsIgnoreCase("live")){
						//Helptip
						HelpTip.requestForHelp(mSearchInstance.getResources().getString(R.string.LIVE_TV_CONTENT),
								mSearchInstance.getResources().getString(R.string.SEARCH_MSG5),mSearchInstance);
						
						if(Constant.DEBUG)  Log.d(TAG ,"Live mServiceType: "+channelType);
						if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
							customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_HIGHTLIGHTED_S;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_HIGHTLIGHTED_S;
							
						}else{
							customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_HIGHTLIGHTED;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_HIGHTLIGHTED;
						}
					}else{
						//Helptip
						HelpTip.requestForHelp(mSearchInstance.getResources().getString(R.string.ON_DEMAND_CONTENT),
								mSearchInstance.getResources().getString(R.string.SEARCH_MSG4),mSearchInstance);
						
						if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
							customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED_S;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED_S;	
						}else{
							customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED;	
						}	
					}
					setMenuBasedOnLike();
					setMenuBasedOnRecord();
					setMenuBasedOnSubscribe();
					if (channelType.equalsIgnoreCase("vod")) {
						setMenuBasedOnPlayon();
					}
					getMenuItemsArray(customMenuValue,customMenuIcons);
				}else { //if app is not connected to the Player
					if(channelType.equalsIgnoreCase("live")){
						//Helptip
						HelpTip.requestForHelp(mSearchInstance.getResources().getString(R.string.LIVE_TV_CONTENT),
								mSearchInstance.getResources().getString(R.string.SEARCH_MSG5),mSearchInstance);
						
						if(Constant.DEBUG)  Log.d(TAG ,"Live mServiceType: "+channelType);
					
						customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_MOBILE_HIGHTLIGHTED;
						customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_MOBILE_HIGHTLIGHTED;
						
					}else{
						//Helptip
						HelpTip.requestForHelp(mSearchInstance.getResources().getString(R.string.ON_DEMAND_CONTENT),
								mSearchInstance.getResources().getString(R.string.SEARCH_MSG4),mSearchInstance);
						
				
						customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_MOBILE_HIGHTLIGHTED;
						customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_MOBILE_HIGHTLIGHTED;	
					}
//					setMenuBasedOnLike();
					setMenuBasedOnSubscribe();
//					setMenuBasedOnRecord();
					getMenuItemsArray(customMenuValue,customMenuIcons);
				}
					
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	private void setMenuBasedOnEvent(){
		
		if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("id", selectedUniqueId);
			list.put("type", pricingModel);
			list.put("channeltype", channelType);
			list.put("caller", "com.player.apps.Search");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			String method = "com.port.apps.search.Search.getEventStatus"; 
			new AsyncDispatch(method, dispatchHashMap,true).execute();
		}else {// if Application is connected to the  player
			
			if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
				AuthDialog.authDialog(mSearchInstance);
			}	
			
			Layout.progressDialog = new ProgressDialog(Search.this,R.style.MyTheme);
			Layout.progressDialog.setCancelable(true);
			Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
			Layout.progressDialog.show();
			
			Intent intent = new Intent(Search.this,SearchService.class);
			intent.putExtra("handler", "com.port.apps.search.Search.getEventStatus");
			intent.putExtra("id",selectedUniqueId );
			intent.putExtra("pricingmodel", pricingModel);
			intent.putExtra("channeltype", channelType);
			startService(intent);
		}		
		
	}
	
	
	/************ Based on like option change the Toggle MenuItem *****************/
	private void setMenuBasedOnLike() {
		try {
			if(islike != null){
				if(customMenuValue != null && islike.equalsIgnoreCase("true")){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("Like")){
							customMenuValue[i] = "UnLike";
							customMenuIcons[i] = R.drawable.v13_ico_dislike_01;
							break;
						}
					}
				}else if(customMenuValue != null){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("UnLike")){
							customMenuValue[i] = "Like";
							customMenuIcons[i] = R.drawable.v13_ico_like_01;
							break;
						}
					}
				}
				if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnLike():"+islike);
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	private void setMenuBasedOnRecord() {
		if(channelType.equalsIgnoreCase("live")){
			try {
				if(isRecord != null){
					if(customMenuValue != null && isRecord.equalsIgnoreCase("true")){
						for(int i=0;i<customMenuValue.length;i++){
							if(customMenuValue[i].equalsIgnoreCase("Record")){
								customMenuValue[i] = "Stop Record";
								customMenuIcons[i] = R.drawable.v13_ico_list_remove;
								break;
							}
						}
					}else if(customMenuValue != null){
						for(int i=0;i<customMenuValue.length;i++){
							if(customMenuValue[i].equalsIgnoreCase("Stop Record")){
								customMenuValue[i] = "Record";
								customMenuIcons[i] = R.drawable.v13_ico_record_01;
								break;
							}
						}
					}
					if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnRecord():"+isRecord);
				}
			}catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
		}
	}
	
	private void setMenuBasedOnSubscribe(){
		try {
			if(isSubscribe != null){
				if(customMenuValue != null && isSubscribe.equalsIgnoreCase("true") && !pricingModel.equalsIgnoreCase("PPV")){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("Subscribe")){
							customMenuValue[i] = "Unsubscribe";
							customMenuIcons[i] = R.drawable.v13_ico_unsubscribe_01;
							break;
						}
					}
				}else if(customMenuValue != null){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("Unsubscribe")){
							customMenuValue[i] = "Subscribe";
							customMenuIcons[i] = R.drawable.v13_ico_subscribe_01;
							break;
						}
					}
				}
				if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnLike():"+islike);
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		
	}
	
	private void setMenuBasedOnPlayon(){
		try {
			if (sourceUrl.trim().contains(".")) {
				extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
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
	
	
	public void processMenuActions(CustomMenuItem key) {
		if(Constant.DEBUG)  Log.d(TAG , "processMenuActions()");
		if(key.getCaption().trim().equalsIgnoreCase("Info")){
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				Info.rInfoAction(jsonInfoDate, mContainer, mSearchInstance,"com.player.apps.Search");
				return;
			}else{ //if app is not connected to the Player
				Info.rInfoAction(searchResults, mContainer, mSearchInstance,"com.player.apps.Search");
				return;
			}
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("AddtoPlaylist") || key.getCaption().toString().trim().equalsIgnoreCase("Playlist")){
			Playlist.requestForAddToPlaylist(selectedItem_Id,"com.player.apps.Search");
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Unsubscribe")){
//			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
				AuthDialog.authDialog(mSearchInstance);
			}else{	
				if(pricingModel.equalsIgnoreCase("PPC")){
					Subscribe.requestForSubscriber(channelId,channelname,"",pricingModel,"unsubscribe","event",mSearchInstance,mContainer,"com.player.apps.Search");
				}
			}
//			}else{ //app is not connected to the Player
//				if(pricingModel.equalsIgnoreCase("PPC")){
//					String Url = Constant.SUBSCRIBE + channelId+"&type=unsubscribe&subscriberid="+DataStorage.getSubscriberId()+"&pricingmodel="+pricingModel+"&adminPassword=Guest"+ "&profile="+DataStorage.getCurrentUserId();
//					new DataFetch(Url).execute();
//				}
//			}
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Subscribe")){
			if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
				AuthDialog.authDialog(mSearchInstance);
			}else{
				if(pricingModel.equalsIgnoreCase("PPV")){
	//				if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						Subscribe.requestForSubscriber(selectedItem_Id,title,price,pricingModel,"subscribe","event",mSearchInstance,mContainer,"com.player.apps.Search");
	//				}else{ // app is not connected to the Player
	//					String Url = Constant.SUBSCRIBE + selectedItem_Id+"&type=subscribe&subscriberid="+DataStorage.getSubscriberId()+"&pricingmodel="+pricingModel+"&adminPassword=Guest"+ "&profile="+DataStorage.getCurrentUserId();
	//					new DataFetch(Url).execute();
	//				}
				}else{
	//				if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						Subscribe.requestForSubscriber(channelId,channelname,channelPrice,pricingModel,"subscribe","service",mSearchInstance,mContainer,"com.player.apps.Search");
	//				}else{ //app is not connected to the Player
	//					String Url = Constant.SUBSCRIBE + channelId+"&type=subscribe&subscriberid="+DataStorage.getSubscriberId()+"&pricingmodel="+pricingModel+"&adminPassword=Guest"+ "&profile="+DataStorage.getCurrentUserId();
	//					new DataFetch(Url).execute();
	//				}
				}
			}
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Like")){
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				Like.requestForLike(selectedItem_Id, key.getCaption().toString().trim(), "event","com.player.apps.Search");
			}else{
//				new Like().FaceBookLogin(this,title,desc,image,pricingModel,sourceUrl);
				if(ShareDialog.canShow(ShareLinkContent.class)){
					ShareLinkContent linkContent = new ShareLinkContent.Builder()
						.setContentTitle(title)
						.setContentDescription(desc)
						.setContentUrl(Uri.parse(sourceUrl))
						.build();
					shareDialog.show(linkContent);
					shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>(){

						@Override
						public void onSuccess(Sharer.Result result) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onError(FacebookException error) {
							// TODO Auto-generated method stub
							HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.SHARE_ERROR), 5000);
						
						}
						
					});

				}
			}
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Unlike")){
			Like.requestForLike(selectedItem_Id, key.getCaption().toString().trim(), "event","com.player.apps.Search");
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Record")){
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				PlanRequest.recordEvent(selectedItem_Id,"com.player.apps.Search");
			}else{
				//to do
			}
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Stop Record")){
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				PlanRequest.StopRecording(selectedItem_Id,"com.player.apps.Search");
			}else{
				//to do
			}
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Remind")){
			PlanRequest.reminderEvent(selectedItem_Id,"com.player.apps.Search");
		}	
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("TV Guide")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Search");
			list.put("called", "startActivity");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
			
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Play On")){
			if(DataStorage.getPlayingUrl() != null && !DataStorage.getPlayingUrl().equalsIgnoreCase("")){
				String isAudio = DataStorage.getPlayingUrl().trim().substring(DataStorage.getPlayingUrl().trim().lastIndexOf("."));
				if(isAudio.trim().equalsIgnoreCase(".mp3")){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Search");
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
				}
			}
			
			extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
			if(extention.trim().equalsIgnoreCase(".mp3")){
				PlayOn.getConnectedDevices(selectedItem_Id,"audio","com.player.apps.Search",mSearchInstance);
			}else if(extention.trim().equalsIgnoreCase(".mp4")){
				//PlayOn.getRemoteDisplays(selectItemId,"com.player.apps.Guide");
				Intent intent = new Intent (Search.this,DlnaRenderer.class);
				intent.putExtra("sourceUrl", sourceUrl);
				startActivity(intent);
			}else {
				HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.PLAY_ERROR), 5000);
			}
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Stop")){
			extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
			if(extention.trim().equalsIgnoreCase(".mp3")){
				PlayOn.requestForStop("com.player.apps.Search");
				DataStorage.setA2dpDevice("");
			}else{
				PlayOn.stopWifiDisplay("com.player.apps.Search");
				DataStorage.setWifiDisplayDevice("");
			}
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Download")){
			//to do
			
		}
				
	}
	
	private void playVideo(String servicetype, String serviceid, String eventid,String url){
		HelpTip.close(mSearchInstance);
		if(servicetype.equalsIgnoreCase("live")&& Constant.DVB){
			Intent play = new Intent(Search.this, TVRemote.class);
			play.putExtra("ActivityName", "Search");
			startActivity(play);
			
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("type", servicetype);
			list.put("id", eventid);
			list.put("serviceid", serviceid);
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Search");
			list.put("activity", "Search");
			if(Constant.DEBUG)  Log.d(TAG , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
			list.put("called", "startActivity");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.epg.Play.PlayOn", dispatchHashMap,true).execute();
			
		}else{
			if(url != null && !url.equalsIgnoreCase("")){
				Layout.progressDialog = new ProgressDialog(Search.this,R.style.MyTheme);
				Layout.progressDialog.setCancelable(true);
				Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
				Layout.progressDialog.show();
				
				Intent play = new Intent(Search.this, PlayBack.class);
				play.putExtra("ActivityName", "Search");
				play.putExtra("serviceid", serviceid);
				play.putExtra("Type", servicetype);
				play.putExtra("EventId", eventid);
				if(url != null){
					play.putExtra("EventUrl", url);
				}
				startActivity(play);
			}else{
				HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.UNABLE_TO_PLAY), 2000);
			}
		}
	}
	
/************************************************************************************************/
	
	public final BroadcastReceiver mSearchReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String jsondata = "";
			String handler = "";
			JSONObject objData = null;
			if(extras != null){
				if(extras.containsKey("Params")){
					jsondata = extras.getString("Params");
					if(extras.containsKey("Handler")){
						handler = extras.getString("Handler");
					}
					if(Constant.DEBUG)  Log.d(TAG , "mSearchReceiver  "+handler+"  param  "+jsondata);
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
	
	public void processUIData(final String handler,final JSONObject jsonData){
		try {
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.dismiss();
			}
			if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.search.Search.getSearchData")){
				if(mSearchInstance != null){
					if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has handler "+handler);
					if(jsonData != null){
						try {if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has jsonData "+jsonData);
						if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has type " +jsonData.has("type"));
						if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has result " +jsonData.has("result"));
							if(jsonData.has("result")){
								if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>. result "+jsonData.getString("result"));
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>. result "+jsonData.getString("result"));
									showSearchList(jsonData);
									searchResults = jsonData;
								}	if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has result "+jsonData.getString("result"));
							}	if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>. result "+jsonData.getString("result"));
						} catch (Exception e) {
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.search.Search.getEventStatus")){
				if(mSearchInstance != null){
					if(jsonData != null){
						try {if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has ");
							if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									jsonInfoDate = jsonData;
									if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has success");
									if(jsonData.has("like")){
										islike = jsonData.getString("like");
									}
									if(jsonData.has("subscribe")){
										isSubscribe = jsonData.getString("subscribe");
									}
									if(jsonData.has("record")){
										isRecord= jsonData.getString("record");
									}
									if(jsonData.has("lock")){
										isLock = jsonData.getString("lock");
									}
									if(jsonData.has("id")){
										selectedItem_Id = jsonData.getString("id");
									}
									Log.d("Search", "getEventStatus===selectedItem_Id "+selectedItem_Id + " isSubscribe" + isSubscribe);
									if(click.equalsIgnoreCase("longclick")){
										showCustomMenu();
									} else {
										if(selectedItem_Id != null && !selectedItem_Id.equalsIgnoreCase("")){
											if(channelType.equalsIgnoreCase("vod")){				
												if(Utils.checkNullAndEmpty(isLock) && !isLock.equalsIgnoreCase("true")){
													if(isSubscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
														playVideo(channelType, channelId, selectedItem_Id, sourceUrl);
													}else{
														if (pricingModel.equalsIgnoreCase("PPV")) {
															HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+title +" "+
																	mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
														}else if (pricingModel.equalsIgnoreCase("PPC")) {
															HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+channelname +" "+
																	mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
														}
													}
												}else{
													HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
												}
											}else{
												playVideo(channelType, channelId, selectedItem_Id, sourceUrl);
											}
										}else{
											HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.UNABLE_TO_PLAY), 2000);
										}
									}
								}
							}else if(jsonData.getJSONObject("json").getString("result").equalsIgnoreCase("success")){
								jsonInfoDate = jsonData;
								if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has success");
								if(jsonData.has("like")){
									islike = jsonData.getString("like");
								}
								if(jsonData.has("subscribe")){
									isSubscribe = jsonData.getString("subscribe");
								}
								if(jsonData.has("record")){
									isRecord= jsonData.getString("record");
								}
								if(jsonData.has("lock")){
									isLock = jsonData.getString("lock");
								}
								if(jsonData.has("id")){
									selectedItem_Id = jsonData.getString("id");
								}
								if(jsonData.has("starttime")){
									startTime = jsonData.getString("starttime");
								}
								if(jsonData.has("startdate")){
									startDate = jsonData.getString("startdate");
								}
								if(jsonData.has("duration")){
									duration = jsonData.getString("duration");
								}
								Log.d("Search", "getEventStatus===selectedItem_Id "+selectedItem_Id + " isSubscribe" + isSubscribe);
								if(jsonData.getJSONObject("json").has("isSubscribed")){
										isSubscribe = jsonData.getJSONObject("json").getString("isSubscribed");
										if(Constant.DEBUG)  Log.d(TAG , "is Subscribed ? " + isSubscribe);
								}
								if(click.equalsIgnoreCase("longclick")){
									if(Constant.DEBUG)  Log.d(TAG , "long clicked");
									showCustomMenu();
								} else {
									if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has isSubscribe "+isSubscribe + "pricingModel" + pricingModel);
									if(channelType.equalsIgnoreCase("vod")){				
										if(isSubscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
											String extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
											if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.has vod  extention"  +extention + "sourceUrl " + sourceUrl);
											Media.playOnClient(sourceUrl, mContainer, mActivity,channelType,"","","");
										}else{
											if (pricingModel.equalsIgnoreCase("PPV")) {
												HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+title +" "+
														mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
											}else if (pricingModel.equalsIgnoreCase("PPC")) {
												HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+channelname +" "+
														mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
											}
										}
									}else{
										if(isSubscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
											String extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
											if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>not vod.has extention"  +extention + "sourceUrl " + sourceUrl);
											Media.playOnClient(sourceUrl, mContainer, mActivity,channelType,startTime,startDate,duration);
										}else{
											HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+channelname +" "+
													mSearchInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
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
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventInfo")){
				if(mSearchInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									try {
										mSearchInstance.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Info.rInfoAction(jsonData, mContainer, mSearchInstance,"com.player.apps.Search");
											}
										});
									} catch (Exception e2) {
										e2.printStackTrace();
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
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Like")){
				if(mSearchInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									if(islike.equalsIgnoreCase("false")){
										islike = "true";
									}else{
										islike = "false";
									}
									if(jsonData.has("msg")){
										CacheDockData.EventList.clear();
										HelpText.showHelpTextDialog(mSearchInstance,jsonData.getString("msg") , 2000);
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
				}
			}else if (handler.equalsIgnoreCase("com.port.apps.epg.Play.lockStatus")){
				if(mSearchInstance != null){
					if(jsonData.has("state")){
						String state = Utils.getDataFromJSON(jsonData, "state");
						String serviceId = Utils.getDataFromJSON(jsonData, "serviceid");
						if(Constant.DEBUG)  Log.d(TAG , "processUIData serviceId  "+serviceId + " Status: "+DataStorage.isRunningStatus());
						if(state.equalsIgnoreCase("false")){
							if(Utils.getDataFromJSON(jsonData, "type").equalsIgnoreCase("live")){
								int irHandler = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, DataStorage.getConnectedVendor());
								for (int i = 0; i < serviceId.length(); i++) {
								    char digit = serviceId.charAt(i);
								    if(Constant.DEBUG)  Log.d(TAG , "number "+digit + " operator : " + DataStorage.getConnectedVendor());
								    IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), String.valueOf(digit)), irHandler, DataStorage.getConnectedVendor());
								}
							}
						}else{
							HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Devices.getOutputDeviceList")){	// Bold
				if(mSearchInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									PlayOn.showConnectedDevices(jsonData, selectedItem_Id,mSearchInstance, customLayout, "BT","com.player.apps.Search");											
								}else{
									HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
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
			} else if (handler.equalsIgnoreCase("com.port.apps.epg.Devices.getRemoteDisplays")){
				if(mSearchInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						try{
							if(result.equalsIgnoreCase("success")){
								PlayOn.showConnectedDevices(jsonData, selectedItem_Id, mSearchInstance, customLayout, "Wifi", "com.player.apps.Search");
							}else{
								HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
							}
						}catch (Exception e) {
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
					}
				}
			} else if(handler.equalsIgnoreCase("com.port.apps.search.Search.getGenre")){	// Bold
				if(mSearchInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(Constant.DEBUG) Log.d(TAG , "Genre ");
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									JSONArray genre = jsonData.getJSONArray("gerneList");
									if(Constant.DEBUG) Log.d(TAG , "Genre "+genre.length());											
									if(genre != null && genre.length() > 0){
										HashMap<String, String> map = new HashMap<String, String>(); 
										map.put(ScreenStyles.LIST_KEY_TITLE,"All"); 
										filterList.add(map);
										for(int i=0; i<genre.length(); i++){
											map = new HashMap<String, String>();
											String name = genre.getString(i);
											if(Constant.DEBUG) Log.d(TAG , "Genre "+name);													
											map.put(ScreenStyles.LIST_KEY_TITLE,name);	
											filterList.add(map);
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
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.search.Search.getLiveEventId")){	// Bold
				if(mSearchInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									selectedUniqueId = jsonData.getString("id");
									jsonData.getString("serviceid");
									setMenuBasedOnEvent();
								}
							}else{
								if (Constant.DEBUG)	Log.d(TAG, "result :" + jsonData.getString("result"));
							}
						}catch (Exception e) {
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
					}
				}
			}else if (handler.equalsIgnoreCase("com.port.apps.epg.Attributes.PlayList")){
				if(mSearchInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							
							if(jsonData.has("state")){
								if (Utils.getDataFromJSON(jsonData, "state").equalsIgnoreCase("added")){
									HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.ADDED), 2000);
								}
							}
						}
						
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.setReminder")){
				if(mSearchInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							if(jsonData.has("msg")){
								try {
									CacheDockData.ReminderEventList.clear(); //clean cache
									HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
								} catch (JSONException e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						}else{
							if(jsonData.has("msg")){
								try {
									HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
								} catch (JSONException e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
							}
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.setRecord")){
				if(mSearchInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							if(jsonData.has("msg")){
								try {
									if(isRecord.equalsIgnoreCase("false")){
										isRecord = "true";
									}else{
										isRecord = "false";
									}
									
									CacheDockData.RecordEventList.clear(); //clean cache
									HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
								} catch (JSONException e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						}else{
							if(jsonData.has("msg")){
								try {
									HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
								} catch (JSONException e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.stopRecord")){
				if(mSearchInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							try {
								HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
							}catch (Exception e) {
								e.printStackTrace();
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
							}
						}else{
							HelpText.showHelpTextDialog(mSearchInstance, mSearchInstance.getResources().getString(R.string.SOMTHING_WRONG), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Subscriptions")){
				if(mSearchInstance != null){
					try{
						if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
							if(jsonData.has("result")){
								String result = Utils.getDataFromJSON(jsonData, "result");
								
								if(result.equalsIgnoreCase("success")){
									if(jsonData.has("id")){
										HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 2000);
									}
								}else{
									if(jsonData.has("msg")){
										HelpText.showHelpTextDialog(mSearchInstance, jsonData.getString("msg"), 4000);
									}
								}
							}
						}else{ //if Lukup Player app is not connected to the device
							if(jsonData.has("data")){
								JSONObject response = jsonData.getJSONObject("data");
								if(response.has("result")){
									String result = Utils.getDataFromJSON(response, "result");
									
									if(result.equalsIgnoreCase("success")){
										if(response.has("msg")){
											HelpText.showHelpTextDialog(mSearchInstance, response.getString("msg"), 4000);
										}
									}else{
										if(response.has("msg")){
											HelpText.showHelpTextDialog(mSearchInstance, response.getString("msg"), 4000);
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
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")+ " ismenu showing" +mMenu.isShowing());
			if (mMenu.isShowing()){
				hideMenu();return true;
			}else if(DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_INFO_SCREEN)){
				SearchResult();
				showSearchList(searchResults);
				return true;
			}else{
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(Search.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Search");
				startActivity(lukup);
				finish();
				return true;
			}
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(Search.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Search");
				startActivity(lukup);
				finish();
				return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_MENU ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			showCustomMenu();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            String result=data.getStringExtra("result");
	            if(result.equalsIgnoreCase("finish")){
	            	finish();
	            }
	        }
	    }
	    callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	private int MoneyConverter(String val){
		int cost = 0;
		if(!val.equalsIgnoreCase("")){
			cost = (int) Double.parseDouble(val);
		}
		return cost;
	}
	
	
	public HashMap<String, String> infoDatacollection(JSONObject objInfo , int index){
		
		if(Constant.DEBUG) System.out.println("getSearchList()");
		HashMap<String, String> map = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		try{
			JSONArray searchJSONArray = Utils.getListFromJSON(objInfo, "eventdata");
				JSONObject obj = searchJSONArray.getJSONObject(index);
				String id = "";
				String name = "";
				String language = "";
				String category = "";
				String genre = "";
				String source = "";
				String channelPrice= "";
				String channelName = "";
				String desc = "";
				String actor = "";
				String pricingModel = "";
				String director = "";
				String price = "";
				String musicdirector = "";
				String producer = "";
				String duration = "";
				String channelType = "";
				String channelId = "";
				String starttime = "";
				
				if(obj.has("programid")){
					id = obj.getString("programid");
				}
				if(obj.has("name")){
					name = obj.getString("name");
				}
				if(obj.has("genre")){
					genre = obj.getString("genre");
				}
				if(obj.has("source")){
					source = obj.getString("source");
				}
				if(obj.has("category")){
					category = obj.getString("category");
				}
				if(obj.has("source")){
					source = obj.getString("source");
				}
				if(obj.has("channelprice")){
					channelPrice = obj.getString("channelprice");
				}
				if(obj.has("channelname")){
					channelName = obj.getString("channelname");
				}
				if(obj.has("description")){
					desc = obj.getString("description");
				}
				if(obj.has("actors")){
					actor = obj.getString("actors");
				}
				if(obj.has("pricingmodel")){
					pricingModel = obj.getString("pricingmodel");
				}
				if(obj.has("director")){
					director = obj.getString("director");
				}
				if(obj.has("price")){
					price = obj.getString("price");
				}
				if(obj.has("musicdirector")){
					musicdirector = obj.getString("musicdirector");
				}
				if(obj.has("producer")){
					producer = obj.getString("producer");
				}
				if(obj.has("duration")){
					duration = obj.getString("duration");
				}
				if(obj.has("channeltype")){
					channelType = obj.getString("channeltype");
				}
				if(obj.has("serviceid")){
					channelId = obj.getString("serviceid");
				}
				if(obj.has("starttime")){
					starttime = obj.getString("starttime");
				}
				
				map = new HashMap<String, String>();
				map.put("id", id);
				map.put(ScreenStyles.LIST_KEY_TITLE,name);
				map.put(ScreenStyles.LIST_KEY_THUMB_URL,R.drawable.defaultimage+"");
				map.put("language", language);
				map.put("urllink", source);
				map.put("category", category);
				map.put("type", "search");
				map.put("channelprice", channelPrice);
				map.put("channelname", channelName);
				map.put("description", desc);
				map.put("pricingmodel", pricingModel);
				map.put("director", director);
				map.put("price", price);
				map.put("event", "true");
				map.put("musicdirector", musicdirector);
				map.put("producer", producer);
				map.put("duration", duration);
				map.put("servicetype", channelType);
				map.put("serviceid", channelId);
				map.put("starttime", starttime);
			
			if(Constant.DEBUG) System.out.println("getSearchList().list "+list.size());
			return map;
			
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			return null;
		}
	}
	
	
}
