/*
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/
package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.player.R;
import com.player.Layout;
import com.player.Player;
import com.player.action.DlnaRenderer;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.action.Like;
import com.player.action.PlanRequest;
import com.player.action.PlayOn;
import com.player.action.Playlist;
import com.player.network.ir.IRTransmitter;
import com.player.service.CacheDockData;
import com.player.service.ProgramInfo;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.OperatorKeys;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;


/**
 * @author abhijeet
 *
 */
public class PlayBack extends Layout{

	private PlayBack mPlayBackInstance;
	private ArrayList<HashMap<String,String>> dispatchHashMap = null;
	private String TAG = "PlayBack";
	
	private String selectedUrl = "";
	private String selectedId = "";
	private String ServiceType = "";
	private String selectTitle = "";
	private String selectedSubType = "";
	private String SelectedIsLiked = "";
	private String price;
	private String subscribe;
	private String pricingModel;
	private String serviceId;
	private ArrayList<HashMap<String, String>> playlist;
	private HashMap<String, String> eventData;
	int playListPos;
	private String extention ="";
	private String state = "play";
	private int totaldur;
	private int currentdur;
	private String ActivityName = null;	
	private String starttime = "";
	private  Dialog dialog ;
	private String dlnadevice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlayBackInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 

		IntentFilter playback = new IntentFilter("com.player.apps.PlayBack");
		registerReceiver(mPlaybackReceiver,playback); 
		
		Bundle br = this.getIntent().getExtras();
		
		if (br != null) {
			if (br.containsKey("ActivityName")) {
				ActivityName = br.getString("ActivityName");
				DataStorage.setCurrentActivity(ActivityName);
				if(Constant.DEBUG)  Log.d(TAG  , "ActivityName: "+ActivityName);
			}
			if (br.containsKey("EventUrl")) {
				selectedUrl = br.getString("EventUrl");
			}
			if (br.containsKey("EventId")) {
				selectedId = br.getString("EventId");
				DataStorage.setPlayingEvent(selectedId);
			}
			if (br.containsKey("serviceid")) {
				serviceId = br.getString("serviceid");
				DataStorage.setPlayingService(serviceId);
			}
			if(br.containsKey("price")){
				price = br.getString("price");
			}
			if(br.containsKey("pricingmodel")){
				pricingModel = br.getString("pricingmodel");
			}
			if (br.containsKey("Type")) {
				ServiceType = br.getString("Type"); //vod or live
				DataStorage.setPlayingType(ServiceType);
			}
			if (br.containsKey("SubType")) {
				selectedSubType = br.getString("SubType"); //music, video, image
			}
			if (br.containsKey("EventLike")) {
				SelectedIsLiked = br.getString("EventLike");
			}
			if (br.containsKey("dlnadevice")) {
				dlnadevice = br.getString("dlnadevice");
			}
			
			if (br.containsKey("EventData")) {
				eventData = (HashMap<String, String>) getIntent().getSerializableExtra("EventData");
				if(Constant.DEBUG)  Log.d(TAG  , "eventData : "+eventData.size());
				eventValue(eventData);
			}
		
			if (br.containsKey("List")) {
				playlist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("List");
				if(Constant.DEBUG)  Log.d(TAG  , "playlist: "+playlist.size());
			}
		}	
		
