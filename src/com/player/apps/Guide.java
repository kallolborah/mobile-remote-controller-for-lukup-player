package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.player.R;
import com.player.Layout;
import com.player.action.AuthDialog;
import com.player.action.DlnaRenderer;
import com.player.action.Download;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.action.Like;
import com.player.action.Lock;
import com.player.action.Media;
import com.player.action.PlanRequest;
import com.player.action.PlayOn;
import com.player.action.Playlist;
import com.player.action.Subscribe;
import com.player.network.ir.IRTransmitter;
import com.player.service.BouquetInfo;
import com.player.service.CacheDockData;
import com.player.service.ChannelInfo;
import com.player.service.ProgramInfo;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.OperatorKeys;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.webservices.Cloudstorage;
import com.player.webservices.EPG;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
import com.player.widget.ScrollList;


public class Guide extends Layout implements OnItemClickListener,OnItemLongClickListener{
	
	private Guide mGuideInstance;
	private static String TAG = "Guide";
	private Bundle instanceState;
	private ListView lukupListView;
	private LinearLayout.LayoutParams standParams;
	private ArrayList<HashMap<String,String>> bouquetList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> serviceList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> eventList = new ArrayList<HashMap<String,String>>();
	
	private ArrayList<HashMap<String,String>> collectionList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> collectionEventList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> collectionPmgList = new ArrayList<HashMap<String, String>>();
//	private ArrayList<HashMap<String,String>> scrolldataList ; //0812
	@SuppressWarnings("unused")
	private ScrollList scrollAdapter = null;
	private ScrollList bouquetAdapter =null;
	private static boolean isScroll=false;
	private int index = 0;
	private int limit = 200;
	private String selectItemId = "";
	private String selectTitle = "";
	private String desc = "";
	private String image = "";
	private String selectChannel = "";
	private String isLock = "false";
	private String starttime = "";
	private String startdate = "";
	private String duration = "";
	private String isLike = "false";
	private String isRecord = "false";
	private static String isSubscribe = "false";
	private String ServiceType;
	private String ServiceId;
	private String type = "";
	private String sourceUrl = "";
	private static String pricingModel = "";
	private String price;
	private String category;
	private static String selectedBouquet;
	private String selectedService;
	private String selectedCollection;
	private String extention = "";
	private int maingrid_select_index = -1;
	private int maingrid_focus_index = -1;
	private View maingridview_focus = null;
	RelativeLayout currentlayoutMain;
	TextView currentTextView;
	private int currentPosition = -1;
	
	private HttpURLConnection urlConnection;
	
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	
	private String method = "com.port.apps.epg.Guide.sendBouquetList";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		if(Constant.DEBUG)  Log.d(TAG , "onCreate() ");
		mGuideInstance = this;
		instanceState = savedInstanceState;
		if(Constant.DEBUG)  Log.d(TAG , "init() CacheDockData.BouquetList.size(): "+CacheDockData.BouquetList.size());
		
		IntentFilter guide = new IntentFilter("com.player.apps.Guide");
		registerReceiver(mGuideReceiver,guide); 
		
		IntentFilter device = new IntentFilter("pairedA2DP");
		registerReceiver(mGuideReceiver,device);	
		
		if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
			if(Constant.DEBUG)Log.d(TAG, "Remote Connected"+mDataAccess.getIsRemoteConnected());
			if(CacheDockData.BouquetList.size()>0){
				bouquetList = ArrayListOfBouquet(CacheDockData.BouquetList);
			}else {
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("start", index+"");
				list.put("limit", limit+"");
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Guide");
				list.put("called", "startService");
				dispatchHashMap.add(list);
				new AsyncDispatch(method, dispatchHashMap,true).execute();
			}
			show(false);
		}else { //if app is not connected to the Player
			if(Constant.DEBUG)Log.d(TAG, "Remote Not Connected"+mDataAccess.getIsRemoteConnected());
			
			if(CommonUtil.isNetworkConnected(mActivity)){
				show(true);
				if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
					AuthDialog.authDialog(mGuideInstance);
				}
			}else{
				HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
			}
		}
		
	}
	
	private void show(boolean t){
		if(t){
			Layout.progressDialog = new ProgressDialog(Guide.this,R.style.MyTheme);
			Layout.progressDialog.setCancelable(true);
			Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
			Layout.progressDialog.show();
			
			Intent account = new Intent(Guide.this, EPG.class);
			account.putExtra("subscriberid", mDataAccess.getSubscriberID());
			account.putExtra("handler", "com.player.webservices.getAccountInfo");
			startService(account);
			
			Intent bouquet = new Intent(Guide.this, EPG.class);
			bouquet.putExtra("handler", "com.player.webservices.fetchBouquet");
			startService(bouquet);
		}
		LayoutInflater  inflater = mGuideInstance.getLayoutInflater();
		View listView = inflater.inflate(R.layout.guidelist,null);
		mContainer.addView(listView,getLinearLayoutParams());
		lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
		/*bouquetAdapter*/scrollAdapter = new ScrollList(mGuideInstance, bouquetList, "bouquet","Guide");
		lukupListView.setAdapter(scrollAdapter); //0812
		refreshListView(lukupListView);		
		
		lukupListView.setOnItemClickListener(mGuideInstance);
		lukupListView.setOnItemLongClickListener(mGuideInstance);
		//lukupListView.setOnScrollListener(mGuideInstance);
		
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
	}
	
	@Override
	protected void onStart(){
		super.onStart();

	}
	
	@Override
	protected void onSaveInstanceState(Bundle instanceState){
		if(Constant.DEBUG)  Log.d(TAG , "saving state, method : " + method + " position : " + currentPosition);
		instanceState.putString("method", method);
		instanceState.putInt("position", currentPosition);
		instanceState.putSerializable("eventList", eventList);
		instanceState.putSerializable("serviceList", serviceList);
		instanceState.putSerializable("bouquetList", bouquetList);
		super.onSaveInstanceState(instanceState);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume(){
		super.onResume();
//		if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
//			if(Layout.progressDialog.isShowing()){
//				Layout.progressDialog.dismiss();
//				if(Constant.DEBUG)  Log.d(TAG , "progressDialog dismiss()");
//			}
			if(instanceState != null && instanceState.getString("method") != null && !instanceState.getString("method").equalsIgnoreCase("") && instanceState.getInt("position")!=-1){
				currentPosition = instanceState.getInt("position");
				eventList = (ArrayList<HashMap<String, String>>) instanceState.getSerializable("eventList");
				serviceList = (ArrayList<HashMap<String, String>>) instanceState.getSerializable("serviceList");
				bouquetList = (ArrayList<HashMap<String, String>>) instanceState.getSerializable("bouquetList");
				showGuide(currentPosition);
			}
			if(Constant.DEBUG)  Log.d(TAG , "restoring state : currentPosition -> " + currentPosition);
//		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		try {
			Intent intent = new Intent(this, Class.forName("com.player.apps.GuideService"));
			startService(intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		
		if(mGuideReceiver != null){
			mGuideInstance.unregisterReceiver(mGuideReceiver);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,long arg3) {
		if(Constant.DEBUG)  Log.d(TAG, "OnItemClickListener(). method is ::"+method);
		if (maingrid_select_index != position){      
			if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
			{
				if(Constant.DEBUG)  Log.w(TAG, "OnItemClickListener().position is ::"+position);
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
		currentPosition = position;
		try{
			hideMenu();
			if(Constant.DEBUG)  Log.d(TAG ,"Long click");
			if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendBouquetList")){	
				HashMap<String, String> map = bouquetList.get(position);
				if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
					selectItemId= map.get(ScreenStyles.LIST_KEY_ID);
				}
				selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
				ServiceType = "None";
				type = "Bouquet";
				if(Constant.DEBUG)  Log.d("onItemLongClick()","Bouquet selectItemId: "+selectItemId+ ",Changed method: "+method);
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){
				HashMap<String, String> map = serviceList.get(position);
				if(Constant.DEBUG)  Log.d(TAG ,"service list position " + position);
				if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
					selectItemId= map.get(ScreenStyles.LIST_KEY_ID);
				}else{
					selectItemId = "";
				}
				if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
					selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
				}else{
					selectTitle = "";
				}
				if(map.containsKey("lock")){
					isLock = map.get("lock");
				}else{
					isLock = "";
				}
				if(map.containsKey("like")){
					isLike = map.get("like");
				}else{
					isLike = "";
				}
				if(map.containsKey("subscribe")){
					isSubscribe =map.get("subscribe");
				}else{
					isSubscribe = "";
				}
				if(map.containsKey("servicetype")){
					ServiceType = map.get("servicetype");
				}else{
					ServiceType = "";
				}
				if(map.containsKey("pricingmodel")){
					pricingModel = map.get("pricingmodel");
				}else{
					pricingModel = "";
				}
				if(map.containsKey(ScreenStyles.LIST_KEY_PRICE)){
					price = map.get(ScreenStyles.LIST_KEY_PRICE);
				}else{
					price = "";
				}
				if(map.containsKey("category")){
					category = map.get("category");
				}else{
					category = "";
				}
				if(map.containsKey("image")){
					image = map.get("image");
				}else{
					image = "";
				}
				if(map.containsKey("description")){
					desc = map.get("description");
				}else{
					desc = "";
				}
				type = "service";
				
				if(Constant.DEBUG)  Log.d(TAG,"Service name : "+ selectTitle+ ", price : "+price + " ,id : " + selectItemId);
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
				HashMap<String, String> map = eventList.get(position);
				if(Constant.DEBUG)  Log.d(TAG ,"event list position " + position);
				if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
					selectItemId= map.get(ScreenStyles.LIST_KEY_ID);
				}else{
					selectItemId = "";
				}
				if(map.containsKey("record")){
					isRecord = map.get("record");
				}else{
					isRecord = "";
				}
				if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
					selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
				}else{
					selectTitle = "";
				}
				if(map.containsKey("lock")){
					isLock = map.get("lock");
				}else{
					isLock = "";
				}
				if(map.containsKey("like")){
					isLike = map.get("like");
				}else{
					isLike = "";
				}
				if(map.containsKey("subscribe")){
					isSubscribe =map.get("subscribe");
				}else{
					isSubscribe = "";
				}
				if(map.containsKey("servicetype")){
					ServiceType = map.get("servicetype");
				}else{
					ServiceType = "";
				}
				if(map.containsKey("serviceid")){
					ServiceId = map.get("serviceid");
				}else{
					ServiceId = "";
				}
				if(map.containsKey("pricingmodel")){
					pricingModel = map.get("pricingmodel");
				}
//				else{
//					pricingModel = "";
//				}
				if(map.containsKey(ScreenStyles.LIST_KEY_PRICE)){
					price = map.get(ScreenStyles.LIST_KEY_PRICE);
				}else{
					price = "";
				}
				if(map.containsKey("category")){
					category = map.get("category");
				}else{
					category = "";
				}
				if(map.containsKey("urllink")){
					sourceUrl = map.get("urllink");
				}else{
					sourceUrl = "";
				}
				if(map.containsKey("image")){
					image = map.get("image");
				}else{
					image = "";
				}
				if(map.containsKey("description")){
					desc = map.get("description");
				}else{
					desc = "";
				}
				if(map.containsKey("starttime")){
					starttime = map.get("starttime");
				}else{
					starttime = "";
				}
				if(map.containsKey("startdate")){
					startdate = map.get("startdate");
				}else{
					startdate = "";
				}
				if(map.containsKey("duration")){
					duration = map.get("duration");
				}else{
					duration = "";
				}
				type = "event";
				if(Constant.DEBUG)  Log.d(TAG,"Event name : "+selectTitle+ ",price: "+price + " ,id : " + selectItemId);
			}
			showCustomMenu();
		}catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		if(Constant.DEBUG)  Log.d(TAG, "onItemClick(). method is ::"+method);
		if (maingrid_select_index != position){      
			if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
			{
				if(Constant.DEBUG)  Log.w(TAG, "OnItemClickListener().position is ::"+position);
				currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
				currentTextView.setTextColor(getResources().getColor(R.color.white));
			}
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
			currentTextView.setTextColor(getResources().getColor(R.color.pink));
		}else if(maingridview_focus != null){
			if(Constant.DEBUG)  Log.w(TAG, "OnItemClickListener().position is ::"+position);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
			currentTextView.setTextColor(getResources().getColor(R.color.white));
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
		}
		maingrid_focus_index = position;
		currentPosition = position;
		
		hideMenu();
		
		showGuide(currentPosition);
	}
	
	private void showGuide(int position){
		try{
			if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendBouquetList")){	
				if(Constant.DEBUG)  Log.d(TAG,"Bouquet item clicked " + method);
				HashMap<String, String> map= bouquetList.get(position);
				if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
					selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
				}
				if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
					selectItemId = map.get(ScreenStyles.LIST_KEY_ID);
				}
				index = 0;
				selectedBouquet = selectItemId;
	
				ArrayList<ChannelInfo> ChlList = new ArrayList<ChannelInfo>();
				if(Constant.DEBUG)  Log.d(TAG,"onItemClick(). Local Cache ServiceList size: "+CacheDockData.ServiceList.size());
				if(CacheDockData.ServiceList.size()>0){
					for (int i = 0; i < CacheDockData.ServiceList.size(); i++) {
						if(CacheDockData.ServiceList.get(i).getChannelBouquetId().equalsIgnoreCase(selectItemId)){
							ChlList.add(CacheDockData.ServiceList.get(i));
						}
					}
					
					if(ChlList.size() > 0){
						if(Constant.DEBUG)  Log.d(TAG,"onItemClick(). Local Cache ChlList size: "+ChlList.size());
						serviceList = ArrayListOfService(ChlList);
						lukupListView.invalidate();
						lukupListView.setAdapter(new ScrollList(mGuideInstance, serviceList, "service","Guide"));
						refreshListView(lukupListView);	
						method = "com.port.apps.epg.Guide.sendServiceList";
					}else if(!mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						if(CommonUtil.isNetworkConnected(mActivity)){
							//get Service list from EPG class
							if(Constant.DEBUG)Log.d(TAG, "Remote Not Connected"+mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
							
							progressDialog = new ProgressDialog(Guide.this,R.style.MyTheme);
							progressDialog.setCancelable(true);
							progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
							progressDialog.show();
							
							Intent service = new Intent(Guide.this, EPG.class);
							service.putExtra("subscriberid", mDataAccess.getSubscriberID());
							service.putExtra("handler", "com.player.webservices.fetchServices");
							service.putExtra("bouquetname",selectTitle);
							startService(service);
						}else{
								HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
						}
					}else{
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
						list.put("start", index+"");
						list.put("limit", limit+"");
						list.put("id", selectItemId);
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("caller", "com.player.apps.Guide");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						new AsyncDispatch("com.port.apps.epg.Guide.sendServiceList", dispatchHashMap,true).execute();
					}
				}else if(!mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
					if(CommonUtil.isNetworkConnected(mActivity)){
						//get Service list from EPG class
						if(Constant.DEBUG)Log.d(TAG, "Remote Not Connected"+mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
						
						progressDialog = new ProgressDialog(Guide.this,R.style.MyTheme);
						progressDialog.setCancelable(true);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
						progressDialog.show();
						
						Intent service = new Intent(Guide.this, EPG.class);
						service.putExtra("subscriberid", mDataAccess.getSubscriberID());
						service.putExtra("handler", "com.player.webservices.fetchServices");
						service.putExtra("bouquetname",selectTitle);
						startService(service);
					}else{
						HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
					}
					
				}else{
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("start", index+"");
					list.put("limit", limit+"");
					list.put("id", selectItemId);
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Guide");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Guide.sendServiceList", dispatchHashMap,true).execute();
				}
				if(Constant.DEBUG)  Log.d(TAG,"Bouquet onItemClick().selectItemId: "+selectItemId+", method " + method);
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){	
				if(Constant.DEBUG)  Log.d(TAG,"Service item clicked " + method);
				HashMap<String, String> map= serviceList.get(position);
				if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
					selectItemId= map.get(ScreenStyles.LIST_KEY_ID);
				}else{
					selectItemId = "";
				}
				if(map.containsKey("title")){
					selectTitle = map.get("title");
				}else{
					selectTitle = "";
				}
				if(map.containsKey("lock")){
					isLock = map.get("lock");
				}else{
					isLock = "";
				}
				if(map.containsKey("like")){
					isLike = map.get("like");
				}else{
					isLike = "";
				}
				if(map.containsKey("subscribe")){
					isSubscribe =map.get("subscribe");
				}else{
					isSubscribe = "";
				}
				if(map.containsKey("servicetype")){
					ServiceType = map.get("servicetype");
				}else{
					ServiceType = "";
				}
				if(map.containsKey("pricingmodel")){
					pricingModel = map.get("pricingmodel");
				}else{
					pricingModel = "";
				}
				if(map.containsKey("price")){
					price = map.get("price");
				}else{
					price = "";
				}
				if(map.containsKey("category")){
					category = map.get("category");
				}else{
					category = "";
				}
				if(map.containsKey("image")){
					image = map.get("image");
				}else{
					image = "";
				}
				if(map.containsKey("description")){
					desc = map.get("description");
				}else{
					desc = "";
				}
				index = 0;
				selectedService = selectItemId;
				selectChannel = selectTitle;
				ArrayList<ProgramInfo> pmgList = new ArrayList<ProgramInfo>();
				if(Constant.DEBUG)  Log.d(TAG,"onItemClick(). Local Cache EventList size: "+CacheDockData.EventList.size());
	
				if(pricingModel.equalsIgnoreCase("PPC")){
					isFreeContent(price, pricingModel,"service");
				}
				
