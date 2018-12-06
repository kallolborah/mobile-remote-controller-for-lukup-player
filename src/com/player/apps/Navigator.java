package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.layout;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.Layout.AsyncDispatch;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.action.OverlayCancelListener;
import com.player.action.Subscribe;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.HelpText;
import com.player.widget.OverLay;

public class Navigator extends Layout {
	
	private GestureDetectorCompat mDetector; 
	private String TAG = "Navigation";
	protected Navigator navigator;
	private boolean home=true;
	public int swipe_Min_Velocity = 100;
	private int swipe_Min_XDistance = 100;
	private int swipe_Min_YDistance = 70;
	protected String direction ="";
	private String state ="Featured";
	private String selectedUrl = "";
	private String selectedServiceId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = this;
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        
        startNavigate();
        
    	if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
		Bundle br = this.getIntent().getExtras();
		if (br != null) {
			if (br.containsKey("ActivityName")) {
				br.getString("ActivityName"); 
			}
			if (br.containsKey("Handler")) {
				String handler = br.getString("Handler");
				if(handler.equalsIgnoreCase("com.port.api.interactive.iView.start")){
					home = false;
				}
			}
		}
		
		HelpText.showHelpTextDialog(navigator, navigator.getResources().getString(R.string.NAVIGATOR_TOAST), 3000);
    }
    
    private void startNavigate(){
    	mDetector = new GestureDetectorCompat(this, new MyGestureListener()); 
    	DataStorage.setCurrentScreen(ScreenStyles.NAVIGATION);
    	//HelpTip
		HelpTip.requestForHelp(navigator.getResources().getString(R.string.CONTROLLING_YOUR_TV),
				navigator.getResources().getString(R.string.NAVIGATOR_MSG1),navigator);
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    
    @Override
	protected void onStart() {
		super.onStart();	
		
		IntentFilter setup = new IntentFilter("com.player.apps.Navigator");
		registerReceiver(mNavigatorReceiver,setup); 
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
		unregisterReceiver(mNavigatorReceiver); 
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
	}
	
	/***********************************GESTURE HANDLING ************************************************/

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		HelpTip.close(navigator);
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "on Back :" + state);
			
			if(DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_INFO_SCREEN)){
				mContainer.removeAllViews();
				startNavigate();
				return true;
			}else if(DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.NAVIGATION)){
				mContainer.removeAllViews();
				Intent intent = new Intent(Navigator.this, Screens.class);
				intent.putExtra("ActivityName", "Navigator");
				startActivity(intent);
				finish();
				return true;
			}else {
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
				
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
	    		HashMap<String, String> list = new HashMap<String, String>();
	        	list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Navigator");
				if(home && !DataStorage.isRunningStatus()){ //we are seeing home screen on TV
					list.put("called", "messageActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Home.Back", dispatchHashMap,true).execute();	
				}else if(!home && DataStorage.isRunningStatus()){  //we are seeing an interactive on TV
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Play.Back", dispatchHashMap,false).execute();
				}else if(home && DataStorage.isRunningStatus()){ //we are seeing a video on TV
					Intent intent = new Intent(Navigator.this, AppGuide.class);
					intent.putExtra("ActivityName", "Navigator");
					startActivity(intent);
					finish(); //go back to AppGuide
				}
			}
			return true;
			
		}if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
			if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());			
			Intent lukup = new Intent(Navigator.this, com.player.apps.AppGuide.class);
			lukup.putExtra("ActivityName", "Navigator");
			startActivity(lukup);
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";         
        
        //must do by default
        @Override
        public boolean onDown(MotionEvent event) { 
        	if(Constant.DEBUG)  Log.d(DEBUG_TAG,"onDown: " + event.toString()); 
            return true;
        }

        
        //direction of swipe
        @Override
        public boolean onFling(MotionEvent startPoint, MotionEvent endPoint,float velocityX, float velocityY) {
        	if(Constant.DEBUG)  Log.d(DEBUG_TAG, "onFling: " + startPoint.toString()+ endPoint.toString());
            
            if(startPoint.getPointerCount() == 2){
				System.out.println("Second finger Touched");
			}
			if(startPoint != null && endPoint != null){
				
				float xDis = Math.abs(startPoint.getX() - endPoint.getX());
				float yDis = Math.abs(startPoint.getY() - endPoint.getY());
				velocityX = Math.abs(velocityX);
				velocityY = Math.abs(velocityY);				
				
				if(velocityX > (swipe_Min_Velocity) && xDis > (swipe_Min_XDistance)){
					if(startPoint.getX() > endPoint.getX()){
						// right to left
						direction = "LEFT";
					} else {
						direction = "RIGHT";
					}					
				}
				else if(velocityY > swipe_Min_Velocity && yDis > swipe_Min_YDistance){
					if(startPoint.getY() > endPoint.getY()){
						// bottom to up
						direction = "UP";
					} else {
						direction = "DOWN";
					}						
					
				}
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
				
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
	    		HashMap<String, String> list = new HashMap<String, String>();
	        	list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.apps.Navigator");
				list.put("direction",direction);
				if(home && !DataStorage.isRunningStatus()){
					list.put("called", "messageActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Home.Fling", dispatchHashMap,false).execute();
				}else if(!home && DataStorage.isRunningStatus()){
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatch("com.port.apps.epg.Play.Fling", dispatchHashMap,false).execute();
				}else if(home && DataStorage.isRunningStatus()){
					Intent intent = new Intent(Navigator.this, AppGuide.class);
					intent.putExtra("ActivityName", "Navigator");
					startActivity(intent);
					finish();
				}
				
			}
            return true;
        }
        
        //for selection
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e){
        	if (Constant.DEBUG) Log.d(TAG, "onSingleTapConfirmed: " + e.toString());
        	dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Navigator");
			
			if(home && !DataStorage.isRunningStatus()){
				list.put("called", "messageActivity");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.apps.epg.Home.Select", dispatchHashMap,false).execute();
			}else if(!home && DataStorage.isRunningStatus()){
				list.put("called", "startActivity");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.apps.epg.Play.Select", dispatchHashMap,false).execute();
			}else if(home && DataStorage.isRunningStatus()){
				Intent intent = new Intent(Navigator.this, AppGuide.class);
				intent.putExtra("ActivityName", "Navigator");
				startActivity(intent);
				finish();
			}
        	return true;
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent e){
        	if (Constant.DEBUG) Log.d(TAG, "onDoubleTap: " + e.toString());
        	dispatchHashMap  = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> list = new HashMap<String, String>();
        	list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Navigator");
			
			if(home && !DataStorage.isRunningStatus()){
				list.put("called", "messageActivity");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.apps.epg.Home.Select", dispatchHashMap,false).execute();
			}else if(!home && DataStorage.isRunningStatus()){
				list.put("called", "startActivity");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.apps.epg.Play.Select", dispatchHashMap,false).execute();
			}else if(home && DataStorage.isRunningStatus()){
				Intent intent = new Intent(Navigator.this, AppGuide.class);
				intent.putExtra("ActivityName", "Navigator");
				startActivity(intent);				
				finish();
			}
        	return true;
        }
    };
    
    
    private void showPackageInfo(final ArrayList<HashMap<String,String>> list,final String packageid,final String packageName,final String price){
		if(navigator != null){
			navigator.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						if(list.size() > 0){
							OverLay overlay = new OverLay(navigator);
							if(Constant.DEBUG)  Log.d(TAG , "packageid:  "+packageid+", packageName:  "+packageName);
							final Dialog listoverlay= overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_LIST, packageName, null, list,null,false);
							listoverlay.show(); 
							ListView ovelaylistItem = (ListView) listoverlay.findViewById(R.id.overlayList);
							ImageView closeBtn = (ImageView) listoverlay.findViewById(R.id.closeBtn);
							if(ovelaylistItem != null){
								ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) ovelaylistItem.getLayoutParams();
								if(customLayout ==null){
									customLayout = new CustomLayout(navigator);
								}
								params.height = customLayout.getConvertedHeight(200);
								ovelaylistItem.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
										HashMap<String, String> gettinglist = list.get(arg2);
										String name = "";
										String desc = "";
										if(gettinglist.containsKey(ScreenStyles.LIST_KEY_TITLE)){
											name = gettinglist.get(ScreenStyles.LIST_KEY_TITLE);
										}
										if(gettinglist.containsKey("desc")){
											desc = gettinglist.get("desc");
										}
										try{
											showSelectedItemInfo(packageid,packageName,price,desc);
										}catch (Exception e) {
											e.printStackTrace();
											StringWriter errors = new StringWriter();
											e.printStackTrace(new PrintWriter(errors));
											SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
										}
									}
								});
							}
							if(closeBtn != null){
								closeBtn.setOnClickListener(new OverlayCancelListener(listoverlay));
							}
						}else{
							HelpText.showHelpTextDialog(navigator, navigator.getResources().getString(R.string.NO_CONNECTED_DEVICE), 2000);
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
	}
    
    
    private void showSelectedItemInfo(final String packageid,final String name,final String price,final String desc){
    	if(navigator != null){
			navigator.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						LayoutInflater inflater = navigator.getLayoutInflater();
						final Dialog dialog = new Dialog(navigator,R.style.ThemeDialogCustom);
						View messageDialog = inflater.inflate(R.layout.overlay_message, null);
						TextView messageView = (TextView) messageDialog.findViewById(R.id.messageView);
						Button okBtn = (Button) messageDialog.findViewById(R.id.messageOkbtn);
						if(okBtn != null){
							okBtn.setText("SUBSCRIBE");
							okBtn.setVisibility(View.VISIBLE);
							okBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									if(dialog != null && dialog.isShowing()){
										Subscribe.requestForSubscriber(packageid, name, price, "package", "subscribe", "package", navigator, mContainer,"com.player.apps.Navigator");
										dialog.cancel();
									}
								}
							});
						}
						messageView.setText(desc);
						dialog.setContentView(messageDialog);
						dialog.show();
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			});
		}
	}
    
    /**********************************TO SHOW INFO**************************************************/
    private String jsondata = "";
    public final BroadcastReceiver mNavigatorReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "BroadcastReceiver ");
			Bundle extras = intent.getExtras();
