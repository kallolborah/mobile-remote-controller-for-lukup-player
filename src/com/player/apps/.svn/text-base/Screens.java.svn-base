package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.player.R;
import com.player.Layout;
import com.player.action.DlnaRenderer;
import com.player.action.HelpTip;
import com.player.action.PlayOn;
import com.player.util.Constant;
import com.player.util.DataProvider;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.widget.HelpText;
import com.player.widget.ScrollList;

public class Screens extends Layout implements OnItemClickListener{

	private Screens mScreensInstance;
	private String TAG = "Screens";
	private int index = 0;
	private int limit = 20;
	private ScrollList scrollAdapter = null;
	@SuppressWarnings("unused")
	private boolean scrollFlag = false;
	@SuppressWarnings("unused")
	private int currentPosition;
	private ListView lukupListView;
	private LinearLayout.LayoutParams standParams;
	private ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
	private HashMap<String, String> map;
	private String selectTitle;
	private String selectItemId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mScreensInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,".CurrentScreen: "+DataStorage.getCurrentScreen());
		DataStorage.setCurrentScreen(ScreenStyles.HOME_SELECTED_TYPE);
		
		requestForScreens();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		map = itemList.get(position);
		if(map != null){
			if(Constant.DEBUG)  Log.d(TAG ,"onItemClick: "+map);
			if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
				selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
			}
			if(map.containsKey(ScreenStyles.LIST_KEY_ID)){
				selectItemId = map.get(ScreenStyles.LIST_KEY_ID);
			}
			if(Constant.DEBUG)  Log.d(TAG ,"selectTitle: "+selectTitle);
			
			if (selectTitle.equalsIgnoreCase(ScreenStyles.SCREENS_LIST[0])) {
				Intent intent = new Intent(Screens.this,PlayBack.class);
				intent.putExtra("ActivityName", "Screens");
				intent.putExtra("EventId", DataStorage.getPlayingEvent());
				intent.putExtra("serviceid", DataStorage.getPlayingService());
				intent.putExtra("Type", DataStorage.getPlayingType());
		    	startActivity(intent);
		    	finish();
				
			} else if (selectTitle.equalsIgnoreCase(ScreenStyles.SCREENS_LIST[1])) {
				if(DataStorage.isRunningStatus()){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Guide");
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
				}
				Intent intent = new Intent(Screens.this,Navigator.class);
				intent.putExtra("ActivityName", "Screens");
		    	startActivity(intent);
		    	finish();
			} else if (selectTitle.contains(ScreenStyles.SCREENS_LIST[2])) {
				PlayOn.requestForStop("com.player.apps.Screens");
//				DataStorage.setA2dpDevice("");
				Intent intent = new Intent(Screens.this,com.player.apps.AppGuide.class);
				intent.putExtra("ActivityName", "Screens");
		    	startActivity(intent);
				finish();
			} else if (selectTitle.contains(ScreenStyles.SCREENS_LIST[3])) {
				Intent intent = new Intent (Screens.this,DlnaRenderer.class);
				intent.putExtra("sourceUrl", "cancel");
				startActivity(intent);
				finish();
			} else if (selectTitle.contains(ScreenStyles.SCREENS_LIST[4])) {
				if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
				if(Constant.DVB){
					Intent intent = new Intent(Screens.this,TVRemote.class);
					intent.putExtra("ActivityName", "Screens");
					intent.putExtra("operatorName", DataStorage.getConnectedVendor());
					startActivity(intent);
					finish();
				}else {
					Intent intent = new Intent(Screens.this,PlayBack.class);
					intent.putExtra("ActivityName", "Screens");
					intent.putExtra("EventId", DataStorage.getPlayingEvent());
					intent.putExtra("serviceid", DataStorage.getPlayingService());
					intent.putExtra("Type", DataStorage.getPlayingType());
					startActivity(intent);
					finish();
				}
				
				
			}