//				if(map.containsKey("channelurl")){
//					String url = map.get("channelurl");
//					if(!url.equalsIgnoreCase(""))
//					playVideo(selectItemId, url, "live");
//				}
				if(!isLock.equalsIgnoreCase("true")){
					if(CacheDockData.EventList.size()>0){
						// commented for checking (collection)
						for (int i = 0; i < CacheDockData.EventList.size(); i++) {
							if(CacheDockData.EventList.get(i).getServiceId().equalsIgnoreCase(selectItemId)){
								pmgList.add(CacheDockData.EventList.get(i));
							}
						}
						
						if(pmgList.size() > 0){
							if(Constant.DEBUG)  Log.d(TAG,"onItemClick(). Local Cache pmgList size: "+pmgList.size());
							eventList = ArrayListOfEvent(pmgList);
							lukupListView.invalidate();
							lukupListView.setAdapter(new ScrollList(mGuideInstance, eventList, "event","Guide"));
							refreshListView(lukupListView);	
							method = "com.port.apps.epg.Guide.sendEventList";
						}else if(!mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
							if(CommonUtil.isNetworkConnected(mActivity)){
								//get Event list from EPG class
								if(Constant.DEBUG)Log.d(TAG, "Remote Not Connected"+mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
								
								progressDialog = new ProgressDialog(Guide.this,R.style.MyTheme);
								progressDialog.setCancelable(true);
								progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
								progressDialog.show();
								
								Intent program = new Intent(Guide.this, EPG.class);
								program.putExtra("subscriberid", mDataAccess.getSubscriberID());
								program.putExtra("handler", "com.player.webservices.fetchPrograms");
								program.putExtra("serviceid", selectItemId);
								startService(program);
							}else{
								HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
							}
							
						}else{
							dispatchHashMap  = new ArrayList<HashMap<String,String>>();
							HashMap<String, String> list = new HashMap<String, String>();
							list.put("start", index+"");
							list.put("limit", limit+"");
							list.put("id", selectItemId);
							list.put("consumer", "TV");
							list.put("network",mDataAccess.getConnectionType());
							list.put("caller", "com.player.apps.Guide");
							list.put("called", "startService");
							dispatchHashMap.add(list);
							new AsyncDispatch("com.port.apps.epg.Guide.sendEventList", dispatchHashMap,true).execute();	
						}
					}else if(!mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						if(CommonUtil.isNetworkConnected(mActivity)){
							//get Event list from EPG class
							if(Constant.DEBUG)Log.d(TAG, "Remote Not Connected"+mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
							
							progressDialog = new ProgressDialog(Guide.this,R.style.MyTheme);
							progressDialog.setCancelable(true);
							progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
							progressDialog.show();
							
							Intent program = new Intent(Guide.this, EPG.class);
							program.putExtra("subscriberid", mDataAccess.getSubscriberID());
							program.putExtra("handler", "com.player.webservices.fetchPrograms");
							program.putExtra("serviceid", selectItemId);
							startService(program);
						}else{
							HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
						}
					}else{
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
						list.put("start", index+"");
						list.put("limit", limit+"");
						list.put("id", selectItemId);
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("caller", "com.player.apps.Guide");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						new AsyncDispatch("com.port.apps.epg.Guide.sendEventList", dispatchHashMap,true).execute();			
					}	
				}else{
					HelpText.showHelpTextMessage(mActivity, "This channel is locked. Please unlock.", 4000);
				}
				collectionList.clear();
				collectionEventList.clear();
				collectionPmgList.clear();
				if(Constant.DEBUG)  Log.d(TAG,"Service onItemClick(). selectItemId: "+selectItemId+", method " + method);
				
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendCollectionList")){	
				if(Constant.DEBUG)  Log.d(TAG,"Collection Method: " + method);
				HashMap<String, String> map = collectionList.get(position);
				if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
					selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
				}
				if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
					selectItemId = map.get(ScreenStyles.LIST_KEY_ID);
				}
				if(Constant.DEBUG)  Log.d(TAG,"Collection item clicked: " + selectItemId);
				index = 0;
				type = "collection";
				selectedCollection = selectItemId;
				
				collectionPmgList.clear();
				if(Constant.DEBUG)  Log.d(TAG,"onItemClick().collectionEventList size: "+collectionEventList.size());
				for (int i = 0; i < collectionEventList.size(); i++) {
					HashMap<String, String> collectionMap = collectionEventList.get(i);
					String collectionId = collectionMap.get("collectionid");
					if(Constant.DEBUG)  Log.d(TAG,"onItemClick().collectionId: "+collectionId);
					if(collectionId.equalsIgnoreCase(selectItemId)){
						collectionPmgList.add(collectionEventList.get(i));
						if(Constant.DEBUG)  Log.d(TAG,"onItemClick().collectionPmgList item: "+collectionEventList.get(i));
					}
				}
				if(Constant.DEBUG)  Log.d(TAG,"onItemClick().collectionPmgList size: "+collectionPmgList.size());
				eventList = collectionPmgList;
				
				lukupListView.invalidate();
				lukupListView.setAdapter(new ScrollList(mGuideInstance, eventList, "event","Guide"));
				refreshListView(lukupListView);	
				method = "com.port.apps.epg.Guide.sendEventList";
				
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){	
				if(Constant.DEBUG)  Log.d(TAG,"Event item clicked" + method);
				HashMap<String, String> map = eventList.get(position);
				if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
					selectItemId= map.get(ScreenStyles.LIST_KEY_ID);
				}else{
					selectItemId = "";
				}
				if(map.containsKey("record")){
					isRecord = map.get("record");
				}else{
					isRecord = "";
				}
				if(map.containsKey("ScreenStyles.LIST_KEY_TITLE")){
					selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
				}else{
					selectTitle = "";
				}
				if(map.containsKey("lock")){
					isLock = map.get("lock");
				}else{
					isLock = "";
				}
				if(map.containsKey("like")){
					isLike = map.get("like");
				}else{
					isLike = "";
				}
				if(map.containsKey("subscribe")){
					isSubscribe =map.get("subscribe");
				}else{
					isSubscribe = "";
				}
				if(map.containsKey("servicetype")){
					ServiceType = map.get("servicetype");
				}else{
					ServiceType = "";
				}
				if(map.containsKey("serviceid")){
					ServiceId = map.get("serviceid");
				}else{
					ServiceId = "";
				}
				if(map.containsKey("pricingmodel")){
					pricingModel = map.get("pricingmodel");
				}
