package com.player.apps;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.action.Info;
import com.player.service.CacheDockData;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.widget.HelpText;
import com.player.widget.OverLay;
import com.player.widget.ScrollList;

public class Notification extends Layout implements OnItemClickListener{

	private Notification mNotificationInstance;
	private String TAG = "Notification";
	private NotificationAdapter scrollAdapter = null;
	private String action = "";
	private ListView lukupListView;
	private LinearLayout.LayoutParams standParams;
	private ArrayList<HashMap<String,String>> notificationList = new ArrayList<HashMap<String,String>>();
	private int maingrid_select_index = -1;
	private int maingrid_focus_index = -1;
	private View maingridview_focus = null;
	RelativeLayout currentlayoutMain;
	TextView currentTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNotificationInstance = this;
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG ,".CurrentScreen: "+DataStorage.getCurrentScreen());
		DataStorage.setCurrentScreen("Notification");
		
		requestForNotification();
	}
	
	private void requestForNotification(){
		if(Constant.DEBUG)  Log.d(TAG , "requestForNotification() ");
		try {
			mNotificationInstance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mContainer.removeAllViews();
					notificationList = CacheDockData.notificationList;
					
					if(notificationList.size()>0){
						LayoutInflater  inflater = mNotificationInstance.getLayoutInflater();
						View listView = inflater.inflate(R.layout.guidelist,null);
						mContainer.addView(listView,getLinearLayoutParams());
						lukupListView = (ListView) listView.findViewById(R.id.lukuplist);
						scrollAdapter = new NotificationAdapter(notificationList);
						lukupListView.setAdapter(scrollAdapter);
						refreshListView(lukupListView);		
						
						lukupListView.setOnItemClickListener(mNotificationInstance);
					}else{
						HelpText.showHelpTextDialog(mNotificationInstance, mNotificationInstance.getResources().getString(R.string.NO_CONTENT), 3000);
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
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		if (maingrid_select_index != position){      
			if(maingridview_focus != null && maingrid_select_index != maingrid_focus_index)
			{
				if(Constant.DEBUG)  Log.w(TAG, "OnItemClickListener().position is ::"+position);
				currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
				currentTextView.setTextColor(getResources().getColor(R.color.white));
			}
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
			currentTextView.setTextColor(getResources().getColor(R.color.pink));
			currentTextView.setSelected(true);
			currentTextView.setMarqueeRepeatLimit(-1);
			currentTextView.setEllipsize(TruncateAt.MARQUEE);
		}else if(maingridview_focus != null){
			if(Constant.DEBUG)  Log.w(TAG, "OnItemClickListener().position is ::"+position);
			currentlayoutMain.setBackgroundColor(getResources().getColor(R.color.pink));
			currentTextView.setSelected(false);
			currentTextView.setTextColor(getResources().getColor(R.color.white));
			maingridview_focus = v;
			currentlayoutMain = (RelativeLayout) v.findViewById(R.id.Layout);
			currentTextView = (TextView) v.findViewById(R.id.overlaylist_title);
		}
		maingrid_focus_index = position;
		
		if(Constant.DEBUG)  Log.d(TAG,"onItemClick().");
		HashMap<String, String> map= notificationList.get(position);
		if(map.containsKey("action")){
			action = map.get("action");
			if(!action.equalsIgnoreCase("")){
				Intent lukup;
				try {
					lukup = new Intent(Notification.this, Class.forName(action));
					startActivity(lukup);
					finish();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		CacheDockData.notificationList.remove(position);
		if(CacheDockData.notificationList.size()<=0){
			mContainer.removeAllViews();
			finish();
		}
	}
	
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}
	
	private void refreshListView(final ListView lukupListView) {
		try {
			if(mNotificationInstance != null){
				mNotificationInstance.runOnUiThread(new Runnable() {
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
			if (DataStorage.getCurrentScreen() != null && DataStorage.getCurrentScreen().equalsIgnoreCase("Notification")) {
				mContainer.removeAllViews();
				if(Constant.DEBUG)  Log.d(TAG ,"Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				finish();
				return true;
			}
			return false;
		}
		
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (Constant.DEBUG)	Log.d(TAG, event.getKeyCode() + " KEYCODE_SEARCH ");
				if (Constant.DEBUG) Log.d("KEY", "Lukup Button Pressed");
				if(Constant.DEBUG)  Log.d(TAG ,"Next Activity.CurrentScreen: "+DataStorage.getCurrentScreen());
				finish();
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
	class NotificationAdapter extends BaseAdapter {
   	 
        private ArrayList<HashMap<String, String>> DataList;
        
    	public NotificationAdapter(ArrayList<HashMap<String, String>> list){
        	DataList = list;
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
    	public long getItemId(int position) {
    		return 0;
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) throws InflateException  {
    		try{
				View v = null;
				if (convertView == null) {
		            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		            v = inflater.inflate(R.layout.notification_list, parent, false);
			    }else{
		        	v=convertView;
		        }
		        
				v.setBackgroundDrawable(mNotificationInstance.getResources().getDrawable(R.drawable.list_selector));
				
		        TextView title = (TextView)v.findViewById(R.id.overlaylist_title);
		        TextView desc = (TextView)v.findViewById(R.id.desc);
		        
		        HashMap<String, String> fields = new HashMap<String, String>();
				fields = DataList.get(position);
				
				if(fields.containsKey(ScreenStyles.LIST_KEY_TITLE)){
					String listTitle = fields.get(ScreenStyles.LIST_KEY_TITLE);
					if(listTitle != null && !listTitle.equalsIgnoreCase("")){
						title.setText(listTitle);
						if(listTitle != null){
							if(listTitle.length() > 40){
								String subTitle = listTitle.substring(0, 40);
								subTitle = subTitle+"..." ;
								title.setSingleLine(true);
								title.setText(subTitle);
							}else{
								title.setSingleLine(true);
								title.setText(listTitle);
							}
						}
					}
				}
				
				String Desc = "";
				if(fields.containsKey("desc")){
					Desc = fields.get("desc");
				}
				if(Desc != null && !Desc.equalsIgnoreCase("")){
					desc.setText(Desc);
					desc.setVisibility(View.VISIBLE);
				}
				return v;
    		}catch (Exception ex) {
				ex.printStackTrace();
			}
			return null; 
    	}
    }
	
}