//			else if (selectTitle.contains(ScreenStyles.SCREENS_LIST[5])) {
//				if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
//				mDataAccess.updateSetupDB(DataProvider.IsDlnaPlaying, "false");
//				mDataAccess.updateSetupDB(DataProvider.DlnaDeviceName, "");
//				DataStorage.setDlnaPlaying(false);
//				DataStorage.setDLNAdevice("");
//				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//				HashMap<String, String> list = new HashMap<String, String>();
//				list.put("consumer", "TV");
//				list.put("network",mDataAccess.getConnectionType());
//				list.put("caller", "com.player.apps.Screens");
//				list.put("called", "startService");
//				dispatchHashMap.add(list);
//				new AsyncDispatch("com.port.apps.epg.Devices.StopDLNA", dispatchHashMap,false).execute();
//				Intent intent = new Intent(Screens.this,com.player.apps.AppGuide.class);
//				intent.putExtra("ActivityName", "Screens");
//		    	startActivity(intent);
//		    	finish();			
//		    }
		}
	}
	
	private void requestForScreens() {
		try {
			getListItem();
			mScreensInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mContainer.removeAllViews();
					LayoutInflater  inflater = mScreensInstance.getLayoutInflater();
					View listView = inflater.inflate(R.layout.guidelist,null);
					mContainer.addView(listView,getLinearLayoutParams());
					lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
					scrollAdapter = new ScrollList(mScreensInstance, itemList, "screen","Screens");
					lukupListView.setAdapter(scrollAdapter);
					refreshListView(lukupListView);	
					
					//HelpTip
					HelpTip.requestForHelp(mScreensInstance.getResources().getString(R.string.VIEW_AND_ARRANGE_YOUR_PLAYLIST),
							mScreensInstance.getResources().getString(R.string.SCREENS),mScreensInstance);
				
					lukupListView.setOnItemClickListener(mScreensInstance);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	private void getListItem(){
		if(Constant.DEBUG)  Log.d(TAG,"getListItem()");
		ArrayList<String> listItem = new ArrayList<String>();
		try {
			boolean runningStatus = DataStorage.isRunningStatus();	// video running status
			String playOnStatus = DataStorage.isPlaying();		//none(00), only video(01), only audio(10), both(11)
			String videoTypeStatus = DataStorage.getPlayingType();		//vod or live
			String isDlnaPlaying = mDataAccess.getIsDlnaPlaying() ;
			//DataStorage.isDlnaPlaying();
			listItem.add(ScreenStyles.SCREENS_LIST[1]);
			if (runningStatus) {
				if (videoTypeStatus != null && videoTypeStatus.equalsIgnoreCase("vod")) {
					listItem.add(ScreenStyles.SCREENS_LIST[0]);
				} else {
					if(Constant.DVB){
						listItem.add(ScreenStyles.SCREENS_LIST[4]);
					}else{
						listItem.add(ScreenStyles.SCREENS_LIST[0]);
					}
				}
			} 
			
			if (playOnStatus.equalsIgnoreCase("10")) {
				listItem.add(ScreenStyles.SCREENS_LIST[2] + DataStorage.getA2dpDevice());
			} else if (playOnStatus.equalsIgnoreCase("01")) {
				listItem.add(ScreenStyles.SCREENS_LIST[3] + DataStorage.getWifiDisplayDevice());
			} else if (playOnStatus.equalsIgnoreCase("11")) {
				listItem.add(ScreenStyles.SCREENS_LIST[2] + DataStorage.getA2dpDevice());
				listItem.add(ScreenStyles.SCREENS_LIST[3] + DataStorage.getWifiDisplayDevice());
			}
			if(isDlnaPlaying != null && isDlnaPlaying.equalsIgnoreCase("true")){
				listItem.add(ScreenStyles.SCREENS_LIST[3]+ mDataAccess.getDlnaDeviceName());//DataStorage.getDLNAdevice()
			}
			
			if (listItem.size() > 0) {
				for(int i=0;i<listItem.size();i++){
					HashMap<String,String> value = new HashMap<String, String>();
					value.put(ScreenStyles.LIST_KEY_TITLE, listItem.get(i));
					value.put("id", i+"");
					itemList.add(value);
				}
			}
			else{	//23 feb 2015
				HelpText.showHelpTextDialog(mScreensInstance, mScreensInstance.getResources().getString(R.string.SCREEN_NO_CONTENT), 2000);
//				if (Constant.DEBUG)	Log.d(TAG, "getListItem()  listItem.size():" + listItem.size());
//				Intent lukup = new Intent(Screens.this, com.player.apps.AppGuide.class);
//				lukup.putExtra("ActivityName", "Screens");
//				startActivity(lukup);
//				finish();
			}
			if(Constant.DEBUG)  Log.d(TAG,"getListItem() size: "+itemList.size());
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
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
	}
	
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}
	
	private void refreshListView(final ListView lukupListView) {
		try {
			if(mScreensInstance != null){
				mScreensInstance.runOnUiThread(new Runnable() {
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
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			Intent lukup = new Intent(Screens.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "Screens");
			startActivity(lukup);
			finish();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
			if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent lukup = new Intent(Screens.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "Screens");
			startActivity(lukup);
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	

}
