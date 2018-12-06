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
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.Layout.AsyncDispatch;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.action.Media;
import com.player.action.OverlayCancelListener;
import com.player.action.PlayOn;
import com.player.service.CacheDockData;
import com.player.service.ProgramInfo;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
import com.player.widget.OverLay;
import com.player.widget.ScrollList;

public class Plan extends Layout implements OnItemClickListener,OnItemLongClickListener{
	private Plan mPlanInstance;
	private String TAG = "Plan";
	private int index = 0;
	private int limit = 20;
	private String currentType;
	private String method;
	private String selectItemId = "";
	private String selectTitle = "";
	private String selectType = "";
	
	private ScrollList scrollAdapter = null;
	private ListView lukupListView;
	private LinearLayout.LayoutParams standParams;
	private ArrayList<HashMap<String,String>> eventList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> defaultPlanList;
	
	private int maingrid_select_index = -1;
	private int maingrid_focus_index = -1;
	private View maingridview_focus = null;
	RelativeLayout currentlayoutMain;
	TextView currentTextView;
	private int currentPosition = -1;
	private SharedPreferences setupDetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlanInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setupDetails = getApplication().getSharedPreferences("SetupDetail", MODE_WORLD_WRITEABLE);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,".CurrentScreen: "+DataStorage.getCurrentScreen());
		
		IntentFilter plan = new IntentFilter("com.player.apps.Plan");
		registerReceiver(mPlanReceiver,plan);
		
		DataStorage.setCurrentScreen(ScreenStyles.HOME_SELECTED_TYPE);
		requestForPlan();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mPlanReceiver != null){
			mPlanInstance.unregisterReceiver(mPlanReceiver);
		}
	}
	
	
	public void requestForPlan(){
		if(Constant.DEBUG)  Log.d(TAG , "init() ");
		try {
			if(mPlanInstance != null){
				mPlanInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						defaultPlanList = new ArrayList<HashMap<String,String>>();
						mContainer.removeAllViews();
						HashMap<String, String> mainHashMap;
						if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
							for(int i=0;i<ScreenStyles.PLAN_ITEM_LIST_S.length;i++){
								mainHashMap = new HashMap<String, String>();
								mainHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.PLAN_ITEM_LIST_S[i]);
								mainHashMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
								defaultPlanList.add(mainHashMap);
							}
						}else{
							for(int i=0;i<ScreenStyles.PLAN_ITEM_LIST.length;i++){
								mainHashMap = new HashMap<String, String>();
								mainHashMap.put(ScreenStyles.LIST_KEY_TITLE, ScreenStyles.PLAN_ITEM_LIST[i]);
								mainHashMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
								defaultPlanList.add(mainHashMap);
							}
						}
						DataStorage.setCurrentScreen(ScreenStyles.INITIAL_SCREEN);
						LayoutInflater  inflater = mPlanInstance.getLayoutInflater();
						View listView = inflater.inflate(R.layout.guidelist,null);
						mContainer.addView(listView,getLinearLayoutParams());
						lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
						scrollAdapter = new ScrollList(mPlanInstance, defaultPlanList, "default","Plan");
						lukupListView.setAdapter(scrollAdapter);
						refreshListView(lukupListView);		
						
						//HelpTip
						HelpTip.requestForHelp(mPlanInstance.getResources().getString(R.string.SHORTCUTS_TO_YOUR_CONTENT),
								mPlanInstance.getResources().getString(R.string.PLAN_MSG1),mPlanInstance);
						
						lukupListView.setOnItemClickListener(mPlanInstance);
						lukupListView.setOnItemLongClickListener(mPlanInstance);
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
	

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,long arg3) {
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
			
		if(DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN)){
			if(Constant.DEBUG)  Log.d(TAG ,"onItemLongClick() Initial Screen");
		}else{
			HashMap<String, String> map = eventList.get(position);
			currentPosition = position;
			
			hideMenu();
			if(Constant.DEBUG)  Log.d(TAG ,"Long click");
			if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
				selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
			}
			if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
				selectItemId = map.get(ScreenStyles.LIST_KEY_ID);
			}	
			if(map.containsKey("event")){
				selectType = map.get("event");
			}
			showCustomMenu();
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
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
		
		if(Constant.DEBUG)  Log.d(TAG,"onItemClick(). method " + method);
		hideMenu();
		
		if(DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN)){
			if(Constant.DEBUG)  Log.d(TAG ,"onItemClick() Initial Screen");
			HashMap<String, String> map= defaultPlanList.get(position);
			if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
				String val = map.get(ScreenStyles.LIST_KEY_TITLE);
				if(val.equalsIgnoreCase("Reminders")){
					currentType = val;
					method = "com.port.apps.epg.Plan.getReminder";
					sendRequestForPlan(CacheDockData.ReminderEventList, method,val);
				}
				if(val.equalsIgnoreCase("Records")){
					currentType = val;
					method = "com.port.apps.epg.Plan.getRecord";
					CacheDockData.RecordEventList.clear();
					sendRequestForPlan(CacheDockData.RecordEventList, method,val);
				}
				if(val.equalsIgnoreCase("Favourites")){
					currentType = val;
					method = "com.port.apps.epg.Guide.sendFavouriteList";
					sendRequestForPlan(CacheDockData.favouriteEventList, method,val);
				}
			}
		}else{
			String isSubscribe = "";
			String isLock = "";
			String pricingModel = "";
			String price = "";
			String url = "";
			String serviceType = "";
			String serviceId = "";
			HashMap<String, String> map= eventList.get(position);
			if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
				selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
			}
			if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
				selectItemId = map.get(ScreenStyles.LIST_KEY_ID);
			}
			if(map.containsKey("event")){
				selectType = map.get("event");
			}
			if(map.containsKey("lock")){
				isLock = map.get("lock");
			}
			if(map.containsKey("subscribe")){
				isSubscribe = map.get("subscribe");
			}
			if(map.containsKey("pricingmodel")){
				pricingModel = map.get("pricingmodel");
			}
			if(map.containsKey("price")){
				price = map.get("price");
			}
			if(map.containsKey("servicetype")){
				serviceType = map.get("servicetype");
			}
			if(map.containsKey("urllink")){
				url = map.get("urllink");
			}
			if(map.containsKey("serviceid")){
				serviceId = map.get("serviceid");
			}
			if(Constant.DEBUG)  Log.d(TAG,"onItemClick(). selectType " + selectType);
			if(selectType.equalsIgnoreCase("true")){
//				Info.requestForInfoById(selectItemId,"com.player.apps.Plan");
				if(!isLock.equalsIgnoreCase("true")){
					if(isSubscribe.equalsIgnoreCase("true") || CommonUtil.MoneyConverter(price) == 0){
//						if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
//							Media.playVideo(serviceid, selectItemId, url,serviceType,map,mPlanInstance,"com.player.apps.Guide");
							if(url != null && !url.equalsIgnoreCase("")){
								Intent play = new Intent(mPlanInstance, PlayBack.class);
								play.putExtra("ActivityName", "com.player.apps.Guide");
								play.putExtra("EventData", map);
								play.putExtra("Type", serviceType);
								play.putExtra("EventId", selectItemId);
								play.putExtra("serviceid", serviceId);
								if(url != null){
									play.putExtra("EventUrl", url);
								}
								startActivity(play);
							}else{
								if(Constant.DEBUG)  Log.d("Info" ,"Url is Null ");
							}
//						}else{ //if not connected to the Lukup Player, play video here itself
//							Media.playOnClient(url, mContainer, mPlanInstance,serviceType);
//						}
					}else{
						if (pricingModel.equalsIgnoreCase("PPV")) {
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+ selectTitle +" "+
									mPlanInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
						}else if (pricingModel.equalsIgnoreCase("PPC")) {
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+serviceId +" "+
									mPlanInstance.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
						}
					}
				}else{
					HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
				}
			}else {
				//show channel list 
				if(map.containsKey("channelurl")){
					if(!map.get("channelurl").equalsIgnoreCase("")){ //Live title
						if(Constant.DEBUG)  Log.d(TAG,"onItemClick(). Going to play live channel from Plan " + map.get("channelurl"));
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
						list.put("type", "live");
						list.put("id", map.get("id"));
						list.put("url", map.get("channelurl"));//Added by Tomesh
						list.put("serviceid", map.get("id"));
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("caller", "com.player.apps.Guide");
						list.put("activity", "Guide");
						if(Constant.DEBUG)  Log.d(TAG , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
						list.put("called", "startActivity");
						dispatchHashMap.add(list);
						new AsyncDispatch("com.port.apps.epg.Play.PlayOn", dispatchHashMap,true).execute();
						
					}else{ //VOD channel
						dispatchHashMap  = new ArrayList<HashMap<String,String>>();
						HashMap<String, String> list = new HashMap<String, String>();
						list.put("start", index+"");
						list.put("limit", limit+"");
						list.put("id", selectItemId);
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("caller", "com.player.apps.Plan");
						list.put("called", "startService");
						dispatchHashMap.add(list);
						new AsyncDispatch("com.port.apps.epg.Guide.sendEventList", dispatchHashMap,true).execute();	
					}
				}   
				
			}
		}
		
	}
	
	private void sendRequestForPlan(ArrayList<ProgramInfo> cacheList,String method,String tag){
		if(Constant.DEBUG)  Log.d(TAG,"sendRequestForPlan(). method " + method);
		if(cacheList.size()>0){
			if(tag.equalsIgnoreCase("Reminders")){
				eventList = ArrayListOfEvent(CacheDockData.ReminderEventList);
			}else if(tag.equalsIgnoreCase("Records")){
				eventList = ArrayListOfEvent(CacheDockData.RecordEventList);
			}else {
//				eventList = ArrayListOfEvent(CacheDockData.favouriteEventList);
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
                HashMap<String, String> list = new HashMap<String, String>();
                list.put("start", index+"");
                list.put("limit", limit+"");
                list.put("consumer", "TV");
                list.put("network",mDataAccess.getConnectionType());
                list.put("caller", "com.player.apps.Plan");
                list.put("called", "startService");
                dispatchHashMap.add(list);
                new AsyncDispatch(method, dispatchHashMap,true).execute();
			}
			
			if(eventList != null && eventList.size() >0){
				DataStorage.setCurrentScreen(tag);
				lukupListView.invalidate();
				lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, tag,"Plan"));
				refreshListView(lukupListView);	
				
			}else{
				if(!tag.equalsIgnoreCase("Favourites")){
					HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
				}
			}
			
		}else {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("start", index+"");
			list.put("limit", limit+"");
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Plan");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch(method, dispatchHashMap,true).execute();
		}
	}
	
	private ArrayList<HashMap<String, String>> ArrayListOfEvent(ArrayList<ProgramInfo> pInfo){
		ArrayList<HashMap<String,String>> processList = new ArrayList<HashMap<String, String>>();
		for(int i= 0;i<pInfo.size();i++){
			HashMap<String,String> showMap = new HashMap<String, String>();
			showMap.put(ScreenStyles.LIST_KEY_ID, pInfo.get(i).getEventId());
			showMap.put(ScreenStyles.LIST_KEY_TITLE, pInfo.get(i).getEventName());
			showMap.put("date", pInfo.get(i).getEventDate());
			showMap.put("starttime", pInfo.get(i).getEventStartTime());
			showMap.put("duration", pInfo.get(i).getEventDur());
			processList.add(showMap);
		}
		return processList;
	}

	private void refreshListView(final ListView lukupListView) {
		try {
			if(mPlanInstance != null){
				mPlanInstance.runOnUiThread(new Runnable() {
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
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	private void requestForRemoveOption(String Id ,String tag) {
		if(Constant.DEBUG)  Log.d(TAG ,"requestForRemoveOption().Id: "+Id);
		if(mPlanInstance != null){
			mPlanInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						Log.d(TAG,"CurrentUserId ::"+setupDetails.getString("userid", "1000"));
						//DataStorage.setCurrentUserId(setupDetails.getString("userid", "1000"));
						DataStorage.setCurrentUserId(mDataAccess.getCurrentUserId());
						OverLay overlay = new OverLay(mPlanInstance);
						String message = mPlanInstance.getResources().getString(R.string.REMOVE_ITEM);
						final String title = mPlanInstance.getResources().getString(R.string.REMOVE_TITLE);
						final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, title, message, null,null,DataStorage.isShowSelected());
						if(dialog != null){
							Button okBtn = (Button) dialog.findViewById(R.id.okButton);
							if(okBtn != null){
								okBtn.setText("YES");
								okBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										String type = "";
										if(currentType.equalsIgnoreCase("Reminders")){
											method = "com.port.apps.epg.Plan.cancelReminder";
											type = "event";
										}else if(currentType.equalsIgnoreCase("Records")){
											method = "com.port.apps.epg.Plan.cancelRecord";
											type = "event";
										}else if(currentType.equalsIgnoreCase("Favourites")){
											method = "com.port.apps.epg.Plan.deleteFavourite";
										}
										dispatchHashMap  = new ArrayList<HashMap<String,String>>();
										HashMap<String, String> list = new HashMap<String, String>();
										list.put("consumer", "TV");
										list.put("network",mDataAccess.getConnectionType());
										list.put("id", selectItemId);
										list.put("type", selectType);
										list.put("caller", "com.player.apps.Plan");
										list.put("called", "startService");
										if(DataStorage.getCurrentUserId().equalsIgnoreCase("1000")){ 
											list.put("userid", "1000"); 
										}else{ 
											list.put("userid", DataStorage.getCurrentUserId()); 
										}
										dispatchHashMap.add(list);
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
					}
				}
			});
		}
	}
	
	private ArrayList<HashMap<String, String>> getEventData(final JSONObject jsonData,final String jsonArrayName) {
		final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		if(mPlanInstance != null){
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
								String mEvent = "";	
								String url = "";
								String channelurl = "";//Added by tomesh
								String mDescription ="";
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
								if(obj.has("servicetype")){
									mserviceType= obj.getString("servicetype");
								}
								if(obj.has("serviceid")){
									mserviceId= obj.getString("serviceid");
								}
								if(obj.has("category")){
									mcategory= obj.getString("category");
								}
								if(obj.has("timeStamp")){
									timeStamp= obj.getString("timeStamp");
								}
								if(obj.has("date")){
									mdate= obj.getString("date");
								}
								if(obj.has("startTime")){
									mstarttime= obj.getString("startTime");
								}
								if(obj.has("duration")){
									mdur= obj.getString("duration");
								}
								if(jsonData.has("image")){
									mImage= jsonData.getString("image");
								}
								if(obj.has("event")){
									mEvent= obj.getString("event");
								}
								if(obj.has("subscribe")){
									mEventSubscribe= obj.getString("subscribe");
								}
								if(obj.has("price")){
									mPrice= obj.getString("price");
								}
								if(obj.has("source")){
									channelurl= obj.getString("source");//Added By tomesh for Live Tv
								}
								if(obj.has("description")){
									mDescription = obj.getString("description");
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
								if(jsonArrayName.equalsIgnoreCase("reminderList") || jsonArrayName.equalsIgnoreCase("recordList")){
									showMap.put("event", "true");
								}else{
									showMap.put("event", mEvent);
								}
								showMap.put(ScreenStyles.LIST_KEY_PRICE, mPrice);
								showMap.put("date", mdate);
								showMap.put("starttime", mstarttime);
								showMap.put("duration", mdur);
								showMap.put("channelurl", channelurl);//Added by tomesh
								showMap.put("description", mDescription);
								showMap.put("pricingmodel", mPricingModel);
								showMap.put("price", mPrice);
								showMap.put("serviceid", mserviceId);
								list.add(showMap);
								
								if (jsonArrayName.equalsIgnoreCase("reminderList")) {
									CacheDockData.ReminderEventList.add(new ProgramInfo(id, mserviceId, mcategory, mPrice, mPricingModel, mserviceType, msubscribe,
										mEventSubscribe, mlock, "true", mImage, mName, mPrice, mlike, timeStamp,mdate,mstarttime,mdur,mDescription));   
								}else if (jsonArrayName.equalsIgnoreCase("recordList")) {
									CacheDockData.RecordEventList.add(new ProgramInfo(id, mserviceId, mcategory, mPrice, mPricingModel, mserviceType, msubscribe,
											mEventSubscribe, mlock, "true", mImage, mName, mPrice, mlike, timeStamp,mdate,mstarttime,mdur,mDescription));   
								}else if (jsonArrayName.equalsIgnoreCase("eventList")) {
									CacheDockData.favouriteEventList.add(new ProgramInfo(id, mserviceId, mcategory, mPrice, mPricingModel, mserviceType, msubscribe,
											mEventSubscribe, mlock, mEvent, mImage, mName, mPrice, mlike, timeStamp,mdate,mstarttime,mdur,mDescription));   
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
		return list;
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
				HelpTip.close(mPlanInstance);
				Intent lukup = new Intent(Plan.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Plan");
				startActivity(lukup);
				finish();
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
		
	private boolean backButtonAction(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG,"backButtonAction()  eventList Size :" + eventList.size());
		if(Constant.DEBUG)  Log.d(TAG ,"backButtonAction().CurrentScreen: "+DataStorage.getCurrentScreen());
		if(mMenu.isShowing()){
			hideMenu();
			return true;
		}
		HelpTip.close(mPlanInstance);
		if (DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN)) {
			mContainer.removeAllViews();
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent lukup = new Intent(Plan.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "Plan");
			startActivity(lukup);
			finish();
			return true;
		}else if(DataStorage.getCurrentScreen() != null && (DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_INFO_SCREEN)||DataStorage.getCurrentScreen().equalsIgnoreCase("Program"))) {
			if(mPlanInstance != null){
				mPlanInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
					mContainer.removeAllViews();
					if (Constant.DEBUG)	Log.d(TAG,"backButtonAction()currentType :" + currentType);
					DataStorage.setCurrentScreen(currentType);
					LayoutInflater  inflater = mPlanInstance.getLayoutInflater();
					View listView = inflater.inflate(R.layout.guidelist,null);
					mContainer.addView(listView,getLinearLayoutParams());
					lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
					lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, currentType,"Plan"));
					refreshListView(lukupListView);	
					
					lukupListView.setOnItemClickListener(mPlanInstance);
					lukupListView.setOnItemLongClickListener(mPlanInstance);
					}
				});
			}
					
			return true;
			
		}else if(DataStorage.getCurrentScreen().equalsIgnoreCase("Reminders") || DataStorage.getCurrentScreen().equalsIgnoreCase("Records") || DataStorage.getCurrentScreen().equalsIgnoreCase("Favourites")){
			mContainer.removeAllViews();
			requestForPlan();
			return true;
		}else if (DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.HOME_SELECTED_TYPE)) {
			if(mPlanInstance != null){
				mPlanInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
					mContainer.removeAllViews();currentType="Favourites";
					if (Constant.DEBUG)	Log.d(TAG,"backButtonAction()currentType :" + currentType);
					DataStorage.setCurrentScreen(currentType);
					LayoutInflater  inflater = mPlanInstance.getLayoutInflater();
					View listView = inflater.inflate(R.layout.guidelist,null);
					mContainer.addView(listView,getLinearLayoutParams());
					lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
					lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, currentType,"Plan"));
					refreshListView(lukupListView);	
					
					lukupListView.setOnItemClickListener(mPlanInstance);
					lukupListView.setOnItemLongClickListener(mPlanInstance);
					}
				});
			}
			return true;
		}
		return false;
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
			customMenuValue = ScreenStyles.HOMESCREEN_PLAN_MENU_ITEMS ;
			customMenuIcons = ScreenStyles.HOMESCREEN_PLAN_MENU_ICONS;
			getMenuItemsArray(customMenuValue, customMenuIcons);
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
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.SOMTHING_WRONG), 3000);
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
	}
	
	public void processMenuActions(CustomMenuItem key) {
		if(Constant.DEBUG)  Log.d(TAG , "processMenuActions() ");
		if(key.getCaption().toString().trim().equalsIgnoreCase("Remove")){
			requestForRemoveOption(selectItemId,currentType);
			return;
		}if(key.getCaption().toString().trim().equalsIgnoreCase("TV Guide")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Guide");
			list.put("called", "startActivity");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
			
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("Stop")){
			PlayOn.requestForStop("com.player.apps.Plan");
		}
		
	}
	
