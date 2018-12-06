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
import android.widget.Button;

import com.player.Layout;
import com.player.R;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;

public class GamePad extends Layout {
	private GamePad mGamePad;
	private String TAG = "GamePad";
	
	private Button upArrow;
	private Button downArrow;
	private Button leftArrow;
	private Button rightArrow;
	
	private Button leftButton;
	private Button rightButton;
	
	private Button xButton;
	private Button yButton;
	private Button aButton;
	private Button bButton;
	
	private Button start;
	private Button select;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGamePad = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,".CurrentScreen: "+DataStorage.getCurrentScreen());
		
//		IntentFilter game = new IntentFilter("com.player.apps.GamePad");
//		registerReceiver(mGameReceiver,game);
		requestForGamepad();
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
//		if(mGameReceiver != null){
//			mGamePad.unregisterReceiver(mGameReceiver);
//		}
	}
		
	private void requestForGamepad(){
		if(Constant.DEBUG)  Log.d(TAG , "requestForGamepad() ");
		try {
			if(mGamePad != null){
				mGamePad.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							mContainer.removeAllViews();
							DataStorage.setCurrentScreen(ScreenStyles.INITIAL_SCREEN);
							LayoutInflater  inflater = mGamePad.getLayoutInflater();
							View pad = inflater.inflate(R.layout.gamepad,null);
							upArrow = (Button) pad.findViewById(R.id.gp_up);
							downArrow = (Button) pad.findViewById(R.id.gp_down);
							leftArrow = (Button) pad.findViewById(R.id.gp_left);
							rightArrow = (Button) pad.findViewById(R.id.gp_right);
							
							xButton = (Button) pad.findViewById(R.id.joystick_x);
							yButton = (Button) pad.findViewById(R.id.joystick_y);
							aButton = (Button) pad.findViewById(R.id.joystick_a);
							bButton = (Button) pad.findViewById(R.id.joystick_b);
							
							leftButton = (Button) pad.findViewById(R.id.left_option);
							rightButton = (Button) pad.findViewById(R.id.right_option);
							
							start = (Button) pad.findViewById(R.id.start_option);
							select = (Button) pad.findViewById(R.id.select_option);
							
							mContainer.addView(pad);
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
	
	
	public void OnUpArrow(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnUpArrow:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","DPAD_UP");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	public void OnDownArrow(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnDownArrow:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","DPAD_DOWN");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	public void OnLeftArrow(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnLeftArrow:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","DPAD_LEFT");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	public void OnRightArrow(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnRightArrow:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","DPAD_RIGHT");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnOptionA(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnOptionA:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_A");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	public void OnOptionB(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnOptionB:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_B");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	public void OnOptionX(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnOptionX:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_X");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	public void OnOptionY(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnOptionY:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_Y");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnStart(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnStart:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_THUMBL");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	public void OnSelect(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnSelect:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_THUMBR");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		}catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnLeft(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnLeft:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_L1");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnRight(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnRight:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","BUTTON_R1");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnBreak(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnBreak:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","AXIS_BRAKE");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		} catch(Exception e){
	    	e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
	    }
	}
	
	public void OnThrottle(View V){
		if(Constant.DEBUG)  Log.d(TAG ,"OnThrottle:");
		try {
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
    		list.put("key","AXIS_THROTTLE");
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.GamePad");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			new AsyncDispatch("com.port.api.interactive.iService.gamepad", dispatchHashMap,false).execute();
		}catch(Exception e){
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
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + mDataAccess.getIsRemoteConnected());
			if (mMenu.isShowing()){
				hideMenu();
			}else if(DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN)){
				mContainer.removeAllViews();
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(GamePad.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "GamePad");
				startActivity(lukup);
				finish();
				return true;
			}
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(GamePad.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Search");
				startActivity(lukup);
				finish();
				return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_MENU ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + mDataAccess.getIsRemoteConnected());
			showCustomMenu();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
}
