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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.widget.OverLay;

public class Keypad extends Layout implements OnGestureListener{
	
	private String TAG = "Keypad";
	private Keypad mKeypadInstance;
	private float x_Value = 0;
	private float y_Value = 0;
	private GestureDetector gestureScanner;
	private String handler;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);       
        mKeypadInstance = this;
        Bundle br = this.getIntent().getExtras();
		if (br != null) {
			if (br.containsKey("Handler")) {
				handler = br.getString("Handler");
				handler = handler.trim().substring(0, handler.lastIndexOf("."));
			}
		}
        showKeypad(mContainer.getRootView());
        gestureScanner = new GestureDetector(this);
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,".CurrentScreen: "+DataStorage.getCurrentScreen());
		DataStorage.setCurrentScreen(ScreenStyles.HOME_SELECTED_TYPE);
		
		gestureScanner.setOnDoubleTapListener(new OnDoubleTapListener() {
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				if(Constant.DEBUG)  Log.d(TAG ,"onDoubleTap");
				return false;
			}
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if(Constant.DEBUG)  Log.d(TAG ,"onSingleTapConfirmed");
//				x_Value = coordinates("x", e.getX());
//	        	y_Value = coordinates("y", e.getY());
	            if(Constant.DEBUG)  Log.d(TAG ,"onDoubleTap().x_Value "+e.getX() +", y_Value"+e.getY());
	            dispatchHashMap  = new ArrayList<HashMap<String,String>>();
	    		HashMap<String, String> list = new HashMap<String, String>();
	    		list.put("x",x_Value+"");
	    		list.put("y",y_Value+"");
	        	list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Keypad");
				list.put("called", "startService");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.api.interactive.iService.SingleTap", dispatchHashMap,false).execute();
				return false;
			}
			
			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				if(Constant.DEBUG)  Log.d(TAG ,"onDoubleTapEvent");
				return false;
			}
		});
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
		if(Constant.DEBUG)  Log.d(TAG ,"onDestroy()");
	}
	
	private float coordinates(String axis,float value){
		// get screen size
		int width = 0;
		int height = 0;
        DisplayMetrics metrics = new DisplayMetrics();
		try {
			WindowManager winMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE) ;
	       	winMgr.getDefaultDisplay().getMetrics(metrics);
	       	width = winMgr.getDefaultDisplay().getWidth();
	       	height = winMgr.getDefaultDisplay().getHeight();
		}
		catch (Exception e) { 
			e.printStackTrace();
		} 
		
		if(axis.equalsIgnoreCase("x")) {
			value = value/width;
		}else if(axis.equalsIgnoreCase("y")) {
			value = value/height;
		}
		if(Constant.DEBUG)  Log.d(TAG ,"coordinate().value "+value);
		return value;
	}

	private void showKeypad(View view){
		if(Constant.DEBUG)  Log.d(TAG ,"showKeypad()"+view.requestFocus());
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" +Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
	        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput
	                (InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
	        closeSoftKeypad();
	        return super.onKeyUp(keyCode, event);
	    }
	    
		if (keyCode == KeyEvent.KEYCODE_MENU ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" +Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			if (DataStorage.getCurrentScreen() != null && !DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.INITIAL_SCREEN)) {
//				showCustomMenu();
			}
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				Intent lukup = new Intent(Keypad.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Keypad");
				startActivity(lukup);
				finish();
				return true;
		}
		if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
			if (Constant.DEBUG) Log.d("KEY", "KEYCODE_PAGE_UP");
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
			if (Constant.DEBUG) Log.d("KEY", "KEYCODE_PAGE_DOWN");
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent event){
		try{
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("keycode", keycode+"");
			if (event.isShiftPressed()) {
				list.put("modifier", event.isShiftPressed()+"");
			} else {
				list.put("modifier", "");
			}
			list.put("called", "startService");
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			dispatchHashMap.add(list);
			String method = "com.port.api.interactive.iService.keyboard";
			new AsyncDispatch(method, dispatchHashMap, false).execute();
		}catch(Exception e){
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			return false;
		}
		return true;
	}
	

/************************************************************************************************/
	
	private void closeSoftKeypad() {
		if(Constant.DEBUG)  Log.d(TAG ,"closeSoftKeypad()");
		if(mKeypadInstance != null){
			mKeypadInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mContainer != null){
						OverLay overlay = new OverLay(mKeypadInstance);
						String message = mKeypadInstance.getResources().getString(R.string.REMOVE_SOFTKEYBOARD);
						final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, "Alert", message, null,null,false);
						if(dialog != null){
							Button okBtn = (Button) dialog.findViewById(R.id.okButton);
							if(okBtn != null){
								okBtn.setText("OK");
								okBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										dispatchHashMap  = new ArrayList<HashMap<String,String>>();
										HashMap<String, String> list = new HashMap<String, String>();
										list.put("called", "messageActivity");
										list.put("consumer", "TV");
										list.put("network",mDataAccess.getConnectionType());
										dispatchHashMap.add(list);
										String method = "com.port.apps.epg.Social.close";
										new AsyncDispatch(method, dispatchHashMap, false).execute();
										
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.toggleSoftInput (InputMethodManager.SHOW_FORCED,0);
										
										if(dialog != null && dialog.isShowing()){
											dialog.dismiss();
											finish();
										}
										
									}
								});
							}
							Button cancelBtn = (Button) dialog.findViewById(R.id.overlayCancelButton);
							if(cancelBtn != null){
								cancelBtn.setVisibility(View.VISIBLE);
								cancelBtn.setText("CANCEL");
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

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		x_Value = coordinates("x", e2.getX());
    	y_Value = coordinates("y", e2.getY());
        if(Constant.DEBUG)  Log.d(TAG ,"onFling().x_Value "+e2.getX() +", y_Value"+e2.getY());
        dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("x",x_Value+"");
		list.put("y",y_Value+"");
    	list.put("consumer", "TV");
		list.put("network",mDataAccess.getConnectionType());
		list.put("caller", "com.player.apps.Keypad");
		list.put("called", "messageActivity");
		dispatchHashMap.add(list);
		new AsyncDispatch(handler+".Mouse", dispatchHashMap,false).execute();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
}