//			String jsondata = "";
			String handler = "";
			JSONObject objData = null;
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
	};
	
	private void processUIData(String handler,final JSONObject jsonData){
		try{
		if(Layout.progressDialog.isShowing()){
			Layout.progressDialog.dismiss();
		}
		if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.  "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Home.showFeaturedInfo")){
				if(navigator != null){
					try {
						if(jsonData.has("type")){
							ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
							String type = Utils.getDataFromJSON(jsonData, "type");
							selectedUrl = Utils.getDataFromJSON(jsonData, "url");
							if(type.equalsIgnoreCase("event")){
								navigator.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Info.rInfoAction(jsonData, mContainer, navigator,"com.player.apps.Navigator");
									}
								});
							}else if(type.equalsIgnoreCase("package")){
								String packageId = Utils.getDataFromJSON(jsonData, "id");
								String packageName = Utils.getDataFromJSON(jsonData, "name");
								String price = Utils.getDataFromJSON(jsonData, "price");
								JSONArray jsonArray = Utils.getListFromJSON(jsonData, "packageList");
								if(jsonArray != null && jsonArray.length() >0){
									for(int i= 0;i<jsonArray.length();i++){
										HashMap<String,String> showMap = new HashMap<String, String>();
										JSONObject obj = jsonArray.getJSONObject(i);
										String mName = "";
										String mAddress = "";
										String mType = "";
										String mDesc = "";
										if(obj.has("name")){
											mName= obj.getString("name");
										}
										if(obj.has("id")){
											mAddress= obj.getString("id");
										}
										if(obj.has("type")){
											mType= obj.getString("type");
										}
										if(obj.has("desc")){
											mDesc= obj.getString("desc");
										}
										showMap.put(ScreenStyles.LIST_KEY_ID, mAddress);
										showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
										showMap.put("desc", mDesc);
										showMap.put("type", mType);
										list.add(showMap);
									}
									if(Constant.DEBUG)  Log.d(TAG , "packageId:  "+packageId+", packageName:  "+packageName);
									showPackageInfo(list,packageId,packageName,price);
								}else{
									
								}
							}else if(type.equalsIgnoreCase("service")){
								navigator.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Info.rInfoAction(jsonData, mContainer, navigator,"com.player.apps.Navigator");
									}
								});
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Home.getState")){
				//get state, if state = Home, then call finish()
				if(navigator != null){
					try {
						if(Constant.DEBUG)  Log.d(TAG ,"state: " +state);
						if(jsonData.has("state")){
							state = Utils.getDataFromJSON(jsonData, "state");	
						}
						if(state.equalsIgnoreCase("Home")){
							if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
							Intent lukup = new Intent(Navigator.this, com.player.apps.AppGuide.class);
							lukup.putExtra("ActivityName", "PlayList");
							startActivity(lukup);
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Home.showRecommendation")){
				if(navigator != null){
					try {
						String Subscribe = Utils.getDataFromJSON(jsonData, "subscribe");
						String Id = Utils.getDataFromJSON(jsonData, "id");
						selectedUrl = Utils.getDataFromJSON(jsonData, "url");
						String Price = Utils.getDataFromJSON(jsonData, "price");
						String Pricingmodel = Utils.getDataFromJSON(jsonData, "pricingmodel");
						selectedServiceId = Utils.getDataFromJSON(jsonData, "serviceid");
						String servicetype = Utils.getDataFromJSON(jsonData, "servicetype");
						if(Subscribe.equalsIgnoreCase("true") || MoneyConverter(Price) == 0){
							Intent play = new Intent(Navigator.this, PlayBack.class);
							play.putExtra("ActivityName", "Navigator");
							play.putExtra("serviceid", selectedServiceId);
							play.putExtra("Type", servicetype);
							play.putExtra("EventId", Id);
							if(selectedUrl != null){
								play.putExtra("EventUrl", selectedUrl);
							}
							startActivity(play);
							finish();
						}else{
							try {
								navigator.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Info.rInfoAction(jsonData, mContainer, navigator,"com.player.apps.Navigator");
									}
								});
							}catch (Exception e) {
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
			} else if(handler.equalsIgnoreCase("com.port.apps.epg.ProgramFragment.Select")){
				if(navigator != null){
					try {
						String Price = Utils.getDataFromJSON(jsonData, "price");
						String Pricingmodel = Utils.getDataFromJSON(jsonData, "pricingmodel");
						String Subscribe = Utils.getDataFromJSON(jsonData, "subscribe");
						String eventid = Utils.getDataFromJSON(jsonData, "id");
						selectedServiceId = Utils.getDataFromJSON(jsonData, "serviceid");
						String servicetype = Utils.getDataFromJSON(jsonData, "servicetype");
						selectedUrl = Utils.getDataFromJSON(jsonData, "url");
						if(!servicetype.equalsIgnoreCase("live")){
							if(Subscribe.equalsIgnoreCase("true") || MoneyConverter(Price) == 0){
								Intent play = new Intent(Navigator.this, PlayBack.class);
								play.putExtra("ActivityName", "Navigator");
								play.putExtra("EventId", eventid);
								play.putExtra("serviceid", selectedServiceId);
								play.putExtra("Type", servicetype);
								if(selectedUrl != null){
									play.putExtra("EventUrl", selectedUrl);
								}
								startActivity(play);
								finish();
							}else{
								try {
									navigator.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Info.rInfoAction(jsonData, mContainer, navigator,"com.player.apps.Navigator");
										}
									});
								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
						}else{
//							Intent play = new Intent(Navigator.this, PlayBack.class);
//							play.putExtra("ActivityName", "Navigator");
//							play.putExtra("EventId", eventid);
//							play.putExtra("serviceid", selectedServiceId);
//							play.putExtra("Type", servicetype);
//							startActivity(play);
//							finish();
							
							Intent play = new Intent(Navigator.this, TVRemote.class);
							play.putExtra("ActivityName", "Navigator");
							startActivity(play);
							
							dispatchHashMap  = new ArrayList<HashMap<String,String>>();
							HashMap<String, String> list = new HashMap<String, String>();
							list.put("type", servicetype);
							list.put("id", eventid);
							list.put("serviceid", selectedServiceId);
							list.put("consumer", "TV");
							list.put("network",mDataAccess.getConnectionType());
							list.put("caller", "com.player.apps.Guide");
							list.put("activity", "Navigator");
							if(Constant.DEBUG)  Log.d(TAG , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
							list.put("called", "startActivity");
							dispatchHashMap.add(list);
							new AsyncDispatch("com.port.apps.epg.Play.PlayOn", dispatchHashMap,true).execute();
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
				}
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.Subscriptions")){
				if(navigator != null){
					try{
						if(jsonData.has("result")){
							String result = Utils.getDataFromJSON(jsonData, "result");
							
							if(result.equalsIgnoreCase("success")){
								//close info
								//show dialog confirming subscription and asking if user wants to play
								mContainer.removeAllViews();
								startNavigate();
								if(jsonData.has("msg")){
									displayNotification(jsonData.getString("id"));
								}
							}else{
								if(jsonData.has("msg")){
									HelpText.showHelpTextDialog(navigator, jsonData.getString("msg"), 4000);
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
			}else if(handler.equalsIgnoreCase("com.port.apps.epg.Play.StopiView")){
				if(jsonData.has("state")){
					String state = Utils.getDataFromJSON(jsonData, "state");
					if(Utils.getDataFromJSON(jsonData, "interactive").equalsIgnoreCase("true")){
						DataStorage.setRunningStatus(false);
						DataStorage.setPlayingUrl("");
					}
					if(state.equalsIgnoreCase("stop")){
						if(Constant.DEBUG)  Log.d(TAG , "com.port.apps.epg.Play.StopiView Finish.");
						finish();
						home = true;
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
	
	private int MoneyConverter(String val){
		int cost = 0;
		if(!val.equalsIgnoreCase("")){
			cost = (int) Double.parseDouble(val);
		}
		return cost;
	}
	
	private void displayNotification(final String eventid){
		if(navigator != null){
			navigator.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					OverLay overlay = new OverLay(navigator);
					String message = navigator.getResources().getString(R.string.MESSAGE_FOR_REPLAY);
					final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, "Play", message, null,null,false);
					if(dialog != null){
						Button okBtn = (Button) dialog.findViewById(R.id.okButton);
						if(okBtn != null){
							okBtn.setText("YES");
							okBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									Intent play = new Intent(Navigator.this, PlayBack.class);
									play.putExtra("ActivityName", "Navigator");
									play.putExtra("EventId", eventid);
									play.putExtra("serviceid", selectedServiceId);
									play.putExtra("Type", "vod");
									if(selectedUrl != null){
										play.putExtra("EventUrl", selectedUrl);
									}
									startActivity(play);
									finish();
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
			});
		}
	}
    
}