/*
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/
package com.player.action;

import java.util.ArrayList;
import java.util.HashMap;

import com.player.Layout;


/**
 * @author jeetendra
 *
 */
public class Playlist {
	
	public static void requestForAddToPlaylist(String id,String caller){
		ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("id", id);
		list.put("state", "add");
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Attributes.PlayList"; 
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}
	
}