		if (!ActivityName.equalsIgnoreCase("Screens")) {
			
			//HelpTip
			HelpTip.requestForHelp(mPlayBackInstance.getResources().getString(R.string.PLAYBACK),
					mPlayBackInstance.getResources().getString(R.string.PLAYBACK_MSG1),mPlayBackInstance);
			playMethod(serviceId, selectedId, ServiceType, selectedSubType, selectedUrl, starttime);
	        showMediaContoller(state,currentdur,totaldur);
		}else{
			if(Constant.DEBUG)  Log.d(TAG , "onCreate() ");
		}
	}	
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG , "onStart() ");
		if(Constant.DEBUG)  Log.d(TAG ,"Previous screen : "+DataStorage.getCurrentScreen());
			
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.DEBUG)	Log.d(TAG, "onResume()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(Constant.DEBUG)  Log.d(TAG , "onPause(), Status: "+DataStorage.isRunningStatus());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mPlaybackReceiver != null){
			mPlayBackInstance.unregisterReceiver(mPlaybackReceiver);
		}
	}
	
	
	private void eventValue(HashMap<String, String> map){
		if(map.containsKey("id")){
			selectedId = map.get("id");
			DataStorage.setPlayingEvent(selectedId);
		}
		if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
			selectTitle = map.get(ScreenStyles.LIST_KEY_TITLE);
		}
		if(map.containsKey("servicetype")){
			ServiceType = map.get("servicetype");
			DataStorage.setPlayingType(ServiceType);
		}
		if(map.containsKey("subscribe")){
			subscribe = map.get("subscribe");
		}
		if(map.containsKey("pricingmodel")){
			pricingModel = map.get("pricingmodel");
		}
		if(map.containsKey("like")){
			SelectedIsLiked = map.get("like");
		}
		if(map.containsKey("price")){
			price = map.get("price");
		}//starttime
		if(map.containsKey("starttime")){
			starttime = map.get("starttime");
		}
		if(map.containsKey("serviceid")){
			serviceId = map.get("serviceid");
			DataStorage.setPlayingService(serviceId);
		}
		if(map.containsKey("category")){
			selectedSubType = map.get("category");
		}
		if(Constant.DEBUG)  Log.d(TAG , "eventValue() selectedId: "+selectedId+", ServiceType: "+ServiceType+", subscribe: "+subscribe+", serviceId: "+serviceId);
		if(Constant.DEBUG)  Log.d(TAG , "eventValue() pricingModel: "+pricingModel+", SelectedIsLiked: "+SelectedIsLiked+", price: "+price);
	}
	
	
	private void playMethod(String serviceId, String id,String type,String subtype,String url, String starttime){
		if(Constant.DEBUG)  Log.d(TAG , "playMethod() serviceId: "+serviceId+", EventId: "+id);
		if(!"recorded".equalsIgnoreCase(subtype)){
			if(!"".equalsIgnoreCase(serviceId) && !"".equalsIgnoreCase(id)){
				if(selectedUrl !=null && !selectedUrl.equalsIgnoreCase("")){
		        	DataStorage.setPlayingUrl(selectedUrl);
		        }
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("type", type);
				list.put("subtype", subtype);
				list.put("id", id);
				list.put("serviceid", serviceId);
				list.put("pricingmodel", pricingModel);
				list.put("url", url);
				list.put("starttime", starttime);
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Guide");
				if (ActivityName != null && !ActivityName.equalsIgnoreCase("")) {
					list.put("activity", ActivityName);
				}
				if(Constant.DEBUG)  Log.d(TAG , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
				list.put("called", "startActivity");
				
				dispatchHashMap.add(list);
				String method = "com.port.apps.epg.Play.PlayOn"; 
				new AsyncDispatch(method, dispatchHashMap,true).execute();
				
				new OperatorKeys(this.getApplicationContext());
			}else{
				HelpText.showHelpTextMessage(mActivity, "Problem playing selected item.", 4000);
			}
		}else{
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("type", type);
			list.put("subtype", subtype);
			list.put("id", id);
			list.put("serviceid", serviceId);
			list.put("pricingmodel", pricingModel);
			list.put("url", url);
			list.put("starttime", starttime);
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Storage");
			if (ActivityName != null && !ActivityName.equalsIgnoreCase("")) {
				list.put("activity", ActivityName);
			}
			if(Constant.DEBUG)  Log.d(TAG , "Starting Record Play "+ url);
			list.put("called", "startActivity");
			
			dispatchHashMap.add(list);
			String method = "com.port.apps.epg.Play.PlayOn"; 
			new AsyncDispatch(method, dispatchHashMap,true).execute();
		}
	}
	
	
	/**
	 * Show Media controller option in Container
	 * @param state 
	 */
	private void showMediaContoller(final String state,final int currentPosition,final int totalDuration) {
		try {
			if(Constant.DEBUG)  Log.d(TAG , "showMediaContoller() ");
			if(mPlayBackInstance != null){
				mPlayBackInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
					if(mPlayBackInstance != null && mContainer != null){
						mContainer.removeAllViews();
						DataStorage.setCurrentScreen(ScreenStyles.MEDIA_CONTROL_SCREEN);
						LayoutInflater inflater = mPlayBackInstance.getLayoutInflater();
						View v =inflater.inflate(R.layout.media_element, null);
						if(v!= null){
							LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
							params.topMargin = customLayout.getConvertedHeight(170);
							final Drawable play = mPlayBackInstance.getResources().getDrawable(R.drawable.v13_ico_player_play_01);
							final Drawable pause = mPlayBackInstance.getResources().getDrawable(R.drawable.v13_ico_player_pause_01);
							final Button playBtn = (Button) v.findViewById(R.id.playBtn);
							final Button stopBtn = (Button) v.findViewById(R.id.stopBtn);
		
							final TextView timerText = (TextView) v.findViewById(R.id.mediaTimeText);
							SeekBar mMedicontrolerSeek = (SeekBar) v.findViewById(R.id.mediacontrolerSeek);
		
							if(mMedicontrolerSeek != null){
								int progress = 0;
								if(currentPosition != -1 && totalDuration != -1){
									progress = Utils.getProgressPercentage(currentPosition, totalDuration);
								}
								if(progress != 0){
									mMedicontrolerSeek.setProgress(progress);
								}
								mMedicontrolerSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
									@Override
									public void onStopTrackingTouch(SeekBar seekBar) {
										int arg1 = seekBar.getProgress();
										dispatchHashMap  = new ArrayList<HashMap<String,String>>();
										HashMap<String, String> list = new HashMap<String, String>();
										list.put("seekto", arg1+"");
										list.put("consumer", "TV");
										list.put("network",mDataAccess.getConnectionType());
										list.put("caller", "com.player.apps.PlayBack");
										list.put("called", "startActivity");
										dispatchHashMap.add(list);
										if(DataStorage.getPlayingType().equalsIgnoreCase("live")){
											String method = "com.port.apps.epg.Play.timeShift";
											new AsyncDispatch(method, dispatchHashMap,false).execute();
										}else{
											String method = "com.port.apps.epg.Play.seekTo";
											new AsyncDispatch(method, dispatchHashMap,false).execute();
										}
										
									}
		
									@Override
									public void onStartTrackingTouch(SeekBar seekBar) {
									}
		
									@Override
									public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		
									}
								});
							}
							if(timerText != null){
								String time = "00:00:00";
//								if(totalDuration > 0){
//									time = CommonUtil.milliSecondsToTimer(totalDuration);
//								}else{
//									time = "00:00:00";
//								}
									
								if(currentPosition > 0 && totalDuration > 0){
									int dur = totalDuration - currentPosition;
									if (dur > 0) {
										time = CommonUtil.milliSecondsToTimer(dur);
									}else{
										time = "00:00:00";
									}
								}
								
//								if(totalDuration > 0){
//									time = CommonUtil.milliSecondsToTimer(totalDuration);
//								}else{
//									time = "00:00:00";
//								}
								
								if(Constant.DEBUG)  Log.d(TAG, "Media Time "+time);
								timerText.setText(time);
							}else{
								if(Constant.DEBUG)  Log.d(TAG, "Media Timer NUll ");
							}
							if(playBtn != null){
//								playBtn.setBackgroundDrawable(pause);
								if(Utils.checkNullAndEmpty(state)){
									if(DataStorage.getPlayingType().equalsIgnoreCase("live")){
										playBtn.setBackgroundDrawable(play);
									}else{
										if(state.equalsIgnoreCase("play")){
											playBtn.setBackgroundDrawable(pause);
										}else if(state.equalsIgnoreCase("pause")){
											playBtn.setBackgroundDrawable(play);
										}
									}
								}
								playBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										if(playBtn.getBackground() == play){
											playBtn.setBackgroundDrawable(pause);
											
											if(DataStorage.getPlayingType().equalsIgnoreCase("Live")){
//												dialog = new Dialog(PlayBack.this);
//												dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//												dialog.setContentView(R.layout.overlay_confirm);
//												Button startOver = (Button)dialog.findViewById(R.id.okButton);
//												Button pause = (Button)dialog.findViewById(R.id.overlayCancelButton);
//												ImageView image = (ImageView)dialog.findViewById(R.id.alertlogo);
//												image.setVisibility(View.GONE);
//												startOver.setVisibility(View.VISIBLE);
//												startOver.setText(getResources().getString(R.string.STARTOVER));
//												pause.setVisibility(View.VISIBLE);
//												pause.setText(getResources().getString(R.string.Pause));
//												TextView message = (TextView)dialog.findViewById(R.id.messageView);
//												message.setText(getResources().getString(R.string.STARTOVER_MESSAGE));
//												
//												startOver.setOnClickListener(new OnClickListener() {
//													
//													@Override
//													public void onClick(View v) {
														
														dispatchHashMap  = new ArrayList<HashMap<String,String>>();
														HashMap<String, String> list = new HashMap<String, String>();
														list.put("state", "play");
														list.put("consumer", "TV");
														list.put("network",mDataAccess.getConnectionType());
														list.put("starttime", starttime);
														list.put("duration", Integer.toString(totalDuration));
														list.put("caller", "com.player.apps.PlayBack");
														list.put("called", "startActivity");
														dispatchHashMap.add(list);
														String method = "com.port.apps.epg.Play.doStartOver"; 
														new AsyncDispatch(method, dispatchHashMap,false).execute();
														
//														dialog.dismiss();
//													}
//												});
//												pause.setOnClickListener(new OnClickListener() {
//													
//													@Override
//													public void onClick(View v) {
//														dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//														HashMap<String, String> list = new HashMap<String, String>();
//														list.put("state", "pause");
//														list.put("consumer", "TV");
//														list.put("network",mDataAccess.getConnectionType());
//														list.put("caller", "com.player.apps.PlayBack");
//														list.put("called", "startActivity");
//														dispatchHashMap.add(list);
//														String method = "com.port.apps.epg.Play.playPauseToggle"; 
//														new AsyncDispatch(method, dispatchHashMap,false).execute();
//														dialog.dismiss();
//													}
//												});
//												dialog.show();
											}else {
												dispatchHashMap  = new ArrayList<HashMap<String,String>>();
												HashMap<String, String> list = new HashMap<String, String>();
												list.put("state", "play");
												list.put("consumer", "TV");
												list.put("network",mDataAccess.getConnectionType());
												list.put("caller", "com.player.apps.PlayBack");
												list.put("called", "startActivity");
												dispatchHashMap.add(list);
												String method = "com.port.apps.epg.Play.playPauseToggle"; 
												new AsyncDispatch(method, dispatchHashMap,false).execute();
											}
										}else{
											playBtn.setBackgroundDrawable(play);
											
											dispatchHashMap  = new ArrayList<HashMap<String,String>>();
											HashMap<String, String> list = new HashMap<String, String>();
											list.put("state", "pause");
											list.put("consumer", "TV");
											list.put("network",mDataAccess.getConnectionType());
											list.put("caller", "com.player.apps.PlayBack");
											list.put("called", "startActivity");
											dispatchHashMap.add(list);
											String method = "com.port.apps.epg.Play.playPauseToggle"; 
											new AsyncDispatch(method, dispatchHashMap,false).execute();
										}
									}
								});
							}
							
							stopBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									playBtn.setBackgroundDrawable(play);
