package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.Layout.AsyncDispatch;
import com.player.action.HelpTip;
import com.player.action.Info;
import com.player.service.CacheDockData;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.OverLay;

public class Home extends Layout implements OnScrollListener,OnItemClickListener {
	
	private static Home mhomeInstance;	
	private JSONObject featuredData;
	private String ActivityName = "";
	private static String TAG = "Home";
	private String name;
	private String id;
	private static String mSelectedType;
	private ArrayList<HashMap<String,String>> featuredList = new ArrayList<HashMap<String,String>>();
	private static int index = 0;
	private boolean isCached = false;
	private boolean scrollFlag = false;
	public static boolean loggedIn = false;	
	private static String status = "invisible";
	private ListView featuredListView;
	private Button addBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mhomeInstance = this;
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		IntentFilter home = new IntentFilter("com.player.apps.Home");
		registerReceiver(mHomeReceiver,home);	

	if(DataStorage.getCurrentUserId().equalsIgnoreCase("")){
		DataStorage.setCurrentUserId(mDataAccess.getCurrentUserId());
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,"CurrentScreen: "+DataStorage.getCurrentScreen());
		Bundle br = this.getIntent().getExtras();
		if (br != null) {
			if (br.containsKey("ActivityName")) {
				
				ActivityName = br.getString("ActivityName"); 
				if(CacheDockData.fragmentFeaturedList.size()>0){
					isCached = true;
					showFeatured(isCached);
				}else{
					dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("mode", "home");
					list.put("start", index+"");
					list.put("limit", "10");
					list.put("consumer", "TV");
					list.put("network",mDataAccess.getConnectionType());
					list.put("caller", "com.player.apps.Home");
					list.put("called", "startService");
					dispatchHashMap.add(list);
					String method = "com.port.apps.epg.Guide.sendFeaturedList"; 
					new AsyncDispatch(method, dispatchHashMap,true).execute();
				}	
				
			}
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Constant.DEBUG)  Log.d(TAG,"onDestroy()");
		if(mHomeReceiver != null){
			mhomeInstance.unregisterReceiver(mHomeReceiver);
		}
	}	
	
		
	/**********************NAVIGATOR, FEATURED ******************************************/
	
	
	private void showFeatured(final boolean isCached){
		if(Constant.DEBUG)  Log.d(TAG , "HomeScreen() ");
		DataStorage.setCurrentScreen(ScreenStyles.HOMEFEATUREDSCREEN);
		mSelectedType = ScreenStyles.HOMEFEATUREDSCREEN;
		try {
			if(mhomeInstance != null ){
				if(!isCached){
					featuredList = getFeaturedItemList(featuredData, null, "featuredList");	
					CacheDockData.fragmentFeaturedList = featuredList;
				}else{
					featuredList = CacheDockData.fragmentFeaturedList;
				}
				
				if(mContainer != null){
					mhomeInstance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mContainer.removeAllViews();
							LayoutInflater  inflater = mhomeInstance.getLayoutInflater();
							View lukupOptionLayout = inflater.inflate(R.layout.home, null);	
							
							if(!loggedIn){
								switchLayout(); //show Guest and Switch profile options
							}else{
								//showNavigatorButton();
							}
							
							if(lukupOptionLayout != null){
								LinearLayout.LayoutParams standParams = null;
								if(standParams== null){
									standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								}
								mContainer.addView(lukupOptionLayout,standParams);
								featuredListView = (ListView) lukupOptionLayout.findViewById(R.id.listView);
								if(featuredList.size()>0){
									featuredListView.setAdapter(new FeaturedAdapter(featuredList));
								}
							}
							
							//HelpTip
							HelpTip.requestForHelp(mhomeInstance.getResources().getString(R.string.CHOOSING_YOUR_PROFILE),
									mhomeInstance.getResources().getString(R.string.HOME_MSG1),mhomeInstance);
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
	
	public static ArrayList<HashMap<String, String>> getFeaturedItemList(JSONObject jsonData ,String type,String arrayName) {
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		try{
			if(jsonData != null){
				if(jsonData.has(arrayName)){
					list = new ArrayList<HashMap<String,String>>();
					JSONArray servicelist = Utils.getListFromJSON(jsonData, arrayName);
					if(servicelist != null && servicelist.length() >0){
						for(int i=0;i<servicelist.length();i++){
							try {
								HashMap<String,String> showMap = new HashMap<String, String>();
								JSONObject obj = servicelist.getJSONObject(i);
								String id = "";
								String mImage= "";
								String mName = "";
								String releasedate = "";
								String actors = "";
								String msubscribe = "";
								String rating = "";
								String mPrice = "";
								String director = "";
								String genre = "";
								String desc = "";
								String production = "";
								String musicdirector = "";
								if(obj.has("releasedate")){
									releasedate= obj.getString("releasedate");
								}
								if(obj.has("actors")){
									actors= obj.getString("actors");
								}
								if(obj.has("rating")){
									rating= obj.getString("rating");
								}
								if(obj.has("genre")){
									genre= obj.getString("genre");
								}
								if(obj.has("director")){
									director= obj.getString("director");
								}
								if(obj.has("production")){
									production= obj.getString("production");
								}
								if(obj.has("musicdirector")){
									musicdirector= obj.getString("musicdirector");
								}
								if(obj.has("id")){
									id= obj.getString("id");
								}
								if(obj.has("image")){
									mImage= obj.getString("image");
								}
								if(obj.has("name")){
									mName= obj.getString("name");
								}
								if(obj.has("subscribe")){
									msubscribe= obj.getString("subscribe");
								}
								if(obj.has("price")){
									mPrice= obj.getString("price");
								}
								if(obj.has("desc")){
									desc= obj.getString("desc");
								}
								
								showMap.put(ScreenStyles.LIST_KEY_ID, id);
								showMap.put(ScreenStyles.LIST_KEY_THUMB_URL,mImage);
								showMap.put(ScreenStyles.LIST_KEY_TITLE, mName);
								showMap.put("subscribe", msubscribe);
								showMap.put("price", mPrice);
								showMap.put("desc", desc);
								showMap.put("releasedate", releasedate);
								showMap.put("actors", actors);
								showMap.put("rating", rating);
								showMap.put("genre", genre);
								showMap.put("director", director);
								showMap.put("production", production);
								showMap.put("musicdirector", musicdirector);
								list.add(showMap);
								
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
				if(Constant.DEBUG)  Log.d(TAG , "featured list size "+list.size());
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			return null;
		}
		return null;
	}
	
		
	private void switchLayout(){
		
		if(mhomeInstance != null){
			mhomeInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LinearLayout  btmLay = (LinearLayout) mhomeInstance.findViewById(R.id.bottomlayout);
					if(btmLay != null){
						btmLay.setVisibility(View.VISIBLE);
					}	
					
					Button guestBtn = (Button) mhomeInstance.findViewById(R.id.guestBtn);
					if(guestBtn != null){
						if(name != null){
							guestBtn.setText(name);
						}
						guestBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								LinearLayout  btmLay = (LinearLayout) mhomeInstance.findViewById(R.id.bottomlayout);
								if(btmLay != null){
									btmLay.setVisibility(View.GONE);
								}	
								DataStorage.setCurrentUserId("1000");
								loggedIn = true;
								Intent lukup = new Intent(Home.this, com.player.apps.AppGuide.class);
								lukup.putExtra("ActivityName", "Home");
								startActivity(lukup);
								finish();
							}
						});
					}
					
					Button swprofileBtn = (Button) mhomeInstance.findViewById(R.id.swithprofileBtn);
					if(swprofileBtn != null){
						swprofileBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								LinearLayout  btmLay = (LinearLayout) mhomeInstance.findViewById(R.id.bottomlayout);
								if(btmLay != null){
									btmLay.setVisibility(View.GONE);
								}	
								loggedIn = true;
								
								if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
								HelpTip.close(mhomeInstance);
								Intent p = new Intent(Home.this, com.player.apps.Profile.class);
								p.putExtra("ActivityName", "Home");
								startActivity(p);
								finish();
							}
						});
					}
				}
			});
		}
	}
	