/************************************************************************************************/
	
	public final BroadcastReceiver mPlanReceiver = new BroadcastReceiver() {
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
					} catch (JSONException e) {
						e.printStackTrace();
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
			}
			if(Constant.DEBUG)  Log.d(TAG  , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.getReminder")){
				if(mPlanInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							boolean ishasVal = jsonData.has("reminderList");
							if(ishasVal){
								CacheDockData.ReminderEventList.clear();
								eventList = getEventData(jsonData, "reminderList");
								if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
								if(eventList != null && eventList.size() >0){
									mPlanInstance.runOnUiThread(new Runnable() {
										@Override
										public void run() {
										DataStorage.setCurrentScreen("Reminders");
										lukupListView.invalidate();
										lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, "Reminders","Plan"));
										refreshListView(lukupListView);	
										
										//HelpTip
										HelpTip.requestForHelp(mPlanInstance.getResources().getString(R.string.REMINDERS_YOU_HAVE_SETUP),
												mPlanInstance.getResources().getString(R.string.PLAN_MSG3),mPlanInstance);
										}
									});
								}else{
									HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}
							}
						}else{
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.cancelReminder")){
				if(mPlanInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							boolean ishasVal = jsonData.has("reminderList");
							if(ishasVal){
								CacheDockData.ReminderEventList.clear();
								eventList = getEventData(jsonData, "reminderList");
								if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
								if(jsonData.has("msg")){
									try {
										HelpText.showHelpTextDialog(mPlanInstance, jsonData.getString("msg"), 2000);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								mPlanInstance.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										DataStorage.setCurrentScreen("Reminders");
										lukupListView.invalidate();
										lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, "Reminders","Plan"));
										refreshListView(lukupListView);	
									}
								});
							}									
						}else{
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.getRecord")){
				if(mPlanInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							boolean ishasVal = jsonData.has("recordList");
							if(ishasVal){
								CacheDockData.RecordEventList.clear();
								eventList = getEventData(jsonData, "recordList");
								if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
								if(eventList != null && eventList.size() >0){
									mPlanInstance.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											DataStorage.setCurrentScreen("Records");
											lukupListView.invalidate();
											lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, "Records","Plan"));
											refreshListView(lukupListView);	
											
											//HelpTip
											HelpTip.requestForHelp(mPlanInstance.getResources().getString(R.string.CONTENT_RECORDED_FOR_YOU),
													mPlanInstance.getResources().getString(R.string.PLAN_MSG4),mPlanInstance);
										}
									});
								}else{
									HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}
							}
						}else{
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.cancelRecord")){
				if(mPlanInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							boolean ishasVal = jsonData.has("recordList");
							if(ishasVal){
								CacheDockData.RecordEventList.clear();
								eventList = getEventData(jsonData, "recordList");
								if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
								if(eventList != null && eventList.size() >0){
									mPlanInstance.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											DataStorage.setCurrentScreen("Records");
											lukupListView.invalidate();
											lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, "Reminders","Plan"));
											refreshListView(lukupListView);	
										}
									});
								}else if(jsonData.has("msg")){
									try {
										HelpText.showHelpTextDialog(mPlanInstance, jsonData.getString("msg"), 2000);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}							
							
						}else{
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendFavouriteList")){
				if(mPlanInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							ArrayList<HashMap<String,String>> favServiceList;
							ArrayList<HashMap<String,String>> favEventList;
							boolean ishasVal = jsonData.has("eventList");
							if(ishasVal){
								CacheDockData.favouriteEventList.clear();
								eventList.clear();
								favServiceList = new ArrayList<HashMap<String,String>>();
								favEventList = new ArrayList<HashMap<String,String>>();
								favEventList = getEventData(jsonData, "eventList");
								favServiceList =  getEventData(jsonData, "serviceList");
								eventList.addAll(favEventList);
								eventList.addAll(favServiceList);
								if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
								if(eventList != null && eventList.size() >0){
									mPlanInstance.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											DataStorage.setCurrentScreen("Favourites");
											lukupListView.invalidate();
											lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, "Favourites","Plan"));
											refreshListView(lukupListView);		
											
											//HelpTip
											HelpTip.requestForHelp(mPlanInstance.getResources().getString(R.string.CONTENT_YOU_LIKED),
													mPlanInstance.getResources().getString(R.string.PLAN_MSG2),mPlanInstance);
										}
									});
								}else{
									HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}										
							}									
						}else{
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.deleteFavourite")){
				if(mPlanInstance != null){
					
							if(jsonData.has("result")){
								String result = Utils.getDataFromJSON(jsonData, "result");
								if(result.equalsIgnoreCase("success")){
									ArrayList<HashMap<String,String>> favServiceList;
									ArrayList<HashMap<String,String>> favEventList;
									boolean ishasVal = jsonData.has("eventList");
									if(ishasVal){
										CacheDockData.favouriteEventList.clear();
										eventList.clear();
										favServiceList = new ArrayList<HashMap<String,String>>();
										favEventList = new ArrayList<HashMap<String,String>>();
										favEventList = getEventData(jsonData, "eventList");
										favServiceList =  getEventData(jsonData, "serviceList");
										eventList.addAll(favEventList);
										eventList.addAll(favServiceList);
										if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
										if(jsonData.has("msg")){
											try {
												HelpText.showHelpTextDialog(mPlanInstance, jsonData.getString("msg"), 2000);
											} catch (Exception e) {
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
											}
										}
										mPlanInstance.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												DataStorage.setCurrentScreen("Favourites");
												lukupListView.invalidate();
												lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, "Favourites","Plan"));
												refreshListView(lukupListView);
											}
										});
									}									
								}else{
									HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
								}
							}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventInfo")){
				try {
					mPlanInstance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Info.rInfoAction(jsonData, mContainer, mPlanInstance,"com.player.apps.Plan");
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventList")){
				if(mPlanInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							boolean ishasVal = jsonData.has("eventList");
							if(ishasVal){
								eventList = Guide.getEventData(jsonData, "eventList");
								if(Constant.DEBUG)  Log.d(TAG , "itemList "+eventList.size());
								mPlanInstance.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										DataStorage.setCurrentScreen("Program");
										lukupListView.invalidate();
										lukupListView.setAdapter(new ScrollList(mPlanInstance, eventList, "event","Plan"));
										refreshListView(lukupListView);	
										method = "com.port.apps.epg.Guide.sendEventList";
									}
								});
							}else{
								HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
							}
						}else{
							if(Constant.DEBUG)  Log.d(TAG , "eventList "+eventList.size());
							HelpText.showHelpTextDialog(mPlanInstance, mPlanInstance.getResources().getString(R.string.NO_CONTENT), 2000);
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Subscriptions")){
				if(mPlanInstance != null){
					try{
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							
							if(result.equalsIgnoreCase("success")){
								if(jsonData.has("id")){
									HelpText.showHelpTextDialog(mPlanInstance, jsonData.getString("msg"), 2000);
								}
							}else{
								if(jsonData.has("msg")){
									HelpText.showHelpTextDialog(mPlanInstance, jsonData.getString("msg"), 4000);
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
	
	
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		} return standParams;
	}
	
	
}