//									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//									HashMap<String, String> list = new HashMap<String, String>();
//									list.put("state", "stop");
//									list.put("consumer", "TV");
//									list.put("network",mDataAccess.getConnectionType());
//									list.put("caller", "com.player.apps.PlayBack");
//									list.put("called", "startActivity");
//									dispatchHashMap.add(list);
//									String method = "com.port.apps.epg.Play.playPauseToggle"; 
//									new AsyncDispatch(method, dispatchHashMap,false).execute();
									
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("caller", "com.player.apps.Guide");
									list.put("called", "startActivity");
									dispatchHashMap.add(list);
									new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
									
									if(ActivityName.equalsIgnoreCase("Screens")){
										Intent intent = new Intent(PlayBack.this, AppGuide.class);
										intent.putExtra("ActivityName", "PlayBack");
										startActivity(intent);
									}									
									finish();
								}
							});
							
							mContainer.addView(v,params);
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
				if(Constant.DEBUG)  Log.d(TAG ,"ServiceType: "+ServiceType);
				if(ServiceType.equalsIgnoreCase("live")){
					if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
						customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_RUNNING_S;
						customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_RUNNING_S;
					}else{
						customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_LIVE_RUNNING;
						customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_LIVE_RUNNING;
					}
				}else if(ServiceType.equalsIgnoreCase("vod")){
					if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
						customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_RUNNING_S;
						customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_RUNNING_S;
					}else{
						customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_VOD_RUNNING;
						customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_VOD_RUNNING;

					}		
				}else if(ServiceType.equalsIgnoreCase("personal")){
					if(selectedSubType.equalsIgnoreCase("videos")){
						if(DataStorage.getDeviceType() != null && DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")){
							customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_PERSONAL_AV_RUNNING_S;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_PERSONAL_AV_RUNNING_S;
						}else{
							customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_PERSONAL_AV_RUNNING;
							customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_PERSONAL_AV_RUNNING;
						}
					}else{
						customMenuValue = ScreenStyles.MENU_ITEM_IF_EVENT_PERSONAL_PHOTO_RUNNING;
						customMenuIcons = ScreenStyles.MENU_ICONS_IF_EVENT_PERSONAL_PHOTO_RUNNING;
					}
				}
				
				setMenuBasedOnLike();
				setMenuBasedOnPlayon();
				getMenuItemsArray(customMenuValue,customMenuIcons);
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
	
	
	/************ Based on like option change the Toggle MenuItem *****************/
	private void setMenuBasedOnLike() {
		try {
			if(SelectedIsLiked != null){
				if(customMenuValue != null && SelectedIsLiked.equalsIgnoreCase("true")){
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
				if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnLike():"+SelectedIsLiked);
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
			if (selectedUrl.trim().contains(".")) {
				extention = selectedUrl.trim().substring(selectedUrl.trim().lastIndexOf("."));
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
		} catch (Exception e) {
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
			return backButtonAction(keyCode, event);
		}
		if (keyCode == KeyEvent.KEYCODE_MENU ) {
			showCustomMenu();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			HelpTip.close(mPlayBackInstance);
			Intent lukup = new Intent(PlayBack.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "PlayBack");
			startActivity(lukup);
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private boolean backButtonAction(int keyCode, KeyEvent event) {
		if(progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		if(Constant.DEBUG)  Log.d(TAG ,"backButtonAction().CurrentScreen: "+DataStorage.getCurrentScreen());
		if (!ActivityName.equalsIgnoreCase("Navigator") && DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.MEDIA_CONTROL_SCREEN)) {
			HelpTip.close(mPlayBackInstance);
			mContainer.removeAllViews();	
			if(ActivityName.equalsIgnoreCase("Screens")){
				Intent intent = new Intent(PlayBack.this,Screens.class);
				startActivity(intent);
				finish(); return true;
			}
			finish();
			return true;
		}else if(DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_INFO_SCREEN)) {
			showMediaContoller(state,currentdur,totaldur);		
			return true;
		}
		return false;
	}
	
	
	/**
	 * process ContextMenu Actions
	 * @param key
	 */
	public void processMenuActions(CustomMenuItem key) {
		if(Constant.DEBUG)  Log.d(TAG , "processMenuActions() ");
		if(key.getCaption().trim().equalsIgnoreCase("Info")){
			DataStorage.setCurrentScreen(ScreenStyles.REMOTE_INFO_SCREEN);
			Info.requestForInfoById(selectedId,"com.player.apps.PlayBack");
			return;
		}
		if(key.getCaption().toString().trim().equalsIgnoreCase("AddtoPlaylist") || key.getCaption().toString().trim().equalsIgnoreCase("Playlist")){
			Playlist.requestForAddToPlaylist(selectedId,"com.player.apps.PlayBack");
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Like")){
			Like.requestForLike(selectedId, key.getCaption().toString().trim(), "event","com.player.apps.PlayBack");
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Unlike")){
			Like.requestForLike(selectedId, key.getCaption().toString().trim(), "event","com.player.apps.PlayBack");
			return;
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Organise")){
			if(Constant.DEBUG)  Log.d(TAG ,"Pre.CurrentScreen: "+DataStorage.getCurrentScreen());
			HelpTip.close(mPlayBackInstance);
			Intent org = new Intent(PlayBack.this, Organise.class);
			org.putExtra("ActivityName", "Guide");
			org.putExtra("Type", "event");
			org.putExtra("id", selectedId);
			startActivity(org);
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Record")){
			PlanRequest.recordEvent(selectedId,"com.player.apps.PlayBack");
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("share")){
			
		}
		
//		if(key.getCaption().toString().trim().equalsIgnoreCase("TV Guide")){
//			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//			HashMap<String, String> list = new HashMap<String, String>();
//			list.put("consumer", "TV");
//			list.put("network",mDataAccess.getConnectionType());
//			list.put("caller", "com.player.apps.PlayBack");
//			list.put("called", "startActivity");
//			dispatchHashMap.add(list);
//			new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
//			
//			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
//			finish(); //go back to Navigator
//		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Play On")){
			
			if(DataStorage.getPlayingUrl() != null && !DataStorage.getPlayingUrl().equalsIgnoreCase("")){
				String isAudio = DataStorage.getPlayingUrl().trim().substring(DataStorage.getPlayingUrl().trim().lastIndexOf("."));
				if(isAudio.trim().equalsIgnoreCase(".mp3")){
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.PlayBack");
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
				}
			}

			extention = selectedUrl.trim().substring(selectedUrl.trim().lastIndexOf("."));
			if(extention.trim().equalsIgnoreCase(".mp3")){
				PlayOn.getConnectedDevices(selectedId,"audio","com.player.apps.PlayBack",mPlayBackInstance);
			}else if(extention.trim().equalsIgnoreCase(".mp4")){
				//PlayOn.getRemoteDisplays(selectItemId,"com.player.apps.Guide");
				Intent intent = new Intent(PlayBack.this,DlnaRenderer.class);
				intent.putExtra("sourceUrl", selectedUrl);
				startActivity(intent);
			}else {
				HelpText.showHelpTextDialog(mPlayBackInstance, mPlayBackInstance.getResources().getString(R.string.PLAY_ERROR), 5000);
			}
			
					
		}
		
		if(key.getCaption().toString().trim().equalsIgnoreCase("Stop")){
			String extention = selectedUrl.trim().substring(selectedUrl.trim().lastIndexOf("."));
			if(extention.trim().equalsIgnoreCase(".mp3")){
				PlayOn.requestForStop("com.player.apps.PlayBack");
				DataStorage.setA2dpDevice("");
			}else{
				PlayOn.stopWifiDisplay("com.player.apps.PlayBack");
				DataStorage.setWifiDisplayDevice("");
			}
		}
	}
	
	
	
	
	
/************************************************************************************************/
	
	public final BroadcastReceiver mPlaybackReceiver = new BroadcastReceiver() {
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
	
	/**
	 * process information from Mediaplayer based on screen id
	 * @param screenId
	 * @param jsonData
	 */
	public void processUIData(String handler,final JSONObject jsonData){
		try{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.dismiss();
			}
			if(Constant.DEBUG)  Log.d(TAG  , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Play.mediaController")){
				if(mPlayBackInstance != null){
					String state = "";
					if(jsonData.has("state")){
						state = Utils.getDataFromJSON(jsonData, "state");
					}
					int currentposition = -1;
					int totalDuration = -1;
					if(jsonData.has("position")){
						try {
							currentposition = Integer.parseInt(Utils.getDataFromJSON(jsonData, "position"));
						}catch (Exception e) {
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
					}
					if(jsonData.has("duration")){
						try {
							totalDuration = Integer.parseInt(Utils.getDataFromJSON(jsonData, "duration"));
						}catch (Exception e) {
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
					}
					showMediaContoller(state,currentposition,totalDuration);
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Play.PlayOn")){
				if(mPlayBackInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									if(ActivityName != null && ActivityName.equalsIgnoreCase("PlayList")){
										for(int i = 0;i < playlist.size();i++){
											if (Constant.DEBUG)	Log.d(TAG,"selectedId 1 = "+ playlist.get(i).get(ScreenStyles.LIST_KEY_ID));
											if(selectedId.equalsIgnoreCase(playlist.get(i).get(ScreenStyles.LIST_KEY_ID))){
												playListPos = i;
												if (Constant.DEBUG)	Log.d(TAG,"playListPos  = "+playListPos);
												if (Constant.DEBUG)	Log.d(TAG,"selectedId 2 = "+ playlist.get(i).get(ScreenStyles.LIST_KEY_ID));
											}
										}
										if(playlist.size()-1 > playListPos){
											HashMap<String, String> map = playlist.get(++playListPos);
											if(map.containsKey("id")){
												selectedId = map.get("id");
											}if(map.containsKey("servicetype")){
												ServiceType  = map.get("servicetype");
											}if(map.containsKey("subtype")){
												selectedSubType  = map.get("subtype");
											}if(map.containsKey("serviceid")){
												serviceId = map.get("serviceid");
											}if(map.containsKey("urllink")){
												selectedUrl = map.get("urllink");
											}if(map.containsKey("starttime")){
												starttime = map.get("starttime");
											}
											playMethod(serviceId, selectedId, ServiceType, selectedSubType, selectedUrl, starttime);
										}
										
									}else{
										String message = mPlayBackInstance.getResources().getString(R.string.MESSAGE_FOR_REPLAY);
										if(jsonData != null){
											if(jsonData.has("msg")){
												message = jsonData.getString("msg");
											}
										}
//												videoReplayDialog(message);
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
				if(mPlayBackInstance != null){
					if(jsonData.has("result")){
						String result = Utils.getDataFromJSON(jsonData, "result");
						if(result.equalsIgnoreCase("success")){
							if(SelectedIsLiked.equalsIgnoreCase("false")){
								SelectedIsLiked = "true";
							}else{
								SelectedIsLiked = "false";
							}
							if(jsonData.has("msg")){
								try {
									cleanList();
									HelpText.showHelpTextDialog(mPlayBackInstance, jsonData.getString("msg"), 2000);
								} catch (Exception e) {
									e.printStackTrace();
									StringWriter errors = new StringWriter();
									e.printStackTrace(new PrintWriter(errors));
									SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
								}
							}
						}else{
							if(jsonData.has("msg")){
								try {
									HelpText.showHelpTextDialog(mPlayBackInstance, jsonData.getString("msg"), 2000);
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
			} else if (handler.equalsIgnoreCase("com.port.apps.epg.Play.lockStatus")){
				if(mPlayBackInstance != null){
					if(jsonData.has("state")){
						if(Constant.DEBUG)  Log.d(TAG , "processUIData serviceId  "+serviceId + " Status: "+DataStorage.isRunningStatus());
						String state = Utils.getDataFromJSON(jsonData, "state");
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
							HelpText.showHelpTextDialog(mPlayBackInstance, mPlayBackInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
						}
					}
				}
			}else if (handler.equalsIgnoreCase("com.port.apps.epg.Play.Stop")){
				if(mPlayBackInstance != null){
					if (Constant.DEBUG)	Log.d(TAG, "processUIData() com.port.apps.epg.Play.Stop");
					if(ActivityName != null && ActivityName.equalsIgnoreCase("PlayList")){
						for(int i = 0;i < playlist.size();i++){
							if (Constant.DEBUG)	Log.d(TAG,"PlayList Item = "+i+" - "+ playlist.get(i).get(ScreenStyles.LIST_KEY_ID));
							if(selectedId.equalsIgnoreCase(playlist.get(i).get(ScreenStyles.LIST_KEY_ID))){
								playListPos = i;
								if (Constant.DEBUG)	Log.d(TAG,"Played Event = "+ playlist.get(i).get(ScreenStyles.LIST_KEY_ID));
							}
						}
						if (Constant.DEBUG)	Log.d(TAG,"playList Position  = "+playListPos);
						if(playlist.size()-1 > playListPos){
							HashMap<String, String> map = playlist.get(++playListPos);
							if(map.containsKey("id")){
								selectedId = map.get("id");
							}if(map.containsKey("servicetype")){
								ServiceType  = map.get("servicetype");
							}if(map.containsKey("subtype")){
								selectedSubType  = map.get("subtype");
							}if(map.containsKey("serviceid")){
								serviceId = map.get("serviceid");
							}if(map.containsKey("urllink")){
								selectedUrl = map.get("urllink");
							}if(map.containsKey("starttime")){
								starttime = map.get("starttime");
							}
						}else{
							++playListPos;
						}
						if (Constant.DEBUG)	Log.d(TAG,"Size PlayList: "+playlist.size()+", playListPos Current Position = "+playListPos);
						if(jsonData.has("state")){
							String state = Utils.getDataFromJSON(jsonData, "state");
							if(state.equalsIgnoreCase("stop")){
								if(playlist.size() > playListPos){
									if (Constant.DEBUG)	Log.d(TAG,"PlayList is Playing");
									DataStorage.setRunningStatus(false);
									DataStorage.setPlayingUrl("");
									playMethod(serviceId,selectedId, ServiceType, selectedSubType, selectedUrl, starttime);
									
								}else{
									if (Constant.DEBUG)	Log.d(TAG,"PlayList is Stop");
									HelpTip.close(mPlayBackInstance);
									DataStorage.setRunningStatus(false);
									DataStorage.setPlayingUrl("");
									finish();
								}
							}
						}
					}else{
						if (Constant.DEBUG)	Log.d(TAG, "processUIData() "+ActivityName);
						if(jsonData.has("state")){
							String state = Utils.getDataFromJSON(jsonData, "state");
							if(state.equalsIgnoreCase("stop")){
								DataStorage.setRunningStatus(false);
								DataStorage.setPlayingUrl("");
								HelpTip.close(mPlayBackInstance);
								if(ActivityName.equalsIgnoreCase("Navigator")){
									Intent intent = new Intent(PlayBack.this, AppGuide.class);
									intent.putExtra("ActivityName", "PlayBack");
									startActivity(intent);
								}
								finish();
							}
						}
					}
				}
			}else if (handler.equalsIgnoreCase("com.port.apps.epg.Guide.checkSubscribedData")){
				if(mPlayBackInstance != null){
					if(jsonData.has("subscribe")){
						String subscribe = Utils.getDataFromJSON(jsonData, "subscribe");
						String id = Utils.getDataFromJSON(jsonData, "id");
						if(subscribe.equalsIgnoreCase("true")){
							dispatchHashMap  = new ArrayList<HashMap<String,String>>();
							HashMap<String, String> list = new HashMap<String, String>();
							list.put("type", ServiceType);
							list.put("subtype", selectedSubType);
							list.put("id", id);
							list.put("url", selectedUrl);
							list.put("pricingmodel", pricingModel);
							list.put("consumer", "TV");
							list.put("network",mDataAccess.getConnectionType());
							list.put("caller", "com.player.apps.Guide");
							if(Constant.DEBUG)  Log.d(TAG , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
							list.put("called", "startActivity");
							dispatchHashMap.add(list);
							String method = "com.port.apps.epg.Play.PlayOn"; 
							new AsyncDispatch(method, dispatchHashMap,true).execute();
						}else{
							
						}
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Devices.getOutputDeviceList")){	// Bold
				if(mPlayBackInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									PlayOn.showConnectedDevices(jsonData,selectedId, mPlayBackInstance, customLayout, "BT","com.player.apps.PlayBack");											
								}else{
									HelpText.showHelpTextDialog(mPlayBackInstance, jsonData.getString("msg"), 2000);
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
				try {
					if(mPlayBackInstance != null){
						mPlayBackInstance.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Info.rInfoAction(jsonData, mContainer, mPlayBackInstance,"com.player.apps.PlayBack");
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
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	private void cleanList(){
		if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.EventList :" + CacheDockData.EventList.size());
		ArrayList<ProgramInfo> defaultList = new ArrayList<ProgramInfo>();
		for (int i = 0; i < CacheDockData.EventList.size(); i++) {
			if (!CacheDockData.EventList.get(i).getServiceId().equalsIgnoreCase(serviceId)) {
				defaultList.add(CacheDockData.EventList.get(i));
			}
		}
		CacheDockData.EventList.clear();
		CacheDockData.EventList = defaultList;
		if (Constant.DEBUG)	Log.d(TAG, "cleanList()  CacheDockData.EventList :" + CacheDockData.EventList.size());
	}	

}