//				else{
//					pricingModel = "";
//				}
				if(map.containsKey(ScreenStyles.LIST_KEY_PRICE)){
					price = map.get(ScreenStyles.LIST_KEY_PRICE);
				}else{
					price = "";
				}
				if(map.containsKey("category")){
					category = map.get("category");
				}else{
					category = "";
				}
				if(map.containsKey("urllink")){
					sourceUrl = map.get("urllink");
				}else{
					sourceUrl = "";
				}
				if(map.containsKey("image")){
					image = map.get("image");
				}else{
					image = "";
				}
				if(map.containsKey("description")){
					desc = map.get("description");
				}else{
					desc = "";
				}
				if(map.containsKey("starttime")){
					starttime = map.get("starttime");
				}else{
					starttime = "";
				}
				if(map.containsKey("startdate")){
					startdate = map.get("startdate");
				}else{
					startdate = "";
				}
				if(map.containsKey("duration")){
					duration = map.get("duration");
				}else{
					duration = "";
				}
				index = 0;
				type = "event";
				if(pricingModel.equalsIgnoreCase("PPV")){
					isFreeContent(price, pricingModel,type);
				}
				
				if(Constant.DEBUG)  Log.d(TAG ,"Click eventListScreen().selectItemId: "+selectItemId+", selectChannel: "+selectChannel);
				if(Constant.DEBUG)  Log.d(TAG ,"Click eventListScreen().price: "+price+", isSubscribe: "+isSubscribe+", pricingModel: "+pricingModel);
				if(Constant.DEBUG)  Log.d(TAG ,"Click eventListScreen().ServiceType: "+ServiceType+", sourceUrl: "+sourceUrl);
				if("vod".equalsIgnoreCase(ServiceType)|| "live".equalsIgnoreCase(ServiceType)){				
					if(!isLock.equalsIgnoreCase("true")){
						if(isSubscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
							if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
								if(Constant.DEBUG)  Log.d(TAG,"Is subscribed, so playing " + sourceUrl);
								playVideo(selectItemId, sourceUrl,ServiceType);
							}else{
								if(Constant.DEBUG)  Log.d(TAG,"Is subscribed, so playing " + sourceUrl);
								Media.playOnClient(sourceUrl, mContainer, mActivity,ServiceType, starttime, startdate, duration);
							}
						}else{
							if (pricingModel.equalsIgnoreCase("PPV")) {
								if(Constant.DEBUG)  Log.d(TAG,"PPV not subscribed ");
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+ selectTitle +" "+
										mGuideInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
							}else if (pricingModel.equalsIgnoreCase("PPC")) {
								if(Constant.DEBUG)  Log.d(TAG,"PPC not subscribed ");
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+selectChannel +" "+
										mGuideInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
							}
						}
					}else{
						HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
					}
				}else{
					if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						playVideo(selectItemId, sourceUrl,ServiceType);
					}else{
						if(Constant.DEBUG)  Log.d(TAG,"Going to play personal video " + sourceUrl);
						Media.playOnClient(sourceUrl, mContainer, mGuideInstance,ServiceType,"","","");
					}
				}
				if(Constant.DEBUG)  Log.d(TAG,"Event onItemClick(). selectItemId: "+selectItemId+"method " + method);
			}	
		}catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	private void isFreeContent(String Price,String PricingModel,String type){
		if(price!=null && price.equalsIgnoreCase("")){
			try{
				if(Double.valueOf(Price) == 0){
					if(!mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						//to do
						
					}else{
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
						list.put("model", PricingModel);
						list.put("type", type);
						list.put("id", selectItemId);
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("caller", "com.player.apps.Guide");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						new AsyncDispatch("com.port.apps.epg.Guide.freeContent", dispatchHashMap,false).execute();
					}
				}
			}catch(NumberFormatException e){
				
			}
		}else{
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("model", PricingModel);
			list.put("type", type);
			list.put("id", selectItemId);
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Guide");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.epg.Guide.freeContent", dispatchHashMap,false).execute();
		}
	}

	private void refreshListView(final ListView lukupListView) {
		if(Constant.DEBUG)  Log.d(TAG ,"refreshListView: ");
		DataStorage.setCurrentScreen(ScreenStyles.SHOW_GUIDE);
		try {
			if(mGuideInstance != null){
				mGuideInstance.runOnUiThread(new Runnable() {
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
		}catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	private void playVideo(String id,String url, String type){
		HelpTip.close(mGuideInstance);
		if(Constant.DEBUG)  Log.d(TAG ,"Pre.CurrentScreen: "+DataStorage.getCurrentScreen());
		if(Constant.DEBUG)  Log.d(TAG ,"id: "+id+", url: "+url+", Service type : "+ type);
		
		if(type.equalsIgnoreCase("live")){
			if(url.equalsIgnoreCase("")){ //Added by kallol
				if(Constant.DVB){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("type", type);
					list.put("id", id);
					list.put("serviceid", selectedService);
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Guide");
					list.put("activity", "Guide");
					if(Constant.DEBUG)  Log.d(TAG , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Play.PlayOn", dispatchHashMap,true).execute();
				}else{ //OTT
					Intent play = new Intent(Guide.this, TVRemote.class);
					play.putExtra("ActivityName", "Guide");
					startActivity(play);
				}
			}else{
				/*dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("type", type);
				list.put("id", id);
				list.put("url", url);//Added by Tomesh
				list.put("serviceid", selectedService);
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Guide");
				list.put("activity", "Guide");
				if(Constant.DEBUG)  Log.d(TAG , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
				list.put("called", "startActivity");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.apps.epg.Play.PlayOn", dispatchHashMap,true).execute();*/
				HashMap<String, String> map= eventList.get(currentPosition);
				Intent play = new Intent(Guide.this, PlayBack.class);
				play.putExtra("ActivityName", "Guide");
				play.putExtra("EventData", map);
				play.putExtra("EventId", id);
				play.putExtra("pricingmodel", pricingModel);
				if(url != null){
					play.putExtra("EventUrl", url);
				}
				startActivity(play);
			}
		}else{
			if(url != null && !url.equalsIgnoreCase("")){
				extention = url.trim().substring(url.trim().lastIndexOf("."));
				if (extention.equalsIgnoreCase(".apk")) {
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("url", url);
					list.put("id", id);
					list.put("name", selectTitle);
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Guide");
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.api.interactive.iApp.installGame", dispatchHashMap,false).execute();	
				} else if(extention.equalsIgnoreCase(".jpeg") || extention.equalsIgnoreCase(".jpg") || extention.equalsIgnoreCase(".png")){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("type", type);
					list.put("subtype", "image");
					list.put("id", id);
					list.put("url", url);
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Storage");
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					String method = "com.port.apps.epg.Play.PlayOn"; 
					new AsyncDispatch(method, dispatchHashMap,true).execute();
				}else{
					HashMap<String, String> map= eventList.get(currentPosition);
					Intent play = new Intent(Guide.this, PlayBack.class);
					play.putExtra("ActivityName", "Guide");
					play.putExtra("EventData", map);
					play.putExtra("EventId", id);
					play.putExtra("pricingmodel", pricingModel);
					if(url != null){
						play.putExtra("EventUrl", url);
					}
					startActivity(play);
				}
			}else{
				if(Constant.DEBUG)  Log.d(TAG ,"Url is Null ");
			}
		}
	}
	
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}	
	
	
	private ArrayList<HashMap<String, String>> ArrayListOfBouquet(ArrayList<BouquetInfo> bInfo){
		ArrayList<HashMap<String,String>> processList = new ArrayList<HashMap<String, String>>();
		for(int i= 0;i<bInfo.size();i++){
			HashMap<String,String> showMap = new HashMap<String, String>();
			showMap.put(ScreenStyles.LIST_KEY_ID, bInfo.get(i).getBouquetId());
			showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, Integer.toString(R.drawable.v13_ico_folder_01));
			showMap.put(ScreenStyles.LIST_KEY_TITLE, bInfo.get(i).getBouquetName());
			processList.add(showMap);
		}
		return processList;
	}

	
	private ArrayList<HashMap<String, String>> ArrayListOfService(ArrayList<ChannelInfo> chlInfo){
		ArrayList<HashMap<String,String>> processList = new ArrayList<HashMap<String, String>>();
		for(int i= 0;i<chlInfo.size();i++){
			HashMap<String,String> showMap = new HashMap<String, String>();
			showMap.put(ScreenStyles.LIST_KEY_ID, chlInfo.get(i).getId());
			showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, Integer.toString(R.drawable.v13_ico_folder_01));
			showMap.put(ScreenStyles.LIST_KEY_TITLE, chlInfo.get(i).getChannelName());
			showMap.put("like", chlInfo.get(i).getLike());
			showMap.put("lock", chlInfo.get(i).getLock());
			showMap.put("category", chlInfo.get(i).getServiceCategory());
			showMap.put("event", chlInfo.get(i).getIsevent());
			showMap.put("servicetype", chlInfo.get(i).getServiceType());
			showMap.put("subscribe", chlInfo.get(i).getSubscribe());
			showMap.put("pricingmodel", chlInfo.get(i).getPriceModel());
			showMap.put(ScreenStyles.LIST_KEY_PRICE, chlInfo.get(i).getPrice());
			showMap.put("image", chlInfo.get(i).getChannelLogo());
			showMap.put("description", chlInfo.get(i).getDescription());
			processList.add(showMap);
		}
		return processList;
	}
	
	
	private ArrayList<HashMap<String, String>> ArrayListOfEvent(ArrayList<ProgramInfo> pmgInfo){
		ArrayList<HashMap<String,String>> processList = new ArrayList<HashMap<String, String>>();
		for(int i= 0;i<pmgInfo.size();i++){
			HashMap<String,String> showMap = new HashMap<String, String>();
			showMap.put(ScreenStyles.LIST_KEY_ID, pmgInfo.get(i).getEventId());
			showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
			showMap.put(ScreenStyles.LIST_KEY_TITLE, pmgInfo.get(i).getEventName());
			showMap.put("category", pmgInfo.get(i).getEventCategory());
			showMap.put("servicetype", pmgInfo.get(i).getServiceType());
			showMap.put("subscribe", pmgInfo.get(i).getEventSubscribe());
			showMap.put("lock", pmgInfo.get(i).getLock());
			showMap.put("like", pmgInfo.get(i).getLike());
			showMap.put("event", "true");
			showMap.put(ScreenStyles.LIST_KEY_PRICE, pmgInfo.get(i).getEventPrice());
			showMap.put("image", pmgInfo.get(i).getEventImage());
			showMap.put("description", pmgInfo.get(i).getDescription());
			processList.add(showMap);
		}
		return processList;
	}
	
	private List<String> getImageIdsList() {
		List<String> finalList = new ArrayList<String>();
		List<String> imageIds = Arrays.asList(ScreenStyles.IMAGE_IDS.split(","));
		Collections.shuffle(imageIds);
		for(int i=0; i<12; i++) {
			finalList.add(imageIds.get(i));
		}
		return finalList;
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
			if(ServiceType != null && !ServiceType.equalsIgnoreCase("")){
				if(Constant.DEBUG)  Log.d(TAG ,"ServiceType: "+ServiceType+ ", pricingModel: "+pricingModel+ ", Type: "+type);
				if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
					if(ServiceType.equalsIgnoreCase("live")){
						if(type.equalsIgnoreCase("service")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.LIVE_TV_CHANNELS),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG5),mGuideInstance);
							
							customMenuValue = ScreenStyles.MENU_ITEM_IF_SERVICE_LIVE;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_SERVICE_LIVE;
						}else if(type.equalsIgnoreCase("event")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.LIVE_TV_PROGRAMS),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG8),mGuideInstance);
							
							if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
								customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_HIGHTLIGHTED_S;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_HIGHTLIGHTED_S;
							}else{
								customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_HIGHTLIGHTED;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_HIGHTLIGHTED;
							}
						}
					}else if(ServiceType.equalsIgnoreCase("vod")){
						if(type.equalsIgnoreCase("service")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.ON_DEMAND_CHANNELS),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG4),mGuideInstance);
							
							if(pricingModel.equalsIgnoreCase("PPC")){
								customMenuValue = ScreenStyles.MENU_ITEM_IF_SERVICE_VOD;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_SERVICE_VOD;
							}else{
								customMenuValue = ScreenStyles.MENU_ITEM_IF_SERVICE_VOD_NOPPC;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_SERVICE_VOD_NOPPC;
							}
						}else if(type.equalsIgnoreCase("event")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.ON_DEMAND_CONTENT),
									mGuideInstance.getResources().getString(R.string.SEARCH_MSG4),mGuideInstance); // Same msg in Search
							
							if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
								if(pricingModel.equalsIgnoreCase("PPV")){
									customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED_S;
									customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED_S;
								}else{
									customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED_S_NOPPV;
									customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED_S_NOPPV;
								}
							}else{
								if(pricingModel.equalsIgnoreCase("PPV")){
									customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED;
									customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED;
								}else{
									customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED_NOPPV;
									customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED_NOPPV;
								}
							}	
						}
					}else if(ServiceType.equalsIgnoreCase("personal")){
						if(type.equalsIgnoreCase("service")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.YOUR_PERSONAL_CHANNEL),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG6),mGuideInstance);
							
							customMenuValue = ScreenStyles.MENU_ITEM_IF_SERVICE_PERSONAL;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_SERVICE_PERSONAL;
						}else if(type.equalsIgnoreCase("event")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.PERSONAL_CONTENT),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG10),mGuideInstance);
							
							if(category.equalsIgnoreCase("videos")){
								if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
									customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_PERSONAL_AV_HIGHTLIGHTED_S;
									customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_PERSONAL_AV_HIGHTLIGHTED_S;
								}else{
									customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_PERSONAL_AV_HIGHTLIGHTED;
									customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_PERSONAL_AV_HIGHTLIGHTED;
								}
							}else{
								customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_PERSONAL_PHOTO_HIGHTLIGTHED;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_PERSONAL_PHOTO_HIGHTLIGTHED;
							}
						}
					}else{
						//HelpTip
						HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.ORGANIZING_CONTENT),
								mGuideInstance.getResources().getString(R.string.GUIDE_MSG2),mGuideInstance);
						
						customMenuValue = ScreenStyles.BOUQUETS_MENU_LIST_ITEM;
						customMenuIcons = ScreenStyles.BOUQUETS_MENU_ICONS_LIST_ITEM;
					}
					
					setMenuBasedOnLike();
					setMenuBasedOnRecord();
					setMenuBasedOnLock();
					setMenuBasedOnSubscribe();
					if (!ServiceType.equalsIgnoreCase("live")) {
						setMenuBasedOnPlayon();
					}
				}else{ //if mobile app is not connected to the Lukup Player
					if(ServiceType.equalsIgnoreCase("live")){
						if(type.equalsIgnoreCase("service")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.LIVE_TV_CHANNELS),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG5),mGuideInstance);
							
							customMenuValue = ScreenStyles.MENU_ITEM_IF_SERVICE_LIVE_MOBILE;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_SERVICE_LIVE_MOBILE;
						}else if(type.equalsIgnoreCase("event")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.LIVE_TV_PROGRAMS),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG8),mGuideInstance);
							
							customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_HIGHTLIGHTED_MOBILE;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_HIGHTLIGHTED_MOBILE;
						}
					}else if(ServiceType.equalsIgnoreCase("vod")){
						if(type.equalsIgnoreCase("service")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.ON_DEMAND_CHANNELS),
									mGuideInstance.getResources().getString(R.string.GUIDE_MSG4),mGuideInstance);
							
							if(pricingModel.equalsIgnoreCase("PPC")){
								customMenuValue = ScreenStyles.MENU_ITEM_IF_SERVICE_VOD_MOBILE;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_SERVICE_VOD_MOBILE;
							}else{
								customMenuValue = ScreenStyles.MENU_ITEM_IF_SERVICE_VOD_NOPPC_MOBILE;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_SERVICE_VOD_NOPPC_MOBILE;
							}
						}else if(type.equalsIgnoreCase("event")){
							//HelpTip
							HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.ON_DEMAND_CONTENT),
									mGuideInstance.getResources().getString(R.string.SEARCH_MSG4),mGuideInstance); // Same msg in Search
							
							if(pricingModel.equalsIgnoreCase("PPV")){
								customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED_MOBILE;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED_MOBILE;
							}else{
								customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_HIGHTLIGHTED_NOPPV_MOBILE;
								customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_HIGHTLIGHTED_NOPPV_MOBILE;
							}
						}
					}
					
					setMenuBasedOnLike();
					setMenuBasedOnRecord();
					setMenuBasedOnSubscribe();
				}
				getMenuItemsArray(customMenuValue,customMenuIcons);
			}
		}catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	
	
	/************ Based on like option change the Toggle MenuItem *****************/
	private void setMenuBasedOnLike() {
		try {
			if(isLike != null){
				if(customMenuValue != null && isLike.equalsIgnoreCase("true")){
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
				if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnLike():"+isLike);
			}
		}catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	private void setMenuBasedOnRecord() {
//		if(type.equalsIgnoreCase("live")){
			try {
				if(isRecord != null){
					if(customMenuValue != null && isRecord.equalsIgnoreCase("true")){
						for(int i=0;i<customMenuValue.length;i++){
							if(customMenuValue[i].equalsIgnoreCase("Record")){
								customMenuValue[i] = "Cancel Record";
								customMenuIcons[i] = R.drawable.v13_ico_list_remove;
								break;
							}
						}
					}else if(customMenuValue != null){
						for(int i=0;i<customMenuValue.length;i++){
							if(customMenuValue[i].equalsIgnoreCase("Cancel Record")){
								customMenuValue[i] = "Record";
								customMenuIcons[i] = R.drawable.v13_ico_record_01;
								break;
							}
						}
					}
					if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnRecord():"+isRecord);
				}
			}catch(Exception e){
		    	e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		    }
//		}
	}
	
	/************ Based on lock option change the Toggle MenuItem *****************/
	private void setMenuBasedOnLock() {
		try {
			if(isLock != null){
				if(customMenuValue != null && isLock.equalsIgnoreCase("true")){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("Lock")){
							customMenuValue[i] = "UnLock";
							customMenuIcons[i] = R.drawable.v13_ico_unlock_01;
							break;
						}
					}
				}else if(customMenuValue != null){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("UnLock")){
							customMenuValue[i] = "Lock";
							customMenuIcons[i] = R.drawable.v13_ico_lock_01;
							break;
						}
					}
				}
				if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnLock():"+isLock);
			}
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	private void setMenuBasedOnSubscribe(){
		try {
			if(isSubscribe != null){
				if(customMenuValue != null && isSubscribe.equalsIgnoreCase("true")){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("Subscribe") && !pricingModel.equalsIgnoreCase("PPV")){
							customMenuValue[i] = "Unsubscribe";
							customMenuIcons[i] = R.drawable.v13_ico_unsubscribe_01;
							break;
						}
						else if(customMenuValue[i].equalsIgnoreCase("Subscribe")){
							customMenuValue[i] = "Subscribed";
							customMenuIcons[i] = R.drawable.v13_ico_show;
							break;							
						}
					}
				}else if(customMenuValue != null){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("Unsubscribe")){
							customMenuValue[i] = "Subscribe";
							customMenuIcons[i] = R.drawable.v13_ico_subscribe_01;
							break;
						}else if(customMenuValue[i].equalsIgnoreCase("Subscribed")){
							customMenuValue[i] = "Subscribe";
							customMenuIcons[i] = R.drawable.v13_ico_subscribe_01;
							break;
						}
					}
				}
				if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnSubscribe():"+isSubscribe);
			}
		}catch(Exception e){
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
		}catch(Exception e){
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
					}catch(Exception e){
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
		if(Constant.DEBUG)  Log.d(TAG , "processMenuActions() ");
		if(key.getCaption().trim().equalsIgnoreCase("Info")){
			if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				Info.requestForInfoById(selectItemId,"com.player.apps.Guide");
			}else{
				//to do
				try{
					JSONObject jsonData = new JSONObject();
					jsonData.put("id", selectItemId);
					jsonData.put("servicetype", ServiceType);
					jsonData.put("serviceid", selectedService);
					jsonData.put("url", sourceUrl);
					jsonData.put("subscribe", isSubscribe);
					jsonData.put("lock", isLock);
					jsonData.put("like", isLike);
					jsonData.put("price", price);
					jsonData.put("pricingmodel", pricingModel);
//					jsonData.put("channelPrice", value);
					jsonData.put("name", selectTitle);
//					jsonData.put("actors", value);
//					jsonData.put("releasedate", value);
//					jsonData.put("rating", value);
					jsonData.put("image", image);
//					jsonData.put("singers", value);
//					jsonData.put("director", value);
//					jsonData.put("production", value);
					jsonData.put("description", desc);
//					jsonData.put("producer", value);
//					jsonData.put("musicdirector", value);
					Info.rInfoAction(jsonData, mContainer, mGuideInstance,"com.player.apps.Guide");
				}catch(JSONException e){
					
				}
				method = "com.port.apps.epg.Guide.sendEventInfo";
			}
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("AddtoPlaylist") || key.getCaption().toString().trim().equalsIgnoreCase("Playlist")){
			Playlist.requestForAddToPlaylist(selectItemId,"com.player.apps.Guide");
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Unsubscribe")){
			
//			if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
//				AuthDialog.authDialog(mGuideInstance);
//			}else{
				if(type.equalsIgnoreCase("service")){
					if(pricingModel.equalsIgnoreCase("PPC")){
						Subscribe.requestForSubscriber(selectItemId, selectTitle, "", pricingModel, "unsubscribe", type, mGuideInstance, mContainer,"com.player.apps.Guide");
					}
				}else{
					if(pricingModel.equalsIgnoreCase("PPV")){
						Subscribe.requestForSubscriber(selectItemId,selectTitle,"",pricingModel,"unsubscribe",type,mGuideInstance,mContainer,"com.player.apps.Guide");
					}
				}
//			}
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Subscribe")){
			
//			if("".equalsIgnoreCase(mDataAccess.getSubscriberID())){
//				AuthDialog.authDialog(mGuideInstance);
//			}else{
				if(type.equalsIgnoreCase("service")){
					if(pricingModel.equalsIgnoreCase("PPC")){
						Subscribe.requestForSubscriber(selectItemId, selectTitle, price, pricingModel, "subscribe", type, mGuideInstance, mContainer,"com.player.apps.Guide");
						if(Constant.DEBUG)  Log.d(TAG ,"Channel name : "+ selectTitle + " Price : " + price);
					}
				}else{
					if(pricingModel.equalsIgnoreCase("PPV")){
						Subscribe.requestForSubscriber(selectItemId,selectTitle,price,pricingModel,"subscribe",type,mGuideInstance,mContainer,"com.player.apps.Guide");
						if(Constant.DEBUG)  Log.d(TAG ,"Program name : "+ selectTitle + " Price : " + price);
					}
				}
//			}
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Like")){
			if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				Like.requestForLike(selectItemId, key.getCaption().toString().trim(), type,"com.player.apps.Guide");
			}else{
//				new Like().FaceBookLogin(this,title,desc,image,pricingModel,sourceUrl);
				//to do
				if(ShareDialog.canShow(ShareLinkContent.class)){
					ShareLinkContent linkContent = new ShareLinkContent.Builder()
						.setContentTitle(selectTitle)
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
							HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.SHARE_ERROR), 5000);
						}
						
					});
				}
			}
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Unlike")){
			Like.requestForLike(selectItemId, key.getCaption().toString().trim(), type,"com.player.apps.Guide");
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Lock")){
			JSONArray imagelist = new JSONArray(getImageIdsList());
			Lock.getImagePassword(mGuideInstance,imagelist,key.getCaption().toString().trim(), selectItemId,type);
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Unlock")){
			JSONArray imagelist = new JSONArray(getImageIdsList());
			Lock.getImagePassword(mGuideInstance,imagelist,key.getCaption().toString().trim(), selectItemId,type);
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Organise")){
			HelpTip.close(mGuideInstance);
			if(Constant.DEBUG)  Log.d(TAG ,"Pre.CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent org = new Intent(Guide.this, Organise.class);
			org.putExtra("ActivityName", "Guide");
			org.putExtra("Type", type);
			org.putExtra("id", selectItemId);
			startActivityForResult(org, 1);
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("FullScreen")){
			
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("SlideShow")){
			
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Grid")){
			
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Record")){
			if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				PlanRequest.recordEvent(selectItemId,"com.player.apps.Guide");
			}else{
				String userid;
				if(DataStorage.getCurrentUserId().equalsIgnoreCase("")){
					userid = String.valueOf(1000);
				}else{
					userid = DataStorage.getCurrentUserId();
				}
				if(Constant.DEBUG)  Log.d(TAG ,"Scheduling record with ItemId "+ selectItemId + " Service id " + ServiceId + " with Title " + selectTitle + " start Time " + starttime + " with Duration " + duration);
				Intent intent = new Intent(Guide.this , Cloudstorage.class);
				intent.putExtra("handler", "com.port.apps.epg.Plan.setRecord");
				intent.putExtra("eventid", selectItemId);
				intent.putExtra("serviceid", ServiceId);
				intent.putExtra("eventname", selectTitle);
				intent.putExtra("starttime", starttime);
				intent.putExtra("stoptime", getEndTime());
				intent.putExtra("subscriberid", mDataAccess.getSubscriberID());
				intent.putExtra("userid", userid);
				startService(intent);
			}
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Cancel Record")){
			if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				PlanRequest.StopRecording(selectItemId,"com.player.apps.Guide");
			}else{
				String userid;
				if(DataStorage.getCurrentUserId().equalsIgnoreCase("")){
					userid = String.valueOf(1000);
				}else{
					userid = DataStorage.getCurrentUserId();
				}
				if(Constant.DEBUG)  Log.d(TAG ,"Scheduling record with ItemId "+ selectItemId + " Service id " + ServiceId + " with Title " + selectTitle + " start Time " + starttime + " with Duration " + duration);
				Intent intent = new Intent(Guide.this , Cloudstorage.class);
				intent.putExtra("handler", "com.port.apps.epg.Plan.stopRecord");
				intent.putExtra("eventid", selectItemId);
				intent.putExtra("serviceid", ServiceId);
				intent.putExtra("eventname", selectTitle);
				intent.putExtra("starttime", starttime);
				intent.putExtra("stoptime", getEndTime());
				intent.putExtra("subscriberid", mDataAccess.getSubscriberID());
				startService(intent);
			}
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Remind")){
			PlanRequest.reminderEvent(selectItemId,"com.player.apps.Guide");
		}	
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("TV Guide")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Guide");
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
					list.put("caller", "com.player.apps.Guide");
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
				}
			}
			extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
			if(extention.trim().equalsIgnoreCase(".mp3")){
				PlayOn.getConnectedDevices(selectItemId,"audio","com.player.apps.Guide",mGuideInstance);
			}else if(extention.trim().equalsIgnoreCase(".mp4")){
				//PlayOn.getRemoteDisplays(selectItemId,"com.player.apps.Guide");
				Intent intent = new Intent (Guide.this,DlnaRenderer.class);
				intent.putExtra("sourceUrl", sourceUrl);
				startActivity(intent);
			}else {
				HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.PLAY_ERROR), 5000);
			}
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Stop")){
			extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
			if(extention.trim().equalsIgnoreCase(".mp3")){
				PlayOn.requestForStop("com.player.apps.Guide");
				DataStorage.setA2dpDevice("");
			}else{
				PlayOn.stopWifiDisplay("com.player.apps.Guide");
				DataStorage.setWifiDisplayDevice("");
			}
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Download")){
			//to do
			if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("consumer", "TV");
				list.put("url", sourceUrl);
				list.put("name", selectTitle);
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Guide");
				list.put("called", "startActivity");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.apps.Storage.download", dispatchHashMap,false).execute();
			}else{
				mGuideInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(Constant.DEBUG)  Log.d(TAG, "External storage used for download "+ Environment.getExternalStorageDirectory().getPath());
						try{
							Boolean b = new Download().new DataFetch(sourceUrl, Environment.getExternalStorageDirectory().getPath()+"/"+selectTitle).execute().get();
							if(b){	
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.DOWNLOAD_SUCCESS), 2000);
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.DOWNLOAD_FAIL), 2000);
							}
						}catch(Exception e){
							
						}
					}
				});
			}
		}
	}
	
	private String getEndTime(){
		StringTokenizer tokens = new StringTokenizer(duration, ":");
		int h = 0;
		int min = 0;
		int sec = 0;
		if (tokens.hasMoreTokens()) {
			h = Integer.parseInt(tokens.nextToken());
		}
		if(tokens.hasMoreTokens()){
			min = Integer.parseInt(tokens.nextToken());
		}
		if(Constant.DEBUG)  Log.d(TAG, "Duration Hours: "+h+", Minute: "+min);
		
		tokens = new StringTokenizer(starttime, ":");
		int sh = 0;
		int smin = 0;
		if (tokens.hasMoreTokens()) {
			sh = Integer.parseInt(tokens.nextToken());
		}
		if(tokens.hasMoreTokens()){
			smin = Integer.parseInt(tokens.nextToken());
		}
		if(Constant.DEBUG)  Log.d(TAG, "Start Hours: "+h+", Minute: "+min);
		int eh = 0;
		int emin = 0;
		if((sh+h)>=24){
			eh = (sh+h)-24;
		}else{
			eh = sh+h;
		}
		if((smin+min)>=60){
			emin = (smin+min)-60;
		}else{
			emin = smin+min;
		}
		String endTime = eh+":"+emin+":00";
		return endTime;
	}
	
	
