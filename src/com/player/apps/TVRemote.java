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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.Layout.AsyncDispatch;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.action.OperatorButtonClickListener;
import com.player.network.ir.IRTransmitter;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.OperatorKeys;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.CustomMenuItem;
import com.player.widget.HelpText;
import com.player.widget.ListViewAdapter;


public class TVRemote extends Layout{
	private TVRemote mTvRemoteInstance;
	private String TAG = "TVRemote";
	private String operatorName;
	private String ActivityName;
	OperatorKeys key;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Constant.DEBUG) System.out.println("Oncreate TVRemote called");
		mTvRemoteInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 

		IntentFilter tv = new IntentFilter("com.player.apps.TVRemote");
		registerReceiver(mTVRemoteReceiver,tv); 
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG  ,"Pre Activity.CurrentScreen: "+DataStorage.getCurrentScreen());

		Bundle br = this.getIntent().getExtras();
		if (br != null) {
			if (br.containsKey("ActivityName")) {
				ActivityName = br.getString("ActivityName");
			}
			if (br.containsKey("operatorName")) {
				operatorName = br.getString("operatorName");
			}
		}
		try{
			new OperatorKeys(this.getApplicationContext());
			operatorName = DataStorage.getConnectedVendor();
			if (Constant.DEBUG)	Log.d(TAG, "init()  operatorName: "+operatorName);
			
			if(DataStorage.getConnectedVendor() != null){
//				irHandlerEvent = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, DataStorage.getConnectedVendor());
//				DataStorage.setIrhandler(irHandlerEvent);
			}
			
			if(operatorName!=null && !operatorName.equalsIgnoreCase("")){
				showOperatorRemote(operatorName);
			}else{
				if (Constant.DEBUG)	Log.d(TAG, "init()  No live Operator");
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
	private void showOperatorRemote(String selectedItemValue){
		//HelpTip
		HelpTip.requestForHelp(mTvRemoteInstance.getResources().getString(R.string.CONTROL_YOUR_SET_TOPBOX),
				mTvRemoteInstance.getResources().getString(R.string.TVREMOTE_MSG1),mTvRemoteInstance);
		
		if(selectedItemValue.equalsIgnoreCase("TATASKY")){
			inittataskyUi();
		}
		if(selectedItemValue.equalsIgnoreCase("HATHWAY")){
			inintHathwayremote();
		}
		if(selectedItemValue.equalsIgnoreCase("SUNDIRECT")){
			initSundirectUi();
		}
		if(selectedItemValue.equalsIgnoreCase("DISHTV")){
			initDishTvUi();
		}
		if(selectedItemValue.equalsIgnoreCase("ACT")){
			initActCableUi();
		}
		if(selectedItemValue.equalsIgnoreCase("INCABLE")){
			initInCableUi();
		}
		if(selectedItemValue.equalsIgnoreCase("DEN")){
			initDenCableUi();
		}
		if(selectedItemValue.equalsIgnoreCase("VIDEOCON")){
			initVideoconUi();
		}
	}
	
	
	/**
	 * initialize tatasky UI based on Box type
	 * @param boxType
	 */
	private void inittataskyUi(){
		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){

						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);

						/**
						 * Tatasky IR structures
						 */
						//	private static int irhandler = 0;
						ImageButton tatasky_redBtn = null;
						ImageButton tatasky_greenBtn = null;
						ImageButton tatasky_yellowBtn = null;
						ImageButton tatasky_blueBtn = null;

						ImageButton tatasky_offBtn = null;
						Button tatasky_backBtn = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button tatasky_upArrowBtn = null;
						Button tatasky_downArrowBtn = null;
						Button tatasky_leftArrowBtn = null;
						Button tatasky_rightArrowBtn = null;
						Button tatasky_center_select_Btn = null;

						Button tatasky_hekpBtn = null;

						Button tatasky_oneBtn = null;
						Button tatasky_twoBtn = null;
						Button tatasky_threeBtn = null;

						Button tatasky_fourBtn = null;
						Button tatasky_fiveBtn = null;
						Button tatasky_sixBtn = null;

						Button tatasky_sevenBtn = null;
						Button tatasky_eightBtn = null;
						Button tatasky_nineBtn = null;

						Button tatasky_zeroBtn = null;
						View tataskyView = inflater.inflate(R.layout.tatasky_remote, null);

						tatasky_redBtn = (ImageButton) tataskyView.findViewById(R.id.tatasky_redBtn);
						tatasky_greenBtn = (ImageButton) tataskyView.findViewById(R.id.tatasky_greenBtn);
						tatasky_yellowBtn = (ImageButton) tataskyView.findViewById(R.id.tatasky_yellowBtn);
						tatasky_blueBtn = (ImageButton) tataskyView.findViewById(R.id.tatasky_blueBtn);

						tatasky_backBtn = (Button) tataskyView.findViewById(R.id.tatasky_backBtn);
						tatasky_offBtn = (ImageButton) tataskyView.findViewById(R.id.tatasky_offBtn);

						tatasky_upArrowBtn = (Button) tataskyView.findViewById(R.id.tatasky_upArrowBtn);
						tatasky_downArrowBtn = (Button) tataskyView.findViewById(R.id.tatasky_downArrowBtn);
						tatasky_rightArrowBtn= (Button) tataskyView.findViewById(R.id.tatasky_rightArrowBtn);
						tatasky_leftArrowBtn = (Button) tataskyView.findViewById(R.id.tatasky_leftArrowBtn);
						tatasky_center_select_Btn = (Button) tataskyView.findViewById(R.id.tatasky_center_select_Btn);

						tatasky_hekpBtn = (Button) tataskyView.findViewById(R.id.tatasky_hekpBtn);

						tatasky_oneBtn = (Button) tataskyView.findViewById(R.id.tatasky_oneBtn);
						tatasky_twoBtn= (Button) tataskyView.findViewById(R.id.tatasky_twoBtn);
						tatasky_threeBtn = (Button) tataskyView.findViewById(R.id.tatasky_threeBtn);
						tatasky_fourBtn= (Button) tataskyView.findViewById(R.id.tatasky_fourBtn);
						tatasky_fiveBtn = (Button) tataskyView.findViewById(R.id.tatasky_fiveBtn);
						tatasky_sixBtn = (Button) tataskyView.findViewById(R.id.tatasky_sixBtn);
						tatasky_sevenBtn = (Button) tataskyView.findViewById(R.id.tatasky_sevenBtn);
						tatasky_eightBtn = (Button) tataskyView.findViewById(R.id.tatasky_eightBtn);
						tatasky_nineBtn= (Button) tataskyView.findViewById(R.id.tatasky_nineBtn);
						tatasky_zeroBtn = (Button) tataskyView.findViewById(R.id.tatasky_zeroBtn);

						dropdownButton = (Button) tataskyView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) tataskyView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForTataSky("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForTataSky("SD");
								}
							});
						}

						if(tatasky_backBtn != null){
							tatasky_backBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","back"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(tatasky_offBtn != null){
							tatasky_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","power"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(tatasky_redBtn != null){
							tatasky_redBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","red"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_greenBtn != null){
							tatasky_greenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","green"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_yellowBtn != null){
							tatasky_yellowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","yellow"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_blueBtn != null){
							tatasky_blueBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","blue"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}


						if(tatasky_upArrowBtn != null){
							tatasky_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","up"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_downArrowBtn != null){
							tatasky_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","down"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_leftArrowBtn != null){
							tatasky_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","left"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_rightArrowBtn != null){
							tatasky_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","right"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_center_select_Btn != null){
							if (Constant.DEBUG)	Log.d(TAG, "Tata Sky Press Ok");
							tatasky_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","select"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(tatasky_hekpBtn!= null){
							tatasky_hekpBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","help"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(tatasky_oneBtn != null){
							if(Constant.DEBUG)  Log.d("TV Remote" , "Operator "+ DataStorage.getConnectedVendor() + " key " + OperatorKeys.getKey("tatasky","1"));
							tatasky_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","1"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_twoBtn != null){
							tatasky_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","2"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_threeBtn != null){
							tatasky_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","3"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(tatasky_fourBtn != null){
							tatasky_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","4"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_fiveBtn != null){
							tatasky_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","5"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_sixBtn != null){
							tatasky_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","6"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(tatasky_sevenBtn != null){
							tatasky_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","7"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_eightBtn != null){
							tatasky_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","8"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_nineBtn != null){
							tatasky_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","9"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tatasky_zeroBtn != null){
							tatasky_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("tatasky","0"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(tataskyView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(tataskyView);
						}
					}
				}
			});
		}
	}

	/**
	 * show extra button in overlay
	 * @param string type of box
	 */
	private void showExtraButtonsForTataSky(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForHathway ");
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.TATASKY_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.TATASKY_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}
			if(mTvRemoteInstance != null){
				mTvRemoteInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
					final Dialog listoverlay = new Dialog(mTvRemoteInstance);
					TextView titleView;
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
					listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
					listoverlay.setContentView(listOverlay);
					titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
					if(titleView != null){
						titleView.setText("Sevice Buttons");
					}
					if(Constant.DEBUG) System.out.println("List Size "+filterList.size());
		
					if(listoverlay != null){
		//				listoverlay.setOnKeyListener(TVRemote.this);
						listoverlay.show();
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
						RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
						ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
						if(filterList != null && filterList.size() > 0 ){
							if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
							if(ovelaylistlayer != null){
								if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
								listView.setCacheColorHint(Color.TRANSPARENT);
								ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
								listView.setAdapter(adapter);
								if(listView != null){
									ovelaylistlayer.addView(listView);
									listView.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
											HashMap<String, String> getinglist = filterList.get(arg2);
											String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);
											String[] TATASKY_EXTRAKEYS_LIST = {"Home","Showcase","Organise","Active","Favourite","Tv"};
											String id = getinglist.get("id");
											if(name != null && !name.equalsIgnoreCase("")){
												if(name.equalsIgnoreCase(TATASKY_EXTRAKEYS_LIST[0])){
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[0]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[0]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "home"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(TATASKY_EXTRAKEYS_LIST[1])){
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[1]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[1]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "showcase"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(TATASKY_EXTRAKEYS_LIST[2])){
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[2]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[2]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "organise"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(TATASKY_EXTRAKEYS_LIST[3])){
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[3]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[3]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "active"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(TATASKY_EXTRAKEYS_LIST[4])){
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "favourite"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(TATASKY_EXTRAKEYS_LIST[5])){
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "tv"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, TATASKY_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
		
											}
		
											try{
												if(listoverlay != null && listoverlay.isShowing())
													listoverlay.dismiss();
											}catch (Exception e) {
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
											}
										}
									});
								}
							}
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
		
							if(closeBtn != null){
								if(listoverlay != null && listoverlay.isShowing())
									listoverlay.dismiss();
							}
						}
					}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
	
	private void initActCableUi(){
		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){
//						IRTransmitter.stopIr();

						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);

						/**
						 * ActCable IR structures
						 */
						//	private static int irhandler = 0;
						ImageButton act_redBtn = null;
						ImageButton act_greenBtn = null;
						ImageButton act_yellowBtn = null;
						ImageButton act_blueBtn = null;
						ImageButton act_offBtn = null;

						ImageButton act_rewindBtn = null;
						ImageButton act_playBtn = null;
						ImageButton act_forwardBtn = null;
						ImageButton act_stopBtn = null;
						ImageButton act_pauseBtn = null;

						Button act_navBtn = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button act_upArrowBtn = null;
						Button act_downArrowBtn = null;
						Button act_leftArrowBtn = null;
						Button act_rightArrowBtn = null;
						Button act_center_select_Btn = null;

						Button act_exitBtn = null;

						Button act_oneBtn = null;
						Button act_twoBtn = null;
						Button act_threeBtn = null;

						Button act_fourBtn = null;
						Button act_fiveBtn = null;
						Button act_sixBtn = null;

						Button act_sevenBtn = null;
						Button act_eightBtn = null;
						Button act_nineBtn = null;

						Button act_zeroBtn = null;
						View actView = inflater.inflate(R.layout.act_remote, null);

						act_redBtn = (ImageButton) actView.findViewById(R.id.act_redBtn);
						act_greenBtn = (ImageButton) actView.findViewById(R.id.act_greenBtn);
						act_yellowBtn = (ImageButton) actView.findViewById(R.id.act_yellowBtn);
						act_blueBtn = (ImageButton) actView.findViewById(R.id.act_blueBtn);

						act_rewindBtn = (ImageButton) actView.findViewById(R.id.act_rewindBtn);
						act_playBtn = (ImageButton) actView.findViewById(R.id.act_playBtn);
						act_forwardBtn = (ImageButton) actView.findViewById(R.id.act_forwardBtn);
						act_stopBtn = (ImageButton) actView.findViewById(R.id.act_stopBtn);
						act_pauseBtn = (ImageButton) actView.findViewById(R.id.act_pauseBtn);

						act_navBtn = (Button) actView.findViewById(R.id.act_navBtn);
						act_offBtn = (ImageButton) actView.findViewById(R.id.act_offBtn);

						act_upArrowBtn = (Button) actView.findViewById(R.id.act_upArrowBtn);
						act_downArrowBtn = (Button) actView.findViewById(R.id.act_downArrowBtn);
						act_rightArrowBtn= (Button) actView.findViewById(R.id.act_rightArrowBtn);
						act_leftArrowBtn = (Button) actView.findViewById(R.id.act_leftArrowBtn);
						act_center_select_Btn = (Button) actView.findViewById(R.id.act_center_select_Btn);

						act_exitBtn = (Button) actView.findViewById(R.id.act_exitBtn);

						act_oneBtn = (Button) actView.findViewById(R.id.act_oneBtn);
						act_twoBtn= (Button) actView.findViewById(R.id.act_twoBtn);
						act_threeBtn = (Button) actView.findViewById(R.id.act_threeBtn);
						act_fourBtn= (Button) actView.findViewById(R.id.act_fourBtn);
						act_fiveBtn = (Button) actView.findViewById(R.id.act_fiveBtn);
						act_sixBtn = (Button) actView.findViewById(R.id.act_sixBtn);
						act_sevenBtn = (Button) actView.findViewById(R.id.act_sevenBtn);
						act_eightBtn = (Button) actView.findViewById(R.id.act_eightBtn);
						act_nineBtn= (Button) actView.findViewById(R.id.act_nineBtn);
						act_zeroBtn = (Button) actView.findViewById(R.id.act_zeroBtn);

						dropdownButton = (Button) actView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) actView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForActCable("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForActCable("SD");
								}
							});
						}

						if(act_navBtn != null){
							act_navBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","navigate"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_offBtn != null){
							act_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","power"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_redBtn != null){
							act_redBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","red"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_greenBtn != null){
							act_greenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","green"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_yellowBtn != null){
							act_yellowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","yellow"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_blueBtn != null){
							act_blueBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","blue"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_rewindBtn != null){
							act_rewindBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","rewind"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_playBtn != null){
							act_playBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","play"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_forwardBtn != null){
							act_forwardBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","forward"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_stopBtn != null){
							act_stopBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","stop"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_pauseBtn != null){
							act_pauseBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","pause"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_upArrowBtn != null){
							act_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","up"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_downArrowBtn != null){
							act_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","down"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_leftArrowBtn != null){
							act_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","left"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_rightArrowBtn != null){
							act_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","right"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_center_select_Btn != null){
							if (Constant.DEBUG)	Log.d(TAG, "Act Press Ok");
							act_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","select"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_exitBtn!= null){
							
							act_exitBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","exit"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_oneBtn != null){
							act_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","1"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_twoBtn != null){
							act_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","2"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_threeBtn != null){
							act_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","3"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(act_fourBtn != null){
							act_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","4"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_fiveBtn != null){
							act_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","5"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_sixBtn != null){
							act_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","6"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}


						if(act_sevenBtn != null){
							act_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","7"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_eightBtn != null){
							act_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","8"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_nineBtn != null){
							act_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","9"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(act_zeroBtn != null){
							act_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("act","0"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(actView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(actView);
						}
					}
				}
			});
		}
	}

	private void showExtraButtonsForActCable(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForActCable ");
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.ACTCABLE_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.ACTCABLE_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}
			if(mTvRemoteInstance != null){
				mTvRemoteInstance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
					final Dialog listoverlay = new Dialog(mTvRemoteInstance);
					TextView titleView;
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
					listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
					listoverlay.setContentView(listOverlay);
					titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
					if(titleView != null){
						titleView.setText("Sevice Buttons");
					}
					if(Constant.DEBUG) System.out.println("List Size "+filterList.size());
		
					if(listoverlay != null){
		//				listoverlay.setOnKeyListener(TVRemote.this);
						listoverlay.show();
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
						RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
						ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
						if(filterList != null && filterList.size() > 0 ){
							if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
							if(ovelaylistlayer != null){
								if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
								listView.setCacheColorHint(Color.TRANSPARENT);
								ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
								listView.setAdapter(adapter);
								if(listView != null){
									ovelaylistlayer.addView(listView);
									listView.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
											HashMap<String, String> getinglist = filterList.get(arg2);
											String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);
		
											String[] ACTCABLE_EXTRAKEYS_LIST = {"Favourites","Last","EPG","Menu","Options","VOD","TV/Radio","Setup"};
											String id = getinglist.get("id");
		
											if(name != null && !name.equalsIgnoreCase("")){
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[0])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[0]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[0]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "favourite"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[1])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[1]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[1]+" Before caOperatorKeys.DISHTV_INFOlling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "last"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[2])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[2]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[2]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "epg"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[3])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[3]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[3]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "menu"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
												}
		
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[4])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "opt"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[5])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[5]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[5]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "vod"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[5]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[6])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[6]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[6]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "tvradio"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[6]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(ACTCABLE_EXTRAKEYS_LIST[7])){
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[7]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[7]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "setup"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, ACTCABLE_EXTRAKEYS_LIST[7]+" Aftrer calling IR");
												}
											}
		
											try{
												if(listoverlay != null && listoverlay.isShowing())
													listoverlay.dismiss();
											}catch(Exception e){
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
											}
										}
									});
								}
							}
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
		
							if(closeBtn != null){
								if(listoverlay != null && listoverlay.isShowing())
									listoverlay.dismiss();
							}
						}
					}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
	private void initDenCableUi(){
		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){

						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);

						/**
						 * DenCable IR structures
						 */
						//	private static int irhandler = 0;
						ImageButton den_redBtn = null;
						ImageButton den_greenBtn = null;
						ImageButton den_yellowBtn = null;
						ImageButton den_blueBtn = null;
						ImageButton den_offBtn = null;

						ImageButton den_rewindBtn = null;
						ImageButton den_playBtn = null;
						ImageButton den_forwardBtn = null;
						ImageButton den_stopBtn = null;
						ImageButton den_pauseBtn = null;

						Button den_navBtn = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button den_upArrowBtn = null;
						Button den_downArrowBtn = null;
						Button den_leftArrowBtn = null;
						Button den_rightArrowBtn = null;
						Button den_center_select_Btn = null;

						Button den_exitBtn = null;

						Button den_oneBtn = null;
						Button den_twoBtn = null;
						Button den_threeBtn = null;

						Button den_fourBtn = null;
						Button den_fiveBtn = null;
						Button den_sixBtn = null;

						Button den_sevenBtn = null;
						Button den_eightBtn = null;
						Button den_nineBtn = null;

						Button den_zeroBtn = null;
						View denView = inflater.inflate(R.layout.den_remote, null);

						den_redBtn = (ImageButton) denView.findViewById(R.id.den_redBtn);
						den_greenBtn = (ImageButton) denView.findViewById(R.id.den_greenBtn);
						den_yellowBtn = (ImageButton) denView.findViewById(R.id.den_yellowBtn);
						den_blueBtn = (ImageButton) denView.findViewById(R.id.den_blueBtn);

						den_rewindBtn = (ImageButton) denView.findViewById(R.id.den_rewindBtn);
						den_playBtn = (ImageButton) denView.findViewById(R.id.den_playBtn);
						den_forwardBtn = (ImageButton) denView.findViewById(R.id.den_forwardBtn);
						den_stopBtn = (ImageButton) denView.findViewById(R.id.den_stopBtn);
						den_pauseBtn = (ImageButton) denView.findViewById(R.id.den_pauseBtn);

						den_navBtn = (Button) denView.findViewById(R.id.den_navBtn);
						den_offBtn = (ImageButton) denView.findViewById(R.id.den_offBtn);

						den_upArrowBtn = (Button) denView.findViewById(R.id.den_upArrowBtn);
						den_downArrowBtn = (Button) denView.findViewById(R.id.den_downArrowBtn);
						den_rightArrowBtn= (Button) denView.findViewById(R.id.den_rightArrowBtn);
						den_leftArrowBtn = (Button) denView.findViewById(R.id.den_leftArrowBtn);
						den_center_select_Btn = (Button) denView.findViewById(R.id.den_center_select_Btn);

						den_exitBtn = (Button) denView.findViewById(R.id.den_exitBtn);

						den_oneBtn = (Button) denView.findViewById(R.id.den_oneBtn);
						den_twoBtn= (Button) denView.findViewById(R.id.den_twoBtn);
						den_threeBtn = (Button) denView.findViewById(R.id.den_threeBtn);
						den_fourBtn= (Button) denView.findViewById(R.id.den_fourBtn);
						den_fiveBtn = (Button) denView.findViewById(R.id.den_fiveBtn);
						den_sixBtn = (Button) denView.findViewById(R.id.den_sixBtn);
						den_sevenBtn = (Button) denView.findViewById(R.id.den_sevenBtn);
						den_eightBtn = (Button) denView.findViewById(R.id.den_eightBtn);
						den_nineBtn= (Button) denView.findViewById(R.id.den_nineBtn);
						den_zeroBtn = (Button) denView.findViewById(R.id.den_zeroBtn);

						dropdownButton = (Button) denView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) denView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForDenCable("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForDenCable("SD");
								}
							});
						}

						if(den_navBtn != null){
							den_navBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_NAVIGATE,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_offBtn != null){
							den_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_POWER,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_redBtn != null){
							den_redBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_RED,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_greenBtn != null){
							den_greenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_GREEN,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_yellowBtn != null){
							den_yellowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_YELLOW,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_blueBtn != null){
							den_blueBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_BLUE,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_rewindBtn != null){
							den_rewindBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_REWIND,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_playBtn != null){
							den_playBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_PLAY,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_forwardBtn != null){
							den_forwardBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_FORWARD,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_stopBtn != null){
							den_stopBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_STOP,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_pauseBtn != null){
							den_pauseBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_PAUSE,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_upArrowBtn != null){
							den_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_UPARROW,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_downArrowBtn != null){
							den_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_DOWNARROW,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_leftArrowBtn != null){
							den_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_LEFTARROW,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_rightArrowBtn != null){
							den_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_RIGHTARROW,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_center_select_Btn != null){
							if (Constant.DEBUG)	Log.d(TAG, "Den Press Ok");
							den_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_OK,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_exitBtn!= null){
							den_exitBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_EXIT,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_oneBtn != null){
							den_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_ONE,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_twoBtn != null){
							den_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_TWO,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_threeBtn != null){
							den_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_THREE,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(den_fourBtn != null){
							den_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_FOUR,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_fiveBtn != null){
							den_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_FIVE,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_sixBtn != null){
							den_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_SIX,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}


						if(den_sevenBtn != null){
							den_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_SEVEN,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_eightBtn != null){
							den_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_EIGHT,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_nineBtn != null){
							den_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_NINE,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(den_zeroBtn != null){
							den_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.DEN_KEY_ZERO,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(denView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(denView);
						}
					}
				}
			});
		}
	}

	private void showExtraButtonsForDenCable(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForDenCable ");
			LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.DEN_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.DEN_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}

			final Dialog listoverlay = new Dialog(mTvRemoteInstance);
			TextView titleView;
			View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
			listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
			listoverlay.setContentView(listOverlay);
			titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
			if(titleView != null){
				titleView.setText("Sevice Buttons");
			}
			if(Constant.DEBUG) System.out.println("List Size "+filterList.size());

			if(listoverlay != null){
//				listoverlay.setOnKeyListener(TVRemote.this);
				listoverlay.show();
				if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
				RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
				ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
				if(filterList != null && filterList.size() > 0 ){
					if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
					if(ovelaylistlayer != null){
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
						listView.setCacheColorHint(Color.TRANSPARENT);
						ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
						listView.setAdapter(adapter);
						if(listView != null){
							ovelaylistlayer.addView(listView);
							listView.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
									HashMap<String, String> getinglist = filterList.get(arg2);
									String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);

									String[] DEN_EXTRAKEYS_LIST = {"Favourites","Last","GUIDE","Menu","Options","VOD","TV/Radio","Setup"};
									String id = getinglist.get("id");

									if(name != null && !name.equalsIgnoreCase("")){
										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[0])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[0]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[0]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_FAVOURITE, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
										}
										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[1])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[1]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[1]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_LAST, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
										}
										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[2])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[2]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[2]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_KEY_EPG, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
										}
										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[3])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[3]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[3]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_KEY_MENU, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
										}

										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[4])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[4]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[4]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_KEY_OPT, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
										}
										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[5])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[5]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[5]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_KEY_VOD, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[5]+" Aftrer calling IR");
										}
										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[6])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[6]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[6]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_TVRADIO, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[6]+" Aftrer calling IR");
										}
										if(name.equalsIgnoreCase(DEN_EXTRAKEYS_LIST[7])){
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[7]+" Clicked");
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[7]+" Before calling IR");
											IRTransmitter.IRInput(OperatorKeys.DEN_SETUP, DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
											if(Constant.DEBUG)  Log.d(TAG, DEN_EXTRAKEYS_LIST[7]+" Aftrer calling IR");
										}
									}

									try{
										if(listoverlay != null && listoverlay.isShowing())
											listoverlay.dismiss();
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							});
						}
					}
					ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);

					if(closeBtn != null){
						if(listoverlay != null && listoverlay.isShowing())
							listoverlay.dismiss();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
	private void initDishTvUi(){
		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){
//						IRTransmitter.stopIr();
//						DataStorage.getIrhandler() = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, DataStorage.getConnectedVendor());

						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);

						/**
						 * DishTv IR structures
						 */
						//	private static int irhandler = 0;
						ImageButton dishtv_redBtn = null;
						ImageButton dishtv_greenBtn = null;
						ImageButton dishtv_yellowBtn = null;
						ImageButton dishtv_blueBtn = null;
						ImageButton dishtv_offBtn = null;

						ImageButton dishtv_rewindBtn = null;
						ImageButton dishtv_playBtn = null;
						ImageButton dishtv_forwardBtn = null;
						ImageButton dishtv_pauseBtn = null;

						ImageButton dishtv_stopBtn = null;
						ImageButton dishtv_recordBtn = null;
						ImageButton dishtv_jumptostartBtn = null;
						ImageButton dishtv_jumptoendBtn = null;

						Button dishtv_backBtn = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button dishtv_plusBtn = null;
						Button dishtv_minusBtn = null;

						Button dishtv_upArrowBtn = null;
						Button dishtv_downArrowBtn = null;
						Button dishtv_leftArrowBtn = null;
						Button dishtv_rightArrowBtn = null;
						Button dishtv_center_select_Btn = null;

						Button dishtv_exitBtn = null;

						Button dishtv_oneBtn = null;
						Button dishtv_twoBtn = null;
						Button dishtv_threeBtn = null;

						Button dishtv_fourBtn = null;
						Button dishtv_fiveBtn = null;
						Button dishtv_sixBtn = null;

						Button dishtv_sevenBtn = null;
						Button dishtv_eightBtn = null;
						Button dishtv_nineBtn = null;

						Button dishtv_zeroBtn = null;
						View dishtvView = inflater.inflate(R.layout.dishtv_remote, null);

						dishtv_redBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_redBtn);
						dishtv_greenBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_greenBtn);
						dishtv_yellowBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_yellowBtn);
						dishtv_blueBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_blueBtn);

						dishtv_rewindBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_rewindBtn);
						dishtv_playBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_playBtn);
						dishtv_forwardBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_forwardBtn);
						dishtv_pauseBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_pauseBtn);

						dishtv_stopBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_stopBtn);
						dishtv_recordBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_recordBtn);
						dishtv_jumptostartBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_jumptostartBtn);
						dishtv_jumptoendBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_jumptoendBtn);

						dishtv_plusBtn = (Button) dishtvView.findViewById(R.id.dishtv_plusBtn);
						dishtv_minusBtn = (Button) dishtvView.findViewById(R.id.dishtv_minusBtn);

						dishtv_backBtn = (Button) dishtvView.findViewById(R.id.dishtv_backBtn);
						dishtv_exitBtn = (Button) dishtvView.findViewById(R.id.dishtv_exitBtn);
						dishtv_offBtn = (ImageButton) dishtvView.findViewById(R.id.dishtv_offBtn);

						dishtv_upArrowBtn = (Button) dishtvView.findViewById(R.id.dishtv_upArrowBtn);
						dishtv_downArrowBtn = (Button) dishtvView.findViewById(R.id.dishtv_downArrowBtn);
						dishtv_rightArrowBtn= (Button) dishtvView.findViewById(R.id.dishtv_rightArrowBtn);
						dishtv_leftArrowBtn = (Button) dishtvView.findViewById(R.id.dishtv_leftArrowBtn);
						dishtv_center_select_Btn = (Button) dishtvView.findViewById(R.id.dishtv_center_select_Btn);

						dishtv_oneBtn = (Button) dishtvView.findViewById(R.id.dishtv_oneBtn);
						dishtv_twoBtn= (Button) dishtvView.findViewById(R.id.dishtv_twoBtn);
						dishtv_threeBtn = (Button) dishtvView.findViewById(R.id.dishtv_threeBtn);
						dishtv_fourBtn= (Button) dishtvView.findViewById(R.id.dishtv_fourBtn);
						dishtv_fiveBtn = (Button) dishtvView.findViewById(R.id.dishtv_fiveBtn);
						dishtv_sixBtn = (Button) dishtvView.findViewById(R.id.dishtv_sixBtn);
						dishtv_sevenBtn = (Button) dishtvView.findViewById(R.id.dishtv_sevenBtn);
						dishtv_eightBtn = (Button) dishtvView.findViewById(R.id.dishtv_eightBtn);
						dishtv_nineBtn= (Button) dishtvView.findViewById(R.id.dishtv_nineBtn);
						dishtv_zeroBtn = (Button) dishtvView.findViewById(R.id.dishtv_zeroBtn);

						dropdownButton = (Button) dishtvView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) dishtvView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForDishTv("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForDishTv("SD");
								}
							});
						}

						if(dishtv_backBtn != null){
							dishtv_backBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","back"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_offBtn != null){
							dishtv_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","power"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_redBtn != null){
							dishtv_redBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","red"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_greenBtn != null){
							dishtv_greenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","green"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_yellowBtn != null){
							dishtv_yellowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","yellow"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_blueBtn != null){
							dishtv_blueBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","blue"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_recordBtn != null){
							dishtv_recordBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","record"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_jumptostartBtn != null){
							dishtv_jumptostartBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","rewindend"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_jumptoendBtn != null){
							dishtv_jumptoendBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","forwardend"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_rewindBtn != null){
							dishtv_rewindBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","reverse"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_playBtn != null){
							dishtv_playBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","play"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_forwardBtn != null){
							dishtv_forwardBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","forward"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_stopBtn != null){
							dishtv_stopBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","stop"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_pauseBtn != null){
							dishtv_pauseBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","pause"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_upArrowBtn != null){
							dishtv_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","up"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_downArrowBtn != null){
							dishtv_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","down"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_leftArrowBtn != null){
							dishtv_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","left"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_rightArrowBtn != null){
							if (Constant.DEBUG)	Log.d(TAG, "Dish Tv Press Ok");
							dishtv_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","right"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_center_select_Btn != null){
							dishtv_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","select"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_exitBtn!= null){
							dishtv_exitBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","exit"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_plusBtn != null){
							dishtv_plusBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","programplus"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_minusBtn != null){
							dishtv_minusBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","programminus"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_oneBtn != null){
							dishtv_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","1"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_twoBtn != null){
							dishtv_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","2"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_threeBtn != null){
							dishtv_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","3"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(dishtv_fourBtn != null){
							dishtv_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","4"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_fiveBtn != null){
							dishtv_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","5"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_sixBtn != null){
							dishtv_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","6"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_sevenBtn != null){
							dishtv_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","7"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_eightBtn != null){
							dishtv_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","8"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_nineBtn != null){
							dishtv_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","9"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(dishtv_zeroBtn != null){
							dishtv_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("dishtv","0"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						//						DISHTV_VOLUMEUP = 10,
						//						DISHTV_VOLUMEDOWN = 11,
						//						DISHTV_CHANNELUP =12,
						//						DISHTV_CHANNELDOWN =13,

						if(dishtvView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(dishtvView);
						}
					}
				}
			});
		}
	}

	private void showExtraButtonsForDishTv(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForDishTv ");
			
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.DISHTV_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.DISHTV_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					final Dialog listoverlay = new Dialog(mTvRemoteInstance);
					TextView titleView;
					View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
					listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
					listoverlay.setContentView(listOverlay);
					titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
					if(titleView != null){
						titleView.setText("Sevice Buttons");
					}
					if(Constant.DEBUG) System.out.println("List Size "+filterList.size());
		
					if(listoverlay != null){
		//				listoverlay.setOnKeyListener(TVRemote.this);
						listoverlay.show();
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
						RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
						ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
						if(filterList != null && filterList.size() > 0 ){
							if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
							if(ovelaylistlayer != null){
								if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
								listView.setCacheColorHint(Color.TRANSPARENT);
								ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
								listView.setAdapter(adapter);
								if(listView != null){
									ovelaylistlayer.addView(listView);
									listView.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
											HashMap<String, String> getinglist = filterList.get(arg2);
											String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);
		
											String[] DISHTV_EXTRAKEYS_LIST = {"Prev Channel","Menu","Buzz @ DishTV","Language","My Account","Active","Games","Favourites","Email","File","Time Shift","MOD","Auto Tune","EPG","Mini EPG"};
											String id = getinglist.get("id");
		
											if(name != null && !name.equalsIgnoreCase("")){
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[0])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[0]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[0]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"previouschannel"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[1])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[1]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[1]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"menu"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[2])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[2]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[2]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"buzz"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[3])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[3]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[3]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"lang"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
												}
		
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[4])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"myac"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[5])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[5]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[5]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"active"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[5]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[6])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[6]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[6]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"games"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[6]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[7])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[7]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[7]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"favourite"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[7]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[8])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[8]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[8]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"message"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[8]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[9])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[9]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[9]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"file"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[9]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[10])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[10]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[10]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"timeshift"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[10]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[11])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[11]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[11]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"mod"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[11]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[12])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[12]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[12]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"clock"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[12]+" Aftrer calling IR");
												}
		
		
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[13])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[13]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[13]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"guide"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[13]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(DISHTV_EXTRAKEYS_LIST[14])){
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[14]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[14]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"info"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, DISHTV_EXTRAKEYS_LIST[14]+" Aftrer calling IR");
												}
											}
											try{
												if(listoverlay != null && listoverlay.isShowing())
													listoverlay.dismiss();
											}catch(Exception e){
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
											}
										}
									});
								}
							}
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
		
							if(closeBtn != null){
								if(listoverlay != null && listoverlay.isShowing())
									listoverlay.dismiss();
							}
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}

	
	private void inintHathwayremote(){

		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){
//						IRTransmitter.stopIr();
//						DataStorage.getIrhandler() = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, DataStorage.getConnectedVendor());
						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);
						View hathwayView = inflater.inflate(R.layout.hathway, null);

						ImageButton hathway_redBtn = null;
						ImageButton hathway_greenBtn = null;
						ImageButton hathway_yellowBtn = null;
						ImageButton hathway_blueBtn = null;

						Button hathway_upArrowBtn = null;
						Button hathway_downArrowBtn = null;
						Button hathway_leftArrowBtn = null;
						Button hathway_rightArrowBtn = null;
						Button hathway_center_select_Btn = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button hathway_backBtn = null;
						Button hathway_helpBtn = null;

						Button hathway_oneBtn = null;
						Button hathway_twoBtn = null;
						Button hathway_threeBtn = null;

						Button hathway_fourBtn = null;
						Button hathway_fiveBtn = null;
						Button hathway_sixBtn = null;

						Button hathway_sevenBtn = null;
						Button hathway_eightBtn = null;
						Button hathway_nineBtn = null;

						Button hathway_zeroBtn = null;

						ImageButton hathway_offBtn = null;
						hathway_redBtn = (ImageButton) hathwayView.findViewById(R.id.hathway_redBtn);
						hathway_greenBtn = (ImageButton) hathwayView.findViewById(R.id.hathway_greenBtn);
						hathway_yellowBtn = (ImageButton) hathwayView.findViewById(R.id.hathway_yellowBtn);
						hathway_blueBtn = (ImageButton) hathwayView.findViewById(R.id.hathway_blueBtn);

						hathway_backBtn = (Button) hathwayView.findViewById(R.id.hathway_backBtn);
						hathway_offBtn = (ImageButton) hathwayView.findViewById(R.id.hathway_offBtn);

						hathway_upArrowBtn = (Button) hathwayView.findViewById(R.id.hathway_upArrowBtn);
						hathway_downArrowBtn = (Button) hathwayView.findViewById(R.id.hathway_downArrowBtn);
						hathway_rightArrowBtn= (Button) hathwayView.findViewById(R.id.hathway_rightArrowBtn);
						hathway_leftArrowBtn = (Button) hathwayView.findViewById(R.id.hathway_leftArrowBtn);
						hathway_center_select_Btn = (Button) hathwayView.findViewById(R.id.hathway_center_select_Btn);

						hathway_helpBtn = (Button) hathwayView.findViewById(R.id.hathway_helpBtn);

						hathway_oneBtn = (Button) hathwayView.findViewById(R.id.hathway_oneBtn);
						hathway_twoBtn= (Button) hathwayView.findViewById(R.id.hathway_twoBtn);
						hathway_threeBtn = (Button) hathwayView.findViewById(R.id.hathway_threeBtn);
						hathway_fourBtn= (Button) hathwayView.findViewById(R.id.hathway_fourBtn);
						hathway_fiveBtn = (Button) hathwayView.findViewById(R.id.hathway_fiveBtn);
						hathway_sixBtn = (Button) hathwayView.findViewById(R.id.hathway_sixBtn);
						hathway_sevenBtn = (Button) hathwayView.findViewById(R.id.hathway_sevenBtn);
						hathway_eightBtn = (Button) hathwayView.findViewById(R.id.hathway_eightBtn);
						hathway_nineBtn= (Button) hathwayView.findViewById(R.id.hathway_nineBtn);
						hathway_zeroBtn = (Button) hathwayView.findViewById(R.id.hathway_zeroBtn);

						dropdownButton = (Button) hathwayView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) hathwayView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForHathway("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForHathway("SD");
								}
							});
						}

						if(hathway_backBtn != null){
							hathway_backBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","back"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(hathway_offBtn != null){
							hathway_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","power"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(hathway_redBtn != null){
							hathway_redBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","red"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_greenBtn != null){
							hathway_greenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","green"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_yellowBtn != null){
							hathway_yellowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","yellow"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_blueBtn != null){
							hathway_blueBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","blue"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}


						if(hathway_upArrowBtn != null){
							hathway_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","up"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_downArrowBtn != null){
							hathway_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","down"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_leftArrowBtn != null){
							hathway_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","left"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_rightArrowBtn != null){
							hathway_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","right"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_center_select_Btn != null){
							if (Constant.DEBUG)	Log.d(TAG, "Hathway Press Ok");
							hathway_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","select"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(hathway_helpBtn!= null){
							hathway_helpBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","help"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(hathway_oneBtn != null){
							hathway_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","1"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_twoBtn != null){
							hathway_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","2"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_threeBtn != null){
							hathway_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","3"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(hathway_fourBtn != null){
							hathway_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","4"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_fiveBtn != null){
							hathway_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","5"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_sixBtn != null){
							hathway_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","6"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}


						if(hathway_sevenBtn != null){
							hathway_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","7"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_eightBtn != null){
							hathway_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","8"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_nineBtn != null){
							hathway_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","9"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathway_zeroBtn != null){
							hathway_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("hathway","0"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(hathwayView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(hathwayView);
						}
					}

				}
			});
		}

	}

	private void showExtraButtonsForHathway(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForHathway ");
			LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.HATHWAY_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.HATHWAY_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					final Dialog listoverlay = new Dialog(mTvRemoteInstance);
					TextView titleView;
					View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
					listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
					listoverlay.setContentView(listOverlay);
					titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
					if(titleView != null){
						titleView.setText("Sevice Buttons");
					}
					if(Constant.DEBUG) System.out.println("List Size "+filterList.size());
		
					if(listoverlay != null){
		//				listoverlay.setOnKeyListener(TVRemote.this);
						listoverlay.show();
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
						RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
						ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
						if(filterList != null && filterList.size() > 0 ){
							if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
							if(ovelaylistlayer != null){
								if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
								listView.setCacheColorHint(Color.TRANSPARENT);
								ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
								listView.setAdapter(adapter);
								if(listView != null){
									ovelaylistlayer.addView(listView);
									listView.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
											HashMap<String, String> getinglist = filterList.get(arg2);
											String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);
											String[] HATHWAY_EXTRAKEYS_LIST = {"Radio","Menu","EPG","Games","Interactive","Favourite","Language","Message","Account","Promo Channel"};
											String id = getinglist.get("id");
											if(name != null && !name.equalsIgnoreCase("")){
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[0])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[0]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[0]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"radio"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[1])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[1]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[1]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"menu"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[2])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[2]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[2]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"epg"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[3])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[3]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[3]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"games"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[4])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"int"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[5])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[5]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[5]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"fav"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[5]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[6])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[6]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[6]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"lng"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[6]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[7])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[7]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[7]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"msg"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[7]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[8])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[8]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[8]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"rs"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[8]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(HATHWAY_EXTRAKEYS_LIST[9])){
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[9]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[9]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"promo"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, HATHWAY_EXTRAKEYS_LIST[9]+" Aftrer calling IR");
												}
											}
											try{
												if(listoverlay != null && listoverlay.isShowing())
													listoverlay.dismiss();
											}catch(Exception e){
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
											}
										}
									});
								}
							}
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
		
							if(closeBtn != null){
								if(listoverlay != null && listoverlay.isShowing())
									listoverlay.dismiss();
							}
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	

	private void initInCableUi(){
		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){
//						IRTransmitter.stopIr();
//						DataStorage.getIrhandler() = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, DataStorage.getConnectedVendor());

						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);

						/**
						 * InCable IR structures
						 */
						//	private static int irhandler = 0;
						ImageButton incable_redBtn = null;
						ImageButton incable_greenBtn = null;
						ImageButton incable_yellowBtn = null;
						ImageButton incable_blueBtn = null;

						ImageButton incable_offBtn = null;

						ImageButton incable_rewindBtn = null;
						ImageButton incable_recordBtn = null;
						ImageButton incable_stopBtn = null;
						ImageButton incable_forwardBtn = null;

						ImageButton incable_playBtn = null;
						ImageButton incable_gotoBtn = null;

						Button incable_backBtn = null;
						Button incable_exitBtn = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button incable_upArrowBtn = null;
						Button incable_downArrowBtn = null;
						Button incable_leftArrowBtn = null;
						Button incable_rightArrowBtn = null;
						Button incable_center_select_Btn = null;

						Button incable_oneBtn = null;
						Button incable_twoBtn = null;
						Button incable_threeBtn = null;

						Button incable_fourBtn = null;
						Button incable_fiveBtn = null;
						Button incable_sixBtn = null;

						Button incable_sevenBtn = null;
						Button incable_eightBtn = null;
						Button incable_nineBtn = null;

						Button incable_zeroBtn = null;
						View incableView = inflater.inflate(R.layout.incable_remote, null);

						incable_redBtn = (ImageButton) incableView.findViewById(R.id.incable_redBtn);
						incable_greenBtn = (ImageButton) incableView.findViewById(R.id.incable_greenBtn);
						incable_yellowBtn = (ImageButton) incableView.findViewById(R.id.incable_yellowBtn);
						incable_blueBtn = (ImageButton) incableView.findViewById(R.id.incable_blueBtn);

						incable_rewindBtn = (ImageButton) incableView.findViewById(R.id.incable_rewindBtn);
						incable_playBtn = (ImageButton) incableView.findViewById(R.id.incable_playBtn);
						incable_forwardBtn = (ImageButton) incableView.findViewById(R.id.incable_forwardBtn);
						incable_stopBtn = (ImageButton) incableView.findViewById(R.id.incable_stopBtn);
						incable_gotoBtn = (ImageButton) incableView.findViewById(R.id.incable_gotoBtn);

						incable_backBtn = (Button) incableView.findViewById(R.id.incable_backBtn);
						incable_offBtn = (ImageButton) incableView.findViewById(R.id.incable_offBtn);

						incable_upArrowBtn = (Button) incableView.findViewById(R.id.incable_upArrowBtn);
						incable_downArrowBtn = (Button) incableView.findViewById(R.id.incable_downArrowBtn);
						incable_rightArrowBtn= (Button) incableView.findViewById(R.id.incable_rightArrowBtn);
						incable_leftArrowBtn = (Button) incableView.findViewById(R.id.incable_leftArrowBtn);
						incable_center_select_Btn = (Button) incableView.findViewById(R.id.incable_center_select_Btn);

						incable_exitBtn = (Button) incableView.findViewById(R.id.incable_exitBtn);

						incable_oneBtn = (Button) incableView.findViewById(R.id.incable_oneBtn);
						incable_twoBtn= (Button) incableView.findViewById(R.id.incable_twoBtn);
						incable_threeBtn = (Button) incableView.findViewById(R.id.incable_threeBtn);
						incable_fourBtn= (Button) incableView.findViewById(R.id.incable_fourBtn);
						incable_fiveBtn = (Button) incableView.findViewById(R.id.incable_fiveBtn);
						incable_sixBtn = (Button) incableView.findViewById(R.id.incable_sixBtn);
						incable_sevenBtn = (Button) incableView.findViewById(R.id.incable_sevenBtn);
						incable_eightBtn = (Button) incableView.findViewById(R.id.incable_eightBtn);
						incable_nineBtn= (Button) incableView.findViewById(R.id.incable_nineBtn);
						incable_zeroBtn = (Button) incableView.findViewById(R.id.incable_zeroBtn);

						dropdownButton = (Button) incableView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) incableView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForInCable("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForInCable("SD");
								}
							});
						}

						if(incable_backBtn != null){
							incable_backBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","back"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_offBtn != null){
							incable_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","standby"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						//						if(incable_redBtn != null){
						//							incable_redBtn.setOnClickListener(new OperatorButtonClickListener("incable_redBtn",incable_redBtn.getId(), OperatorKeys.INCABLE_R,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						//						}
						//						if(incable_greenBtn != null){
						//							incable_greenBtn.setOnClickListener(new OperatorButtonClickListener("incable_greenBtn",incable_greenBtn.getId(), OperatorKeys.INCABLE_MALL,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						//						}
						//						if(incable_yellowBtn != null){
						//							incable_yellowBtn.setOnClickListener(new OperatorButtonClickListener("incable_yellowBtn",incable_yellowBtn.getId(), OperatorKeys.INCABLE_FORMAT,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						//						}
						//						if(incable_blueBtn != null){
						//							incable_blueBtn.setOnClickListener(new OperatorButtonClickListener("incable_blueBtn",incable_blueBtn.getId(), OperatorKeys.INCABLE_AUDIO,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						//						}

						if(incable_rewindBtn != null){
							incable_rewindBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","rewind"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_playBtn != null){
							incable_playBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","play"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_forwardBtn != null){
							incable_forwardBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","forward"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_stopBtn != null){
							incable_stopBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","stop"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_gotoBtn != null){
							incable_gotoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","goto"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_upArrowBtn != null){
							incable_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","up"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_downArrowBtn != null){
							incable_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","down"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_leftArrowBtn != null){
							incable_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","left"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_rightArrowBtn != null){
							incable_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","right"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_center_select_Btn != null){
							if (Constant.DEBUG)	Log.d(TAG, "In Cable Press Ok");
							incable_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","select"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_exitBtn!= null){
							incable_exitBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","exit"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_oneBtn != null){
							incable_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","1"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_twoBtn != null){
							incable_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","2"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_threeBtn != null){
							incable_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","3"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_fourBtn != null){
							incable_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","4"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_fiveBtn != null){
							incable_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","5"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_sixBtn != null){
							incable_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","6"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(incable_sevenBtn != null){
							incable_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","7"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_eightBtn != null){
							incable_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","8"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_nineBtn != null){
							incable_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","9"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(incable_zeroBtn != null){
							incable_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("incable","0"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						//						INCABLE_VOLUMEUP = 23,
						//						INCABLE_VOLUMEDOWN = 24,
						//						INCABLE_CHANNELUP = 26,
						//						INCABLE_CHANNELDOWN = 27,

						if(incableView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(incableView);
						}
					}
				}
			});
		}
	}


	private void showExtraButtonsForInCable(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForInCable ");
			
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.INCABLE_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.INCABLE_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}

			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					final Dialog listoverlay = new Dialog(mTvRemoteInstance);
					TextView titleView;
					View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
					listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
					listoverlay.setContentView(listOverlay);
					titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
					if(titleView != null){
						titleView.setText("Sevice Buttons");
					}
					if(Constant.DEBUG) System.out.println("List Size "+filterList.size());
		
					if(listoverlay != null){
		//				listoverlay.setOnKeyListener(TVRemote.this);
						listoverlay.show();
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
						RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
						ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
						if(filterList != null && filterList.size() > 0 ){
							if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
							if(ovelaylistlayer != null){
								if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
								listView.setCacheColorHint(Color.TRANSPARENT);
								ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
								listView.setAdapter(adapter);
								if(listView != null){
									ovelaylistlayer.addView(listView);
									listView.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
											HashMap<String, String> getinglist = filterList.get(arg2);
											String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);
											String[] INCABLE_EXTRAKEYS_LIST = {"Help","Menu","List","Favourites","Audio","TV/Radio"};
											String id = getinglist.get("id");
											if(name != null && !name.equalsIgnoreCase("")){
												if(name.equalsIgnoreCase(INCABLE_EXTRAKEYS_LIST[0])){
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[0]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[0]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"qmark"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(INCABLE_EXTRAKEYS_LIST[1])){
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[1]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[1]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"menu"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(INCABLE_EXTRAKEYS_LIST[2])){
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[2]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[2]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"list"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(INCABLE_EXTRAKEYS_LIST[3])){
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[3]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[3]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"fav"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(INCABLE_EXTRAKEYS_LIST[4])){
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"audio"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(INCABLE_EXTRAKEYS_LIST[5])){
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[5]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[5]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"tvradio"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, INCABLE_EXTRAKEYS_LIST[5]+" Aftrer calling IR");
												}
											}
		
											try{
												if(listoverlay != null && listoverlay.isShowing())
													listoverlay.dismiss();
											}catch(Exception e){
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
											}
										}
									});
								}
							}
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
		
							if(closeBtn != null){
								if(listoverlay != null && listoverlay.isShowing())
									listoverlay.dismiss();
							}
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
	private void initSundirectUi(){
		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){
//						IRTransmitter.stopIr();
//						DataStorage.getIrhandler() = IRTransmitter.startIr(ScreenStyles.DEVICE_NAME_IR, DataStorage.getConnectedVendor());

						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);

						/**
						 * Sundirect IR structures
						 */
						//	private static int irhandler = 0;
						ImageButton sun_redBtn = null;
						ImageButton sun_greenBtn = null;
						ImageButton sun_yellowBtn = null;
						ImageButton sun_blueBtn = null;

						ImageButton sun_stopBtn = null;
						ImageButton sun_playBtn = null;
						ImageButton sun_playpauseBtn = null;
						ImageButton sun_recordBtn = null;

						ImageButton sun_offBtn = null;
						Button sun_recallBtn = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button sun_upArrowBtn = null;
						Button sun_downArrowBtn = null;
						Button sun_leftArrowBtn = null;
						Button sun_rightArrowBtn = null;
						Button sun_center_select_Btn = null;

						Button sun_tvBtn = null;

						Button sun_oneBtn = null;
						Button sun_twoBtn = null;
						Button sun_threeBtn = null;

						Button sun_fourBtn = null;
						Button sun_fiveBtn = null;
						Button sun_sixBtn = null;

						Button sun_sevenBtn = null;
						Button sun_eightBtn = null;
						Button sun_nineBtn = null;

						Button sun_zeroBtn = null;
						View sunView = inflater.inflate(R.layout.sun_remote, null);

						sun_redBtn = (ImageButton) sunView.findViewById(R.id.sun_redBtn);
						sun_greenBtn = (ImageButton) sunView.findViewById(R.id.sun_greenBtn);
						sun_yellowBtn = (ImageButton) sunView.findViewById(R.id.sun_yellowBtn);
						sun_blueBtn = (ImageButton) sunView.findViewById(R.id.sun_blueBtn);

						sun_stopBtn = (ImageButton) sunView.findViewById(R.id.sun_stopBtn);
						sun_playBtn = (ImageButton) sunView.findViewById(R.id.sun_playBtn);
						sun_playpauseBtn = (ImageButton) sunView.findViewById(R.id.sun_playpauseBtn);
						sun_recordBtn = (ImageButton) sunView.findViewById(R.id.sun_recordBtn);

						sun_recallBtn = (Button) sunView.findViewById(R.id.sun_recallBtn);
						sun_offBtn = (ImageButton) sunView.findViewById(R.id.sun_offBtn);
						sun_tvBtn = (Button) sunView.findViewById(R.id.sun_tvBtn);

						sun_upArrowBtn = (Button) sunView.findViewById(R.id.sun_upArrowBtn);
						sun_downArrowBtn = (Button) sunView.findViewById(R.id.sun_downArrowBtn);
						sun_rightArrowBtn= (Button) sunView.findViewById(R.id.sun_rightArrowBtn);
						sun_leftArrowBtn = (Button) sunView.findViewById(R.id.sun_leftArrowBtn);
						sun_center_select_Btn = (Button) sunView.findViewById(R.id.sun_center_select_Btn);

						sun_oneBtn = (Button) sunView.findViewById(R.id.sun_oneBtn);
						sun_twoBtn= (Button) sunView.findViewById(R.id.sun_twoBtn);
						sun_threeBtn = (Button) sunView.findViewById(R.id.sun_threeBtn);
						sun_fourBtn= (Button) sunView.findViewById(R.id.sun_fourBtn);
						sun_fiveBtn = (Button) sunView.findViewById(R.id.sun_fiveBtn);
						sun_sixBtn = (Button) sunView.findViewById(R.id.sun_sixBtn);
						sun_sevenBtn = (Button) sunView.findViewById(R.id.sun_sevenBtn);
						sun_eightBtn = (Button) sunView.findViewById(R.id.sun_eightBtn);
						sun_nineBtn= (Button) sunView.findViewById(R.id.sun_nineBtn);
						sun_zeroBtn = (Button) sunView.findViewById(R.id.sun_zeroBtn);

						dropdownButton = (Button) sunView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) sunView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForSunDirect("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForSunDirect("SD");
								}
							});
						}

						if(sun_recallBtn != null){
							sun_recallBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","recall"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_tvBtn!= null){
							sun_tvBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","tvradio"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_offBtn != null){
							sun_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","power"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_stopBtn != null){
							sun_stopBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","stop"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_playBtn!= null){
							sun_playBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","tms"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_playpauseBtn != null){
							sun_playpauseBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","slow"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_recordBtn != null){
							sun_recordBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","record"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_redBtn != null){
							sun_redBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","red"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_greenBtn != null){
							sun_greenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","green"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_yellowBtn != null){
							sun_yellowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","yellow"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_blueBtn != null){
							sun_blueBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","blue"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}


						if(sun_upArrowBtn != null){
							sun_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","up"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_downArrowBtn != null){
							sun_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","down"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_leftArrowBtn != null){
							sun_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","left"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_rightArrowBtn != null){
							sun_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","right"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_center_select_Btn != null){
							if (Constant.DEBUG)	Log.d(TAG, "Sun TV Press Ok");
							sun_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","select"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_oneBtn != null){
							sun_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","1"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_twoBtn != null){
							sun_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","2"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_threeBtn != null){
							sun_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","3"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(sun_fourBtn != null){
							sun_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","4"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_fiveBtn != null){
							sun_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","5"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_sixBtn != null){
							sun_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","6"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}


						if(sun_sevenBtn != null){
							sun_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","7"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_eightBtn != null){
							sun_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","8"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_nineBtn != null){
							sun_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","9"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(sun_zeroBtn != null){
							sun_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("sundirect","0"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						//						SUNDIRECT_CHANNELUP = 33,
						//						SUNDIRECT_CHANNELDOWN	= 34,
						//						SUNDIRECT_VOLUMEUP = 35, 
						//						SUNDIRECT_VOLUMEDOWN = 36,

						if(sunView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(sunView);
						}
					}
				}
			});
		}
	}

	private void showExtraButtonsForSunDirect(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForHathway ");
			LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.SUNDIRECT_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.SUNDIRECT_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					final Dialog listoverlay = new Dialog(mTvRemoteInstance);
					TextView titleView;
					View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
					listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
					listoverlay.setContentView(listOverlay);
					titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
					if(titleView != null){
						titleView.setText("Sevice Buttons");
					}
					if(Constant.DEBUG) System.out.println("List Size "+filterList.size());
		
					if(listoverlay != null){
		//				listoverlay.setOnKeyListener(TVRemote.this);
						listoverlay.show();
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
						RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
						ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
						if(filterList != null && filterList.size() > 0 ){
							if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
							if(ovelaylistlayer != null){
								if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
								listView.setCacheColorHint(Color.TRANSPARENT);
								ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
								listView.setAdapter(adapter);
								if(listView != null){
									ovelaylistlayer.addView(listView);
									listView.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
											HashMap<String, String> getinglist = filterList.get(arg2);
											String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);
		
		
											String[] SUNDIRECT_EXTRAKEYS_LIST = {"Language","Audio","Reminder","Favourite","Menu","Media","Games","Guide","Exit"};
											String id = getinglist.get("id");
											if(name != null && !name.equalsIgnoreCase("")){
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[0])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[0]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[0]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"language"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[1])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[1]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[1]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"audio"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[2])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[2]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[2]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"remainder"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[3])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[3]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[3]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"favourite"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
												}
		
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[4])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"menu"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[5])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[5]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[5]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"media"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[5]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[6])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[6]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[6]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"game"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[6]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[7])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[7]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[7]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"guide"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[7]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(SUNDIRECT_EXTRAKEYS_LIST[8])){
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[8]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[8]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"exit"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, SUNDIRECT_EXTRAKEYS_LIST[8]+" Aftrer calling IR");
												}
											}
		
											try{
												if(listoverlay != null && listoverlay.isShowing())
													listoverlay.dismiss();
											}catch(Exception e){
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
											}
										}
									});
								}
							}
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
		
							if(closeBtn != null){
								if(listoverlay != null && listoverlay.isShowing())
									listoverlay.dismiss();
							}
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
	
	
	
	
	private void initVideoconUi(){
		if(mTvRemoteInstance != null){
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					if(mContainer != null){

						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);

						/**
						 * Videocon IR structures
						 */
						//	private static int irhandler = 0;
						ImageButton videocon_redBtn = null;
						ImageButton videocon_greenBtn = null;
						ImageButton videocon_yellowBtn = null;
						ImageButton videocon_blueBtn = null;
						ImageButton videocon_offBtn = null;
						

						Button videocon_q = null;

						Button dropdownButton = null;
						TextView dropdownText = null;

						Button videocon_upArrowBtn = null;
						Button videocon_downArrowBtn = null;
						Button videocon_leftArrowBtn = null;
						Button videocon_rightArrowBtn = null;
						Button videocon_center_select_Btn = null;

						Button videocon_x = null;

						Button videocon_astriBtn = null;
						Button videocon_hashBtn = null;
						
						Button videocon_oneBtn = null;
						Button videocon_twoBtn = null;
						Button videocon_threeBtn = null;

						Button videocon_fourBtn = null;
						Button videocon_fiveBtn = null;
						Button videocon_sixBtn = null;

						Button videocon_sevenBtn = null;
						Button videocon_eightBtn = null;
						Button videocon_nineBtn = null;
						Button videocon_zeroBtn = null;
						
						View videoconView = inflater.inflate(R.layout.videocon_remote, null);

						videocon_redBtn = (ImageButton) videoconView.findViewById(R.id.videocon_redBtn);
						videocon_greenBtn = (ImageButton) videoconView.findViewById(R.id.videocon_greenBtn);
						videocon_yellowBtn = (ImageButton) videoconView.findViewById(R.id.videocon_yellowBtn);
						videocon_blueBtn = (ImageButton) videoconView.findViewById(R.id.videocon_blueBtn);

						videocon_offBtn = (ImageButton) videoconView.findViewById(R.id.videocon_offBtn);

						videocon_upArrowBtn = (Button) videoconView.findViewById(R.id.videocon_upArrowBtn);
						videocon_downArrowBtn = (Button) videoconView.findViewById(R.id.videocon_downArrowBtn);
						videocon_rightArrowBtn= (Button) videoconView.findViewById(R.id.videocon_rightArrowBtn);
						videocon_leftArrowBtn = (Button) videoconView.findViewById(R.id.videocon_leftArrowBtn);
						videocon_center_select_Btn = (Button) videoconView.findViewById(R.id.videocon_center_select_Btn);

						videocon_x = (Button) videoconView.findViewById(R.id.videocon_x);
						videocon_q = (Button) videoconView.findViewById(R.id.videocon_quemark);
						
						videocon_astriBtn = (Button) videoconView.findViewById(R.id.videocon_astri);
						videocon_hashBtn = (Button) videoconView.findViewById(R.id.videocon_hash);

						videocon_oneBtn = (Button) videoconView.findViewById(R.id.videocon_oneBtn);
						videocon_twoBtn= (Button) videoconView.findViewById(R.id.videocon_twoBtn);
						videocon_threeBtn = (Button) videoconView.findViewById(R.id.videocon_threeBtn);
						videocon_fourBtn= (Button) videoconView.findViewById(R.id.videocon_fourBtn);
						videocon_fiveBtn = (Button) videoconView.findViewById(R.id.videocon_fiveBtn);
						videocon_sixBtn = (Button) videoconView.findViewById(R.id.videocon_sixBtn);
						videocon_sevenBtn = (Button) videoconView.findViewById(R.id.videocon_sevenBtn);
						videocon_eightBtn = (Button) videoconView.findViewById(R.id.videocon_eightBtn);
						videocon_nineBtn= (Button) videoconView.findViewById(R.id.videocon_nineBtn);
						videocon_zeroBtn = (Button) videoconView.findViewById(R.id.videocon_zeroBtn);

						dropdownButton = (Button) videoconView.findViewById(R.id.dropdownButton);
						dropdownText = (TextView) videoconView.findViewById(R.id.dropdownText);

						if(dropdownButton != null){
							dropdownButton.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForVideocon("HD");
								}
							});
						}

						if(dropdownText != null){
							dropdownText.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Constant.DEBUG) System.out.println("DropDown button Clicked");
									showExtraButtonsForVideocon("SD");
								}
							});
						}

						if(videocon_q != null){
							videocon_q.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","qmark"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_offBtn != null){
							videocon_offBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","power"),DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_astriBtn != null){
							videocon_astriBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","asterisk"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_hashBtn != null){
							videocon_hashBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","hash"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_redBtn != null){
							videocon_redBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","red"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_greenBtn != null){
							videocon_greenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","green"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_yellowBtn != null){
							videocon_yellowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","yellow"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_blueBtn != null){
							videocon_blueBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","blue"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(videocon_upArrowBtn != null){
							videocon_upArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","forwardend"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_downArrowBtn != null){
							videocon_downArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","rewindend"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_leftArrowBtn != null){
							videocon_leftArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","rewind"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_rightArrowBtn != null){
							videocon_rightArrowBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","forward"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_center_select_Btn != null){
							if (Constant.DEBUG)	Log.d(TAG, "Videocon Press Ok");
							videocon_center_select_Btn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","select"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(videocon_x!= null){
							videocon_x.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","keyx"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

						if(videocon_oneBtn != null){
							videocon_oneBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","1") ,DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_twoBtn != null){
							videocon_twoBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","2"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_threeBtn != null){
							videocon_threeBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","3"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_fourBtn != null){
							videocon_fourBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","4"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_fiveBtn != null){
							videocon_fiveBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","5"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_sixBtn != null){
							videocon_sixBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","6"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_sevenBtn != null){
							videocon_sevenBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","7"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_eightBtn != null){
							videocon_eightBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","8"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_nineBtn != null){
							videocon_nineBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","9"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}
						if(videocon_zeroBtn != null){
							videocon_zeroBtn.setOnClickListener(new OperatorButtonClickListener(OperatorKeys.getKey("videocon","0"), DataStorage.getIrhandler(),DataStorage.getConnectedVendor()));
						}

//								VIDEOCON_VOLUMEUP = 15,
//								VIDEOCON_VOLUMEDOWN = 16,
//								VIDEOCON_CHANNELUP = 17,
//								VIDEOCON_CHANNELDOWN = 18,

						if(videoconView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(videoconView);
						}
					}
				}
			});
		}
	}
	
	
	private void showExtraButtonsForVideocon(String string) {
		try {
			if(Constant.DEBUG) System.out.println(" showExtraButtonsForVideocon ");
			LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
			final ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map=null;
			for(int i=0;i< ScreenStyles.VIDEOCON_EXTRAKEYS_LIST.length;i++){
				map = new HashMap<String, String>();
				map.put(ScreenStyles.LIST_KEY_TITLE,ScreenStyles.VIDEOCON_EXTRAKEYS_LIST[i]);
				map.put("id", i+""); 
				filterList.add(map);
			}
			mTvRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mTvRemoteInstance.getLayoutInflater();
					final Dialog listoverlay = new Dialog(mTvRemoteInstance);
					TextView titleView;
					View listOverlay = inflater.inflate(R.layout.tataskyoverlay_list,null);
					listoverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
					listoverlay.setContentView(listOverlay);
					titleView = (TextView) listoverlay.findViewById(R.id.tataskyoverlayTitle);
					if(titleView != null){
						titleView.setText("Sevice Buttons");
					}
					if(Constant.DEBUG) System.out.println("List Size "+filterList.size());
		
					if(listoverlay != null){
		//				listoverlay.setOnKeyListener(TVRemote.this);
						listoverlay.show();
						if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay >>>>>>>");
						RelativeLayout ovelaylistlayer = (RelativeLayout) listoverlay.findViewById(R.id.tataskyoverlaylistlayer);
						ListView listView = new ListView(mTvRemoteInstance.getApplicationContext());
						if(filterList != null && filterList.size() > 0 ){
							if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 1>>>>>>>");
							if(ovelaylistlayer != null){
								if(Constant.DEBUG) System.out.println("<<<<<<< Showing Overlay 2>>>>>>>");
								listView.setCacheColorHint(Color.TRANSPARENT);
								ListViewAdapter adapter = new ListViewAdapter(mTvRemoteInstance, filterList, 0, "", "", -1);
								listView.setAdapter(adapter);
								if(listView != null){
									ovelaylistlayer.addView(listView);
									listView.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
											HashMap<String, String> getinglist = filterList.get(arg2);
											String name = getinglist.get(ScreenStyles.LIST_KEY_TITLE);
											String[] VIDEOCON_EXTRAKEYS_LIST = {"Favourites","Active","Radio","VOD","Mosaic","Menu","Prev Channel"};
											String id = getinglist.get("id");
											if(name != null && !name.equalsIgnoreCase("")){
												if(name.equalsIgnoreCase(VIDEOCON_EXTRAKEYS_LIST[0])){
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[0]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[0]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "lusym"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[0]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(VIDEOCON_EXTRAKEYS_LIST[1])){
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[1]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[1]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "active"),DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[1]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(VIDEOCON_EXTRAKEYS_LIST[2])){
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[2]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[2]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(), "radio"),DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[2]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(VIDEOCON_EXTRAKEYS_LIST[3])){
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[3]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[3]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"vod"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[3]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(VIDEOCON_EXTRAKEYS_LIST[4])){
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[4]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[4]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"mosaic"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[4]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(VIDEOCON_EXTRAKEYS_LIST[5])){
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[5]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[5]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"keydot"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[5]+" Aftrer calling IR");
												}
												if(name.equalsIgnoreCase(VIDEOCON_EXTRAKEYS_LIST[6])){
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[6]+" Clicked");
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[6]+" Before calling IR");
													IRTransmitter.IRInput(OperatorKeys.getKey(DataStorage.getConnectedVendor(),"swap"), DataStorage.getIrhandler(), DataStorage.getConnectedVendor());
													if(Constant.DEBUG)  Log.d(TAG, VIDEOCON_EXTRAKEYS_LIST[6]+" Aftrer calling IR");
												}
											}
		
											try{
												if(listoverlay != null && listoverlay.isShowing())
													listoverlay.dismiss();
											}catch (Exception e) {
												e.printStackTrace();
												StringWriter errors = new StringWriter();
												e.printStackTrace(new PrintWriter(errors));
												SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
											}
										}
									});
								}
							}
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
		
							if(closeBtn != null){
								if(listoverlay != null && listoverlay.isShowing())
									listoverlay.dismiss();
							}
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
//	public void showMenuItems() { 
//		if(Constant.DEBUG)  Log.d(TAG , "showMenuItems() ");
//		getContextMenu();
//	}
//	
//	private void getContextMenu(){
//		try {
//			if(mMenu != null){
//				mMenu.setMenuItems(null);
//				mMenu.setItemsPerLineInPortraitOrientation(3);
//			}
//			customMenuValue = null;
//			customMenuIcons = null;
//			customMenuValue = ScreenStyles.REMOTE_MENU_ITEMS ;
//			customMenuIcons = ScreenStyles.REMOTE_MENU_ICONS;
//			getMenuItemsArray(customMenuValue, customMenuIcons);
//		}catch (Exception e) {
//			e.printStackTrace();
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
//		}
//	}
//			
//		
//	private void getMenuItemsArray(String[] customMenuValue,int[] customMenuIcons){
//		if(Constant.DEBUG)  Log.d(TAG , "getMenuItemsArray() ");
//		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
//		if(customMenuValue != null && customMenuValue.length >0 && customMenuIcons != null && customMenuIcons.length >0){
//			for(int i=0;i<customMenuValue.length;i++){
//				CustomMenuItem cmi = new CustomMenuItem();
//				cmi.setCaption(customMenuValue[i]);
//				cmi.setImageResourceId(customMenuIcons[i]);
//				cmi.setId(i);
//				menuItems.add(cmi);
//				if(Constant.DEBUG)  Log.d(TAG , "getMenuItemsArray().isShowing(): "+mMenu.isShowing());
//				if (!mMenu.isShowing() && menuItems.size() > 0){
//					try {
//						mMenu.setMenuItems(menuItems);
//					} catch (Exception ex) {
//						try {
//							HelpText.showHelpTextDialog(mTvRemoteInstance, mTvRemoteInstance.getResources().getString(R.string.SOMTHING_WRONG), 3000);
//						}catch (Exception e) {
//							e.printStackTrace();
//							StringWriter errors = new StringWriter();
//							e.printStackTrace(new PrintWriter(errors));
//							SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	public void processMenuActions(CustomMenuItem key) {
//		if(Constant.DEBUG)  Log.d(TAG , "processMenuActions() ");
//		if(key.getCaption().toString().trim().equalsIgnoreCase("TV Guide")){
//			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
//			HashMap<String, String> list = new HashMap<String, String>();
//			list.put("consumer", "TV");
//			list.put("network",mDataAccess.getConnectionType());
//			list.put("caller", "com.player.apps.TVRemote");
//			list.put("called", "startActivity");
//			dispatchHashMap.add(list);
//			new AsyncDispatch("com.port.apps.epg.Play.stopLiveTV", dispatchHashMap,false).execute();
//			
//			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
//			return;
//		}
//	}
	
	
/************************************************************************************************/
	
	public final BroadcastReceiver mTVRemoteReceiver = new BroadcastReceiver() {
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
					new OperatorKeys(context);
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
			if (handler.equalsIgnoreCase("com.port.apps.epg.Play.lockStatus")){
				if(mTvRemoteInstance != null){
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
							HelpText.showHelpTextDialog(mTvRemoteInstance, mTvRemoteInstance.getResources().getString(R.string.LOCK_MESSAGE), 2000);
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
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			HelpTip.close(mTvRemoteInstance);
			IRTransmitter.stopIr();
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			if (ActivityName.equalsIgnoreCase("Navigator") || ActivityName.equalsIgnoreCase("Guide") || ActivityName.equalsIgnoreCase("Search")) {
				mContainer.removeAllViews();			
				finish();
				return true;
			} else {
				Intent lukup = new Intent(TVRemote.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "TVRemote");
				startActivity(lukup);
				finish();
				return true;
			}
			
			
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				HelpTip.close(mTvRemoteInstance);
				Intent lukup = new Intent(TVRemote.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "TVRemote");
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
	
	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.DEBUG)	Log.d(TAG, "onResume()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mTVRemoteReceiver != null){
			mTvRemoteInstance.unregisterReceiver(mTVRemoteReceiver);
		}
	}	
	
	@Override
	public void finish() {
		super.finish();
	}
	
	

}
