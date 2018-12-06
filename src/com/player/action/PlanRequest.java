package com.player.action;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.player.Layout;
import com.player.util.Constant;
import com.player.util.DataStorage;

public class PlanRequest {
	
	public static void recordEvent(String selectedItem_id,String caller){
		if(Constant.DEBUG)  Log.d("PlanRequest", "selectedItem_id: "+selectedItem_id+", caller: "+caller);
		ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("id", selectedItem_id);
		if(DataStorage.getCurrentUserId().equalsIgnoreCase("1000")){ 
			list.put("userid", "1000"); 
		}else{ 
			list.put("userid", DataStorage.getCurrentUserId()); 
		}
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Plan.setRecord"; 
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}
	
	public static void StopRecording(String selectedItem_id,String caller){
		if(Constant.DEBUG)  Log.d("PlanRequest", "selectedItem_id: "+selectedItem_id+", caller: "+caller);
		ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("id", selectedItem_id);
		if(DataStorage.getCurrentUserId().equalsIgnoreCase("1000")){ 
			list.put("userid", "1000"); 
		}else{ 
			list.put("userid", DataStorage.getCurrentUserId()); 
		}
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Plan.cancelRecord"; 
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}
	
	public static void reminderEvent(String selectedItem_id,String caller){
		
		ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("id", selectedItem_id);
		if(DataStorage.getCurrentUserId().equalsIgnoreCase("1000")){ 
			list.put("userid", "1000"); 
		}else{ 
			list.put("userid", DataStorage.getCurrentUserId()); 
		}
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Plan.setReminder"; 
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}

}