/**********************************************************************************************************/
		
	public final BroadcastReceiver mGuideReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "BroadcastReceiver ");
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
					try {
						objData = new JSONObject(jsondata);
					}catch(Exception e){
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
			if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendBouquetList")){
				if(mGuideInstance != null){
					if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){ //if connected to the device
						if(Constant.DEBUG)  Log.d(TAG , "Bouquet with remote connected");
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
//								scrolldataList= new ArrayList<HashMap<String,String>>();//0812
								JSONArray jsonArray = Utils.getListFromJSON(jsonData, "bouquetList");
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										HashMap<String,String> showMap = new HashMap<String, String>();
										
										JSONObject obj;
										try {
											obj = jsonArray.getJSONObject(i);
											String id = "";
											String mName = "";
											String mImage = "";
											if(obj.has("id")){
												id= obj.getString("id");
											}
											if(obj.has("image")){
												mImage= obj.getString("image");
											}
											if(obj.has("name")){
												mName= obj.getString("name");
											}
											showMap.put(ScreenStyles.LIST_KEY_ID, id);
											if(mImage != null && !mImage.equalsIgnoreCase("")){
												showMap.put(ScreenStyles.LIST_KEY_HTTP_URL, mImage);
											}else{
												showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, Integer.toString(R.drawable.v13_ico_folder_01));
											}
											showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
	//												CacheDockData.BouquetList.add(new BouquetInfo(id, mName, ""));
											bouquetList.add(showMap);
//											scrolldataList.add(showMap);//0812
											
										}catch(Exception e){
									    	e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
									    }
									}
								}
								mGuideInstance.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										
	//									if(isScroll){//added by tomesh 
	//										Log.i(TAG, "scrolldataList"+scrolldataList.size());
	//										bouquetAdapter.addData(scrolldataList);
	//										isScroll=false;
										//	bouquetAdapter.notifyDataSetChanged();	
										//}else {
											lukupListView.invalidate();
											bouquetAdapter = new ScrollList(mGuideInstance, bouquetList, "bouquet", "Guide");
											lukupListView.setAdapter(bouquetAdapter);
											refreshListView(lukupListView);	
										//}									
										
										//lukupListView.setAdapter(new ScrollList(mGuideInstance, bouquetList, "bouquet","Guide"));
											
											method = "com.port.apps.epg.Guide.sendBouquetList";
											//HelpTip
											HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.CONTENT_BOUQUETS),
													mGuideInstance.getResources().getString(R.string.GUIDE_MSG1),mGuideInstance);
									}
								});
							}
						}else if(jsonData.has("failure")){
							HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}else{
						if(jsonData.has("data")){
							String result = Utils.getDataFromJSON(jsonData, "data");
							if(Constant.DEBUG)  Log.d(TAG , "Result with remote connected"+result);
							if(!result.equalsIgnoreCase("")){
//								scrolldataList= new ArrayList<HashMap<String,String>>();//0812
								JSONArray jsonArray = Utils.getListFromJSON(jsonData, "data");
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										HashMap<String,String> showMap = new HashMap<String, String>();
										
										JSONObject obj;
										try {
											obj = jsonArray.getJSONObject(i);
											String id = "";
											String mName = "";
											String mImage = "";
											
											if(obj.has("bouquetid")){
												id= obj.getString("bouquetid");
											}
											if(obj.has("bouquet")){
												mName= obj.getString("bouquet");
												if(Constant.DEBUG)  Log.d(TAG , "Bouquet "+mName);
											}
											showMap.put(ScreenStyles.LIST_KEY_ID, id);
											if(mImage != null && !mImage.equalsIgnoreCase("")){
												showMap.put(ScreenStyles.LIST_KEY_HTTP_URL, mImage);
											}else{
												showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, Integer.toString(R.drawable.v13_ico_folder_01));
											}
											showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
											
											bouquetList.add(showMap);
//											scrolldataList.add(showMap);//0812
											
										}catch(Exception e){
										    	e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										    }
										}
									}
								mGuideInstance.runOnUiThread(new Runnable() {
									@Override
									public void run() {
											lukupListView.invalidate();
											bouquetAdapter = new ScrollList(mGuideInstance, bouquetList, "bouquet", "Guide");
											lukupListView.setAdapter(bouquetAdapter);
											refreshListView(lukupListView);	

											method = "com.port.apps.epg.Guide.sendBouquetList";
											//HelpTip
											HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.CONTENT_BOUQUETS),
													mGuideInstance.getResources().getString(R.string.GUIDE_MSG1),mGuideInstance);
									}
								});
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
							}
						}
					}
				}
			} else if (handler.equalsIgnoreCase("com.port.apps.epg.Play.lockStatus")){
				if(mGuideInstance != null){
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
							HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){
				if(mGuideInstance != null){
					if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								boolean ishasVal = jsonData.has("serviceList");
								if(ishasVal){
									try {
										serviceList = getServiceData(jsonData, "serviceList");
										if(Constant.DEBUG)  Log.d(TAG , "itemList "+serviceList.size());
										mGuideInstance.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												lukupListView.invalidate();
												
												lukupListView.setAdapter(new ScrollList(mGuideInstance, serviceList, "service","Guide"));
												refreshListView(lukupListView);
												method = "com.port.apps.epg.Guide.sendServiceList";
											}
										});
										//HelpTip
										HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.CHANNELS),
												mGuideInstance.getResources().getString(R.string.GUIDE_MSG3),mGuideInstance);
									} catch (Exception e) {
										e.printStackTrace();
										StringWriter errors = new StringWriter();
										e.printStackTrace(new PrintWriter(errors));
										SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
									}
								}else{
									HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}
							}else{
								method = "com.port.apps.epg.Guide.sendBouquetList";
							}
						}
							
					} else {
						if(jsonData.has("data")){
							String result = Utils.getDataFromJSON(jsonData, "data");
							if(!result.equalsIgnoreCase("")){
//								scrolldataList= new ArrayList<HashMap<String,String>>();//0812
								JSONArray jsonArray = Utils.getListFromJSON(jsonData, "data");
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										JSONObject obj;
										try {
											obj = jsonArray.getJSONObject(i);
											String id = "";
											
											if(obj.has("bouquetid")){
												id= obj.getString("bouquetid");
												if(id.equalsIgnoreCase(selectItemId)){
													if(obj.has("list")){
														serviceList = getServiceData(obj, "list");
														if(Constant.DEBUG)  Log.d(TAG , "itemList "+serviceList.size());
														mGuideInstance.runOnUiThread(new Runnable() {
															@Override
															public void run() {
																lukupListView.invalidate();
																
																lukupListView.setAdapter(new ScrollList(mGuideInstance, serviceList, "service","Guide"));
																refreshListView(lukupListView);
																method = "com.port.apps.epg.Guide.sendServiceList";
															}
														});
														//HelpTip
														HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.CHANNELS),
																mGuideInstance.getResources().getString(R.string.GUIDE_MSG3),mGuideInstance);
													}
												}
											}
											
										}catch(Exception e){
										    	e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										}
									}
								}else{
									HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
							}							
						}else{
							HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
				if(mGuideInstance != null){
					if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								boolean ishasVal = jsonData.has("eventList");
								if(ishasVal){
									eventList = getEventData(jsonData, "eventList");
									if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
									mGuideInstance.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											lukupListView.invalidate();
											lukupListView.setAdapter(new ScrollList(mGuideInstance, eventList, "event","Guide"));
											refreshListView(lukupListView);	
											method = "com.port.apps.epg.Guide.sendEventList";
										}
									});
									//HelpTip
									HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.PROGRAMS),
											mGuideInstance.getResources().getString(R.string.GUIDE_MSG7),mGuideInstance);
								}else{
									boolean ishasCollection = jsonData.has("collection");
									if (ishasCollection) {
										collectionList  = getCollectionData(jsonData, "collection");
										if(Constant.DEBUG)  Log.d(TAG , "collectionList "+collectionList.size());
										mGuideInstance.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												lukupListView.invalidate();
												lukupListView.setAdapter(new ScrollList(mGuideInstance, collectionList, "collection","Guide"));
												refreshListView(lukupListView);	
												method = "com.port.apps.epg.Guide.sendCollectionList";
											}
										});
									}
								}
								
							}else{
								if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
	//							method = "com.port.apps.epg.Guide.sendServiceList";
	//							HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								String serviceType = Utils.getDataFromJSON(jsonData, "servicetype");
								if(Constant.DEBUG)  Log.d(TAG , "serviceType "+serviceType);
								if (serviceType.equalsIgnoreCase("live")) {
									Intent play = new Intent(Guide.this, PlayBack.class);
									play.putExtra("ActivityName", "Guide");
									play.putExtra("serviceid", selectItemId);
									play.putExtra("Type", serviceType);
									play.putExtra("pricingmodel", pricingModel);
									play.putExtra("EventId", "0");
									startActivity(play);
								}else{
									method = "com.port.apps.epg.Guide.sendServiceList";
									HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}
							}
						}
					}else{ //not connected to device
						if(jsonData.has("json")){
							JSONObject json = (JSONObject) jsonData.get("json");
							if(json.has("result")){
								String result = json.getString("result");
								if(result.equalsIgnoreCase("success")){
									boolean ishasVal = json.has("data");
									if(ishasVal){
										eventList = getEventData(json, "data");
										if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
										mGuideInstance.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												lukupListView.invalidate();
												lukupListView.setAdapter(new ScrollList(mGuideInstance, eventList, "event","Guide"));
												refreshListView(lukupListView);	
												method = "com.port.apps.epg.Guide.sendEventList";
											}
										});
										//HelpTip
										HelpTip.requestForHelp(mGuideInstance.getResources().getString(R.string.PROGRAMS),
												mGuideInstance.getResources().getString(R.string.GUIDE_MSG7),mGuideInstance);
									}
								}else{
									method = "com.port.apps.epg.Guide.sendServiceList";
									HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}
							}else{
								method = "com.port.apps.epg.Guide.sendServiceList";
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.NO_CONTENT), 2000);
							}
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Subscriptions")){
				if(mGuideInstance != null){
					if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								if(isSubscribe.equalsIgnoreCase("false")){
									isSubscribe = "true";
								}else{
									isSubscribe = "false";
								}
								if(jsonData.has("id")){
									try {
										cleanList(method);
										if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){ //to do
											changeValue(serviceList, "subscribe", isSubscribe, jsonData.getInt("id"));
										}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
											changeValue(eventList, "subscribe",isSubscribe , jsonData.getInt("id"));
										}
										
										HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
										HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 4000);
									} catch (JSONException e) {
										e.printStackTrace();
										StringWriter errors = new StringWriter();
										e.printStackTrace(new PrintWriter(errors));
										SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
									}
								}
							}
						}
					}else{ //not connected to the device
						if(jsonData.has("data")){
							String resp = Utils.getDataFromJSON(jsonData, "data");
							JSONObject response = new JSONObject(resp);
							if(response.has("result")){
								String result = response.getString("result");
								if(result.equalsIgnoreCase("success")){
									if(isSubscribe.equalsIgnoreCase("false")){
										isSubscribe = "true";
									}else{
										isSubscribe = "false";
									}
									cleanList(method);
									if(Constant.DEBUG)  Log.d(TAG , "Subscription status of : " + selectItemId + " is : "+ isSubscribe);
									if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){ //to do
										changeValue(serviceList, "subscribe", isSubscribe, Integer.valueOf(selectItemId));
									}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
										changeValue(eventList, "subscribe",isSubscribe , Integer.valueOf(selectItemId));
									}
								}
							}
							if(response.has("msg")){
								HelpText.showHelpTextDialog(mGuideInstance, response.getString("msg"), 4000);
							}
						}else{
//							HelpText.showHelpTextDialog(mGuideInstance, response.getString("msg"), 4000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Like")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							if(isLike.equalsIgnoreCase("false")){
								isLike = "true";
							}else{
								isLike = "false";
							}
							if(jsonData.has("id")){
								try {
									cleanList(method);
									if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){
										changeValue(serviceList, "like", isLike, jsonData.getInt("id"));
									}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
										changeValue(eventList, "like",isLike , jsonData.getInt("id"));
									}
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Lock")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							if(isLock.equalsIgnoreCase("false")){
								isLock = "true";
							}else{
								isLock = "false";
							}
							if(jsonData.has("id")){
								try {
									cleanList(method);
									if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){
										changeValue(serviceList, "lock", isLock, jsonData.getInt("id"));
									}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
										changeValue(eventList, "lock",isLock , jsonData.getInt("id"));
									}
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.setReminder")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							if(jsonData.has("msg")){
								try {
									cleanList("com.port.apps.epg.Plan.setReminder"); //clean cache
//											if(method.equalsIgnoreCase("com.port.apps.epg.Plan.setReminder")){
										getPlanEventData(jsonData,"reminderList"); //cache reminders
//											}
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
								} catch (JSONException e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						}
						method = "com.port.apps.epg.Guide.sendEventList";
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.setRecord")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								if(jsonData.has("msg")){
									try {
										if(Constant.DEBUG)Log.d(TAG, "is Recorded ? : "+ isRecord);
										if(isRecord.equalsIgnoreCase("false")){
											isRecord = "true";
										}else{
											isRecord = "false";
										}
										
										cleanList("com.port.apps.epg.Plan.setRecord"); //clean cache
										cleanList("com.port.apps.epg.Guide.sendEventList");
										getPlanEventData(jsonData, "recordList"); //cache records
											
										if(jsonData.has("id")){
											changeValue(eventList, "record",isRecord , jsonData.getInt("id"));
										}
										HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
										HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
									} catch (JSONException e) {
										e.printStackTrace();
										StringWriter errors = new StringWriter();
										e.printStackTrace(new PrintWriter(errors));
										SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
									}
								}
							}
							method = "com.port.apps.epg.Guide.sendEventList";
						}else{
							if(jsonData.has("data")){
								String resp = Utils.getDataFromJSON(jsonData, "data");
								JSONObject response = new JSONObject(resp);
								if(response.has("result")){
									String result = response.getString("result");
									if (Constant.DEBUG) Log.d(TAG, "Scheduling record " + result);
									if(result.equalsIgnoreCase("success")){
										if(jsonData.has("msg")){
											HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
										}
									}else{
										if(jsonData.has("msg")){
											HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
										}
									}
								}else{
									HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.SOMTHING_WRONG), 2000);
								}
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.SOMTHING_WRONG), 2000);
							}
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.stopRecord")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								try {
									if (Constant.DEBUG) Log.d(TAG, "cancel record " + result);
									if(isRecord.equalsIgnoreCase("false")){
										isRecord = "true";
									}else{
										isRecord = "false";
									}
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
									if(jsonData.has("id")){
										cleanList("com.port.apps.epg.Plan.setRecord"); //clean cache
										cleanList("com.port.apps.epg.Guide.sendEventList");
										getPlanEventData(jsonData, "recordList"); //cache records
										changeValue(eventList, "record",isRecord , jsonData.getInt("id"));
									}
								} catch (JSONException e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.SOMTHING_WRONG), 2000);
							}
						}else{
							if (Constant.DEBUG) Log.d(TAG, "Canceling record ");
							String result = Utils.getDataFromJSON(jsonData, "result");
							if(result.equalsIgnoreCase("success")){
								if(jsonData.has("msg")){
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
								}
							}else{
								if(jsonData.has("msg")){
									HelpText.showHelpTextDialog(mGuideInstance, mGuideInstance.getResources().getString(R.string.SOMTHING_WRONG), 2000);
								}
							}
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventInfo")){
				try {
					if(mGuideInstance != null){
						mGuideInstance.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								 Info.rInfoAction(jsonData, mContainer, mGuideInstance,"com.player.apps.Guide");
								 method = "com.port.apps.epg.Guide.sendEventInfo";
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Devices.getOutputDeviceList")){	// Bold
				if(mGuideInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									PlayOn.showConnectedDevices(jsonData,selectItemId, mGuideInstance, customLayout, "BT","com.player.apps.Guide");											
								}else{
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
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
			} else if (handler.equalsIgnoreCase("com.port.apps.epg.Devices.getRemoteDisplays")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						try{
							if(result.equalsIgnoreCase("success")){
								PlayOn.showConnectedDevices(jsonData,selectItemId, mGuideInstance, customLayout, "Wifi", "com.player.apps.Guide");
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
							}
						}catch(Exception e){
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
					}
				}
			}else if (handler.equalsIgnoreCase("com.port.apps.epg.Devices.playRemoteDisplay")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						try{
							if(result.equalsIgnoreCase("failure")){
								HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
							}
						}catch(Exception e){
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
					}
				}
			}
			else if (handler.equalsIgnoreCase("com.port.apps.epg.Attributes.PlayList")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						try{
							if(result.equalsIgnoreCase("success")){
								String state = Utils.getDataFromJSON(jsonData, "state");
								if (state.equalsIgnoreCase("added")){
									HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
								}
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, jsonData.getString("msg"), 2000);
							}
						}catch(Exception e){
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
				}
			}
			else if(handler.equalsIgnoreCase("com.port.apps.storage.download")){
				if(mGuideInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						String name = Utils.getDataFromJSON(jsonData, "name");
						try{
							if(result.equalsIgnoreCase("success")){
								HelpText.showHelpTextDialog(mGuideInstance, name + mGuideInstance.getResources().getString(R.string.DOWNLOAD_SUCCESS), 4000);
							}else{
								HelpText.showHelpTextDialog(mGuideInstance, name + mGuideInstance.getResources().getString(R.string.DOWNLOAD_FAIL), 4000);
							}
						}catch(Exception e){
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
	
	
	private void cleanList(String method){
		if (Constant.DEBUG)	Log.d(TAG, "cleanList() method :" + method);
		if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.ServiceList :" + CacheDockData.ServiceList.size());
			ArrayList<ChannelInfo> defaultList = new ArrayList<ChannelInfo>();
			for (int i = 0; i < CacheDockData.ServiceList.size(); i++) {
				if (!CacheDockData.ServiceList.get(i).getChannelBouquetId().equalsIgnoreCase(selectedBouquet)) {
					defaultList.add(CacheDockData.ServiceList.get(i));
				}
			}
			CacheDockData.ServiceList.clear();
			CacheDockData.ServiceList = defaultList;
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.ServiceList :" + CacheDockData.ServiceList.size());
		}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.EventList :" + CacheDockData.EventList.size());
			ArrayList<ProgramInfo> defaultList = new ArrayList<ProgramInfo>();
			for (int i = 0; i < CacheDockData.EventList.size(); i++) {
				if (!CacheDockData.EventList.get(i).getServiceId().equalsIgnoreCase(selectedService)) {
					defaultList.add(CacheDockData.EventList.get(i));
				}
			}
			CacheDockData.EventList.clear();
			CacheDockData.EventList = defaultList;
			if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.EventList :" + CacheDockData.EventList.size());
		}else if(method.equalsIgnoreCase("com.port.apps.epg.Plan.setReminder")){
			CacheDockData.ReminderEventList.clear();
		}else if(method.equalsIgnoreCase("com.port.apps.epg.Plan.setRecord")){
			CacheDockData.RecordEventList.clear();
		}
	}
	
	
	private ArrayList<HashMap<String,String>> changeValue(ArrayList<HashMap<String,String>> list,String tag,String value,int id){
		String uid ="";
		ArrayList<HashMap<String,String>> changedList = new ArrayList<HashMap<String, String>>();
		for(int i= 0;i<list.size();i++){
			HashMap<String,String> hashMap = list.get(i);
			uid = hashMap.get("id");
			if (Constant.DEBUG)	Log.d(TAG,"UID : " + uid.toString() + " and passed ID is : " + id);
			if(uid.equalsIgnoreCase(id+"")){
				hashMap.remove(tag);
				hashMap.put(tag, value);
				if (Constant.DEBUG)	Log.d(TAG, "changeValue()  Value :" + hashMap.get(tag));
			}
			changedList.add(hashMap);
		}
		return changedList;
	}
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG," Current Screen: " + DataStorage.getCurrentScreen());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")+", method :" + method);
			if(mMenu.isShowing()){
				hideMenu();
				return true;
			}
			HelpTip.close(mGuideInstance);
			if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendBouquetList")){			
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(Guide.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Guide");
				startActivity(lukup);
				finish(); //close activity and show appguide
				return true;
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendServiceList")){
				//show bouquet list
				if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  bouquetList :" + bouquetList.size());
				method = "com.port.apps.epg.Guide.sendBouquetList";
				lukupListView.invalidate();
				lukupListView.setAdapter(new ScrollList(mGuideInstance, bouquetList, "bouquet","Guide"));
				refreshListView(lukupListView);	
				return true;
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendCollectionList")){
				if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  collectionList :" + collectionList.size());
				method = "com.port.apps.epg.Guide.sendServiceList";
				lukupListView.invalidate();
				lukupListView.setAdapter(new ScrollList(mGuideInstance, serviceList, "service","Guide"));
				refreshListView(lukupListView);	
				return true;
			}else if(method.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
				if(mGuideInstance != null){
					mGuideInstance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(collectionList.size()<=0){
								//show service list
								if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  serviceList :" + serviceList.size());
								method = "com.port.apps.epg.Guide.sendServiceList";
								lukupListView.invalidate();
								lukupListView.setAdapter(new ScrollList(mGuideInstance, serviceList, "service","Guide"));
								refreshListView(lukupListView);	
							}else{
								//show service list
								if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  collectionList :" + collectionList.size());
								method = "com.port.apps.epg.Guide.sendCollectionList";
								lukupListView.invalidate();
								lukupListView.setAdapter(new ScrollList(mGuideInstance, collectionList, "collection","Guide"));
								refreshListView(lukupListView);	
							}
						}
					});
				}
				
				return true;
			} else if(DataStorage.getCurrentScreen() != null && (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_INFO_SCREEN) || 
					DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_OPERATOR_UI))){
				if(Constant.DEBUG)  Log.d(TAG ,"Going back from info on TV guide: "+DataStorage.getCurrentScreen());
				method = "com.port.apps.epg.Guide.sendEventList";
				if(mGuideInstance != null){
					mGuideInstance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mContainer.removeAllViews();
							LayoutInflater inflater = mGuideInstance.getLayoutInflater();
							View listView = inflater.inflate(R.layout.guidelist,null);
							mContainer.addView(listView,getLinearLayoutParams());
							lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
							lukupListView.invalidate();
							lukupListView.setAdapter(new ScrollList(mGuideInstance, eventList, "event","Guide"));
							refreshListView(lukupListView);
							lukupListView.setOnItemClickListener(mGuideInstance);
							lukupListView.setOnItemLongClickListener(mGuideInstance);
						}
					});
				}
							
				return true;
			}
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				HelpTip.close(mGuideInstance);
				Intent lukup = new Intent(Guide.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Guide");
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

			if (resultCode == RESULT_OK) {

				String type = data.getStringExtra("Type");

				String value = data.getStringExtra("ID");

				String tag = data.getStringExtra("Tag");

				if (tag.equalsIgnoreCase("delete")) {

					if (Constant.DEBUG)
						Log.d(TAG, "onActivityResult() Delete ServiceList :"
								+ serviceList.size());

					ArrayList<HashMap<String, String>> defaultList = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < serviceList.size(); i++) {

						HashMap<String, String> map = serviceList.get(i);

						String id = "";

						if (map.containsKey(ScreenStyles.LIST_KEY_ID)) {

							id = map.get(ScreenStyles.LIST_KEY_ID);

						}

						if (!id.equalsIgnoreCase(value)) {

							defaultList.add(serviceList.get(i));

						}

					}

					serviceList.clear();

					serviceList = defaultList;

				} else if (tag.equalsIgnoreCase("rename")) {

					if (Constant.DEBUG)
						Log.d(TAG, "onActivityResult() Rename ServiceList :"
								+ serviceList.size());

					ArrayList<HashMap<String, String>> defaultList = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < serviceList.size(); i++) {

						HashMap<String, String> map = serviceList.get(i);

						String id = "";

						if (map.containsKey(ScreenStyles.LIST_KEY_ID)) {

							id = map.get(ScreenStyles.LIST_KEY_ID);

						}

						if (id.equalsIgnoreCase(value)) {

							map.remove("title");

							map.put("title", type);

							defaultList.add(map);

							if (Constant.DEBUG)
								Log.d(TAG, "onActivityResult() map :" + map);

						} else if (!id.equalsIgnoreCase(value)) {

							defaultList.add(serviceList.get(i));

						}

					}
					serviceList.clear();
					serviceList = defaultList;
				}

				if (Constant.DEBUG) Log.d(TAG,"onActivityResult() ServiceList :"+ serviceList.size());

				lukupListView.invalidate();
				lukupListView.setAdapter(new ScrollList(mGuideInstance,serviceList, "service","Guide"));
				refreshListView(lukupListView);

			}
			
		}
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	public static ArrayList<HashMap<String, String>> getServiceData(JSONObject jsonData ,String jsonArrayName) {
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		try{
			if(jsonData != null){
				if(jsonData.has(jsonArrayName)){
					list = new ArrayList<HashMap<String,String>>();
					JSONArray channelList = Utils.getListFromJSON(jsonData, jsonArrayName);
					if(channelList != null && channelList.length() >0){
						for(int i=0;i<channelList.length();i++){
							try {
								HashMap<String,String> showMap = new HashMap<String, String>();
								JSONObject obj = channelList.getJSONObject(i);
								String id = "";
								String mImage= "";
								String mName = "";
								String mcategory = "";
								String mserviceType = "";
								String msubscribe = "";
								String mlock = "";
								String mlike = "";
								String mPrice = "";
								String mPricingModel = "";
								String timeStamp = "";
								String channelurl = "";//Added by tomesh
								String description = "";
								
								if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
									if(obj.has("id")){
										id= obj.getString("id");
									}
									if(obj.has("image")){
										mImage= obj.getString("image");
									}
									if(obj.has("name")){
										mName= obj.getString("name");
									}
									if(obj.has("category")){
										mcategory= obj.getString("category");
									}
									if(obj.has("servicetype")){
										mserviceType= obj.getString("servicetype");
									}
									if(obj.has("subscribe")){
										msubscribe= obj.getString("subscribe");
									}
									if(obj.has("lock")){
										mlock= obj.getString("lock");
									}
									if(obj.has("like")){
										mlike= obj.getString("like");
									}
									if(obj.has("price")){
										mPrice= obj.getString("price");
									}
									if(obj.has("pricingmodel")){
										mPricingModel= obj.getString("pricingmodel");
									}
									if(obj.has("timeStamp")){
										timeStamp= obj.getString("timeStamp");
									}
									if(obj.has("source")){
										channelurl= obj.getString("source");//Added By tomesh for Live Tv
									}
//									if(Utils.checkNullAndEmpty(mPricingModel)){
//										if(mPricingModel.equalsIgnoreCase("PPC")){
//											if(obj.has("price")){
//												mPrice = obj.getString("price");
//											}
//										}else{
//											mPrice = "";
//										}
//									}else{
//										mPrice = "";
//									}
									if(Constant.DEBUG)  Log.d("Guide","In get service data, Channel name : "+ mName + " Price : " + mPrice);
								}else{ //if app is not connected to the device
									if(obj.has("serviceid")){
										id= obj.getString("serviceid");
									}
									if(obj.has("logo")){
										mImage= obj.getString("logo");
									}
									if(obj.has("name")){
										mName= obj.getString("name");
									}
									if(obj.has("category")){
										mcategory= obj.getString("category");
									}
									if(obj.has("type")){
										mserviceType= obj.getString("type");
									}
									if(obj.has("description")){
										description = obj.getString("description");
									}
									if(obj.has("subscribed")){
//										if(String.valueOf(DataStorage.getchannelidlist()).contains(id)){
//											msubscribe = "true";
//										}else{
											msubscribe= obj.getString("subscribed");
//											msubscribe = "false";
//										}
									}
//									if(obj.has("lock")){
//										mlock= obj.getString("lock");
//									}
//									if(obj.has("like")){
//										mlike= obj.getString("like");
//									}
									if(obj.has("price")){
										mPrice= obj.getString("price");
									}
									if(obj.has("pricingmodel")){
										mPricingModel= obj.getString("pricingmodel");
									}
//									if(obj.has("timeStamp")){
//										timeStamp= obj.getString("timeStamp");
//									}
									if(obj.has("contentUrl")){
										channelurl= obj.getString("contentUrl");//Added By tomesh for Live Tv
									}
//									if(Utils.checkNullAndEmpty(mPricingModel)){
//										if(mPricingModel.equalsIgnoreCase("PPC")){
//											if(obj.has("price")){
//												mPrice = obj.getString("price");
//											}
//										}else{
//											mPrice = "";
//										}
//									}else{
//										mPrice = "";
//									}
								}
								
								showMap.put(ScreenStyles.LIST_KEY_ID, id);
								showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.v13_ico_folder_01+"");
								showMap.put("title", mName);
								showMap.put("image", mImage);
								showMap.put("like", mlike);
								showMap.put("lock", mlock);
								showMap.put("category", mcategory);
								showMap.put("description", description);
								showMap.put("event", "false");
								showMap.put("servicetype", mserviceType);
								showMap.put("subscribe", msubscribe);
								showMap.put("pricingmodel", mPricingModel);
								showMap.put("price", mPrice);
								showMap.put("channelurl", channelurl);//Added by tomesh
								list.add(showMap);
								
								if(Constant.DEBUG)  Log.d("Guide"," id: "+id+", selectedBouquet: "+selectedBouquet+", servicetype: "+mserviceType+", subscribe: "+msubscribe+", locked: "+mlock+", like: "+mlike+", mPrice: "+mPrice);
								
//								CacheDockData.ServiceList.add(new ChannelInfo(id, mName, mImage, mcategory, mserviceType, mPricingModel, mPrice, msubscribe, mlock, mlike, "false",selectedBouquet, timeStamp));
							} catch (Exception e) {
								e.printStackTrace();
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								continue;
							}
						}
					}
				}
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return null;
	}
	
	public static ArrayList<HashMap<String, String>> getEventData(JSONObject jsonData ,String jsonArrayName) {
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		try{
			if(jsonData != null){
				if(Constant.DEBUG)  Log.d("Guide","JSONData " + jsonData.toString());
				boolean ishasEvent = jsonData.has(jsonArrayName);
				if(ishasEvent){
					JSONArray eventlist = Utils.getListFromJSON(jsonData, jsonArrayName);
					String mserviceId = "";
					String mcategory = "";
					String mserviceType = "";
					String msubscribe = "";
					String mPricingModel = "";
					String mPrice = "" ;
					String mlock = "";
					String description = "";
					long now = 0;
					
					if(mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
					
						if(jsonData.has("serviceid")){
							mserviceId= jsonData.getString("serviceid");
						}
	//					if(jsonData.has("category")){
	//						mcategory= jsonData.getString("category");
	//					}
						if(jsonData.has("servicetype")){
							mserviceType= jsonData.getString("servicetype");
						}
	
						if(jsonData.has("subscribe")){
							msubscribe= jsonData.getString("subscribe");
						}
						if(jsonData.has("pricingmodel")){
							mPricingModel = jsonData.getString("pricingmodel");
						}
						if(jsonData.has("price")){
							mPrice = jsonData.getString("price");
						}
						if(jsonData.has("lock")){
							mlock= jsonData.getString("lock");
						}
						if(jsonData.has("timenow")){
							now = jsonData.getLong("timenow");
						}
	
						if(eventlist != null && eventlist.length() >0){
							for(int i=0;i<eventlist.length();i++){
								try {
									HashMap<String,String> showMap = new HashMap<String, String>();
									JSONObject obj = eventlist.getJSONObject(i);
									String id = "";
									String mName = "";
									String mImage = "";
									String mlike = "";
									String mRecord = "";
									String mlocked = "";
									String timeStamp = "";
									String mEventSubscribe = "";
									String starttime = "";
									String url = "";
									String mCollectionid = "";
									String mCollectionName = "";
									if(Utils.checkNullAndEmpty(mlock)){
										if(mlock.equalsIgnoreCase("false")){
											if(obj.has("lock")){
												mlocked= obj.getString("lock");
											}else{
												mlocked= mlock;
											}
										}else{
											mlocked = mlock;
										}
									}else{
										if(obj.has("lock")){
											mlocked = obj.getString("lock");
										}
									}
	
									if(obj.has("id")){
										id= obj.getString("id");
									}
									if(obj.has("image")){
										mImage= obj.getString("image");
									}
									if(obj.has("name")){
										mName= obj.getString("name");
									}
									if(obj.has("like")){
										mlike= obj.getString("like");
									}
									if(obj.has("record")){
										mRecord= obj.getString("record");
									}
									if(obj.has("pricingmodel")){
										mPricingModel = obj.getString("pricingmodel");
									}
									if(obj.has("timeStamp")){
										timeStamp= obj.getString("timeStamp");
									}
									if(obj.has("url")){
										url= obj.getString("url");
									}
									
									if(obj.has("starttime")){
										starttime = obj.getString("starttime");
									}
									if(obj.has("subcategory")){
										mcategory= obj.getString("subcategory");
									}
									
				                    if(obj.has("collectionid")){
										mCollectionid = obj.getString("collectionid");
									}
									if(obj.has("collectionname")){
										mCollectionName= obj.getString("collectionname");
									}
									
									if(Utils.checkNullAndEmpty(mPricingModel) && mPricingModel.equalsIgnoreCase("PPV")){
										if(obj.has("price")){
											mPrice = obj.getString("price");
										}
										if(obj.has("subscribe")){
											mEventSubscribe= obj.getString("subscribe");
										}
									}else{
	//									mPrice ="";
										mEventSubscribe = msubscribe;
									}
									if(Constant.DEBUG)  Log.d("Guide" ,"Now : " + now + " Starttime : " + starttime);
									if(Constant.DEBUG)  Log.d("Guide","In getevent data, Event name : "+ mName + " Price : " + mPrice);
									showMap.put(ScreenStyles.LIST_KEY_ID, id);
									showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
									showMap.put("title", mName);
									showMap.put("image", mImage);
									showMap.put("category", mcategory);
									showMap.put("starttime", starttime);
									showMap.put("servicetype", mserviceType);
									showMap.put("serviceid", mserviceId);
									showMap.put("subscribe", mEventSubscribe);
									showMap.put("urllink", url);
									showMap.put("lock", mlocked);
									showMap.put("pricingmodel", mPricingModel);
									showMap.put("like", mlike);
									showMap.put("record", mRecord);
									showMap.put("event", "true");
									showMap.put("price", mPrice);
									showMap.put("collectionid", mCollectionid);
									showMap.put("collectionname", mCollectionName);
									list.add(showMap);
								} catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
									continue;
								}
							}
						}
					}else{
						if(Constant.DEBUG)  Log.d(TAG, "fetching event list " + eventlist.toString());
						if(eventlist != null && eventlist.length() >0){
							if(Constant.DEBUG)  Log.d(TAG,"Going into for loop");
							for(int i=0;i<eventlist.length();i++){
								if(Constant.DEBUG)  Log.d(TAG,"Inside for loop");
								try {
									HashMap<String,String> showMap = new HashMap<String, String>();
									JSONObject obj = eventlist.getJSONObject(i);
									String id = "";
									String mName = "";
									String mImage = "";
									String url = "";
									String mCollectionid = "";
									String mCollectionName = "";
									String starttime ="";
									String startdate = "";
									String duration = "";
									if(Constant.DEBUG)  Log.d(TAG,"Object " + obj.toString());
									
									if(obj.has("uniqueid")){
										id= obj.getString("uniqueid");
										if(Constant.DEBUG)  Log.d("Guide","Id " + id);
									}
									if(obj.has("image")){
										mImage= obj.getString("image");
										if(Constant.DEBUG)  Log.d("Guide","Image " + mImage);
									}
									if(obj.has("name")){
										mName= obj.getString("name");
										if(Constant.DEBUG)  Log.d("Guide","Name " + mName);
									}
									if(obj.has("description")){
										description = obj.getString("description");
									}
//									if(obj.has("like")){
//										mlike= obj.getString("like");
//									}
//									if(obj.has("record")){
//										mRecord= obj.getString("record");
//									}
//									if(obj.has("pricingmodel")){
//										mPricingModel = obj.getString("pricingmodel");
//									}
//									if(obj.has("timeStamp")){
//										timeStamp= obj.getString("timeStamp");
//									}
									if(obj.has("source")){
										url= obj.getString("source");
										if(Constant.DEBUG)  Log.d("Guide","Source " + url);
									}
									
									if(obj.has("starttime")){
										starttime = obj.getString("starttime");
										if(Constant.DEBUG)  Log.d(TAG,"Start time " + starttime);
									}
									if(obj.has("startdate")){
										startdate = obj.getString("startdate");
										if(Constant.DEBUG)  Log.d(TAG,"Start date " + startdate);
									}
									if(obj.has("duration")){
										duration = obj.getString("duration");
										if(Constant.DEBUG)  Log.d(TAG,"Duration " + starttime);
									}
//									if(obj.has("subcategory")){
//										mcategory= obj.getString("subcategory");
//									}
									
				                    if(obj.has("collectionId")){
										mCollectionid = obj.getString("collectionId");
										if(Constant.DEBUG)  Log.d("Guide","CollectionId " + mCollectionid);
									}
									if(obj.has("collectionName")){
										mCollectionName= obj.getString("collectionName");
										if(Constant.DEBUG)  Log.d("Guide","CollectionName " + mCollectionName);
									}
									if(obj.has("serviceid")){
										mserviceId = obj.getString("serviceid");
										if(Constant.DEBUG)  Log.d(TAG,"ServiceId " + mserviceId);
										if(Integer.valueOf(mserviceId)>1000){
											mserviceType = "vod";
										}else{
											mserviceType = "live";
										}
									}
									if(Utils.checkNullAndEmpty(pricingModel) && pricingModel.equalsIgnoreCase("PPV")){
										if(obj.has("subscribed")){
											msubscribe= obj.getString("subscribed");
										}
									}else{
										msubscribe = isSubscribe;
									}
//									if(String.valueOf(DataStorage.geteventidlist()).contains(id)){
//										msubscribe = "true";
//									}else{
//										msubscribe = "false";
//									}
									if(Constant.DEBUG)  Log.d(TAG,"event subscribed ? "+ msubscribe + " pricing model is : " + mPricingModel);
									if(obj.has("price")){
										mPrice = obj.getString("price");
									}
									
									showMap.put(ScreenStyles.LIST_KEY_ID, id);
									showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
									showMap.put("title", mName);
									showMap.put("image", mImage);
									showMap.put("description", description);
//									showMap.put("category", mcategory);
									showMap.put("starttime", starttime);
									showMap.put("startdate", startdate);
									showMap.put("duration", duration);
									showMap.put("servicetype", mserviceType);
									showMap.put("serviceid", mserviceId);
									showMap.put("subscribe", msubscribe);
									showMap.put("urllink", url);
//									showMap.put("lock", mlocked);
									showMap.put("pricingmodel", pricingModel);
//									showMap.put("like", mlike);
//									showMap.put("record", mRecord);
									showMap.put("event", "true");
									showMap.put("price", mPrice);
									showMap.put("collectionid", mCollectionid);
									showMap.put("collectionname", mCollectionName);
									list.add(showMap);
								} catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
									continue;
								}
							}
						}
					}
					return list;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return null;
	}
	
	public ArrayList<HashMap<String, String>> getCollectionData(JSONObject jsonData ,String jsonArrayName) {
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		try{
			if(jsonData != null){
				boolean ishasEvent = jsonData.has(jsonArrayName);
				if(ishasEvent){
					JSONArray collectionlist = Utils.getListFromJSON(jsonData, jsonArrayName);
					String mserviceId = "";
					String mcategory = "";
					String mserviceType = "";
					String msubscribe = "";
					String mPricingModel = "";
					String mPrice = "" ;
					String mlock = "";
					if(jsonData.has("serviceid")){
						mserviceId= jsonData.getString("serviceid");
					}
					if(jsonData.has("servicetype")){
						mserviceType= jsonData.getString("servicetype");
					}

					if(jsonData.has("subscribe")){
						msubscribe= jsonData.getString("subscribe");
					}
					if(jsonData.has("pricingmodel")){
						mPricingModel = jsonData.getString("pricingmodel");
					}
					if(jsonData.has("price")){
						mPrice = jsonData.getString("price");
					}
					if(jsonData.has("lock")){
						mlock= jsonData.getString("lock");
					}
					
					if(collectionlist != null && collectionlist.length() >0){
						for(int i=0;i<collectionlist.length();i++){
							try {
								HashMap<String,String> showMap = new HashMap<String, String>();
								JSONObject obj = collectionlist.getJSONObject(i);
								String mcollectionId = "";
								String mcollectionName = "";
								if(obj.has("collectionid")){
									mcollectionId = obj.getString("collectionid");
								}
								if(obj.has("collectionname")){
									mcollectionName = obj.getString("collectionname");
								}
								if(Constant.DEBUG)  Log.d("Guide" ,"mcollectionId : " + mcollectionId + " mcollectionName : " + mcollectionName);
								if(obj.has("collectionEventList")){
									JSONArray collectioneventlist = Utils.getListFromJSON(obj, "collectionEventList");
									for(int j=0;j<collectioneventlist.length();j++){
										JSONObject jsonobj = collectioneventlist.getJSONObject(j);
										HashMap<String,String> map = new HashMap<String, String>();
										try {
											String id = "";
											String mName = "";
											String mlike = "";
											String mRecord = "";
											String mlocked = "";
											String mEventSubscribe = "";
											String starttime = "";
											String url = "";
											String mCollectionid = "";
											String mCollectionName = "";
											if(Utils.checkNullAndEmpty(mlock)){
												if(mlock.equalsIgnoreCase("false")){
													if(jsonobj.has("lock")){
														mlocked= jsonobj.getString("lock");
													}else{
														mlocked= mlock;
													}
												}else{
													mlocked = mlock;
												}
											}else{
												if(jsonobj.has("lock")){
													mlocked = jsonobj.getString("lock");
												}
											}

											if(jsonobj.has("id")){
												id= jsonobj.getString("id");
											}
											if(jsonobj.has("name")){
												mName= jsonobj.getString("name");
											}
											if(jsonobj.has("like")){
												mlike= jsonobj.getString("like");
											}
											if(jsonobj.has("record")){
												mRecord= jsonobj.getString("record");
											}
											if(jsonobj.has("pricingmodel")){
												mPricingModel = jsonobj.getString("pricingmodel");
											}
											if(jsonobj.has("url")){
												url= jsonobj.getString("url");
											}
											
											if(jsonobj.has("starttime")){
												starttime = jsonobj.getString("starttime");
											}
											if(jsonobj.has("subcategory")){
												mcategory= jsonobj.getString("subcategory");
											}
											
						                    if(jsonobj.has("collectionid")){
												mCollectionid = jsonobj.getString("collectionid");
											}
											if(jsonobj.has("collectionname")){
												mCollectionName= jsonobj.getString("collectionname");
											}
											
											if(Utils.checkNullAndEmpty(mPricingModel) && mPricingModel.equalsIgnoreCase("PPV")){
												if(jsonobj.has("price")){
													mPrice = jsonobj.getString("price");
												}
												if(jsonobj.has("subscribe")){
													mEventSubscribe= jsonobj.getString("subscribe");
												}
											}else{
//												mPrice ="";
												mEventSubscribe = msubscribe;
											}
											map.put(ScreenStyles.LIST_KEY_ID, id);
											map.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
											map.put(ScreenStyles.LIST_KEY_TITLE, mName);
											map.put("category", mcategory);
											map.put("starttime", starttime);
											map.put("servicetype", mserviceType);
											map.put("serviceid", mserviceId);
											map.put("subscribe", mEventSubscribe);
											map.put("urllink", url);
											map.put("lock", mlocked);
											map.put("pricingmodel", mPricingModel);
											map.put("like", mlike);
											map.put("record", mRecord);
											map.put("event", "true");
											map.put(ScreenStyles.LIST_KEY_PRICE, mPrice);
											map.put("collectionid", mCollectionid);
											map.put("collectionname", mCollectionName);
											collectionEventList.add(map);
											if(Constant.DEBUG)  Log.d("getCollectionData():",", mCollectionid: "+mCollectionid+" id: "+id+", mName: "+mName+", servicetype: "+mserviceType+", subscribe: "+msubscribe+", locked: "+mlock+", like: "+mlike+", mPrice: "+mPrice+", mEventSubscribe: "+mEventSubscribe);
											
										} catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
											continue;
										}
									}
								}
								if(Constant.DEBUG)  Log.d(TAG , "collectionEventList "+collectionEventList.size());
								if(Constant.DEBUG)  Log.d("Guide" ,"Add mcollectionId : " + mcollectionId + " mcollectionName : " + mcollectionName);
								
								showMap.put(ScreenStyles.LIST_KEY_ID, mcollectionId);
								showMap.put(ScreenStyles.LIST_KEY_TITLE, mcollectionName);
								showMap.put("category", mcategory);
								showMap.put("servicetype", mserviceType);
								showMap.put("serviceid", mserviceId);
								if(Utils.checkNullAndEmpty(mPricingModel) && mPricingModel.equalsIgnoreCase("PPC")){
									showMap.put("subscribe", msubscribe);
								}
								showMap.put("lock", mlock);
								showMap.put("pricingmodel", mPricingModel);
//								showMap.put("like", mlike);
								
								list.add(showMap);
								
							} catch (Exception e) {
								e.printStackTrace();
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								continue;
							}
						}
					}
						if(Constant.DEBUG)  Log.d(TAG , "collectionList "+list.size());
						return list;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return null;
	}
		

	private int MoneyConverter(String val){
		int cost = 0;
		if(!val.equalsIgnoreCase("")){
			cost = (int) Double.parseDouble(val);
		}
		return cost;
	}
	
	private void getPlanEventData(final JSONObject jsonData,final String jsonArrayName) {
		final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		if(mGuideInstance != null){
			try {
				boolean ishasEvent = jsonData.has(jsonArrayName);
				if(ishasEvent){
					JSONArray eventlist = Utils.getListFromJSON(jsonData, jsonArrayName);
					if(eventlist != null && eventlist.length() >0){
						for(int i=0;i<eventlist.length();i++){
							HashMap<String,String> showMap = new HashMap<String, String>();
							JSONObject obj = eventlist.getJSONObject(i);
							try {
								String id = "";
								String mName = "";
								String mlike = "";
								String mlock = "";
								String mEventSubscribe = "";
								String mPricingModel = "";
								String mPrice = "" ;
								String msubscribe = "";
								String mcategory = "";
								String timeStamp = "";
								String mserviceType = "";
								String mserviceId = "";
								String mstarttime = "" ;
								String mdate = "" ;
								String mdur = "" ;
								String mImage = "" ;
								String mDescription = "";
								
								String url = "";
								if(obj.has("lock")){
									mlock= obj.getString("lock");
								}

								if(obj.has("id")){
									id= obj.getString("id");
								}
								if(obj.has("name")){
									mName= obj.getString("name");
								}
								if(obj.has("like")){
									mlike= obj.getString("like");
								}
								if(obj.has("url")){
									url= obj.getString("url");
								}
								if(obj.has("pricingmodel")){
									mPricingModel = obj.getString("pricingmodel");
								}
								if(jsonData.has("servicetype")){
									mserviceType= jsonData.getString("servicetype");
								}
								if(jsonData.has("serviceid")){
									mserviceType= jsonData.getString("serviceid");
								}
								if(jsonData.has("category")){
									mcategory= jsonData.getString("category");
								}
								if(obj.has("timeStamp")){
									timeStamp= obj.getString("timeStamp");
								}
								if(jsonData.has("date")){
									mdate= jsonData.getString("date");
								}
								if(jsonData.has("startTime")){
									mstarttime= jsonData.getString("startTime");
								}
								if(jsonData.has("duration")){
									mdur= jsonData.getString("duration");
								}
								if(jsonData.has("image")){
									mImage= jsonData.getString("image");
								}
								if(jsonData.has("description")){
									mImage= jsonData.getString("description");
								}
								if(Utils.checkNullAndEmpty(mPricingModel) && mPricingModel.equalsIgnoreCase("PPV")){
									if(obj.has("price")){
										mPrice = obj.getString("price");
									}
									if(obj.has("subscribe")){
										mEventSubscribe= obj.getString("subscribe");
									}
								}else{
									mPrice ="";
									mEventSubscribe = msubscribe;
								}
								showMap.put(ScreenStyles.LIST_KEY_ID, id);
								showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
								showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
								showMap.put("servicetype", mserviceType);
								showMap.put("category", mcategory);
								showMap.put("subscribe", mEventSubscribe);
								showMap.put("urllink", url);
								showMap.put("lock", mlock);
								showMap.put("like", mlike);
								showMap.put("event", "true");
								showMap.put(ScreenStyles.LIST_KEY_PRICE, mPrice);
								showMap.put("date", mdate);
								showMap.put("starttime", mstarttime);
								showMap.put("duration", mdur);
								showMap.put("description", mDescription);
								list.add(showMap);
								
								if (jsonArrayName.equalsIgnoreCase("reminderList")) {
									CacheDockData.ReminderEventList.add(new ProgramInfo(id, mserviceId, mcategory, mPrice, mPricingModel, mserviceType, msubscribe,
										mEventSubscribe, mlock, "true", mImage, mName, mPrice, mlike, timeStamp,mdate,mstarttime,mdur,mDescription));   
								}else if (jsonArrayName.equalsIgnoreCase("recordList")) {
									CacheDockData.RecordEventList.add(new ProgramInfo(id, mserviceId, mcategory, mPrice, mPricingModel, mserviceType, msubscribe,
											mEventSubscribe, mlock, "true", mImage, mName, mPrice, mlike, timeStamp,mdate,mstarttime,mdur,mDescription));   
								}
							} catch (Exception e) {
								e.printStackTrace();
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								continue;
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
//Added by Tomesh to scroll more than 20 items
/*	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int loadedItems = firstVisibleItem + visibleItemCount;
		if (loadedItems == totalItemCount && isScroll) {
			{if(Constant.DEBUG)Log.i("OnScroll", "Getting next Slot"+"  loadedItems"+loadedItems+"  totalItemCount"+totalItemCount);
			
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("start", (totalItemCount)+"");
				list.put("limit", (totalItemCount+10)+"");
				if(!method.equalsIgnoreCase("com.port.apps.epg.Guide.sendBouquetList"))
				list.put("id", selectItemId);
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Guide");
				list.put("called", "startService");
				dispatchHashMap.add(list);
				//new AsyncDispatch(method, dispatchHashMap,true).execute();
			}
		}
		
	}
//Added by Tomesh to scroll more than 20 items
	

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_FLING)//isScroll =true;
		if(Constant.DEBUG)Log.i("OnScroll", "State Change");
	}
	*/
	
	
	
	

	
	
	
}
