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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.player.R;
import com.player.Layout;
import com.player.action.HelpTip;
import com.player.action.Lock;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SetBackgroundFromUrl;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
import com.player.widget.ListViewAdapter;
import com.player.widget.OverLay;

/**
 * @author abhijeet	
 *
 */
public class Profile extends Layout{

	private static Profile mprofileInstance;	
	private String ActivityName = "";
	private String TAG = "Profile";
	private String sendName;
	private String profileName = "";
	private String userId = "";
	private String subscriberId = "";
	private String fbStatus = "";
	
	private int maingrid_select_index = -1;
	private int maingrid_focus_index = -1;
	private View maingridview_focus = null;
	RelativeLayout currentlayoutMain;
	TextView currentTextView;
	
	private SharedPreferences setupDetails;
	private SharedPreferences.Editor edit;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mprofileInstance = this;
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setupDetails = getApplication().getSharedPreferences("SetupDetail", MODE_WORLD_WRITEABLE);
		edit = setupDetails.edit();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,"Pre Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
		{
			Bundle br = this.getIntent().getExtras();
			
			if (br != null) {
				if (br.containsKey("ActivityName")) {
					ActivityName = br.getString("ActivityName");
				}
			}
			
			IntentFilter profile = new IntentFilter("com.player.apps.Profile");
			registerReceiver(mProfileReceiver,profile); 

			if(ActivityName != null && ActivityName.equalsIgnoreCase("Home")){
				userId = DataStorage.getCurrentUserId();
				userId = mDataAccess.getCurrentUserId();
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("tag", "switchProfile");
				list.put("caller", "com.player.apps.Profile");
				list.put("called", "startService");
				if(userId != null){
					list.put("id", userId);
				}else{
					list.put("id", "");
				}
				dispatchHashMap.add(list);
				String method = "com.port.apps.settings.Profile.switchProfile"; 
				new AsyncDispatch(method, dispatchHashMap,true).execute();
				
			}else if(ActivityName != null && ActivityName.equalsIgnoreCase("Settings") || ActivityName.equalsIgnoreCase("Setup")){
				showProfileScreen();
			}
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
		if(mProfileReceiver != null){
			mprofileInstance.unregisterReceiver(mProfileReceiver);
		}
	}
	
	/***************************************************************************************************/
	
