package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.player.Layout;
import com.player.R;
import com.player.Layout.AsyncDispatch;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;

public class DVBRemote extends Layout{
	private DVBRemote mDVBRemoteInstance;
	private String TAG = "DVBRemote";
	private String ActivityName = ""; 
	private static StringBuffer channelNumber;
	private static int prevChlNumber;
	private int DELAY = 3000;
	private boolean trigger = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(Constant.DEBUG) System.out.println("DVBRemote showed");
		mDVBRemoteInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
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
		}
		channelNumber = new StringBuffer();
		showDVBRemote();
		trigger = true;
	}
	
	private void showDVBRemote() {
		if (mDVBRemoteInstance !=null) {
			mDVBRemoteInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LayoutInflater inflater = mDVBRemoteInstance.getLayoutInflater();
					if (mContainer != null) {
						DataStorage.setCurrentScreen(ScreenStyles.REMOTE_OPERATOR_UI);
						
						Button dvb_oneBtn = null;
						Button dvb_twoBtn = null;
						Button dvb_threeBtn = null;

						Button dvb_fourBtn = null;
						Button dvb_fiveBtn = null;
						Button dvb_sixBtn = null;

						Button dvb_sevenBtn = null;
						Button dvb_eightBtn = null;
						Button dvb_nineBtn = null;
						
						Button dvb_plusBtn = null;
						Button dvb_zeroBtn = null;
						Button dvb_minusBtn = null;
						
						View dvbView = inflater.inflate(R.layout.dvb_remote, null);

						dvb_plusBtn = (Button) dvbView.findViewById(R.id.dvb_plusBtn);
						dvb_minusBtn = (Button) dvbView.findViewById(R.id.dvb_minusBtn);
						
						dvb_oneBtn = (Button) dvbView.findViewById(R.id.dvb_oneBtn);
						dvb_twoBtn= (Button) dvbView.findViewById(R.id.dvb_twoBtn);
						dvb_threeBtn = (Button) dvbView.findViewById(R.id.dvb_threeBtn);
						dvb_fourBtn= (Button) dvbView.findViewById(R.id.dvb_fourBtn);
						dvb_fiveBtn = (Button) dvbView.findViewById(R.id.dvb_fiveBtn);
						dvb_sixBtn = (Button) dvbView.findViewById(R.id.dvb_sixBtn);
						dvb_sevenBtn = (Button) dvbView.findViewById(R.id.dvb_sevenBtn);
						dvb_eightBtn = (Button) dvbView.findViewById(R.id.dvb_eightBtn);
						dvb_nineBtn= (Button) dvbView.findViewById(R.id.dvb_nineBtn);
						dvb_zeroBtn = (Button) dvbView.findViewById(R.id.dvb_zeroBtn);
						
						dvb_oneBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("1");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_twoBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("2");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_threeBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("3");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_fourBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("4");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_fiveBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("5");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_sixBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("6");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_sevenBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("7");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_eightBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("8");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_nineBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("9");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_zeroBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								channelNumber = channelNumber.append("0");
								if(Constant.DEBUG)  Log.d(TAG ,"trigger: "+trigger);
								if (trigger) {
									lcnSender(true);
									trigger = false;
								}
							}
						});
						
						dvb_plusBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(Constant.DEBUG)  Log.d(TAG ,"prevChlNumber: "+prevChlNumber);
								prevChlNumber = prevChlNumber+1;
								lcnChanged(prevChlNumber);
							}
						});
						
						dvb_minusBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(Constant.DEBUG)  Log.d(TAG ,"prevChlNumber: "+prevChlNumber);
								prevChlNumber = prevChlNumber-1;
								lcnChanged(prevChlNumber);
							}
						});
						
						if(dvbView != null){
							if(Constant.DEBUG) System.out.println("Remote View Added");
							mContainer.removeAllViews();
							mContainer.addView(dvbView);
						}
					}
				}
			});
		}
	}
	
	private void lcnSender(boolean value){
		try{
			if(value){
				Handler handler = new Handler();
			    handler.postDelayed(new Runnable() {            
			        @Override
			        public void run() {
			        	dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			        	String lcn = "";
						HashMap<String, String> list = new HashMap<String, String>();
						if (channelNumber.toString().length() >= 4) {
							lcn = channelNumber.toString().substring(0,4);
						}else{
							lcn = channelNumber.toString();
						}
						list.put("serviceid", lcn);
						list.put("type", "live");
						list.put("activity", "DVBRemote");
						list.put("consumer", "TV");
						list.put("network",mDataAccess.getConnectionType());
						list.put("caller", "com.player.apps.DVBRemote");
						list.put("called", "startActivity");
						dispatchHashMap.add(list);
						if(Constant.DEBUG)  Log.d(TAG ,"dispatchHashMap: "+dispatchHashMap);
						new AsyncDispatch("com.port.apps.epg.Play.PlayOn", dispatchHashMap,true).execute();
						prevChlNumber = Integer.parseInt(lcn);
						channelNumber = new StringBuffer();
						trigger = true;
			        }
			    }, DELAY);
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	private void lcnChanged(int number){
		if(Constant.DEBUG)  Log.d(TAG ,"lcn number: "+number);
		try{
			if (number<0) {
				prevChlNumber = 0;
				number = 0;
			}
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("serviceid", number+"");
			list.put("type", "live");
			list.put("activity", "DVBRemote");
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.DVBRemote");
			list.put("called", "startActivity");
			dispatchHashMap.add(list);
			if(Constant.DEBUG)  Log.d(TAG ,"dispatchHashMap: "+dispatchHashMap);
			new AsyncDispatch("com.port.apps.epg.Play.PlayOn", dispatchHashMap,true).execute(); 
			
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
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
			Intent lukup = new Intent(DVBRemote.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "DVBRemote");
			startActivity(lukup);
			finish();
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
	}
	
	
	@Override
	public void finish() {
		super.finish();
	}
}