/************************************************************************************************/
	
	public final BroadcastReceiver mHomeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String jsondata = "";
			String method = "";
			JSONObject objData = null;
			if(extras != null){
				if(extras != null){
					if(extras.containsKey("Params")){
						jsondata = extras.getString("Params");
					}if(extras.containsKey("Handler")){
						method = extras.getString("Handler");
					}
					try {
						objData = new JSONObject(jsondata);
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
					}
					if(Constant.DEBUG)  Log.d(TAG , "jsondata  "+jsondata+", handler  "+method);
					processUIData(method, objData);
				}
			}
		}
	};
	
	
	
	private void processUIData(String handler,final JSONObject jsonData){
		try{
		if(super.progressDialog.isShowing()){
			super.progressDialog.dismiss();
		}
		if(Constant.DEBUG)  Log.d(TAG  , "Process Action >>>>>. "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Guide.sendFeaturedList")){
				if(mhomeInstance != null){
					//HelpTip
					HelpTip.requestForHelp(mhomeInstance.getResources().getString(R.string.GETTING_STARTED),
							mhomeInstance.getResources().getString(R.string.HOME_MSG3),mhomeInstance);
					
					if(jsonData != null){
						try {
							if(jsonData.has("result")){
								if(jsonData.getString("result").equalsIgnoreCase("success")){
									boolean isCached = false;
									if(jsonData.length()>0){
										featuredData = jsonData;
										showFeatured(isCached);
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
			}if(handler.equalsIgnoreCase("com.port.apps.epg.Attributes.getTVHomeStatus")){
				if(mhomeInstance != null){
					if(jsonData != null){
						try {
							if(jsonData.has("status")){
								if(jsonData.getString("status").equalsIgnoreCase("visible")){
									status = "visible";											
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
					if(mhomeInstance != null){
						mhomeInstance.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Info.rInfoAction(jsonData, mContainer, mhomeInstance,"com.player.apps.Home");
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
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG, keyCode + " Key Released " + event.getAction() + "  "+ event.getKeyCode());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true"));
			if(DataStorage.getCurrentScreen() != null&& DataStorage.getCurrentScreen().equalsIgnoreCase(ScreenStyles.REMOTE_INFO_SCREEN)) {
				HelpTip.close(mhomeInstance);
				mContainer.removeAllViews();
				showFeatured(true);
				return true;
			}else{
				if(Constant.Mobile){
					Intent lukup = new Intent(Home.this, com.player.apps.AppGuide.class);
					lukup.putExtra("ActivityName", "Home");
					startActivity(lukup);
					finish();
					return true;					
				}
			}
		}if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				HelpTip.close(mhomeInstance);
				Intent lukup = new Intent(Home.this, com.player.apps.AppGuide.class);
				lukup.putExtra("ActivityName", "Home");
				startActivity(lukup);
				finish();
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}

    class FeaturedAdapter extends BaseAdapter {
	   	 
        Random randomGenerator = new Random();
        ArrayList<HashMap<String, String>> DataList;
        
    	public FeaturedAdapter(ArrayList<HashMap<String, String>> title){
        	DataList = title;
        }

    	@Override
    	public int getCount() {
    	      return DataList.size();
    	}
    	
    	@Override
    	public Object getItem(int position) {
    		return position;
    	}
    	
    	@Override
        public int getViewTypeCount(){ 
            return 2;
        }
    	
    	@Override
        public int getItemViewType(int position){
            if(position % 2 == 0)
                return 0;
            else
                return 1;
        }

    	class fullViewHolder{
	        TextView title;
	        TextView subTitle;
	    }
    	
    	class halfViewHolder{
            TextView mainTitle;
            TextView desc;
        }
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) throws InflateException  {
    		try{
    			halfViewHolder holder2 = null;
		        fullViewHolder holder1 = null;
    		    View v = convertView;
    		    if (getItemViewType(position) == 0) {
    	            if (v == null){
    	            	holder1 = new fullViewHolder();
    	                //GET View 1
    	            	LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			            v = inflater.inflate(R.layout.home_list1, parent, false);
    	                holder1.title = (TextView) v.findViewById(R.id.maintext);
    	                holder1.subTitle = (TextView) v.findViewById(R.id.subtext);
    	                v.setTag(holder1);
    	            }else{
    	    			holder1 = (fullViewHolder)v.getTag();
    	    		}
    	            HashMap<String, String> fields = new HashMap<String, String>();
    	    		fields = DataList.get(position);
    	            
    	    		if(fields.get(ScreenStyles.LIST_KEY_TITLE) != null){
    	    			holder1.title.setText(fields.get(ScreenStyles.LIST_KEY_TITLE));
    	    		}
    	            
    	    		if(fields.get("desc") != null){
    	    			holder1.subTitle.setText(fields.get("desc"));
    	    		}

    	            return v;
    	        }  else  {
    	            if (v == null) {
    	            	holder2 = new halfViewHolder();
    	                //GET View 2
    	            	LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			            v = inflater.inflate(R.layout.home_list2, parent, false);

    	                holder2.mainTitle = (TextView) v.findViewById(R.id.maintext1);
    	                holder2.desc = (TextView) v.findViewById(R.id.maintext2);
    	                v.setTag(holder2);
    	            } else{
    	    			holder2 = (halfViewHolder)v.getTag();
    	    		}
    	            HashMap<String, String> fields = new HashMap<String, String>();
    	    		fields = DataList.get(position);
    	    		if(fields.get("desc") != null){
    	    			holder2.desc.setText(fields.get("desc"));
    	    		}
    	    		if(fields.get(ScreenStyles.LIST_KEY_TITLE) != null){
    	    			holder2.mainTitle.setText(fields.get(ScreenStyles.LIST_KEY_TITLE));
    	    		}
    	            return v;
    	        }
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    			StringWriter errors = new StringWriter();
    			e.printStackTrace(new PrintWriter(errors));
    			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				return null;
			}
    	}

    	@Override
    	public long getItemId(int position) {
    		return 0;
    	}
	}
    
    
    public Bitmap StringToBitMap(String image){
        try{
     	   BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6;
            
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length,options);
            
            return bitmap;
          }catch (Exception e) {
  			e.printStackTrace();
  			StringWriter errors = new StringWriter();
  			e.printStackTrace(new PrintWriter(errors));
  			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
           return null;
          }
 	 }
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		HashMap<String, String> map = CacheDockData.fragmentFeaturedList.get(position);
		if(map != null){
			if(Constant.DEBUG)  Log.d(TAG , "onItemClick(). Item: "+CacheDockData.fragmentFeaturedList.get(position).get(ScreenStyles.LIST_KEY_TITLE));
			Info.requestForInfoById(CacheDockData.fragmentFeaturedList.get(position).get("id"), "com.player.apps.Home");
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		int lastInScreen = firstVisibleItem + visibleItemCount;
		if((lastInScreen == totalItemCount) && !scrollFlag){   
			scrollFlag = true;	
			dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("start", index+"");
			list.put("limit", "10");
			list.put("consumer", "TV");
			list.put("network",mDataAccess.getConnectionType());
			list.put("caller", "com.player.apps.Home");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			String method = "com.port.apps.epg.Home.sendFeaturedList"; 
			new AsyncDispatch(method, dispatchHashMap,true).execute();
			index+=10;
		}
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

}