	private void showProfileScreen(){
		if(Constant.DEBUG)  Log.d(TAG ,"showProfileScreen()");
		if(mprofileInstance != null){
			mprofileInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						if(mContainer != null){
							mContainer.removeAllViews();
						}
						DataStorage.setCurrentScreen(ScreenStyles.ADD_PROFILESCREEN);
						
						final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
						
						if(ActivityName.equalsIgnoreCase("Settings")){
							if(Constant.DEBUG)  Log.d(TAG ,"show Profile()");
							//HelpTip
							HelpTip.requestForHelp(mprofileInstance.getResources().getString(R.string.PERSONALIZING_YOUR_EXPERIENCE),
									mprofileInstance.getResources().getString(R.string.PROFILE_MSG1),mprofileInstance);
							
							String[] loginoption  = {"Create profile","View profiles","Switch profile"};
							for(int i=0;i<loginoption.length;i++){
								HashMap<String,String> map = new HashMap<String, String>();
								map.put(ScreenStyles.LIST_KEY_TITLE,loginoption[i]);
								list.add(map);
							}
						}else{
							String[] loginoption  = {"Create profile"};
							for(int i=0;i<loginoption.length;i++){
								HashMap<String,String> map = new HashMap<String, String>();
								map.put(ScreenStyles.LIST_KEY_TITLE,loginoption[i]);
								list.add(map);
							}
						}
						
						ListView listV = new ListView(mprofileInstance);
						listV.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(mprofileInstance, list,0,"login", null,-1);
						listV.setAdapter(adapter);
						mContainer.addView(listV);
						
						listV.setOnItemClickListener(new OnItemClickListener() {
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
								HashMap<String, String> map= list.get(position);
								if(map.containsKey(ScreenStyles.LIST_KEY_TITLE)){
									String val = map.get(ScreenStyles.LIST_KEY_TITLE);
									if(val.equalsIgnoreCase("Create profile")){
										showProfileDialog(true);
									}
									if(val.equalsIgnoreCase("View profiles")){
										dispatchHashMap  = new ArrayList<HashMap<String,String>>();
										HashMap<String, String> list = new HashMap<String, String>();
										list.put("consumer", "TV");
										list.put("network",mDataAccess.getConnectionType());
										list.put("caller", "com.player.apps.Profile");
										list.put("called", "startService");
										dispatchHashMap.add(list);
										String method = "com.port.apps.settings.Profile.getAllProfiles"; 
										new AsyncDispatch(method, dispatchHashMap,true).execute();
									}
									if(val.equalsIgnoreCase("Switch profile")){
										userId = DataStorage.getCurrentUserId();
										dispatchHashMap  = new ArrayList<HashMap<String,String>>();
										HashMap<String, String> list = new HashMap<String, String>();
										list.put("consumer", "TV");
										list.put("network",mDataAccess.getConnectionType());
										list.put("tag", "switchProfile");
										list.put("caller", "com.player.apps.Profile");
										list.put("called", "startService");
										if(userId != null){
											list.put("id", userId);
										}else{
											list.put("id", "");
										}
										dispatchHashMap.add(list);
										String method = "com.port.apps.settings.Profile.switchProfile"; 
										new AsyncDispatch(method, dispatchHashMap,true).execute();
									}
								}
							}
						});
						
						if(ActivityName.equalsIgnoreCase("Setup")){
							LinearLayout.LayoutParams mParams = customLayout.getLinearLayoutParams(ScreenStyles.LIST_TAB_SCREEN_WIDTH, ScreenStyles.LIST_TAB_SCREEN_HEIGHT);
							LayoutInflater inflater = mprofileInstance.getLayoutInflater();
							View v =inflater .inflate(R.layout.tablayout, null);
							LinearLayout tabContainer = (LinearLayout) v.findViewById(R.id.tabelementsContainer);
							
							Button addBtn = new Button(mprofileInstance);
							Typeface font = Typeface.createFromAsset(mprofileInstance.getAssets(), "OpenSans-Regular.ttf");  
							addBtn.setTypeface(font);  
							addBtn.setText("SKIP");
							addBtn.setTextSize(16);
							addBtn.setTextColor(R.color.pink);
							addBtn.setBackgroundColor(Color.WHITE);
							LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(customLayout.getConvertedWidth(220), customLayout.getConvertedHeight(44));
							layoutParams.leftMargin = customLayout.getConvertedWidth(50);
							layoutParams.gravity = Gravity.CENTER|Gravity.CENTER_HORIZONTAL;
							layoutParams.topMargin = customLayout.getConvertedHeight(2);
							layoutParams.bottomMargin = customLayout.getConvertedHeight(1);
							tabContainer.addView(addBtn, layoutParams);
							addBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									if(ActivityName != null && ActivityName.equalsIgnoreCase("Setup")){
										if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
//										Intent setup = new Intent(Profile.this, Home.class);
										Intent setup = new Intent(Profile.this, AppGuide.class);
										setup.putExtra("ActivityName", "Profile");
										startActivity(setup);
										finish();
									}
								}
							});
							listV.setLayoutParams(mParams);
							v.setVisibility(View.VISIBLE);
							mContainer.addView(v);	//add Button in Container
						}
						
					}						
				}
			});
		}
	}
	

	private void showProfileDialog(final boolean create){
		if(mprofileInstance != null){
			mprofileInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
				OverLay overlay = new OverLay(mprofileInstance);
				String title = "";
				if(!create){
					title = mprofileInstance.getResources().getString(R.string.EDIT);
				}else{
					title = mprofileInstance.getResources().getString(R.string.LOGIN);
				}
				
				final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL, title, null,null,null,isLukupSelected);
				if(dialog != null){
					Button connectBtn =(Button)dialog.findViewById(R.id.connect);
					Button closeBtn =(Button)dialog.findViewById(R.id.overlayCancelButton);
					LinearLayout layer = (LinearLayout) dialog.findViewById(R.id.dialogLayout);
					final EditText textfield = (EditText) dialog.findViewById(R.id.username);
					textfield.setHint(mprofileInstance.getResources().getString(R.string.ENTER_PROFILE_NAME));
					customLayout = new CustomLayout(mprofileInstance);
					final EditText passwordfield = (EditText) dialog.findViewById(R.id.password);
					if(passwordfield != null){
						passwordfield.setVisibility(View.GONE);
					}
					if(!create){
						textfield.setText(profileName);
						title = mprofileInstance.getResources().getString(R.string.EDIT);
						connectBtn.setText(mprofileInstance.getResources().getString(R.string.SAVE));
					}else{
						title = mprofileInstance.getResources().getString(R.string.LOGIN);
						connectBtn.setText(mprofileInstance.getResources().getString(R.string.REGISTER));
					}
					
					if(connectBtn != null){
						
						connectBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								if(textfield != null){
									sendName = textfield.getText().toString().trim();
									if(sendName == null || sendName.equalsIgnoreCase("")){
										HelpText.showHelpTextDialog(mprofileInstance, mprofileInstance.getResources().getString(R.string.EMPTY_INPUT), 2000);
										return;
									}
									JSONArray imagelist = new JSONArray(getImageIdsList());
									if(imagelist != null && imagelist.length() >0){
										String method;
										String tag = "";
										if(create){
											method = "com.port.apps.settings.Profile.createProfile"; 
											tag = "com.port.apps.settings.Profile.createProfile";
										}else{
											method = "com.port.apps.settings.Profile.editProfile"; 
											tag = "editname";
										}
										
										Lock.getImagePassword(mprofileInstance,imagelist, tag, userId, sendName);
									}
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
	
		
	private List<String> getImageIdsList() {
		List<String> finalList = new ArrayList<String>();
		List<String> imageIds = Arrays.asList(ScreenStyles.IMAGE_IDS.split(","));
		Collections.shuffle(imageIds);
		for(int i=0; i<12; i++) {
			finalList.add(imageIds.get(i));
		}
		return finalList;
	}
	
	/**
	 * Switch one profile to another profile
	 * Add New profile
	 */
	private void showProfile(final ArrayList<HashMap<String, String>> list, final boolean change) {
		mprofileInstance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if(mContainer != null){
						mContainer.removeAllViews();
						if(Constant.DEBUG)  Log.d(TAG,"showProfile() ");
						DataStorage.setCurrentScreen(ScreenStyles.SWITCH_PROFILE_SCREEN);
						LinearLayout.LayoutParams params = customLayout.getLinearLayoutParams(ScreenStyles.PROFILE_TABLE_WIDTH, ScreenStyles.PROFILE_TABLE_HEIGHT);
						if(list != null && list.size()>0){
							TableLayout profileTab = getProfileList(list,change);
							ScrollView scroll = new ScrollView(mprofileInstance);
							scroll.addView(profileTab);
							mContainer.addView(scroll,params);
						}else{
							LinearLayout.LayoutParams txtParams = customLayout.getLinearLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							TextView noProfileView = new TextView(mprofileInstance);
							noProfileView.setTextColor(R.color.white);
							Typeface font = Typeface.createFromAsset(mprofileInstance.getAssets(), "OpenSans-Regular.ttf"); 
							noProfileView.setText(mprofileInstance.getResources().getString(R.string.NO_PROFILES));
							noProfileView.setTypeface(font);
							customLayout = new CustomLayout(mprofileInstance);
							noProfileView.setTextSize(customLayout.getConvertedHeight(16));
							txtParams.gravity = Gravity.CENTER;
							noProfileView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
							noProfileView.setLayoutParams(txtParams);
							mContainer.addView(noProfileView);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
		});
	}
	
	
	/**get ProfileList from Mediaplayer
	 * @param jsonData2
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getProfiles(JSONObject jsonData) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		try {
			if(jsonData.has("profileList")){
				JSONArray array = jsonData.getJSONArray("profileList");
				if(array != null && array.length() >0){
					HashMap<String, String> map;
					for(int i=0;i<array.length();i++){
						JSONObject obj = array.getJSONObject(i);
						String name = null;
						if(obj.has("name")){
							name = obj.getString("name");
						}
						String id = "";
						if(obj.has("id")){
							id = obj.getString("id");
						}
						String image = "";
						if(obj.has("image")){
							image = obj.getString("image");
						}
						String status = "";
						if(obj.has("status")){
							status = obj.getString("status");
						}
						map= new HashMap<String, String>();
						map.put(ScreenStyles.PROFILE_NAME, name);
						map.put(ScreenStyles.LIST_KEY_ID, id);
						map.put(ScreenStyles.LIST_KEY_HTTP_URL, image);
						map.put("status", status);
						list.add(map);
					}
				}
				return list;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			return null;
		}
	}
	
	
	private ArrayList<HashMap<String, String>> profileList;
	
	public TableLayout getProfileList(ArrayList<HashMap<String, String>> list,boolean flag){
		if(Constant.DEBUG)  Log.d(TAG ,"getProfileList()");
		profileList = list;
		if(mprofileInstance != null && profileList != null && profileList.size() > 0){
			CustomLayout customView = new CustomLayout(mprofileInstance);
			int itemCount = profileList.size();
			int mRows = 0;
			int divisor = ScreenStyles.PROFILE_COLUMN;
			int remainder = 0;
			if (itemCount < divisor) {
				mRows = 1;
				remainder = itemCount;
			} else {
				mRows = (itemCount / divisor);
				remainder = itemCount % divisor;
				if (remainder != 0) 
					mRows++;
			}
		
			TableLayout profieTable = new TableLayout(mprofileInstance);
			profieTable.removeAllViews();
		
			for (int i=0; i < mRows; i++) {
				TableRow row = null;
				TextView profilenameView = null;
				ImageView profileImage = null;
				row = new TableRow(mprofileInstance);
				int topMargin=customView.getConvertedHeight(ScreenStyles.PROFILE_ELEMENT_TOPMARGIN);
				RelativeLayout.LayoutParams relativelayoutParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				row.setBackgroundColor(Color.TRANSPARENT);
				row.setPadding(0, topMargin, 0, 0);
				row.setWeightSum(0);
				LinearLayout layout = new LinearLayout(mprofileInstance);
				for (int j=0; j< divisor; j++) {
					int k = i*divisor+j;
					if (k >= itemCount) break;
		
					Drawable profileDrawable = null;
					HashMap<String, String> map = null;
					if(k <= profileList.size()){
						map = profileList.get(k);
						profileName = map.get(ScreenStyles.PROFILE_NAME);
						userId = map.get(ScreenStyles.LIST_KEY_ID);
					}
		
					layout.setClickable(true);
					LayoutInflater  mLayoutInflater = mprofileInstance.getLayoutInflater();
					View itemLayout = mLayoutInflater.inflate(R.layout.profile_elements, null);
					profilenameView = (TextView)itemLayout.findViewById(R.id.profileName);
		
					if(profileName != null && profilenameView !=null ){
						Typeface font = Typeface.createFromAsset(mprofileInstance.getAssets(), "OpenSans-Regular.ttf");
						profilenameView.setTypeface(font);
						profilenameView.setText(profileName);
						if (DataStorage.getCurrentUserId() != null && userId.equalsIgnoreCase(DataStorage.getCurrentUserId())) {
							profilenameView.setTextColor(getResources().getColor(R.color.pink));
						}
						
					}
					profileImage = (ImageView)itemLayout.findViewById(R.id.profileImage);
					if(profileImage != null){
						profileDrawable = getProfileImage(map,mprofileInstance);
						if(map.containsKey(ScreenStyles.LIST_KEY_HTTP_URL)){
							if(map.get(ScreenStyles.LIST_KEY_HTTP_URL) != null && !map.get(ScreenStyles.LIST_KEY_HTTP_URL).equalsIgnoreCase("")){
								Runnable r = new SetBackgroundFromUrl(mprofileInstance,map.get(ScreenStyles.LIST_KEY_HTTP_URL),profileImage);
								new Thread(r).start();
							}else{
								profileImage.setBackgroundDrawable(mprofileInstance.getResources().getDrawable(R.drawable.v13_ico_profile_80_c));
							}
						}else if(profileDrawable != null){
							profileImage.setBackgroundDrawable(profileDrawable);
						}else{
							profileImage.setBackgroundDrawable(mprofileInstance.getResources().getDrawable(R.drawable.v13_ico_profile_80_c));
						}
					}
				
					LinearLayout.LayoutParams tableRowParams = new LinearLayout.LayoutParams(customView.getConvertedWidth(148),customView.getConvertedWidth(148));
					if(j == 0){
						tableRowParams.leftMargin = customView.getConvertedWidth(ScreenStyles.PROFILE_ELEMENT_LEFTMARGIN);
					}else{
						tableRowParams.leftMargin = customView.getConvertedWidth(ScreenStyles.PROFILE_ELEMENT_PADDING);
					}
					layout.addView(itemLayout,tableRowParams);
					itemLayout.setId(k);
		
					if(flag){
						//HelpTip
						HelpTip.requestForHelp(mprofileInstance.getResources().getString(R.string.EDIT_YOUR_PROFILE),
								mprofileInstance.getResources().getString(R.string.PROFILE_MSG2),mprofileInstance);
						
						itemLayout.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								if(Constant.DEBUG)  Log.d(TAG ,"getProfileList().OnLongClickListener");
								HashMap<String, String> map = profileList.get(v.getId());
								userId = map.get(ScreenStyles.LIST_KEY_ID);
								profileName = map.get(ScreenStyles.PROFILE_NAME);
								String status = map.get("status");
								if (status.equalsIgnoreCase("0")) {
									fbStatus = "disconnect";
								}else{
									fbStatus = "connect";
								}
								Log.d(TAG, "Profile" + profileName );
								if(userId != null && !profileName.equalsIgnoreCase("Guest")){//2nd Nov to remove the edit option for Guest profile
									showCustomMenu();
								}else {
									Toast.makeText(mprofileInstance,getResources().getString(R.string.GUEST_ERROR), Toast.LENGTH_SHORT).show();
								}
								return true;
							}
						});
					}else{
						itemLayout.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(Constant.DEBUG)  Log.d(TAG ,"getProfileList().OnClickListener");
								HashMap<String, String> map = profileList.get(v.getId());
								userId = map.get(ScreenStyles.LIST_KEY_ID);
								profileName = map.get(ScreenStyles.PROFILE_NAME);
								String method = "com.port.apps.settings.Profile.switchProfile"; 
//								if(!profileName.equalsIgnoreCase("Guest")){
								if(!userId.equalsIgnoreCase("1000")){
									JSONArray imagelist = new JSONArray(getImageIdsList());
									if(imagelist != null && imagelist.length() >0){										
										Lock.getImagePassword(mprofileInstance,imagelist,method,userId,profileName);
									}
								}else{
									dispatchHashMap  = new ArrayList<HashMap<String,String>>();
									HashMap<String, String> list = new HashMap<String, String>();
									list.put("consumer", "TV");
									list.put("network",mDataAccess.getConnectionType());
									list.put("id", userId);
									list.put("name", profileName);		
									list.put("imageid", "");
									list.put("tag", "switchProfile");
									list.put("caller", "com.player.apps.Profile");
									list.put("called", "startService");
									dispatchHashMap.add(list);
									String Methods = "com.port.apps.settings.Profile.authenticate";
									new AsyncDispatch(Methods, dispatchHashMap,true).execute();
								}
							}
						});
					}
				}
				row.addView(layout);
				profieTable.addView(row,relativelayoutParams);
			}
			return profieTable;
}
		return null;
	}
	
	private void deleteDialog(){
		if(mprofileInstance != null){
			mprofileInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					OverLay overlay = new OverLay(mprofileInstance);
					String message = mprofileInstance.getResources().getString(R.string.DELETE_PROFILE_MESSAGE);
					final String title = mprofileInstance.getResources().getString(R.string.DELETE_PROFILE);
					final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, title, message, null,DataStorage.getCurrentUserId(),false);
					if(dialog != null){
						Button deletBtn = (Button) dialog.findViewById(R.id.okButton);
						Button closeBtn = (Button) dialog.findViewById(R.id.overlayCancelButton);
						if(deletBtn != null){
							deletBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
//									if(title.equalsIgnoreCase("Profile Delete")){
										try{
											String method= "com.port.apps.settings.Profile.deleteProfile";
											JSONArray imagelist = new JSONArray(getImageIdsList());
											if(imagelist != null && imagelist.length() >0){
												Lock.getImagePassword(mprofileInstance,imagelist,method,userId,null);
											}					
											
											dialog.dismiss();
										}catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										}
//									}
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
						
						dialog.show();
					}
				}
			});
		}
	}
	
	private void changePasswordDialog(){
		if(mprofileInstance != null){
			mprofileInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					OverLay overlay = new OverLay(mprofileInstance);
					final String title ="Reset Password";
					String message = mprofileInstance.getResources().getString(R.string.RESET_PASSWORD);
					final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, title, message, null,null,DataStorage.isShowSelected());
					ImageView alertLogo = (ImageView) dialog.findViewById(R.id.alertlogo);
					alertLogo.setBackgroundResource(R.drawable.v13_ico_alert_01);
					if(dialog != null){
						Button okBtn = (Button) dialog.findViewById(R.id.okButton);
						if(okBtn != null){
							okBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									try{
										JSONArray imagelist = new JSONArray(getImageIdsList());
										if(imagelist != null && imagelist.length() >0){
											String tag = "editpwd";
											Lock.getImagePassword(mprofileInstance,imagelist,tag, userId,sendName);
										}
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
						Button cancelBtn = (Button) dialog.findViewById(R.id.overlayCancelButton);
						if(cancelBtn != null){
							cancelBtn.setVisibility(View.VISIBLE);
							cancelBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									if(dialog != null && dialog.isShowing()){
										try{
											dialog.dismiss();
										}catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										}
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
	
	private Drawable getProfileImage(HashMap<String, String> map,Activity activity) {
		if(activity != null && map.get(ScreenStyles.PROFILE_IMAGE)!= null){
			Drawable d = activity.getResources().getDrawable(Integer.parseInt(map.get(ScreenStyles.PROFILE_IMAGE)));
			return d;
		}
		return null;
	}

	
	public void showMenuItems() { 
		if(Constant.DEBUG)  Log.d(TAG , "showMenuItems() ");
		getContextMenu();
	}
	
	private void getContextMenu() {
		try {
			if(mMenu != null){
				mMenu.setMenuItems(null);
				mMenu.setItemsPerLineInPortraitOrientation(3);
			}
			customMenuValue = null;
			customMenuIcons = null;
			customMenuValue = ScreenStyles.PROFILE_MENU_ITEMS;
			customMenuIcons = ScreenStyles.PROFILE_MENU_ICONS;
			setMenuBasedOnStatus();
			
			ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
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
						if(Constant.DEBUG)  Log.d(TAG ,"Exception "+ e);
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
	
	/************ Based on fb Status option change the Toggle MenuItem *****************/
	private void setMenuBasedOnStatus() {
		try {
			if(fbStatus != null){
				if(customMenuValue != null && fbStatus.equalsIgnoreCase("connect")){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("Connect")){
							customMenuValue[i] = "Disconnect";
							customMenuIcons[i] = R.drawable.v13_ico_disconnect_01;
							break;
						}
					}
				}else if(customMenuValue != null){
					for(int i=0;i<customMenuValue.length;i++){
						if(customMenuValue[i].equalsIgnoreCase("disconnect")){
							customMenuValue[i] = "Connect";
							customMenuIcons[i] = R.drawable.v13_ico_connect_01;
							break;
						}
					}
				}
				if(Constant.DEBUG)  Log.d(TAG ,"setMenuBasedOnStatus():"+fbStatus);
			}
		}catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void processMenuActions(CustomMenuItem key) {
		if(Constant.DEBUG)  Log.d(TAG , "processMenuActions() ");
		
		if(key.getCaption().trim().equalsIgnoreCase("Edit")){
			showProfileDialog(false);
			return;
		}
		if(key.getCaption().trim().equalsIgnoreCase("Delete")){
			deleteDialog();
			return;
		}
		if(key.getCaption().trim().equalsIgnoreCase("Connect")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("status","connect");
			list.put("id",userId);
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Profile");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.settings.Profile.fbConnectDisconnect", dispatchHashMap,true).execute();
			return;
		}
		
		if(key.getCaption().trim().equalsIgnoreCase("Disconnect")){
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("status","disconnect");
			list.put("id",userId);
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Profile");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.apps.settings.Profile.fbConnectDisconnect", dispatchHashMap,true).execute();

			return;
		}
	}
	
/************************************************************************************************/
	
	public final BroadcastReceiver mProfileReceiver = new BroadcastReceiver() {
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
		if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.  "+handler);
		
		if(handler.equalsIgnoreCase("com.port.apps.settings.Profile.authenticate")){
			//after logging in or registering
			if(mprofileInstance != null){
				try {
					String result = Utils.getDataFromJSON(jsonData, "result");
					String tag  = Utils.getDataFromJSON(jsonData, "tag");
//							if(result.equalsIgnoreCase("success")){
						try {
							if(tag.equalsIgnoreCase("switchProfile")){
								String id = Utils.getDataFromJSON(jsonData, "id");
								DataStorage.setCurrentUserId(id);
								mDataAccess.updateSetupDB("CurrentUserId", id);
								JSONArray imagelist;
								if(jsonData.has("result")){
									result = jsonData.getString("result");
									if(result.equalsIgnoreCase("success")){
										if(jsonData.has("msg")){
											HelpText.showHelpTextDialog(mprofileInstance, jsonData.getString("msg"), 2000);
											
										}
										
									}else{
										if(jsonData.has("msg")){
											HelpText.showHelpTextDialog(mprofileInstance, jsonData.getString("msg"), 2000);
										}
									}
								}
							}else if(tag.equalsIgnoreCase("deleteProfile")){
								if(jsonData.has("result")){
									result = jsonData.getString("result");
									if(result.equalsIgnoreCase("success")){
										if(mContainer != null){
											mContainer.removeAllViews();
										}
										ArrayList<HashMap<String, String>> list = getProfiles(jsonData);
										HelpText.showHelpTextDialog(mprofileInstance, jsonData.getString("msg"), 2000);
										showProfile(list,true);												
									}else{
										HelpText.showHelpTextDialog(mprofileInstance, jsonData.getString("msg"), 2000);
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
						}
//							}
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
		}else if(handler.equalsIgnoreCase("com.port.apps.settings.Profile.createProfile")){
			//after logging in or registering
			if(mprofileInstance != null){
				try {
					String result = Utils.getDataFromJSON(jsonData, "result");
					String tag  = Utils.getDataFromJSON(jsonData, "tag");
					if(result.equalsIgnoreCase("success")){
						HelpText.showHelpTextDialog(mprofileInstance, mprofileInstance.getResources().getString(R.string.PROFILE_CREATION_SUCCESS), 2000);
					}else{
						HelpText.showHelpTextDialog(mprofileInstance, mprofileInstance.getResources().getString(R.string.PROFILE_CREATION_FAILED), 2000);
					}
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
		}else if(handler.equalsIgnoreCase("com.port.apps.settings.Profile.getAllProfiles")){
			try {
				ArrayList<HashMap<String, String>> list = getProfiles(jsonData);
				showProfile(list, true);
			}catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
			
		}
		else if(handler.equalsIgnoreCase("com.port.apps.settings.Profile.switchProfile")){
			if(mprofileInstance != null){
				if(mContainer != null){
					mContainer.removeAllViews();
				}
				ArrayList<HashMap<String, String>> profilelist;
				profilelist = getProfiles(jsonData);
				showProfile(profilelist, false);
			}
		}
		else if(handler.equalsIgnoreCase("com.port.apps.settings.Profile.editProfile")){
			if(mprofileInstance != null){
				String result = Utils.getDataFromJSON(jsonData, "result");
				String tag  = Utils.getDataFromJSON(jsonData, "tag");
				if(tag.equalsIgnoreCase("editname")){
					if(result.equalsIgnoreCase("success")){
						changePasswordDialog();
						if(mContainer != null){
							mContainer.removeAllViews();
						}
						ArrayList<HashMap<String, String>> list = getProfiles(jsonData);
						showProfile(list,true);
					}else{
						HelpText.showHelpTextDialog(mprofileInstance, mprofileInstance.getResources().getString(R.string.NAME_EDIT_FAILED), 2000);
					}
				}else{
					if(result.equalsIgnoreCase("success")){
						HelpText.showHelpTextDialog(mprofileInstance, mprofileInstance.getResources().getString(R.string.PASSWORD_SET), 2000);
					}else{
						HelpText.showHelpTextDialog(mprofileInstance, mprofileInstance.getResources().getString(R.string.PASSWORD_SET_FAILURE), 2000);
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
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			return backButtonAction(keyCode, event);
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				HelpTip.close(mprofileInstance);
				Intent lukup = new Intent(Profile.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Profile");
				startActivity(lukup);
				finish();
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private boolean backButtonAction(int keyCode, KeyEvent event) {
		if(Constant.DEBUG)  Log.d(TAG ,"backButtonAction().CurrentScreen: "+DataStorage.getCurrentScreen());
		if(mMenu.isShowing()){
			hideMenu();
			return true;
		}
		HelpTip.close(mprofileInstance);
		if (DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.SWITCH_PROFILE_SCREEN)) {
			if(ActivityName.equalsIgnoreCase("Setup") || ActivityName.equalsIgnoreCase("Settings")){
				showProfileScreen();
			}else if(ActivityName.equalsIgnoreCase("Home")){
				Intent setup = new Intent(Profile.this, Home.class);
				setup.putExtra("ActivityName", "Profile");
				startActivity(setup);
				finish();
			}else{
				finish();
			}
			return true;
		}else if (DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.ADD_PROFILESCREEN)) {
			if(ActivityName.equalsIgnoreCase("Setup")){
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent setup = new Intent(Profile.this, Home.class);
				setup.putExtra("ActivityName", "Profile");
				startActivity(setup);
				finish();
			}else {
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent setup = new Intent(Profile.this, Setup.class);
				setup.putExtra("ActivityName", "Profile");
				startActivity(setup);
				finish();
			}
			return true;
		} 
		return false;
	}
}
