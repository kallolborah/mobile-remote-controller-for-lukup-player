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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.Layout.AsyncDispatch;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.action.OverlayCancelListener;
import com.player.action.PlayOn;
import com.player.action.Subscribe;
import com.player.service.CacheDockData;
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


public class PlayList extends Layout implements OnItemClickListener,OnItemLongClickListener{

	private PlayList mPlayListInstance;
	private String TAG = "PlayList";
	private int index = 0;
	private int limit = 200;
	private ScrollList scrollAdapter = null;
	private boolean scrollFlag = false;
	private String selectItemId = "";
	private String selectTitle = "";
	private String isLock = "false";
	private String isLike = "false";
	private String isSubscribe = "false";
	private String sourceUrl = "";
	private String pricingModel;
	private String price;
	private String serviceid;
	private String servicetype;
	private String channelprice;
	private String channelname;
	private int currentPosition;
	private ListView lukupListView;
	private LinearLayout.LayoutParams standParams;
	private ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
	private HashMap<String, String> map;
	private String extention;
	private SharedPreferences setupDetails;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlayListInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setupDetails = getApplication().getSharedPreferences("SetupDetail", MODE_WORLD_WRITEABLE);
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,".CurrentScreen: "+DataStorage.getCurrentScreen());
		DataStorage.setCurrentScreen(ScreenStyles.HOME_SELECTED_TYPE);
		
		IntentFilter playlist = new IntentFilter("com.player.apps.PlayList");
		registerReceiver(mPlaylistReceiver,playlist); 
		
		requestForPlayList();
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
		if(mPlaylistReceiver != null){
			mPlayListInstance.unregisterReceiver(mPlaylistReceiver);
		}
	}
		
	/**
	 * request to get play list data 
	 */
	private void requestForPlayList() {
		try {
			String userid = setupDetails.getString("userid","1000");
			userid = mDataAccess.getCurrentUserId();
			DataStorage.setCurrentScreen(ScreenStyles.HOME_SELECTED_TYPE);
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("userid",userid);
			list.put("caller", "com.player.apps.PlayList");
			list.put("called", "startService");
			list.put("start", index+"");
			list.put("limit", limit+"");
			dispatchHashMap.add(list);
			String method = "com.port.apps.epg.Attributes.sendPlaylistData"; 
			new AsyncDispatch(method, dispatchHashMap,true).execute();
			
			mPlayListInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mContainer.removeAllViews();
					LayoutInflater  inflater = mPlayListInstance.getLayoutInflater();
					View listView = inflater.inflate(R.layout.guidelist,null);
					mContainer.addView(listView,getLinearLayoutParams());
					lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
					scrollAdapter = new ScrollList(mPlayListInstance, itemList, "event","PlayList");
					lukupListView.setAdapter(scrollAdapter);
					refreshListView(lukupListView);	
					
					//HelpTip
					HelpTip.requestForHelp(mPlayListInstance.getResources().getString(R.string.VIEW_AND_ARRANGE_YOUR_PLAYLIST),
							mPlayListInstance.getResources().getString(R.string.PLAYLIST_MSG1),mPlayListInstance);
				
					lukupListView.setOnItemClickListener(mPlayListInstance);
					lukupListView.setOnItemLongClickListener(mPlayListInstance);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
		HashMap<String, String> map = itemList.get(position);
		currentPosition = position;
		hideMenu();
		if(map != null){
			if(Constant.DEBUG)  Log.d(TAG ,"Long click");
			if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
				selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
			}
			if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
				selectItemId = map.get(ScreenStyles.LIST_KEY_ID);
			}
			isLock = map.get("lock");
			isLike = map.get("like");
			isSubscribe =map.get("subscribe");
			pricingModel = map.get("pricingmodel");
			price = map.get(ScreenStyles.LIST_KEY_PRICE);
			if(map.containsKey("urllink")){
				sourceUrl = map.get("urllink");
				extention = sourceUrl.trim().substring(sourceUrl.trim().lastIndexOf("."));
				if(Constant.DEBUG)  Log.d("onItemLongClick()","Event extention: "+extention);
			}
			if(Constant.DEBUG)  Log.d(TAG ,"selectItemId: "+selectItemId);
			
			showCustomMenu();
			return true;
		}
		return false;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		map = itemList.get(position);
		hideMenu();
		if(map != null){
			if(Constant.DEBUG)  Log.d(TAG ,"click");
			if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
				selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
			}
			if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
				selectItemId = map.get(ScreenStyles.LIST_KEY_ID);
			}
			if(Constant.DEBUG)  Log.d(TAG ,"selectItemId: "+selectItemId);
			isLock = map.get("lock");
			isSubscribe = "false";
			if(map.containsKey("subscribe")){
				isSubscribe = map.get("subscribe");
			}
			if(map.containsKey(ScreenStyles.LIST_KEY_PRICE)){
				price = map.get(ScreenStyles.LIST_KEY_PRICE);
			}
			if(map.containsKey("urllink")){
				sourceUrl = map.get("urllink");
			}
			if(map.containsKey("servicetype")){
				servicetype = map.get("servicetype");
			}
			if(map.containsKey("serviceid")){
				serviceid = map.get("serviceid");
			}
			if(map.containsKey("channelprice")){
				channelprice = map.get("channelprice");
			}
			if(map.containsKey("channelname")){
				channelname = map.get("channelname");
			}
			if(map.containsKey("pricingmodel")){
				pricingModel = map.get("pricingmodel");
			}
				
			if(Constant.DEBUG)  Log.d(TAG ,"Click Lock: "+isLock+", Subscribe: "+isSubscribe+", price: "+price+", pricingModel: "+pricingModel);
			if(Constant.DEBUG)  Log.d(TAG ,"Click channelname: "+channelname+", channelprice: "+channelprice+", serviceid: "+serviceid);
			if(Utils.checkNullAndEmpty(isLock) && !isLock.equalsIgnoreCase("true")){
				if(isSubscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
					playVideo(servicetype, serviceid, selectItemId, sourceUrl);
				}else if(pricingModel.equalsIgnoreCase("PPV")){
					Subscribe.requestForSubscriber(selectItemId,selectTitle,price,pricingModel,"subscribe","event",mPlayListInstance,mContainer,"com.player.apps.PlayList");
				}else if(pricingModel.equalsIgnoreCase("PPC")){
					Subscribe.requestForSubscriber(serviceid,channelname,channelprice,pricingModel,"subscribe","service",mPlayListInstance,mContainer,"com.player.apps.PlayList");
				}
			}else{
				HelpText.showHelpTextDialog(mPlayListInstance, mPlayListInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
			}
		}
	}
	
	private void playVideo(String servicetype, String serviceid, String eventid,String url){
		HelpTip.close(mPlayListInstance);
		if(url != null && !url.equalsIgnoreCase("")){
			extention = url.trim().substring(url.trim().lastIndexOf("."));
			Intent play = new Intent(PlayList.this, PlayBack.class);
			play.putExtra("ActivityName", "PlayList");
			play.putExtra("serviceid", serviceid);
			play.putExtra("Type", servicetype);
			play.putExtra("EventId", eventid);
			play.putExtra("List", itemList);
			if(url != null){
				play.putExtra("EventUrl", url);
			}
			startActivity(play);
		}else{
			HelpText.showHelpTextDialog(mPlayListInstance, mPlayListInstance.getResources().getString(R.string.UNABLE_TO_PLAY), 2000);
		}
		
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
	}
	
	private int MoneyConverter(String val){
		int cost = 0;
		if(!val.equalsIgnoreCase("")){
			cost = (int) Double.parseDouble(val);
		}
		return cost;
	}
	
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}
	
	private void refreshListView(final ListView lukupListView) {
		try {
			if(mPlayListInstance != null){
				mPlayListInstance.runOnUiThread(new Runnable() {
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
	
	private void showPlayList(final JSONObject jsonData,final String screenType) {
		itemList.clear();
		if(mPlayListInstance != null){
			try {
				boolean ishasEvent = jsonData.has("eventList");
				if(ishasEvent){
					JSONArray eventlist = Utils.getListFromJSON(jsonData, "eventList");
					if(eventlist != null && eventlist.length() >0){
						for(int i=0;i<eventlist.length();i++){
							try {
								HashMap<String,String> showMap = new HashMap<String, String>();
								JSONObject obj = eventlist.getJSONObject(i);
								String id = "";
								String mName = "";
								String mlike = "";
								String mlock = "";
								String mEventSubscribe = "";
								String mPricingModel = "";
								String mPrice = "" ;
								String mchannelPrice = "";
								String mchannelName = "";
								String mcategory = "";
								String mserviceType = "";
								String mserviceId = "";
								
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
								if(obj.has("channelname")){
									mchannelName= obj.getString("channelname");
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
								if(obj.has("category")){
									mcategory= obj.getString("category");
								}
								
								if(obj.has("price")){
									mPrice = obj.getString("price");
								}
								if(obj.has("channelprice")){
									mchannelPrice = obj.getString("channelprice");
								}
								if(obj.has("subscribe")){
									mEventSubscribe= obj.getString("subscribe");
								}
								if(obj.has("serviceid")){
									mserviceId= obj.getString("serviceid");
								}
								showMap.put(ScreenStyles.LIST_KEY_ID, id);
								showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
								showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
								showMap.put("servicetype", mserviceType);
								showMap.put("serviceid", mserviceId);
								showMap.put("category", mcategory);
								showMap.put("subscribe", mEventSubscribe);
								showMap.put("pricingmodel", mPricingModel);
								showMap.put("urllink", url);
								showMap.put("lock", mlock);
								showMap.put("like", mlike);
								showMap.put("event", "true");
								showMap.put("channelprice", mchannelPrice);
								showMap.put("channelname", mchannelName);
								showMap.put(ScreenStyles.LIST_KEY_PRICE, mPrice);
								itemList.add(showMap);
								if(Constant.DEBUG)  Log.d(TAG ,"showPlayList(): id: "+id+", serviceid: "+mserviceId+", pricingmodel: "+mPricingModel+", servicetype: "+mserviceType+", EventSubscribe: "+mEventSubscribe+", locked: "+mlock+", like: "+mlike+", mPrice: "+mPrice);
								
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
				mPlayListInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(itemList != null && itemList.size() >0){
							scrollAdapter.notifyDataSetChanged();
							scrollFlag = false;
						}else{
							HelpText.showHelpTextDialog(mPlayListInstance, mPlayListInstance.getResources().getString(R.string.NO_CONTENT), 3000);
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
	
	
	
	private void requestForRemoveOption(String Id ,final String subscribeOption) {
		if(Constant.DEBUG)  Log.d(TAG ,"requestForRemoveOption().Id: "+Id);
		if(mPlayListInstance != null){
			mPlayListInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						OverLay overlay = new OverLay(mPlayListInstance);
						String message = mPlayListInstance.getResources().getString(R.string.REMOVE_ITEM);
						final String title = subscribeOption;
						final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, title, message, null,null,DataStorage.isShowSelected());
						if(dialog != null){
							Button okBtn = (Button) dialog.findViewById(R.id.okButton);
							if(okBtn != null){
								okBtn.setText("YES");
								okBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										if(title.trim().equalsIgnoreCase("Remove")){
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.PlayList");
											list.put("called", "startService");
											list.put("id", selectItemId);
											list.put("state", subscribeOption);
											dispatchHashMap.add(list);
											String method = "com.port.apps.epg.Attributes.PlayList"; 
											new AsyncDispatch(method, dispatchHashMap,true).execute();
											
											if(dialog != null && dialog.isShowing()){
												dialog.dismiss();
											}
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
			customMenuValue = ScreenStyles.HOMESCREEN_PLAYLIST_MENU_ITEMS ;
			customMenuIcons = ScreenStyles.HOMESCREEN_PLAYLIST_MENU_ICONS;
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
							HelpText.showHelpTextDialog(mPlayListInstance, mPlayListInstance.getResources().getString(R.string.SOMTHING_WRONG), 3000);
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
			requestForRemoveOption(selectItemId,"remove");
			return;
		}if(key.getCaption().trim().equalsIgnoreCase("Info")){
			if(mPlayListInstance != null){
				mPlayListInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Info.requestForInfoById(selectItemId,"com.player.apps.PlayList");
					}
				});
			}
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
			showCustomMenu();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
			if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			HelpTip.close(mPlayListInstance);
			Intent lukup = new Intent(PlayList.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "PlayList");
			startActivity(lukup);
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private boolean backButtonAction(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG,"backButtonAction()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
		if(Constant.DEBUG)  Log.d(TAG ,"backButtonAction().CurrentScreen: "+DataStorage.getCurrentScreen());
			HelpTip.close(mPlayListInstance);
			if(mMenu.isShowing()){
				hideMenu();
				return true;
			}
			if (DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.HOME_SELECTED_TYPE)) {
				mContainer.removeAllViews();
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(PlayList.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "PlayList");
				startActivity(lukup);
				finish();
				return true;
			}else if(DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_INFO_SCREEN) || DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.FILTER_SCREEN)) {
				requestForPlayList();
				return true;
			}else{
				finish();
				return true;
			}
	}
	
/************************************************************************************************/
	
	public final BroadcastReceiver mPlaylistReceiver = new BroadcastReceiver() {
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
		
	private void processUIData(String handler,final JSONObject jsonData){
		try{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.dismiss();
			}
			if(Constant.DEBUG)  Log.d(TAG  , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.sendPlaylistData")){
				showPlayList(jsonData,ScreenStyles.HOME_PLAYLIST);		
				
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendEventInfo")){
				try {
					if(mPlayListInstance != null){
						mPlayListInstance.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Info.rInfoAction(jsonData, mContainer, mPlayListInstance,"com.player.apps.PlayList");
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.PlayList")){
				if(mPlayListInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							if(Constant.DEBUG)  Log.d(TAG  , "itemList currentPosition : "+currentPosition);
							itemList.remove(currentPosition);
							if(Constant.DEBUG)  Log.d(TAG  , "itemList size : "+itemList.size());
							mPlayListInstance.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if(itemList != null && itemList.size() >0){
										scrollAdapter.notifyDataSetChanged();
									}
								}
							});
							if(jsonData.has("state")){
								if(Utils.getDataFromJSON(jsonData, "state").equalsIgnoreCase("removed")){
									HelpText.showHelpTextDialog(mPlayListInstance, mPlayListInstance.getResources().getString(R.string.REMOVED), 2000);
								}else if (Utils.getDataFromJSON(jsonData, "state").equalsIgnoreCase("added")){
									HelpText.showHelpTextDialog(mPlayListInstance, mPlayListInstance.getResources().getString(R.string.ADDED), 2000);
								}
							}
						}
					}
				}
			}
			else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Subscriptions")){
				if(mPlayListInstance != null){
					try{
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							
							if(result.equalsIgnoreCase("success")){
								if(jsonData.has("id")){
									HelpText.showHelpTextDialog(mPlayListInstance, jsonData.getString("msg"), 2000);
								}
							}else{
								if(jsonData.has("msg")){
									HelpText.showHelpTextDialog(mPlayListInstance, jsonData.getString("msg"), 4000);
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
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
}
